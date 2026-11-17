package org.spotube.millennium.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.DisclosedContent
import com.composeunstyled.DisclosureButton
import com.composeunstyled.Text
import com.composeunstyled.UnstyledDisclosure
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.millenniumActionPointer
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumInteractionSurface

/**
 * Visual, layout and motion properties of a [MillenniumDisclosure].
 *
 * @property background Surface fill; null uses the palette bevel brush.
 * @property foreground Colour of title, chevron and content text; null uses the palette text colour.
 * @property shape Corner shape of the whole disclosure (12dp rounded).
 * @property borderColor Border colour; null picks accent for hovered, focused and selected (expanded), danger for
 * error and the palette border colour otherwise. Colour changes animate over [animationMillis].
 * @property borderWidth Border thickness (1dp, 2dp when focused or in error).
 * @property shadowElevation Drop shadow depth (0dp, 2dp while pressed); changes animate.
 * @property innerGlowColor Inner highlight; null uses the palette highlight at 50%.
 * @property titleStyle Style of the header title (15sp SemiBold).
 * @property contentStyle Default text style supplied to the body content (14sp).
 * @property minimumHeaderHeight Minimum header height (48dp, above the 44dp target size).
 * @property headerPadding Padding around the header row (12dp).
 * @property contentPadding Padding around the expanded body (16dp).
 * @property headerContentSpacing Horizontal gap between the title and the chevron in the header (12dp).
 * @property contentSpacing Vertical spacing between body children (12dp).
 * @property animationMillis Base duration in milliseconds for colour, elevation and fade transitions (180).
 * @property alpha Whole-disclosure opacity (0.45 in the default disabled bag).
 * @property headerBackground Brush painted behind the header row (inside the surface); `null` = none.
 * @property contentBackground Brush painted behind the expanded body; `null` = none.
 * @property chevronForeground Chevron colour; `null` = [foreground] (or the palette text colour).
 * @property chevronSize Size of the chevron icon; `null` = the icon's intrinsic size.
 * @property chevronRotation Rotation in degrees of the chevron when expanded. Default `180f`.
 * @property chevronDampingRatio Damping ratio of the chevron rotation spring. Default `0.68f`.
 * @property chevronStiffness Stiffness of the chevron rotation spring. Default [Spring.StiffnessMediumLow].
 * @property headerPressedScale Scale of the whole disclosure while pressed. Default `0.992f`.
 * @property headerScaleDampingRatio Damping ratio of the press-scale spring. Default `0.72f`.
 * @property headerScaleStiffness Stiffness of the press-scale spring. Default [Spring.StiffnessMedium].
 * @property contentEnterDampingRatio Damping ratio of the body's expand spring. Default `0.80f`.
 * @property contentEnterStiffness Stiffness of the body's expand spring. Default [Spring.StiffnessMediumLow].
 * @property shadows Drop-shadow colour layers (visible when [shadowElevation] is above 0). `null` = a single
 * 16% black layer.
 * @property innerGlowWidth Thickness of the inner highlight. Default `1.dp`.
 * @property innerGlowTop Offset of the highlight from the top edge. Default `1.dp`.
 * @property bottomRimColor Colour of the lower inner rim; `null` = none.
 * @property headerInteractionOverlay Brush painted over the header while this state is active, replacing the
 * built-in hover/press lighting (see [MillenniumButtonThemeProperties.interactionOverlay]). `null` =
 * built-in lighting; a transparent brush disables it.
 * @property headerInteractionRim Brush stroked 1dp inside the header edge together with the overlay. `null` =
 * the built-in rim when the overlay is also `null`, otherwise none.
 * @property headerInteractionTint Accent of the built-in lighting; `null` = the palette accent.
 *
 * An expanded disclosure that is also hovered, focused or pressed resolves that interaction state; the
 * nullable visuals then fall back to the theme's `selected` entry before the built-in default.
 * @see MillenniumDisclosureTheme
 * @see defaultMillenniumDisclosureTheme
 */
