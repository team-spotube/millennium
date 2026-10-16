package org.spotube.millennium.composables

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
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
import com.composeunstyled.AnchorAlignment
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
import org.spotube.millennium.theme.MillenniumStyledContent
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
    val maximumHeight: Dp = 320.dp,
    val iconSize: Dp = 18.dp,
    val iconLabelSpacing: Dp = 10.dp,
    val descriptionTextStyle: TextStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
    val shortcutTextStyle: TextStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
    val groupLabelStyle: TextStyle = TextStyle(
        fontSize = 11.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp,
    ),
    val groupLabelPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
    val sectionPadding: PaddingValues = PaddingValues(0.dp),
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


// ---------------------------------------------------------------------------
// Root
// ---------------------------------------------------------------------------

/**
 * A themed, caller-owned dropdown menu. Compose the panel from lego bricks:
 *
 * ```
 * MillenniumDropdownMenu(expanded, onExpandedChange, anchor = { ... }) {
 *     MillenniumDropdownMenuHeader { MillenniumDropdownMenuFilter(filterState) }  // fixed
 *     MillenniumDropdownMenuContent {                                             // scrolls
 *         MillenniumDropdownMenuGroup {
 *             MillenniumDropdownMenuLabel("Account")
 *             MillenniumDropdownMenuItem("Profile", onClick = {}, icon = MillenniumIcons.Music)
 *             MillenniumDropdownMenuCheckboxItem(checked, onChange, "Show lyrics")
 *         }
 *         MillenniumDropdownMenuSeparator()
 *         MillenniumDropdownMenuItem("Delete", onClick = {}, destructive = true)
 *     }
 *     MillenniumDropdownMenuFooter { MillenniumText("3 results") }                // fixed
 * }
 * ```
 *
 * Every brick is optional and any composable may sit between them.
 */
@Composable
fun MillenniumDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    side: AnchorSide = AnchorSide.Bottom,
    alignment: AnchorAlignment = AnchorAlignment.Start,
    width: Dp? = null,
    panelBackground: Brush? = null,
    panelShape: Shape? = null,
    panelBorderColor: Color? = null,
    panelBorderWidth: Dp? = null,
    panelShadowElevation: Dp? = null,
    panelGlowColor: Color? = null,
    minimumWidth: Dp? = null,
    sideOffset: Dp? = null,
    panelPadding: PaddingValues? = null,
    sectionSpacing: Dp? = null,
    openAnimationMillis: Int? = null,
    closeAnimationMillis: Int? = null,
    enterScale: Float? = null,
    exitScale: Float? = null,
    alpha: Float? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    anchor: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
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
    val enterSpring = spring<Float>(dampingRatio = 0.76f, stiffness = Spring.StiffnessMediumLow)
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
    val exitSpec = tween<Float>(resolvedCloseMillis, easing = FastOutLinearInEasing)
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
        alignment = alignment,
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
                        animationSpec = enterSpring,
                    ) + enterSlide,
                exit = fadeOut(tween(resolvedCloseMillis, easing = FastOutLinearInEasing)) +
                    scaleOut(
                        targetScale = resolvedExitScale,
                        transformOrigin = transformOrigin,
                        animationSpec = exitSpec,
                    ) + exitSlide,
            ) {
                Column(
                    modifier = Modifier.width(width ?: panelProperties.itemWidth),
                    verticalArrangement = Arrangement.spacedBy(sectionSpacing ?: panelProperties.itemSpacing),
                ) {
                    MillenniumStyledContent(panelProperties.itemTextStyle, colors.text) { content() }
                }
            }
        },
    )
}

