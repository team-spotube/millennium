package org.spotube.millennium.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.LocalTextStyle

/** States understood by every Millennium control. Each component decides how they look. */
enum class MillenniumControlState {
    Default,
    Hovered,
    Focused,
    Pressed,
    Disabled,
    Selected,
    Error,
}

/** A component-owned value for every supported control state. */
@Immutable
data class MillenniumControlStateProperty<T>(
    val default: T,
    val hovered: T = default,
    val focused: T = default,
    val pressed: T = default,
    val disabled: T = default,
    val selected: T = default,
    val error: T = default,
) {
    companion object {
        fun <T> resolveWith(stateCb: (state: MillenniumControlState) -> T) =
            MillenniumControlStateProperty(
                default = stateCb(MillenniumControlState.Default),
                hovered = stateCb(MillenniumControlState.Hovered),
                focused = stateCb(MillenniumControlState.Focused),
                pressed = stateCb(MillenniumControlState.Pressed),
                disabled = stateCb(MillenniumControlState.Disabled),
                selected = stateCb(MillenniumControlState.Selected),
                error = stateCb(MillenniumControlState.Error),
            )

        fun <T> all(value: T) = MillenniumControlStateProperty(value)
    }

    fun resolve(state: MillenniumControlState): T = when (state) {
        MillenniumControlState.Default -> default
        MillenniumControlState.Hovered -> hovered
        MillenniumControlState.Focused -> focused
        MillenniumControlState.Pressed -> pressed
        MillenniumControlState.Disabled -> disabled
        MillenniumControlState.Selected -> selected
        MillenniumControlState.Error -> error
    }
}

/** Shared semantic color roles. Component themes store their own palette value. */
@Immutable
data class MillenniumColors(
    val background: Color,
    val surface: Color,
    val raised: Color,
    val inset: Color,
    val text: Color,
    val muted: Color,
    val border: Color,
    val highlight: Color,
    val accent: Color,
    val accentTop: Color,
    val accentBottom: Color,
    val onAccent: Color,
    val success: Color,
    val danger: Color,
    val scrim: Color,
)

/** Pearl surfaces and clear navy text for light appearances. */
val MillenniumLightColors = MillenniumColors(
    background = Color(0xFFE9EFF5),
    surface = Color(0xFFF8FAFD),
    raised = Color.White,
    inset = Color(0xFFE1E8F0),
    text = Color(0xFF182C43),
    muted = Color(0xFF52667C),
    border = Color(0xFFB3C3D4),
    highlight = Color.White,
    accent = Color(0xFF0064C8),
    accentTop = Color(0xFF3375B5),
    accentBottom = Color(0xFF0051AF),
    onAccent = Color.White,
    success = Color(0xFF257B51),
    danger = Color(0xFFBC334A),
    scrim = Color(0x99101E30),
)

/** Smoked navy surfaces and bright accessible foregrounds for dark appearances. */
val MillenniumDarkColors = MillenniumColors(
    background = Color(0xFF0D1522),
    surface = Color(0xFF182536),
    raised = Color(0xFF26374B),
    inset = Color(0xFF101C2A),
    text = Color(0xFFEAF2FC),
    muted = Color(0xFFA5B7CC),
    border = Color(0xFF43566F),
    highlight = Color(0xFF63768C),
    accent = Color(0xFF79C4FF),
    accentTop = Color(0xFF2F75B9),
    accentBottom = Color(0xFF003E83),
    onAccent = Color.White,
    success = Color(0xFF7AE2AE),
    danger = Color(0xFFFF98A9),
    scrim = Color(0xBB030912),
)

/** Four-stop gel highlight: the hard midpoint is a deliberate 2007 reference. */
fun gelBrush(top: Color, bottom: Color): Brush = Brush.verticalGradient(
    0f to top,
    0.48f to androidx.compose.ui.graphics.lerp(top, bottom, 0.30f),
    0.50f to androidx.compose.ui.graphics.lerp(top, bottom, 0.64f),
    1f to bottom,
)

/** Surface brush used by component-local default themes. */
fun millenniumBevelBrush(
    colors: MillenniumColors,
    accent: Boolean = false,
    sunken: Boolean = false,
): Brush = if (accent) {
    gelBrush(colors.accentTop, colors.accentBottom)
} else {
    Brush.verticalGradient(listOf(if (sunken) colors.inset else colors.raised, colors.surface))
}

/** Applies one complete surface layer, avoiding a second background/bevel pass. */
fun Modifier.millenniumSurface(
    background: Brush,
    shape: Shape,
    borderColor: Color? = null,
    borderWidth: Dp = 0.dp,
    shadowElevation: Dp = 0.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.16f),
    shadowColors: List<Color>? = null,
    innerGlowColor: Color? = null,
    innerGlowWidth: Dp = 1.dp,
    innerGlowInset: Dp = 8.dp,
    innerGlowTop: Dp = 1.dp,
): Modifier {
    var result = this
    if (shadowElevation > 0.dp) {
        val colors = shadowColors?.takeIf { it.isNotEmpty() } ?: listOf(shadowColor)
        colors.forEach { color ->
            result = result.shadow(
                shadowElevation,
                shape,
                ambientColor = color,
                spotColor = color,
            )
        }
    }
    result = result.clip(shape).background(background, shape)
    if (borderColor != null && borderWidth > 0.dp) {
        result = result.border(borderWidth, borderColor, shape)
    }
    if (innerGlowColor != null) {
        result = result.drawWithContent {
            drawContent()
            drawLine(
                color = innerGlowColor,
                start = Offset(innerGlowInset.toPx(), innerGlowTop.toPx()),
                end = Offset(size.width - innerGlowInset.toPx(), innerGlowTop.toPx()),
                strokeWidth = innerGlowWidth.toPx(),
            )
        }
    }
    return result
}

@Composable
internal fun MillenniumStyledContent(
    textStyle: TextStyle,
    foreground: Color,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalTextStyle provides LocalTextStyle.current.merge(textStyle).copy(color = foreground),
        content = content,
    )
}
