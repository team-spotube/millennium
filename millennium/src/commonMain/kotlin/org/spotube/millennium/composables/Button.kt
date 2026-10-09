package org.spotube.millennium.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.UnstyledButton
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview
import com.composeunstyled.Text

enum class MillenniumButtonStyle { Primary, Secondary, Quiet, Danger }

@Composable
fun MillenniumButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: MillenniumButtonStyle = MillenniumButtonStyle.Primary,
    content: @Composable () -> Unit,
) {
    val colors = MillenniumTheme.colors
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val primary = style == MillenniumButtonStyle.Primary
    val foreground = when (style) {
        MillenniumButtonStyle.Primary -> colors.onAccent
        MillenniumButtonStyle.Danger -> colors.danger
        else -> colors.text
    }
    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        indication = LocalIndication.current,
        modifier = modifier.defaultMinSize(minWidth = 48.dp, minHeight = 44.dp)
            .alpha(if (enabled) 1f else 0.45f)
            .shadow(if (pressed || style == MillenniumButtonStyle.Quiet) 0.dp else 3.dp, MillenniumTheme.shape)
            .beveled(colors, accent = primary, sunken = pressed)
            .then(if (focused) Modifier.border(2.dp, colors.accent, MillenniumTheme.shape) else Modifier),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.copy(
                color = if (pressed) foreground.copy(alpha = 0.92f) else foreground,
                fontWeight = FontWeight.SemiBold,
            ),
        ) { content() }
    }
}

@Preview
@Composable
private fun MillenniumButtonPreview() = MillenniumPreview {
    MillenniumButton(onClick = {}) { Text("Continue") }
}
