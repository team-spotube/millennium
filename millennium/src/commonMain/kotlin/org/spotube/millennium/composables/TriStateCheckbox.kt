package org.spotube.millennium.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.composeunstyled.Text
import com.composeunstyled.UnstyledTriStateCheckbox
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MillenniumTriStateCheckbox(
    value: ToggleableState,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    UnstyledTriStateCheckbox(
        value = value,
        onClick = onClick,
        modifier = modifier.heightIn(min = 44.dp).alpha(if (enabled) 1f else 0.45f),
        enabled = enabled,
        accessibilityLabel = label,
        indication = LocalIndication.current,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MillenniumCheckFace(value)
            Text(label)
        }
    }
}

@Preview
@Composable
private fun MillenniumTriStateCheckboxPreview() = MillenniumPreview {
    MillenniumTriStateCheckbox(value = ToggleableState.Indeterminate, onClick = {}, label = "Select the collection")
}