/**
 * Convenience menu for a plain list of strings, built from the same bricks as the
 * composable overload. Reach for the lego overload when rows need icons, groups,
 * toggles, separators or any custom content.
 */
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
    maximumHeight: Dp? = null,
    filterable: Boolean = false,
    onFilter: ((query: String) -> List<String>)? = null,
    filterPlaceholder: String = "Filter",
    emptyText: String = "No results",
    stateAnimationMillis: Int? = null,
    openAnimationMillis: Int? = null,
    closeAnimationMillis: Int? = null,
    enterScale: Float? = null,
    exitScale: Float? = null,
    alpha: Float? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    anchor: @Composable () -> Unit,
) {
    val filterState = rememberTextFieldState()
    LaunchedEffect(expanded) { if (!expanded) filterState.clearText() }
    val query = filterState.text.toString()
    val visibleItems = when {
        !filterable || query.isBlank() -> items
        onFilter != null -> onFilter(query)
        else -> items.filter { it.contains(query.trim(), ignoreCase = true) }
    }
    MillenniumDropdownMenu(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        side = side,
        width = itemWidth,
        panelBackground = panelBackground,
        panelShape = panelShape,
        panelBorderColor = panelBorderColor,
        panelBorderWidth = panelBorderWidth,
        panelShadowElevation = panelShadowElevation,
        panelGlowColor = panelGlowColor,
        minimumWidth = minimumWidth,
        sideOffset = sideOffset,
        panelPadding = panelPadding,
        sectionSpacing = itemSpacing,
        openAnimationMillis = openAnimationMillis,
        closeAnimationMillis = closeAnimationMillis,
        enterScale = enterScale,
        exitScale = exitScale,
        alpha = alpha,
        theme = theme,
        anchor = anchor,
    ) {
        if (filterable) {
            MillenniumDropdownMenuHeader(showDivider = false, theme = theme) {
                MillenniumDropdownMenuFilter(filterState, placeholder = filterPlaceholder)
            }
        }
        MillenniumDropdownMenuContent(
            maximumHeight = maximumHeight,
            contentSpacing = itemSpacing,
            theme = theme,
        ) {
            if (visibleItems.isEmpty()) MillenniumDropdownMenuEmpty(emptyText, textStyle = itemTextStyle, theme = theme)
            visibleItems.forEach { item ->
                MillenniumDropdownMenuItem(
                    label = item,
                    onClick = { onItemClick(item) },
                    background = itemBackground,
                    foreground = itemForeground,
                    shape = itemShape,
                    textStyle = itemTextStyle,
                    minimumHeight = itemMinimumHeight,
                    contentPadding = itemPadding,
                    borderColor = itemBorderColor,
                    borderWidth = itemBorderWidth,
                    innerGlowColor = itemGlowColor,
                    stateAnimationMillis = stateAnimationMillis,
                    theme = theme,
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Sections: header / content / footer
// ---------------------------------------------------------------------------

/** Fixed top region (filters, titles, account summaries). Stays put while content scrolls. */
@Composable
fun MillenniumDropdownMenuHeader(
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
    contentPadding: PaddingValues? = null,
    contentSpacing: Dp? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    content: @Composable ColumnScope.() -> Unit,
) {
    val properties = theme.states.default
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(properties.itemSpacing)) {
        Column(
            Modifier.fillMaxWidth().padding(contentPadding ?: properties.sectionPadding),
            verticalArrangement = Arrangement.spacedBy(contentSpacing ?: properties.itemSpacing),
        ) { content() }
        if (showDivider) MillenniumDropdownMenuSeparator(theme = theme)
    }
}

/**
 * The scrollable region. Its height is capped at [maximumHeight] and a themed
 * scrollbar appears only when the rows actually overflow.
 */
@Composable
fun MillenniumDropdownMenuContent(
    modifier: Modifier = Modifier,
    maximumHeight: Dp? = null,
    contentSpacing: Dp? = null,
    showScrollbar: Boolean = true,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    content: @Composable ColumnScope.() -> Unit,
) {
    val properties = theme.states.default
    val scrollState = rememberScrollState()
    val overflowing = showScrollbar && scrollState.maxValue > 0
    Box(modifier.fillMaxWidth().heightIn(max = maximumHeight ?: properties.maximumHeight)) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(scrollState)
                .padding(end = if (overflowing) 8.dp else 0.dp),
            verticalArrangement = Arrangement.spacedBy(contentSpacing ?: properties.itemSpacing),
        ) { content() }
        if (overflowing) {
            MillenniumScrollbar(scrollState, Modifier.align(Alignment.CenterEnd).fillMaxHeight())
        }
    }
}

/** Fixed bottom region (actions, counts, hints). */
@Composable
fun MillenniumDropdownMenuFooter(
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
    contentPadding: PaddingValues? = null,
    contentSpacing: Dp? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    content: @Composable ColumnScope.() -> Unit,
) {
    val properties = theme.states.default
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(properties.itemSpacing)) {
        if (showDivider) MillenniumDropdownMenuSeparator(theme = theme)
        Column(
            Modifier.fillMaxWidth().padding(contentPadding ?: properties.sectionPadding),
            verticalArrangement = Arrangement.spacedBy(contentSpacing ?: properties.itemSpacing),
        ) { content() }
    }
}

/** A search field wired to caller-owned [state], usually placed in a header. */
@Composable
fun MillenniumDropdownMenuFilter(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String = "Filter",
    enabled: Boolean = true,
) {
    MillenniumTextField(
        state = state,
        placeholder = placeholder,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
    )
}

// ---------------------------------------------------------------------------
// Groups, labels, separators, empty state
// ---------------------------------------------------------------------------

/** A cluster of related rows, typically opened by a [MillenniumDropdownMenuLabel]. */
@Composable
fun MillenniumDropdownMenuGroup(
    modifier: Modifier = Modifier,
    contentSpacing: Dp? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(contentSpacing ?: theme.states.default.itemSpacing),
    ) { content() }
}