@Immutable
data class MillenniumDisclosureThemeProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val shape: Shape,
    val borderColor: Color? = null,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val innerGlowColor: Color? = null,
    val titleStyle: TextStyle,
    val contentStyle: TextStyle,
    val minimumHeaderHeight: Dp,
    val headerPadding: PaddingValues,
    val contentPadding: PaddingValues,
    val headerContentSpacing: Dp,
    val contentSpacing: Dp,
    val animationMillis: Int,
    val alpha: Float = 1f,
    val headerBackground: Brush? = null,
    val contentBackground: Brush? = null,
    val chevronForeground: Color? = null,
    val chevronSize: Dp? = null,
    val chevronRotation: Float = 180f,
    val chevronDampingRatio: Float = 0.68f,
    val chevronStiffness: Float = Spring.StiffnessMediumLow,
    val headerPressedScale: Float = 0.992f,
    val headerScaleDampingRatio: Float = 0.72f,
    val headerScaleStiffness: Float = Spring.StiffnessMedium,
    val contentEnterDampingRatio: Float = 0.80f,
    val contentEnterStiffness: Float = Spring.StiffnessMediumLow,
    val shadows: List<Color>? = null,
    val innerGlowWidth: Dp = 1.dp,
    val innerGlowTop: Dp = 1.dp,
    val bottomRimColor: Color? = null,
    val headerInteractionOverlay: Brush? = null,
    val headerInteractionRim: Brush? = null,
    val headerInteractionTint: Color? = null,
)

/**
 * Per-state theme of [MillenniumDisclosure].
 *
 * @property states Properties per [MillenniumControlState]. [MillenniumDisclosure] resolves Disabled, Pressed,
 * keyboard Focused, Error, Hovered, Selected (expanded) or Default.
 */
@Immutable
data class MillenniumDisclosureTheme(
    val states: MillenniumControlStateProperty<MillenniumDisclosureThemeProperties>,
)

/**
 * Creates the default [MillenniumDisclosureTheme] installed as `MillenniumTheme.disclosureTheme`.
 *
 * Defaults: a 12dp rounded bevelled surface with a 48dp header, SemiBold 15sp title, 16dp body padding, a 180ms
 * base animation, a flat resting shadow that deepens slightly while pressed, and a thicker border when focused.
 *
 * @return A fresh theme instance.
 */
fun defaultMillenniumDisclosureTheme(): MillenniumDisclosureTheme {
    val base = MillenniumDisclosureThemeProperties(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        borderWidth = 1.dp,
        shadowElevation = 0.dp,
        titleStyle = TextStyle(fontSize = 15.sp, lineHeight = 21.sp, fontWeight = FontWeight.SemiBold),
        contentStyle = TextStyle(fontSize = 14.sp, lineHeight = 21.sp),
        minimumHeaderHeight = 48.dp,
        headerPadding = PaddingValues(12.dp),
        contentPadding = PaddingValues(16.dp),
        headerContentSpacing = 12.dp,
        contentSpacing = 12.dp,
        animationMillis = 180,
    )
    return MillenniumDisclosureTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 2.dp),
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(borderWidth = 2.dp),
        ),
    )
}

