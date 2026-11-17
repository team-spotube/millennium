package org.spotube.millennium.composables

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

/**
 * Visual and layout properties of a [MillenniumCard].
 *
 * @property background Panel fill; null uses the palette panel brush (pearl in light, smoked zinc in dark).
 * @property foreground Default content colour provided to children; null uses the palette text colour.
 * @property shape Corner shape (12dp rounded by default).
 * @property maxWidth Maximum width of the card (640dp by default); the card still shrinks to fit narrower parents.
 * @property contentPadding Padding between the card edge and its content (20dp by default).
 * @property contentSpacing Vertical spacing between the card's children (16dp by default).
 * @property contentTextStyle Default text style provided to children through [MillenniumStyledContent].
 * @property shadowElevation Depth of the drop shadow (10dp by default).
 * @property shadowColor Shadow colour; null uses black at 14%.
 * @property borderColor Hairline border colour; null uses the palette border colour.
 * @property borderWidth Border thickness (1dp by default).
 * @property innerGlowColor Inner top highlight; null uses the palette highlight at 55%.
 * @property alpha Whole-card opacity.
 * @property bottomRimColor Light rim along the bottom edge; null uses the palette highlight at 50%.
 * @property innerGlowWidth Stroke width of the inner rim (1dp by default).
 * @property insetShadeColor Shade along the inner top perimeter for a recessed card; null draws none.
 * @property shadowColors When non-empty, one shadow layer per colour (stacked) replaces [shadowColor]; null or
 * empty uses [shadowColor].
 * @property interactionOverlay Brush drawn over the fill, below the content, clipped to [shape]. null draws none.
 * Because the card resolves its state from `hovered`, `selected`, `isError` and `enabled`, a theme can give the
 * Hovered, Selected or Error entry its own overlay (gloss, tint, glow) without touching Kotlin.
 * @see MillenniumCardTheme
 * @see defaultMillenniumCardTheme
 */
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
    val innerGlowWidth: Dp = 1.dp,
    val insetShadeColor: Color? = null,
    val shadowColors: List<Color>? = null,
    val interactionOverlay: Brush? = null,
)

/**
 * Per-state theme of [MillenniumCard].
 *
 * @property states Properties per [MillenniumControlState]. [MillenniumCard] resolves them with [cardControlState]:
 * Disabled (`enabled = false`), Error (`isError`), Selected (`selected`), Hovered (pointer over the card) and
 * Default. The card is not clickable, so Pressed and Focused are not resolved.
 */
@Immutable
data class MillenniumCardTheme(
    val states: MillenniumControlStateProperty<MillenniumCardThemeProperties>,
)

/**
 * Resolves the theme state of a [MillenniumCard]: Disabled, then Error, then Selected, then Hovered, else Default.
 */
internal fun cardControlState(
    enabled: Boolean,
    isError: Boolean,
    selected: Boolean,
    hovered: Boolean,
): MillenniumControlState = when {
    !enabled -> MillenniumControlState.Disabled
    isError -> MillenniumControlState.Error
    selected -> MillenniumControlState.Selected
    hovered -> MillenniumControlState.Hovered
    else -> MillenniumControlState.Default
}

/**
 * Creates the default [MillenniumCardTheme] installed as `MillenniumTheme.cardTheme`.
 *
 * Defaults: 12dp rounded pearl/glass panel, 640dp maximum width, 20dp padding, 16dp child spacing, 10dp soft shadow
 * and a 1dp border. Customise with `copy`:
 *
 * ```kotlin
 * val base = defaultMillenniumCardTheme()
 * val compact = base.copy(
 *     states = base.states.copy(default = base.states.default.copy(contentPadding = PaddingValues(12.dp))),
 * )
 * ```
 *
 * @return A fresh theme instance.
 */
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

