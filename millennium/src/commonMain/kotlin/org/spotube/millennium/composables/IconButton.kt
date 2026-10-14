package org.spotube.millennium.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
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
import org.spotube.millennium.theme.millenniumActionPointer
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumInteractionSurface
import org.spotube.millennium.theme.millenniumQuietEnamelStops

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
    /** Draws the spherical glass reflections used by filled variants. */
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
                filled -> 1.dp
                else -> 1.dp
            },
            shadowElevation = if (style == MillenniumIconButtonStyle.Quiet) 0.dp else 5.dp,
            orb = filled,
            orbStrength = 0.48f,
        )
        return MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(borderWidth = 1.dp, shadowElevation = if (filled) 7.dp else 3.dp, orbStrength = 0.62f),
            focused = base.copy(borderWidth = maxOf(base.borderWidth, 2.dp)),
            pressed = base.copy(borderWidth = maxOf(base.borderWidth, 1.dp), shadowElevation = 0.dp, orbStrength = base.orbStrength * 0.8f),
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
 * Vista-style glass orb; [MillenniumIconButtonStyle.Secondary] is enamel and
 * [MillenniumIconButtonStyle.Quiet] is transparent at rest and becomes glassy enamel on hover/press.
 */
@Composable
fun MillenniumIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: MillenniumIconButtonStyle = MillenniumIconButtonStyle.Primary,
    background: Brush? = null,
    foreground: Color? = null,
    shape: Shape? = null,
    size: DpSize? = null,
    iconSize: Dp? = null,
    borderWidth: Dp? = null,
    borderColor: Color? = null,
    shadows: List<Color>? = null,
    shadowElevation: Dp? = null,
    innerGlowColor: Color? = null,
    bottomRimColor: Color? = null,
    orb: Boolean? = null,
    orbColor: Color? = null,
    orbStrength: Float? = null,
    alpha: Float? = null,
    motion: MillenniumButtonMotion? = null,
    theme: MillenniumIconButtonTheme = MillenniumTheme.iconButtonTheme,
) {
    IconButtonContainer(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        style = style,
        icon = icon,
        contentDescription = contentDescription,
        content = null,
        theme = theme,
        background = background,
        foreground = foreground,
        shape = shape,
        size = size,
        iconSize = iconSize,
        borderWidth = borderWidth,
        borderColor = borderColor,
        shadows = shadows,
        shadowElevation = shadowElevation,
        innerGlowColor = innerGlowColor,
        bottomRimColor = bottomRimColor,
        orb = orb,
        orbColor = orbColor,
        orbStrength = orbStrength,
        alpha = alpha,
        motion = motion,
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
    background: Brush? = null,
    foreground: Color? = null,
    shape: Shape? = null,
    size: DpSize? = null,
    iconSize: Dp? = null,
    borderWidth: Dp? = null,
    borderColor: Color? = null,
    shadows: List<Color>? = null,
    shadowElevation: Dp? = null,
    innerGlowColor: Color? = null,
    bottomRimColor: Color? = null,
    orb: Boolean? = null,
    orbColor: Color? = null,
    orbStrength: Float? = null,
    alpha: Float? = null,
    motion: MillenniumButtonMotion? = null,
    theme: MillenniumIconButtonTheme = MillenniumTheme.iconButtonTheme,
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
        theme = theme,
        background = background,
        foreground = foreground,
        shape = shape,
        size = size,
        iconSize = iconSize,
        borderWidth = borderWidth,
        borderColor = borderColor,
        shadows = shadows,
        shadowElevation = shadowElevation,
        innerGlowColor = innerGlowColor,
        bottomRimColor = bottomRimColor,
        orb = orb,
        orbColor = orbColor,
        orbStrength = orbStrength,
        alpha = alpha,
        motion = motion,
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
    theme: MillenniumIconButtonTheme,
    background: Brush?,
    foreground: Color?,
    shape: Shape?,
    size: DpSize?,
    iconSize: Dp?,
    borderWidth: Dp?,
    borderColor: Color?,
    shadows: List<Color>?,
    shadowElevation: Dp?,
    innerGlowColor: Color?,
    bottomRimColor: Color?,
    orb: Boolean?,
    orbColor: Color?,
    orbStrength: Float?,
    alpha: Float?,
    motion: MillenniumButtonMotion?,
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
    val properties = theme.forStyle(style).resolve(state)
    val resolvedOrb = orb ?: properties.orb
    val orbTop = if (style == MillenniumIconButtonStyle.Danger) colors.danger else colors.accentTop
    val orbBottom = if (style == MillenniumIconButtonStyle.Danger) {
        lerp(colors.danger, Color.Black, 0.20f)
    } else {
        colors.accentBottom
    }
    val resolvedMotion = motion ?: properties.motion
    val resolvedForeground = foreground ?: properties.foreground ?: when (style) {
        MillenniumIconButtonStyle.Primary -> if (resolvedOrb) Color.White else colors.onAccent
        MillenniumIconButtonStyle.Danger -> if (resolvedOrb) Color.White else colors.surface
        MillenniumIconButtonStyle.Quiet -> when (state) {
            MillenniumControlState.Hovered, MillenniumControlState.Focused,
            MillenniumControlState.Pressed -> colors.accent
            else -> colors.text
        }
        else -> colors.text
    }
    val quietVisibility by animateFloatAsState(
        targetValue = if (state == MillenniumControlState.Hovered || state == MillenniumControlState.Focused || state == MillenniumControlState.Pressed) 1f else 0f,
        animationSpec = tween(resolvedMotion.stateChangeMillis),
    )
    val quietStops = millenniumQuietEnamelStops(colors, state == MillenniumControlState.Pressed)
        .map { (stop, color) -> stop to color.copy(alpha = color.alpha * quietVisibility) }
    val animatedForeground by animateColorAsState(
        targetValue = resolvedForeground,
        animationSpec = tween(resolvedMotion.stateChangeMillis),
    )
    val resolvedBackground = background ?: properties.background ?: if (resolvedOrb) {
        Brush.verticalGradient(*millenniumIconOrbStops(orbTop, orbBottom).toTypedArray())
    } else when (style) {
        MillenniumIconButtonStyle.Primary -> gelBrush(colors.accentTop, colors.accentBottom)
        MillenniumIconButtonStyle.Secondary -> millenniumBevelBrush(colors)
        MillenniumIconButtonStyle.Quiet -> Brush.verticalGradient(*quietStops.toTypedArray())
        MillenniumIconButtonStyle.Danger -> gelBrush(
            colors.danger,
            lerp(colors.danger, if (colors.surface.luminance() > colors.danger.luminance()) Color.Black else Color.White, 0.20f),
        )
    }
    val resolvedBorderColor = borderColor ?: properties.borderColor ?: when (state) {
        MillenniumControlState.Focused, MillenniumControlState.Selected -> colors.accent
        MillenniumControlState.Error -> colors.danger
        else -> when (style) {
            MillenniumIconButtonStyle.Primary -> if (resolvedOrb) lerp(colors.accentBottom, Color.Black, 0.45f) else colors.accentBottom
            MillenniumIconButtonStyle.Secondary -> colors.border
            MillenniumIconButtonStyle.Quiet -> if (hovered || pressed) colors.accent.copy(alpha = 0.55f) else Color.Transparent
            MillenniumIconButtonStyle.Danger -> lerp(colors.danger, Color.Black, 0.35f)
        }
    }
    val resolvedInnerGlowColor = innerGlowColor ?: properties.innerGlowColor
        ?: if (resolvedOrb || style == MillenniumIconButtonStyle.Quiet) null else when (state) {
        MillenniumControlState.Hovered, MillenniumControlState.Focused -> colors.highlight.copy(alpha = 0.60f)
        MillenniumControlState.Pressed -> colors.inset.copy(alpha = 0.32f)
        else -> colors.highlight.copy(alpha = 0.70f)
    }
    val resolvedOrbColor = if (resolvedOrb) {
        orbColor ?: properties.orbColor ?: Color.White.copy(alpha = orbStrength ?: properties.orbStrength)
    } else {
        null
    }
    val scale by animateFloatAsState(
        targetValue = when {
            !enabled -> 1f
            pressed -> resolvedMotion.pressedScale
            hovered -> maxOf(resolvedMotion.hoverScale, 1.04f)
            else -> 1f
        },
        animationSpec = spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessMedium),
    )
    val elevation by animateDpAsState(
        shadowElevation ?: properties.shadowElevation,
        spring(dampingRatio = 0.78f, stiffness = Spring.StiffnessMediumLow),
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = resolvedBorderColor,
        animationSpec = tween(resolvedMotion.stateChangeMillis),
    )
    val semantics = if (contentDescription != null) {
        Modifier.semantics { this.contentDescription = contentDescription }
    } else {
        Modifier
    }
    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        indication = null,
        contentPadding = PaddingValues(0.dp),
        contentAlignment = Alignment.Center,
        modifier = modifier
            .millenniumActionPointer(enabled)
            .then(semantics)
            .size(size ?: properties.size)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .alpha(alpha ?: properties.alpha)
            .millenniumSurface(
                background = resolvedBackground,
                shape = shape ?: properties.shape,
                borderColor = animatedBorderColor,
                borderWidth = borderWidth ?: properties.borderWidth,
                shadowElevation = elevation,
                opaqueFill = style != MillenniumIconButtonStyle.Quiet || resolvedOrb,
                shadowColors = shadows ?: properties.shadows ?: listOf(Color.Black.copy(alpha = 0.16f)),
                innerGlowColor = resolvedInnerGlowColor,
                bottomRimColor = bottomRimColor ?: properties.bottomRimColor
                    ?: if (!resolvedOrb && style != MillenniumIconButtonStyle.Quiet) colors.highlight.copy(alpha = 0.28f) else null,
            )
            .then(
                if (resolvedOrbColor != null) Modifier.millenniumIconOrb(
                    reflection = resolvedOrbColor,
                    tint = orbTop,
                    shape = shape ?: properties.shape,
                    borderWidth = borderWidth ?: properties.borderWidth,
                    borderColor = if (borderColor != null || properties.borderColor != null || focused) animatedBorderColor else null,
                ) else Modifier,
            )
            .millenniumInteractionSurface(state, shape ?: properties.shape,
                tint = if (style == MillenniumIconButtonStyle.Danger) colors.danger else colors.accent,
                animationMillis = resolvedMotion.stateChangeMillis),
    ) {
        if (icon != null) {
            UnstyledIcon(icon, null, Modifier.size(iconSize ?: properties.iconSize), animatedForeground)
        } else {
            MillenniumStyledContent(TextStyle.Default, animatedForeground) { content?.invoke() }
        }
    }
}

