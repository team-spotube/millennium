package org.spotube.millennium.composables

import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumDisclosureThemeProperties(
    val colors: MillenniumColors,
    val background: Brush,
    val foreground: Color,
    val shape: Shape,
    val borderColor: Color,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val innerGlowColor: Color,
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

fun defaultMillenniumDisclosureTheme(colors: MillenniumColors): MillenniumDisclosureTheme {
    val base = MillenniumDisclosureThemeProperties(
        colors = colors,
        background = millenniumBevelBrush(colors),
        foreground = colors.text,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        borderColor = colors.border,
        borderWidth = 1.dp,
        shadowElevation = 0.dp,
        innerGlowColor = colors.highlight.copy(alpha = 0.5f),
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
            hovered = base.copy(borderColor = colors.accent),
            focused = base.copy(borderColor = colors.accent, borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 2.dp),
            disabled = base.copy(alpha = 0.45f, foreground = colors.muted),
            selected = base.copy(borderColor = colors.accent),
            error = base.copy(borderColor = colors.danger, borderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumDisclosure(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val state = when {
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        expanded -> MillenniumControlState.Selected
        else -> MillenniumControlState.Default
    }
    val properties = MillenniumTheme.disclosureTheme.states.resolve(state)
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(properties.animationMillis),
    )
    UnstyledDisclosure(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier.millenniumSurface(
            background = properties.background,
            shape = properties.shape,
            borderColor = properties.borderColor,
            borderWidth = properties.borderWidth,
            shadowElevation = properties.shadowElevation,
            innerGlowColor = properties.innerGlowColor,
        ).alpha(properties.alpha),
    ) {
        Column {
            DisclosureButton(
                modifier = Modifier.fillMaxWidth().heightIn(min = properties.minimumHeaderHeight)
                    .padding(properties.headerPadding),
                contentPadding = PaddingValues(0.dp),
                indication = LocalIndication.current,
                interactionSource = interaction,
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(title, modifier = Modifier.weight(1f), style = properties.titleStyle, color = properties.foreground)
                    MillenniumIcon(
                        MillenniumIcons.Chevron,
                        modifier = Modifier.rotate(arrowRotation),
                        tint = properties.foreground,
                    )
                }
            }
            DisclosedContent(
                enter = expandVertically(animationSpec = tween(properties.animationMillis)) +
                    fadeIn(animationSpec = tween(properties.animationMillis)),
                exit = shrinkVertically(animationSpec = tween(properties.animationMillis)) +
                    fadeOut(animationSpec = tween(properties.animationMillis)),
            ) {
                Column(
                    Modifier.padding(properties.contentPadding),
                    verticalArrangement = Arrangement.spacedBy(properties.contentSpacing),
                ) {
                    MillenniumStyledContent(properties.contentStyle, properties.foreground, content)
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
