package org.spotube.millennium.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import com.composeunstyled.UnstyledButton
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

// ---------------------------------------------------------------------------
// Theme
// ---------------------------------------------------------------------------

/** Raised shell that frames a sidebar: width, surface, shape and motion. */
@Immutable
data class MillenniumSidebarRootProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val width: Dp,
    val collapsedWidth: Dp,
    val shape: Shape,
    val borderColor: Color? = null,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val innerGlowColor: Color? = null,
    val stateAnimationMillis: Int,
    val alpha: Float = 1f,
)

/** Fixed top region of a sidebar. */
@Immutable
data class MillenniumSidebarHeaderProperties(
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val minimumHeight: Dp,
    val background: Brush? = null,
    val dividerColor: Color? = null,
    val dividerWidth: Dp,
)

/** The scrollable middle region of a sidebar. */
@Immutable
data class MillenniumSidebarContentProperties(
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val showScrollbar: Boolean,
    val scrollbarEndPadding: Dp,
)

/** Fixed bottom region of a sidebar. */
@Immutable
data class MillenniumSidebarFooterProperties(
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val minimumHeight: Dp,
    val background: Brush? = null,
    val dividerColor: Color? = null,
    val dividerWidth: Dp,
)

/** A collapsible cluster of items with its own label. */
@Immutable
data class MillenniumSidebarGroupProperties(
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val groupSpacing: Dp,
    val labelStyle: TextStyle,
    val labelForeground: Color? = null,
    val labelPadding: PaddingValues,
    val labelMinimumHeight: Dp,
    val labelShape: Shape,
    val labelHoveredBackground: Brush? = null,
    val labelBorderColor: Color? = null,
    val labelBorderWidth: Dp,
    val animationMillis: Int,
    val alpha: Float = 1f,
)

/** One selectable sidebar row. */
@Immutable
data class MillenniumSidebarItemProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val iconTint: Color? = null,
    val labelStyle: TextStyle,
    val shape: Shape,
    val contentPadding: PaddingValues,
    val minimumHeight: Dp,
    val iconSize: Dp,
    val iconLabelSpacing: Dp,
    val borderColor: Color? = null,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val innerGlowColor: Color? = null,
    val selectedIndicatorColor: Color? = null,
    val selectedIndicatorWidth: Dp,
    val selectedIndicatorHeight: Dp,
    val stateAnimationMillis: Int,
    val alpha: Float = 1f,
)

/** Compact trailing counter or status pill. */
@Immutable
data class MillenniumSidebarBadgeProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val textStyle: TextStyle,
    val shape: Shape,
    val contentPadding: PaddingValues,
    val minimumHeight: Dp,
)

/** The collapse/expand affordance, usually placed in the header. */
@Immutable
data class MillenniumSidebarTriggerProperties(
    val foreground: Color? = null,
    val shape: Shape,
    val size: Dp,
    val animationMillis: Int,
)

/** Complete, independently replaceable theme for every sidebar brick. */
@Immutable
data class MillenniumSidebarTheme(
    val root: MillenniumControlStateProperty<MillenniumSidebarRootProperties>,
    val header: MillenniumControlStateProperty<MillenniumSidebarHeaderProperties>,
    val content: MillenniumControlStateProperty<MillenniumSidebarContentProperties>,
    val footer: MillenniumControlStateProperty<MillenniumSidebarFooterProperties>,
    val group: MillenniumControlStateProperty<MillenniumSidebarGroupProperties>,
    val item: MillenniumControlStateProperty<MillenniumSidebarItemProperties>,
    val badge: MillenniumControlStateProperty<MillenniumSidebarBadgeProperties>,
    val trigger: MillenniumControlStateProperty<MillenniumSidebarTriggerProperties>,
)

