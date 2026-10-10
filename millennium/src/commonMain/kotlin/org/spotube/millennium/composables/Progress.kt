package org.spotube.millennium.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.Indicator
import com.composeunstyled.UnstyledProgress
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumProgressThemeProperties(
    val trackBackground: Brush? = null,
    val progressBackground: Brush? = null,
    val shape: Shape,
    val height: Dp,
    val borderColor: Color? = null,
    val borderWidth: Dp,
    val innerGlowColor: Color? = null,
    val determinateAnimationMillis: Int,
    val indeterminateAnimationMillis: Int,
    val indeterminateFraction: Float,
    val alpha: Float,
)

@Immutable
data class MillenniumProgressTheme(
    val states: MillenniumControlStateProperty<MillenniumProgressThemeProperties>,
)

fun defaultMillenniumProgressTheme(): MillenniumProgressTheme {
    val base = MillenniumProgressThemeProperties(
        shape = RoundedCornerShape(50),
        height = 12.dp,
        borderWidth = 1.dp,
        determinateAnimationMillis = 180,
        indeterminateAnimationMillis = 1400,
        indeterminateFraction = 0.30f,
        alpha = 1f,
    )
    return MillenniumProgressTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(borderWidth = 2.dp),
            pressed = base,
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(borderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumProgress(progress: Float, modifier: Modifier = Modifier) {
    val properties = MillenniumTheme.progressTheme.states.resolve(MillenniumControlState.Default)
    val colors = MillenniumTheme.colors
    val trackBackground = properties.trackBackground ?: millenniumBevelBrush(colors, sunken = true)
    val progressBackground = properties.progressBackground ?: gelBrush(colors.accentTop, colors.accentBottom)
    val borderColor = properties.borderColor ?: colors.border
    val glowColor = properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.45f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(properties.determinateAnimationMillis),
    )
    UnstyledProgress(
        animatedProgress,
        modifier.fillMaxWidth().height(properties.height).millenniumSurface(
            background = trackBackground,
            shape = properties.shape,
            borderColor = borderColor,
            borderWidth = properties.borderWidth,
            innerGlowColor = glowColor,
        ).alpha(properties.alpha),
    ) {
        Indicator(
            Modifier.fillMaxHeight().background(progressBackground, properties.shape),
        )
    }
}

@Composable
fun MillenniumIndeterminateProgress(modifier: Modifier = Modifier) {
    val properties = MillenniumTheme.progressTheme.states.resolve(MillenniumControlState.Default)
    val colors = MillenniumTheme.colors
    val trackBackground = properties.trackBackground ?: millenniumBevelBrush(colors, sunken = true)
    val progressBackground = properties.progressBackground ?: gelBrush(colors.accentTop, colors.accentBottom)
    val borderColor = properties.borderColor ?: colors.border
    val glowColor = properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.45f)
    val transition = rememberInfiniteTransition()
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(properties.indeterminateAnimationMillis),
            RepeatMode.Reverse,
        ),
    )
    UnstyledProgress(
        modifier.fillMaxWidth().height(properties.height).millenniumSurface(
            background = trackBackground,
            shape = properties.shape,
            borderColor = borderColor,
            borderWidth = properties.borderWidth,
            innerGlowColor = glowColor,
        ).alpha(properties.alpha),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = BiasAlignment(offset * 2f - 1f, 0f)) {
            Box(
                Modifier.fillMaxWidth(properties.indeterminateFraction.coerceIn(0f, 1f))
                    .fillMaxHeight().background(progressBackground, properties.shape),
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
