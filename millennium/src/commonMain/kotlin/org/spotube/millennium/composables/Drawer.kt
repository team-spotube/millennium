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
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumComponentContent
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
    val drawerTheme = MillenniumTheme.drawerTheme
    val colors = MillenniumTheme.colorsFor(drawerTheme)
    val visual = drawerTheme.resolve(
        MillenniumVisualState.Default,
        MillenniumVisualStyle(backgroundColor = colors.surface, foregroundColor = colors.text,
            borderColor = colors.border, borderWidth = drawerTheme.strokes.border,
            shadowElevation = drawerTheme.elevation.card, innerGlowColor = colors.highlight),
    )
    val shape = visual.shape ?: drawerTheme.shapes.panel
    UnstyledDrawer(
        state,
        placement = DrawerPlacement.Start,
        presentation = DrawerPresentation.Modal,
        overlay = { Overlay { Box(Modifier.fillMaxSize().background(colors.scrim)) } },
    ) {
        Viewport(Modifier.fillMaxSize()) {
            Panel(Modifier.widthIn(max = drawerTheme.dimensions.drawerMaxWidth).fillMaxWidth().fillMaxHeight()
                .beveled(colors, shape).componentStyle(visual, shape)) {
                Column(
                    Modifier.padding(visual.contentPadding ?: PaddingValues(drawerTheme.spacing.panelContent)).systemBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(visual.contentSpacing ?: drawerTheme.spacing.xLarge),
                    content = { MillenniumComponentContent(visual) { content() } },
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
        MillenniumText("A softly raised edge drawer.", color = MillenniumTheme.colorsFor(MillenniumTheme.drawerTheme).muted)
    }
}
