package org.spotube.millennium.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.composeunstyled.Text
import com.composeunstyled.UnstyledButton
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.millenniumSelectedGelBrush
import org.spotube.millennium.theme.millenniumSurface

/** Complete, state-specific list-tile appearance and layout configuration. */
@Immutable
data class MillenniumListTileThemeProperties(
    val titleStyle: TextStyle,
    val subtitleStyle: TextStyle,
    val titleForeground: Color? = null,
    val subtitleForeground: Color? = null,
    val selectedTitleForeground: Color? = null,
    val selectedSubtitleForeground: Color? = null,
    val background: Brush? = null,
    val selectedBackground: Brush? = null,
    /** How strongly the selected fill blends the gel toward the row surface. */
    val selectedGelStrength: Float = 0.35f,
    val shape: Shape,
    val contentPadding: PaddingValues,
    val minimumHeight: Dp,
    val leadingSpacing: Dp,
    val trailingSpacing: Dp,
    val titleSpacing: Dp,
    val borderWidth: Dp,
    val borderColor: Color? = null,
    val innerGlowColor: Color? = null,
    val alpha: Float = 1f,
    val stateAnimationMillis: Int = 140,
)

@Immutable
data class MillenniumListTileTheme(
    val states: MillenniumControlStateProperty<MillenniumListTileThemeProperties>,
)

fun defaultMillenniumListTileTheme(): MillenniumListTileTheme {
    val base = MillenniumListTileThemeProperties(
        titleStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
        subtitleStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        minimumHeight = 56.dp,
        leadingSpacing = 12.dp,
        trailingSpacing = 12.dp,
        titleSpacing = 2.dp,
        borderWidth = 0.dp,
    )
    return MillenniumListTileTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(borderWidth = 2.dp),
            pressed = base,
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(borderWidth = 2.dp),
        ),
    )
}

/** Resolves the visual state of a list tile from its interaction inputs. */
internal fun listTileControlState(
    enabled: Boolean,
    clickable: Boolean,
    pressed: Boolean,
    focused: Boolean,
    hovered: Boolean,
    selected: Boolean,
): MillenniumControlState = when {
    !enabled -> MillenniumControlState.Disabled
    clickable && pressed -> MillenniumControlState.Pressed
    clickable && focused -> MillenniumControlState.Focused
    clickable && hovered -> MillenniumControlState.Hovered
    selected -> MillenniumControlState.Selected
    else -> MillenniumControlState.Default
}

/** Surface for a list tile; selected rows use a quieter, surface-blended gel. */
internal fun listTileBackground(
    colors: MillenniumColors,
    selected: Boolean,
    state: MillenniumControlState,
    selectedGelStrength: Float,
): Brush {
    if (selected) {
        val strength = when (state) {
            MillenniumControlState.Pressed -> selectedGelStrength + 0.15f
            MillenniumControlState.Hovered, MillenniumControlState.Focused -> selectedGelStrength + 0.08f
            MillenniumControlState.Disabled -> selectedGelStrength * 0.6f
            else -> selectedGelStrength
        }
        return millenniumSelectedGelBrush(colors, strength)
    }
    return when (state) {
        MillenniumControlState.Hovered -> Brush.verticalGradient(
            listOf(colors.accent.copy(alpha = 0.10f), colors.accent.copy(alpha = 0.05f)),
        )
        MillenniumControlState.Focused -> Brush.verticalGradient(
            listOf(colors.accent.copy(alpha = 0.12f), colors.accent.copy(alpha = 0.06f)),
        )
        MillenniumControlState.Pressed -> Brush.verticalGradient(
            listOf(colors.accent.copy(alpha = 0.16f), colors.accent.copy(alpha = 0.09f)),
        )
        MillenniumControlState.Error -> Brush.verticalGradient(
            listOf(colors.danger.copy(alpha = 0.14f), colors.danger.copy(alpha = 0.08f)),
        )
        else -> Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
    }
}

/**
 * A flexible list row with leading, title, optional subtitle and trailing slots.
 *
 * The row becomes an accessible, keyboard-focusable button when [onClick] is non-null,
 * and shows a [selected] state whose fill is a lighter, surface-blended gel rather than
 * a primary button's full-strength surface. [onRightClick] (desktop/pointer) receives the
 * position of a secondary click measured relative to the tile.
 */
