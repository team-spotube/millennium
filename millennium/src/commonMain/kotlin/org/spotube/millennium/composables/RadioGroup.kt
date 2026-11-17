package org.spotube.millennium.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.sp
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.RadioButton
import com.composeunstyled.SelectedIndicator
import com.composeunstyled.Text
import com.composeunstyled.UnstyledRadioGroup
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
 * State-specific appearance and layout of each option in a [MillenniumRadioGroup].
 *
 * @property indicatorBackground Unselected indicator brush; `null` = sunken bevel.
 * @property selectedIndicatorBackground Selected indicator brush; `null` = accent gel.
 * @property indicatorShape Indicator outline; default fully rounded (50%).
 * @property indicatorSize Indicator width and height; default 22 x 22dp.
 * @property dotSize Diameter of the inner selection dot; default `8.dp`.
 * @property dotColor Selection dot colour; `null` = on-accent.
 * @property indicatorBorderColor Indicator border; `null` = accent when focused/hovered/pressed/selected,
 * danger in error, otherwise the palette border.
 * @property indicatorBorderWidth Indicator border thickness; `1.dp`, `2.dp` when focused or in error.
 * @property minimumTouchHeight Minimum height of each option row; default `44.dp`.
 * @property optionPadding Padding around each option's content.
 * @property optionSpacing Gap between indicator and label; default `12.dp`.
 * @property groupSpacing Vertical gap between options; default `4.dp`. Read from the default state only.
 * @property labelStyle Option label typography; default 14sp / 20sp normal.
 * @property labelColor Label colour; `null` = text colour.
 * @property selectedLabelColor Label colour of the selected option; `null` = text colour.
 * @property stateAnimationMillis Duration in ms of border, label and alpha tweens and the dot's pop-in. Default `120`.
 * @property alpha Opacity of the option; default `1f` (the default disabled state uses `0.45f`).
 * @property indicatorGlowColor Indicator inner highlight; `null` = palette highlight at 32% when selected, none otherwise.
 * @property indicatorElevation Indicator drop-shadow elevation; default `0.dp`.
 * @property indicatorBottomRimColor Colour of the lower inner rim of the indicator; `null` = none.
 * @property indicatorShadows Drop-shadow colour layers of the indicator (visible when [indicatorElevation] is
 * above 0). `null` = a single 16% black layer.
 * @property dotBrush Brush of the selection dot; `null` = [dotColor] (or on-accent) as a solid colour.
 * @property dotShape Shape of the selection dot; default [CircleShape].
 * @property indicatorHoverScale Indicator scale while hovered. Default `1.05f`.
 * @property indicatorPressedScale Indicator scale while pressed. Default `0.90f`.
 * @property indicatorScaleDampingRatio Damping ratio of the indicator scale spring. Default `0.65f`.
 * @property indicatorScaleStiffness Stiffness of the indicator scale spring. Default [Spring.StiffnessMedium].
 * @property dotEnterScale Initial scale of the dot popping in. Default `0.3f`.
 * @property dotEnterDampingRatio Damping ratio of the dot pop-in spring. Default `0.52f`.
 * @property dotExitScale Final scale of the dot shrinking away. Default `0.3f`.
 * @property indicatorInteractionOverlay Brush painted over the indicator while this state is active,
 * replacing the built-in hover/press lighting (see [MillenniumButtonThemeProperties.interactionOverlay]).
 * `null` = built-in lighting; a transparent brush disables it.
 * @property indicatorInteractionRim Brush stroked 1dp inside the indicator edge together with the overlay.
 * `null` = the built-in rim when the overlay is also `null`, otherwise none.
 * @property indicatorInteractionTint Accent of the built-in lighting; `null` = the palette accent.
 *
 * A selected option that is also hovered, focused or pressed resolves that interaction state; the nullable
 * visuals (brushes, border, glow, dot, rim, interaction brushes) then fall back to the theme's `selected`
 * entry before the built-in default.
 */
