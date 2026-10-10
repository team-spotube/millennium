package org.spotube.millennium.composables

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.DragIndication
import com.composeunstyled.ModalBottomSheetState
import com.composeunstyled.Scrim
import com.composeunstyled.Sheet
import com.composeunstyled.UnstyledModalBottomSheet
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
import com.composeunstyled.rememberModalBottomSheetState

@Composable
fun MillenniumModalBottomSheet(
    state: ModalBottomSheetState,
    content: @Composable ColumnScope.() -> Unit,
) {
    val sheetTheme = MillenniumTheme.modalBottomSheetTheme
    val colors = MillenniumTheme.colorsFor(sheetTheme)
    val visual = sheetTheme.resolve(
        MillenniumVisualState.Default,
        MillenniumVisualStyle(backgroundColor = colors.surface, foregroundColor = colors.text,
            borderColor = colors.border, borderWidth = sheetTheme.strokes.border,
            shadowElevation = sheetTheme.elevation.card, innerGlowColor = colors.highlight),
    )
    val shape = visual.shape ?: sheetTheme.shapes.panel
    UnstyledModalBottomSheet(
        state = state,
        overlay = { Scrim(scrimColor = colors.scrim, enter = fadeIn(), exit = fadeOut()) },
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            Sheet(
                Modifier.widthIn(max = sheetTheme.dimensions.cardMaxWidth).fillMaxWidth()
                    .beveled(colors, shape).componentStyle(visual, shape)
            ) {
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
private fun MillenniumModalBottomSheetPreview() = MillenniumPreview {
    val state = rememberModalBottomSheetState(initialDetent = SheetDetent.FullyExpanded)
    MillenniumModalBottomSheet(state) {
        MillenniumText("Your next favorite track", weight = androidx.compose.ui.text.font.FontWeight.SemiBold)
        MillenniumText("A modal sheet with a soft glass finish.", color = MillenniumTheme.colorsFor(MillenniumTheme.modalBottomSheetTheme).muted)
    }
}
