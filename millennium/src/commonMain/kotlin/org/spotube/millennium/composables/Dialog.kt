package org.spotube.millennium.composables

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview
import org.spotube.millennium.theme.MillenniumText

@Composable
fun MillenniumDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    UnstyledDialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        overlay = { Scrim(scrimColor = colors.scrim, enter = fadeIn(), exit = fadeOut()) },
    ) {
        Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
            DialogPanel(
                modifier.widthIn(max = 480.dp).fillMaxWidth()
                    .beveled(colors, MillenniumTheme.panelShape),
                paneTitle = title,
                enter = fadeIn() + scaleIn(initialScale = 0.96f),
                exit = fadeOut(),
            ) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(title, fontSize = 22.sp)
                    content()
                }
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumDialogPreview() = MillenniumPreview {
    MillenniumDialog(visible = true, onDismissRequest = {}, title = "A little update") {
        MillenniumText("A familiar message panel, dressed for today.")
    }
}