/** Non-interactive heading for a group. Any composable may go inside. */
@Composable
fun MillenniumDropdownMenuLabel(
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null,
    foreground: Color? = null,
    contentPadding: PaddingValues? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    content: @Composable () -> Unit,
) {
    val properties = theme.states.default
    Box(modifier.fillMaxWidth().padding(contentPadding ?: properties.groupLabelPadding)) {
        MillenniumStyledContent(
            textStyle ?: properties.groupLabelStyle,
            foreground ?: MillenniumTheme.colors.muted,
            content,
        )
    }
}

@Composable
fun MillenniumDropdownMenuLabel(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null,
    foreground: Color? = null,
    contentPadding: PaddingValues? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
) {
    MillenniumDropdownMenuLabel(modifier, textStyle, foreground, contentPadding, theme) { Text(text) }
}

/** A hairline between groups. */
@Composable
fun MillenniumDropdownMenuSeparator(
    modifier: Modifier = Modifier,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
) {
    MillenniumSeparator(modifier.padding(vertical = 2.dp))
}

/** Placeholder shown when a filtered list has no rows. Any composable may go inside. */
@Composable
fun MillenniumDropdownMenuEmpty(
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null,
    contentPadding: PaddingValues? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    content: @Composable () -> Unit,
) {
    val properties = theme.states.default
    Box(modifier.fillMaxWidth().padding(contentPadding ?: properties.itemPadding)) {
        MillenniumStyledContent(textStyle ?: properties.itemTextStyle, MillenniumTheme.colors.muted, content)
    }
}

@Composable
fun MillenniumDropdownMenuEmpty(
    text: String = "No results",
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null,
    contentPadding: PaddingValues? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
) {
    MillenniumDropdownMenuEmpty(modifier, textStyle, contentPadding, theme) { Text(text) }
}


// ---------------------------------------------------------------------------
// Items
// ---------------------------------------------------------------------------

/** Visual facts a row exposes to a custom content slot. */
@Immutable
data class MillenniumDropdownMenuItemVisuals(
    val state: MillenniumControlState,
    val selected: Boolean,
    val destructive: Boolean,
    val foreground: Color,
    val mutedForeground: Color,
    val iconTint: Color,
)

