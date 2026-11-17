package org.spotube.millennium.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.Text
import com.composeunstyled.Track
import com.composeunstyled.Thumb
import com.composeunstyled.UnstyledSwitch
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.millenniumActionPointer
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumInteractionSurface

/**
 * State-specific appearance and layout of a [MillenniumSwitch].
 *
 * @property trackBackground Track brush when off; `null` = sunken bevel.
 * @property selectedTrackBackground Track brush when on; `null` = accent gel.
 * @property trackShape Track outline; default a fully rounded (50%) shape.
 * @property trackSize Track width and height; default 52 x 30dp.
 * @property trackBorderColor Track border; `null` = accent when focused/selected/hovered/pressed, danger in
 * error, otherwise the palette border.
 * @property trackBorderWidth Track border thickness; `1.dp`, `2.dp` when focused or in error.
 * @property trackGlowColor Track inner highlight; `null` = palette highlight at 35% when on, none when off.
 * @property thumbBackground Thumb brush; `null` = raised-to-inset gel.
 * @property thumbShape Thumb outline; default fully rounded.
 * @property thumbSize Thumb diameter; default `24.dp`.
 * @property thumbInset Padding between thumb and track edge; default `3.dp`.
 * @property thumbElevation Thumb drop-shadow elevation; `3.dp`, `1.dp` while pressed.
 * @property minimumTouchHeight Minimum height of the interactive row; default `44.dp`.
 * @property labelForeground Label colour; `null` = text colour (muted when disabled).
 * @property labelStyle Label typography merged into the current text style.
 * @property contentPadding Padding around the track and label row.
 * @property contentSpacing Gap between track and label; default `12.dp`.
 * @property thumbAnimationMillis Duration in ms of border/elevation tweens and interaction lighting.
 * (The thumb slide itself uses a spring.) Default `160`.
 * @property alpha Overall opacity of the whole row; `0.45f` when disabled.
 * @property thumbBorderColor Thumb border; `null` = palette border.
 * @property thumbBorderWidth Thumb border thickness; default `1.dp`.
 * @property thumbGlowColor Thumb inner highlight; `null` = palette highlight at 36%.
 * @property selectedThumbBackground Thumb brush while the switch is on; `null` = [thumbBackground] (or its default).
 * @property trackBottomRimColor Colour of the lower inner rim of the track; `null` = none.
 * @property thumbBottomRimColor Colour of the lower inner rim of the thumb; `null` = none.
 * @property trackShadowElevation Drop-shadow elevation of the track; default `0.dp` (flat).
 * @property trackShadows Drop-shadow colour layers of the track. `null` = a single 16% black layer.
 * @property thumbShadows Drop-shadow colour layers of the thumb. `null` = a single 16% black layer.
 * @property trackInteractionOverlay Brush painted over the track while this state is active, replacing the
 * built-in hover/press lighting (see [MillenniumButtonThemeProperties.interactionOverlay]). `null` =
 * built-in lighting; a transparent brush disables it.
 * @property trackInteractionRim Brush stroked 1dp inside the track edge together with the overlay. `null` =
 * the built-in rim when the overlay is also `null`, otherwise none.
 * @property trackInteractionTint Accent of the built-in track lighting; `null` = the palette accent.
 * @property thumbInteractionOverlay Same as [trackInteractionOverlay], for the thumb.
 * @property thumbInteractionRim Same as [trackInteractionRim], for the thumb.
 * @property thumbInteractionTint Same as [trackInteractionTint], for the thumb.
 * @property thumbHoverScaleX Horizontal thumb scale while hovered (enabled only). Default `1.04f`.
 * @property thumbHoverScaleY Vertical thumb scale while hovered. Default `1.04f`.
 * @property thumbPressedScaleX Horizontal thumb scale while pressed (the "squish"). Default `1.12f`.
 * @property thumbPressedScaleY Vertical thumb scale while pressed. Default `0.92f`.
 * @property thumbScaleDampingRatio Damping ratio of the thumb scale spring. Default `0.62f`.
 * @property thumbScaleStiffness Stiffness of the thumb scale spring. Default [Spring.StiffnessMedium].
 * @property thumbSlideDampingRatio Damping ratio of the thumb slide spring. Default `0.68f`.
 * @property thumbSlideStiffness Stiffness of the thumb slide spring. Default [Spring.StiffnessMediumLow].
 *
 * When the switch is on and also hovered, focused or pressed, that interaction state is resolved; the
 * nullable visuals (track/thumb brushes, border and glow colours, label colour, rims, interaction brushes)
 * then fall back to the theme's `selected` entry before the built-in default.
 */
