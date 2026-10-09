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
    val colors = MillenniumTheme.colors
    UnstyledModalBottomSheet(
        state = state,
        overlay = { Scrim(scrimColor = colors.scrim, enter = fadeIn(), exit = fadeOut()) },
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            Sheet(
                Modifier.widthIn(max = 640.dp).fillMaxWidth()
                    .beveled(colors, MillenniumTheme.panelShape)
            ) {
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
private fun MillenniumModalBottomSheetPreview() = MillenniumPreview {
    val state = rememberModalBottomSheetState(initialDetent = SheetDetent.FullyExpanded)
    MillenniumModalBottomSheet(state) {
        MillenniumText("Your next favorite track", weight = androidx.compose.ui.text.font.FontWeight.SemiBold)
        MillenniumText("A modal sheet with a soft glass finish.", color = MillenniumTheme.colors.muted)
    }
}
