package org.spotube.millennium.theme

import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.Text
import com.composeunstyled.theme.*

/** Semantic palette; all surfaces and foregrounds have explicit light and dark counterparts. */
@Immutable
data class MillenniumColors(
    val background: Color, val surface: Color, val raised: Color, val inset: Color,
    val text: Color, val muted: Color, val border: Color, val highlight: Color,
    val accent: Color, val accentTop: Color, val accentBottom: Color, val onAccent: Color,
    val success: Color, val danger: Color, val scrim: Color,
)

val MillenniumLightColors = MillenniumColors(
    Color(0xFFE9EFF5), Color(0xFFF8FAFD), Color.White, Color(0xFFE1E8F0),
    Color(0xFF182C43), Color(0xFF52667C), Color(0xFFB3C3D4), Color.White,
    Color(0xFF0064C8), Color(0xFF3375B5), Color(0xFF0051AF), Color.White,
    Color(0xFF257B51), Color(0xFFBC334A), Color(0x99101E30),
)
val MillenniumDarkColors = MillenniumColors(
    Color(0xFF0D1522), Color(0xFF182536), Color(0xFF26374B), Color(0xFF101C2A),
    Color(0xFFEAF2FC), Color(0xFFA5B7CC), Color(0xFF43566F), Color(0xFF63768C),
    Color(0xFF79C4FF), Color(0xFF2F75B9), Color(0xFF003E83), Color.White,
    Color(0xFF7AE2AE), Color(0xFFFF98A9), Color(0xBB030912),
)

private val paletteProperty = ThemeProperty<MillenniumColors>("millennium.palette")
private val paletteToken = ThemeToken<MillenniumColors>("palette")
private val UnstyledMillenniumTheme = buildThemeV2 {
    name = "Millennium / Aero refined"
    colorSchemeTransitionSpec = tween(180)
    colorScheme(ColorScheme.Light) {
        properties[paletteProperty] = mapOf(paletteToken to MillenniumLightColors)
    }
    colorScheme(ColorScheme.Dark) {
        properties[paletteProperty] = mapOf(paletteToken to MillenniumDarkColors)
    }
}

object MillenniumTheme {
    val colors: MillenniumColors @Composable get() = Theme[paletteProperty][paletteToken]
    val shape = RoundedCornerShape(10.dp)
    val panelShape = RoundedCornerShape(18.dp)
}

/** Install once at the app root; nested themes are supported for previews. */
@Composable
fun MillenniumTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    UnstyledMillenniumTheme(colorScheme = if (dark) ColorScheme.Dark else ColorScheme.Light) {
        val colors = MillenniumTheme.colors
        val indication: Indication = remember(colors) {
            ColoredIndication(
                colors.accent.copy(alpha = 0.10f),
                colors.accent.copy(alpha = 0.16f),
                colors.accent.copy(alpha = 0.24f),
                colors.accent.copy(alpha = 0.16f),
                tween(90),
                tween(120),
            )
        }
        CompositionLocalProvider(
            LocalIndication provides indication,
            LocalTextStyle provides TextStyle(
                color = MillenniumTheme.colors.text, fontFamily = FontFamily.SansSerif,
                fontSize = 14.sp, lineHeight = 21.sp,
            ),
        ) { content() }
    }
}

@Composable
fun MillenniumText(
    text: String, modifier: Modifier = Modifier, color: Color = MillenniumTheme.colors.text,
    size: TextUnit = 14.sp, weight: FontWeight = FontWeight.Normal,
) = Text(
    text,
    modifier = modifier,
    color = color,
    fontSize = size,
    lineHeight = size * 1.5f,
    fontWeight = weight
)

/** Shared light-theme frame for the component previews; keep preview setup in one place. */
@Composable
fun MillenniumPreview(content: @Composable () -> Unit) {
    MillenniumTheme(dark = false) {
        Box(
            Modifier.fillMaxWidth()
                .background(MillenniumTheme.colors.background)
                .padding(20.dp),
        ) {
            Column(Modifier.widthIn(max = 480.dp)) { content() }
        }
    }
}

@Preview
@Composable
private fun MillenniumDefaultPreview() {
    MillenniumPreview {
        MillenniumText("Millennium preview", size = 22.sp, weight = FontWeight.SemiBold)
        MillenniumText("Shared light palette · Aero-inspired surfaces", color = MillenniumTheme.colors.muted)
    }
}

/** Four-stop gel highlight: the hard midpoint is a deliberate 2007 reference. */
fun gelBrush(top: Color, bottom: Color): Brush = Brush.verticalGradient(
    0f to top, 0.48f to lerp(top, bottom, 0.30f),
    0.50f to lerp(top, bottom, 0.64f), 1f to bottom,
)

internal fun Modifier.beveled(
    colors: MillenniumColors, shape: Shape = MillenniumTheme.shape,
    accent: Boolean = false, sunken: Boolean = false,
): Modifier = clip(shape).background(
        if (accent) gelBrush(colors.accentTop, colors.accentBottom)
        else Brush.verticalGradient(
            listOf(
                if (sunken) colors.inset else colors.raised, colors.surface
            )
        )
    ).border(1.dp, if (accent) colors.accentBottom else colors.border, shape).drawWithContent {
        drawContent()
        drawLine(
            colors.highlight.copy(alpha = if (accent) 0.5f else 0.35f),
            Offset(8.dp.toPx(), 1.5.dp.toPx()),
            Offset(size.width - 8.dp.toPx(), 1.5.dp.toPx()),
            1.dp.toPx()
        )
    }
