package org.spotube.millennium.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import org.spotube.millennium.theme.*

@Composable
fun MillenniumTriStateCheckbox(
    value: ToggleableState,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val focused by source.collectIsFocusedAsState()
    val hovered by source.collectIsHoveredAsState()
    val visualState = when {
        !enabled -> MillenniumVisualState.Disabled
        pressed -> MillenniumVisualState.Clicked
        focused -> MillenniumVisualState.Focused
        hovered -> MillenniumVisualState.Hovered
        value != ToggleableState.Off -> MillenniumVisualState.Selected
        else -> MillenniumVisualState.Default
    }
    val triStateTheme = MillenniumTheme.triStateCheckboxTheme
    val colors = MillenniumTheme.colorsFor(triStateTheme)
    val visual = triStateTheme.resolve(visualState, MillenniumVisualStyle(foregroundColor = colors.text, borderColor = colors.border))
    UnstyledTriStateCheckbox(
        value = value,
        onClick = onClick,
        modifier = modifier.heightIn(min = triStateTheme.dimensions.touchTarget)
            .componentStyle(visual, triStateTheme.shapes.control).alpha(visual.alpha ?: 1f),
        enabled = enabled,
        accessibilityLabel = label,
        indication = LocalIndication.current,
        interactionSource = source,
    ) {
        Row(
            modifier = Modifier.padding(visual.contentPadding ?: PaddingValues(0.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(visual.contentSpacing ?: triStateTheme.spacing.medium),
        ) {
            MillenniumCheckFace(value, triStateTheme, !enabled, hovered, focused, pressed)
            CompositionLocalProvider(LocalTextStyle provides LocalTextStyle.current.copy(color = visual.foregroundColor ?: colors.text)) { Text(label) }
        }
    }
}

@Preview
@Composable
private fun MillenniumTriStateCheckboxPreview() = MillenniumPreview {
    MillenniumTriStateCheckbox(value = ToggleableState.Indeterminate, onClick = {}, label = "Select the collection")
}
