package org.spotube.millennium.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledIcon
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

enum class MillenniumIconButtonStyle { Primary, Secondary, Quiet, Danger }

/** Complete, state-specific icon-button appearance and layout configuration. */
@Immutable
data class MillenniumIconButtonThemeProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val shape: Shape,
    val size: DpSize,
    val iconSize: Dp,
    val contentPadding: PaddingValues,
    val borderWidth: Dp,
    val borderColor: Color? = null,
    val shadows: List<Color>? = null,
    val shadowElevation: Dp,
    val innerGlowColor: Color? = null,
    val bottomRimColor: Color? = null,
    /** Draws the glass orb dome used by filled (gel) variants. */
    val orb: Boolean = false,
    val orbColor: Color? = null,
    val orbStrength: Float = 0.55f,
    val alpha: Float = 1f,
    val motion: MillenniumButtonMotion = MillenniumButtonMotion(hoverScale = 1f, pressedScale = 0.94f),
)

/** Four independently configurable variants, each with all control states. */
@Immutable
data class MillenniumIconButtonTheme(
    val primary: MillenniumControlStateProperty<MillenniumIconButtonThemeProperties>,
    val secondary: MillenniumControlStateProperty<MillenniumIconButtonThemeProperties>,
    val quiet: MillenniumControlStateProperty<MillenniumIconButtonThemeProperties>,
    val danger: MillenniumControlStateProperty<MillenniumIconButtonThemeProperties>,
)

fun defaultMillenniumIconButtonTheme(): MillenniumIconButtonTheme {
    fun variant(style: MillenniumIconButtonStyle): MillenniumControlStateProperty<MillenniumIconButtonThemeProperties> {
        val filled = style == MillenniumIconButtonStyle.Primary || style == MillenniumIconButtonStyle.Danger
        val base = MillenniumIconButtonThemeProperties(
            shape = CircleShape,
            size = DpSize(44.dp, 44.dp),
            iconSize = 20.dp,
            contentPadding = PaddingValues(10.dp),
            borderWidth = when {
                style == MillenniumIconButtonStyle.Quiet -> 0.dp
                filled -> 3.dp
                else -> 1.dp
            },
            shadowElevation = if (style == MillenniumIconButtonStyle.Quiet) 0.dp else 5.dp,
            orb = filled,
            orbStrength = 0.60f,
        )
        return MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(borderWidth = maxOf(base.borderWidth, 2.dp)),
            pressed = base.copy(shadowElevation = 0.dp, orbStrength = base.orbStrength * 0.8f),
            disabled = base.copy(alpha = 0.45f, shadowElevation = 0.dp),
            selected = base.copy(borderWidth = 2.dp),
            error = base.copy(borderWidth = 2.dp),
        )
    }

    return MillenniumIconButtonTheme(
        primary = variant(MillenniumIconButtonStyle.Primary),
        secondary = variant(MillenniumIconButtonStyle.Secondary),
        quiet = variant(MillenniumIconButtonStyle.Quiet),
        danger = variant(MillenniumIconButtonStyle.Danger),
    )
}

private fun MillenniumIconButtonTheme.forStyle(style: MillenniumIconButtonStyle) = when (style) {
    MillenniumIconButtonStyle.Primary -> primary
    MillenniumIconButtonStyle.Secondary -> secondary
    MillenniumIconButtonStyle.Quiet -> quiet
    MillenniumIconButtonStyle.Danger -> danger
}

/**
 * A compact, circular control holding a single icon. Filled variants
 * ([MillenniumIconButtonStyle.Primary], [MillenniumIconButtonStyle.Danger]) carry the
 * glass orb dome; [MillenniumIconButtonStyle.Secondary] is enamel and
 * [MillenniumIconButtonStyle.Quiet] is transparent.
 */
@Composable
fun MillenniumIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: MillenniumIconButtonStyle = MillenniumIconButtonStyle.Primary,
) {
    IconButtonContainer(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        style = style,
        icon = icon,
        contentDescription = contentDescription,
        content = null,
    )
}

/** Fully custom icon-button content; inherits the variant foreground through `LocalTextStyle`. */
@Composable
fun MillenniumIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: MillenniumIconButtonStyle = MillenniumIconButtonStyle.Primary,
    contentDescription: String? = null,
    content: @Composable () -> Unit,
) {
    IconButtonContainer(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        style = style,
        icon = null,
        contentDescription = contentDescription,
        content = content,
    )
}

