package org.spotube.millennium.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumText
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun <T> MillenniumTabGroup(
    selectedTab: T,
    onSelectedTabChange: (T) -> Unit,
    tabs: List<T>,
    modifier: Modifier = Modifier,
    tabLabel: (T) -> String = { it.toString() },
    panel: @Composable (T) -> Unit,
) {
    val colors = MillenniumTheme.colors
    UnstyledTabGroup(selectedTab, onSelectedTabChange, tabs, modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TabList(Modifier.fillMaxWidth().beveled(colors, sunken = true).padding(4.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    tabs.forEach { key ->
                        Tab(
                            key,
                            modifier = Modifier.weight(1f).heightIn(min = 44.dp)
                                .then(if (key == selectedTab) Modifier.beveled(colors, accent = true) else Modifier),
                            indication = LocalIndication.current,
                        ) {
                            Text(
                                tabLabel(key),
                                color = if (selected) colors.onAccent else colors.muted,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
            tabs.forEach { key -> TabPanel(key) { panel(key) } }
        }
    }
}

@Preview
@Composable
private fun MillenniumTabGroupPreview() = MillenniumPreview {
    MillenniumTabGroup(
        selectedTab = "Discover",
        onSelectedTabChange = {},
        tabs = listOf("Discover", "Saved"),
    ) { tab -> MillenniumText(if (tab == "Discover") "A golden-hour mix" else "Your favorites") }
}
