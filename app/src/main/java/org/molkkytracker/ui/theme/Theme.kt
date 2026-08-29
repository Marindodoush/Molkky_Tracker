package org.molkkytracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8DB600),
    secondary = Color(0xFF3B6D11),
    tertiary = Color(0xFF26E3FF)
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF3B6D11),
    secondary = Color(0xFF8DB600),
    tertiary = Color(0xFF26E3FF)
)

@Composable
fun MolkkyTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MolkkyTypography,
        content = content
    )
}