@Composable
fun MillenniumListTile(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    onRightClick: ((Offset) -> Unit)? = null,
) {
    val colors = MillenniumTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsKeyboardFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val hasOnClick = onClick != null
    val clickable = hasOnClick && enabled
    val state = listTileControlState(
        enabled = enabled,
        clickable = clickable,
        pressed = pressed,
        focused = focused,
        hovered = hovered,
        selected = selected,
    )
    val properties = MillenniumTheme.listTileTheme.states.resolve(state)
    val background = properties.background
        ?: properties.selectedBackground.takeIf { selected }
        ?: listTileBackground(colors, selected, state, properties.selectedGelStrength)
    val titleForeground = if (selected) {
        properties.selectedTitleForeground ?: properties.titleForeground ?: colors.text
    } else {
        properties.titleForeground ?: if (enabled) colors.text else colors.muted
    }
    val subtitleForeground = if (selected) {
        properties.selectedSubtitleForeground ?: colors.text.copy(alpha = 0.82f)
    } else {
        properties.subtitleForeground ?: colors.muted
    }
    val borderColor = properties.borderColor ?: when {
        state == MillenniumControlState.Error -> colors.danger
        state == MillenniumControlState.Focused -> colors.accent
        selected -> colors.accent.copy(alpha = 0.45f)
        state == MillenniumControlState.Hovered || state == MillenniumControlState.Pressed -> colors.border
        else -> null
    }
    val animatedAlpha by animateFloatAsState(properties.alpha, tween(properties.stateAnimationMillis))
    val surfaceModifier = modifier
        .fillMaxWidth()
        .alpha(animatedAlpha)
        .millenniumSurface(
            background = background,
            shape = properties.shape,
            borderColor = borderColor,
            borderWidth = properties.borderWidth,
            innerGlowColor = properties.innerGlowColor
                ?: if (selected || state == MillenniumControlState.Hovered || state == MillenniumControlState.Focused) {
                    colors.highlight.copy(alpha = 0.25f)
                } else {
                    null
                },
        )
        .then(secondaryClickModifier(onRightClick))
    val row: @Composable () -> Unit = {
        ListTileRow(
            properties = properties,
            titleForeground = titleForeground,
            subtitleForeground = subtitleForeground,
            leading = leading,
            title = title,
            subtitle = subtitle,
            trailing = trailing,
        )
    }
    if (onClick != null) {
        UnstyledButton(
            onClick = onClick,
            enabled = enabled,
            interactionSource = interaction,
            indication = LocalIndication.current,
            contentPadding = PaddingValues(0.dp),
            modifier = surfaceModifier,
        ) { row() }
    } else {
        Box(surfaceModifier) { row() }
    }
}

@Composable
private fun secondaryClickModifier(onRightClick: ((Offset) -> Unit)?): Modifier {
    if (onRightClick == null) return Modifier
    val current by rememberUpdatedState(onRightClick)
    return Modifier.pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                if (event.type == PointerEventType.Press && event.buttons.isSecondaryPressed) {
                    current(event.changes.firstOrNull()?.position ?: Offset.Zero)
                    event.changes.forEach { it.consume() }
                }
            }
        }
    }
}

@Composable
private fun ListTileRow(
    properties: MillenniumListTileThemeProperties,
    titleForeground: Color,
    subtitleForeground: Color,
    leading: (@Composable () -> Unit)?,
    title: @Composable () -> Unit,
    subtitle: (@Composable () -> Unit)?,
    trailing: (@Composable () -> Unit)?,
) {
    MillenniumStyledContent(properties.titleStyle, titleForeground) {
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(min = properties.minimumHeight)
                .padding(properties.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leading != null) {
                leading()
                Spacer(Modifier.width(properties.leadingSpacing))
            }
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(properties.titleSpacing),
            ) {
                title()
                if (subtitle != null) {
                    MillenniumStyledContent(properties.subtitleStyle, subtitleForeground) { subtitle() }
                }
            }
            if (trailing != null) {
                Spacer(Modifier.width(properties.trailingSpacing))
                trailing()
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumListTilePreview() = MillenniumPreview {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        MillenniumListTile(
            title = { Text("Midnight City") },
            leading = { MillenniumAvatar("MC", "Midnight City") },
            subtitle = { Text("M83 · Hurry Up, We're Dreaming") },
            trailing = { MillenniumText("4:03") },
        )
        MillenniumListTile(
            selected = true,
            onClick = {},
            title = { Text("Selected row") },
            subtitle = { Text("A lighter pour of the same gel.") },
            trailing = { MillenniumIcon(MillenniumIcons.Chevron) },
        )
    }
}
