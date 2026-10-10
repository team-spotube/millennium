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
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumButtonVariant
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.forVariant
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
    val buttonTheme = MillenniumTheme.buttonTheme
    val variant = when (style) {
        MillenniumButtonStyle.Primary -> MillenniumButtonVariant.Primary
        MillenniumButtonStyle.Secondary -> MillenniumButtonVariant.Secondary
        MillenniumButtonStyle.Quiet -> MillenniumButtonVariant.Quiet
        MillenniumButtonStyle.Danger -> MillenniumButtonVariant.Danger
    }
    val variantTheme = buttonTheme.forVariant(variant)
    val colors = MillenniumTheme.colorsFor(buttonTheme, variant)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val dimensions = variantTheme.dimensions
    val spacing = variantTheme.spacing
    val elevation = variantTheme.elevation
    val strokes = variantTheme.strokes
    val primary = style == MillenniumButtonStyle.Primary
    val foreground = when (style) {
        MillenniumButtonStyle.Primary -> colors.onAccent
        MillenniumButtonStyle.Danger -> colors.danger
        else -> colors.text
    }
    val visualState = when {
        !enabled -> MillenniumVisualState.Disabled
        pressed -> MillenniumVisualState.Clicked
        focused -> MillenniumVisualState.Focused
        hovered -> MillenniumVisualState.Hovered
        else -> MillenniumVisualState.Default
    }
    val baseVisual = MillenniumVisualStyle(
        background = if (primary) gelBrush(colors.accentTop, colors.accentBottom) else null,
        foregroundColor = foreground,
        borderColor = if (focused) colors.accent else if (primary) colors.accentBottom else colors.border,
        borderWidth = if (focused) strokes.focus else strokes.border,
        shadowElevation = if (pressed || style == MillenniumButtonStyle.Quiet) 0.dp else elevation.button,
    )
    val visual = variantTheme.resolve(visualState, baseVisual)
    val shape = visual.shape ?: variantTheme.shapes.control
    UnstyledButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interaction,
        indication = LocalIndication.current,
        modifier = modifier.defaultMinSize(minWidth = dimensions.buttonMinWidth, minHeight = dimensions.touchTarget)
            .alpha(visual.alpha ?: 1f)
            .beveled(colors, shape, accent = primary, sunken = pressed)
            .componentStyle(visual, shape),
        contentPadding = visual.contentPadding ?: PaddingValues(horizontal = spacing.buttonHorizontal, vertical = spacing.buttonVertical),
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.copy(
                color = visual.foregroundColor ?: foreground,
                fontSize = visual.textStyle?.fontSize ?: buttonTheme.typography.button.fontSize,
                lineHeight = visual.textStyle?.lineHeight ?: buttonTheme.typography.button.lineHeight,
                fontFamily = visual.textStyle?.fontFamily ?: buttonTheme.typography.button.fontFamily,
                fontWeight = visual.textStyle?.fontWeight ?: buttonTheme.typography.button.fontWeight,
            ),
        ) { content() }
    }
}

@Preview
@Composable
private fun MillenniumButtonPreview() = MillenniumPreview {
    MillenniumButton(onClick = {}) { Text("Continue") }
}