fun defaultMillenniumSidebarTheme(): MillenniumSidebarTheme {
    val root = MillenniumSidebarRootProperties(
        width = 264.dp,
        collapsedWidth = 76.dp,
        shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
        borderWidth = 1.dp,
        shadowElevation = 8.dp,
        stateAnimationMillis = 180,
    )
    val header = MillenniumSidebarHeaderProperties(
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 16.dp, bottom = 12.dp),
        contentSpacing = 12.dp,
        minimumHeight = 0.dp,
        dividerWidth = 1.dp,
    )
    val content = MillenniumSidebarContentProperties(
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        contentSpacing = 14.dp,
        showScrollbar = true,
        scrollbarEndPadding = 6.dp,
    )
    val footer = MillenniumSidebarFooterProperties(
        contentPadding = PaddingValues(14.dp),
        contentSpacing = 10.dp,
        minimumHeight = 0.dp,
        dividerWidth = 1.dp,
    )
    val group = MillenniumSidebarGroupProperties(
        contentPadding = PaddingValues(horizontal = 2.dp, vertical = 2.dp),
        contentSpacing = 2.dp,
        groupSpacing = 4.dp,
        labelStyle = TextStyle(fontSize = 11.sp, lineHeight = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp),
        labelPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
        labelMinimumHeight = 30.dp,
        labelShape = RoundedCornerShape(8.dp),
        labelBorderWidth = 1.dp,
        animationMillis = 180,
    )
    val item = MillenniumSidebarItemProperties(
        labelStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
        minimumHeight = 40.dp,
        iconSize = 18.dp,
        iconLabelSpacing = 10.dp,
        borderWidth = 1.dp,
        shadowElevation = 0.dp,
        selectedIndicatorWidth = 3.dp,
        selectedIndicatorHeight = 18.dp,
        stateAnimationMillis = 140,
    )
    val badge = MillenniumSidebarBadgeProperties(
        textStyle = TextStyle(fontSize = 11.sp, lineHeight = 14.sp, fontWeight = FontWeight.SemiBold),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        minimumHeight = 18.dp,
    )
    val trigger = MillenniumSidebarTriggerProperties(
        shape = RoundedCornerShape(8.dp),
        size = 30.dp,
        animationMillis = 160,
    )
    return MillenniumSidebarTheme(
        root = MillenniumControlStateProperty.all(root),
        header = MillenniumControlStateProperty.all(header),
        content = MillenniumControlStateProperty.all(content),
        footer = MillenniumControlStateProperty.all(footer),
        group = MillenniumControlStateProperty(
            default = group,
            hovered = group,
            focused = group.copy(labelBorderWidth = 2.dp),
            pressed = group.copy(labelBorderWidth = 1.dp),
            disabled = group.copy(alpha = 0.45f),
            selected = group,
            error = group.copy(labelBorderWidth = 2.dp),
        ),
        item = MillenniumControlStateProperty(
            default = item,
            hovered = item.copy(shadowElevation = 1.dp),
            focused = item.copy(borderWidth = 2.dp),
            pressed = item.copy(shadowElevation = 0.dp),
            disabled = item.copy(alpha = 0.45f),
            selected = item.copy(shadowElevation = 1.dp),
            error = item.copy(borderWidth = 2.dp),
        ),
        badge = MillenniumControlStateProperty.all(badge),
        trigger = MillenniumControlStateProperty.all(trigger),
    )
}

// ---------------------------------------------------------------------------
// State & scopes
// ---------------------------------------------------------------------------

/**
 * Hoistable sidebar state: whether the rail is collapsed and which groups are
 * open. Callers may also drive every function with plain `Boolean` state.
 */
@Stable
class MillenniumSidebarState(
    collapsed: Boolean = false,
    private val defaultGroupExpanded: Boolean = true,
) {
    var collapsed: Boolean by mutableStateOf(collapsed)

    private val expandedGroups = mutableStateMapOf<String, Boolean>()

    fun isGroupExpanded(id: String): Boolean = expandedGroups[id] ?: defaultGroupExpanded

    fun setGroupExpanded(id: String, expanded: Boolean) {
        expandedGroups[id] = expanded
    }

    fun toggleGroup(id: String) {
        setGroupExpanded(id, !isGroupExpanded(id))
    }

    fun expandAll(ids: Collection<String>) {
        ids.forEach { expandedGroups[it] = true }
    }

    fun collapseAll(ids: Collection<String>) {
        ids.forEach { expandedGroups[it] = false }
    }
}

@Composable
fun rememberMillenniumSidebarState(
    collapsed: Boolean = false,
    defaultGroupExpanded: Boolean = true,
): MillenniumSidebarState = remember { MillenniumSidebarState(collapsed, defaultGroupExpanded) }

