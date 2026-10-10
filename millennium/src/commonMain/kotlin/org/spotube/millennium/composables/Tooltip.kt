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
fun MillenniumTooltip(text: String, anchor: @Composable () -> Unit) {
    val properties = MillenniumTheme.tooltipTheme.states.default
    val colors = MillenniumTheme.colors
    val background = properties.background ?: millenniumPanelBrush(colors)
    val borderColor = properties.borderColor ?: colors.border
    val glowColor = properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.5f)
    val foreground = properties.foreground ?: colors.text
    UnstyledTooltip(
        hoverDelayMillis = properties.hoverDelayMillis,
        sideOffset = properties.sideOffset,
        panel = {
            TooltipPanel(
                Modifier.widthIn(max = properties.maxWidth).millenniumSurface(
                    background = background,
                    shape = properties.shape,
                    borderColor = borderColor,
                    borderWidth = properties.borderWidth,
                    shadowElevation = properties.shadowElevation,
                    innerGlowColor = glowColor,
                ).padding(properties.padding).alpha(properties.alpha),
            ) {
                Text(text, style = properties.textStyle, color = foreground)
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