/** Continuous spherical shading, deliberately without the button gel's hard halfway break. */
internal fun millenniumIconOrbStops(top: Color, bottom: Color): List<Pair<Float, Color>> {
    // Every accent uses deep jewel glass. Bright amber/citron belong at the illuminated lip,
    // not across the face; keeping the body dark also gives every orb a white icon.
    val body = lerp(bottom, Color.Black, 0.52f)
    return listOf(
        0f to lerp(bottom, body, 0.65f),
        0.28f to body,
        0.60f to body,
        0.82f to lerp(body, top, 0.40f),
        1f to top,
    )
}

/** Cached, size-relative glass layers; the surface clips them to the caller's shape. */
private fun Modifier.millenniumIconOrb(
    reflection: Color,
    tint: Color,
    shape: Shape,
    borderWidth: Dp,
    borderColor: Color?,
): Modifier = drawWithCache {
    val width = size.width
    val height = size.height
    val rim = maxOf(borderWidth.toPx(), size.minDimension * 0.025f)
    val outline = shape.createOutline(size, layoutDirection, this)
    val dome = Path().apply {
        moveTo(width * 0.08f, height * 0.37f)
        cubicTo(width * 0.12f, -height * 0.04f, width * 0.88f, -height * 0.04f, width * 0.92f, height * 0.37f)
        cubicTo(width * 0.94f, height * 0.49f, width * 0.81f, height * 0.39f, width * 0.70f, height * 0.38f)
        cubicTo(width * 0.48f, height * 0.32f, width * 0.28f, height * 0.43f, width * 0.12f, height * 0.44f)
        cubicTo(width * 0.08f, height * 0.45f, width * 0.07f, height * 0.41f, width * 0.08f, height * 0.37f)
        close()
    }
    val domeBrush = Brush.radialGradient(
        0f to reflection,
        0.50f to reflection.copy(alpha = reflection.alpha * 0.42f),
        1f to reflection.copy(alpha = reflection.alpha * 0.03f),
        center = Offset(width * 0.46f, height * 0.04f),
        radius = size.minDimension * 0.63f,
    )
    // A light pool below the sphere, not a white smile painted across its face.
    val glowTint = if (tint.blue > tint.red && tint.blue > tint.green) {
        lerp(tint, Color(0xFF65E8FF), 0.65f)
    } else {
        lerp(tint, Color.White, 0.25f)
    }
    val lowerGlow = Brush.radialGradient(
        0f to glowTint.copy(alpha = reflection.alpha * 0.95f),
        0.40f to glowTint.copy(alpha = reflection.alpha * 0.65f),
        0.78f to glowTint.copy(alpha = reflection.alpha * 0.10f),
        1f to Color.Transparent,
        center = Offset(width * 0.5f, height * 1.12f),
        radius = size.minDimension * 0.66f,
    )
    val rimBrush = Brush.linearGradient(
        0f to Color(0xFF26364F),
        0.28f to Color(0xFF66748A),
        0.62f to Color(0xFFC6D4DA),
        1f to Color(0xFF89949D),
        start = Offset.Zero,
        end = Offset(width * 0.65f, height),
    )
    val innerRim = Brush.verticalGradient(
        0f to reflection.copy(alpha = reflection.alpha * 0.70f),
        0.35f to Color.Transparent,
        0.75f to Color.Transparent,
        1f to glowTint.copy(alpha = reflection.alpha * 0.75f),
    )
    onDrawWithContent {
        drawRect(lowerGlow)
        drawPath(dome, domeBrush)
        if (borderWidth > 0.dp) {
            if (borderColor != null) drawOutline(outline, borderColor, style = Stroke(rim * 2f))
            else drawOutline(outline, rimBrush, style = Stroke(rim * 2f))
        }
        val innerInset = rim * 1.7f
        if (size.minDimension > innerInset * 2f) {
            val innerSize = Size(width - innerInset * 2f, height - innerInset * 2f)
            val innerOutline = shape.createOutline(innerSize, layoutDirection, this)
            inset(innerInset) {
                drawOutline(innerOutline, innerRim, style = Stroke(size.minDimension * 0.018f))
            }
        }
        drawContent()
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