/** Read by sidebar children to align themselves with the rail width. */
@Stable
class MillenniumSidebarScope internal constructor(
    val collapsed: Boolean,
    val toggleCollapsed: () -> Unit,
)

/** Read by group children to share one open/closed decision. */
@Stable
class MillenniumSidebarGroupScope internal constructor(
    val expanded: Boolean,
    val collapsible: Boolean,
    val enabled: Boolean,
    val toggle: () -> Unit,
)

val LocalMillenniumSidebar = staticCompositionLocalOf<MillenniumSidebarScope?> { null }
internal val LocalMillenniumSidebarGroup = staticCompositionLocalOf<MillenniumSidebarGroupScope?> { null }

// ---------------------------------------------------------------------------
// Root
// ---------------------------------------------------------------------------

/**
 * A themed, caller-owned sidebar shell. Compose it from lego bricks:
 *
 * ```
 * MillenniumSidebar {
 *     MillenniumSidebarHeader { ... }        // fixed
 *     MillenniumSidebarContent { ... }       // scrolls
 *     MillenniumSidebarFooter { ... }        // fixed
 * }
 * ```
 */
@Composable
fun MillenniumSidebar(
    state: MillenniumSidebarState,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    MillenniumSidebar(
        modifier = modifier,
        collapsed = state.collapsed,
        onCollapsedChange = { state.collapsed = it },
        content = content,
    )
}

@Composable
fun MillenniumSidebar(
    modifier: Modifier = Modifier,
    collapsed: Boolean = false,
    onCollapsedChange: (Boolean) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.sidebarTheme.root.resolve(MillenniumControlState.Default)
    val targetWidth = if (collapsed) properties.collapsedWidth else properties.width
    val animatedWidth by animateDpAsState(
        targetValue = targetWidth,
        animationSpec = tween(properties.stateAnimationMillis),
    )
    val scope = remember(collapsed, onCollapsedChange) {
        MillenniumSidebarScope(collapsed = collapsed) { onCollapsedChange(!collapsed) }
    }
    CompositionLocalProvider(LocalMillenniumSidebar provides scope) {
        Column(
            modifier = modifier
                .width(animatedWidth)
                .fillMaxHeight()
                .millenniumSurface(
                    background = properties.background ?: millenniumBevelBrush(colors),
                    shape = properties.shape,
                    borderColor = properties.borderColor ?: colors.border,
                    borderWidth = properties.borderWidth,
                    shadowElevation = properties.shadowElevation,
                    innerGlowColor = properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.55f),
                )
                .alpha(properties.alpha),
        ) {
            content()
        }
    }
}

// ---------------------------------------------------------------------------
// Header / Content / Footer
// ---------------------------------------------------------------------------

@Composable
fun MillenniumSidebarHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.sidebarTheme.header.resolve(MillenniumControlState.Default)
    val background = properties.background
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (background != null) Modifier.background(background) else Modifier),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .heightIn(min = properties.minimumHeight)
                .padding(properties.contentPadding),
            verticalArrangement = Arrangement.spacedBy(properties.contentSpacing),
        ) {
            content()
        }
        SidebarHairline(properties.dividerColor ?: colors.border, properties.dividerWidth)
    }
}

/** The only region that scrolls. Must be placed directly inside `MillenniumSidebar`. */
@Composable
fun ColumnScope.MillenniumSidebarContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val properties = MillenniumTheme.sidebarTheme.content.resolve(MillenniumControlState.Default)
    val scrollState = rememberScrollState()
    Box(modifier = modifier.weight(1f).fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(properties.contentPadding)
                .padding(end = if (properties.showScrollbar) properties.scrollbarEndPadding else 0.dp),
            verticalArrangement = Arrangement.spacedBy(properties.contentSpacing),
        ) {
            content()
        }
        if (properties.showScrollbar) {
            MillenniumScrollbar(
                scrollState,
                Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(vertical = 6.dp),
            )
        }
    }
}

@Composable
fun MillenniumSidebarFooter(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.sidebarTheme.footer.resolve(MillenniumControlState.Default)
    val background = properties.background
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(if (background != null) Modifier.background(background) else Modifier),
    ) {
        SidebarHairline(properties.dividerColor ?: colors.border, properties.dividerWidth)
        Column(
            Modifier
                .fillMaxWidth()
                .heightIn(min = properties.minimumHeight)
                .padding(properties.contentPadding),
            verticalArrangement = Arrangement.spacedBy(properties.contentSpacing),
        ) {
            content()
        }
    }
}

