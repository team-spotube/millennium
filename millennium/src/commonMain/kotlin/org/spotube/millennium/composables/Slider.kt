package org.spotube.millennium.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledSlider
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.gelBrush
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
    val colors = MillenniumTheme.colors
    UnstyledSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth().heightIn(min = 44.dp)
            .alpha(if (enabled) 1f else 0.45f).semantics { contentDescription = label },
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        track = { state ->
            Box(Modifier.fillMaxWidth().height(8.dp).beveled(colors, CircleShape, sunken = true)) {
                Box(
                    Modifier.fillMaxHeight().fillMaxWidth(state.fraction)
                        .background(gelBrush(colors.accentTop, colors.accentBottom))
                )
            }
        },
        thumb = { state ->
            Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                Box(
                    Modifier.size(if (state.isPressed || state.isDragging) 26.dp else 22.dp)
                        .shadow(4.dp, CircleShape).beveled(colors, CircleShape)
                        .border(if (state.isFocused) 2.dp else 1.dp, colors.accent, CircleShape),
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
