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
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumComponentContent


/** A softly raised pearl/glass panel for grouping related content. */
@Composable
fun MillenniumCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val cardTheme = MillenniumTheme.cardTheme
    val colors = MillenniumTheme.colorsFor(cardTheme)
    val visual = cardTheme.resolve(
        MillenniumVisualState.Default,
        MillenniumVisualStyle(
            backgroundColor = colors.surface,
            foregroundColor = colors.text,
            shadowElevation = cardTheme.elevation.card,
            borderColor = colors.border,
            borderWidth = cardTheme.strokes.border,
            innerGlowColor = colors.highlight,
        ),
    )
    val shape = visual.shape ?: cardTheme.shapes.panel
    Column(
        modifier = modifier.shadow(visual.shadowElevation ?: cardTheme.elevation.card, shape)
            .beveled(colors, shape)
            .componentStyle(visual, shape)
            .padding(visual.contentPadding ?: contentPadding ?: PaddingValues(cardTheme.spacing.cardPadding)),
        verticalArrangement = Arrangement.spacedBy(visual.contentSpacing ?: cardTheme.spacing.large),
        content = { MillenniumComponentContent(visual) { content() } },
    )
}

@Preview
@Composable
private fun MillenniumCardPreview() = MillenniumPreview {
    MillenniumCard {
        MillenniumText("A little nostalgia", weight = androidx.compose.ui.text.font.FontWeight.SemiBold)
        MillenniumText("Pearl surfaces, a fine bevel, and a soft lift.", color = MillenniumTheme.colorsFor(MillenniumTheme.cardTheme).muted)
    }
}
