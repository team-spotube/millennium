package org.spotube.millennium.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
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
import org.spotube.millennium.theme.millenniumActionPointer
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumInteractionSurface

/**
 * Visual, layout and motion properties of a [MillenniumTabGroup].
 *
 * List-level properties (`list*`, `groupContentSpacing`, `tabShape`, `minimumTabHeight`, `tabBorderWidth`,
 * `tabElevation`, `panelAnimationMillis`) are read from the default state; tab-level colours, text style, padding, alpha
 * and `stateAnimationMillis` are resolved per tab from that tab's state.
 *
 * @property listBackground Fill of the tab list track; null uses a sunken bevel brush.
 * @property listShape Corner shape of the tab list (10dp rounded).
 * @property listBorderColor Border colour of the list; null uses the palette border colour.
 * @property listBorderWidth Border thickness of the list (1dp).
 * @property listPadding Padding between the list border and the tabs (4dp).
 * @property listInnerSpacing Gap between adjacent tabs (4dp).
 * @property groupContentSpacing Vertical gap between the tab list and the panel (16dp).
 * @property tabShape Corner shape of tabs and the sliding selection pill (8dp rounded).
 * @property selectedTabBackground Fill of the sliding selection pill; null uses the accent gel gradient.
 * @property unselectedTabBackground Fill of unselected tabs; null is transparent, with an accent wash when hovered or pressed.
 * @property selectedTabForeground Label colour of the selected tab; null uses the palette on-accent colour.
 * @property unselectedTabForeground Label colour of unselected tabs; null uses muted, or text when hovered/focused.
 * @property tabTextStyle Style of tab labels (14sp SemiBold).
 * @property minimumTabHeight Minimum tab height (44dp touch target).
 * @property tabPadding Padding inside each tab (12dp horizontal, 8dp vertical).
 * @property stateAnimationMillis Duration in milliseconds of tab alpha, label colour and interaction lighting changes.
 * @property panelAnimationMillis Base duration in milliseconds of the panel transition (200).
 * @property alpha Tab opacity.
 * @property tabBorderWidth Border width of the selection pill (1dp); keyboard focus draws at least 2dp.
 * @property tabElevation Shadow depth of the selection pill (2dp).
 * @property tabGlowColor Inner highlight of the selection pill; null uses the palette highlight at 50%.
 * @property selectedTabBorderColor Border colour of the selection pill; null falls back to the Selected state's
 *   `listBorderColor`, then the palette accent-bottom colour.
 * @property selectedTabBottomRimColor Colour of the pill's lower rim lip; null uses the palette highlight at 30%.
 * @property selectedTabOrbColor Colour of a glass orb overlay on the pill; null (default) draws none.
 * @property selectedTabOverlayBrush Brush drawn over the pill fill (gloss, texture); null (default) draws none.
 * @property selectedTabItemBackground Fill of the selected tab's own cell (above the pill, below the label); null is
 *   transparent.
 * @property tabHoverBackground Fill of an unselected tab while hovered when `unselectedTabBackground` is null; null
 *   uses an 8% to 3% accent wash.
 * @property tabPressedBackground Fill of an unselected tab while pressed when `unselectedTabBackground` is null; null
 *   uses a 14% to 7% accent wash.
 * @property tabHoverBorderColor Outline colour of a hovered unselected tab; null uses the palette border colour.
 * @property tabHoverBorderWidth Outline width of a hovered unselected tab (1dp).
 * @property tabFocusBorderColor Outline colour of a keyboard-focused tab; null uses the palette accent.
 * @property tabFocusBorderWidth Minimum outline width of a keyboard-focused tab (2dp).
 * @property tabHoverScale Scale of a hovered unselected tab (1.015).
 * @property tabPressedScale Scale of a pressed tab (0.96).
 * @property tabScaleDamping Damping ratio of the hover/press scale spring (0.70).
 * @property indicatorSpringDamping Damping ratio of the sliding selection pill spring (0.78).
 * @property panelSpringDamping Damping ratio of the panel enter springs (0.82).
 * @property panelEnterScale Initial scale of the entering panel (0.985).
 * @property panelExitScale Target scale of the leaving panel (0.99).
 * @property listInnerGlowColor Inner top rim colour of the list track; null draws none.
 * @property listBottomRimColor Lower lip colour of the list rim; null draws none.
 * @property listInsetShadeColor Colour of the shade along the inner top edge of the track; null draws none.
 * @property listShadowElevation Drop shadow of the list track; 0.dp (default) draws none.
 * @see MillenniumTabGroupTheme
 * @see defaultMillenniumTabGroupTheme
 */
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
    val panelAnimationMillis: Int = 200,
    val alpha: Float = 1f,
    val tabBorderWidth: Dp = 1.dp,
    val tabElevation: Dp = 2.dp,
    val tabGlowColor: Color? = null,
    val selectedTabBorderColor: Color? = null,
    val selectedTabBottomRimColor: Color? = null,
    val selectedTabOrbColor: Color? = null,
    val selectedTabOverlayBrush: Brush? = null,
    val selectedTabItemBackground: Brush? = null,
    val tabHoverBackground: Brush? = null,
    val tabPressedBackground: Brush? = null,
    val tabHoverBorderColor: Color? = null,
    val tabHoverBorderWidth: Dp = 1.dp,
    val tabFocusBorderColor: Color? = null,
    val tabFocusBorderWidth: Dp = 2.dp,
    val tabHoverScale: Float = 1.015f,
    val tabPressedScale: Float = 0.96f,
    val tabScaleDamping: Float = 0.70f,
    val indicatorSpringDamping: Float = 0.78f,
    val panelSpringDamping: Float = 0.82f,
    val panelEnterScale: Float = 0.985f,
    val panelExitScale: Float = 0.99f,
    val listInnerGlowColor: Color? = null,
    val listBottomRimColor: Color? = null,
    val listInsetShadeColor: Color? = null,
    val listShadowElevation: Dp = 0.dp,
)