@Immutable
data class MillenniumRadioGroupThemeProperties(
    val indicatorBackground: Brush? = null,
    val selectedIndicatorBackground: Brush? = null,
    val indicatorShape: Shape,
    val indicatorSize: DpSize,
    val dotSize: Dp,
    val dotColor: Color? = null,
    val indicatorBorderColor: Color? = null,
    val indicatorBorderWidth: Dp,
    val minimumTouchHeight: Dp,
    val optionPadding: PaddingValues,
    val optionSpacing: Dp,
    val groupSpacing: Dp,
    val labelStyle: TextStyle,
    val labelColor: Color? = null,
    val selectedLabelColor: Color? = null,
    val stateAnimationMillis: Int,
    val alpha: Float = 1f,
    val indicatorGlowColor: Color? = null,
    val indicatorElevation: Dp = 0.dp,
    val indicatorBottomRimColor: Color? = null,
    val indicatorShadows: List<Color>? = null,
    val dotBrush: Brush? = null,
    val dotShape: Shape = CircleShape,
    val indicatorHoverScale: Float = 1.05f,
    val indicatorPressedScale: Float = 0.90f,
    val indicatorScaleDampingRatio: Float = 0.65f,
    val indicatorScaleStiffness: Float = Spring.StiffnessMedium,
    val dotEnterScale: Float = 0.3f,
    val dotEnterDampingRatio: Float = 0.52f,
    val dotExitScale: Float = 0.3f,
    val indicatorInteractionOverlay: Brush? = null,
    val indicatorInteractionRim: Brush? = null,
    val indicatorInteractionTint: Color? = null,
)

/**
 * Theme for [MillenniumRadioGroup]: one [MillenniumRadioGroupThemeProperties] per control state.
 * Installed as [MillenniumTheme.radioGroupTheme].
 *
 * @property states Properties for each control state (default, hovered, focused, pressed, disabled,
 * selected, error).
 * @see defaultMillenniumRadioGroupTheme
 */
@Immutable
data class MillenniumRadioGroupTheme(
    val states: MillenniumControlStateProperty<MillenniumRadioGroupThemeProperties>,
)

/**
 * Creates the default [MillenniumRadioGroupTheme].
 *
 * 22dp round indicators with an 8dp dot, 1dp border (2dp focused or in error), 12dp indicator-label
 * spacing, 4dp between options, 44dp minimum option height, 14sp label and 120ms tweens. Disabled uses
 * `alpha = 0.45f`. Colours are `null` and follow the palette. Customise with `copy`:
 *
 * ```kotlin
 * val base = defaultMillenniumRadioGroupTheme()
 * val roomy = base.copy(states = base.states.copy(default = base.states.default.copy(groupSpacing = 12.dp)))
 * ```
 *
 * @return A new default theme.
 */
fun defaultMillenniumRadioGroupTheme(): MillenniumRadioGroupTheme {
    val base = MillenniumRadioGroupThemeProperties(
        indicatorShape = RoundedCornerShape(50),
        indicatorSize = DpSize(22.dp, 22.dp),
        dotSize = 8.dp,
        indicatorBorderWidth = 1.dp,
        minimumTouchHeight = 44.dp,
        optionPadding = PaddingValues(0.dp),
        optionSpacing = 12.dp,
        groupSpacing = 4.dp,
        labelStyle = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Normal
        ),
        stateAnimationMillis = 120,
    )
    return MillenniumRadioGroupTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(indicatorBorderWidth = 2.dp),
            pressed = base,
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(indicatorBorderWidth = 2.dp),
        ),
    )
}

