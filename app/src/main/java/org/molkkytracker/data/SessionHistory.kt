package org.molkkytracker.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Représente une entrée dans l'historique de session pour un joueur.
 */
data class PlayerSessionEntry(
    val gameIndex: Int,
    val style: TeamStyle,
    val scores: List<Int>
)

/**
 * Gère l'historique de la session en cours (effacé à la fermeture de l'app).
 */
object SessionHistoryManager {
    private var gameCounter = 0
    
    // Map: Nom du joueur -> Liste de ses parties dans la session
    private val _history = mutableMapOf<String, MutableList<PlayerSessionEntry>>()
    
    var historyState by mutableStateOf(emptyMap<String, List<PlayerSessionEntry>>())
        private set

    fun recordGame(teams: List<Team>) {
        gameCounter++
        teams.forEach { team ->
            team.playerNames.forEach { name ->
                val scores = team.scoreHistory[name] ?: emptyList()
                val entry = PlayerSessionEntry(gameCounter, team.style, scores.toList())
                _history.getOrPut(name) { mutableListOf() }.add(entry)
            }
        }
        historyState = _history.toMap()
    }

    fun getPlayerHistory(name: String): List<PlayerSessionEntry> {
        return _history[name] ?: emptyList()
    }
}