@Composable
private fun SidebarHairline(color: Color, width: Dp) {
    if (width > 0.dp) {
        Box(Modifier.fillMaxWidth().height(width).background(color))
    }
}

// ---------------------------------------------------------------------------
// Groups
// ---------------------------------------------------------------------------

/**
 * A cluster of items. When [collapsible] is true, a
 * [MillenniumSidebarGroupLabel] inside it becomes a toggle and a nested
 * [MillenniumSidebarGroupContent] animates open and shut.
 */
@Composable
fun MillenniumSidebarGroup(
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    collapsible: Boolean = false,
    enabled: Boolean = true,
    onExpandedChange: (Boolean) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    SidebarGroupContainer(modifier, expanded, collapsible, enabled, onExpandedChange, content)
}

/** Convenience overload that reads and writes group expansion from [state]. */
@Composable
fun MillenniumSidebarGroup(
    id: String,
    state: MillenniumSidebarState,
    modifier: Modifier = Modifier,
    collapsible: Boolean = true,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    SidebarGroupContainer(
        modifier = modifier,
        expanded = state.isGroupExpanded(id),
        collapsible = collapsible,
        enabled = enabled,
        onExpandedChange = { state.setGroupExpanded(id, it) },
        content = content,
    )
}

@Composable
private fun SidebarGroupContainer(
    modifier: Modifier,
    expanded: Boolean,
    collapsible: Boolean,
    enabled: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val properties = MillenniumTheme.sidebarTheme.group.resolve(MillenniumControlState.Default)
    val scope = remember(expanded, collapsible, enabled, onExpandedChange) {
        MillenniumSidebarGroupScope(
            expanded = expanded,
            collapsible = collapsible,
            enabled = enabled,
            toggle = { onExpandedChange(!expanded) },
        )
    }
    CompositionLocalProvider(LocalMillenniumSidebarGroup provides scope) {
        Column(
            modifier = modifier.fillMaxWidth().alpha(properties.alpha),
            verticalArrangement = Arrangement.spacedBy(properties.groupSpacing),
        ) {
            content()
        }
    }
}

@Composable
fun MillenniumSidebarGroupLabel(
    label: String,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    MillenniumSidebarGroupLabel(modifier = modifier, leading = leading, trailing = trailing) {
        Text(label)
    }
}

@Composable
fun MillenniumSidebarGroupLabel(
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val group = LocalMillenniumSidebarGroup.current
    val railCollapsed = LocalMillenniumSidebar.current?.collapsed == true
    val properties = MillenniumTheme.sidebarTheme.group.resolve(MillenniumControlState.Default)
    val collapsible = group?.collapsible == true
    val expanded = group?.expanded ?: true
    val enabled = group?.enabled ?: true

    // In a collapsed rail only toggleable groups keep an affordance.
    if (railCollapsed && !collapsible) return

    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val state = when {
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        else -> MillenniumControlState.Default
    }
    val rowForeground = properties.labelForeground ?: when (state) {
        MillenniumControlState.Hovered, MillenniumControlState.Focused, MillenniumControlState.Pressed -> colors.text
        else -> colors.muted
    }
    val background: Brush = when (state) {
        MillenniumControlState.Hovered, MillenniumControlState.Focused ->
            properties.labelHoveredBackground ?: Brush.verticalGradient(
                listOf(colors.accent.copy(alpha = 0.12f), colors.accent.copy(alpha = 0.05f)),
            )
        MillenniumControlState.Pressed -> Brush.verticalGradient(
            listOf(colors.accent.copy(alpha = 0.18f), colors.accent.copy(alpha = 0.10f)),
        )
        else -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
    }
    val borderColor = properties.labelBorderColor ?: when (state) {
        MillenniumControlState.Focused -> colors.accent
        MillenniumControlState.Error -> colors.danger
        else -> null
    }
    val borderWidth = when (state) {
        MillenniumControlState.Focused -> maxOf(properties.labelBorderWidth, 2.dp)
        else -> properties.labelBorderWidth
    }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 0f else -90f,
        animationSpec = tween(properties.animationMillis),
    )

    val rowContent: @Composable RowScope.() -> Unit = {
        val rowScope = this
        if (!railCollapsed) {
            if (leading != null) {
                leading()
                Spacer(Modifier.width(6.dp))
            }
            Box(Modifier.weight(1f)) {
                MillenniumStyledContent(properties.labelStyle, rowForeground) { rowScope.content() }
            }
            if (trailing != null) trailing()
            if (collapsible) Spacer(Modifier.width(6.dp))
        }
        if (collapsible) {
            MillenniumIcon(
                MillenniumIcons.Chevron,
                modifier = Modifier.rotate(rotation),
                tint = rowForeground,
                state = state,
            )
        }
    }

    val surface = Modifier
        .fillMaxWidth()
        .heightIn(min = properties.labelMinimumHeight)
        .millenniumSurface(
            background = background,
            shape = properties.labelShape,
            borderColor = borderColor,
            borderWidth = borderWidth,
        )
        .padding(properties.labelPadding)

    if (collapsible) {
        UnstyledButton(
            onClick = { group.toggle() },
            enabled = enabled,
            interactionSource = interaction,
            indication = LocalIndication.current,
            contentPadding = PaddingValues(0.dp),
            modifier = modifier.then(surface),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (railCollapsed) Arrangement.Center else Arrangement.Start,
            ) {
                rowContent()
            }
        }
    } else {
        Box(modifier.then(surface)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (railCollapsed) Arrangement.Center else Arrangement.Start,
            ) {
                rowContent()
            }
        }
    }
}