@Immutable
data class MillenniumToggleSwitchThemeProperties(
    val trackBackground: Brush? = null,
    val selectedTrackBackground: Brush? = null,
    val trackShape: Shape,
    val trackSize: DpSize,
    val trackBorderColor: Color? = null,
    val trackBorderWidth: Dp,
    val trackGlowColor: Color? = null,
    val thumbBackground: Brush? = null,
    val thumbShape: Shape,
    val thumbSize: Dp,
    val thumbInset: Dp,
    val thumbElevation: Dp,
    val minimumTouchHeight: Dp,
    val labelForeground: Color? = null,
    val labelStyle: TextStyle,
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val thumbAnimationMillis: Int,
    val alpha: Float = 1f,
    val thumbBorderColor: Color? = null,
    val thumbBorderWidth: Dp = 1.dp,
    val thumbGlowColor: Color? = null,
    val selectedThumbBackground: Brush? = null,
    val trackBottomRimColor: Color? = null,
    val thumbBottomRimColor: Color? = null,
    val trackShadowElevation: Dp = 0.dp,
    val trackShadows: List<Color>? = null,
    val thumbShadows: List<Color>? = null,
    val trackInteractionOverlay: Brush? = null,
    val trackInteractionRim: Brush? = null,
    val trackInteractionTint: Color? = null,
    val thumbInteractionOverlay: Brush? = null,
    val thumbInteractionRim: Brush? = null,
    val thumbInteractionTint: Color? = null,
    val thumbHoverScaleX: Float = 1.04f,
    val thumbHoverScaleY: Float = 1.04f,
    val thumbPressedScaleX: Float = 1.12f,
    val thumbPressedScaleY: Float = 0.92f,
    val thumbScaleDampingRatio: Float = 0.62f,
    val thumbScaleStiffness: Float = Spring.StiffnessMedium,
    val thumbSlideDampingRatio: Float = 0.68f,
    val thumbSlideStiffness: Float = Spring.StiffnessMediumLow,
)

/**
 * Theme for [MillenniumSwitch]: one [MillenniumToggleSwitchThemeProperties] per control state.
 * Installed as [MillenniumTheme.toggleSwitchTheme].
 *
 * @property states Properties for each control state (default, hovered, focused, pressed, disabled,
 * selected, error).
 * @see defaultMillenniumToggleSwitchTheme
 */
@Immutable
data class MillenniumToggleSwitchTheme(
    val states: MillenniumControlStateProperty<MillenniumToggleSwitchThemeProperties>,
)

/**
 * Creates the default [MillenniumToggleSwitchTheme].
 *
 * A 52 x 30dp pill track with a 24dp thumb inset 3dp, 3dp thumb shadow (1dp pressed), a 44dp minimum
 * row height, 12dp label spacing and 160ms tweens. Focused and error states use a 2dp track border;
 * disabled uses `alpha = 0.45f`. Colours are `null` and follow the palette. Customise with `copy`:
 *
 * ```kotlin
 * val base = defaultMillenniumToggleSwitchTheme()
 * val wide = base.copy(
 *     states = base.states.copy(default = base.states.default.copy(trackSize = DpSize(64.dp, 34.dp))),
 * )
 * ```
 *
 * @return A new default theme.
 */
