package org.spotube.millennium.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.Text
import com.composeunstyled.UnstyledCheckbox
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

/** Shared face anatomy for the binary and tri-state checkbox controls. */
interface MillenniumCheckFaceStyle {
    val colors: MillenniumColors
    val faceBackground: Brush
    val selectedFaceBackground: Brush
    val selectedFaceForeground: Color
    val faceShape: Shape
    val faceSize: DpSize
    val markSize: Dp
    val dashSize: DpSize
    val faceBorderColor: Color
    val faceBorderWidth: Dp
    val faceGlowColor: Color
    val faceElevation: Dp
    val faceAlpha: Float
    val stateChangeMillis: Int
    val labelForeground: Color
    val labelStyle: TextStyle
    val minimumTouchHeight: Dp
    val contentPadding: PaddingValues
    val contentSpacing: Dp
}

@Immutable
data class MillenniumCheckboxThemeProperties(
    override val colors: MillenniumColors,
    override val faceBackground: Brush,
    override val selectedFaceBackground: Brush,
    override val selectedFaceForeground: Color,
    override val faceShape: Shape,
    override val faceSize: DpSize,
    override val markSize: Dp,
    override val dashSize: DpSize,
    override val faceBorderColor: Color,
    override val faceBorderWidth: Dp,
    override val faceGlowColor: Color,
    override val faceElevation: Dp,
    override val faceAlpha: Float,
    override val stateChangeMillis: Int,
    override val labelForeground: Color,
    override val labelStyle: TextStyle,
    override val minimumTouchHeight: Dp,
    override val contentPadding: PaddingValues,
    override val contentSpacing: Dp,
) : MillenniumCheckFaceStyle

@Immutable
data class MillenniumCheckboxTheme(
    val states: MillenniumControlStateProperty<MillenniumCheckboxThemeProperties>,
)

fun defaultMillenniumCheckboxTheme(colors: MillenniumColors): MillenniumCheckboxTheme {
    val base = MillenniumCheckboxThemeProperties(
        colors = colors,
        faceBackground = millenniumBevelBrush(colors, sunken = true),
        selectedFaceBackground = gelBrush(colors.accentTop, colors.accentBottom),
        selectedFaceForeground = colors.onAccent,
        faceShape = androidx.compose.foundation.shape.RoundedCornerShape(5.dp),
        faceSize = DpSize(22.dp, 22.dp),
        markSize = 16.dp,
        dashSize = DpSize(11.dp, 2.dp),
        faceBorderColor = colors.border,
        faceBorderWidth = 1.dp,
        faceGlowColor = colors.highlight.copy(alpha = 0.55f),
        faceElevation = 0.dp,
        faceAlpha = 1f,
        stateChangeMillis = 120,
        labelForeground = colors.text,
        labelStyle = TextStyle(fontWeight = FontWeight.Normal),
        minimumTouchHeight = 44.dp,
        contentPadding = PaddingValues(0.dp),
        contentSpacing = 12.dp,
    )
    return MillenniumCheckboxTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(faceBorderColor = colors.accent, faceGlowColor = colors.highlight),
            focused = base.copy(faceBorderColor = colors.accent, faceBorderWidth = 2.dp),
            pressed = base.copy(faceElevation = 0.dp),
            disabled = base.copy(faceAlpha = 0.45f, labelForeground = colors.muted),
            selected = base.copy(
                faceBorderColor = colors.accentBottom,
            ),
            error = base.copy(faceBorderColor = colors.danger, faceBorderWidth = 2.dp),
        ),
    )
}

@Composable
internal fun MillenniumCheckFace(
    value: ToggleableState,
    properties: MillenniumCheckFaceStyle,
    state: MillenniumControlState,
) {
    val selected = value != ToggleableState.Off
    val scale by animateFloatAsState(
        targetValue = if (state == MillenniumControlState.Pressed) 0.94f else 1f,
        animationSpec = tween(properties.stateChangeMillis),
    )
    Box(
        Modifier
            .size(properties.faceSize)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .millenniumSurface(
                background = if (selected) properties.selectedFaceBackground else properties.faceBackground,
                shape = properties.faceShape,
                borderColor = properties.faceBorderColor,
                borderWidth = properties.faceBorderWidth,
                shadowElevation = properties.faceElevation,
                innerGlowColor = properties.faceGlowColor,
            )
            .alpha(properties.faceAlpha),
        contentAlignment = Alignment.Center,
    ) {
        when (value) {
            ToggleableState.On -> MillenniumIcon(
                MillenniumIcons.Check,
                modifier = Modifier.size(properties.markSize),
                tint = properties.selectedFaceForeground,
            )
            ToggleableState.Indeterminate -> Box(
                Modifier
                    .size(properties.dashSize)
                    .background(properties.selectedFaceForeground, CircleShape),
            )
            ToggleableState.Off -> Unit
        }
    }
}

@Composable
fun MillenniumCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val focused by source.collectIsFocusedAsState()
    val hovered by source.collectIsHoveredAsState()
    val state = when {
        !enabled -> MillenniumControlState.Disabled
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        checked -> MillenniumControlState.Selected
        else -> MillenniumControlState.Default
    }
    val properties = MillenniumTheme.checkboxTheme.states.resolve(state)
    UnstyledCheckbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.heightIn(min = properties.minimumTouchHeight),
        enabled = enabled,
        accessibilityLabel = label,
        indication = LocalIndication.current,
        interactionSource = source,
    ) {
        Row(
            modifier = Modifier.padding(properties.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(properties.contentSpacing),
        ) {
            MillenniumCheckFace(
                value = if (checked) ToggleableState.On else ToggleableState.Off,
                properties = properties,
                state = state,
            )
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.merge(properties.labelStyle)
                    .copy(color = properties.labelForeground),
            ) { Text(label) }
        }
    }
}

@Preview
@Composable
private fun MillenniumCheckboxPreview() = MillenniumPreview {
    MillenniumCheckbox(checked = true, onCheckedChange = {}, label = "Remember this choice")
}
