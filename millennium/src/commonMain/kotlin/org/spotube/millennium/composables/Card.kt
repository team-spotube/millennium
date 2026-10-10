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
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumCardThemeProperties(
    val colors: MillenniumColors,
    val background: Brush,
    val foreground: Color,
    val shape: Shape,
    val maxWidth: Dp,
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val contentTextStyle: TextStyle,
    val shadowElevation: Dp,
    val shadowColor: Color,
    val borderColor: Color,
    val borderWidth: Dp,
    val innerGlowColor: Color,
    val alpha: Float = 1f,
)

@Immutable
data class MillenniumCardTheme(
    val states: MillenniumControlStateProperty<MillenniumCardThemeProperties>,
)

fun defaultMillenniumCardTheme(colors: MillenniumColors): MillenniumCardTheme {
    val base = MillenniumCardThemeProperties(
        colors = colors,
        background = millenniumBevelBrush(colors),
        foreground = colors.text,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
        maxWidth = 640.dp,
        contentPadding = PaddingValues(20.dp),
        contentSpacing = 16.dp,
        contentTextStyle = TextStyle(fontWeight = FontWeight.Normal),
        shadowElevation = 8.dp,
        shadowColor = Color.Black.copy(alpha = 0.14f),
        borderColor = colors.border,
        borderWidth = 1.dp,
        innerGlowColor = colors.highlight.copy(alpha = 0.55f),
    )
    return MillenniumCardTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(shadowElevation = 10.dp, borderColor = colors.accent),
            focused = base.copy(borderWidth = 2.dp, borderColor = colors.accent),
            pressed = base.copy(shadowElevation = 4.dp),
            disabled = base.copy(alpha = 0.55f),
            selected = base.copy(borderColor = colors.accent),
            error = base.copy(borderColor = colors.danger, borderWidth = 2.dp),
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
    val properties = MillenniumTheme.cardTheme.states.resolve(MillenniumControlState.Default)
    Column(
        modifier = modifier
            .widthIn(max = properties.maxWidth)
            .millenniumSurface(
                background = properties.background,
                shape = properties.shape,
                borderColor = properties.borderColor,
                borderWidth = properties.borderWidth,
                shadowElevation = properties.shadowElevation,
                shadowColor = properties.shadowColor,
                innerGlowColor = properties.innerGlowColor,
            )
            .padding(contentPadding ?: properties.contentPadding)
            .alpha(properties.alpha),
        verticalArrangement = Arrangement.spacedBy(properties.contentSpacing),
        content = {
            MillenniumStyledContent(properties.contentTextStyle, properties.foreground) { content() }
        },
    )
}

@Preview
@Composable
private fun MillenniumCardPreview() = MillenniumPreview {
    MillenniumCard {
        MillenniumText("A little nostalgia", weight = FontWeight.SemiBold)
        MillenniumText("Pearl surfaces, a fine bevel, and a soft lift.", color = MillenniumTheme.cardTheme.states.default.colors.muted)
    }
}
