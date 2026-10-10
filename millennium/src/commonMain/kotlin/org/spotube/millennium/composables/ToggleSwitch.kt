package org.spotube.millennium.composables

import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumToggleSwitchThemeProperties(
    val colors: MillenniumColors,
    val trackBackground: Brush,
    val selectedTrackBackground: Brush,
    val trackShape: Shape,
    val trackSize: DpSize,
    val trackBorderColor: Color,
    val trackBorderWidth: Dp,
    val trackGlowColor: Color,
    val thumbBackground: Brush,
    val thumbShape: Shape,
    val thumbSize: Dp,
    val thumbInset: Dp,
    val thumbElevation: Dp,
    val minimumTouchHeight: Dp,
    val labelForeground: Color,
    val labelStyle: TextStyle,
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val thumbAnimationMillis: Int,
    val alpha: Float = 1f,
)

@Immutable
data class MillenniumToggleSwitchTheme(
    val states: MillenniumControlStateProperty<MillenniumToggleSwitchThemeProperties>,
)

fun defaultMillenniumToggleSwitchTheme(colors: MillenniumColors): MillenniumToggleSwitchTheme {
    val base = MillenniumToggleSwitchThemeProperties(
        colors = colors,
        trackBackground = millenniumBevelBrush(colors, sunken = true),
        selectedTrackBackground = gelBrush(colors.accentTop, colors.accentBottom),
        trackShape = RoundedCornerShape(50),
        trackSize = DpSize(52.dp, 30.dp),
        trackBorderColor = colors.border,
        trackBorderWidth = 1.dp,
        trackGlowColor = colors.highlight.copy(alpha = 0.45f),
        thumbBackground = Brush.verticalGradient(listOf(colors.raised, colors.surface)),
        thumbShape = RoundedCornerShape(50),
        thumbSize = 24.dp,
        thumbInset = 3.dp,
        thumbElevation = 3.dp,
        minimumTouchHeight = 44.dp,
        labelForeground = colors.text,
        labelStyle = TextStyle(fontWeight = FontWeight.Normal),
        contentPadding = PaddingValues(0.dp),
        contentSpacing = 12.dp,
        thumbAnimationMillis = 160,
    )
    return MillenniumToggleSwitchTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(trackBorderColor = colors.accent, trackGlowColor = colors.highlight),
            focused = base.copy(trackBorderColor = colors.accent, trackBorderWidth = 2.dp),
            pressed = base.copy(thumbElevation = 1.dp),
            disabled = base.copy(alpha = 0.45f, labelForeground = colors.muted),
            selected = base.copy(trackBorderColor = colors.accentBottom),
            error = base.copy(trackBorderColor = colors.danger, trackBorderWidth = 2.dp),
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
    val focused by interaction.collectIsFocusedAsState()
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
    UnstyledSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.heightIn(min = properties.minimumTouchHeight).alpha(properties.alpha),
        enabled = enabled,
        accessibilityLabel = label,
        indication = LocalIndication.current,
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
                        background = if (checked) properties.selectedTrackBackground else properties.trackBackground,
                        shape = properties.trackShape,
                        borderColor = properties.trackBorderColor,
                        borderWidth = properties.trackBorderWidth,
                        innerGlowColor = properties.trackGlowColor,
                    ),
            ) {
                Thumb(
                    animationSpec = tween(properties.thumbAnimationMillis),
                    modifier = Modifier
                        .padding(properties.thumbInset)
                        .size(properties.thumbSize)
                        .millenniumSurface(
                            background = properties.thumbBackground,
                            shape = properties.thumbShape,
                            shadowElevation = properties.thumbElevation,
                        ),
                )
            }
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.merge(properties.labelStyle)
                    .copy(color = properties.labelForeground),
            ) { Text(label) }
        }
    }
}

@Preview
@Composable
private fun MillenniumSwitchPreview() = MillenniumPreview {
    MillenniumSwitch(checked = true, onCheckedChange = {}, label = "Airplane mode")
}
