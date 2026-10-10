package org.spotube.millennium.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.ui.semantics.Role
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
import org.spotube.millennium.theme.*

@Immutable
data class MillenniumBottomNavigationBarThemeProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val shape: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    val contentPadding: PaddingValues = PaddingValues(8.dp),
    val itemSpacing: Dp = 6.dp,
    val minimumHeight: Dp = 76.dp,
    val borderColor: Color? = null,
    val borderWidth: Dp = 1.dp,
    val shadowElevation: Dp = 8.dp,
    val innerGlowColor: Color? = null,
)

@Immutable
data class MillenniumBottomNavigationItemThemeProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val shape: Shape = RoundedCornerShape(10.dp),
    val contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    val minimumHeight: Dp = 60.dp,
    val minimumWidth: Dp = 48.dp,
    val iconSize: Dp = 22.dp,
    val iconLabelSpacing: Dp = 4.dp,
    val labelStyle: TextStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
    val borderColor: Color? = null,
    val borderWidth: Dp = 1.dp,
    val shadowElevation: Dp = 0.dp,
    val innerGlowColor: Color? = null,
    val alpha: Float = 1f,
    val animationMillis: Int = 150,
)

@Immutable
data class MillenniumBottomNavigationBarTheme(
    val states: MillenniumControlStateProperty<MillenniumBottomNavigationBarThemeProperties>,
    val item: MillenniumControlStateProperty<MillenniumBottomNavigationItemThemeProperties>,
)

fun defaultMillenniumBottomNavigationBarTheme(): MillenniumBottomNavigationBarTheme {
    val item = MillenniumBottomNavigationItemThemeProperties()
    return MillenniumBottomNavigationBarTheme(
        states = MillenniumControlStateProperty.all(MillenniumBottomNavigationBarThemeProperties()),
        item = MillenniumControlStateProperty(
            default = item,
            hovered = item.copy(shadowElevation = 1.dp),
            focused = item.copy(borderWidth = 2.dp),
            pressed = item.copy(shadowElevation = 0.dp),
            disabled = item.copy(alpha = 0.45f),
            selected = item.copy(shadowElevation = 3.dp, labelStyle = item.labelStyle.copy(fontWeight = FontWeight.SemiBold)),
        ),
    )
}

/**
 * Pearl/smoked-glass navigation chrome. Place outside the scrolling body in a
 * Column to keep it fixed at the bottom. Selection and routing belong to callers.
 * Typically holds 3–5 items; this is not a horizontally scrolling menu.
 * Pass WindowInsets(0, 0, 0, 0) when a parent already handles system insets.
 */
@Composable
fun MillenniumBottomNavigationBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = WindowInsets.navigationBars,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.bottomNavigationBarTheme.states.default
    Row(
        modifier = modifier.fillMaxWidth()
            .millenniumSurface(
                background = properties.background ?: gelBrush(colors.raised, colors.surface),
                shape = properties.shape,
                borderColor = properties.borderColor ?: colors.border,
                borderWidth = properties.borderWidth,
                shadowElevation = properties.shadowElevation,
                innerGlowColor = properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.55f),
            )
            .windowInsetsPadding(windowInsets)
            .heightIn(min = properties.minimumHeight)
            .padding(properties.contentPadding)
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(properties.itemSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MillenniumStyledContent(TextStyle.Default, properties.foreground ?: colors.text) { content() }
    }
}

/** Palette-aware facts for custom icon, label and badge content. */
@Immutable
data class MillenniumBottomNavigationItemVisuals(
    val selected: Boolean,
    val enabled: Boolean,
    val state: MillenniumControlState,
    val foreground: Color,
)

/** Equal-width destination with independently composable icon, label and badge. */
@Composable
fun RowScope.MillenniumBottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    icon: @Composable () -> Unit,
    label: (@Composable () -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
) {
    MillenniumBottomNavigationItem(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentDescription = contentDescription,
    ) { visuals ->
        val properties = MillenniumTheme.bottomNavigationBarTheme.item.resolve(visuals.state)
        Box(contentAlignment = Alignment.Center) {
            Box(Modifier.size(properties.iconSize), contentAlignment = Alignment.Center) { icon() }
            if (badge != null) {
                Box(Modifier.align(Alignment.TopEnd)) { badge() }
            }
        }
        if (label != null) label()
    }
}

/** Fully custom item body. Content inherits the state-specific foreground/style. */
@Composable
fun RowScope.MillenniumBottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    content: @Composable ColumnScope.(MillenniumBottomNavigationItemVisuals) -> Unit,
) {
    val colors = MillenniumTheme.colors
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
    val properties = MillenniumTheme.bottomNavigationBarTheme.item.resolve(state)
    val foreground = properties.foreground ?: bottomNavigationItemForeground(colors, selected, enabled)
    val background = properties.background ?: when {
        selected -> gelBrush(colors.accentTop, colors.accentBottom)
        pressed -> millenniumBevelBrush(colors, sunken = true)
        hovered || focused -> millenniumBevelBrush(colors)
        else -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
    }
    val elevation by animateDpAsState(properties.shadowElevation, tween(properties.animationMillis))
    val alpha by animateFloatAsState(properties.alpha, tween(properties.animationMillis))
    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        role = Role.Tab,
        interactionSource = interaction,
        indication = LocalIndication.current,
        contentPadding = properties.contentPadding,
        modifier = modifier.weight(1f)
            .heightIn(min = properties.minimumHeight).widthIn(min = properties.minimumWidth)
            .semantics {
                this.selected = selected
                if (contentDescription != null) this.contentDescription = contentDescription
            }
            .alpha(alpha)
            .millenniumSurface(
                background = background,
                shape = properties.shape,
                borderColor = properties.borderColor ?: when {
                    focused -> colors.accent
                    selected -> colors.accentBottom
                    hovered || pressed -> colors.border
                    else -> Color.Transparent
                },
                borderWidth = properties.borderWidth,
                shadowElevation = elevation,
                innerGlowColor = properties.innerGlowColor ?: if (selected || hovered || focused) {
                    colors.highlight.copy(alpha = 0.35f)
                } else null,
            ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(properties.iconLabelSpacing),
        ) {
            MillenniumStyledContent(properties.labelStyle, foreground) {
                content(MillenniumBottomNavigationItemVisuals(selected, enabled, state, foreground))
            }
        }
    }
}

internal fun bottomNavigationItemForeground(colors: MillenniumColors, selected: Boolean, enabled: Boolean): Color = when {
    selected -> colors.onAccent
    !enabled -> colors.muted
    else -> colors.text
}

@Preview
@Composable
private fun MillenniumBottomNavigationBarPreview() = MillenniumPreview {
    MillenniumBottomNavigationBar(windowInsets = WindowInsets(0, 0, 0, 0)) {
        listOf("Discover", "Library", "Saved").forEachIndexed { index, label ->
            MillenniumBottomNavigationItem(
                selected = index == 0,
                onClick = {},
                contentDescription = label,
                icon = {
                    com.composeunstyled.UnstyledIcon(
                        if (index == 0) MillenniumIcons.Sparkle else MillenniumIcons.Music,
                        contentDescription = null,
                        tint = com.composeunstyled.LocalTextStyle.current.color,
                    )
                },
                label = { Text(label) },
            )
        }
    }
}
