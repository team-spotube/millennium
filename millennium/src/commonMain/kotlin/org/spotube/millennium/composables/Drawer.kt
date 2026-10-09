package org.spotube.millennium.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.LaunchedEffect

enum class MillenniumDrawerValue { Closed, Open }

@Composable
fun rememberMillenniumDrawerState(): UnstyledDrawerState<MillenniumDrawerValue> = remember {
    UnstyledDrawerState(
        initialValue = MillenniumDrawerValue.Closed,
        snapPoints = DrawerSnapPoints {
            MillenniumDrawerValue.Closed at DrawerSnapPoint.Zero
            MillenniumDrawerValue.Open at DrawerSnapPoint.ContentSize
        },
    )
}

@Composable
fun MillenniumDrawer(
    state: UnstyledDrawerState<MillenniumDrawerValue>,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    UnstyledDrawer(
        state,
        placement = DrawerPlacement.Start,
        presentation = DrawerPresentation.Modal,
        overlay = { Overlay { Box(Modifier.fillMaxSize().background(colors.scrim)) } },
    ) {
        Viewport(Modifier.fillMaxSize()) {
            Panel(Modifier.widthIn(max = 320.dp).fillMaxWidth().fillMaxHeight().beveled(colors, MillenniumTheme.panelShape)) {
                Column(
                    Modifier.padding(24.dp).systemBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    content = content,
                )
            }
        }
    }
}


@Preview
@Composable
private fun MillenniumDrawerPreview() = MillenniumPreview {
    val state = rememberMillenniumDrawerState()
    LaunchedEffect(state) { state.targetValue = MillenniumDrawerValue.Open }
    MillenniumDrawer(state) {
        MillenniumText("A little more space", weight = androidx.compose.ui.text.font.FontWeight.SemiBold)
        MillenniumText("A softly raised edge drawer.", color = MillenniumTheme.colors.muted)
    }
}
