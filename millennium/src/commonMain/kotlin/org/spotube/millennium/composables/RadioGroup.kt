package org.spotube.millennium.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import com.composeunstyled.LocalTextStyle
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun <T> MillenniumRadioGroup(
    value: T,
    onValueChange: (T) -> Unit,
    options: List<T>,
    label: String,
    modifier: Modifier = Modifier,
    optionLabel: (T) -> String = { it.toString() },
) {
    val radioTheme = MillenniumTheme.radioGroupTheme
    val colors = MillenniumTheme.colorsFor(radioTheme)
    val groupVisual = radioTheme.resolve(MillenniumVisualState.Default)
    UnstyledRadioGroup(value, onValueChange, modifier, accessibilityLabel = label) {
        Column(verticalArrangement = Arrangement.spacedBy(groupVisual.contentSpacing ?: radioTheme.spacing.xSmall)) {
            options.forEach { option ->
                val selected = value == option
                val interaction = remember(option) { MutableInteractionSource() }
                val pressed by interaction.collectIsPressedAsState()
                val focused by interaction.collectIsFocusedAsState()
                val hovered by interaction.collectIsHoveredAsState()
                val state = when {
                    pressed -> MillenniumVisualState.Clicked
                    focused -> MillenniumVisualState.Focused
                    hovered -> MillenniumVisualState.Hovered
                    selected -> MillenniumVisualState.Selected
                    else -> MillenniumVisualState.Default
                }
                val visual = MillenniumTheme.radioGroupTheme.resolve(
                    state,
                    MillenniumVisualStyle(
                        background = if (selected) gelBrush(colors.accentTop, colors.accentBottom) else null,
                        foregroundColor = colors.text,
                        borderColor = colors.border,
                        borderWidth = radioTheme.strokes.border,
                    ),
                )
                RadioButton(
                    value = option,
                    modifier = Modifier.heightIn(min = radioTheme.dimensions.touchTarget)
                        .componentStyle(visual, radioTheme.shapes.control),
                    indication = LocalIndication.current,
                    interactionSource = interaction,
                ) {
                    Row(
                        modifier = Modifier.padding(visual.contentPadding ?: PaddingValues(0.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(visual.contentSpacing ?: radioTheme.spacing.medium)
                    ) {
                        Box(
                            Modifier.size(radioTheme.dimensions.checkboxSize)
                                .beveled(colors, visual.shape ?: radioTheme.shapes.radio, accent = selected)
                                .componentStyle(visual, visual.shape ?: radioTheme.shapes.radio),
                            contentAlignment = Alignment.Center
                        ) {
                            SelectedIndicator {
                                Box(Modifier.size(radioTheme.dimensions.radioDotSize).background(visual.foregroundColor ?: colors.onAccent, CircleShape))
                            }
                        }
                        CompositionLocalProvider(
                            LocalTextStyle provides (visual.textStyle ?: radioTheme.typography.label)
                                .copy(color = visual.foregroundColor ?: colors.text),
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
