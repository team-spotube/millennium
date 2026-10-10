package org.spotube.millennium.composables

import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MillenniumSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val switchTheme = MillenniumTheme.toggleSwitchTheme
    val colors = MillenniumTheme.colorsFor(switchTheme)
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val focused by source.collectIsFocusedAsState()
    val hovered by source.collectIsHoveredAsState()
    val state = when {
        !enabled -> MillenniumVisualState.Disabled
        pressed -> MillenniumVisualState.Clicked
        focused -> MillenniumVisualState.Focused
        hovered -> MillenniumVisualState.Hovered
        checked -> MillenniumVisualState.Selected
        else -> MillenniumVisualState.Default
    }
    val visual = MillenniumTheme.toggleSwitchTheme.resolve(
        state,
        MillenniumVisualStyle(
            background = if (checked) gelBrush(colors.accentTop, colors.accentBottom) else null,
            foregroundColor = colors.text,
            borderColor = colors.border,
            borderWidth = switchTheme.strokes.border,
        ),
    )
    val trackShape = visual.shape ?: switchTheme.shapes.pill
    UnstyledSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.heightIn(min = switchTheme.dimensions.touchTarget)
            .alpha(visual.alpha ?: 1f),
        enabled = enabled,
        accessibilityLabel = label,
        indication = LocalIndication.current,
        interactionSource = source,
    ) {
        Row(
            modifier = Modifier.padding(visual.contentPadding ?: PaddingValues(0.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(visual.contentSpacing ?: switchTheme.spacing.medium),
        ) {
            Track(
                Modifier.size(switchTheme.dimensions.switchTrack)
                    .beveled(colors, trackShape, accent = checked, sunken = !checked)
                    .componentStyle(visual, trackShape),
            ) {
                Thumb(
                    animationSpec = tween(switchTheme.motion.switchMillis),
                    modifier = Modifier.padding(switchTheme.dimensions.switchThumbInset)
                        .size(switchTheme.dimensions.switchThumb)
                        .beveled(colors, switchTheme.shapes.control)
                        .componentStyle(visual, switchTheme.shapes.control),
                )
            }
            CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.copy(color = visual.foregroundColor ?: colors.text)) { Text(label) }
        }
    }
}

@Preview
@Composable
private fun MillenniumSwitchPreview() = MillenniumPreview {
    MillenniumSwitch(checked = true, onCheckedChange = {}, label = "Airplane mode")
}
