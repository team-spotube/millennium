package org.spotube.millennium.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
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
import org.spotube.millennium.theme.millenniumActionPointer
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumInteractionSurface
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
    val openAnimationMillis: Int = 180,
    val closeAnimationMillis: Int = 130,
    val enterScale: Float = 0.94f,
    val exitScale: Float = 0.96f,
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
    panelBackground: Brush? = null,
    panelShape: Shape? = null,
    panelBorderColor: Color? = null,
    panelBorderWidth: Dp? = null,
    panelShadowElevation: Dp? = null,
    panelGlowColor: Color? = null,
    minimumWidth: Dp? = null,
    sideOffset: Dp? = null,
    panelPadding: PaddingValues? = null,
    itemBackground: Brush? = null,
    itemShape: Shape? = null,
    itemForeground: Color? = null,
    itemTextStyle: TextStyle? = null,
    itemMinimumHeight: Dp? = null,
    itemPadding: PaddingValues? = null,
    itemSpacing: Dp? = null,
    itemBorderColor: Color? = null,
    itemBorderWidth: Dp? = null,
    itemGlowColor: Color? = null,
    itemWidth: Dp? = null,
    stateAnimationMillis: Int? = null,
    openAnimationMillis: Int? = null,
    closeAnimationMillis: Int? = null,
    enterScale: Float? = null,
    exitScale: Float? = null,
    alpha: Float? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    anchor: @Composable () -> Unit,
) {
    val colors = MillenniumTheme.colors
    val panelProperties = theme.states.default
    val resolvedPanelBackground = panelBackground ?: panelProperties.panelBackground ?: millenniumPanelBrush(colors)
    val resolvedPanelBorderColor = panelBorderColor ?: panelProperties.panelBorderColor ?: colors.border
    val resolvedPanelGlowColor = panelGlowColor ?: panelProperties.panelGlowColor ?: colors.highlight.copy(alpha = 0.5f)
    val resolvedOpenMillis = openAnimationMillis ?: panelProperties.openAnimationMillis
    val resolvedCloseMillis = closeAnimationMillis ?: panelProperties.closeAnimationMillis
    val resolvedEnterScale = enterScale ?: panelProperties.enterScale
    val resolvedExitScale = exitScale ?: panelProperties.exitScale
    val transformOrigin = when (side) {
        AnchorSide.Bottom -> TransformOrigin(0.5f, 0f)
        AnchorSide.Top -> TransformOrigin(0.5f, 1f)
        AnchorSide.Start -> TransformOrigin(1f, 0.5f)
        AnchorSide.End -> TransformOrigin(0f, 0.5f)
    }
    val enterSlide = when (side) {
        AnchorSide.Bottom -> expandVertically(
            animationSpec = spring(dampingRatio = 0.80f, stiffness = Spring.StiffnessMediumLow),
            expandFrom = Alignment.Top,
        ) + slideInVertically(
            animationSpec = spring(dampingRatio = 0.78f, stiffness = Spring.StiffnessMediumLow),
            initialOffsetY = { -it / 6 },
        )
        AnchorSide.Top -> expandVertically(
            animationSpec = spring(dampingRatio = 0.80f, stiffness = Spring.StiffnessMediumLow),
            expandFrom = Alignment.Bottom,
        ) + slideInVertically(
            animationSpec = spring(dampingRatio = 0.78f, stiffness = Spring.StiffnessMediumLow),
            initialOffsetY = { it / 6 },
        )
        AnchorSide.Start -> slideInHorizontally(
            animationSpec = spring(dampingRatio = 0.78f, stiffness = Spring.StiffnessMediumLow),
            initialOffsetX = { it / 6 },
        )
        AnchorSide.End -> slideInHorizontally(
            animationSpec = spring(dampingRatio = 0.78f, stiffness = Spring.StiffnessMediumLow),
            initialOffsetX = { -it / 6 },
        )
    }
    val exitSlide = when (side) {
        AnchorSide.Bottom -> shrinkVertically(
            animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing),
            shrinkTowards = Alignment.Top,
        ) + slideOutVertically(
            animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing),
            targetOffsetY = { -it / 8 },
        )
        AnchorSide.Top -> shrinkVertically(
            animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing),
            shrinkTowards = Alignment.Bottom,
        ) + slideOutVertically(
            animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing),
            targetOffsetY = { it / 8 },
        )
        AnchorSide.Start -> slideOutHorizontally(
            animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing),
            targetOffsetX = { it / 8 },
        )
        AnchorSide.End -> slideOutHorizontally(
            animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing),
            targetOffsetX = { -it / 8 },
        )
    }
    UnstyledDropdownMenu(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        side = side,
        sideOffset = sideOffset ?: panelProperties.sideOffset,
        anchor = anchor,
        panel = {
            DropdownMenuPanel(
                modifier = Modifier.widthIn(min = minimumWidth ?: panelProperties.minimumWidth).millenniumSurface(
                    background = resolvedPanelBackground,
                    shape = panelShape ?: panelProperties.panelShape,
                    borderColor = resolvedPanelBorderColor,
                    borderWidth = panelBorderWidth ?: panelProperties.panelBorderWidth,
                    shadowElevation = panelShadowElevation ?: panelProperties.panelShadowElevation,
                    innerGlowColor = resolvedPanelGlowColor,
                ).padding(panelPadding ?: panelProperties.panelPadding).alpha(alpha ?: panelProperties.alpha),
                enter = fadeIn(tween(resolvedOpenMillis, easing = FastOutSlowInEasing)) +
                    scaleIn(
                        initialScale = resolvedEnterScale,
                        transformOrigin = transformOrigin,
                        animationSpec = spring(
                            dampingRatio = 0.76f,
                            stiffness = Spring.StiffnessMediumLow,
                        ),
                    ) + enterSlide,
                exit = fadeOut(tween(resolvedCloseMillis, easing = FastOutLinearInEasing)) +
                    scaleOut(
                        targetScale = resolvedExitScale,
                        transformOrigin = transformOrigin,
                        animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing),
                    ) + exitSlide,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(itemSpacing ?: panelProperties.itemSpacing)) {
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
                        val properties = theme.states.resolve(state)
                        val resolvedStateMillis = stateAnimationMillis ?: properties.stateAnimationMillis
                        val active = hovered || focused || pressed
                        val resolvedItemBackground = itemBackground ?: properties.itemBackground ?: when (state) {
                            MillenniumControlState.Hovered, MillenniumControlState.Focused,
                            MillenniumControlState.Pressed -> millenniumBevelBrush(colors, accent = true)
                            MillenniumControlState.Selected -> millenniumBevelBrush(colors, accent = true)
                            else -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
                        }
                        val targetItemForeground = itemForeground ?: properties.itemForeground ?: when (state) {
                            MillenniumControlState.Disabled -> colors.muted
                            MillenniumControlState.Selected, MillenniumControlState.Hovered,
                            MillenniumControlState.Focused, MillenniumControlState.Pressed -> colors.onAccent
                            else -> colors.text
                        }
                        val resolvedItemForeground by animateColorAsState(
                            targetValue = targetItemForeground,
                            animationSpec = tween(resolvedStateMillis),
                        )
                        val targetBorderColor = itemBorderColor ?: properties.itemBorderColor
                            ?: if (active) colors.accentBottom else Color.Transparent
                        val animatedBorderColor by animateColorAsState(
                            targetValue = targetBorderColor,
                            animationSpec = tween(resolvedStateMillis),
                        )
                        val animatedAlpha by animateFloatAsState(
                            targetValue = alpha ?: properties.alpha,
                            animationSpec = tween(resolvedStateMillis),
                        )
                        val itemScale by animateFloatAsState(
                            targetValue = when {
                                pressed -> 0.975f
                                hovered || focused -> 1.012f
                                else -> 1f
                            },
                            animationSpec = spring(
                                dampingRatio = 0.72f,
                                stiffness = Spring.StiffnessMedium,
                            ),
                        )
                        val itemElevation by animateDpAsState(
                            targetValue = when {
                                pressed -> 0.dp
                                active -> 2.dp
                                else -> 0.dp
                            },
                            animationSpec = tween(resolvedStateMillis),
                        )
                        UnstyledDropdownMenuItem(
                            onClick = { onItemClick(item) },
                            indication = null,
                            interactionSource = interaction,
                            modifier = Modifier.millenniumActionPointer().width(itemWidth ?: properties.itemWidth)
                                .heightIn(min = itemMinimumHeight ?: properties.itemMinimumHeight)
                                .graphicsLayer { scaleX = itemScale; scaleY = itemScale }
                                .alpha(animatedAlpha)
                                .millenniumSurface(
                                    resolvedItemBackground, itemShape ?: properties.itemShape,
                                    borderColor = animatedBorderColor,
                                    borderWidth = if (active || itemBorderColor != null || properties.itemBorderColor != null) {
                                        itemBorderWidth ?: properties.itemBorderWidth
                                    } else 0.dp,
                                    shadowElevation = itemElevation,
                                    innerGlowColor = itemGlowColor ?: properties.itemGlowColor
                                        ?: if (active) colors.highlight.copy(alpha = 0.35f) else null,
                                )
                                .millenniumInteractionSurface(state, itemShape ?: properties.itemShape, animationMillis = resolvedStateMillis),
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .heightIn(min = itemMinimumHeight ?: properties.itemMinimumHeight),
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                Text(
                                    item,
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(itemPadding ?: properties.itemPadding),
                                    style = itemTextStyle ?: properties.itemTextStyle,
                                    color = resolvedItemForeground,
                                )
                            }
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
