package org.spotube.millennium.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.Indicator
import com.composeunstyled.UnstyledProgress
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MillenniumProgress(progress: Float, modifier: Modifier = Modifier) {
    val progressTheme = MillenniumTheme.progressTheme
    val colors = MillenniumTheme.colorsFor(progressTheme)
    val visual = progressTheme.resolve(MillenniumVisualState.Default,
        MillenniumVisualStyle(backgroundColor = colors.inset,
            borderColor = colors.border, borderWidth = progressTheme.strokes.border, innerGlowColor = colors.highlight))
    UnstyledProgress(
        progress.coerceIn(0f, 1f),
        modifier.fillMaxWidth().height(progressTheme.dimensions.progressHeight)
            .beveled(colors, progressTheme.shapes.pill, sunken = true).componentStyle(visual, progressTheme.shapes.pill)
    ) {
        Indicator(
            Modifier.fillMaxHeight().background(
                visual.background ?: visual.foregroundColor?.let { androidx.compose.ui.graphics.Brush.verticalGradient(listOf(it, it)) }
                    ?: gelBrush(colors.accentTop, colors.accentBottom), CircleShape
            )
        )
    }
}

@Composable
fun MillenniumIndeterminateProgress(modifier: Modifier = Modifier) {
    val progressTheme = MillenniumTheme.progressTheme
    val colors = MillenniumTheme.colorsFor(progressTheme)
    val visual = progressTheme.resolve(MillenniumVisualState.Default,
        MillenniumVisualStyle(backgroundColor = colors.inset,
            borderColor = colors.border, borderWidth = progressTheme.strokes.border, innerGlowColor = colors.highlight))
    val transition = rememberInfiniteTransition()
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(progressTheme.motion.progressMillis), RepeatMode.Reverse),
    )
    UnstyledProgress(
        modifier = modifier.fillMaxWidth().height(progressTheme.dimensions.progressHeight)
            .beveled(colors, progressTheme.shapes.pill, sunken = true).componentStyle(visual, progressTheme.shapes.pill)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = BiasAlignment(offset * 2f - 1f, 0f)) {
            Box(
                Modifier.fillMaxWidth(progressTheme.motion.indeterminateProgressFraction.coerceIn(0f, 1f)).fillMaxHeight().background(
                    visual.background ?: visual.foregroundColor?.let { androidx.compose.ui.graphics.Brush.verticalGradient(listOf(it, it)) }
                        ?: gelBrush(colors.accentTop, colors.accentBottom), CircleShape
                )
            )
        }
    }
}

@Preview
@Composable
private fun MillenniumProgressPreview() = MillenniumPreview {
    MillenniumProgress(0.68f)
    MillenniumIndeterminateProgress()
}
