package org.spotube.millennium.composables

import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.Text
import com.composeunstyled.Track
import com.composeunstyled.Thumb
import com.composeunstyled.UnstyledSwitch
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumToggleSwitchThemeProperties(
    val trackBackground: Brush? = null,
    val selectedTrackBackground: Brush? = null,
    val trackShape: Shape,
    val trackSize: DpSize,
    val trackBorderColor: Color? = null,
    val trackBorderWidth: Dp,
    val trackGlowColor: Color? = null,
    val thumbBackground: Brush? = null,
    val thumbShape: Shape,
    val thumbSize: Dp,
    val thumbInset: Dp,
    val thumbElevation: Dp,
    val minimumTouchHeight: Dp,
    val labelForeground: Color? = null,
    val labelStyle: TextStyle,
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val thumbAnimationMillis: Int,
    val alpha: Float = 1f,
    val thumbBorderColor: Color? = null,
    val thumbBorderWidth: Dp = 1.dp,
    val thumbGlowColor: Color? = null,
)

@Immutable
data class MillenniumToggleSwitchTheme(
    val states: MillenniumControlStateProperty<MillenniumToggleSwitchThemeProperties>,
)

fun defaultMillenniumToggleSwitchTheme(): MillenniumToggleSwitchTheme {
    val base = MillenniumToggleSwitchThemeProperties(
        trackShape = RoundedCornerShape(50),
        trackSize = DpSize(52.dp, 30.dp),
        trackBorderWidth = 1.dp,
        thumbShape = RoundedCornerShape(50),
        thumbSize = 24.dp,
        thumbInset = 3.dp,
        thumbElevation = 3.dp,
        minimumTouchHeight = 44.dp,
        labelStyle = TextStyle(fontWeight = FontWeight.Normal),
        contentPadding = PaddingValues(0.dp),
        contentSpacing = 12.dp,
        thumbAnimationMillis = 160,
    )
    return MillenniumToggleSwitchTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(trackBorderWidth = 2.dp),
            pressed = base.copy(thumbElevation = 1.dp),
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(trackBorderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsKeyboardFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val state = when {
        !enabled -> MillenniumControlState.Disabled
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        checked -> MillenniumControlState.Selected
        else -> MillenniumControlState.Default
    }
    val properties = MillenniumTheme.toggleSwitchTheme.states.resolve(state)
    val colors = MillenniumTheme.colors
    val trackBackground = properties.trackBackground ?: millenniumBevelBrush(colors, sunken = true)
    val selectedTrackBackground = properties.selectedTrackBackground ?: gelBrush(colors.accentTop, colors.accentBottom)
    val trackBorderColor = properties.trackBorderColor ?: when (state) {
        MillenniumControlState.Focused, MillenniumControlState.Selected -> colors.accent
        MillenniumControlState.Hovered -> colors.border
        MillenniumControlState.Error -> colors.danger
        else -> colors.border
    }
    val trackGlowColor = properties.trackGlowColor ?: if (checked) colors.highlight.copy(alpha = 0.35f) else null
    val thumbBackground = properties.thumbBackground ?: gelBrush(colors.raised, colors.inset)
    val labelForeground = properties.labelForeground ?: if (state == MillenniumControlState.Disabled) colors.muted else colors.text
    UnstyledSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.heightIn(min = properties.minimumTouchHeight).alpha(properties.alpha),
        enabled = enabled,
        accessibilityLabel = label,
        // State feedback is drawn on the track and thumb; don't wash the whole
        // labelled row with the app-wide hover indication.
        indication = null,
        interactionSource = interaction,
    ) {
        Row(
            modifier = androidx.compose.ui.Modifier.padding(properties.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(properties.contentSpacing),
        ) {
            Track(
                Modifier
                    .size(properties.trackSize)
                    .millenniumSurface(
                        background = if (checked) selectedTrackBackground else trackBackground,
                        shape = properties.trackShape,
                        borderColor = trackBorderColor,
                        borderWidth = properties.trackBorderWidth,
                        innerGlowColor = trackGlowColor,
                    ),
            ) {
                Thumb(
                    animationSpec = tween(properties.thumbAnimationMillis),
                    modifier = Modifier
                        .padding(properties.thumbInset)
                        .size(properties.thumbSize)
                        .millenniumSurface(
                            background = thumbBackground,
                            shape = properties.thumbShape,
                            shadowElevation = properties.thumbElevation,
                            borderColor = properties.thumbBorderColor ?: colors.border,
                            borderWidth = properties.thumbBorderWidth,
                            innerGlowColor = properties.thumbGlowColor ?: colors.highlight.copy(alpha = 0.36f),
                        ),
                )
            }
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.merge(properties.labelStyle)
                    .copy(color = labelForeground),
            ) { Text(label) }
        }
    }
}

@Preview
@Composable
private fun MillenniumSwitchPreview() = MillenniumPreview {
    MillenniumSwitch(checked = true, onCheckedChange = {}, label = "Airplane mode")
}