/**
 * A vertical group of mutually exclusive options rendered as beveled radio buttons.
 *
 * Use it to pick exactly one value from a short list. Selection is caller-owned: pass the current
 * [value], update it in [onValueChange] and supply [options] of any type [T].
 *
 * Built on Compose Unstyled `UnstyledRadioGroup` and `RadioButton`, which provide the radio-group
 * semantics, per-option selection, [label] as the group's accessibility label and arrow-key
 * navigation behaviour of the primitive. Each option row has a 44dp default minimum height. Option
 * state resolves as disabled, pressed, keyboard-focused (`collectIsKeyboardFocusedAsState`, no ring after
 * pointer presses), error, hovered, selected, default. Feedback stays on the small indicator (spring scale, border
 * colour, interaction lighting) instead of washing the row; the selection dot pops in with a spring.
 * The hand cursor is shown over options.
 *
 * [label] names the group for accessibility; option labels are always drawn. Pass `enabled = false` to dim
 * and disable every option.
 *
 * Nullable parameters override single theme properties (`param ?: themeValue ?: fallback`).
 *
 * ```kotlin
 * var quality by remember { mutableStateOf("Original") }
 * MillenniumRadioGroup(
 *     value = quality,
 *     onValueChange = { quality = it },
 *     options = listOf("Original", "Remastered", "Hi-fi"),
 *     label = "Playback quality",
 * )
 * ```
 *
 * @param T Option type; compared with `==` against [value] to determine the selection.
 * @param value The currently selected option; caller-owned.
 * @param onValueChange Called with the option the user selects.
 * @param options The options, shown top to bottom in order.
 * @param label Accessibility label of the group; also required for option labels to be rendered (see note).
 * @param modifier Modifier applied to the group container.
 * @param optionLabel Maps an option to its display text; defaults to `toString()`.
 * @param indicatorBackground Overrides the unselected indicator brush; `null` falls back to the theme or sunken bevel.
 * @param selectedIndicatorBackground Overrides the selected indicator brush; `null` falls back to the theme or accent gel.
 * @param indicatorShape Overrides the indicator outline; `null` falls back to the theme.
 * @param indicatorSize Overrides the indicator size; `null` falls back to the theme (22dp).
 * @param dotSize Overrides the selection dot size; `null` falls back to the theme (8dp).
 * @param dotColor Overrides the selection dot colour; `null` falls back to the theme or on-accent.
 * @param indicatorBorderColor Overrides the border colour; `null` falls back to the theme or palette.
 * @param indicatorBorderWidth Overrides the border thickness; `null` falls back to the state's value.
 * @param indicatorGlowColor Overrides the indicator inner highlight; `null` falls back to the theme or palette.
 * @param indicatorElevation Overrides the indicator shadow elevation; `null` falls back to the theme.
 * @param minimumTouchHeight Overrides the minimum option height; `null` falls back to the theme (44dp).
 * @param optionPadding Overrides the padding around each option; `null` falls back to the theme.
 * @param optionSpacing Overrides the gap between indicator and label; `null` falls back to the theme.
 * @param groupSpacing Overrides the vertical gap between options; `null` falls back to the theme's default state.
 * @param labelStyle Overrides the option label typography; `null` falls back to the theme.
 * @param labelColor Overrides the option label colour; `null` falls back to the theme or text colour.
 * @param selectedLabelColor Overrides the selected option's label colour; `null` falls back to the theme or text colour.
 * @param stateAnimationMillis Overrides the tween duration; `null` falls back to the theme.
 * @param alpha Overrides the option opacity; `null` falls back to the state's value.
 * @param enabled When `false` every option is dimmed, ignores input and resolves the disabled theme state.
 * @param theme Theme supplying state properties; defaults to [MillenniumTheme.radioGroupTheme].
 * @param error Marks every option as invalid; resolves the theme's `error` state unless an option is
 * disabled, pressed or keyboard-focused.
 * @see MillenniumCheckbox
 * @see MillenniumRadioGroupTheme
 */
