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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.LocalContentColor
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

/** Convex Aqua gel: bright cap, crisp reflection break and a luminous lower lip. */
fun gelBrush(top: Color, bottom: Color): Brush = Brush.verticalGradient(
    *millenniumGelStops(top, bottom).toTypedArray(),
)

/** Every stop stays between the endpoints, preserving the tested label contrast. */
internal fun millenniumGelStops(top: Color, bottom: Color): List<Pair<Float, Color>> = listOf(
    0f to lerp(top, bottom, 0.18f),
    0.08f to top,
    0.46f to lerp(top, bottom, 0.06f),
    0.49f to lerp(top, bottom, 0.70f),
    0.80f to bottom,
    1f to lerp(bottom, top, 0.32f),
)

/**
 * A lighter, surface-blended version of the control gel. Used for selected rows
 * (e.g. [org.spotube.millennium.composables.MillenniumListTile]) that want a quieter
 * selection than a primary button's full-strength gel.
 */
fun millenniumSelectedGelBrush(colors: MillenniumColors, strength: Float = 0.35f): Brush =
    Brush.verticalGradient(*millenniumSelectedGelStops(colors, strength).toTypedArray())

/** The individual stops behind [millenniumSelectedGelBrush], exposed for contrast tests. */
internal fun millenniumSelectedGelStops(colors: MillenniumColors, strength: Float = 0.35f): List<Pair<Float, Color>> {
    val amount = strength.coerceIn(0f, 1f)
    return millenniumGelStops(colors.accentTop, colors.accentBottom)
        .map { (stop, color) -> stop to lerp(colors.surface, color, amount) }
}

/** Surface brush used by component-local default themes. */
fun millenniumBevelBrush(
    colors: MillenniumColors,
    accent: Boolean = false,
    sunken: Boolean = false,
): Brush = if (accent) {
    gelBrush(colors.accentTop, colors.accentBottom)
} else {
    millenniumNeutralBrush(colors, sunken)
}

/** Neutral stops stay within tested palette surfaces; no white wash over labels. */
internal fun millenniumNeutralStops(colors: MillenniumColors, sunken: Boolean): List<Color> =
    if (sunken) listOf(colors.inset, lerp(colors.inset, colors.surface, 0.55f), colors.surface)
    else listOf(colors.raised, colors.surface, lerp(colors.surface, colors.inset, 0.65f))

private fun millenniumNeutralBrush(colors: MillenniumColors, sunken: Boolean): Brush {
    val stops = millenniumNeutralStops(colors, sunken)
    return if (sunken) Brush.verticalGradient(0f to stops[0], 0.18f to stops[1], 1f to stops[2])
    else Brush.verticalGradient(
        0f to stops[0],
        0.47f to stops[1],
        0.50f to stops[2],
        1f to stops[1],
    )
}

/** Pearl enamel / smoked glass panel, distinct from the stronger control gel. */
fun millenniumPanelBrush(colors: MillenniumColors): Brush = Brush.verticalGradient(
    0f to colors.raised,
    0.12f to lerp(colors.raised, colors.surface, 0.45f),
    0.22f to colors.surface,
    0.85f to colors.surface,
    1f to lerp(colors.surface, colors.inset, 0.35f),
)

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
    innerGlowFollowsShape: Boolean = true,
    bottomRimColor: Color? = null,
    insetShadeColor: Color? = null,
    orbColor: Color? = null,
    orbBottomFraction: Float = 0.55f,
    orbDepthFraction: Float = 0.16f,
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
    if (innerGlowColor != null || bottomRimColor != null || insetShadeColor != null || orbColor != null) {
        result = result.drawWithContent {
            // The Aqua glass orb sits above the fill but below the content: a hazy
            // top dome with a curved meniscus edge, a bright inner top rim and a
            // soft reflection along the lower rim.
            if (orbColor != null) {
                val domeBottom = size.height * orbBottomFraction
                val domeDepth = size.height * orbDepthFraction
                val dome = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width, 0f)
                    lineTo(size.width, domeBottom)
                    quadraticTo(size.width / 2f, domeBottom + domeDepth, 0f, domeBottom)
                    close()
                }
                drawPath(
                    path = dome,
                    brush = Brush.verticalGradient(
                        0f to orbColor,
                        0.78f to orbColor.copy(alpha = orbColor.alpha * 0.82f),
                        0.95f to orbColor.copy(alpha = orbColor.alpha * 0.3f),
                        1f to Color.Transparent,
                        startY = 0f,
                        endY = domeBottom,
                    ),
                )
                val minSide = size.minDimension
                val arcInset = minSide * 0.10f
                drawArc(
                    color = orbColor.copy(alpha = (orbColor.alpha + 0.3f).coerceAtMost(1f)),
                    startAngle = 200f,
                    sweepAngle = 140f,
                    useCenter = false,
                    topLeft = Offset(arcInset, arcInset),
                    size = Size(size.width - arcInset * 2f, size.height - arcInset * 2f),
                    style = Stroke(width = minSide * 0.045f),
                )
                val reflectionInset = minSide * 0.08f
                drawArc(
                    color = orbColor.copy(alpha = orbColor.alpha * 0.55f),
                    startAngle = 25f,
                    sweepAngle = 130f,
                    useCenter = false,
                    topLeft = Offset(reflectionInset, reflectionInset),
                    size = Size(size.width - reflectionInset * 2f, size.height - reflectionInset * 2f),
                    style = Stroke(width = minSide * 0.06f),
                )
            }
            drawContent()
            val stroke = innerGlowWidth.toPx()
            val edgeInset = maxOf(borderWidth.toPx(), innerGlowTop.toPx()) + stroke / 2f
            // Shade only the inner perimeter, never the input's label or value.
            if (insetShadeColor != null && size.minDimension > edgeInset * 4f) {
                inset(edgeInset) {
                    drawOutline(
                        shape.createOutline(size, layoutDirection, this),
                        brush = Brush.verticalGradient(0f to insetShadeColor, 0.4f to Color.Transparent),
                        style = Stroke(3.dp.toPx()),
                    )
                }
            }
            if (innerGlowFollowsShape && stroke > 0f && size.minDimension > edgeInset * 2f) {
                // A fine inner rim follows rounded corners instead of a floating
                // horizontal line. It fades before the lower edge, keeping the
                // highlight off the label and avoiding a second surface layer.
                inset(edgeInset) {
                    drawOutline(
                        outline = shape.createOutline(size, layoutDirection, this),
                        brush = Brush.verticalGradient(
                            0f to (innerGlowColor ?: Color.Transparent),
                            0.3f to (innerGlowColor?.copy(alpha = innerGlowColor.alpha * 0.35f) ?: Color.Transparent),
                            0.65f to Color.Transparent,
                            1f to (bottomRimColor ?: Color.Transparent),
                        ),
                        style = Stroke(stroke),
                    )
                }
            } else if (innerGlowColor != null && !innerGlowFollowsShape && size.width > innerGlowInset.toPx() * 2f) {
                drawLine(
                    color = innerGlowColor,
                    start = Offset(innerGlowInset.toPx(), innerGlowTop.toPx()),
                    end = Offset(size.width - innerGlowInset.toPx(), innerGlowTop.toPx()),
                    strokeWidth = stroke,
                )
            }
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
        LocalContentColor provides foreground,
        content = content,
    )
}
