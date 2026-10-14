package org.spotube.millennium.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
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
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.millenniumActionPointer
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumInteractionSurface

/** Shared face anatomy for the binary and tri-state checkbox controls. */
interface MillenniumCheckFaceStyle {
    val faceBackground: Brush?
    val selectedFaceBackground: Brush?
    val selectedFaceForeground: Color?
    val faceShape: Shape
    val faceSize: DpSize
    val markSize: Dp
    val dashSize: DpSize
    val faceBorderColor: Color?
    val faceBorderWidth: Dp
    val faceGlowColor: Color?
    val faceElevation: Dp
    val faceAlpha: Float
    val stateChangeMillis: Int
    val labelForeground: Color?
    val labelStyle: TextStyle
    val minimumTouchHeight: Dp
    val contentPadding: PaddingValues
    val contentSpacing: Dp
}

@Immutable
data class MillenniumCheckboxThemeProperties(
    override val faceBackground: Brush? = null,
    override val selectedFaceBackground: Brush? = null,
    override val selectedFaceForeground: Color? = null,
    override val faceShape: Shape,
    override val faceSize: DpSize,
    override val markSize: Dp,
    override val dashSize: DpSize,
    override val faceBorderColor: Color? = null,
    override val faceBorderWidth: Dp,
    override val faceGlowColor: Color? = null,
    override val faceElevation: Dp,
    override val faceAlpha: Float,
    override val stateChangeMillis: Int,
    override val labelForeground: Color? = null,
    override val labelStyle: TextStyle,
    override val minimumTouchHeight: Dp,
    override val contentPadding: PaddingValues,
    override val contentSpacing: Dp,
) : MillenniumCheckFaceStyle

@Immutable
data class MillenniumCheckboxTheme(
    val states: MillenniumControlStateProperty<MillenniumCheckboxThemeProperties>,
)

fun defaultMillenniumCheckboxTheme(): MillenniumCheckboxTheme {
    val base = MillenniumCheckboxThemeProperties(
        faceShape = androidx.compose.foundation.shape.RoundedCornerShape(5.dp),
        faceSize = DpSize(22.dp, 22.dp),
        markSize = 16.dp,
        dashSize = DpSize(11.dp, 2.dp),
        faceBorderWidth = 1.dp,
        faceElevation = 0.dp,
        faceAlpha = 1f,
        stateChangeMillis = 120,
        labelStyle = TextStyle(fontWeight = FontWeight.Normal),
        minimumTouchHeight = 44.dp,
        contentPadding = PaddingValues(0.dp),
        contentSpacing = 12.dp,
    )
    return MillenniumCheckboxTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(faceBorderWidth = 2.dp),
            pressed = base.copy(faceElevation = 0.dp),
            disabled = base.copy(faceAlpha = 0.45f),
            selected = base,
            error = base.copy(faceBorderWidth = 2.dp),
        ),
    )
}

@Composable
internal fun MillenniumCheckFace(
    value: ToggleableState,
    properties: MillenniumCheckFaceStyle,
    state: MillenniumControlState,
) {
    val colors = MillenniumTheme.colors
    val faceBackground = properties.faceBackground
        ?: if (value == ToggleableState.Off) millenniumBevelBrush(colors, sunken = true)
        else gelBrush(colors.accentTop, colors.accentBottom)
    val targetBorderColor = properties.faceBorderColor
        ?: when (state) {
            MillenniumControlState.Hovered, MillenniumControlState.Pressed, MillenniumControlState.Focused -> colors.accent
            else -> if (value == ToggleableState.Off) colors.border else colors.accentBottom
        }
    val borderColor by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(properties.stateChangeMillis),
    )
    val glowColor = properties.faceGlowColor ?: colors.highlight.copy(alpha = 0.55f)
    val selectedForeground = properties.selectedFaceForeground ?: colors.onAccent
    val selected = value != ToggleableState.Off
    val scale by animateFloatAsState(
        targetValue = when (state) {
            MillenniumControlState.Pressed -> 0.90f
            MillenniumControlState.Hovered -> 1.05f
            else -> 1f
        },
        animationSpec = spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessMedium),
    )
    Box(
        Modifier
            .size(properties.faceSize)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .millenniumSurface(
                background = if (selected) properties.selectedFaceBackground
                    ?: gelBrush(colors.accentTop, colors.accentBottom) else faceBackground,
                shape = properties.faceShape,
                borderColor = borderColor,
                borderWidth = properties.faceBorderWidth,
                shadowElevation = properties.faceElevation,
                innerGlowColor = glowColor,
            )
            .millenniumInteractionSurface(state, properties.faceShape, animationMillis = properties.stateChangeMillis)
            .alpha(properties.faceAlpha),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                (scaleIn(
                    initialScale = 0.4f,
                    animationSpec = spring(
                        dampingRatio = 0.52f,
                        stiffness = Spring.StiffnessMedium
                    ),
                ) + fadeIn(
                    tween(
                        properties.stateChangeMillis,
                        easing = FastOutSlowInEasing
                    )
                )) togetherWith
                        (scaleOut(
                            targetScale = 0.4f,
                            animationSpec = tween(
                                (properties.stateChangeMillis * 0.7f).toInt().coerceAtLeast(60),
                                easing = FastOutLinearInEasing
                            ),
                        ) + fadeOut(
                            tween(
                                (properties.stateChangeMillis * 0.7f).toInt().coerceAtLeast(60),
                                easing = FastOutLinearInEasing
                            )
                        ))
            },
        ) { currentValue ->
            Box(Modifier.size(properties.faceSize), contentAlignment = Alignment.Center) {
                when (currentValue) {
                    ToggleableState.On -> MillenniumIcon(
                        MillenniumIcons.Check,
                        modifier = Modifier.size(properties.markSize),
                        tint = selectedForeground,
                    )

                    ToggleableState.Indeterminate -> Box(
                        Modifier
                            .size(properties.dashSize)
                            .background(selectedForeground, CircleShape),
                    )

                    ToggleableState.Off -> Unit
                }
            }
        }
    }
}

