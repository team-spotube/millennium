package org.spotube.millennium.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme

@Immutable
data class MillenniumTextThemeProperties(
    val body: TextStyle,
    val label: TextStyle,
    val button: TextStyle,
    val title: TextStyle,
    val headline: TextStyle,
    val caption: TextStyle,
    val avatar: TextStyle,
)

@Immutable
data class MillenniumTextTheme(
    val states: MillenniumControlStateProperty<MillenniumTextThemeProperties>,
)

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

@Composable
fun MillenniumText(
    text: String,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    color: Color = MillenniumTheme.colors.text,
    size: TextUnit = MillenniumTheme.textTheme.states.default.body.fontSize,
    weight: FontWeight = MillenniumTheme.textTheme.states.default.body.fontWeight ?: FontWeight.Normal,
) {
    val style = MillenniumTheme.textTheme.states.resolve(MillenniumControlState.Default).body
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = size,
        lineHeight = millenniumTextLineHeight(size, style.fontSize, style.lineHeight),
        fontWeight = weight,
        fontFamily = style.fontFamily,
    )
}

/** Scale the configured leading with explicit font sizes rather than overlapping lines. */
internal fun millenniumTextLineHeight(size: TextUnit, baseSize: TextUnit, baseLineHeight: TextUnit): TextUnit =
    if (size.isSp && baseSize.isSp && baseLineHeight.isSp && baseSize.value > 0f) {
        (size.value * baseLineHeight.value / baseSize.value).sp
    } else baseLineHeight
