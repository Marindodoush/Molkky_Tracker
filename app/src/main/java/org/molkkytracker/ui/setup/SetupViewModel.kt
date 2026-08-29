package org.molkkytracker.ui.setup

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.molkkytracker.data.*

const val MAX_PLAYERS = 8

class SetupViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MolkkyDatabase.get(application)
    private val playerDao = db.playerDao()

    val rememberedNames = playerDao.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var slots by mutableStateOf(List(MAX_PLAYERS) { PlayerSlot() })
        private set

    var teamCount by mutableStateOf(2)
        private set

    fun updateName(index: Int, name: String) {
        slots = slots.toMutableList().also { it[index] = it[index].copy(name = name) }
    }

    fun savePlayer(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            playerDao.insert(Player(name = name))
        }
    }

    fun deletePlayer(player: Player) {
        viewModelScope.launch {
            playerDao.delete(player.id)
        }
    }

    /** Cycle vers le style suivant (ou retire le style si on dépasse la dernière option). */
    fun cycleStyle(index: Int) {
        val current = slots[index].style
        val next = when (current) {
            null -> TeamStyle.entries.first()
            TeamStyle.entries.last() -> null
            else -> TeamStyle.entries[TeamStyle.entries.indexOf(current) + 1]
        }
        slots = slots.toMutableList().also { it[index] = it[index].copy(style = next) }
    }

    fun updateTeamCount(count: Int) {
        teamCount = count
    }

    private val activeSlots get() = slots.filter { it.name.isNotBlank() }

    /**
     * Un slot actif (avec un alias) est "manquant" tant qu'aucune couleur
     * ne lui a été attribuée ET que l'auto-génération n'a pas encore eu lieu.
     * Sert à afficher le contour rouge / clignotant.
     */
    fun isSlotIncomplete(index: Int): Boolean {
        val slot = slots[index]
        return slot.name.isNotBlank() && slot.style == null && !canAutoGenerate()
    }

    /** Auto-génération autorisée seulement si AUCUNE couleur n'a été saisie à la main. */
    private fun canAutoGenerate(): Boolean = activeSlots.all { it.style == null }

    /**
     * Construit les équipes. Retourne null tant que la configuration n'est
     * pas exploitable (pas assez de joueurs, ou couleurs partiellement
     * renseignées sans que tout ne le soit).
     */
    fun buildTeamsOrNull(): List<Team>? {
        val active = activeSlots
        if (active.isEmpty()) return null

        val effectiveTeamCount = if (teamCount == 0) active.size else teamCount
        if (active.size < effectiveTeamCount) return null

        val groups = List(effectiveTeamCount) { mutableListOf<PlayerSlot>() }

        if (canAutoGenerate()) {
            // Répartition aléatoire mais on garde l'ordre de saisie au sein des équipes
            val shuffledIndices = active.indices.shuffled()
            shuffledIndices.forEachIndexed { i, originalIdx ->
                groups[i % effectiveTeamCount].add(active[originalIdx])
            }
            // Retrier chaque groupe par son index d'origine dans 'active'
            groups.forEach { group ->
                group.sortBy { slot -> active.indexOf(slot) }
            }
        } else {
            if (active.any { it.style == null }) return null // saisie manuelle incomplète
            // Regrouper par style
            val styleToGroup = mutableMapOf<TeamStyle, MutableList<PlayerSlot>>()
            active.forEach { slot ->
                styleToGroup.getOrPut(slot.style!!) { mutableListOf() }.add(slot)
            }
            // Vérifier qu'on a bien le bon nombre d'équipes
            if (styleToGroup.size != effectiveTeamCount) return null
            
            styleToGroup.values.forEachIndexed { i, members ->
                groups[i].addAll(members)
            }
        }

        val assignedStyles = if (canAutoGenerate()) {
            TeamStyle.entries.shuffled().take(effectiveTeamCount)
        } else {
            // Les styles sont déjà dans les groupes
            groups.map { it.first().style!! }
        }

        return groups.mapIndexed { i, members ->
            Team(
                id = i,
                style = assignedStyles[i],
                playerNames = members.map { it.name }
            )
        }.shuffled()
    }
}
