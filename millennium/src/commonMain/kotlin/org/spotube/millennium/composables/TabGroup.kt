package org.spotube.millennium.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import com.composeunstyled.LocalTextStyle
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.resolve
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
    val tabTheme = MillenniumTheme.tabGroupTheme
    val colors = MillenniumTheme.colorsFor(tabTheme)
    val groupVisual = tabTheme.resolve(MillenniumVisualState.Default)
    UnstyledTabGroup(selectedTab, onSelectedTabChange, tabs, modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(groupVisual.contentSpacing ?: tabTheme.spacing.large)) {
            val listVisual = tabTheme.resolve(
                MillenniumVisualState.Default,
                MillenniumVisualStyle(backgroundColor = colors.inset, borderColor = colors.border,
                    borderWidth = tabTheme.strokes.border, innerGlowColor = colors.highlight),
            )
            TabList(
                Modifier.fillMaxWidth().beveled(colors, tabTheme.shapes.control, sunken = true)
                    .componentStyle(listVisual, tabTheme.shapes.control).padding(tabTheme.dimensions.tabListInset),
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(tabTheme.spacing.xSmall)) {
                    tabs.forEach { key ->
                        val isSelected = key == selectedTab
                        val interaction = remember(key) { MutableInteractionSource() }
                        val pressed by interaction.collectIsPressedAsState()
                        val focused by interaction.collectIsFocusedAsState()
                        val hovered by interaction.collectIsHoveredAsState()
                        val state = when {
                            pressed -> MillenniumVisualState.Clicked
                            focused -> MillenniumVisualState.Focused
                            hovered -> MillenniumVisualState.Hovered
                            isSelected -> MillenniumVisualState.Selected
                            else -> MillenniumVisualState.Default
                        }
                        val visual = tabTheme.resolve(
                            state,
                            MillenniumVisualStyle(
                                background = if (isSelected) gelBrush(colors.accentTop, colors.accentBottom) else null,
                                foregroundColor = if (isSelected) colors.onAccent else colors.muted,
                                borderColor = if (isSelected) colors.accentBottom else colors.border,
                                borderWidth = if (isSelected) tabTheme.strokes.border else 0.dp,
                            ),
                        )
                        val tabShape = visual.shape ?: tabTheme.shapes.control
                        Tab(
                            key,
                            modifier = Modifier.weight(1f).heightIn(min = tabTheme.dimensions.touchTarget)
                                .then(if (isSelected) Modifier.beveled(colors, tabShape, accent = true) else Modifier)
                                .componentStyle(visual, tabShape)
                                .padding(visual.contentPadding ?: PaddingValues(vertical = tabTheme.spacing.small)),
                            indication = LocalIndication.current,
                            interactionSource = interaction,
                        ) {
                            CompositionLocalProvider(
                                LocalTextStyle provides (visual.textStyle ?: tabTheme.typography.button)
                                    .copy(color = visual.foregroundColor ?: if (selected) colors.onAccent else colors.muted),
                            ) { Text(tabLabel(key)) }
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
