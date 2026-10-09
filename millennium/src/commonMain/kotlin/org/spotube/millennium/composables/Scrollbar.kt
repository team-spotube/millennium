package org.spotube.millennium.composables

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
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
    val colors = MillenniumTheme.colors
    if (horizontal) UnstyledHorizontalScrollbar(
        state,
        modifier.height(10.dp).beveled(colors, CircleShape, sunken = true)
    ) {
        Thumb(Modifier.fillMaxHeight().beveled(colors, CircleShape))
    } else UnstyledVerticalScrollbar(
        state,
        modifier.width(10.dp).beveled(colors, CircleShape, sunken = true)
    ) {
        Thumb(Modifier.fillMaxWidth().beveled(colors, CircleShape))
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
