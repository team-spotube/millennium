package org.spotube.millennium.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.composeunstyled.BottomSheetState
import com.composeunstyled.DragIndication
import com.composeunstyled.Sheet
import com.composeunstyled.SheetDetent
import com.composeunstyled.UnstyledBottomSheet
import com.composeunstyled.rememberBottomSheetState
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumPanelBrush

@Immutable
data class MillenniumBottomSheetThemeProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val shape: Shape,
    val maximumWidth: Dp,
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val handleSize: DpSize,
    val handleColor: Color? = null,
    val borderColor: Color? = null,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val innerGlowColor: Color? = null,
    val alpha: Float,
)

@Immutable
data class MillenniumBottomSheetTheme(
    val states: MillenniumControlStateProperty<MillenniumBottomSheetThemeProperties>,
)

fun defaultMillenniumBottomSheetTheme(): MillenniumBottomSheetTheme {
    val base = MillenniumBottomSheetThemeProperties(
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        maximumWidth = 640.dp,
        contentPadding = PaddingValues(24.dp),
        contentSpacing = 16.dp,
        handleSize = DpSize(40.dp, 5.dp),
        borderWidth = 1.dp,
        shadowElevation = 10.dp,
        alpha = 1f,
    )
    return MillenniumBottomSheetTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 6.dp),
            disabled = base.copy(alpha = 0.55f),
            selected = base,
            error = base.copy(borderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumBottomSheet(
    state: BottomSheetState,
    modifier: Modifier = Modifier,
    background: Brush? = null,
    foreground: Color? = null,
    shape: Shape? = null,
    maximumWidth: Dp? = null,
    contentPadding: PaddingValues? = null,
    contentSpacing: Dp? = null,
    handleSize: DpSize? = null,
    handleColor: Color? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    shadowElevation: Dp? = null,
    innerGlowColor: Color? = null,
    alpha: Float? = null,
    theme: MillenniumBottomSheetTheme = MillenniumTheme.bottomSheetTheme,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = theme.states.resolve(MillenniumControlState.Default)
    val resolvedBackground = background ?: properties.background ?: millenniumPanelBrush(colors)
    val resolvedForeground = foreground ?: properties.foreground ?: colors.text
    val resolvedHandleColor = handleColor ?: properties.handleColor ?: colors.muted
    val resolvedBorderColor = borderColor ?: properties.borderColor ?: colors.border
    val resolvedGlowColor = innerGlowColor ?: properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.55f)
    UnstyledBottomSheet(state, modifier) {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.TopCenter) {
            Sheet(
                Modifier.widthIn(max = maximumWidth ?: properties.maximumWidth).fillMaxWidth().millenniumSurface(
                    background = resolvedBackground,
                    shape = shape ?: properties.shape,
                    borderColor = resolvedBorderColor,
                    borderWidth = borderWidth ?: properties.borderWidth,
                    shadowElevation = shadowElevation ?: properties.shadowElevation,
                    innerGlowColor = resolvedGlowColor,
                ).alpha(alpha ?: properties.alpha),
            ) {
                Column(
                    Modifier.padding(contentPadding ?: properties.contentPadding).navigationBarsPadding(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
                        contentSpacing ?: properties.contentSpacing,
                    ),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                ) {
                    DragIndication(
                        Modifier.size(handleSize ?: properties.handleSize).background(resolvedHandleColor, CircleShape),
                    )
                    MillenniumStyledContent(TextStyle(color = resolvedForeground), resolvedForeground) {
                        content()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumBottomSheetPreview() = MillenniumPreview {
    val state = rememberBottomSheetState(initialDetent = SheetDetent.FullyExpanded)
    MillenniumBottomSheet(state) {
        MillenniumText("Hello from down here.", weight = FontWeight.SemiBold)
        MillenniumText("A softly raised sheet with a tactile handle.", color = MillenniumTheme.colors.muted)
    }
}