@Composable
fun MillenniumCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    faceBackground: Brush? = null,
    selectedFaceBackground: Brush? = null,
    selectedFaceForeground: Color? = null,
    faceShape: Shape? = null,
    faceSize: DpSize? = null,
    markSize: Dp? = null,
    dashSize: DpSize? = null,
    faceBorderColor: Color? = null,
    faceBorderWidth: Dp? = null,
    faceGlowColor: Color? = null,
    faceElevation: Dp? = null,
    faceAlpha: Float? = null,
    stateChangeMillis: Int? = null,
    labelForeground: Color? = null,
    labelStyle: TextStyle? = null,
    minimumTouchHeight: Dp? = null,
    contentPadding: PaddingValues? = null,
    contentSpacing: Dp? = null,
    theme: MillenniumCheckboxTheme = MillenniumTheme.checkboxTheme,
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val focused by source.collectIsKeyboardFocusedAsState()
    val hovered by source.collectIsHoveredAsState()
    val state = when {
        !enabled -> MillenniumControlState.Disabled
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        checked -> MillenniumControlState.Selected
        else -> MillenniumControlState.Default
    }
    val properties = theme.states.resolve(state)
    val faceProperties = properties.copy(
        faceBackground = faceBackground ?: properties.faceBackground,
        selectedFaceBackground = selectedFaceBackground ?: properties.selectedFaceBackground,
        selectedFaceForeground = selectedFaceForeground ?: properties.selectedFaceForeground,
        faceShape = faceShape ?: properties.faceShape,
        faceSize = faceSize ?: properties.faceSize,
        markSize = markSize ?: properties.markSize,
        dashSize = dashSize ?: properties.dashSize,
        faceBorderColor = faceBorderColor ?: properties.faceBorderColor,
        faceBorderWidth = faceBorderWidth ?: properties.faceBorderWidth,
        faceGlowColor = faceGlowColor ?: properties.faceGlowColor,
        faceElevation = faceElevation ?: properties.faceElevation,
        faceAlpha = faceAlpha ?: properties.faceAlpha,
        stateChangeMillis = stateChangeMillis ?: properties.stateChangeMillis,
    )
    val labelColor = labelForeground ?: properties.labelForeground ?: when (state) {
        MillenniumControlState.Disabled -> MillenniumTheme.colors.muted
        else -> MillenniumTheme.colors.text
    }
    UnstyledCheckbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier.millenniumActionPointer(enabled)
            .heightIn(min = minimumTouchHeight ?: properties.minimumTouchHeight),
        enabled = enabled,
        accessibilityLabel = label,
        indication = null,
        interactionSource = source,
    ) {
        Row(
            modifier = Modifier.padding(contentPadding ?: properties.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                contentSpacing ?: properties.contentSpacing
            ),
        ) {
            MillenniumCheckFace(
                value = if (checked) ToggleableState.On else ToggleableState.Off,
                properties = faceProperties,
                state = state,
            )
            if (!label.isNullOrBlank()) {
                CompositionLocalProvider(
                    LocalTextStyle provides LocalTextStyle.current.merge(
                        labelStyle ?: properties.labelStyle
                    )
                        .copy(color = labelColor),
                ) { Text(label) }
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumCheckboxPreview() = MillenniumPreview {
    MillenniumCheckbox(checked = true, onCheckedChange = {}, label = "Remember this choice")
}
