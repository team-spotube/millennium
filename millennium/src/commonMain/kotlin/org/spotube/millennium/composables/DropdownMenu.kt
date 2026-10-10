package org.spotube.millennium.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import com.composeunstyled.DropdownMenuPanel
import com.composeunstyled.Text
import com.composeunstyled.UnstyledDropdownMenu
import com.composeunstyled.UnstyledDropdownMenuItem
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumDropdownMenuThemeProperties(
    val colors: MillenniumColors,
    val panelBackground: Brush,
    val panelShape: Shape,
    val panelBorderColor: Color,
    val panelBorderWidth: Dp,
    val panelShadowElevation: Dp,
    val panelGlowColor: Color,
    val minimumWidth: Dp,
    val sideOffset: Dp,
    val panelPadding: PaddingValues,
    val itemBackground: Brush,
    val itemShape: Shape,
    val itemForeground: Color,
    val itemTextStyle: TextStyle,
    val itemMinimumHeight: Dp,
    val itemPadding: PaddingValues,
    val itemSpacing: Dp,
    val stateAnimationMillis: Int,
    val alpha: Float = 1f,
)

@Immutable
data class MillenniumDropdownMenuTheme(
    val states: MillenniumControlStateProperty<MillenniumDropdownMenuThemeProperties>,
)

fun defaultMillenniumDropdownMenuTheme(colors: MillenniumColors): MillenniumDropdownMenuTheme {
    val base = MillenniumDropdownMenuThemeProperties(
        colors = colors,
        panelBackground = millenniumBevelBrush(colors),
        panelShape = RoundedCornerShape(12.dp),
        panelBorderColor = colors.border,
        panelBorderWidth = 1.dp,
        panelShadowElevation = 8.dp,
        panelGlowColor = colors.highlight.copy(alpha = 0.5f),
        minimumWidth = 200.dp,
        sideOffset = 8.dp,
        panelPadding = PaddingValues(4.dp),
        itemBackground = Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent)),
        itemShape = RoundedCornerShape(8.dp),
        itemForeground = colors.text,
        itemTextStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
        itemMinimumHeight = 44.dp,
        itemPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        itemSpacing = 4.dp,
        stateAnimationMillis = 120,
    )
    return MillenniumDropdownMenuTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(itemBackground = millenniumBevelBrush(colors), itemForeground = colors.text, alpha = 0.97f),
            focused = base.copy(itemBackground = millenniumBevelBrush(colors), itemForeground = colors.text),
            pressed = base.copy(itemBackground = millenniumBevelBrush(colors, sunken = true), alpha = 0.90f),
            disabled = base.copy(alpha = 0.45f, itemForeground = colors.muted),
            selected = base.copy(itemBackground = millenniumBevelBrush(colors, accent = true), itemForeground = colors.onAccent),
            error = base.copy(panelBorderColor = colors.danger, panelBorderWidth = 2.dp),
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
    anchor: @Composable () -> Unit,
) {
    val panelProperties = MillenniumTheme.dropdownMenuTheme.states.default
    UnstyledDropdownMenu(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        sideOffset = panelProperties.sideOffset,
        anchor = anchor,
        panel = {
            DropdownMenuPanel(
                Modifier.widthIn(min = panelProperties.minimumWidth).millenniumSurface(
                    background = panelProperties.panelBackground,
                    shape = panelProperties.panelShape,
                    borderColor = panelProperties.panelBorderColor,
                    borderWidth = panelProperties.panelBorderWidth,
                    shadowElevation = panelProperties.panelShadowElevation,
                    innerGlowColor = panelProperties.panelGlowColor,
                ).padding(panelProperties.panelPadding).alpha(panelProperties.alpha),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(panelProperties.itemSpacing)) {
                    items.forEach { item ->
                        val interaction = remember(item) { MutableInteractionSource() }
                        val pressed by interaction.collectIsPressedAsState()
                        val focused by interaction.collectIsFocusedAsState()
                        val hovered by interaction.collectIsHoveredAsState()
                        val state = when {
                            pressed -> MillenniumControlState.Pressed
                            focused -> MillenniumControlState.Focused
                            hovered -> MillenniumControlState.Hovered
                            else -> MillenniumControlState.Default
                        }
                        val properties = MillenniumTheme.dropdownMenuTheme.states.resolve(state)
                        val animatedAlpha = animateFloatAsState(
                            targetValue = properties.alpha,
                            animationSpec = tween(properties.stateAnimationMillis),
                        ).value
                        UnstyledDropdownMenuItem(
                            onClick = { onItemClick(item) },
                            indication = LocalIndication.current,
                            interactionSource = interaction,
                            modifier = Modifier.fillMaxWidth().heightIn(min = properties.itemMinimumHeight)
                                .alpha(animatedAlpha)
                                .millenniumSurface(properties.itemBackground, properties.itemShape)
                                .padding(properties.itemPadding),
                        ) {
                            Text(item, style = properties.itemTextStyle, color = properties.itemForeground)
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
