package org.spotube.millennium.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme

/**
 * Named text styles shared across Millennium components.
 *
 * These are the typographic roles of the design system. [MillenniumText] reads [body]; the other roles are
 * available to callers (for example `MillenniumTheme.textTheme.states.default.title`) and to component themes.
 * All default styles use [FontFamily.SansSerif].
 *
 * @property body Running text, 14sp with 21sp leading; the base style of [MillenniumText].
 * @property label Form and control labels, 14sp Medium with 20sp leading.
 * @property button Button captions, 14sp SemiBold with 20sp leading.
 * @property title Section titles, 22sp SemiBold with 29sp leading.
 * @property headline Large headings, 32sp Bold with 40sp leading.
 * @property caption Secondary and helper text, 12sp with 17sp leading.
 * @property avatar Base style for avatar initials (Bold, no size: the avatar scales the size from its diameter).
 * @property alpha Opacity of [MillenniumText] in this state (1 by default); give the Disabled entry a lower value
 * to dim text that is rendered with `state = MillenniumControlState.Disabled`.
 *
 * Every field is a full [TextStyle], so colour, font family, letter spacing, decoration and text shadow
 * (`TextStyle.shadow`) of each role come from the theme: [MillenniumText] applies the style of its `role` in full.
 * @see MillenniumTextTheme
 * @see defaultMillenniumTextTheme
 */
@Immutable
data class MillenniumTextThemeProperties(
    val body: TextStyle,
    val label: TextStyle,
    val button: TextStyle,
    val title: TextStyle,
    val headline: TextStyle,
    val caption: TextStyle,
    val avatar: TextStyle,
    val alpha: Float = 1f,
)

/**
 * Typography roles of [MillenniumTextThemeProperties] that [MillenniumText] can render.
 *
 * @property Body Running text ([MillenniumTextThemeProperties.body]); the default role.
 * @property Label Form and control labels.
 * @property Button Button captions.
 * @property Title Section titles.
 * @property Headline Large headings.
 * @property Caption Secondary and helper text.
 * @property Avatar Avatar initials style (has no font size of its own).
 */
enum class MillenniumTextRole { Body, Label, Button, Title, Headline, Caption, Avatar }

/**
 * Returns the [TextStyle] of [role].
 *
 * @param role The typography role to look up.
 */
fun MillenniumTextThemeProperties.styleFor(role: MillenniumTextRole): TextStyle = when (role) {
    MillenniumTextRole.Body -> body
    MillenniumTextRole.Label -> label
    MillenniumTextRole.Button -> button
    MillenniumTextRole.Title -> title
    MillenniumTextRole.Headline -> headline
    MillenniumTextRole.Caption -> caption
    MillenniumTextRole.Avatar -> avatar
}

/**
 * Theme of the shared typography scale.
 *
 * @property states Typography per [MillenniumControlState]. The default factory uses
 * [MillenniumControlStateProperty.all], so every state shares the same styles; [MillenniumText] resolves the
 * Default state.
 * @see MillenniumTextThemeProperties
 */
@Immutable
data class MillenniumTextTheme(
    val states: MillenniumControlStateProperty<MillenniumTextThemeProperties>,
)

/**
 * Creates the default [MillenniumTextTheme] installed as `MillenniumTheme.textTheme`.
 *
 * All states share one set of styles (body, label, button, title, headline, caption, avatar) on the platform
 * sans-serif font. Replace roles with `copy` to change the font family or scale:
 *
 * ```kotlin
 * val base = defaultMillenniumTextTheme().states.default
 * val theme = MillenniumTextTheme(
 *     MillenniumControlStateProperty.all(base.copy(body = base.body.copy(fontFamily = FontFamily.Serif))),
 * )
 * MillenniumText("Serif body", theme = theme)
 * ```
 *
 * @return A fresh theme instance.
 */
fun defaultMillenniumTextTheme() = MillenniumTextTheme(
    states = MillenniumControlStateProperty.all(
        MillenniumTextThemeProperties(
            body = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, lineHeight = 21.sp),
            label = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
            button = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
            title = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 22.sp, lineHeight = 29.sp, fontWeight = FontWeight.SemiBold),
            headline = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 32.sp, lineHeight = 40.sp, fontWeight = FontWeight.Bold),
            caption = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 12.sp, lineHeight = 17.sp),
            avatar = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold),
        ),
    ),
)