/**
 * A collapsible section with a clickable title header and an animated chevron.
 *
 * Built on Compose Unstyled's `UnstyledDisclosure`, which supplies the button semantics, keyboard activation and
 * expanded/collapsed accessibility state. Use it for FAQs, advanced settings and any content worth hiding by default.
 * The expanded state is owned by the caller.
 *
 * Behaviour: the header is at least 48dp tall and shows the hand cursor; the whole surface presses in slightly; the
 * chevron rotates with a spring; the body expands/collapses vertically with a fade. The focus border appears only for
 * keyboard focus. The border highlights in accent when hovered, focused or expanded. Body children inherit
 * `contentStyle` and the foreground colour.
 *
 * Example:
 * ```kotlin
 * var expanded by remember { mutableStateOf(false) }
 * MillenniumDisclosure(
 *     expanded = expanded,
 *     onExpandedChange = { expanded = it },
 *     title = "Why the glossy details?",
 * ) {
 *     MillenniumText("Familiar depth and a little shine, with modern accessibility.")
 * }
 * ```
 *
 * @param expanded Whether the body is visible (caller-owned).
 * @param onExpandedChange Called with the requested new state when the header is activated.
 * @param title Header text.
 * @param modifier Modifier applied to the whole disclosure surface.
 * @param background Overrides the fill; null uses the theme, then the palette bevel brush.
 * @param foreground Overrides title, chevron and body colour; null uses the theme, then the palette text colour.
 * @param shape Overrides the corner shape; null uses the theme.
 * @param borderColor Overrides the border colour in every state; null lets the state decide.
 * @param borderWidth Overrides the border thickness; null uses the state's theme value.
 * @param shadowElevation Overrides the shadow depth; null uses the state's theme value.
 * @param innerGlowColor Overrides the inner highlight; null uses the theme, then the palette highlight.
 * @param titleStyle Overrides the title style; null uses the theme.
 * @param contentStyle Overrides the body text style; null uses the theme.
 * @param minimumHeaderHeight Overrides the minimum header height; null uses the theme (48dp).
 * @param headerPadding Overrides the header padding; null uses the theme.
 * @param contentPadding Overrides the body padding; null uses the theme.
 * @param contentSpacing Overrides the vertical spacing between body children; null uses the theme.
 * @param animationMillis Overrides the base animation duration; null uses the theme.
 * @param alpha Overrides the opacity; null uses the state's theme value.
 * @param theme Per-state theme; defaults to `MillenniumTheme.disclosureTheme`.
 * @param enabled When `false` the header ignores input, the theme's disabled state applies and the surface is
 * exposed as disabled.
 * @param error Marks the disclosure as invalid; resolves the theme's `error` state unless disabled, pressed or
 * keyboard-focused.
 * @param content Body content, shown when [expanded] is true.
 * @see MillenniumDisclosureTheme
 * @see MillenniumIcons.Chevron
 */
