package org.spotube.millennium.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview
import com.composeunstyled.Text

@Composable
fun MillenniumDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<String>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    anchor: @Composable () -> Unit,
) {
    val colors = MillenniumTheme.colors
    UnstyledDropdownMenu(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        sideOffset = 8.dp,
        anchor = anchor,
        panel = {
            DropdownMenuPanel(Modifier.widthIn(min = 200.dp).beveled(colors).padding(6.dp)) {
                Column {
                    items.forEach { item ->
                        UnstyledDropdownMenuItem(
                            onClick = { onItemClick(item) },
                            indication = LocalIndication.current,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 44.dp)
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                        ) { Text(item) }
                    }
                }
            }
        },
    )
}

@Preview
@Composable
private fun MillenniumDropdownMenuPreview() = MillenniumPreview {
    MillenniumDropdownMenu(
        expanded = true,
        onExpandedChange = {},
        items = listOf("Golden hour", "Midnight drive", "Back to 2007"),
        onItemClick = {},
        anchor = { MillenniumButton(onClick = {}) { Text("Choose a mood") } },
    )
}
