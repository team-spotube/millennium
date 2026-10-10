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
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumTooltipThemeProperties(
    val colors: MillenniumColors,
    val background: Brush,
    val shape: Shape,
    val foreground: Color,
    val textStyle: TextStyle,
    val maxWidth: Dp,
    val padding: PaddingValues,
    val borderColor: Color,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val innerGlowColor: Color,
    val hoverDelayMillis: Long,
    val sideOffset: Dp,
    val alpha: Float,
)

@Immutable
data class MillenniumTooltipTheme(
    val states: MillenniumControlStateProperty<MillenniumTooltipThemeProperties>,
)

fun defaultMillenniumTooltipTheme(colors: MillenniumColors): MillenniumTooltipTheme {
    val base = MillenniumTooltipThemeProperties(
        colors = colors,
        background = millenniumBevelBrush(colors),
        shape = RoundedCornerShape(10.dp),
        foreground = colors.text,
        textStyle = TextStyle(fontSize = 12.sp, lineHeight = 17.sp),
        maxWidth = 280.dp,
        padding = PaddingValues(12.dp),
        borderColor = colors.border,
        borderWidth = 1.dp,
        shadowElevation = 6.dp,
        innerGlowColor = colors.highlight.copy(alpha = 0.5f),
        hoverDelayMillis = 350,
        sideOffset = 8.dp,
        alpha = 1f,
    )
    return MillenniumTooltipTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(borderColor = colors.accent),
            focused = base.copy(borderColor = colors.accent),
            pressed = base.copy(shadowElevation = 3.dp),
            disabled = base.copy(alpha = 0.55f),
            selected = base.copy(borderColor = colors.accent),
            error = base.copy(borderColor = colors.danger, borderWidth = 2.dp),
        ),
    )
}

/** Install once above content that contains [MillenniumTooltip] anchors. */
@Composable
fun MillenniumTooltipHost(modifier: Modifier = Modifier, content: @Composable () -> Unit) =
    TooltipHost(modifier, content)

@Composable
fun MillenniumTooltip(text: String, anchor: @Composable () -> Unit) {
    val properties = MillenniumTheme.tooltipTheme.states.default
    UnstyledTooltip(
        hoverDelayMillis = properties.hoverDelayMillis,
        sideOffset = properties.sideOffset,
        panel = {
            TooltipPanel(
                Modifier.widthIn(max = properties.maxWidth).millenniumSurface(
                    background = properties.background,
                    shape = properties.shape,
                    borderColor = properties.borderColor,
                    borderWidth = properties.borderWidth,
                    shadowElevation = properties.shadowElevation,
                    innerGlowColor = properties.innerGlowColor,
                ).padding(properties.padding).alpha(properties.alpha),
            ) {
                Text(text, style = properties.textStyle, color = properties.foreground)
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
