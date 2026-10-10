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
    panel: @Composable (T) -> Unit,
) {
    val colors = MillenniumTheme.colors
    val groupProperties = MillenniumTheme.tabGroupTheme.states.default
    val listBackground = groupProperties.listBackground ?: millenniumBevelBrush(colors, sunken = true)
    val listBorderColor = groupProperties.listBorderColor ?: colors.border
    UnstyledTabGroup(selectedTab, onSelectedTabChange, tabs, modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(groupProperties.groupContentSpacing)) {
            TabList(
                Modifier.fillMaxWidth().millenniumSurface(
                    background = listBackground,
                    shape = groupProperties.listShape,
                    borderColor = listBorderColor,
                    borderWidth = groupProperties.listBorderWidth,
                ).padding(groupProperties.listPadding),
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(groupProperties.listInnerSpacing),
                ) {
                    tabs.forEach { key ->
                        val isSelected = key == selectedTab
                        val interaction = remember(key) { MutableInteractionSource() }
                        val pressed by interaction.collectIsPressedAsState()
                        val focused by interaction.collectIsFocusedAsState()
                        val hovered by interaction.collectIsHoveredAsState()
                        val state = when {
                            pressed -> MillenniumControlState.Pressed
                            focused -> MillenniumControlState.Focused
                            hovered -> MillenniumControlState.Hovered
                            isSelected -> MillenniumControlState.Selected
                            else -> MillenniumControlState.Default
                        }
                        val properties = MillenniumTheme.tabGroupTheme.states.resolve(state)
                        val animatedAlpha = animateFloatAsState(
                            targetValue = properties.alpha,
                            animationSpec = tween(properties.stateAnimationMillis),
                        ).value
                        val tabBackground = if (isSelected) properties.selectedTabBackground
                            ?: gelBrush(colors.accentTop, colors.accentBottom)
                        else properties.unselectedTabBackground
                            ?: Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
                        val tabForeground = if (isSelected) properties.selectedTabForeground ?: colors.onAccent
                            else properties.unselectedTabForeground ?: colors.muted
                        val tabBorder = properties.listBorderColor ?: if (isSelected) colors.accentBottom else Color.Transparent
                        Tab(
                            key,
                            modifier = Modifier.weight(1f).heightIn(min = properties.minimumTabHeight)
                                .alpha(animatedAlpha)
                                .millenniumSurface(
                                    background = tabBackground,
                                    shape = properties.tabShape,
                                    borderColor = if (focused) colors.accent else if (isSelected) tabBorder else null,
                                    borderWidth = if (focused) maxOf(2.dp, properties.tabBorderWidth) else properties.tabBorderWidth,
                                    shadowElevation = if (isSelected) properties.tabElevation else 0.dp,
                                    innerGlowColor = if (isSelected) properties.tabGlowColor ?: colors.highlight.copy(alpha = 0.45f) else null,
                                )
                                .padding(properties.tabPadding),
                            indication = LocalIndication.current,
                            interactionSource = interaction,
                            contentAlignment = androidx.compose.ui.Alignment.Center,
                        ) {
                            CompositionLocalProvider(
                                LocalTextStyle provides LocalTextStyle.current.merge(properties.tabTextStyle)
                                    .copy(color = tabForeground),
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