/** Hands the resolved row colours to any composable inside a row, e.g. [MillenniumDropdownMenuItemIcon]. */
val LocalMillenniumDropdownMenuItemVisuals = staticCompositionLocalOf<MillenniumDropdownMenuItemVisuals?> { null }

/** An icon that follows the row's state colours (white on a hovered gel row, red when destructive). */
@Composable
fun MillenniumDropdownMenuItemIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color? = null,
    size: Dp? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
) {
    MillenniumIcon(
        imageVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint ?: LocalMillenniumDropdownMenuItemVisuals.current?.iconTint,
        size = size ?: theme.states.default.iconSize,
    )
}

/**
 * A standard row where every part is a slot: [leading] (icon, avatar, indicator),
 * the [label] content, an optional [description] beneath it, a [shortcut] hint and
 * a [trailing] slot. Text inside the slots inherits the row's style and colours.
 * [selected] marks the current value with [selectedIndicator]; [destructive]
 * switches to the danger palette.
 */
@Composable
fun MillenniumDropdownMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    destructive: Boolean = false,
    closeOnClick: Boolean = true,
    leading: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    shortcut: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    selectedIndicator: (@Composable () -> Unit)? = { MillenniumDropdownMenuItemIcon(MillenniumIcons.Check) },
    background: Brush? = null,
    foreground: Color? = null,
    shape: Shape? = null,
    textStyle: TextStyle? = null,
    minimumHeight: Dp? = null,
    contentPadding: PaddingValues? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    innerGlowColor: Color? = null,
    stateAnimationMillis: Int? = null,
    alpha: Float? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    label: @Composable () -> Unit,
) {
    val base = theme.states.default
    MillenniumDropdownMenuItemContainer(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        selected = selected,
        destructive = destructive,
        closeOnClick = closeOnClick,
        background = background,
        foreground = foreground,
        shape = shape,
        minimumHeight = minimumHeight,
        contentPadding = contentPadding,
        borderColor = borderColor,
        borderWidth = borderWidth,
        innerGlowColor = innerGlowColor,
        stateAnimationMillis = stateAnimationMillis,
        alpha = alpha,
        theme = theme,
    ) { visuals ->
        if (leading != null) {
            leading()
            Spacer(Modifier.width(base.iconLabelSpacing))
        }
        Column(Modifier.weight(1f)) {
            MillenniumStyledContent(textStyle ?: base.itemTextStyle, visuals.foreground, label)
            if (description != null) {
                MillenniumStyledContent(base.descriptionTextStyle, visuals.mutedForeground, description)
            }
        }
        if (shortcut != null) {
            Spacer(Modifier.width(base.iconLabelSpacing))
            MillenniumStyledContent(base.shortcutTextStyle, visuals.mutedForeground, shortcut)
        }
        if (trailing != null) {
            Spacer(Modifier.width(base.iconLabelSpacing))
            trailing()
        }
        if (selected && selectedIndicator != null) {
            Spacer(Modifier.width(base.iconLabelSpacing))
            selectedIndicator()
        }
    }
}

/** String/icon convenience over the slotted [MillenniumDropdownMenuItem]. */
@Composable
fun MillenniumDropdownMenuItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    destructive: Boolean = false,
    closeOnClick: Boolean = true,
    icon: ImageVector? = null,
    description: String? = null,
    shortcut: String? = null,
    trailing: (@Composable () -> Unit)? = null,
    background: Brush? = null,
    foreground: Color? = null,
    shape: Shape? = null,
    textStyle: TextStyle? = null,
    minimumHeight: Dp? = null,
    contentPadding: PaddingValues? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    innerGlowColor: Color? = null,
    stateAnimationMillis: Int? = null,
    alpha: Float? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
) {
    MillenniumDropdownMenuItem(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        selected = selected,
        destructive = destructive,
        closeOnClick = closeOnClick,
        leading = icon?.let { { MillenniumDropdownMenuItemIcon(it, theme = theme) } },
        description = description?.let { { Text(it) } },
        shortcut = shortcut?.let { { Text(it) } },
        trailing = trailing,
        background = background,
        foreground = foreground,
        shape = shape,
        textStyle = textStyle,
        minimumHeight = minimumHeight,
        contentPadding = contentPadding,
        borderColor = borderColor,
        borderWidth = borderWidth,
        innerGlowColor = innerGlowColor,
        stateAnimationMillis = stateAnimationMillis,
        alpha = alpha,
        theme = theme,
    ) { Text(label) }
}

