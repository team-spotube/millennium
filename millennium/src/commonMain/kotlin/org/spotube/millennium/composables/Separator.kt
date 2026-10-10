package org.spotube.millennium.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import com.composeunstyled.UnstyledHorizontalSeparator
import com.composeunstyled.UnstyledVerticalSeparator
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumText
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MillenniumSeparator(modifier: Modifier = Modifier, vertical: Boolean = false) {
    val separatorTheme = MillenniumTheme.separatorTheme
    val colors = MillenniumTheme.colorsFor(separatorTheme)
    val visual = separatorTheme.resolve(
        MillenniumVisualState.Default,
        MillenniumVisualStyle(foregroundColor = colors.border),
    )
    val color = visual.foregroundColor ?: visual.borderColor ?: colors.border
    val thickness = visual.borderWidth ?: separatorTheme.strokes.separator
    if (vertical) UnstyledVerticalSeparator(color, modifier, thickness)
    else UnstyledHorizontalSeparator(color, modifier, thickness)
}

@Preview
@Composable
private fun MillenniumSeparatorPreview() = MillenniumPreview {
    MillenniumText("Above the line")
    MillenniumSeparator()
    MillenniumText("Below the line")
    Column {
        MillenniumText("Leading edge")
        MillenniumSeparator(Modifier.height(28.dp), vertical = true)
        MillenniumText("Trailing edge")
    }
}
