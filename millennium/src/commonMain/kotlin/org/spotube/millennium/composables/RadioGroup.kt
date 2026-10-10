package org.spotube.millennium.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
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
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumRadioGroupThemeProperties(
    val colors: MillenniumColors,
    val indicatorBackground: Brush,
    val selectedIndicatorBackground: Brush,
    val indicatorShape: Shape,
    val indicatorSize: DpSize,
    val dotSize: Dp,
    val dotColor: Color,
    val indicatorBorderColor: Color,
    val indicatorBorderWidth: Dp,
    val minimumTouchHeight: Dp,
    val optionPadding: PaddingValues,
    val optionSpacing: Dp,
    val groupSpacing: Dp,
    val labelStyle: TextStyle,
    val labelColor: Color,
    val selectedLabelColor: Color,
    val stateAnimationMillis: Int,
    val alpha: Float = 1f,
)

@Immutable
data class MillenniumRadioGroupTheme(
    val states: MillenniumControlStateProperty<MillenniumRadioGroupThemeProperties>,
)

fun defaultMillenniumRadioGroupTheme(colors: MillenniumColors): MillenniumRadioGroupTheme {
    val base = MillenniumRadioGroupThemeProperties(
        colors = colors,
        indicatorBackground = millenniumBevelBrush(colors, sunken = true),
        selectedIndicatorBackground = gelBrush(colors.accentTop, colors.accentBottom),
        indicatorShape = RoundedCornerShape(50),
        indicatorSize = DpSize(22.dp, 22.dp),
        dotSize = 8.dp,
        dotColor = colors.onAccent,
        indicatorBorderColor = colors.border,
        indicatorBorderWidth = 1.dp,
        minimumTouchHeight = 44.dp,
        optionPadding = PaddingValues(0.dp),
        optionSpacing = 12.dp,
        groupSpacing = 4.dp,
        labelStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal),
        labelColor = colors.text,
        selectedLabelColor = colors.text,
        stateAnimationMillis = 120,
    )
    return MillenniumRadioGroupTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(indicatorBorderColor = colors.accent, alpha = 0.96f),
            focused = base.copy(indicatorBorderColor = colors.accent, indicatorBorderWidth = 2.dp),
            pressed = base.copy(alpha = 0.90f),
            disabled = base.copy(alpha = 0.45f, labelColor = colors.muted),
            selected = base.copy(indicatorBorderColor = colors.accent),
            error = base.copy(indicatorBorderColor = colors.danger, indicatorBorderWidth = 2.dp),
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
                RadioButton(
                    value = option,
                    modifier = Modifier.heightIn(min = itemProperties.minimumTouchHeight)
                        .alpha(animatedAlpha)
                        .padding(itemProperties.optionPadding),
                    indication = LocalIndication.current,
                    interactionSource = interaction,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(itemProperties.optionSpacing),
                    ) {
                        Box(
                            Modifier.size(itemProperties.indicatorSize).millenniumSurface(
                                background = if (selected) itemProperties.selectedIndicatorBackground else itemProperties.indicatorBackground,
                                shape = itemProperties.indicatorShape,
                                borderColor = itemProperties.indicatorBorderColor,
                                borderWidth = itemProperties.indicatorBorderWidth,
                            ),
                            contentAlignment = Alignment.Center,
                        ) {
                            SelectedIndicator {
                                Box(Modifier.size(itemProperties.dotSize).background(itemProperties.dotColor, CircleShape))
                            }
                        }
                        CompositionLocalProvider(
                            LocalTextStyle provides LocalTextStyle.current.merge(itemProperties.labelStyle)
                                .copy(color = if (selected) itemProperties.selectedLabelColor else itemProperties.labelColor),
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