/** The raw row surface: a row with fully custom content that receives the resolved [MillenniumDropdownMenuItemVisuals]. */
@Composable
fun MillenniumDropdownMenuItemContainer(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    destructive: Boolean = false,
    closeOnClick: Boolean = true,
    background: Brush? = null,
    foreground: Color? = null,
    shape: Shape? = null,
    minimumHeight: Dp? = null,
    contentPadding: PaddingValues? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    innerGlowColor: Color? = null,
    stateAnimationMillis: Int? = null,
    alpha: Float? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    content: @Composable RowScope.(MillenniumDropdownMenuItemVisuals) -> Unit,
) {
    val colors = MillenniumTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsKeyboardFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val state = when {
        !enabled -> MillenniumControlState.Disabled
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        selected -> MillenniumControlState.Selected
        else -> MillenniumControlState.Default
    }
    val properties = theme.states.resolve(state)
    val resolvedStateMillis = stateAnimationMillis ?: properties.stateAnimationMillis
    val active = enabled && (hovered || focused || pressed)
    val resolvedShape = shape ?: properties.itemShape
    val resolvedBackground = background ?: properties.itemBackground ?: when {
        active && destructive -> Brush.verticalGradient(
            listOf(colors.danger.copy(alpha = 0.18f), colors.danger.copy(alpha = 0.10f)),
        )
        active -> millenniumBevelBrush(colors, accent = true)
        selected && enabled -> Brush.verticalGradient(
            listOf(colors.accent.copy(alpha = 0.14f), colors.accent.copy(alpha = 0.07f)),
        )
        else -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
    }
    val onFilled = active && !destructive
    val targetForeground = foreground ?: properties.itemForeground ?: when {
        !enabled -> colors.muted
        destructive -> colors.danger
        onFilled -> colors.onAccent
        else -> colors.text
    }
    val animatedForeground by animateColorAsState(targetForeground, tween(resolvedStateMillis))
    val mutedForeground by animateColorAsState(
        when {
            onFilled -> colors.onAccent.copy(alpha = 0.82f)
            destructive && enabled -> colors.danger.copy(alpha = 0.8f)
            else -> colors.muted
        },
        tween(resolvedStateMillis),
    )
    val iconTint by animateColorAsState(
        when {
            !enabled -> colors.muted
            destructive -> colors.danger
            onFilled -> colors.onAccent
            selected -> colors.accent
            else -> colors.muted
        },
        tween(resolvedStateMillis),
    )
    val targetBorderColor = borderColor ?: properties.itemBorderColor
        ?: if (active) (if (destructive) colors.danger else colors.accentBottom) else Color.Transparent
    val animatedBorderColor by animateColorAsState(targetBorderColor, tween(resolvedStateMillis))
    val animatedAlpha by animateFloatAsState(alpha ?: properties.alpha, tween(resolvedStateMillis))
    val itemScale by animateFloatAsState(
        targetValue = when {
            !enabled -> 1f
            pressed -> 0.975f
            hovered || focused -> 1.012f
            else -> 1f
        },
        animationSpec = spring(dampingRatio = 0.72f, stiffness = Spring.StiffnessMedium),
    )
    val itemElevation by animateDpAsState(
        targetValue = if (active && !pressed) 2.dp else 0.dp,
        animationSpec = tween(resolvedStateMillis),
    )
    val visuals = remember(state, selected, destructive, animatedForeground, mutedForeground, iconTint) {
        MillenniumDropdownMenuItemVisuals(
            state, selected, destructive, animatedForeground, mutedForeground, iconTint,
        )
    }
    UnstyledDropdownMenuItem(
        onClick = onClick,
        enabled = enabled,
        closeOnClick = closeOnClick,
        indication = null,
        interactionSource = interaction,
        modifier = modifier.millenniumActionPointer(enabled).fillMaxWidth()
            .heightIn(min = minimumHeight ?: properties.itemMinimumHeight)
            .graphicsLayer { scaleX = itemScale; scaleY = itemScale }
            .alpha(animatedAlpha)
            .millenniumSurface(
                resolvedBackground, resolvedShape,
                borderColor = animatedBorderColor,
                borderWidth = if (active || borderColor != null || properties.itemBorderColor != null) {
                    borderWidth ?: properties.itemBorderWidth
                } else 0.dp,
                shadowElevation = itemElevation,
                // Translucent washes must not paint a shadow behind themselves.
                opaqueFill = onFilled || background != null,
                innerGlowColor = innerGlowColor ?: properties.itemGlowColor
                    ?: if (onFilled) colors.highlight.copy(alpha = 0.35f) else null,
            )
            .millenniumInteractionSurface(state, resolvedShape, animationMillis = resolvedStateMillis),
    ) {
        Row(
            Modifier.fillMaxWidth()
                .heightIn(min = minimumHeight ?: properties.itemMinimumHeight)
                .padding(contentPadding ?: properties.itemPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(LocalMillenniumDropdownMenuItemVisuals provides visuals) { content(visuals) }
        }
    }
}

// ---------------------------------------------------------------------------
// Checkbox & radio items
// ---------------------------------------------------------------------------

/**
 * A toggleable row; [label], [description] and [shortcut] are slots. Stays open by
 * default so several options can be changed in one visit.
 */
@Composable
fun MillenniumDropdownMenuCheckboxItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    closeOnClick: Boolean = false,
    indicator: @Composable () -> Unit = { MillenniumDropdownMenuItemIcon(MillenniumIcons.Check) },
    description: (@Composable () -> Unit)? = null,
    shortcut: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    label: @Composable () -> Unit,
) {
    MillenniumDropdownMenuItem(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier.semantics(mergeDescendants = true) {
            role = Role.Checkbox
            toggleableState = ToggleableState(checked)
        },
        enabled = enabled,
        closeOnClick = closeOnClick,
        leading = { DropdownIndicatorSlot(checked, theme.states.default.iconSize, indicator) },
        description = description,
        shortcut = shortcut,
        trailing = trailing,
        theme = theme,
        label = label,
    )
}

