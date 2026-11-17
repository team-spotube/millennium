package org.spotube.millennium.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import com.composeunstyled.UnstyledAvatar
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumSurface

/**
 * Visual properties of a [MillenniumAvatar].
 *
 * @property background Fill behind the initials; null uses the accent gel gradient (accent top to accent bottom).
 * @property foreground Initials colour; null uses the palette on-accent colour.
 * @property shape Avatar outline; the default is a circle (`RoundedCornerShape(50)`).
 * @property size Diameter of the avatar (48dp by default). Size changes animate.
 * @property textStyle Style of the initials; when it has no font size the size is 30% of the avatar diameter.
 * @property borderColor Border colour; null uses the palette accent-bottom colour.
 * @property borderWidth Border thickness (1dp by default).
 * @property shadowElevation Drop shadow depth (3dp by default).
 * @property shadowColor Drop shadow colour; null uses black at 16%.
 * @property innerGlowColor Inner top highlight colour; null uses the palette highlight at 55%.
 * @property sizeAnimationMillis Duration in milliseconds of size changes.
 * @property alpha Whole-avatar opacity.
 * @property bottomRimColor Light rim along the avatar's lower edge; null uses the palette highlight at 40%.
 * @property innerGlowWidth Stroke width of the inner rim (1dp by default).
 * @property initialsBackground Brush painted behind the initials only (under the image); null paints none.
 * @property imageOverlay Brush painted over the avatar (above image and initials), clipped to [shape], for tints
 * or gloss on photos; null draws none.
 * @property borderBrush Brush stroked as the border instead of [borderColor]; null uses [borderColor]. Needs
 * [borderWidth] above 0dp.
 * @property interactionOverlay Brush drawn over the avatar fill; null draws none. Resolved per state, so the
 * Pressed or Selected entry of the theme can supply a highlight when a wrapper drives the state.
 * @see MillenniumAvatarTheme
 * @see defaultMillenniumAvatarTheme
 */
@Immutable
data class MillenniumAvatarThemeProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val shape: Shape,
    val size: Dp,
    val textStyle: TextStyle,
    val borderColor: Color? = null,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val shadowColor: Color? = null,
    val innerGlowColor: Color? = null,
    val sizeAnimationMillis: Int,
    val alpha: Float = 1f,
    val bottomRimColor: Color? = null,
    val innerGlowWidth: Dp = 1.dp,
    val initialsBackground: Brush? = null,
    val imageOverlay: Brush? = null,
    val borderBrush: Brush? = null,
    val interactionOverlay: Brush? = null,
)

/**
 * Per-state theme of [MillenniumAvatar].
 *
 * @property states Properties per [MillenniumControlState]. The avatar is not interactive itself; [MillenniumAvatar]
 * resolves the state passed through its `state` parameter (Default when omitted), so wrappers that make the avatar
 * clickable or selectable can drive hover, press, selection, error and disabled looks from the theme.
 */
@Immutable
data class MillenniumAvatarTheme(
    val states: MillenniumControlStateProperty<MillenniumAvatarThemeProperties>,
)

/**
 * Creates the default [MillenniumAvatarTheme] installed as `MillenniumTheme.avatarTheme`.
 *
 * Defaults: 48dp circular gel avatar with a bold initials style, 1dp accent border, 3dp shadow and a 140ms size
 * animation. Customise with `copy` on the per-state bags.
 *
 * @return A fresh theme instance.
 */
fun defaultMillenniumAvatarTheme(): MillenniumAvatarTheme {
    val base = MillenniumAvatarThemeProperties(
        shape = RoundedCornerShape(50),
        size = 48.dp,
        textStyle = TextStyle(fontWeight = FontWeight.Bold),
        borderWidth = 1.dp,
        shadowElevation = 3.dp,
        sizeAnimationMillis = 140,
    )
    return MillenniumAvatarTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 1.dp),
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(borderWidth = 2.dp),
        ),
    )
}