/**
 * A softly raised pearl/glass panel for grouping related content.
 *
 * The card is a non-interactive column surface with a hairline border, a fading inner highlight, a bottom rim and
 * a soft drop shadow. Children are laid out vertically with the theme's spacing and inherit the card's text style
 * and foreground colour through [MillenniumStyledContent]. Use it to group related controls or information;
 * for clickable rows use `MillenniumListTile`.
 *
 * Example:
 * ```kotlin
 * MillenniumCard(Modifier.fillMaxWidth()) {
 *     MillenniumText("A little nostalgia", weight = FontWeight.SemiBold)
 *     MillenniumText("Pearl surfaces and a fine bevel.", color = MillenniumTheme.colors.muted)
 *     MillenniumButton(onClick = { }) { Text("Play") }
 * }
 * ```
 *
 * @param modifier Modifier applied to the card, before the width cap and surface.
 * @param contentPadding Overrides the inner padding; null uses the theme.
 * @param maxWidth Overrides the maximum width; null uses the theme (640dp).
 * @param shape Overrides the corner shape; null uses the theme.
 * @param background Overrides the fill; null uses the theme, then the palette panel brush.
 * @param borderColor Overrides the border colour; null uses the theme, then the palette border colour.
 * @param borderWidth Overrides the border thickness; null uses the theme.
 * @param shadowElevation Overrides the shadow depth; null uses the theme.
 * @param shadowColor Overrides the shadow colour; null uses the theme, then black at 14%.
 * @param innerGlowColor Overrides the inner highlight; null uses the theme, then the palette highlight.
 * @param bottomRimColor Overrides the bottom rim; null uses the theme, then the palette highlight.
 * @param contentSpacing Overrides the spacing between children; null uses the theme.
 * @param alpha Overrides the opacity; null uses the theme.
 * @param theme Per-state theme; defaults to `MillenniumTheme.cardTheme`.
 * @param enabled When false the card resolves the theme's Disabled state (and ignores hover).
 * @param selected When true the card resolves the theme's Selected state.
 * @param isError When true the card resolves the theme's Error state.
 * @param content Column content of the card.
 * @see MillenniumCardTheme
 * @see MillenniumText
 */
@Composable
fun MillenniumCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues? = null,
    maxWidth: Dp? = null,
    shape: Shape? = null,
    background: Brush? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    shadowElevation: Dp? = null,
    shadowColor: Color? = null,
    innerGlowColor: Color? = null,
    bottomRimColor: Color? = null,
    contentSpacing: Dp? = null,
    alpha: Float? = null,
    theme: MillenniumCardTheme = MillenniumTheme.cardTheme,
    enabled: Boolean = true,
    selected: Boolean = false,
    isError: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val properties = theme.states.resolve(cardControlState(enabled, isError, selected, hovered))
    val cardShape = shape ?: properties.shape
    Column(
        modifier = modifier
            .hoverable(interaction, enabled)
            .widthIn(max = maxWidth ?: properties.maxWidth)
            .millenniumSurface(
                background = background ?: properties.background ?: millenniumPanelBrush(colors),
                shape = cardShape,
                borderColor = borderColor ?: properties.borderColor ?: colors.border,
                borderWidth = borderWidth ?: properties.borderWidth,
                shadowElevation = shadowElevation ?: properties.shadowElevation,
                shadowColor = shadowColor ?: properties.shadowColor ?: Color.Black.copy(alpha = 0.14f),
                shadowColors = properties.shadowColors,
                innerGlowColor = innerGlowColor ?: properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.55f),
                innerGlowWidth = properties.innerGlowWidth,
                insetShadeColor = properties.insetShadeColor,
                bottomRimColor = bottomRimColor ?: properties.bottomRimColor ?: colors.highlight.copy(alpha = 0.5f),
            )
            .millenniumOverlayBrush(properties.interactionOverlay, cardShape)
            .padding(contentPadding ?: properties.contentPadding)
            .alpha(alpha ?: properties.alpha),
        verticalArrangement = Arrangement.spacedBy(contentSpacing ?: properties.contentSpacing),
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
