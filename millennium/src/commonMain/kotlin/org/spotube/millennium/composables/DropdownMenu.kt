package org.spotube.millennium.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumComponentContent
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
    val menuTheme = MillenniumTheme.dropdownMenuTheme
    val colors = MillenniumTheme.colorsFor(menuTheme)
    val panelVisual = MillenniumTheme.dropdownMenuTheme.resolve(
        MillenniumVisualState.Default,
        MillenniumVisualStyle(backgroundColor = colors.surface, foregroundColor = colors.text,
            borderColor = colors.border, borderWidth = menuTheme.strokes.border,
            shadowElevation = menuTheme.elevation.card, innerGlowColor = colors.highlight),
    )
    val panelShape = panelVisual.shape ?: menuTheme.shapes.control
    UnstyledDropdownMenu(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        sideOffset = menuTheme.spacing.small,
        anchor = anchor,
        panel = {
            DropdownMenuPanel(
                Modifier.widthIn(min = menuTheme.dimensions.dropdownMinWidth).beveled(colors, panelShape)
                    .componentStyle(panelVisual, panelShape)
                    .padding(panelVisual.contentPadding ?: PaddingValues(menuTheme.spacing.xSmall)),
            ) {
                MillenniumComponentContent(panelVisual) {
                  Column(verticalArrangement = Arrangement.spacedBy(panelVisual.contentSpacing ?: menuTheme.spacing.xSmall)) {
                    items.forEach { item ->
                        val interaction = remember(item) { MutableInteractionSource() }
                        val pressed by interaction.collectIsPressedAsState()
                        val focused by interaction.collectIsFocusedAsState()
                        val hovered by interaction.collectIsHoveredAsState()
                        val state = when {
                            pressed -> MillenniumVisualState.Clicked
                            focused -> MillenniumVisualState.Focused
                            hovered -> MillenniumVisualState.Hovered
                            else -> MillenniumVisualState.Default
                        }
                        val itemVisual = MillenniumTheme.dropdownMenuTheme.resolve(state)
                        UnstyledDropdownMenuItem(
                            onClick = { onItemClick(item) },
                            indication = LocalIndication.current,
                            interactionSource = interaction,
                            modifier = Modifier.fillMaxWidth().heightIn(min = menuTheme.dimensions.touchTarget)
                                .componentStyle(itemVisual, menuTheme.shapes.control).padding(itemVisual.contentPadding
                                    ?: PaddingValues(horizontal = menuTheme.spacing.medium, vertical = menuTheme.spacing.small)),
                        ) { Text(item, color = itemVisual.foregroundColor ?: panelVisual.foregroundColor ?: colors.text) }
                    }
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