@Composable
fun MillenniumDisclosure(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    background: Brush? = null,
    foreground: Color? = null,
    shape: Shape? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    shadowElevation: Dp? = null,
    innerGlowColor: Color? = null,
    titleStyle: TextStyle? = null,
    contentStyle: TextStyle? = null,
    minimumHeaderHeight: Dp? = null,
    headerPadding: PaddingValues? = null,
    contentPadding: PaddingValues? = null,
    contentSpacing: Dp? = null,
    animationMillis: Int? = null,
    alpha: Float? = null,
    theme: MillenniumDisclosureTheme = MillenniumTheme.disclosureTheme,
    enabled: Boolean = true,
    error: Boolean = false,
    content: @Composable () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsKeyboardFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val state = millenniumResolveControlState(
        enabled = enabled,
        pressed = pressed,
        focused = focused,
        hovered = hovered,
        selected = expanded,
        error = error,
    )
    val properties = theme.states.resolve(state)
    val selectedProperties = theme.states.selected
    fun <V> pick(read: (MillenniumDisclosureThemeProperties) -> V?): V? =
        read(properties) ?: if (expanded) read(selectedProperties) else null
    val colors = MillenniumTheme.colors
    val resolvedForeground = foreground ?: pick { it.foreground } ?: colors.text
    val targetBorderColor = borderColor ?: pick { it.borderColor } ?: when (state) {
        MillenniumControlState.Hovered, MillenniumControlState.Focused, MillenniumControlState.Selected -> colors.accent
        MillenniumControlState.Error -> colors.danger
        else -> colors.border
    }
    val resolvedAnimationMillis = animationMillis ?: properties.animationMillis
    val resolvedBorderColor by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(resolvedAnimationMillis),
    )
    val resolvedElevation by animateDpAsState(
        targetValue = shadowElevation ?: properties.shadowElevation,
        animationSpec = tween(resolvedAnimationMillis),
    )
    val headerScale by animateFloatAsState(
        targetValue = if (pressed) properties.headerPressedScale else 1f,
        animationSpec = spring(
            dampingRatio = properties.headerScaleDampingRatio,
            stiffness = properties.headerScaleStiffness,
        ),
    )
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) properties.chevronRotation else 0f,
        animationSpec = spring(
            dampingRatio = properties.chevronDampingRatio,
            stiffness = properties.chevronStiffness,
        ),
    )
    UnstyledDisclosure(
        expanded = expanded,
        onExpandedChange = { if (enabled) onExpandedChange(it) },
        modifier = modifier
            .then(if (enabled) Modifier else Modifier.semantics { disabled() })
            .graphicsLayer { scaleX = headerScale; scaleY = headerScale }
            .millenniumSurface(
                background = background ?: pick { it.background } ?: millenniumBevelBrush(colors),
                shape = shape ?: properties.shape,
                borderColor = resolvedBorderColor,
                borderWidth = borderWidth ?: properties.borderWidth,
                shadowElevation = resolvedElevation,
                shadowColors = pick { it.shadows } ?: listOf(Color.Black.copy(alpha = 0.16f)),
                innerGlowColor = innerGlowColor ?: pick { it.innerGlowColor } ?: colors.highlight.copy(alpha = 0.5f),
                innerGlowWidth = properties.innerGlowWidth,
                innerGlowTop = properties.innerGlowTop,
                bottomRimColor = pick { it.bottomRimColor },
            ).alpha(alpha ?: properties.alpha),
    ) {
        Column {
            DisclosureButton(
                modifier = Modifier.millenniumActionPointer(enabled).fillMaxWidth()
                    .heightIn(min = minimumHeaderHeight ?: properties.minimumHeaderHeight)
                    .then(
                        pick { it.headerBackground }?.let { Modifier.background(it) } ?: Modifier,
                    )
                    .millenniumThemedInteraction(
                        state = state,
                        shape = shape ?: properties.shape,
                        overlay = pick { it.headerInteractionOverlay },
                        rim = pick { it.headerInteractionRim },
                        tint = pick { it.headerInteractionTint },
                        animationMillis = resolvedAnimationMillis,
                    )
                    .padding(headerPadding ?: properties.headerPadding),
                contentPadding = PaddingValues(0.dp),
                indication = null,
                interactionSource = interaction,
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(properties.headerContentSpacing),
                ) {
                    Text(title, modifier = Modifier.weight(1f), style = titleStyle ?: properties.titleStyle, color = resolvedForeground)
                    MillenniumIcon(
                        MillenniumIcons.Chevron,
                        modifier = Modifier
                            .then(properties.chevronSize?.let { Modifier.size(it) } ?: Modifier)
                            .rotate(arrowRotation),
                        tint = pick { it.chevronForeground } ?: resolvedForeground,
                    )
                }
            }
            DisclosedContent(
                enter = expandVertically(
                    animationSpec = spring(
                        dampingRatio = properties.contentEnterDampingRatio,
                        stiffness = properties.contentEnterStiffness,
                    ),
                    expandFrom = Alignment.Top,
                ) + fadeIn(animationSpec = tween(resolvedAnimationMillis, easing = FastOutSlowInEasing)),
                exit = shrinkVertically(
                    animationSpec = tween((resolvedAnimationMillis * 0.8f).toInt().coerceAtLeast(100), easing = FastOutLinearInEasing),
                    shrinkTowards = Alignment.Top,
                ) + fadeOut(animationSpec = tween((resolvedAnimationMillis * 0.7f).toInt().coerceAtLeast(90), easing = FastOutLinearInEasing)),
            ) {
                Column(
                    Modifier
                        .then(pick { it.contentBackground }?.let { Modifier.background(it) } ?: Modifier)
                        .padding(contentPadding ?: properties.contentPadding),
                    verticalArrangement = Arrangement.spacedBy(contentSpacing ?: properties.contentSpacing),
                ) {
                    MillenniumStyledContent(contentStyle ?: properties.contentStyle, resolvedForeground, content)
                }
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumDisclosurePreview() = MillenniumPreview {
    MillenniumDisclosure(expanded = true, onExpandedChange = {}, title = "Why the glossy details?") {
        MillenniumText("Familiar depth and a little shine, with the accessibility and flexibility of modern Compose.")
    }
}
