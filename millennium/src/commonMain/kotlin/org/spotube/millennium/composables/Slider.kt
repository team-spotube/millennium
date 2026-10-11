package org.spotube.millennium.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledSlider
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumSliderThemeProperties(
    val trackBackground: Brush? = null,
    val activeTrackBackground: Brush? = null,
    val thumbBackground: Brush? = null,
    val trackShape: Shape,
    val thumbShape: Shape,
    val trackHeight: Dp,
    val thumbSize: Dp,
    val activeThumbSize: Dp,
    val touchTargetHeight: Dp,
    val trackBorderColor: Color? = null,
    val trackBorderWidth: Dp,
    val thumbBorderColor: Color? = null,
    val thumbBorderWidth: Dp,
    val thumbElevation: Dp,
    val thumbGlowColor: Color? = null,
    val stateAnimationMillis: Int,
    val alpha: Float = 1f,
    val thumbBottomRimColor: Color? = null,
    val trackInsetShadeColor: Color? = null,
)

@Immutable
data class MillenniumSliderTheme(
    val states: MillenniumControlStateProperty<MillenniumSliderThemeProperties>,
)

fun defaultMillenniumSliderTheme(): MillenniumSliderTheme {
    val base = MillenniumSliderThemeProperties(
        trackShape = RoundedCornerShape(50),
        thumbShape = RoundedCornerShape(6.dp),
        trackHeight = 10.dp,
        thumbSize = 24.dp,
        activeThumbSize = 26.dp,
        touchTargetHeight = 44.dp,
        trackBorderWidth = 1.dp,
        thumbBorderWidth = 1.dp,
        thumbElevation = 4.dp,
        stateAnimationMillis = 120,
    )
    return MillenniumSliderTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(thumbBorderWidth = 2.dp),
            pressed = base.copy(thumbElevation = 2.dp),
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(trackBorderWidth = 2.dp, thumbBorderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsKeyboardFocusedAsState()
    val pressed by interaction.collectIsPressedAsState()
    val dragged by interaction.collectIsDraggedAsState()
    val controlState = when {
        !enabled -> MillenniumControlState.Disabled
        dragged || pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        else -> MillenniumControlState.Default
    }
    val properties = MillenniumTheme.sliderTheme.states.resolve(controlState)
    val colors = MillenniumTheme.colors
    val trackBackground = properties.trackBackground ?: millenniumBevelBrush(colors, sunken = true)
    val activeTrackBackground = properties.activeTrackBackground ?: gelBrush(colors.accentTop, colors.accentBottom)
    val trackBorderColor = properties.trackBorderColor
        ?: if (controlState == MillenniumControlState.Error) colors.danger else colors.border
    val baseThumbSize = MillenniumTheme.sliderTheme.states.default.thumbSize
    UnstyledSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth().heightIn(min = properties.touchTargetHeight)
            .alpha(properties.alpha).semantics { contentDescription = label },
        enabled = enabled,
        interactionSource = interaction,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        track = { state ->
            // The thumb travels between half-thumb insets; inset the track the same way so
            // the active fill endpoint lines up with the thumb center.
            Box(Modifier.fillMaxWidth().padding(horizontal = baseThumbSize / 2)) {
                Box(
                    Modifier.fillMaxWidth().height(properties.trackHeight).millenniumSurface(
                        background = trackBackground,
                        shape = properties.trackShape,
                        borderColor = trackBorderColor,
                        borderWidth = properties.trackBorderWidth,
                        insetShadeColor = properties.trackInsetShadeColor ?: colors.text.copy(alpha = 0.1f),
                    ),
                ) {
                    Box(
                        Modifier.fillMaxHeight().fillMaxWidth(state.fraction)
                            .background(activeTrackBackground, properties.trackShape),
                    )
                }
            }
        },
        thumb = { state ->
            val thumbState = when {
                !enabled -> MillenniumControlState.Disabled
                state.isDragging || state.isPressed -> MillenniumControlState.Pressed
                state.isFocused -> MillenniumControlState.Focused
                else -> MillenniumControlState.Default
            }
            val thumbProperties = MillenniumTheme.sliderTheme.states.resolve(thumbState)
            val thumbBackground = thumbProperties.thumbBackground
                ?: millenniumBevelBrush(colors)
            val thumbBorderColor = thumbProperties.thumbBorderColor ?: when (thumbState) {
                MillenniumControlState.Focused, MillenniumControlState.Selected -> colors.accent
                MillenniumControlState.Error -> colors.danger
                else -> colors.border
            }
            val thumbGlowColor = thumbProperties.thumbGlowColor ?: colors.highlight.copy(alpha = 0.55f)
            val targetSize = if (state.isPressed || state.isDragging) thumbProperties.activeThumbSize else thumbProperties.thumbSize
            val animatedSize by animateDpAsState(targetSize, tween(thumbProperties.stateAnimationMillis))
            Box(Modifier.size(baseThumbSize), contentAlignment = Alignment.Center) {
                Box(
                    Modifier.size(animatedSize).millenniumSurface(
                        background = thumbBackground,
                        shape = thumbProperties.thumbShape,
                        borderColor = thumbBorderColor,
                        borderWidth = thumbProperties.thumbBorderWidth,
                        shadowElevation = thumbProperties.thumbElevation,
                        innerGlowColor = thumbGlowColor,
                        bottomRimColor = thumbProperties.thumbBottomRimColor ?: colors.highlight.copy(alpha = 0.6f),
                    ),
                )
            }
        },
    )
}

@Preview
@Composable
private fun MillenniumSliderPreview() = MillenniumPreview {
    MillenniumSlider(value = 0.68f, onValueChange = {}, label = "Volume")
}
