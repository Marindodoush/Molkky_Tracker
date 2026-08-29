package org.molkkytracker.data

/**
 * Comportement appliqué à une équipe après 3 coups manqués consécutifs
 * (le compteur est propre à chaque équipe et se remet à zéro dès
 * qu'un lancer de l'équipe touche une quille).
 */
enum class MissRule {
    /** L'équipe est éliminée de la partie. */
    ELIMINATION,

    /** Le joueur suivant de l'équipe passe son tour, puis le compteur repart à zéro. */
    SKIP_TURN
}