/**
 * Per-state theme of [MillenniumTabGroup].
 *
 * @property states Properties per [MillenniumControlState]. Individual tabs resolve Pressed, keyboard Focused, Hovered,
 * Selected or Default; list-level values come from the default state (and the selection pill from Selected).
 */
@Immutable
data class MillenniumTabGroupTheme(
    val states: MillenniumControlStateProperty<MillenniumTabGroupThemeProperties>,
)

/**
 * Creates the default [MillenniumTabGroupTheme] installed as `MillenniumTheme.tabGroupTheme`.
 *
 * Defaults: a sunken 10dp rounded track holding equal-width 44dp tabs with a 4dp gap, a gel selection pill that
 * slides between tabs, 140ms state animation and a 200ms panel transition.
 *
 * @return A fresh theme instance.
 */
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

/**
 * A segmented tab strip with a sliding gel selection pill and an animated content panel.
 *
 * Built on Compose Unstyled's `UnstyledTabGroup`, so tab semantics, selection and keyboard navigation come from the
 * primitive. The tabs are generic: any type [T] with a label from [tabLabel]. Tabs share the row width equally. The
 * selected tab is highlighted by a gel pill whose position follows the selection with a spring; the panel for the
 * selected tab cross-fades and slides in the direction of the change.
 *
 * Selection is caller-owned. If [selectedTab] is not in [tabs] no pill is drawn and the panel still shows the state.
 * Tabs show the hand cursor, are at least 44dp tall, scale slightly on hover and press, and show the focus border only
 * for keyboard focus.
 *
 * Example:
 * ```kotlin
 * val tabs = listOf("Discover", "Saved", "Recent")
 * var selected by remember { mutableStateOf(tabs.first()) }
 * MillenniumTabGroup(
 *     selectedTab = selected,
 *     onSelectedTabChange = { selected = it },
 *     tabs = tabs,
 * ) { tab ->
 *     MillenniumText("Content for $tab")
 * }
 * ```
 *
 * @param T The tab key type; must have stable equality because it identifies tabs and drives selection.
 * @param selectedTab The currently selected tab (caller-owned).
 * @param onSelectedTabChange Called with the tab the user selected.
 * @param tabs All tabs in display order.
 * @param modifier Modifier applied to the whole group (list and panel).
 * @param tabLabel Maps a tab to its label; defaults to `toString()`.
 * @param listBackground Overrides the list track fill; null uses the theme, then a sunken bevel brush.
 * @param listShape Overrides the list corner shape; null uses the theme.
 * @param listBorderColor Overrides the list border colour (the selection pill border also takes this value when
 * set); null uses the theme, then the palette border colour (accent-bottom for the pill).
 * @param listBorderWidth Overrides the list border thickness; null uses the theme.
 * @param listPadding Overrides the padding inside the list; null uses the theme.
 * @param listInnerSpacing Overrides the gap between tabs; null uses the theme.
 * @param groupContentSpacing Overrides the gap between list and panel; null uses the theme.
 * @param tabShape Overrides the tab and pill shape; null uses the theme.
 * @param selectedTabBackground Overrides the selection pill fill; null uses the theme, then the accent gel.
 * @param unselectedTabBackground Overrides the unselected tab fill; null uses the theme or the hover wash.
 * @param selectedTabForeground Overrides the selected label colour; null uses the theme, then on-accent.
 * @param unselectedTabForeground Overrides the unselected label colour; null uses the theme, then muted/text.
 * @param tabTextStyle Overrides the label style; null uses the theme.
 * @param tabBorderWidth Overrides the pill border width; null uses the theme.
 * @param tabElevation Overrides the pill shadow depth; null uses the theme.
 * @param tabGlowColor Overrides the pill inner highlight; null uses the theme, then the palette highlight.
 * @param minimumTabHeight Overrides the minimum tab height; null uses the theme (44dp).
 * @param tabPadding Overrides the padding inside each tab; null uses the tab's state value.
 * @param stateAnimationMillis Overrides tab state animation duration; null uses the tab's state value.
 * @param panelAnimationMillis Overrides the panel transition duration; null uses the theme.
 * @param alpha Overrides the tab opacity; null uses the tab's state value.
 * @param theme Per-state theme; defaults to `MillenniumTheme.tabGroupTheme`.
 * @param panel Content of a tab; invoked with the tab being shown and animated on selection change.
 * @see MillenniumTabGroupTheme
 * @see MillenniumSegmentedButton
 */
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
    panelAnimationMillis: Int? = null,
    alpha: Float? = null,
    theme: MillenniumTabGroupTheme = MillenniumTheme.tabGroupTheme,
    panel: @Composable (T) -> Unit,
) {
    val colors = MillenniumTheme.colors
    val groupProperties = theme.states.default
    val resolvedListBackground = listBackground ?: groupProperties.listBackground
        ?: millenniumBevelBrush(colors, sunken = true)
    val resolvedListBorderColor = listBorderColor ?: groupProperties.listBorderColor ?: colors.border
    val resolvedListInnerSpacing = listInnerSpacing ?: groupProperties.listInnerSpacing
    val resolvedMinimumTabHeight = minimumTabHeight ?: groupProperties.minimumTabHeight
    val resolvedTabShape = tabShape ?: groupProperties.tabShape
    val resolvedTabBorderWidth = tabBorderWidth ?: groupProperties.tabBorderWidth
    val resolvedTabElevation = tabElevation ?: groupProperties.tabElevation
    val resolvedPanelMillis = panelAnimationMillis ?: groupProperties.panelAnimationMillis
    val selectedIndex = tabs.indexOf(selectedTab).coerceAtLeast(0)
    val density = LocalDensity.current
    var measuredTabHeight by remember { mutableStateOf(resolvedMinimumTabHeight) }
    UnstyledTabGroup(selectedTab, onSelectedTabChange, tabs, modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(groupContentSpacing ?: groupProperties.groupContentSpacing)) {
            TabList(
                Modifier.fillMaxWidth().millenniumSurface(
                    background = resolvedListBackground,
                    shape = listShape ?: groupProperties.listShape,
                    borderColor = resolvedListBorderColor,
                    borderWidth = listBorderWidth ?: groupProperties.listBorderWidth,
                    shadowElevation = groupProperties.listShadowElevation,
                    innerGlowColor = groupProperties.listInnerGlowColor,
                    bottomRimColor = groupProperties.listBottomRimColor,
                    insetShadeColor = groupProperties.listInsetShadeColor,
                ).padding(listPadding ?: groupProperties.listPadding),
            ) {
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    val tabCount = tabs.size.coerceAtLeast(1)
                    val totalSpacing = resolvedListInnerSpacing * (tabCount - 1)
                    val slotWidth = (maxWidth - totalSpacing) / tabCount
                    val indicatorOffset by animateDpAsState(
                        targetValue = (slotWidth + resolvedListInnerSpacing) * selectedIndex,
                        animationSpec = spring(
                            dampingRatio = groupProperties.indicatorSpringDamping,
                            stiffness = Spring.StiffnessMediumLow,
                        ),
                    )
                    val selectedProps = theme.states.resolve(MillenniumControlState.Selected)
                    val resolvedSelectedBackground = selectedTabBackground
                        ?: selectedProps.selectedTabBackground
                        ?: gelBrush(colors.accentTop, colors.accentBottom)
                    val resolvedSelectedBorder = listBorderColor ?: selectedProps.selectedTabBorderColor ?: selectedProps.listBorderColor ?: colors.accentBottom
                    if (tabs.isNotEmpty() && selectedTab in tabs) {
                        Box(
                            Modifier
                                .offset(x = indicatorOffset)
                                .width(slotWidth)
                                .height(measuredTabHeight)
                                .millenniumSurface(
                                    background = resolvedSelectedBackground,
                                    shape = resolvedTabShape,
                                    borderColor = resolvedSelectedBorder,
                                    borderWidth = resolvedTabBorderWidth,
                                    shadowElevation = resolvedTabElevation,
                                    innerGlowColor = tabGlowColor ?: selectedProps.tabGlowColor
                                        ?: colors.highlight.copy(alpha = 0.50f),
                                    bottomRimColor = selectedProps.selectedTabBottomRimColor ?: colors.highlight.copy(alpha = 0.30f),
                                    orbColor = selectedProps.selectedTabOrbColor,
                                )
                                .then(
                                    if (selectedProps.selectedTabOverlayBrush != null) {
                                        Modifier.background(selectedProps.selectedTabOverlayBrush)
                                    } else {
                                        Modifier
                                    },
                                ),
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth()
                            .onSizeChanged { size ->
                                if (size.height > 0) {
                                    measuredTabHeight = with(density) { size.height.toDp() }
                                }
                            },
                        horizontalArrangement = Arrangement.spacedBy(resolvedListInnerSpacing),
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
                            val resolvedStateMillis = stateAnimationMillis ?: properties.stateAnimationMillis
                            val animatedAlpha by animateFloatAsState(
                                targetValue = alpha ?: properties.alpha,
                                animationSpec = tween(resolvedStateMillis),
                            )
                            val tabScale by animateFloatAsState(
                                targetValue = when {
                                    pressed -> properties.tabPressedScale
                                    hovered && !isSelected -> properties.tabHoverScale
                                    else -> 1f
                                },
                                animationSpec = spring(
                                    dampingRatio = properties.tabScaleDamping,
                                    stiffness = Spring.StiffnessMedium,
                                ),
                            )
                            val resolvedTabBackground = if (isSelected) {
                                selectedProps.selectedTabItemBackground ?: TransparentTabBrush
                            } else {
                                unselectedTabBackground
                                    ?: properties.unselectedTabBackground
                                    ?: if (pressed) {
                                        properties.tabPressedBackground ?: Brush.verticalGradient(
                                            listOf(colors.accent.copy(alpha = 0.14f), colors.accent.copy(alpha = 0.07f)),
                                        )
                                    } else if (hovered) {
                                        properties.tabHoverBackground ?: Brush.verticalGradient(
                                            listOf(colors.accent.copy(alpha = 0.08f), colors.accent.copy(alpha = 0.03f)),
                                        )
                                    } else {
                                        TransparentTabBrush
                                    }
                            }
                            val targetTabForeground = if (isSelected) {
                                selectedTabForeground ?: selectedProps.selectedTabForeground ?: properties.selectedTabForeground ?: colors.onAccent
                            } else {
                                unselectedTabForeground ?: properties.unselectedTabForeground
                                    ?: if (hovered || focused) colors.text else colors.muted
                            }
                            val resolvedTabForeground by animateColorAsState(
                                targetValue = targetTabForeground,
                                animationSpec = tween(resolvedStateMillis),
                            )
                            Tab(
                                key,
                                modifier = Modifier.millenniumActionPointer().weight(1f).heightIn(min = resolvedMinimumTabHeight)
                                    .graphicsLayer { scaleX = tabScale; scaleY = tabScale }
                                    .alpha(animatedAlpha)
                                    .millenniumSurface(
                                        background = resolvedTabBackground,
                                        shape = resolvedTabShape,
                                        borderColor = if (focused) {
                                            properties.tabFocusBorderColor ?: colors.accent
                                        } else if (hovered && !isSelected) {
                                            properties.tabHoverBorderColor ?: colors.border
                                        } else {
                                            null
                                        },
                                        borderWidth = if (focused) {
                                            maxOf(properties.tabFocusBorderWidth, resolvedTabBorderWidth)
                                        } else if (hovered && !isSelected) {
                                            properties.tabHoverBorderWidth
                                        } else {
                                            0.dp
                                        },
                                    )
                                    .millenniumInteractionSurface(state, resolvedTabShape, animationMillis = resolvedStateMillis)
                                    .padding(tabPadding ?: properties.tabPadding),
                                indication = null,
                                interactionSource = interaction,
                                contentAlignment = Alignment.Center,
                            ) {
                                CompositionLocalProvider(
                                    LocalTextStyle provides LocalTextStyle.current.merge(tabTextStyle ?: properties.tabTextStyle)
                                        .copy(color = resolvedTabForeground),
                                ) { Text(tabLabel(key)) }
                            }
                        }
                    }
                }
            }
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    val fromIndex = tabs.indexOf(initialState)
                    val toIndex = tabs.indexOf(targetState)
                    val forward = toIndex >= fromIndex
                    (fadeIn(tween(resolvedPanelMillis, easing = FastOutSlowInEasing)) +
                        slideInHorizontally(
                            animationSpec = spring(
                                dampingRatio = groupProperties.panelSpringDamping,
                                stiffness = Spring.StiffnessMediumLow,
                            ),
                            initialOffsetX = { width -> if (forward) width / 12 else -width / 12 },
                        ) +
                        scaleIn(
                            initialScale = groupProperties.panelEnterScale,
                            animationSpec = spring(
                                dampingRatio = groupProperties.panelSpringDamping,
                                stiffness = Spring.StiffnessMediumLow,
                            ),
                        )) togetherWith
                        (fadeOut(tween((resolvedPanelMillis * 0.65f).toInt().coerceAtLeast(80), easing = FastOutLinearInEasing)) +
                            slideOutHorizontally(
                                animationSpec = tween(
                                    (resolvedPanelMillis * 0.75f).toInt().coerceAtLeast(90),
                                    easing = FastOutLinearInEasing,
                                ),
                                targetOffsetX = { width -> if (forward) -width / 16 else width / 16 },
                            ) +
                            scaleOut(
                                targetScale = groupProperties.panelExitScale,
                                animationSpec = tween((resolvedPanelMillis * 0.75f).toInt().coerceAtLeast(90)),
                            ))
                },
            ) { tab ->
                TabPanel(tab) { panel(tab) }
            }
        }
    }
}

private val TransparentTabBrush: Brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))

@Preview
@Composable
private fun MillenniumTabGroupPreview() = MillenniumPreview {
    MillenniumTabGroup(
        selectedTab = "Discover",
        onSelectedTabChange = {},
        tabs = listOf("Discover", "Saved"),
    ) { tab -> MillenniumText(if (tab == "Discover") "A golden-hour mix" else "Your favorites") }
}
