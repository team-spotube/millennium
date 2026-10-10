package org.spotube.millennium.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.BottomSheetState
import com.composeunstyled.DragIndication
import com.composeunstyled.Sheet
import com.composeunstyled.UnstyledBottomSheet
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumComponentContent
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumText
import androidx.compose.ui.tooling.preview.Preview
import com.composeunstyled.SheetDetent
import com.composeunstyled.rememberBottomSheetState

@Composable
fun MillenniumBottomSheet(
    state: BottomSheetState,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetTheme = MillenniumTheme.bottomSheetTheme
    val colors = MillenniumTheme.colorsFor(sheetTheme)
    val visual = sheetTheme.resolve(
        MillenniumVisualState.Default,
        MillenniumVisualStyle(backgroundColor = colors.surface, foregroundColor = colors.text,
            borderColor = colors.border, borderWidth = sheetTheme.strokes.border,
            shadowElevation = sheetTheme.elevation.card, innerGlowColor = colors.highlight),
    )
    val shape = visual.shape ?: sheetTheme.shapes.panel
    UnstyledBottomSheet(state, modifier) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Sheet(Modifier.widthIn(max = sheetTheme.dimensions.cardMaxWidth).fillMaxWidth()
                .beveled(colors, shape).componentStyle(visual, shape)) {
                Column(
                    Modifier.padding(visual.contentPadding ?: PaddingValues(sheetTheme.spacing.panelContent)).navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(visual.contentSpacing ?: sheetTheme.spacing.large),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DragIndication(Modifier.size(sheetTheme.dimensions.dragHandle).background(colors.muted, CircleShape))
                    MillenniumComponentContent(visual) { content() }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumBottomSheetPreview() = MillenniumPreview {
    val state = rememberBottomSheetState(initialDetent = SheetDetent.FullyExpanded)
    MillenniumBottomSheet(state) {
        MillenniumText("Hello from down here.", weight = androidx.compose.ui.text.font.FontWeight.SemiBold)
        MillenniumText("A softly raised sheet with a tactile handle.", color = MillenniumTheme.colorsFor(MillenniumTheme.bottomSheetTheme).muted)
    }
}