@Composable
private fun IconButtonContainer(
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    style: MillenniumIconButtonStyle,
    icon: ImageVector?,
    contentDescription: String?,
    content: (@Composable () -> Unit)?,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsKeyboardFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val state = when {
        !enabled -> MillenniumControlState.Disabled
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        else -> MillenniumControlState.Default
    }
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.iconButtonTheme.forStyle(style).resolve(state)
    val foreground = properties.foreground ?: when (style) {
        MillenniumIconButtonStyle.Primary -> colors.onAccent
        MillenniumIconButtonStyle.Danger -> colors.surface
        else -> colors.text
    }
    val background = properties.background ?: when (style) {
        MillenniumIconButtonStyle.Primary -> gelBrush(colors.accentTop, colors.accentBottom)
        MillenniumIconButtonStyle.Secondary -> millenniumBevelBrush(colors)
        MillenniumIconButtonStyle.Quiet -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
        MillenniumIconButtonStyle.Danger -> gelBrush(
            colors.danger,
            lerp(colors.danger, if (colors.surface.luminance() > colors.danger.luminance()) Color.Black else Color.White, 0.20f),
        )
    }
    val borderColor = properties.borderColor ?: when (state) {
        MillenniumControlState.Focused, MillenniumControlState.Selected -> colors.accent
        MillenniumControlState.Error -> colors.danger
        else -> when (style) {
            MillenniumIconButtonStyle.Primary -> colors.accentBottom
            MillenniumIconButtonStyle.Secondary -> colors.border
            MillenniumIconButtonStyle.Quiet -> Color.Transparent
            MillenniumIconButtonStyle.Danger -> lerp(colors.danger, Color.Black, 0.35f)
        }
    }
    val innerGlowColor = properties.innerGlowColor ?: when (state) {
        MillenniumControlState.Hovered, MillenniumControlState.Focused -> colors.highlight.copy(alpha = 0.60f)
        MillenniumControlState.Pressed -> colors.inset.copy(alpha = 0.32f)
        else -> colors.highlight.copy(alpha = 0.70f)
    }
    val orbColor = if (properties.orb) {
        properties.orbColor ?: Color.White.copy(alpha = properties.orbStrength)
    } else {
        null
    }
    val scale by animateFloatAsState(
        targetValue = when {
            !enabled -> 1f
            pressed -> properties.motion.pressedScale
            hovered -> properties.motion.hoverScale
            else -> 1f
        },
        animationSpec = tween(properties.motion.stateChangeMillis),
    )
    val elevation by animateDpAsState(properties.shadowElevation, tween(properties.motion.stateChangeMillis))
    val semantics = if (contentDescription != null) {
        Modifier.semantics { this.contentDescription = contentDescription }
    } else {
        Modifier
    }
    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        indication = LocalIndication.current,
        contentPadding = PaddingValues(0.dp),
        contentAlignment = Alignment.Center,
        modifier = modifier
            .then(semantics)
            .size(properties.size)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .alpha(properties.alpha)
            .millenniumSurface(
                background = background,
                shape = properties.shape,
                borderColor = borderColor,
                borderWidth = properties.borderWidth,
                shadowElevation = elevation,
                shadowColors = properties.shadows ?: listOf(Color.Black.copy(alpha = 0.16f)),
                innerGlowColor = innerGlowColor,
                bottomRimColor = properties.bottomRimColor
                    ?: if (style != MillenniumIconButtonStyle.Quiet) colors.highlight.copy(alpha = 0.28f) else null,
                orbColor = orbColor,
            ),
    ) {
        if (icon != null) {
            UnstyledIcon(icon, null, Modifier.size(properties.iconSize), foreground)
        } else {
            MillenniumStyledContent(TextStyle.Default, foreground) { content?.invoke() }
        }
    }
}

@Preview
@Composable
private fun MillenniumIconButtonPreview() = MillenniumPreview {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MillenniumIconButton(MillenniumIcons.Sparkle, "Favorite", {})
        MillenniumIconButton(MillenniumIcons.Music, "Play", {}, style = MillenniumIconButtonStyle.Secondary)
        MillenniumIconButton(MillenniumIcons.Chevron, "More", {}, style = MillenniumIconButtonStyle.Quiet)
        MillenniumIconButton(MillenniumIcons.Check, "Delete", {}, style = MillenniumIconButtonStyle.Danger)
    }
}
