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
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumComponentContent

@Composable
fun MillenniumDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val dialogTheme = MillenniumTheme.dialogTheme
    val colors = MillenniumTheme.colorsFor(dialogTheme)
    val visual = MillenniumTheme.dialogTheme.resolve(
        MillenniumVisualState.Default,
        MillenniumVisualStyle(backgroundColor = colors.surface, foregroundColor = colors.text,
            borderColor = colors.border, borderWidth = dialogTheme.strokes.border,
            shadowElevation = dialogTheme.elevation.card, innerGlowColor = colors.highlight),
    )
    val shape = visual.shape ?: dialogTheme.shapes.panel
    UnstyledDialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        overlay = { Scrim(scrimColor = colors.scrim, enter = fadeIn(), exit = fadeOut()) },
    ) {
        Box(Modifier.fillMaxSize().padding(dialogTheme.spacing.overlayPadding), contentAlignment = Alignment.Center) {
            DialogPanel(
                modifier.widthIn(max = dialogTheme.dimensions.dialogMaxWidth).fillMaxWidth()
                    .beveled(colors, shape).componentStyle(visual, shape),
                paneTitle = title,
                enter = fadeIn() + scaleIn(initialScale = 0.96f),
                exit = fadeOut(),
            ) {
                MillenniumComponentContent(visual) {
                    Column(
                        Modifier.padding(visual.contentPadding ?: PaddingValues(dialogTheme.spacing.panelContent)),
                        verticalArrangement = Arrangement.spacedBy(visual.contentSpacing ?: dialogTheme.spacing.large),
                    ) {
                        Text(title, style = visual.textStyle ?: dialogTheme.typography.title)
                        content()
                    }
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
