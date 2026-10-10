package org.spotube.millennium.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumPanelBrush

@Immutable
data class MillenniumCardThemeProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val shape: Shape,
    val maxWidth: Dp,
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val contentTextStyle: TextStyle,
    val shadowElevation: Dp,
    val shadowColor: Color? = null,
    val borderColor: Color? = null,
    val borderWidth: Dp,
    val innerGlowColor: Color? = null,
    val alpha: Float = 1f,
    val bottomRimColor: Color? = null,
)

@Immutable
data class MillenniumCardTheme(
    val states: MillenniumControlStateProperty<MillenniumCardThemeProperties>,
)

fun defaultMillenniumCardTheme(): MillenniumCardTheme {
    val base = MillenniumCardThemeProperties(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        maxWidth = 640.dp,
        contentPadding = PaddingValues(20.dp),
        contentSpacing = 16.dp,
        contentTextStyle = TextStyle(fontWeight = FontWeight.Normal),
        shadowElevation = 10.dp,
        borderWidth = 1.dp,
    )
    return MillenniumCardTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(shadowElevation = 10.dp),
            focused = base.copy(borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 4.dp),
            disabled = base.copy(alpha = 0.55f),
            selected = base.copy(borderWidth = 2.dp),
            error = base.copy(borderWidth = 2.dp),
        ),
    )
}

/** A softly raised pearl/glass panel for grouping related content. */
@Composable
fun MillenniumCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.cardTheme.states.resolve(MillenniumControlState.Default)
    Column(
        modifier = modifier
            .widthIn(max = properties.maxWidth)
            .millenniumSurface(
                background = properties.background ?: millenniumPanelBrush(colors),
                shape = properties.shape,
                borderColor = properties.borderColor ?: colors.border,
                borderWidth = properties.borderWidth,
                shadowElevation = properties.shadowElevation,
                shadowColor = properties.shadowColor ?: Color.Black.copy(alpha = 0.14f),
                innerGlowColor = properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.55f),
                bottomRimColor = properties.bottomRimColor ?: colors.highlight.copy(alpha = 0.5f),
            )
            .padding(contentPadding ?: properties.contentPadding)
            .alpha(properties.alpha),
        verticalArrangement = Arrangement.spacedBy(properties.contentSpacing),
        content = {
            MillenniumStyledContent(properties.contentTextStyle, properties.foreground ?: colors.text) { content() }
        },
    )
}

@Preview
@Composable
private fun MillenniumCardPreview() = MillenniumPreview {
    MillenniumCard {
        MillenniumText("A little nostalgia", weight = FontWeight.SemiBold)
        MillenniumText("Pearl surfaces, a fine bevel, and a soft lift.", color = MillenniumTheme.colors.muted)
    }
}