/** The animated, collapsible body of a group. */
@Composable
fun MillenniumSidebarGroupContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val group = LocalMillenniumSidebarGroup.current
    val properties = MillenniumTheme.sidebarTheme.group.resolve(MillenniumControlState.Default)
    val expanded = group?.expanded ?: true
    val animate = group?.collapsible == true

    val body: @Composable () -> Unit = {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(properties.contentPadding),
            verticalArrangement = Arrangement.spacedBy(properties.contentSpacing),
        ) {
            content()
        }
    }

    if (animate) {
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(animationSpec = tween(properties.animationMillis)) +
                fadeIn(animationSpec = tween(properties.animationMillis)),
            exit = shrinkVertically(animationSpec = tween(properties.animationMillis)) +
                fadeOut(animationSpec = tween(properties.animationMillis)),
        ) {
            body()
        }
    } else {
        body()
    }
}

/** Vertical stack of items with the group's spacing. */
@Composable
fun MillenniumSidebarMenu(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val properties = MillenniumTheme.sidebarTheme.group.resolve(MillenniumControlState.Default)
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(properties.contentSpacing),
    ) {
        content()
    }
}

// ---------------------------------------------------------------------------
// Items
// ---------------------------------------------------------------------------

/** Visual facts an item exposes to a custom content slot. */
@Immutable
data class MillenniumSidebarItemVisuals(
    val selected: Boolean,
    val collapsed: Boolean,
    val state: MillenniumControlState,
    val foreground: Color,
    val iconTint: Color,
)

/**
 * A single selectable row with an optional leading icon and trailing badge.
 */
@Composable
fun MillenniumSidebarItem(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    badge: String? = null,
    contentDescription: String? = null,
) {
    val itemProperties = MillenniumTheme.sidebarTheme.item.default
    SidebarItemContainer(
        onClick = onClick,
        enabled = enabled,
        selected = selected,
        modifier = modifier.sidebarItemSemantics(contentDescription ?: label, selected),
    ) { visuals ->
        if (icon != null) {
            MillenniumIcon(
                icon,
                contentDescription = null,
                tint = visuals.iconTint,
                modifier = Modifier.size(itemProperties.iconSize),
            )
            if (!visuals.collapsed) Spacer(Modifier.width(itemProperties.iconLabelSpacing))
        }
        if (!visuals.collapsed) {
            Box(Modifier.weight(1f)) {
                MillenniumStyledContent(itemProperties.labelStyle, visuals.foreground) { Text(label) }
            }
            if (badge != null) {
                Spacer(Modifier.width(8.dp))
                MillenniumSidebarBadge(badge, highlighted = visuals.selected)
            }
        }
    }
}

