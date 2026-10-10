package org.spotube.millennium.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.Thumb
import com.composeunstyled.UnstyledHorizontalScrollbar
import com.composeunstyled.UnstyledVerticalScrollbar
import com.composeunstyled.rememberScrollbarState
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumScrollbarThemeProperties(
    val colors: MillenniumColors,
    val trackBackground: Brush,
    val thumbBackground: Brush,
    val shape: Shape,
    val thickness: Dp,
    val borderColor: Color,
    val borderWidth: Dp,
    val alpha: Float,
    val stateAnimationMillis: Int,
)

@Immutable
data class MillenniumScrollbarTheme(
    val states: MillenniumControlStateProperty<MillenniumScrollbarThemeProperties>,
)

fun defaultMillenniumScrollbarTheme(colors: MillenniumColors): MillenniumScrollbarTheme {
    val base = MillenniumScrollbarThemeProperties(
        colors = colors,
        trackBackground = millenniumBevelBrush(colors, sunken = true),
        thumbBackground = Brush.verticalGradient(listOf(colors.border, colors.muted)),
        shape = RoundedCornerShape(50),
        thickness = 10.dp,
        borderColor = colors.border,
        borderWidth = 1.dp,
        alpha = 0.74f,
        stateAnimationMillis = 120,
    )
    return MillenniumScrollbarTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(thumbBackground = Brush.verticalGradient(listOf(colors.accent, colors.accentBottom)), alpha = 0.95f),
            focused = base.copy(thumbBackground = Brush.verticalGradient(listOf(colors.accent, colors.accentBottom)), alpha = 0.95f),
            pressed = base.copy(thumbBackground = millenniumBevelBrush(colors, accent = true), alpha = 1f),
            disabled = base.copy(alpha = 0.35f),
            selected = base.copy(thumbBackground = millenniumBevelBrush(colors, accent = true)),
            error = base.copy(thumbBackground = Brush.verticalGradient(listOf(colors.danger, colors.danger)), borderColor = colors.danger),
        ),
    )
}

@Composable
fun MillenniumScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    horizontal: Boolean = false,
) {
    val state = rememberScrollbarState(scrollState)
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val dragged by interaction.collectIsDraggedAsState()
    val visualState = when {
        dragged -> MillenniumControlState.Pressed
        hovered -> MillenniumControlState.Hovered
        else -> MillenniumControlState.Default
    }
    val properties = MillenniumTheme.scrollbarTheme.states.resolve(visualState)
    val animatedAlpha = animateFloatAsState(
        targetValue = properties.alpha,
        animationSpec = tween(properties.stateAnimationMillis),
    ).value
    if (horizontal) {
        UnstyledHorizontalScrollbar(
            state,
            modifier.height(properties.thickness).millenniumSurface(
                background = properties.trackBackground,
                shape = properties.shape,
                borderColor = properties.borderColor,
                borderWidth = properties.borderWidth,
            ),
            interactionSource = interaction,
        ) {
            Thumb(
                Modifier.fillMaxHeight().millenniumSurface(
                    background = properties.thumbBackground,
                    shape = properties.shape,
                ).alpha(animatedAlpha),
            )
        }
    } else {
        UnstyledVerticalScrollbar(
            state,
            modifier.width(properties.thickness).millenniumSurface(
                background = properties.trackBackground,
                shape = properties.shape,
                borderColor = properties.borderColor,
                borderWidth = properties.borderWidth,
            ),
            interactionSource = interaction,
        ) {
            Thumb(
                Modifier.fillMaxWidth().millenniumSurface(
                    background = properties.thumbBackground,
                    shape = properties.shape,
                ).alpha(animatedAlpha),
            )
        }
    }
}

@Preview
@Composable
private fun MillenniumScrollbarPreview() = MillenniumPreview {
    val scrollState = rememberScrollState()
    Box(Modifier.height(128.dp).verticalScroll(scrollState).padding(end = 16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(6) { index -> MillenniumText("A little nostalgia · ${index + 1}") }
        }
    }
    MillenniumScrollbar(scrollState)
}
