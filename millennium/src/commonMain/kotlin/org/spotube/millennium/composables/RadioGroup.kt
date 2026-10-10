package org.spotube.millennium.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

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
)

@Immutable
data class MillenniumRadioGroupTheme(
    val states: MillenniumControlStateProperty<MillenniumRadioGroupThemeProperties>,
)

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
        labelStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal),
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

@Composable
fun <T> MillenniumRadioGroup(
    value: T,
    onValueChange: (T) -> Unit,
    options: List<T>,
    label: String,
    modifier: Modifier = Modifier,
    optionLabel: (T) -> String = { it.toString() },
) {
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.radioGroupTheme.states.default
    UnstyledRadioGroup(value, onValueChange, modifier, accessibilityLabel = label) {
        Column(verticalArrangement = Arrangement.spacedBy(properties.groupSpacing)) {
            options.forEach { option ->
                val selected = value == option
                val interaction = remember(option) { MutableInteractionSource() }
                val pressed by interaction.collectIsPressedAsState()
                val focused by interaction.collectIsFocusedAsState()
                val hovered by interaction.collectIsHoveredAsState()
                val state = when {
                    pressed -> MillenniumControlState.Pressed
                    focused -> MillenniumControlState.Focused
                    hovered -> MillenniumControlState.Hovered
                    selected -> MillenniumControlState.Selected
                    else -> MillenniumControlState.Default
                }
                val itemProperties = MillenniumTheme.radioGroupTheme.states.resolve(state)
                val animatedAlpha = animateFloatAsState(
                    targetValue = itemProperties.alpha,
                    animationSpec = tween(itemProperties.stateAnimationMillis),
                ).value
                val indicatorBorderColor = itemProperties.indicatorBorderColor ?: when (state) {
                    MillenniumControlState.Focused -> colors.accent
                    MillenniumControlState.Hovered -> colors.border
                    MillenniumControlState.Selected -> colors.accent
                    MillenniumControlState.Error -> colors.danger
                    else -> colors.border
                }
                val labelColor = itemProperties.labelColor
                    ?: if (state == MillenniumControlState.Disabled) colors.muted else colors.text
                val selectedLabelColor = itemProperties.selectedLabelColor ?: colors.text
                RadioButton(
                    value = option,
                    modifier = Modifier.heightIn(min = itemProperties.minimumTouchHeight)
                        .alpha(animatedAlpha)
                        .padding(itemProperties.optionPadding),
                    // Keep hover feedback on the small control; a row-wide ripple
                    // creates a distracting blue slab behind the option label.
                    indication = null,
                    interactionSource = interaction,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(itemProperties.optionSpacing),
                    ) {
                        Box(
                            Modifier.size(itemProperties.indicatorSize).millenniumSurface(
                                background = if (selected) itemProperties.selectedIndicatorBackground
                                    ?: gelBrush(colors.accentTop, colors.accentBottom)
                                else itemProperties.indicatorBackground ?: millenniumBevelBrush(colors, sunken = true),
                                shape = itemProperties.indicatorShape,
                                borderColor = indicatorBorderColor,
                                borderWidth = itemProperties.indicatorBorderWidth,
                                shadowElevation = itemProperties.indicatorElevation,
                                innerGlowColor = itemProperties.indicatorGlowColor
                                    ?: if (selected) colors.highlight.copy(alpha = 0.32f) else null,
                            ),
                            contentAlignment = Alignment.Center,
                        ) {
                            SelectedIndicator {
                                Box(Modifier.size(itemProperties.dotSize).background(itemProperties.dotColor ?: colors.onAccent, CircleShape))
                            }
                        }
                        CompositionLocalProvider(
                            LocalTextStyle provides LocalTextStyle.current.merge(itemProperties.labelStyle)
                                .copy(color = if (selected) selectedLabelColor else labelColor),
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
