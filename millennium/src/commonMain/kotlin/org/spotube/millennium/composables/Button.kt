package org.spotube.millennium.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.composeunstyled.Text
import com.composeunstyled.UnstyledButton
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

enum class MillenniumButtonStyle { Primary, Secondary, Quiet, Danger }

/** Backwards-friendly name for callers that think of button styles as variants. */
typealias MillenniumButtonVariant = MillenniumButtonStyle

@Immutable
data class MillenniumButtonMotion(
    val stateChangeMillis: Int = 150,
    val hoverScale: Float = 1.015f,
    val pressedScale: Float = 0.975f,
)

/** Complete, state-specific button appearance and layout configuration. */
@Immutable
data class MillenniumButtonThemeProperties(
    val colors: MillenniumColors,
    val background: Brush,
    val foreground: Color,
    val textStyle: TextStyle,
    val shape: Shape,
    val padding: PaddingValues,
    val minSize: DpSize,
    val borderWidth: Dp,
    val borderColor: Color,
    val shadows: List<Color>,
    val shadowElevation: Dp,
    val innerGlowColor: Color,
    val innerGlowWidth: Dp = 1.dp,
    val innerGlowInset: Dp = 8.dp,
    val innerGlowTop: Dp = 1.dp,
    val alpha: Float = 1f,
    val motion: MillenniumButtonMotion = MillenniumButtonMotion(),
)

/** Four independently configurable variants, each with all control states. */
@Immutable
data class MillenniumButtonTheme(
    val primary: MillenniumControlStateProperty<MillenniumButtonThemeProperties>,
    val secondary: MillenniumControlStateProperty<MillenniumButtonThemeProperties>,
    val quiet: MillenniumControlStateProperty<MillenniumButtonThemeProperties>,
    val danger: MillenniumControlStateProperty<MillenniumButtonThemeProperties>,
)

fun defaultMillenniumButtonTheme(colors: MillenniumColors): MillenniumButtonTheme {
    fun variant(style: MillenniumButtonStyle): MillenniumControlStateProperty<MillenniumButtonThemeProperties> {
        val isPrimary = style == MillenniumButtonStyle.Primary
        val isQuiet = style == MillenniumButtonStyle.Quiet
        val isDanger = style == MillenniumButtonStyle.Danger
        val defaultBackground = when {
            isPrimary -> gelBrush(colors.accentTop, colors.accentBottom)
            isDanger -> Brush.verticalGradient(listOf(colors.danger, colors.danger))
            isQuiet -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
            else -> millenniumBevelBrush(colors)
        }
        val foreground = when {
            isPrimary -> colors.onAccent
            isDanger -> colors.surface
            else -> colors.text
        }
        val base = MillenniumButtonThemeProperties(
            colors = colors,
            background = defaultBackground,
            foreground = foreground,
            textStyle = TextStyle(
                color = foreground,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            shape = RoundedCornerShape(10.dp),
            padding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
            minSize = DpSize(48.dp, 44.dp),
            borderWidth = if (isQuiet) 0.dp else 1.dp,
            borderColor = when {
                isDanger -> colors.danger
                isPrimary -> colors.accentBottom
                else -> colors.border
            },
            shadows = listOf(Color.Black.copy(alpha = 0.16f)),
            shadowElevation = if (isQuiet) 0.dp else 3.dp,
            innerGlowColor = colors.highlight.copy(alpha = 0.35f),
        )
        return MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(
                borderColor = if (isQuiet) colors.accent else base.borderColor,
                innerGlowColor = colors.highlight.copy(alpha = 0.60f),
            ),
            focused = base.copy(
                borderWidth = 2.dp,
                borderColor = colors.accent,
                innerGlowColor = colors.highlight.copy(alpha = 0.60f),
            ),
            pressed = base.copy(
                shadowElevation = 0.dp,
                innerGlowColor = colors.inset.copy(alpha = 0.32f),
            ),
            disabled = base.copy(alpha = 0.45f, shadowElevation = 0.dp),
            selected = base.copy(borderColor = colors.accent),
            error = base.copy(borderWidth = 2.dp, borderColor = colors.danger),
        )
    }

    return MillenniumButtonTheme(
        primary = variant(MillenniumButtonStyle.Primary),
        secondary = variant(MillenniumButtonStyle.Secondary),
        quiet = variant(MillenniumButtonStyle.Quiet),
        danger = variant(MillenniumButtonStyle.Danger),
    )
}

private fun MillenniumButtonTheme.forStyle(style: MillenniumButtonStyle) = when (style) {
    MillenniumButtonStyle.Primary -> primary
    MillenniumButtonStyle.Secondary -> secondary
    MillenniumButtonStyle.Quiet -> quiet
    MillenniumButtonStyle.Danger -> danger
}

@Composable
fun MillenniumButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: MillenniumButtonStyle = MillenniumButtonStyle.Primary,
    content: @Composable () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val state = when {
        !enabled -> MillenniumControlState.Disabled
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        else -> MillenniumControlState.Default
    }
    val properties = MillenniumTheme.buttonTheme.forStyle(style).resolve(state)
    val targetScale = when {
        !enabled -> 1f
        pressed -> properties.motion.pressedScale
        hovered -> properties.motion.hoverScale
        else -> 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(properties.motion.stateChangeMillis),
    )
    val elevation by animateDpAsState(
        targetValue = properties.shadowElevation,
        animationSpec = tween(properties.motion.stateChangeMillis),
    )
    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        indication = LocalIndication.current,
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .defaultMinSize(minWidth = properties.minSize.width, minHeight = properties.minSize.height)
            .alpha(properties.alpha)
            .millenniumSurface(
                background = properties.background,
                shape = properties.shape,
                borderColor = properties.borderColor,
                borderWidth = properties.borderWidth,
                shadowElevation = elevation,
                shadowColors = properties.shadows,
                innerGlowColor = properties.innerGlowColor,
                innerGlowWidth = properties.innerGlowWidth,
                innerGlowInset = properties.innerGlowInset,
                innerGlowTop = properties.innerGlowTop,
            ),
        contentPadding = properties.padding,
    ) {
        MillenniumStyledContent(properties.textStyle, properties.foreground, content)
    }
}

@Preview
@Composable
private fun MillenniumButtonPreview() = MillenniumPreview {
    MillenniumButton(onClick = {}) { Text("Continue") }
}
