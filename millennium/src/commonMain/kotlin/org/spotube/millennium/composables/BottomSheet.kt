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
    val colors = MillenniumTheme.colors
    UnstyledBottomSheet(state, modifier) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Sheet(Modifier.widthIn(max = 640.dp).fillMaxWidth().beveled(colors, MillenniumTheme.panelShape)) {
                Column(
                    Modifier.padding(24.dp).navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DragIndication(Modifier.size(40.dp, 5.dp).background(colors.muted, CircleShape))
                    content()
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
        MillenniumText("A softly raised sheet with a tactile handle.", color = MillenniumTheme.colors.muted)
    }
}
