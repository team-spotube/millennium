package org.spotube.millennium.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.composeunstyled.Text
import com.composeunstyled.UnstyledButton
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.millenniumActionPointer
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumInteractionSurface

/** Shape helpers for [MillenniumSegmentedButton]. */
object MillenniumSegmentedButtonDefaults {
    /** A single segment's shape: only the outer edges of the row are rounded. */
    fun itemShape(index: Int, count: Int): Shape = RoundedCornerShape(
        topStart = if (index == 0) 10.dp else 0.dp,
        bottomStart = if (index == 0) 10.dp else 0.dp,
        topEnd = if (index == count - 1) 10.dp else 0.dp,
        bottomEnd = if (index == count - 1) 10.dp else 0.dp,
    )
}

/** Complete, state-specific segment appearance and layout configuration. */
@Immutable
data class MillenniumSegmentedButtonThemeProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val textStyle: TextStyle,
    val contentPadding: PaddingValues,
    val minimumHeight: Dp,
    val borderWidth: Dp,
    val borderColor: Color? = null,
    val innerGlowColor: Color? = null,
    val alpha: Float = 1f,
    val motion: MillenniumButtonMotion = MillenniumButtonMotion(hoverScale = 1f, pressedScale = 0.97f),
)

@Immutable
data class MillenniumSegmentedButtonTheme(
    val states: MillenniumControlStateProperty<MillenniumSegmentedButtonThemeProperties>,
)

fun defaultMillenniumSegmentedButtonTheme(): MillenniumSegmentedButtonTheme {
    val base = MillenniumSegmentedButtonThemeProperties(
        textStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        minimumHeight = 44.dp,
        borderWidth = 1.dp,
    )
    return MillenniumSegmentedButtonTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(borderWidth = 2.dp),
            pressed = base,
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(borderWidth = 2.dp),
        ),
    )
}

/** A horizontal row of connected [MillenniumSegmentedButton] segments. */
@Composable
fun MillenniumSegmentedButtonRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(modifier = modifier.fillMaxWidth(), content = content)
}

/**
 * A single selectable segment. Only one segment in a [MillenniumSegmentedButtonRow] is
 * typically [selected] at a time; the caller owns that state. Selection uses the accent
 * gel; the surrounding segments keep the enamel bevel. Customize through
 * [MillenniumTheme.segmentedButtonTheme].
 */
@Composable
fun RowScope.MillenniumSegmentedButton(
    selected: Boolean,
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    background: Brush? = null,
    foreground: Color? = null,
    textStyle: TextStyle? = null,
    contentPadding: PaddingValues? = null,
    minimumHeight: Dp? = null,
    borderWidth: Dp? = null,
    borderColor: Color? = null,
    innerGlowColor: Color? = null,
    alpha: Float? = null,
    motion: MillenniumButtonMotion? = null,
    theme: MillenniumSegmentedButtonTheme = MillenniumTheme.segmentedButtonTheme,
    content: @Composable () -> Unit,
) {
    val colors = MillenniumTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val focused by interaction.collectIsKeyboardFocusedAsState()
    val state = when {
        !enabled -> MillenniumControlState.Disabled
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        selected -> MillenniumControlState.Selected
        else -> MillenniumControlState.Default
    }
    val properties = theme.states.resolve(state)
    val resolvedMotion = motion ?: properties.motion
    val resolvedBackground = background ?: properties.background ?: when {
        selected -> gelBrush(colors.accentTop, colors.accentBottom)
        !enabled || pressed -> millenniumBevelBrush(colors, sunken = true)
        else -> millenniumBevelBrush(colors)
    }
    val targetForeground = foreground ?: properties.foreground ?: if (selected) colors.onAccent else colors.text
    val resolvedForeground by animateColorAsState(
        targetValue = targetForeground,
        animationSpec = tween(resolvedMotion.stateChangeMillis),
    )
    val targetBorderColor = borderColor ?: properties.borderColor ?: when {
        focused -> colors.accent
        selected -> colors.accentBottom
        else -> colors.border
    }
    val resolvedBorderColor by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(resolvedMotion.stateChangeMillis),
    )
    val scale by animateFloatAsState(
        targetValue = when {
            !enabled -> 1f
            pressed -> resolvedMotion.pressedScale
            hovered && !selected -> 1.015f
            else -> 1f
        },
        animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
    )
    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        indication = null,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .millenniumActionPointer(enabled)
            .weight(1f)
            .defaultMinSize(minHeight = minimumHeight ?: properties.minimumHeight)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .alpha(alpha ?: properties.alpha)
            .millenniumSurface(
                background = resolvedBackground,
                shape = shape,
                borderColor = resolvedBorderColor,
                borderWidth = borderWidth ?: properties.borderWidth,
                innerGlowColor = innerGlowColor ?: properties.innerGlowColor
                    ?: colors.highlight.copy(alpha = if (selected) 0.5f else 0.3f),
            )
            .millenniumInteractionSurface(state, shape, animationMillis = resolvedMotion.stateChangeMillis),
    ) {
        Row(
            Modifier.padding(contentPadding ?: properties.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            MillenniumStyledContent(textStyle ?: properties.textStyle, resolvedForeground, content)
        }
    }
}

@Preview
@Composable
private fun MillenniumSegmentedButtonPreview() = MillenniumPreview {
    var selected by remember { mutableStateOf(0) }
    val labels = listOf("Day", "Week", "Month")
    MillenniumSegmentedButtonRow {
        labels.forEachIndexed { index, label ->
            MillenniumSegmentedButton(
                selected = selected == index,
                onClick = { selected = index },
                shape = MillenniumSegmentedButtonDefaults.itemShape(index, labels.size),
            ) { Text(label) }
        }
    }
}