/** A row with fully custom leading, content and trailing slots. */
@Composable
fun MillenniumSidebarItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    contentDescription: String? = null,
    content: @Composable RowScope.() -> Unit,
) {
    SidebarItemContainer(
        onClick = onClick,
        enabled = enabled,
        selected = selected,
        modifier = modifier.sidebarItemSemantics(contentDescription, selected),
    ) { content() }
}

@Composable
private fun SidebarItemContainer(
    onClick: () -> Unit,
    enabled: Boolean,
    selected: Boolean,
    modifier: Modifier,
    content: @Composable RowScope.(MillenniumSidebarItemVisuals) -> Unit,
) {
    val colors = MillenniumTheme.colors
    val collapsed = LocalMillenniumSidebar.current?.collapsed == true
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val state = when {
        !enabled -> MillenniumControlState.Disabled
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        selected -> MillenniumControlState.Selected
        else -> MillenniumControlState.Default
    }
    val properties = MillenniumTheme.sidebarTheme.item.resolve(state)
    val foreground = properties.foreground ?: when (state) {
        MillenniumControlState.Disabled -> colors.muted
        MillenniumControlState.Selected -> colors.accent
        else -> colors.text
    }
    val iconTint = properties.iconTint ?: when (state) {
        MillenniumControlState.Disabled -> colors.muted
        MillenniumControlState.Selected, MillenniumControlState.Hovered,
        MillenniumControlState.Focused, MillenniumControlState.Pressed,
        -> colors.accent
        else -> colors.muted
    }
    val background = properties.background ?: when (state) {
        MillenniumControlState.Selected -> Brush.verticalGradient(
            listOf(colors.accent.copy(alpha = 0.24f), colors.accent.copy(alpha = 0.12f)),
        )
        MillenniumControlState.Hovered -> Brush.verticalGradient(
            listOf(colors.accent.copy(alpha = 0.12f), colors.accent.copy(alpha = 0.05f)),
        )
        MillenniumControlState.Focused -> Brush.verticalGradient(
            listOf(colors.accent.copy(alpha = 0.14f), colors.accent.copy(alpha = 0.07f)),
        )
        MillenniumControlState.Pressed -> Brush.verticalGradient(
            listOf(colors.accent.copy(alpha = 0.18f), colors.accent.copy(alpha = 0.10f)),
        )
        MillenniumControlState.Error -> Brush.verticalGradient(
            listOf(colors.danger.copy(alpha = 0.16f), colors.danger.copy(alpha = 0.09f)),
        )
        MillenniumControlState.Disabled, MillenniumControlState.Default ->
            Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
    }
    val borderColor = properties.borderColor ?: when (state) {
        MillenniumControlState.Focused -> colors.accent
        MillenniumControlState.Error -> colors.danger
        else -> null
    }
    val indicatorColor = properties.selectedIndicatorColor ?: colors.accent
    val visuals = remember(selected, collapsed, state, foreground, iconTint) {
        MillenniumSidebarItemVisuals(selected, collapsed, state, foreground, iconTint)
    }
    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        indication = LocalIndication.current,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .fillMaxWidth()
            .alpha(properties.alpha)
            .millenniumSurface(
                background = background,
                shape = properties.shape,
                borderColor = borderColor,
                borderWidth = properties.borderWidth,
                shadowElevation = properties.shadowElevation,
                innerGlowColor = properties.innerGlowColor,
            ),
    ) {
        Box(Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = properties.minimumHeight)
                    .padding(properties.contentPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (collapsed) Arrangement.Center else Arrangement.Start,
            ) {
                content(visuals)
            }
            if (selected && !collapsed) {
                Box(
                    Modifier
                        .align(Alignment.CenterStart)
                        .width(properties.selectedIndicatorWidth)
                        .height(properties.selectedIndicatorHeight)
                        .clip(RoundedCornerShape(50))
                        .background(indicatorColor),
                )
            }
        }
    }
}

