package org.molkkytracker.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.molkkytracker.data.*

class GameViewModel : ViewModel() {
    var teams by mutableStateOf<List<Team>>(emptyList())
        private set

    var turnOrder by mutableStateOf<List<Pair<Int, Int>>>(emptyList()) // Pair(teamIndex, playerIndexInTeam)
        private set

    var currentTurnIndex by mutableStateOf(0)
        private set

    var isGameOver by mutableStateOf(false)
        private set

    data class PlayerHistoryDetail(
        val name: String,
        val currentGameScores: List<Int>,
        val sessionHistory: List<PlayerSessionEntry>
    )

    var selectedPlayerHistoryDetail by mutableStateOf<PlayerHistoryDetail?>(null)

    var missRule by mutableStateOf(MissRule.SKIP_TURN)

    fun start(teams: List<Team>) {
        this.teams = teams
        generateTurnOrder()
    }

    private fun generateTurnOrder() {
        val maxPlayers = teams.maxOfOrNull { it.playerNames.size } ?: 0
        val order = mutableListOf<Pair<Int, Int>>()
        for (pIdx in 0 until maxPlayers) {
            for (tIdx in teams.indices) {
                if (pIdx < teams[tIdx].playerNames.size) {
                    order.add(tIdx to pIdx)
                }
            }
        }
        turnOrder = order
    }

    fun applyPoints(points: Int) {
        if (isGameOver) return

        val (tIdx, pIdx) = turnOrder[currentTurnIndex]
        val team = teams[tIdx]
        val playerName = team.playerNames[pIdx]

        val (updatedTeam, outcome) = if (points == 0) {
            applyMiss(team, missRule)
        } else {
            applyScore(team, points)
        }

        // Mise à jour des historiques sur la nouvelle instance de Team
        val finalTeam = updatedTeam.copy(
            lastScores = updatedTeam.lastScores.toMutableMap().apply { put(playerName, points) },
            scoreHistory = updatedTeam.scoreHistory.toMutableMap().apply { 
                put(playerName, (get(playerName) ?: emptyList()) + points)
            }
        )

        val newTeams = teams.toMutableList()
        newTeams[tIdx] = finalTeam
        teams = newTeams

        if (outcome is ThrowOutcome.TeamWon) {
            isGameOver = true
            SessionHistoryManager.recordGame(teams)
        }

        if (outcome is ThrowOutcome.TurnWillBeSkipped) {
            // Le joueur suivant de CETTE équipe saute son tour au prochain cycle
            // Mais la règle dit : "Au 3ème coups manqué par équipe, le joueur suivant de l'équipe passera son tour"
            // Ça veut dire qu'on doit trouver la PROCHAINE occurrence de cette équipe dans turnOrder et la sauter.
            markNextTurnAsSkipped(tIdx)
        }

        moveToNextTurn()
    }

    private var skippedTurns = mutableSetOf<Int>()

    private fun markNextTurnAsSkipped(teamIndex: Int) {
        var found = false
        var searchIdx = (currentTurnIndex + 1) % turnOrder.size
        // On cherche le prochain tour de cette équipe
        for (i in 0 until turnOrder.size) {
            val idx = (currentTurnIndex + 1 + i) % turnOrder.size
            if (turnOrder[idx].first == teamIndex) {
                skippedTurns.add(idx)
                found = true
                break
            }
        }
    }

    private fun moveToNextTurn() {
        do {
            currentTurnIndex = (currentTurnIndex + 1) % turnOrder.size
        } while (skippedTurns.contains(currentTurnIndex).also { if (it) skippedTurns.remove(currentTurnIndex) })
    }

    fun restartSameTeams() {
        teams = teams.map { it.reset() }.shuffled()
        currentTurnIndex = 0
        skippedTurns.clear()
        isGameOver = false
        generateTurnOrder()
    }

    fun restartNewTeams(teamCount: Int) {
        val allPlayers = teams.flatMap { team ->
            team.playerNames.map { name -> PlayerSlot(name, null) }
        }.shuffled()

        val groups = List(teamCount) { mutableListOf<PlayerSlot>() }
        allPlayers.forEachIndexed { i, slot -> groups[i % teamCount].add(slot) }

        val assignedStyles = TeamStyle.entries.shuffled().take(teamCount)

        teams = groups.mapIndexed { i, members ->
            Team(
                id = i,
                style = assignedStyles[i],
                playerNames = members.map { it.name }
            )
        }.shuffled()

        currentTurnIndex = 0
        skippedTurns.clear()
        isGameOver = false
        generateTurnOrder()
    }

    fun showHistory(playerName: String, team: Team) {
        val currentHistory = team.scoreHistory[playerName] ?: emptyList()
        val sessionHistory = SessionHistoryManager.getPlayerHistory(playerName)
        selectedPlayerHistoryDetail = PlayerHistoryDetail(playerName, currentHistory, sessionHistory)
    }

    fun getCurrentPlayerName(): String {
        if (turnOrder.isEmpty()) return ""
        val (tIdx, pIdx) = turnOrder[currentTurnIndex]
        return teams[tIdx].playerNames[pIdx]
    }

    fun getCurrentTeam(): Team? {
        if (turnOrder.isEmpty()) return null
        return teams[turnOrder[currentTurnIndex].first]
    }
}
