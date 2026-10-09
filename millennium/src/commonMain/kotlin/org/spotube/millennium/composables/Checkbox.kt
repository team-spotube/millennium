package org.spotube.millennium.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumPreview

@Composable
internal fun MillenniumCheckFace(value: ToggleableState) {
    val colors = MillenniumTheme.colors
    Box(
        Modifier.size(22.dp).beveled(
            colors, RoundedCornerShape(5.dp), accent = value != ToggleableState.Off,
            sunken = value == ToggleableState.Off,
        ),
        contentAlignment = Alignment.Center,
    ) {
        when (value) {
            ToggleableState.On -> MillenniumIcon(MillenniumIcons.Check, modifier = Modifier.size(16.dp), tint = colors.onAccent)
            ToggleableState.Indeterminate -> Box(Modifier.size(11.dp, 2.dp).background(colors.onAccent, CircleShape))
            ToggleableState.Off -> Unit
        }
    }
}

@Composable
fun MillenniumCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    UnstyledCheckbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.heightIn(min = 44.dp).alpha(if (enabled) 1f else 0.45f),
        enabled = enabled,
        accessibilityLabel = label,
        indication = LocalIndication.current,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MillenniumCheckFace(if (checked) ToggleableState.On else ToggleableState.Off)
            Text(label)
        }
    }
}

@Preview
@Composable
private fun MillenniumCheckboxPreview() = MillenniumPreview {
    MillenniumCheckbox(checked = true, onCheckedChange = {}, label = "Remember this choice")
}
