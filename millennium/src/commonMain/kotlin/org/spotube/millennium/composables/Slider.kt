package org.spotube.millennium.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledSlider
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MillenniumSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val sliderTheme = MillenniumTheme.sliderTheme
    val colors = MillenniumTheme.colorsFor(sliderTheme)
    val interaction = remember { MutableInteractionSource() }
    val baseVisual = MillenniumVisualStyle(
        backgroundColor = colors.inset,
        borderColor = colors.border,
        borderWidth = sliderTheme.strokes.border,
        shadowElevation = sliderTheme.elevation.thumb,
    )
    val visualState = if (enabled) MillenniumVisualState.Default else MillenniumVisualState.Disabled
    val visual = MillenniumTheme.sliderTheme.resolve(visualState, baseVisual)
    UnstyledSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth().heightIn(min = sliderTheme.dimensions.touchTarget)
            .alpha(visual.alpha ?: 1f).semantics { contentDescription = label },
        enabled = enabled,
        interactionSource = interaction,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        track = { state ->
            Box(Modifier.fillMaxWidth().height(sliderTheme.dimensions.sliderTrackHeight)
                .beveled(colors, sliderTheme.shapes.pill, sunken = true).componentStyle(visual, sliderTheme.shapes.pill)) {
                Box(
                    Modifier.fillMaxHeight().fillMaxWidth(state.fraction)
                        .background(
                            visual.background ?: visual.foregroundColor?.let { androidx.compose.ui.graphics.Brush.verticalGradient(listOf(it, it)) }
                                ?: gelBrush(colors.accentTop, colors.accentBottom),
                            sliderTheme.shapes.pill,
                        )
                )
            }
        },
        thumb = { state ->
            val thumbState = when {
                !enabled -> MillenniumVisualState.Disabled
                state.isDragging || state.isPressed -> MillenniumVisualState.Clicked
                state.isFocused -> MillenniumVisualState.Focused
                else -> MillenniumVisualState.Default
            }
            val thumbVisual = sliderTheme.resolve(thumbState, baseVisual)
            val thumbShape = thumbVisual.shape ?: sliderTheme.shapes.pill
            Box(Modifier.size(sliderTheme.dimensions.touchTarget), contentAlignment = Alignment.Center) {
                Box(
                    Modifier.size(if (state.isPressed || state.isDragging) sliderTheme.dimensions.sliderThumbActive else sliderTheme.dimensions.sliderThumb)
                        .beveled(colors, thumbShape).componentStyle(thumbVisual, thumbShape),
                )
            }
        },
    )
}

@Preview
@Composable
private fun MillenniumSliderPreview() = MillenniumPreview {
    MillenniumSlider(value = 0.68f, onValueChange = {}, label = "Volume")
}