/**
 * Themed plain text built on Compose Unstyled's `Text`.
 *
 * Use it for ordinary strings in Millennium layouts so they pick up the palette text colour, the body font family
 * and a consistent leading. Colour, size and weight have explicit parameters; everything else comes from [style]
 * or the theme's body style.
 *
 * Leading: unless [lineHeight] is given, the line height is the body style's leading scaled by
 * `size / body.fontSize`, so larger explicit sizes do not produce overlapping lines.
 *
 * Example:
 * ```kotlin
 * MillenniumText("Now playing", size = 22.sp, weight = FontWeight.SemiBold)
 * MillenniumText("Pearl surfaces, a fine bevel.", color = MillenniumTheme.colors.muted)
 * ```
 *
 * @param text The string to render.
 * @param modifier Modifier applied to the text.
 * @param color Text colour; unspecified (the default) uses the role style's colour from the theme when it has one,
 * otherwise the palette text colour.
 * @param size Font size; unspecified (the default) uses the role style's size (14sp for body).
 * @param weight Font weight; null (the default) uses the role style's weight or Normal.
 * @param style Optional style whose line height and font family are used as the base instead of the theme body style.
 * Its colour, size and weight are not applied; use the explicit parameters for those.
 * @param lineHeight Explicit line height; null scales the base leading with [size].
 * @param theme Typography theme; defaults to `MillenniumTheme.textTheme`. The style of [role] in the entry for
 * [state] is applied in full (family, spacing, shadow, decoration, colour...), not just its size and leading.
 * @param role Typography role to render; defaults to [MillenniumTextRole.Body]. [size], [weight] and
 * [lineHeight] default to the role style's own values.
 * @param state Theme state to resolve (Default when omitted); its `alpha` dims the text.
 * @see MillenniumTextTheme
 * @see MillenniumStyledContent
 */
@Composable
fun MillenniumText(
    text: String,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    color: Color = Color.Unspecified,
    size: TextUnit = TextUnit.Unspecified,
    weight: FontWeight? = null,
    style: TextStyle? = null,
    lineHeight: TextUnit? = null,
    theme: MillenniumTextTheme = MillenniumTheme.textTheme,
    role: MillenniumTextRole = MillenniumTextRole.Body,
    state: MillenniumControlState = MillenniumControlState.Default,
) {
    val properties = theme.states.resolve(state)
    val themeStyle = properties.styleFor(role)
    val baseStyle = style ?: themeStyle
    val resolvedSize = if (size.isSpecified) size else baseStyle.fontSize
    val resolvedColor = when {
        color.isSpecified -> color
        style == null && themeStyle.color.isSpecified -> themeStyle.color
        else -> MillenniumTheme.colors.text
    }
    Text(
        text = text,
        modifier = if (properties.alpha < 1f) modifier.alpha(properties.alpha) else modifier,
        style = baseStyle,
        color = resolvedColor,
        fontSize = resolvedSize,
        lineHeight = lineHeight ?: millenniumTextLineHeight(resolvedSize, baseStyle.fontSize, baseStyle.lineHeight),
        fontWeight = weight ?: baseStyle.fontWeight ?: FontWeight.Normal,
        fontFamily = baseStyle.fontFamily,
    )
}

/**
 * Scales the configured leading with an explicit font size instead of letting lines overlap.
 *
 * Returns `size * baseLineHeight / baseSize` when all three values are in `sp` and [baseSize] is positive;
 * otherwise returns [baseLineHeight] unchanged.
 *
 * @param size The font size actually used for the text.
 * @param baseSize The font size the base leading was designed for.
 * @param baseLineHeight The base style's line height.
 * @return The line height to use for [size].
 */
internal fun millenniumTextLineHeight(size: TextUnit, baseSize: TextUnit, baseLineHeight: TextUnit): TextUnit =
    if (size.isSp && baseSize.isSp && baseLineHeight.isSp && baseSize.value > 0f) {
        (size.value * baseLineHeight.value / baseSize.value).sp
    } else baseLineHeight
