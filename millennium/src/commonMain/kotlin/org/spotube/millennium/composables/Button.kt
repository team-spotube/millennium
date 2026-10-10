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
    val background: Brush? = null,
    val foreground: Color? = null,
    val textStyle: TextStyle,
    val shape: Shape,
    val padding: PaddingValues,
    val minSize: DpSize,
    val borderWidth: Dp,
    val borderColor: Color? = null,
    val shadows: List<Color>? = null,
    val shadowElevation: Dp,
    val innerGlowColor: Color? = null,
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

fun defaultMillenniumButtonTheme(): MillenniumButtonTheme {
    fun variant(style: MillenniumButtonStyle): MillenniumControlStateProperty<MillenniumButtonThemeProperties> {
        val isQuiet = style == MillenniumButtonStyle.Quiet
        val base = MillenniumButtonThemeProperties(
            textStyle = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            shape = RoundedCornerShape(10.dp),
            padding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
            minSize = DpSize(48.dp, 44.dp),
            borderWidth = if (isQuiet) 0.dp else 1.dp,
            shadowElevation = if (isQuiet) 0.dp else 3.dp,
        )
        return MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(borderWidth = if (isQuiet) 1.dp else base.borderWidth),
            focused = base.copy(borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 0.dp),
            disabled = base.copy(alpha = 0.45f, shadowElevation = 0.dp),
            selected = base.copy(borderWidth = 2.dp),
            error = base.copy(borderWidth = 2.dp),
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
    val colors = MillenniumTheme.colors
    val foreground = properties.foreground
        ?: properties.textStyle.color.takeUnless { it == Color.Unspecified }
        ?: when (style) {
            MillenniumButtonStyle.Primary -> colors.onAccent
            MillenniumButtonStyle.Danger -> colors.surface
            else -> colors.text
        }
    val background = properties.background ?: when (style) {
        MillenniumButtonStyle.Primary -> gelBrush(colors.accentTop, colors.accentBottom)
        MillenniumButtonStyle.Secondary -> millenniumBevelBrush(colors)
        MillenniumButtonStyle.Quiet -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
        MillenniumButtonStyle.Danger -> Brush.verticalGradient(listOf(colors.danger, colors.danger))
    }
    val borderColor = properties.borderColor ?: when (state) {
        MillenniumControlState.Focused, MillenniumControlState.Selected -> colors.accent
        MillenniumControlState.Error -> colors.danger
        else -> when (style) {
            MillenniumButtonStyle.Primary -> colors.accentBottom
            MillenniumButtonStyle.Secondary -> colors.border
            MillenniumButtonStyle.Quiet -> Color.Transparent
            MillenniumButtonStyle.Danger -> colors.danger
        }
    }
    val innerGlowColor = properties.innerGlowColor ?: when (state) {
        MillenniumControlState.Hovered, MillenniumControlState.Focused -> colors.highlight.copy(alpha = 0.60f)
        MillenniumControlState.Pressed -> colors.inset.copy(alpha = 0.32f)
        else -> colors.highlight.copy(alpha = 0.35f)
    }
    val shadowColors = properties.shadows ?: listOf(Color.Black.copy(alpha = 0.16f))
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
                background = background,
                shape = properties.shape,
                borderColor = borderColor,
                borderWidth = properties.borderWidth,
                shadowElevation = elevation,
                shadowColors = shadowColors,
                innerGlowColor = innerGlowColor,
                innerGlowWidth = properties.innerGlowWidth,
                innerGlowInset = properties.innerGlowInset,
                innerGlowTop = properties.innerGlowTop,
            ),
        contentPadding = properties.padding,
    ) {
        MillenniumStyledContent(properties.textStyle, foreground, content)
    }
}

@Preview
@Composable
private fun MillenniumButtonPreview() = MillenniumPreview {
    MillenniumButton(onClick = {}) { Text("Continue") }
}