/**
 * A round gel avatar that shows an image when available and the person's initials otherwise.
 *
 * Built on Compose Unstyled's `UnstyledAvatar`: the image [painter] is drawn over the initials, which sit in the
 * underlay and therefore remain visible while no painter is provided or while an image is loading. Use it for user
 * and artist identity in lists, headers and menus. It is not interactive; wrap it in a clickable surface if needed.
 *
 * The avatar animates its size when [size] (or the theme size) changes. Without an explicit font size, initials are
 * 30% of the current diameter.
 *
 * Example:
 * ```kotlin
 * MillenniumAvatar(initials = "AJ", contentDescription = "Alex Jordan")
 * MillenniumAvatar(
 *     initials = "KR",
 *     contentDescription = "Kingkor Roy",
 *     painter = rememberAsyncImagePainter(url),
 *     size = 64.dp,
 * )
 * ```
 *
 * @param initials Text shown in the underlay, typically one or two letters.
 * @param contentDescription Accessibility description of the person or entity the avatar represents.
 * @param modifier Modifier applied to the avatar; size is set by [size] and the theme.
 * @param painter Optional image drawn over the initials; null shows only the initials.
 * @param size Overrides the diameter; null uses the theme (48dp).
 * @param shape Overrides the outline; null uses the theme.
 * @param background Overrides the fill; null uses the theme, then the accent gel gradient.
 * @param foreground Overrides the initials colour; null uses the theme, then the palette on-accent colour.
 * @param borderColor Overrides the border colour; null uses the theme, then accent-bottom.
 * @param borderWidth Overrides the border thickness; null uses the theme.
 * @param shadowElevation Overrides the shadow depth; null uses the theme.
 * @param shadowColor Overrides the shadow colour; null uses the theme, then black at 16%.
 * @param innerGlowColor Overrides the inner glow; null uses the theme, then the palette highlight.
 * @param textStyle Overrides the initials style; null uses the theme.
 * @param sizeAnimationMillis Overrides the size animation duration; null uses the theme.
 * @param alpha Overrides the opacity; null uses the theme.
 * @param theme Per-state theme; defaults to `MillenniumTheme.avatarTheme`.
 * @param state Theme state to render: Default (initial), or Hovered/Focused/Pressed/Selected/Error/Disabled when a
 * wrapper that owns the interaction wants the theme's matching look.
 * @see MillenniumAvatarTheme
 */
@Composable
fun MillenniumAvatar(
    initials: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    size: Dp? = null,
    shape: Shape? = null,
    background: Brush? = null,
    foreground: Color? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    shadowElevation: Dp? = null,
    shadowColor: Color? = null,
    innerGlowColor: Color? = null,
    textStyle: TextStyle? = null,
    sizeAnimationMillis: Int? = null,
    alpha: Float? = null,
    theme: MillenniumAvatarTheme = MillenniumTheme.avatarTheme,
    state: MillenniumControlState = MillenniumControlState.Default,
) {
    val colors = MillenniumTheme.colors
    val properties = theme.states.resolve(state)
    val targetSize = size ?: properties.size
    val animatedSize = animateDpAsState(
        targetSize,
        tween(sizeAnimationMillis ?: properties.sizeAnimationMillis),
    ).value
    val resolvedTextStyle = textStyle ?: properties.textStyle
    val resolvedShape = shape ?: properties.shape
    val resolvedBorderWidth = borderWidth ?: properties.borderWidth
    val borderBrush = properties.borderBrush
    val imageOverlay = properties.imageOverlay
    UnstyledAvatar(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.size(animatedSize).millenniumSurface(
            background = background ?: properties.background ?: gelBrush(colors.accentTop, colors.accentBottom),
            shape = resolvedShape,
            borderColor = if (borderBrush != null) null else borderColor ?: properties.borderColor ?: colors.accentBottom,
            borderWidth = resolvedBorderWidth,
            shadowElevation = shadowElevation ?: properties.shadowElevation,
            shadowColor = shadowColor ?: properties.shadowColor ?: Color.Black.copy(alpha = 0.16f),
            innerGlowColor = innerGlowColor ?: properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.55f),
            innerGlowWidth = properties.innerGlowWidth,
            bottomRimColor = properties.bottomRimColor ?: colors.highlight.copy(alpha = 0.4f),
        ).then(
            if (borderBrush != null && resolvedBorderWidth > 0.dp) {
                Modifier.border(resolvedBorderWidth, borderBrush, resolvedShape)
            } else Modifier,
        ).millenniumOverlayBrush(properties.interactionOverlay, resolvedShape).then(
            if (imageOverlay != null) {
                Modifier.drawWithContent {
                    drawContent()
                    drawRect(imageOverlay)
                }
            } else Modifier,
        ).alpha(alpha ?: properties.alpha),
        underlay = {
            val fontSize = resolvedTextStyle.fontSize.takeUnless { it == androidx.compose.ui.unit.TextUnit.Unspecified }
                ?: (animatedSize.value * 0.3f).sp
            val initialsText: @Composable () -> Unit = {
                Text(
                    initials,
                    style = resolvedTextStyle,
                    color = foreground ?: properties.foreground ?: colors.onAccent,
                    fontSize = fontSize,
                )
            }
            val initialsBackground = properties.initialsBackground
            if (initialsBackground != null) {
                Box(Modifier.fillMaxSize().background(initialsBackground), contentAlignment = Alignment.Center) {
                    initialsText()
                }
            } else {
                initialsText()
            }
        },
    )
}

@Preview
@Composable
private fun MillenniumAvatarPreview() = MillenniumPreview {
    MillenniumAvatar(initials = "AJ", contentDescription = "Alex Jordan")
}
