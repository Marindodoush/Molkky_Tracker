package org.molkkytracker.data

/**
 * Les 5 combinaisons couleur + motif disponibles pour distinguer les
 * équipes. Le motif permet de distinguer les équipes même pour les
 * joueurs daltoniens (pas seulement la couleur).
 */
enum class TeamStyle(val colorHex: Long, val pattern: Pattern) {
    RED(0xFFFF4D4D, Pattern.DIAMOND),
    CYAN(0xFF26E3FF, Pattern.HEART),
    LIME(0xFF96F266, Pattern.MOON),
    YELLOW(0xFFFFC922, Pattern.STAR),
    MAUVE(0xFFC89CFF, Pattern.DROP),
    BLUE(0xFF26A6FF, Pattern.CIRCLE),
    ORANGE(0xFFFF9500, Pattern.SQUARE),
    WHITE(0xFFFFFFFF, Pattern.TRIANGLE);

    enum class Pattern { DIAMOND, HEART, MOON, STAR, DROP, CIRCLE, SQUARE, TRIANGLE }
}
