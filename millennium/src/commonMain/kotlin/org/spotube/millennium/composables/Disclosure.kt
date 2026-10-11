package org.spotube.millennium.composables

import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.DisclosedContent
import com.composeunstyled.DisclosureButton
import com.composeunstyled.Text
import com.composeunstyled.UnstyledDisclosure
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumDisclosureThemeProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val shape: Shape,
    val borderColor: Color? = null,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val innerGlowColor: Color? = null,
    val titleStyle: TextStyle,
    val contentStyle: TextStyle,
    val minimumHeaderHeight: Dp,
    val headerPadding: PaddingValues,
    val contentPadding: PaddingValues,
    val headerContentSpacing: Dp,
    val contentSpacing: Dp,
    val animationMillis: Int,
    val alpha: Float = 1f,
)

@Immutable
data class MillenniumDisclosureTheme(
    val states: MillenniumControlStateProperty<MillenniumDisclosureThemeProperties>,
)

fun defaultMillenniumDisclosureTheme(): MillenniumDisclosureTheme {
    val base = MillenniumDisclosureThemeProperties(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        borderWidth = 1.dp,
        shadowElevation = 0.dp,
        titleStyle = TextStyle(fontSize = 15.sp, lineHeight = 21.sp, fontWeight = FontWeight.SemiBold),
        contentStyle = TextStyle(fontSize = 14.sp, lineHeight = 21.sp),
        minimumHeaderHeight = 48.dp,
        headerPadding = PaddingValues(12.dp),
        contentPadding = PaddingValues(16.dp),
        headerContentSpacing = 12.dp,
        contentSpacing = 12.dp,
        animationMillis = 180,
    )
    return MillenniumDisclosureTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 2.dp),
            disabled = base.copy(alpha = 0.45f),
            selected = base,
            error = base.copy(borderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumDisclosure(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    background: Brush? = null,
    foreground: Color? = null,
    shape: Shape? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    shadowElevation: Dp? = null,
    innerGlowColor: Color? = null,
    titleStyle: TextStyle? = null,
    contentStyle: TextStyle? = null,
    minimumHeaderHeight: Dp? = null,
    headerPadding: PaddingValues? = null,
    contentPadding: PaddingValues? = null,
    contentSpacing: Dp? = null,
    animationMillis: Int? = null,
    alpha: Float? = null,
    theme: MillenniumDisclosureTheme = MillenniumTheme.disclosureTheme,
    content: @Composable () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsKeyboardFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val state = when {
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        expanded -> MillenniumControlState.Selected
        else -> MillenniumControlState.Default
    }
    val properties = theme.states.resolve(state)
    val colors = MillenniumTheme.colors
    val resolvedForeground = foreground ?: properties.foreground ?: colors.text
    val resolvedBorderColor = borderColor ?: properties.borderColor ?: when (state) {
        MillenniumControlState.Hovered, MillenniumControlState.Focused, MillenniumControlState.Selected -> colors.accent
        MillenniumControlState.Error -> colors.danger
        else -> colors.border
    }
    val resolvedAnimationMillis = animationMillis ?: properties.animationMillis
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(resolvedAnimationMillis),
    )
    UnstyledDisclosure(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier.millenniumSurface(
            background = background ?: properties.background ?: millenniumBevelBrush(colors),
            shape = shape ?: properties.shape,
            borderColor = resolvedBorderColor,
            borderWidth = borderWidth ?: properties.borderWidth,
            shadowElevation = shadowElevation ?: properties.shadowElevation,
            innerGlowColor = innerGlowColor ?: properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.5f),
        ).alpha(alpha ?: properties.alpha),
    ) {
        Column {
            DisclosureButton(
                modifier = Modifier.fillMaxWidth()
                    .heightIn(min = minimumHeaderHeight ?: properties.minimumHeaderHeight)
                    .padding(headerPadding ?: properties.headerPadding),
                contentPadding = PaddingValues(0.dp),
                indication = LocalIndication.current,
                interactionSource = interaction,
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(title, modifier = Modifier.weight(1f), style = titleStyle ?: properties.titleStyle, color = resolvedForeground)
                    MillenniumIcon(
                        MillenniumIcons.Chevron,
                        modifier = Modifier.rotate(arrowRotation),
                        tint = resolvedForeground,
                    )
                }
            }
            DisclosedContent(
                enter = expandVertically(animationSpec = tween(resolvedAnimationMillis)) +
                    fadeIn(animationSpec = tween(resolvedAnimationMillis)),
                exit = shrinkVertically(animationSpec = tween(resolvedAnimationMillis)) +
                    fadeOut(animationSpec = tween(resolvedAnimationMillis)),
            ) {
                Column(
                    Modifier.padding(contentPadding ?: properties.contentPadding),
                    verticalArrangement = Arrangement.spacedBy(contentSpacing ?: properties.contentSpacing),
                ) {
                    MillenniumStyledContent(contentStyle ?: properties.contentStyle, resolvedForeground, content)
                }
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumDisclosurePreview() = MillenniumPreview {
    MillenniumDisclosure(expanded = true, onExpandedChange = {}, title = "Why the glossy details?") {
        MillenniumText("Familiar depth and a little shine, with the accessibility and flexibility of modern Compose.")
    }
}
