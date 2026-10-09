package org.spotube.millennium.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumText


/** A softly raised pearl/glass panel for grouping related content. */
@Composable
fun MillenniumCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.shadow(8.dp, MillenniumTheme.panelShape)
            .beveled(MillenniumTheme.colors, MillenniumTheme.panelShape)
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content,
    )
}

@Preview
@Composable
private fun MillenniumCardPreview() = MillenniumPreview {
    MillenniumCard {
        MillenniumText("A little nostalgia", weight = androidx.compose.ui.text.font.FontWeight.SemiBold)
        MillenniumText("Pearl surfaces, a fine bevel, and a soft lift.", color = MillenniumTheme.colors.muted)
    }
}