fun defaultMillenniumToggleSwitchTheme(): MillenniumToggleSwitchTheme {
    val base = MillenniumToggleSwitchThemeProperties(
        trackShape = RoundedCornerShape(50),
        trackSize = DpSize(52.dp, 30.dp),
        trackBorderWidth = 1.dp,
        thumbShape = RoundedCornerShape(50),
        thumbSize = 24.dp,
        thumbInset = 3.dp,
        thumbElevation = 3.dp,
        minimumTouchHeight = 44.dp,
        labelStyle = TextStyle(fontWeight = FontWeight.Normal),
        contentPadding = PaddingValues(0.dp),
        contentSpacing = 12.dp,
        thumbAnimationMillis = 160,
    )
    return MillenniumToggleSwitchTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(trackBorderWidth = 2.dp),
            pressed = base.copy(thumbElevation = 1.dp),
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(trackBorderWidth = 2.dp),
        ),
    )
}

/**
 * A glossy on/off switch with an optional label, for settings that take effect immediately.
 *
 * Use it for instantly applied preferences; for choices submitted with a form prefer
 * [MillenniumCheckbox]. (This composable lives in `ToggleSwitch.kt`; its theme types are named
 * `MillenniumToggleSwitch*`.)
 *
 * Built on Compose Unstyled `UnstyledSwitch` with `Track` and `Thumb`, so switch semantics, toggle on
 * click/Space and [label] as the accessibility label come from the primitive. The row has a 44dp
 * default minimum height. State resolves as disabled, pressed, keyboard-focused
 * (`collectIsKeyboardFocusedAsState`, no ring after pointer presses), error, hovered, selected, default.
 * The thumb slides on a spring and squishes (wider/shorter when pressed, slightly larger on hover);
 * feedback is drawn only on the track and thumb, not the whole labelled row. The hand cursor is
 * shown while enabled. State is caller-owned.
 *
 * Nullable parameters override single theme properties (`param ?: themeValue ?: fallback`).
 *
 * ```kotlin
 * var airplane by remember { mutableStateOf(false) }
 * MillenniumSwitch(checked = airplane, onCheckedChange = { airplane = it }, label = "Airplane mode")
 * ```
 *
 * @param checked Whether the switch is on; caller-owned.
 * @param onCheckedChange Called with the new value when toggled; not called when disabled.
 * @param label Optional text beside the track and accessibility label. Blank or `null` draws no text.
 * @param modifier Modifier applied to the interactive row.
 * @param enabled When `false` the switch ignores input and uses the disabled state (45% alpha by default).
 * @param trackBackground Overrides the off track brush; `null` falls back to the theme or sunken bevel.
 * @param selectedTrackBackground Overrides the on track brush; `null` falls back to the theme or accent gel.
 * @param trackShape Overrides the track outline; `null` falls back to the theme.
 * @param trackSize Overrides the track size; `null` falls back to the theme (52 x 30dp).
 * @param trackBorderColor Overrides the track border colour; `null` falls back to the theme or palette.
 * @param trackBorderWidth Overrides the track border thickness; `null` falls back to the state's value.
 * @param trackGlowColor Overrides the track inner highlight; `null` falls back to the theme or palette.
 * @param thumbBackground Overrides the thumb brush; `null` falls back to the theme or raised gel.
 * @param thumbShape Overrides the thumb outline; `null` falls back to the theme.
 * @param thumbSize Overrides the thumb diameter; `null` falls back to the theme (24dp).
 * @param thumbInset Overrides the thumb inset; `null` falls back to the theme (3dp).
 * @param thumbElevation Overrides the thumb shadow elevation; `null` falls back to the state's value.
 * @param thumbBorderColor Overrides the thumb border colour; `null` falls back to the theme or palette.
 * @param thumbBorderWidth Overrides the thumb border thickness; `null` falls back to the theme.
 * @param thumbGlowColor Overrides the thumb inner highlight; `null` falls back to the theme or palette.
 * @param minimumTouchHeight Overrides the minimum row height; `null` falls back to the theme (44dp).
 * @param labelForeground Overrides the label colour; `null` falls back to the theme or text/muted colour.
 * @param labelStyle Overrides the label typography; `null` falls back to the theme.
 * @param contentPadding Overrides the padding around the row content; `null` falls back to the theme.
 * @param contentSpacing Overrides the gap between track and label; `null` falls back to the theme.
 * @param thumbAnimationMillis Overrides the border/elevation/lighting tween duration; `null` falls back to the theme.
 * @param alpha Overrides the overall opacity; `null` falls back to the state's value.
 * @param theme Theme supplying state properties; defaults to [MillenniumTheme.toggleSwitchTheme].
 * @param error Marks the switch as invalid; resolves the theme's `error` state unless disabled, pressed or
 * keyboard-focused.
 * @see MillenniumCheckbox
 * @see MillenniumToggleSwitchTheme
 */