@Composable
fun MillenniumDropdownMenuCheckboxItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    closeOnClick: Boolean = false,
    description: String? = null,
    shortcut: String? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
) {
    MillenniumDropdownMenuCheckboxItem(
        checked, onCheckedChange, modifier, enabled, closeOnClick,
        description = description?.let { { Text(it) } },
        shortcut = shortcut?.let { { Text(it) } },
        theme = theme,
    ) { Text(label) }
}

@Stable
private class DropdownRadioScope(
    val value: Any?,
    val onValueChange: (Any?) -> Unit,
)

private val LocalDropdownRadioScope = staticCompositionLocalOf<DropdownRadioScope?> { null }

/** Shares one selected [value] among the [MillenniumDropdownMenuRadioItem]s inside it. */
@Composable
fun <T> MillenniumDropdownMenuRadioGroup(
    value: T,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scope = remember(value, onValueChange) {
        @Suppress("UNCHECKED_CAST")
        DropdownRadioScope(value) { onValueChange(it as T) }
    }
    CompositionLocalProvider(LocalDropdownRadioScope provides scope) {
        MillenniumDropdownMenuGroup(modifier, theme = theme, content = content)
    }
}

/** One choice in a [MillenniumDropdownMenuRadioGroup]; [label] and [description] are slots. */
@Composable
fun <T> MillenniumDropdownMenuRadioItem(
    value: T,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    closeOnClick: Boolean = false,
    indicator: @Composable () -> Unit = {
        Box(
            Modifier.size(8.dp).clip(CircleShape)
                .background(LocalMillenniumDropdownMenuItemVisuals.current?.iconTint ?: MillenniumTheme.colors.accent),
        )
    },
    description: (@Composable () -> Unit)? = null,
    shortcut: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
    label: @Composable () -> Unit,
) {
    val group = LocalDropdownRadioScope.current
    val isSelected = group?.value == value
    MillenniumDropdownMenuItem(
        onClick = { group?.onValueChange(value) },
        modifier = modifier.semantics(mergeDescendants = true) {
            role = Role.RadioButton
            selected = isSelected
        },
        enabled = enabled,
        closeOnClick = closeOnClick,
        leading = { DropdownIndicatorSlot(isSelected, theme.states.default.iconSize, indicator) },
        description = description,
        shortcut = shortcut,
        trailing = trailing,
        theme = theme,
        label = label,
    )
}

