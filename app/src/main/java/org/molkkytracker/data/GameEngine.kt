package org.molkkytracker.data

/**
 * Résultat de l'application d'un lancer à une équipe.
 */
sealed class ThrowOutcome {
    data class Scored(val newScore: Int) : ThrowOutcome()
    data class Overshoot(val cappedScore: Int) : ThrowOutcome() // dépassement de 50 -> 25
    data class MissRecorded(val consecutiveMisses: Int) : ThrowOutcome()
    object TeamEliminated : ThrowOutcome()
    object TurnWillBeSkipped : ThrowOutcome()
    data class TeamWon(val finalScore: Int) : ThrowOutcome()
}

const val WINNING_SCORE = 50
const val OVERSHOOT_RESET_SCORE = 25
const val MISSES_BEFORE_PENALTY = 3

/**
 * Applique un lancer manqué (0 point) à une équipe.
 * Retourne une nouvelle instance de Team avec les valeurs mises à jour.
 */
fun applyMiss(team: Team, missRule: MissRule): Pair<Team, ThrowOutcome> {
    val newMissCount = team.consecutiveMisses + 1

    if (newMissCount < MISSES_BEFORE_PENALTY) {
        return team.copy(consecutiveMisses = newMissCount) to ThrowOutcome.MissRecorded(newMissCount)
    }

    // 3e échec consécutif
    return when (missRule) {
        MissRule.ELIMINATION -> {
            team.copy(consecutiveMisses = 0, eliminated = true) to ThrowOutcome.TeamEliminated
        }
        MissRule.SKIP_TURN -> {
            team.copy(consecutiveMisses = 0) to ThrowOutcome.TurnWillBeSkipped
        }
    }
}

/**
 * Applique un lancer réussi (points = 1..12) à une équipe.
 * Retourne une nouvelle instance de Team avec les valeurs mises à jour.
 */
fun applyScore(team: Team, points: Int): Pair<Team, ThrowOutcome> {
    require(points in 1..12) { "points doit être entre 1 et 12" }
    
    val candidate = team.score + points
    return when {
        candidate == WINNING_SCORE -> {
            team.copy(score = WINNING_SCORE, consecutiveMisses = 0) to ThrowOutcome.TeamWon(WINNING_SCORE)
        }
        candidate > WINNING_SCORE -> {
            team.copy(score = OVERSHOOT_RESET_SCORE, consecutiveMisses = 0) to ThrowOutcome.Overshoot(OVERSHOOT_RESET_SCORE)
        }
        else -> {
            team.copy(score = candidate, consecutiveMisses = 0) to ThrowOutcome.Scored(candidate)
        }
    }
}
