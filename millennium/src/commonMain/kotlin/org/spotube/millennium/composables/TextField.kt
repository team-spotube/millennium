package org.spotube.millennium.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import com.composeunstyled.Text
import com.composeunstyled.TextInput
import com.composeunstyled.UnstyledTextField
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumInteractionSurface
import androidx.compose.foundation.hoverable

@Immutable
data class MillenniumTextFieldThemeProperties(
    val background: Brush? = null,
    val shape: Shape,
    val textStyle: TextStyle,
    val labelStyle: TextStyle,
    val placeholderStyle: TextStyle,
    val errorStyle: TextStyle,
    val foreground: Color? = null,
    val placeholderColor: Color? = null,
    val cursorColor: Color? = null,
    val selectionColor: Color? = null,
    val errorColor: Color? = null,
    val borderColor: Color? = null,
    val borderWidth: Dp,
    val minimumHeight: Dp,
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val stateAnimationMillis: Int,
    val alpha: Float = 1f,
    val insetShadeColor: Color? = null,
    val innerGlowColor: Color? = null,
    val bottomRimColor: Color? = null,
)

@Immutable
data class MillenniumTextFieldTheme(
    val states: MillenniumControlStateProperty<MillenniumTextFieldThemeProperties>,
)

fun defaultMillenniumTextFieldTheme(): MillenniumTextFieldTheme {
    val base = MillenniumTextFieldThemeProperties(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(7.dp),
        textStyle = TextStyle(fontSize = 14.sp, lineHeight = 21.sp),
        labelStyle = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium
        ),
        placeholderStyle = TextStyle(fontSize = 14.sp, lineHeight = 21.sp),
        errorStyle = TextStyle(fontSize = 12.sp, lineHeight = 17.sp),
        borderWidth = 1.dp,
        minimumHeight = 52.dp,
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
        contentSpacing = 8.dp,
        stateAnimationMillis = 130,
    )
    return MillenniumTextFieldTheme(
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

@Composable
fun MillenniumTextField(
    state: TextFieldState,
    label: String? = null,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    error: String? = null,
    background: Brush? = null,
    shape: Shape? = null,
    textStyle: TextStyle? = null,
    labelStyle: TextStyle? = null,
    placeholderStyle: TextStyle? = null,
    errorStyle: TextStyle? = null,
    foreground: Color? = null,
    placeholderColor: Color? = null,
    cursorColor: Color? = null,
    selectionColor: Color? = null,
    errorColor: Color? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    minimumHeight: Dp? = null,
    contentPadding: PaddingValues? = null,
    contentSpacing: Dp? = null,
    stateAnimationMillis: Int? = null,
    insetShadeColor: Color? = null,
    innerGlowColor: Color? = null,
    bottomRimColor: Color? = null,
    alpha: Float? = null,
    theme: MillenniumTextFieldTheme = MillenniumTheme.textFieldTheme,
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsKeyboardFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val pressed by interaction.collectIsPressedAsState()
    val controlState = when {
        !enabled -> MillenniumControlState.Disabled
        error != null -> MillenniumControlState.Error
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        else -> MillenniumControlState.Default
    }
    val properties = theme.states.resolve(controlState)
    val colors = MillenniumTheme.colors
    val resolvedTextStyle = textStyle ?: properties.textStyle
    val resolvedForeground = foreground
        ?: properties.foreground
        ?: resolvedTextStyle.color.takeUnless { it == Color.Unspecified }
        ?: if (controlState == MillenniumControlState.Disabled) colors.muted else colors.text
    val resolvedPlaceholderStyle = placeholderStyle ?: properties.placeholderStyle
    val resolvedPlaceholderColor = placeholderColor
        ?: properties.placeholderColor
        ?: resolvedPlaceholderStyle.color.takeUnless { it == Color.Unspecified }
        ?: colors.muted
    val resolvedCursorColor = cursorColor ?: properties.cursorColor ?: colors.accent
    val resolvedSelectionColor =
        selectionColor ?: properties.selectionColor ?: colors.accent.copy(alpha = 0.25f)
    val resolvedErrorColor = errorColor ?: properties.errorColor ?: colors.danger
    val resolvedBorderColor = borderColor ?: properties.borderColor ?: when (controlState) {
        MillenniumControlState.Focused, MillenniumControlState.Hovered, MillenniumControlState.Pressed -> colors.accent
        MillenniumControlState.Error -> colors.danger
        else -> colors.border
    }
    val animatedBorder by animateColorAsState(
        targetValue = resolvedBorderColor,
        animationSpec = tween(stateAnimationMillis ?: properties.stateAnimationMillis),
    )
    UnstyledTextField(
        state = state,
        modifier = modifier.alpha(alpha ?: properties.alpha),
        enabled = enabled,
        accessibilityLabel = label,
        interactionSource = interaction,
        lineLimits = TextFieldLineLimits.SingleLine,
        textStyle = resolvedTextStyle.copy(color = resolvedForeground),
        cursorBrush = SolidColor(resolvedCursorColor),
        selectionColors = TextSelectionColors(resolvedCursorColor, resolvedSelectionColor),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                contentSpacing ?: properties.contentSpacing
            )
        ) {
            if (!label.isNullOrBlank())
                Text(
                    label,
                    style = (labelStyle ?: properties.labelStyle).copy(color = resolvedForeground)
                )
            Box(
                Modifier
                    .hoverable(interaction, enabled)
                    .fillMaxWidth()
                    .heightIn(min = minimumHeight ?: properties.minimumHeight)
                    .millenniumSurface(
                        background = background ?: properties.background ?: millenniumBevelBrush(
                            colors,
                            sunken = true
                        ),
                        shape = shape ?: properties.shape,
                        borderColor = animatedBorder,
                        borderWidth = borderWidth ?: properties.borderWidth,
                        insetShadeColor = insetShadeColor ?: properties.insetShadeColor
                        ?: colors.text.copy(alpha = 0.09f),
                        innerGlowColor = innerGlowColor ?: properties.innerGlowColor
                        ?: colors.border.copy(alpha = 0.4f),
                        bottomRimColor = bottomRimColor ?: properties.bottomRimColor
                        ?: colors.highlight.copy(alpha = 0.8f),
                    )
                    .millenniumInteractionSurface(controlState, shape ?: properties.shape, animationMillis = stateAnimationMillis ?: properties.stateAnimationMillis),
                contentAlignment = Alignment.CenterStart,
            ) {
                TextInput(
                    Modifier.fillMaxWidth().padding(contentPadding ?: properties.contentPadding),
                    placeholder = {
                        Text(
                            placeholder,
                            style = resolvedPlaceholderStyle.copy(color = resolvedPlaceholderColor),
                            color = resolvedPlaceholderColor,
                        )
                    },
                )
            }
            if (error != null) {
                Text(
                    error,
                    style = (errorStyle ?: properties.errorStyle).copy(color = resolvedErrorColor),
                    color = resolvedErrorColor,
                )
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumTextFieldPreview() = MillenniumPreview {
    MillenniumTextField(
        state = rememberTextFieldState("Alex"),
        label = "Display name",
        placeholder = "Your name",
    )
}
