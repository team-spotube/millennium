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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.Tab
import com.composeunstyled.TabList
import com.composeunstyled.TabPanel
import com.composeunstyled.Text
import com.composeunstyled.UnstyledTabGroup
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumTabGroupThemeProperties(
    val listBackground: Brush? = null,
    val listShape: Shape,
    val listBorderColor: Color? = null,
    val listBorderWidth: Dp,
    val listPadding: Dp,
    val listInnerSpacing: Dp,
    val groupContentSpacing: Dp,
    val tabShape: Shape,
    val selectedTabBackground: Brush? = null,
    val unselectedTabBackground: Brush? = null,
    val selectedTabForeground: Color? = null,
    val unselectedTabForeground: Color? = null,
    val tabTextStyle: TextStyle,
    val minimumTabHeight: Dp,
    val tabPadding: PaddingValues,
    val stateAnimationMillis: Int,
    val alpha: Float = 1f,
    val tabBorderWidth: Dp = 1.dp,
    val tabElevation: Dp = 2.dp,
    val tabGlowColor: Color? = null,
)

@Immutable
data class MillenniumTabGroupTheme(
    val states: MillenniumControlStateProperty<MillenniumTabGroupThemeProperties>,
)

fun defaultMillenniumTabGroupTheme(): MillenniumTabGroupTheme {
    val base = MillenniumTabGroupThemeProperties(
        listShape = RoundedCornerShape(10.dp),
        listBorderWidth = 1.dp,
        listPadding = 4.dp,
        listInnerSpacing = 4.dp,
        groupContentSpacing = 16.dp,
        tabShape = RoundedCornerShape(8.dp),
        tabTextStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
        minimumTabHeight = 44.dp,
        tabPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp),
        stateAnimationMillis = 140,
    )
    return MillenniumTabGroupTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(alpha = 0.97f),
            focused = base.copy(listBorderWidth = 2.dp),
            pressed = base.copy(alpha = 0.90f),
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(listBorderWidth = 2.dp),
        ),
    )
}

@Composable
fun <T> MillenniumTabGroup(
    selectedTab: T,
    onSelectedTabChange: (T) -> Unit,
    tabs: List<T>,
    modifier: Modifier = Modifier,
    tabLabel: (T) -> String = { it.toString() },
    listBackground: Brush? = null,
    listShape: Shape? = null,
    listBorderColor: Color? = null,
    listBorderWidth: Dp? = null,
    listPadding: Dp? = null,
    listInnerSpacing: Dp? = null,
    groupContentSpacing: Dp? = null,
    tabShape: Shape? = null,
    selectedTabBackground: Brush? = null,
    unselectedTabBackground: Brush? = null,
    selectedTabForeground: Color? = null,
    unselectedTabForeground: Color? = null,
    tabTextStyle: TextStyle? = null,
    tabBorderWidth: Dp? = null,
    tabElevation: Dp? = null,
    tabGlowColor: Color? = null,
    minimumTabHeight: Dp? = null,
    tabPadding: PaddingValues? = null,
    stateAnimationMillis: Int? = null,
    alpha: Float? = null,
    theme: MillenniumTabGroupTheme = MillenniumTheme.tabGroupTheme,
    panel: @Composable (T) -> Unit,
) {
    val colors = MillenniumTheme.colors
    val groupProperties = theme.states.default
    val resolvedListBackground = listBackground ?: groupProperties.listBackground
        ?: millenniumBevelBrush(colors, sunken = true)
    val resolvedListBorderColor = listBorderColor ?: groupProperties.listBorderColor ?: colors.border
    UnstyledTabGroup(selectedTab, onSelectedTabChange, tabs, modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(groupContentSpacing ?: groupProperties.groupContentSpacing)) {
            TabList(
                Modifier.fillMaxWidth().millenniumSurface(
                    background = resolvedListBackground,
                    shape = listShape ?: groupProperties.listShape,
                    borderColor = resolvedListBorderColor,
                    borderWidth = listBorderWidth ?: groupProperties.listBorderWidth,
                ).padding(listPadding ?: groupProperties.listPadding),
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(listInnerSpacing ?: groupProperties.listInnerSpacing),
                ) {
                    tabs.forEach { key ->
                        val isSelected = key == selectedTab
                        val interaction = remember(key) { MutableInteractionSource() }
                        val pressed by interaction.collectIsPressedAsState()
                        val focused by interaction.collectIsKeyboardFocusedAsState()
                        val hovered by interaction.collectIsHoveredAsState()
                        val state = when {
                            pressed -> MillenniumControlState.Pressed
                            focused -> MillenniumControlState.Focused
                            hovered -> MillenniumControlState.Hovered
                            isSelected -> MillenniumControlState.Selected
                            else -> MillenniumControlState.Default
                        }
                        val properties = theme.states.resolve(state)
                        val animatedAlpha = animateFloatAsState(
                            targetValue = alpha ?: properties.alpha,
                            animationSpec = tween(stateAnimationMillis ?: properties.stateAnimationMillis),
                        ).value
                        val resolvedTabBackground = if (isSelected) selectedTabBackground
                            ?: properties.selectedTabBackground ?: gelBrush(colors.accentTop, colors.accentBottom)
                        else unselectedTabBackground
                            ?: properties.unselectedTabBackground
                            ?: Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
                        val resolvedTabForeground = if (isSelected) selectedTabForeground
                            ?: properties.selectedTabForeground ?: colors.onAccent
                        else unselectedTabForeground
                            ?: properties.unselectedTabForeground ?: colors.muted
                        val resolvedTabBorder = listBorderColor ?: properties.listBorderColor
                            ?: if (isSelected) colors.accentBottom else Color.Transparent
                        val resolvedTabBorderWidth = tabBorderWidth ?: properties.tabBorderWidth
                        Tab(
                            key,
                            modifier = Modifier.weight(1f).heightIn(min = minimumTabHeight ?: properties.minimumTabHeight)
                                .alpha(animatedAlpha)
                                .millenniumSurface(
                                    background = resolvedTabBackground,
                                    shape = tabShape ?: properties.tabShape,
                                    borderColor = if (focused) colors.accent else if (isSelected) resolvedTabBorder else null,
                                    borderWidth = if (focused) maxOf(2.dp, resolvedTabBorderWidth) else resolvedTabBorderWidth,
                                    shadowElevation = if (isSelected) (tabElevation ?: properties.tabElevation) else 0.dp,
                                    innerGlowColor = if (isSelected) {
                                        tabGlowColor ?: properties.tabGlowColor ?: colors.highlight.copy(alpha = 0.45f)
                                    } else null,
                                )
                                .padding(tabPadding ?: properties.tabPadding),
                            indication = LocalIndication.current,
                            interactionSource = interaction,
                            contentAlignment = androidx.compose.ui.Alignment.Center,
                        ) {
                            CompositionLocalProvider(
                                LocalTextStyle provides LocalTextStyle.current.merge(tabTextStyle ?: properties.tabTextStyle)
                                    .copy(color = resolvedTabForeground),
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