@Composable
fun MillenniumSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trackBackground: Brush? = null,
    selectedTrackBackground: Brush? = null,
    trackShape: Shape? = null,
    trackSize: DpSize? = null,
    trackBorderColor: Color? = null,
    trackBorderWidth: Dp? = null,
    trackGlowColor: Color? = null,
    thumbBackground: Brush? = null,
    thumbShape: Shape? = null,
    thumbSize: Dp? = null,
    thumbInset: Dp? = null,
    thumbElevation: Dp? = null,
    thumbBorderColor: Color? = null,
    thumbBorderWidth: Dp? = null,
    thumbGlowColor: Color? = null,
    minimumTouchHeight: Dp? = null,
    labelForeground: Color? = null,
    labelStyle: TextStyle? = null,
    contentPadding: PaddingValues? = null,
    contentSpacing: Dp? = null,
    thumbAnimationMillis: Int? = null,
    alpha: Float? = null,
    theme: MillenniumToggleSwitchTheme = MillenniumTheme.toggleSwitchTheme,
    error: Boolean = false,
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
        selected = checked,
        error = error,
    )
    val properties = theme.states.resolve(state)
    val selectedProperties = theme.states.selected
    fun <V> pick(read: (MillenniumToggleSwitchThemeProperties) -> V?): V? =
        read(properties) ?: if (checked) read(selectedProperties) else null
    val colors = MillenniumTheme.colors
    val resolvedTrackBackground =
        trackBackground ?: pick { it.trackBackground } ?: millenniumBevelBrush(colors, sunken = true)
    val resolvedSelectedTrackBackground = selectedTrackBackground
        ?: pick { it.selectedTrackBackground } ?: gelBrush(colors.accentTop, colors.accentBottom)
    val resolvedTrackBorderColor = trackBorderColor ?: pick { it.trackBorderColor } ?: when (state) {
        MillenniumControlState.Focused, MillenniumControlState.Selected -> colors.accent
        MillenniumControlState.Hovered, MillenniumControlState.Pressed -> colors.accent
        MillenniumControlState.Error -> colors.danger
        else -> colors.border
    }
    val resolvedMillis = thumbAnimationMillis ?: properties.thumbAnimationMillis
    val animatedTrackBorderColor by animateColorAsState(
        targetValue = resolvedTrackBorderColor,
        animationSpec = tween(resolvedMillis),
    )
    val resolvedTrackGlowColor = trackGlowColor ?: pick { it.trackGlowColor }
    ?: if (checked) colors.highlight.copy(alpha = 0.35f) else null
    val resolvedThumbBackground = thumbBackground
        ?: (if (checked) pick { it.selectedThumbBackground } else null)
        ?: pick { it.thumbBackground } ?: gelBrush(colors.raised, colors.inset)
    val resolvedLabelForeground = labelForeground ?: pick { it.labelForeground }
    ?: if (state == MillenniumControlState.Disabled) colors.muted else colors.text
    val animatedThumbElevation by animateDpAsState(
        targetValue = thumbElevation ?: properties.thumbElevation,
        animationSpec = tween(resolvedMillis),
    )
    val thumbScaleX by animateFloatAsState(
        targetValue = when {
            !enabled -> 1f
            pressed -> properties.thumbPressedScaleX
            hovered -> properties.thumbHoverScaleX
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = properties.thumbScaleDampingRatio,
            stiffness = properties.thumbScaleStiffness,
        ),
    )
    val thumbScaleY by animateFloatAsState(
        targetValue = when {
            !enabled -> 1f
            pressed -> properties.thumbPressedScaleY
            hovered -> properties.thumbHoverScaleY
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = properties.thumbScaleDampingRatio,
            stiffness = properties.thumbScaleStiffness,
        ),
    )
    UnstyledSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.millenniumActionPointer(enabled).heightIn(min = minimumTouchHeight ?: properties.minimumTouchHeight)
            .alpha(alpha ?: properties.alpha),
        enabled = enabled,
        accessibilityLabel = label,
        // State feedback is drawn on the track and thumb; don't wash the whole
        // labelled row with the app-wide hover indication.
        indication = null,
        interactionSource = interaction,
    ) {
        Row(
            modifier = androidx.compose.ui.Modifier.padding(
                contentPadding ?: properties.contentPadding
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                contentSpacing ?: properties.contentSpacing
            ),
        ) {
            Track(
                Modifier
                    .size(trackSize ?: properties.trackSize)
                    .millenniumSurface(
                        background = if (checked) resolvedSelectedTrackBackground else resolvedTrackBackground,
                        shape = trackShape ?: properties.trackShape,
                        borderColor = animatedTrackBorderColor,
                        borderWidth = trackBorderWidth ?: properties.trackBorderWidth,
                        shadowElevation = properties.trackShadowElevation,
                        shadowColors = properties.trackShadows ?: listOf(Color.Black.copy(alpha = 0.16f)),
                        innerGlowColor = resolvedTrackGlowColor,
                        bottomRimColor = pick { it.trackBottomRimColor },
                    )
                    .millenniumThemedInteraction(
                        state = state,
                        shape = trackShape ?: properties.trackShape,
                        overlay = pick { it.trackInteractionOverlay },
                        rim = pick { it.trackInteractionRim },
                        tint = pick { it.trackInteractionTint },
                        animationMillis = resolvedMillis,
                    ),
            ) {
                Thumb(
                    animationSpec = spring(
                        dampingRatio = properties.thumbSlideDampingRatio,
                        stiffness = properties.thumbSlideStiffness,
                    ),
                    modifier = Modifier
                        .padding(thumbInset ?: properties.thumbInset)
                        .size(thumbSize ?: properties.thumbSize)
                        .graphicsLayer { scaleX = thumbScaleX; scaleY = thumbScaleY }
                        .millenniumSurface(
                            background = resolvedThumbBackground,
                            shape = thumbShape ?: properties.thumbShape,
                            shadowElevation = animatedThumbElevation,
                            shadowColors = properties.thumbShadows ?: listOf(Color.Black.copy(alpha = 0.16f)),
                            borderColor = thumbBorderColor ?: pick { it.thumbBorderColor }
                            ?: colors.border,
                            borderWidth = thumbBorderWidth ?: properties.thumbBorderWidth,
                            innerGlowColor = thumbGlowColor ?: pick { it.thumbGlowColor }
                            ?: colors.highlight.copy(alpha = 0.36f),
                            bottomRimColor = pick { it.thumbBottomRimColor },
                        )
                        .millenniumThemedInteraction(
                            state = state,
                            shape = thumbShape ?: properties.thumbShape,
                            overlay = pick { it.thumbInteractionOverlay },
                            rim = pick { it.thumbInteractionRim },
                            tint = pick { it.thumbInteractionTint },
                            animationMillis = resolvedMillis,
                        ),
                )
            }
            if (!label.isNullOrBlank()) {
                CompositionLocalProvider(
                    LocalTextStyle provides LocalTextStyle.current.merge(
                        labelStyle ?: properties.labelStyle
                    )
                        .copy(color = resolvedLabelForeground),
                ) { Text(label) }
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumSwitchPreview() = MillenniumPreview {
    MillenniumSwitch(checked = true, onCheckedChange = {}, label = "Airplane mode")
}
