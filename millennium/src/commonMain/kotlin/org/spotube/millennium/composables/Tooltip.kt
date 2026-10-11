package org.spotube.millennium.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import com.composeunstyled.TooltipHost
import com.composeunstyled.TooltipPanel
import com.composeunstyled.UnstyledTooltip
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumPanelBrush

@Immutable
data class MillenniumTooltipThemeProperties(
    val background: Brush? = null,
    val shape: Shape,
    val foreground: Color? = null,
    val textStyle: TextStyle,
    val maxWidth: Dp,
    val padding: PaddingValues,
    val borderColor: Color? = null,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val innerGlowColor: Color? = null,
    val hoverDelayMillis: Long,
    val sideOffset: Dp,
    val alpha: Float,
)

@Immutable
data class MillenniumTooltipTheme(
    val states: MillenniumControlStateProperty<MillenniumTooltipThemeProperties>,
)

fun defaultMillenniumTooltipTheme(): MillenniumTooltipTheme {
    val base = MillenniumTooltipThemeProperties(
        shape = RoundedCornerShape(10.dp),
        textStyle = TextStyle(fontSize = 12.sp, lineHeight = 17.sp),
        maxWidth = 280.dp,
        padding = PaddingValues(12.dp),
        borderWidth = 1.dp,
        shadowElevation = 6.dp,
        hoverDelayMillis = 350,
        sideOffset = 8.dp,
        alpha = 1f,
    )
    return MillenniumTooltipTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base,
            pressed = base.copy(shadowElevation = 3.dp),
            disabled = base.copy(alpha = 0.55f),
            selected = base,
            error = base.copy(borderWidth = 2.dp),
        ),
    )
}

/** Install once above content that contains [MillenniumTooltip] anchors. */
@Composable
fun MillenniumTooltipHost(modifier: Modifier = Modifier, content: @Composable () -> Unit) =
    TooltipHost(modifier, content)

@Composable
fun MillenniumTooltip(
    text: String,
    background: Brush? = null,
    shape: Shape? = null,
    foreground: Color? = null,
    textStyle: TextStyle? = null,
    maxWidth: Dp? = null,
    padding: PaddingValues? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    shadowElevation: Dp? = null,
    innerGlowColor: Color? = null,
    hoverDelayMillis: Long? = null,
    sideOffset: Dp? = null,
    alpha: Float? = null,
    theme: MillenniumTooltipTheme = MillenniumTheme.tooltipTheme,
    anchor: @Composable () -> Unit,
) {
    val properties = theme.states.default
    val colors = MillenniumTheme.colors
    val resolvedBackground = background ?: properties.background ?: millenniumPanelBrush(colors)
    val resolvedBorderColor = borderColor ?: properties.borderColor ?: colors.border
    val resolvedGlowColor = innerGlowColor ?: properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.5f)
    val resolvedForeground = foreground ?: properties.foreground ?: colors.text
    UnstyledTooltip(
        hoverDelayMillis = hoverDelayMillis ?: properties.hoverDelayMillis,
        sideOffset = sideOffset ?: properties.sideOffset,
        panel = {
            TooltipPanel(
                Modifier.widthIn(max = maxWidth ?: properties.maxWidth).millenniumSurface(
                    background = resolvedBackground,
                    shape = shape ?: properties.shape,
                    borderColor = resolvedBorderColor,
                    borderWidth = borderWidth ?: properties.borderWidth,
                    shadowElevation = shadowElevation ?: properties.shadowElevation,
                    innerGlowColor = resolvedGlowColor,
                ).padding(padding ?: properties.padding).alpha(alpha ?: properties.alpha),
            ) {
                Text(text, style = textStyle ?: properties.textStyle, color = resolvedForeground)
            }
        },
        anchor = anchor,
    )
}

@Preview
@Composable
private fun MillenniumTooltipPreview() = MillenniumPreview {
    MillenniumTooltip("A hint of the past. Built for today.") {
        MillenniumButton(onClick = {}) { Text("Hover or focus for a hint") }
    }
}
