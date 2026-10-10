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
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumBottomSheetThemeProperties(
    val colors: MillenniumColors,
    val background: Brush,
    val foreground: Color,
    val shape: Shape,
    val maximumWidth: Dp,
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val handleSize: DpSize,
    val handleColor: Color,
    val borderColor: Color,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val innerGlowColor: Color,
    val alpha: Float,
)

@Immutable
data class MillenniumBottomSheetTheme(
    val states: MillenniumControlStateProperty<MillenniumBottomSheetThemeProperties>,
)

fun defaultMillenniumBottomSheetTheme(colors: MillenniumColors): MillenniumBottomSheetTheme {
    val base = MillenniumBottomSheetThemeProperties(
        colors = colors,
        background = millenniumBevelBrush(colors),
        foreground = colors.text,
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        maximumWidth = 640.dp,
        contentPadding = PaddingValues(24.dp),
        contentSpacing = 16.dp,
        handleSize = DpSize(40.dp, 5.dp),
        handleColor = colors.muted,
        borderColor = colors.border,
        borderWidth = 1.dp,
        shadowElevation = 10.dp,
        innerGlowColor = colors.highlight.copy(alpha = 0.55f),
        alpha = 1f,
    )
    return MillenniumBottomSheetTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(borderColor = colors.accent),
            focused = base.copy(borderColor = colors.accent, borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 6.dp),
            disabled = base.copy(alpha = 0.55f),
            selected = base.copy(borderColor = colors.accent),
            error = base.copy(borderColor = colors.danger, borderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumBottomSheet(
    state: BottomSheetState,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val properties = MillenniumTheme.bottomSheetTheme.states.resolve(MillenniumControlState.Default)
    UnstyledBottomSheet(state, modifier) {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.TopCenter) {
            Sheet(
                Modifier.widthIn(max = properties.maximumWidth).fillMaxWidth().millenniumSurface(
                    background = properties.background,
                    shape = properties.shape,
                    borderColor = properties.borderColor,
                    borderWidth = properties.borderWidth,
                    shadowElevation = properties.shadowElevation,
                    innerGlowColor = properties.innerGlowColor,
                ).alpha(properties.alpha),
            ) {
                Column(
                    Modifier.padding(properties.contentPadding).navigationBarsPadding(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(properties.contentSpacing),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                ) {
                    DragIndication(
                        Modifier.size(properties.handleSize).background(properties.handleColor, CircleShape),
                    )
                    MillenniumStyledContent(TextStyle(color = properties.foreground), properties.foreground) {
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
        MillenniumText("A softly raised sheet with a tactile handle.", color = MillenniumTheme.bottomSheetTheme.states.default.colors.muted)
    }
}
