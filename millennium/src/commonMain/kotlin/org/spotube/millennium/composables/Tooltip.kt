package org.spotube.millennium.composables

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import com.composeunstyled.TooltipHost
import com.composeunstyled.TooltipPanel
import com.composeunstyled.UnstyledTooltip
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumComponentContent
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview
import org.spotube.millennium.composables.MillenniumButton

/** Install once above content that contains [MillenniumTooltip] anchors. */
@Composable
fun MillenniumTooltipHost(modifier: Modifier = Modifier, content: @Composable () -> Unit) =
    TooltipHost(modifier, content)

@Composable
fun MillenniumTooltip(text: String, anchor: @Composable () -> Unit) {
    val tooltipTheme = MillenniumTheme.tooltipTheme
    val colors = MillenniumTheme.colorsFor(tooltipTheme)
    val visual = MillenniumTheme.tooltipTheme.resolve(
        MillenniumVisualState.Default,
        MillenniumVisualStyle(backgroundColor = colors.raised, foregroundColor = colors.text,
            borderColor = colors.border, borderWidth = tooltipTheme.strokes.border,
            shadowElevation = tooltipTheme.elevation.card, innerGlowColor = colors.highlight),
    )
    val shape = visual.shape ?: tooltipTheme.shapes.control
    UnstyledTooltip(hoverDelayMillis = tooltipTheme.motion.tooltipDelayMillis, sideOffset = tooltipTheme.spacing.small, panel = {
        TooltipPanel(
            Modifier.widthIn(max = tooltipTheme.dimensions.tooltipMaxWidth)
                .beveled(colors, shape).componentStyle(visual, shape)
                .padding(visual.contentPadding ?: PaddingValues(tooltipTheme.spacing.medium))
        ) {
            MillenniumComponentContent(visual.copy(textStyle = visual.textStyle ?: tooltipTheme.typography.caption)) {
                Text(text, color = visual.foregroundColor ?: colors.text)
            }
        }
    }, anchor = anchor)
}

@Preview
@Composable
private fun MillenniumTooltipPreview() = MillenniumPreview {
    MillenniumTooltip("A hint of the past. Built for today.") {
        MillenniumButton(onClick = {}) { Text("Hover or focus for a hint") }
    }
}
