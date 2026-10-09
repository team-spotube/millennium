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
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview
import org.spotube.millennium.composables.MillenniumButton

/** Install once above content that contains [MillenniumTooltip] anchors. */
@Composable
fun MillenniumTooltipHost(modifier: Modifier = Modifier, content: @Composable () -> Unit) =
    TooltipHost(modifier, content)

@Composable
fun MillenniumTooltip(text: String, anchor: @Composable () -> Unit) {
    UnstyledTooltip(hoverDelayMillis = 350, sideOffset = 8.dp, panel = {
        TooltipPanel(
            Modifier.widthIn(max = 280.dp).beveled(MillenniumTheme.colors).padding(12.dp)
        ) {
            Text(text, fontSize = 12.sp)
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
