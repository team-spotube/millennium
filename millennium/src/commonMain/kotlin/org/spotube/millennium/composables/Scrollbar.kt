package org.spotube.millennium.composables

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumText
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MillenniumScrollbar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    horizontal: Boolean = false
) {
    val state = rememberScrollbarState(scrollState)
    val scrollbarTheme = MillenniumTheme.scrollbarTheme
    val colors = MillenniumTheme.colorsFor(scrollbarTheme)
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val dragged by interaction.collectIsDraggedAsState()
    val visualState = when {
        dragged -> MillenniumVisualState.Clicked
        hovered -> MillenniumVisualState.Hovered
        else -> MillenniumVisualState.Default
    }
    val visual = MillenniumTheme.scrollbarTheme.resolve(visualState,
        MillenniumVisualStyle(backgroundColor = colors.border, foregroundColor = colors.accent,
            borderColor = colors.border, borderWidth = scrollbarTheme.strokes.border))
    val shape = visual.shape ?: scrollbarTheme.shapes.pill
    if (horizontal) UnstyledHorizontalScrollbar(
        state,
        modifier.height(scrollbarTheme.dimensions.scrollbarThickness).beveled(colors, shape, sunken = true)
            .componentStyle(visual, shape),
        interactionSource = interaction,
    ) {
        Thumb(Modifier.fillMaxHeight().beveled(colors, shape).componentStyle(visual, shape))
    } else UnstyledVerticalScrollbar(
        state,
        modifier.width(scrollbarTheme.dimensions.scrollbarThickness).beveled(colors, shape, sunken = true)
            .componentStyle(visual, shape),
        interactionSource = interaction,
    ) {
        Thumb(Modifier.fillMaxWidth().beveled(colors, shape).componentStyle(visual, shape))
    }
}

@Preview
@Composable
private fun MillenniumScrollbarPreview() = MillenniumPreview {
    val scrollState = rememberScrollState()
    Box(Modifier.height(128.dp).verticalScroll(scrollState).padding(end = 16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(6) { index -> MillenniumText("A little nostalgia · ${index + 1}") }
        }
    }
    MillenniumScrollbar(scrollState)
}
