package org.spotube.millennium.composables

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
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
import com.composeunstyled.DragIndication
import com.composeunstyled.ModalBottomSheetState
import com.composeunstyled.Scrim
import com.composeunstyled.Sheet
import com.composeunstyled.SheetDetent
import com.composeunstyled.UnstyledModalBottomSheet
import com.composeunstyled.rememberModalBottomSheetState
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumModalBottomSheetThemeProperties(
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
    val scrimColor: Color,
    val animationMillis: Int,
    val alpha: Float,
)

@Immutable
data class MillenniumModalBottomSheetTheme(
    val states: MillenniumControlStateProperty<MillenniumModalBottomSheetThemeProperties>,
)

fun defaultMillenniumModalBottomSheetTheme(colors: MillenniumColors): MillenniumModalBottomSheetTheme {
    val base = MillenniumModalBottomSheetThemeProperties(
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
        shadowElevation = 12.dp,
        innerGlowColor = colors.highlight.copy(alpha = 0.55f),
        scrimColor = colors.scrim,
        animationMillis = 220,
        alpha = 1f,
    )
    return MillenniumModalBottomSheetTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(borderColor = colors.accent),
            focused = base.copy(borderColor = colors.accent, borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 8.dp),
            disabled = base.copy(alpha = 0.55f),
            selected = base.copy(borderColor = colors.accent),
            error = base.copy(borderColor = colors.danger, borderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumModalBottomSheet(
    state: ModalBottomSheetState,
    content: @Composable ColumnScope.() -> Unit,
) {
    val properties = MillenniumTheme.modalBottomSheetTheme.states.resolve(MillenniumControlState.Default)
    UnstyledModalBottomSheet(
        state = state,
        overlay = {
            Scrim(
                scrimColor = properties.scrimColor,
                enter = fadeIn(animationSpec = tween(properties.animationMillis)),
                exit = fadeOut(animationSpec = tween(properties.animationMillis)),
            )
        },
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.TopCenter) {
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
private fun MillenniumModalBottomSheetPreview() = MillenniumPreview {
    val state = rememberModalBottomSheetState(initialDetent = SheetDetent.FullyExpanded)
    MillenniumModalBottomSheet(state) {
        MillenniumText("Your next favorite track", weight = FontWeight.SemiBold)
        MillenniumText("A modal sheet with a soft glass finish.", color = MillenniumTheme.modalBottomSheetTheme.states.default.colors.muted)
    }
}