@Composable
fun MillenniumSidebarBadge(
    text: String,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
) {
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.sidebarTheme.badge.resolve(MillenniumControlState.Default)
    val background = properties.background ?: if (highlighted) {
        gelBrush(colors.accentTop, colors.accentBottom)
    } else {
        Brush.verticalGradient(listOf(colors.inset, colors.inset))
    }
    val foreground = properties.foreground ?: if (highlighted) colors.onAccent else colors.muted
    Box(
        modifier = modifier
            .heightIn(min = properties.minimumHeight)
            .millenniumSurface(
                background = background,
                shape = properties.shape,
                borderColor = if (highlighted) null else colors.border,
                borderWidth = if (highlighted) 0.dp else 1.dp,
            )
            .padding(properties.contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        MillenniumStyledContent(properties.textStyle, foreground) { Text(text) }
    }
}

/** A hairline tuned to the sidebar's rhythm, for use between bricks. */
@Composable
fun MillenniumSidebarSeparator(modifier: Modifier = Modifier) {
    MillenniumSeparator(modifier.padding(vertical = 4.dp))
}

// ---------------------------------------------------------------------------
// Trigger
// ---------------------------------------------------------------------------

/** Collapses or expands the enclosing [MillenniumSidebar]. */
@Composable
fun MillenniumSidebarTrigger(modifier: Modifier = Modifier) {
    val colors = MillenniumTheme.colors
    val scope = LocalMillenniumSidebar.current
    val properties = MillenniumTheme.sidebarTheme.trigger.resolve(MillenniumControlState.Default)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val state = when {
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        else -> MillenniumControlState.Default
    }
    val rotation by animateFloatAsState(
        targetValue = if (scope?.collapsed == true) 270f else 90f,
        animationSpec = tween(properties.animationMillis),
    )
    val background = when (state) {
        MillenniumControlState.Hovered, MillenniumControlState.Focused ->
            Brush.verticalGradient(listOf(colors.accent.copy(alpha = 0.14f), colors.accent.copy(alpha = 0.06f)))
        MillenniumControlState.Pressed ->
            Brush.verticalGradient(listOf(colors.accent.copy(alpha = 0.20f), colors.accent.copy(alpha = 0.12f)))
        else -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
    }
    UnstyledButton(
        onClick = { scope?.toggleCollapsed?.invoke() },
        enabled = scope != null,
        interactionSource = interaction,
        indication = LocalIndication.current,
        contentPadding = PaddingValues(0.dp),
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(properties.size)
            .millenniumSurface(
                background = background,
                shape = properties.shape,
                borderColor = when (state) {
                    MillenniumControlState.Focused -> colors.accent
                    else -> null
                },
                borderWidth = 1.dp,
            ),
    ) {
        MillenniumIcon(
            MillenniumIcons.Chevron,
            contentDescription = "Toggle sidebar",
            modifier = Modifier.rotate(rotation),
            tint = properties.foreground ?: colors.text,
        )
    }
}

private fun Modifier.sidebarItemSemantics(description: String?, isSelected: Boolean): Modifier =
    semantics {
        if (description != null) contentDescription = description
        selected = isSelected
    }

@Preview
@Composable
private fun MillenniumSidebarPreview() = MillenniumPreview {
    val state = rememberMillenniumSidebarState()
    Row(Modifier.height(420.dp)) {
        MillenniumSidebar(state) {
            MillenniumSidebarHeader {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MillenniumText("Millennium", weight = FontWeight.Bold)
                    MillenniumSidebarTrigger()
                }
            }
            MillenniumSidebarContent {
                MillenniumSidebarGroup(id = "library", state = state) {
                    MillenniumSidebarGroupLabel("Library")
                    MillenniumSidebarGroupContent {
                        MillenniumSidebarMenu {
                            MillenniumSidebarItem(
                                label = "Discover",
                                icon = MillenniumIcons.Sparkle,
                                onClick = {},
                                selected = true,
                                badge = "20",
                            )
                            MillenniumSidebarItem(label = "Saved", icon = MillenniumIcons.Music, onClick = {})
                        }
                    }
                }
                MillenniumSidebarGroup(id = "workspace", state = state) {
                    MillenniumSidebarGroupLabel("Workspace")
                    MillenniumSidebarGroupContent {
                        MillenniumSidebarMenu {
                            MillenniumSidebarItem(label = "Playlists", icon = MillenniumIcons.Music, onClick = {})
                            MillenniumSidebarItem(label = "History", icon = MillenniumIcons.Check, onClick = {})
                        }
                    }
                }
            }
            MillenniumSidebarFooter {
                MillenniumText("Edition 001 / 2026", color = MillenniumTheme.colors.muted, size = 10.sp)
            }
        }
    }
}