@Composable
fun <T> MillenniumRadioGroup(
    value: T,
    onValueChange: (T) -> Unit,
    options: List<T>,
    label: String? = null,
    modifier: Modifier = Modifier,
    optionLabel: (T) -> String = { it.toString() },
    indicatorBackground: Brush? = null,
    selectedIndicatorBackground: Brush? = null,
    indicatorShape: Shape? = null,
    indicatorSize: DpSize? = null,
    dotSize: Dp? = null,
    dotColor: Color? = null,
    indicatorBorderColor: Color? = null,
    indicatorBorderWidth: Dp? = null,
    indicatorGlowColor: Color? = null,
    indicatorElevation: Dp? = null,
    minimumTouchHeight: Dp? = null,
    optionPadding: PaddingValues? = null,
    optionSpacing: Dp? = null,
    groupSpacing: Dp? = null,
    labelStyle: TextStyle? = null,
    labelColor: Color? = null,
    selectedLabelColor: Color? = null,
    stateAnimationMillis: Int? = null,
    alpha: Float? = null,
    enabled: Boolean = true,
    theme: MillenniumRadioGroupTheme = MillenniumTheme.radioGroupTheme,
    error: Boolean = false,
) {
    val colors = MillenniumTheme.colors
    val properties = theme.states.default
    UnstyledRadioGroup(value, onValueChange, modifier, accessibilityLabel = label) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                groupSpacing ?: properties.groupSpacing
            )
        ) {
            options.forEach { option ->
                val selected = value == option
                val interaction = remember(option) { MutableInteractionSource() }
                val pressed by interaction.collectIsPressedAsState()
                val focused by interaction.collectIsKeyboardFocusedAsState()
                val hovered by interaction.collectIsHoveredAsState()
                val state = millenniumResolveControlState(
                    enabled = enabled,
                    pressed = pressed,
                    focused = focused,
                    hovered = hovered,
                    selected = selected,
                    error = error,
                )
                val itemProperties = theme.states.resolve(state)
                val selectedProperties = theme.states.selected
                fun <V> pick(read: (MillenniumRadioGroupThemeProperties) -> V?): V? =
                    read(itemProperties) ?: if (selected) read(selectedProperties) else null
                val resolvedMillis = stateAnimationMillis ?: itemProperties.stateAnimationMillis
                val animatedAlpha = animateFloatAsState(
                    targetValue = alpha ?: itemProperties.alpha,
                    animationSpec = tween(resolvedMillis),
                ).value
                val targetIndicatorBorderColor =
                    indicatorBorderColor ?: pick { it.indicatorBorderColor } ?: when (state) {
                        MillenniumControlState.Focused -> colors.accent
                        MillenniumControlState.Hovered, MillenniumControlState.Pressed -> colors.accent
                        MillenniumControlState.Selected -> colors.accent
                        MillenniumControlState.Error -> colors.danger
                        else -> colors.border
                    }
                val resolvedIndicatorBorderColor by animateColorAsState(
                    targetValue = targetIndicatorBorderColor,
                    animationSpec = tween(resolvedMillis),
                )
                val indicatorScale by animateFloatAsState(
                    targetValue = when {
                        pressed -> itemProperties.indicatorPressedScale
                        hovered -> itemProperties.indicatorHoverScale
                        else -> 1f
                    },
                    animationSpec = spring(
                        dampingRatio = itemProperties.indicatorScaleDampingRatio,
                        stiffness = itemProperties.indicatorScaleStiffness,
                    ),
                )
                val resolvedLabelColor = labelColor ?: pick { it.labelColor }
                ?: if (state == MillenniumControlState.Disabled) colors.muted else colors.text
                val resolvedSelectedLabelColor =
                    selectedLabelColor ?: itemProperties.selectedLabelColor ?: colors.text
                val animatedLabelColor by animateColorAsState(
                    targetValue = if (selected) resolvedSelectedLabelColor else resolvedLabelColor,
                    animationSpec = tween(resolvedMillis),
                )
                RadioButton(
                    value = option,
                    modifier = Modifier.millenniumActionPointer().heightIn(
                        min = minimumTouchHeight ?: itemProperties.minimumTouchHeight
                    )
                        .alpha(animatedAlpha)
                        .padding(optionPadding ?: itemProperties.optionPadding),
                    // Keep hover feedback on the small control; a row-wide ripple
                    // creates a distracting blue slab behind the option label.
                    indication = null,
                    interactionSource = interaction,
                    enabled = enabled,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            optionSpacing ?: itemProperties.optionSpacing
                        ),
                    ) {
                        Box(
                            Modifier.size(indicatorSize ?: itemProperties.indicatorSize)
                                .graphicsLayer { scaleX = indicatorScale; scaleY = indicatorScale }
                                .millenniumSurface(
                                    background = if (selected) selectedIndicatorBackground
                                        ?: pick { it.selectedIndicatorBackground }
                                        ?: gelBrush(colors.accentTop, colors.accentBottom)
                                    else indicatorBackground ?: itemProperties.indicatorBackground
                                    ?: millenniumBevelBrush(colors, sunken = true),
                                    shape = indicatorShape ?: itemProperties.indicatorShape,
                                    borderColor = resolvedIndicatorBorderColor,
                                    borderWidth = indicatorBorderWidth
                                        ?: itemProperties.indicatorBorderWidth,
                                    shadowElevation = indicatorElevation
                                        ?: itemProperties.indicatorElevation,
                                    shadowColors = itemProperties.indicatorShadows
                                        ?: listOf(Color.Black.copy(alpha = 0.16f)),
                                    innerGlowColor = indicatorGlowColor
                                        ?: pick { it.indicatorGlowColor }
                                        ?: if (selected) colors.highlight.copy(alpha = 0.32f) else null,
                                    bottomRimColor = pick { it.indicatorBottomRimColor },
                                )
                                .millenniumThemedInteraction(
                                    state = state,
                                    shape = indicatorShape ?: itemProperties.indicatorShape,
                                    overlay = pick { it.indicatorInteractionOverlay },
                                    rim = pick { it.indicatorInteractionRim },
                                    tint = pick { it.indicatorInteractionTint },
                                    animationMillis = resolvedMillis,
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            SelectedIndicator(
                                enter = scaleIn(
                                    initialScale = itemProperties.dotEnterScale,
                                    animationSpec = spring(
                                        dampingRatio = itemProperties.dotEnterDampingRatio,
                                        stiffness = Spring.StiffnessMedium,
                                    ),
                                ) + fadeIn(tween(resolvedMillis, easing = FastOutSlowInEasing)),
                                exit = scaleOut(
                                    targetScale = itemProperties.dotExitScale,
                                    animationSpec = tween((resolvedMillis * 0.75f).toInt().coerceAtLeast(70), easing = FastOutLinearInEasing),
                                ) + fadeOut(tween((resolvedMillis * 0.75f).toInt().coerceAtLeast(70), easing = FastOutLinearInEasing)),
                            ) {
                                val resolvedDotBrush = pick { it.dotBrush }
                                val resolvedDotColor = dotColor ?: pick { it.dotColor } ?: colors.onAccent
                                Box(
                                    Modifier.size(dotSize ?: itemProperties.dotSize)
                                        .then(
                                            if (dotColor == null && resolvedDotBrush != null) {
                                                Modifier.background(resolvedDotBrush, itemProperties.dotShape)
                                            } else {
                                                Modifier.background(resolvedDotColor, itemProperties.dotShape)
                                            },
                                        ),
                                )
                            }
                        }
                        CompositionLocalProvider(
                            LocalTextStyle provides LocalTextStyle.current.merge(
                                labelStyle ?: itemProperties.labelStyle
                            )
                                .copy(color = animatedLabelColor),
                        ) { Text(optionLabel(option)) }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumRadioGroupPreview() = MillenniumPreview {
    MillenniumRadioGroup(
        value = "Original",
        onValueChange = {},
        options = listOf("Original", "Remastered", "Hi-fi"),
        label = "Playback quality",
    )
}
