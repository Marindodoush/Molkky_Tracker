package org.molkkytracker.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.molkkytracker.data.TeamStyle

@Composable
fun TeamPattern(pattern: TeamStyle.Pattern, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val size = size.minDimension
        val strokeWidth = 2.dp.toPx()
        when (pattern) {
            TeamStyle.Pattern.DIAMOND -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size / 2, 0f)
                    lineTo(size * 0.9f, size / 2)
                    lineTo(size / 2, size)
                    lineTo(size * 0.1f, size / 2)
                    close()
                }
                drawPath(path, color, style = Stroke(width = strokeWidth))
            }
            TeamStyle.Pattern.HEART -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size / 2, size * 0.3f)
                    cubicTo(size * 0.2f, 0f, 0f, size * 0.25f, 0f, size * 0.55f)
                    cubicTo(0f, size * 0.85f, size / 2, size, size / 2, size)
                    cubicTo(size / 2, size, size, size * 0.85f, size, size * 0.55f)
                    cubicTo(size, size * 0.25f, size * 0.8f, 0f, size / 2, size * 0.3f)
                }
                drawPath(path, color, style = Stroke(width = strokeWidth))
            }
            TeamStyle.Pattern.MOON -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size * 0.75f, size * 0.1f)
                    cubicTo(size * 0.25f, size * 0.1f, size * 0.2f, size * 0.5f, size * 0.2f, size * 0.5f)
                    cubicTo(size * 0.2f, size * 0.5f, size * 0.25f, size * 0.9f, size * 0.75f, size * 0.9f)
                    cubicTo(size * 0.5f, size * 0.75f, size * 0.45f, size * 0.5f, size * 0.45f, size * 0.5f)
                    cubicTo(size * 0.45f, size * 0.5f, size * 0.5f, size * 0.25f, size * 0.75f, size * 0.1f)
                    close()
                }
                drawPath(path, color, style = Stroke(width = strokeWidth))
            }
            TeamStyle.Pattern.STAR -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    val mid = size / 2
                    moveTo(mid, 0f)
                    lineTo(size * 0.65f, size * 0.35f)
                    lineTo(size, size * 0.35f)
                    lineTo(size * 0.75f, size * 0.6f)
                    lineTo(size * 0.85f, size)
                    lineTo(mid, size * 0.8f)
                    lineTo(size * 0.15f, size)
                    lineTo(size * 0.25f, size * 0.6f)
                    lineTo(0f, size * 0.35f)
                    lineTo(size * 0.35f, size * 0.35f)
                    close()
                }
                drawPath(path, color, style = Stroke(width = strokeWidth))
            }
            TeamStyle.Pattern.DROP -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size / 2, 0f)
                    cubicTo(size * 0.9f, size * 0.5f, size * 0.9f, size, size / 2, size)
                    cubicTo(size * 0.1f, size, size * 0.1f, size * 0.5f, size / 2, 0f)
                }
                drawPath(path, color, style = Stroke(width = strokeWidth))
            }
            TeamStyle.Pattern.SQUARE -> {
                drawRect(color, style = Stroke(width = strokeWidth))
            }
            TeamStyle.Pattern.CIRCLE -> {
                drawCircle(color, style = Stroke(width = strokeWidth))
            }
            TeamStyle.Pattern.TRIANGLE -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size / 2, 0f)
                    lineTo(size, size)
                    lineTo(0f, size)
                    close()
                }
                drawPath(path, color, style = Stroke(width = strokeWidth))
            }
        }
    }
}
