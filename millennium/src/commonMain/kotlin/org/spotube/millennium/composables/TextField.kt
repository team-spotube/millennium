package org.spotube.millennium.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
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
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumTextFieldThemeProperties(
    val colors: MillenniumColors,
    val background: Brush,
    val shape: Shape,
    val textStyle: TextStyle,
    val labelStyle: TextStyle,
    val placeholderStyle: TextStyle,
    val errorStyle: TextStyle,
    val foreground: Color,
    val placeholderColor: Color,
    val cursorColor: Color,
    val selectionColor: Color,
    val errorColor: Color,
    val borderColor: Color,
    val borderWidth: Dp,
    val minimumHeight: Dp,
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val stateAnimationMillis: Int,
    val alpha: Float = 1f,
)

@Immutable
data class MillenniumTextFieldTheme(
    val states: MillenniumControlStateProperty<MillenniumTextFieldThemeProperties>,
)

fun defaultMillenniumTextFieldTheme(colors: MillenniumColors): MillenniumTextFieldTheme {
    val base = MillenniumTextFieldThemeProperties(
        colors = colors,
        background = millenniumBevelBrush(colors, sunken = true),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp),
        textStyle = TextStyle(fontSize = 14.sp, lineHeight = 21.sp, color = colors.text),
        labelStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium, color = colors.text),
        placeholderStyle = TextStyle(fontSize = 14.sp, lineHeight = 21.sp, color = colors.muted),
        errorStyle = TextStyle(fontSize = 12.sp, lineHeight = 17.sp, color = colors.danger),
        foreground = colors.text,
        placeholderColor = colors.muted,
        cursorColor = colors.accent,
        selectionColor = colors.accent.copy(alpha = 0.25f),
        errorColor = colors.danger,
        borderColor = colors.border,
        borderWidth = 1.dp,
        minimumHeight = 46.dp,
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        contentSpacing = 8.dp,
        stateAnimationMillis = 130,
    )
    return MillenniumTextFieldTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(borderColor = colors.accent),
            focused = base.copy(borderColor = colors.accent, borderWidth = 2.dp),
            pressed = base.copy(borderColor = colors.accent),
            disabled = base.copy(alpha = 0.45f, foreground = colors.muted),
            selected = base.copy(borderColor = colors.accent),
            error = base.copy(borderColor = colors.danger, borderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumTextField(
    state: TextFieldState,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    error: String? = null,
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
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
    val properties = MillenniumTheme.textFieldTheme.states.resolve(controlState)
    val animatedBorder by animateColorAsState(
        targetValue = properties.borderColor,
        animationSpec = tween(properties.stateAnimationMillis),
    )
    UnstyledTextField(
        state = state,
        modifier = modifier.alpha(properties.alpha),
        enabled = enabled,
        accessibilityLabel = label,
        interactionSource = interaction,
        lineLimits = TextFieldLineLimits.SingleLine,
        textStyle = properties.textStyle.copy(color = properties.foreground),
        cursorBrush = SolidColor(properties.cursorColor),
        selectionColors = TextSelectionColors(properties.cursorColor, properties.selectionColor),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(properties.contentSpacing)) {
            Text(label, style = properties.labelStyle.copy(color = properties.foreground))
            TextInput(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = properties.minimumHeight)
                    .millenniumSurface(
                        background = properties.background,
                        shape = properties.shape,
                        borderColor = animatedBorder,
                        borderWidth = properties.borderWidth,
                    )
                    .padding(properties.contentPadding),
                placeholder = {
                    Text(placeholder, style = properties.placeholderStyle, color = properties.placeholderColor)
                },
            )
            if (error != null) {
                Text(error, style = properties.errorStyle, color = properties.errorColor)
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
