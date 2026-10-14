package org.spotube.millennium.composables

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.millenniumInteractionSurface
import org.spotube.millennium.theme.millenniumActionPointer
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumPanelBrush

@Immutable
data class MillenniumModalBottomSheetThemeProperties(
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
    val scrimColor: Color? = null,
    val animationMillis: Int,
    val alpha: Float,
)

@Immutable
data class MillenniumModalBottomSheetTheme(
    val states: MillenniumControlStateProperty<MillenniumModalBottomSheetThemeProperties>,
)

fun defaultMillenniumModalBottomSheetTheme(): MillenniumModalBottomSheetTheme {
    val base = MillenniumModalBottomSheetThemeProperties(
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        maximumWidth = 640.dp,
        contentPadding = PaddingValues(24.dp),
        contentSpacing = 16.dp,
        handleSize = DpSize(40.dp, 5.dp),
        borderWidth = 1.dp,
        shadowElevation = 12.dp,
        animationMillis = 220,
        alpha = 1f,
    )
    return MillenniumModalBottomSheetTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 8.dp),
            disabled = base.copy(alpha = 0.55f),
            selected = base,
            error = base.copy(borderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumModalBottomSheet(
    state: ModalBottomSheetState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues? = null,
    background: Brush? = null,
    foreground: Color? = null,
    shape: Shape? = null,
    maximumWidth: Dp? = null,
    contentSpacing: Dp? = null,
    handleSize: DpSize? = null,
    handleColor: Color? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    shadowElevation: Dp? = null,
    innerGlowColor: Color? = null,
    scrimColor: Color? = null,
    animationMillis: Int? = null,
    alpha: Float? = null,
    theme: MillenniumModalBottomSheetTheme = MillenniumTheme.modalBottomSheetTheme,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val handleInteraction = remember { MutableInteractionSource() }
    val handleHovered by handleInteraction.collectIsHoveredAsState()
    val handlePressed by handleInteraction.collectIsPressedAsState()
    val handleState = when {
        handlePressed -> MillenniumControlState.Pressed
        handleHovered -> MillenniumControlState.Hovered
        else -> MillenniumControlState.Default
    }
    val properties = theme.states.resolve(MillenniumControlState.Default)
    val resolvedBackground = background ?: properties.background ?: millenniumPanelBrush(colors)
    val resolvedForeground = foreground ?: properties.foreground ?: colors.text
    val resolvedHandleColor = handleColor ?: properties.handleColor ?: colors.muted
    val resolvedBorderColor = borderColor ?: properties.borderColor ?: colors.border
    val resolvedGlowColor = innerGlowColor ?: properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.55f)
    val resolvedScrimColor = scrimColor ?: properties.scrimColor ?: colors.scrim
    val resolvedAnimationMillis = animationMillis ?: properties.animationMillis
    UnstyledModalBottomSheet(
        state = state,
        overlay = {
            Scrim(
                modifier = Modifier.millenniumActionPointer(),
                scrimColor = resolvedScrimColor,
                enter = fadeIn(animationSpec = tween(resolvedAnimationMillis, easing = FastOutSlowInEasing)),
                exit = fadeOut(animationSpec = tween((resolvedAnimationMillis * 0.8f).toInt().coerceAtLeast(120), easing = FastOutLinearInEasing)),
            )
        },
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.TopCenter) {
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
                        Modifier.millenniumActionPointer().size(handleSize ?: properties.handleSize).background(resolvedHandleColor, CircleShape)
                            .millenniumInteractionSurface(handleState, CircleShape),
                        interactionSource = handleInteraction,
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
private fun MillenniumModalBottomSheetPreview() = MillenniumPreview {
    val state = rememberModalBottomSheetState(initialDetent = SheetDetent.FullyExpanded)
    MillenniumModalBottomSheet(state) {
        MillenniumText("Your next favorite track", weight = FontWeight.SemiBold)
        MillenniumText("A modal sheet with a soft glass finish.", color = MillenniumTheme.colors.muted)
    }
}
