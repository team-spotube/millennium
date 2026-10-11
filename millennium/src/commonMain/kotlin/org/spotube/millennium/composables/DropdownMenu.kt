package org.spotube.millennium.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.AnchorSide
import com.composeunstyled.DropdownMenuPanel
import com.composeunstyled.Text
import com.composeunstyled.UnstyledDropdownMenu
import com.composeunstyled.UnstyledDropdownMenuItem
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumPanelBrush

@Immutable
data class MillenniumDropdownMenuThemeProperties(
    val panelBackground: Brush? = null,
    val panelShape: Shape,
    val panelBorderColor: Color? = null,
    val panelBorderWidth: Dp,
    val panelShadowElevation: Dp,
    val panelGlowColor: Color? = null,
    val minimumWidth: Dp,
    val sideOffset: Dp,
    val panelPadding: PaddingValues,
    val itemBackground: Brush? = null,
    val itemShape: Shape,
    val itemForeground: Color? = null,
    val itemTextStyle: TextStyle,
    val itemMinimumHeight: Dp,
    val itemPadding: PaddingValues,
    val itemSpacing: Dp,
    val stateAnimationMillis: Int,
    val alpha: Float = 1f,
    val itemBorderColor: Color? = null,
    val itemBorderWidth: Dp = 1.dp,
    val itemGlowColor: Color? = null,
    val itemWidth: Dp = 192.dp,
)

@Immutable
data class MillenniumDropdownMenuTheme(
    val states: MillenniumControlStateProperty<MillenniumDropdownMenuThemeProperties>,
)

fun defaultMillenniumDropdownMenuTheme(): MillenniumDropdownMenuTheme {
    val base = MillenniumDropdownMenuThemeProperties(
        panelShape = RoundedCornerShape(12.dp),
        panelBorderWidth = 1.dp,
        panelShadowElevation = 8.dp,
        minimumWidth = 200.dp,
        sideOffset = 8.dp,
        panelPadding = PaddingValues(4.dp),
        itemShape = RoundedCornerShape(8.dp),
        itemTextStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
        itemMinimumHeight = 44.dp,
        itemPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        itemSpacing = 4.dp,
        stateAnimationMillis = 120,
    )
    return MillenniumDropdownMenuTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(alpha = 0.97f),
            focused = base,
            pressed = base.copy(alpha = 0.90f),
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(panelBorderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: List<String>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    side: AnchorSide = AnchorSide.Bottom,
    anchor: @Composable () -> Unit,
) {
    val colors = MillenniumTheme.colors
    val panelProperties = MillenniumTheme.dropdownMenuTheme.states.default
    val panelBackground = panelProperties.panelBackground ?: millenniumPanelBrush(colors)
    val panelBorderColor = panelProperties.panelBorderColor ?: colors.border
    val panelGlowColor = panelProperties.panelGlowColor ?: colors.highlight.copy(alpha = 0.5f)
    UnstyledDropdownMenu(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        side = side,
        sideOffset = panelProperties.sideOffset,
        anchor = anchor,
        panel = {
            DropdownMenuPanel(
                Modifier.widthIn(min = panelProperties.minimumWidth).millenniumSurface(
                    background = panelBackground,
                    shape = panelProperties.panelShape,
                    borderColor = panelBorderColor,
                    borderWidth = panelProperties.panelBorderWidth,
                    shadowElevation = panelProperties.panelShadowElevation,
                    innerGlowColor = panelGlowColor,
                ).padding(panelProperties.panelPadding).alpha(panelProperties.alpha),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(panelProperties.itemSpacing)) {
                    items.forEach { item ->
                        val interaction = remember(item) { MutableInteractionSource() }
                        val pressed by interaction.collectIsPressedAsState()
                        val focused by interaction.collectIsKeyboardFocusedAsState()
                        val hovered by interaction.collectIsHoveredAsState()
                        val state = when {
                            pressed -> MillenniumControlState.Pressed
                            focused -> MillenniumControlState.Focused
                            hovered -> MillenniumControlState.Hovered
                            else -> MillenniumControlState.Default
                        }
                        val properties = MillenniumTheme.dropdownMenuTheme.states.resolve(state)
                        val itemBackground = properties.itemBackground ?: when (state) {
                            MillenniumControlState.Hovered, MillenniumControlState.Focused,
                            MillenniumControlState.Pressed -> millenniumBevelBrush(colors, accent = true)
                            MillenniumControlState.Selected -> millenniumBevelBrush(colors, accent = true)
                            else -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
                        }
                        val itemForeground = properties.itemForeground ?: when (state) {
                            MillenniumControlState.Disabled -> colors.muted
                            MillenniumControlState.Selected, MillenniumControlState.Hovered,
                            MillenniumControlState.Focused, MillenniumControlState.Pressed -> colors.onAccent
                            else -> colors.text
                        }
                        val animatedAlpha = animateFloatAsState(
                            targetValue = properties.alpha,
                            animationSpec = tween(properties.stateAnimationMillis),
                        ).value
                        UnstyledDropdownMenuItem(
                            onClick = { onItemClick(item) },
                            indication = LocalIndication.current,
                            interactionSource = interaction,
                            modifier = Modifier.width(properties.itemWidth).heightIn(min = properties.itemMinimumHeight)
                                .alpha(animatedAlpha)
                                .millenniumSurface(
                                    itemBackground, properties.itemShape,
                                    borderColor = properties.itemBorderColor ?: if (hovered || focused || pressed) colors.accentBottom else null,
                                    borderWidth = properties.itemBorderWidth,
                                    innerGlowColor = properties.itemGlowColor ?: if (hovered || focused || pressed) colors.highlight.copy(alpha = 0.35f) else null,
                                ),
                        ) {
                            Text(
                                item,
                                modifier = Modifier.fillMaxWidth().padding(properties.itemPadding),
                                style = properties.itemTextStyle,
                                color = itemForeground,
                            )
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
