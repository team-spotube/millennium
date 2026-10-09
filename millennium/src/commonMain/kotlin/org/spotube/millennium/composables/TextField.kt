package org.spotube.millennium.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
    val colors = MillenniumTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    UnstyledTextField(
        state = state,
        modifier = modifier.alpha(if (enabled) 1f else 0.45f),
        enabled = enabled,
        accessibilityLabel = label,
        interactionSource = interaction,
        lineLimits = TextFieldLineLimits.SingleLine,
        textStyle = TextStyle(color = colors.text, fontSize = 14.sp, lineHeight = 21.sp),
        cursorBrush = SolidColor(colors.accent),
        selectionColors = TextSelectionColors(colors.accent, colors.accent.copy(alpha = 0.25f)),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(label, fontWeight = FontWeight.Medium)
            TextInput(
                Modifier.fillMaxWidth().heightIn(min = 46.dp).beveled(colors, sunken = true)
                    .border(
                        if (focused || error != null) 2.dp else 0.dp,
                        if (error != null) colors.danger else if (focused) colors.accent else Color.Transparent,
                        MillenniumTheme.shape,
                    ).padding(horizontal = 14.dp, vertical = 12.dp),
                placeholder = { Text(placeholder, color = colors.muted) },
            )
            if (error != null) Text(error, color = colors.danger, fontSize = 12.sp)
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
