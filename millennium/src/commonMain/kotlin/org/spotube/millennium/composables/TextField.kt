package org.spotube.millennium.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MillenniumTextField(
    state: TextFieldState,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    error: String? = null,
) {
    val fieldTheme = MillenniumTheme.textFieldTheme
    val colors = MillenniumTheme.colorsFor(fieldTheme)
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val pressed by interaction.collectIsPressedAsState()
    val visualState = when {
        !enabled -> MillenniumVisualState.Disabled
        error != null -> MillenniumVisualState.Error
        pressed -> MillenniumVisualState.Clicked
        focused -> MillenniumVisualState.Focused
        hovered -> MillenniumVisualState.Hovered
        else -> MillenniumVisualState.Default
    }
    val visual = MillenniumTheme.textFieldTheme.resolve(
        visualState,
        MillenniumVisualStyle(
            backgroundColor = colors.inset,
            foregroundColor = colors.text,
            borderColor = when {
                error != null -> colors.danger
                focused -> colors.accent
                else -> colors.border
            },
            borderWidth = if (focused || error != null) fieldTheme.strokes.focus else fieldTheme.strokes.border,
        ),
    )
    UnstyledTextField(
        state = state,
        modifier = modifier.componentStyle(visual, fieldTheme.shapes.control).alpha(visual.alpha ?: 1f),
        enabled = enabled,
        accessibilityLabel = label,
        interactionSource = interaction,
        lineLimits = TextFieldLineLimits.SingleLine,
        textStyle = visual.textStyle ?: fieldTheme.typography.body.copy(color = visual.foregroundColor ?: colors.text),
        cursorBrush = SolidColor(visual.foregroundColor ?: colors.accent),
        selectionColors = TextSelectionColors(colors.accent, colors.accent.copy(alpha = 0.25f)),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(visual.contentSpacing ?: fieldTheme.spacing.small)) {
            Text(label, style = visual.textStyle ?: fieldTheme.typography.label, color = visual.foregroundColor ?: colors.text)
            TextInput(
                Modifier.fillMaxWidth().heightIn(min = fieldTheme.dimensions.textFieldMinHeight)
                    .beveled(colors, visual.shape ?: fieldTheme.shapes.control, sunken = true)
                    .componentStyle(visual, visual.shape ?: fieldTheme.shapes.control)
                    .padding(visual.contentPadding ?: PaddingValues(
                        horizontal = fieldTheme.spacing.textFieldHorizontal,
                        vertical = fieldTheme.spacing.textFieldVertical,
                    )),
                placeholder = { Text(placeholder, color = colors.muted) },
            )
            if (error != null) Text(error, style = fieldTheme.typography.caption, color = colors.danger)
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
