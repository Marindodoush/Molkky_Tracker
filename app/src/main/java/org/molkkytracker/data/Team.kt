package org.molkkytracker.data

/** Un membre saisi dans le formulaire de configuration (pas encore une partie). */
data class PlayerSlot(
    val name: String = "",
    val style: TeamStyle? = null
)

/**
 * Une équipe engagée dans la partie en cours. `score` retombe à 25
 * si un lancer fait dépasser 50. `consecutiveMisses` est remis à
 * zéro dès qu'un lancer de l'équipe touche une quille, ou après
 * application de la règle SKIP_TURN.
 */
data class Team(
    val id: Int,
    val style: TeamStyle,
    val playerNames: List<String>,
    val score: Int = 0,
    val consecutiveMisses: Int = 0,
    val eliminated: Boolean = false,
    val nextPlayerIndex: Int = 0,
    val lastScores: Map<String, Int> = emptyMap(),
    val scoreHistory: Map<String, List<Int>> = emptyMap()
) {
    val name: String get() = playerNames.joinToString(" & ")

    fun reset(): Team = copy(
        score = 0,
        consecutiveMisses = 0,
        eliminated = false,
        lastScores = emptyMap(),
        scoreHistory = emptyMap()
    )
}