@Composable
fun <T> MillenniumDropdownMenuRadioItem(
    value: T,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    closeOnClick: Boolean = false,
    description: String? = null,
    theme: MillenniumDropdownMenuTheme = MillenniumTheme.dropdownMenuTheme,
) {
    MillenniumDropdownMenuRadioItem(
        value, modifier, enabled, closeOnClick,
        description = description?.let { { Text(it) } },
        theme = theme,
    ) { Text(label) }
}

@Composable
private fun DropdownIndicatorSlot(visible: Boolean, size: Dp, content: @Composable () -> Unit) {
    Box(Modifier.size(size), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = visible,
            enter = scaleIn(spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium), initialScale = 0.4f) + fadeIn(),
            exit = scaleOut(targetScale = 0.4f) + fadeOut(),
        ) { content() }
    }
}


@Preview
@Composable
private fun MillenniumDropdownMenuPreview() = MillenniumPreview {
    var showLyrics by remember { mutableStateOf(true) }
    var quality by remember { mutableStateOf("High") }
    val filter = rememberTextFieldState()
    MillenniumDropdownMenu(
        expanded = true,
        onExpandedChange = {},
        anchor = { MillenniumButton(onClick = {}) { Text("Track options") } },
    ) {
        MillenniumDropdownMenuHeader { MillenniumDropdownMenuFilter(filter, placeholder = "Search actions") }
        MillenniumDropdownMenuContent {
            MillenniumDropdownMenuGroup {
                MillenniumDropdownMenuLabel { Text("Track") }
                MillenniumDropdownMenuItem("Add to playlist", onClick = {}, icon = MillenniumIcons.Music, shortcut = "Ctrl+P")
                MillenniumDropdownMenuItem("Share", onClick = {}, description = "Copy a link", icon = MillenniumIcons.Sparkle)
                MillenniumDropdownMenuItem(
                    onClick = {},
                    leading = { MillenniumAvatar("K", "Kingkor", size = 28.dp) },
                    description = { Text("Following") },
                    trailing = { MillenniumSidebarBadge("3") },
                ) { Text("Kingkor Roy", fontWeight = FontWeight.SemiBold) }
                MillenniumDropdownMenuItem("Unavailable", onClick = {}, enabled = false)
            }
            MillenniumDropdownMenuSeparator()
            MillenniumDropdownMenuGroup {
                MillenniumDropdownMenuLabel("Playback")
                MillenniumDropdownMenuCheckboxItem(showLyrics, { showLyrics = it }, shortcut = { Text("L") }) { Text("Show lyrics") }
                MillenniumDropdownMenuRadioGroup(quality, { quality = it }) {
                    listOf("Low", "High", "Lossless").forEach { MillenniumDropdownMenuRadioItem(it, it) }
                }
            }
            MillenniumDropdownMenuSeparator()
            MillenniumDropdownMenuItem("Remove from library", onClick = {}, destructive = true)
        }
        MillenniumDropdownMenuFooter { MillenniumText("Quality: $quality", color = MillenniumTheme.colors.muted, size = 11.sp) }
    }
}
