package org.spotube.millennium.composables

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.DialogPanel
import com.composeunstyled.Scrim
import com.composeunstyled.Text
import com.composeunstyled.UnstyledDialog
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumDialogThemeProperties(
    val colors: MillenniumColors,
    val panelBackground: Brush,
    val shape: Shape,
    val foreground: Color,
    val titleStyle: TextStyle,
    val panelBorderColor: Color,
    val panelBorderWidth: Dp,
    val panelShadowElevation: Dp,
    val panelGlowColor: Color,
    val panelMaxWidth: Dp,
    val overlayPadding: Dp,
    val contentPadding: Dp,
    val contentSpacing: Dp,
    val scrimColor: Color,
    val enterScale: Float,
    val animationMillis: Int,
    val alpha: Float = 1f,
)

@Immutable
data class MillenniumDialogTheme(
    val states: MillenniumControlStateProperty<MillenniumDialogThemeProperties>,
)

fun defaultMillenniumDialogTheme(colors: MillenniumColors): MillenniumDialogTheme {
    val base = MillenniumDialogThemeProperties(
        colors = colors,
        panelBackground = millenniumBevelBrush(colors),
        shape = RoundedCornerShape(18.dp),
        foreground = colors.text,
        titleStyle = TextStyle(fontSize = 22.sp, lineHeight = 29.sp, fontWeight = FontWeight.SemiBold),
        panelBorderColor = colors.border,
        panelBorderWidth = 1.dp,
        panelShadowElevation = 12.dp,
        panelGlowColor = colors.highlight.copy(alpha = 0.6f),
        panelMaxWidth = 480.dp,
        overlayPadding = 24.dp,
        contentPadding = 24.dp,
        contentSpacing = 16.dp,
        scrimColor = colors.scrim,
        enterScale = 0.96f,
        animationMillis = 180,
    )
    return MillenniumDialogTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(panelBorderColor = colors.accent),
            focused = base.copy(panelBorderColor = colors.accent, panelBorderWidth = 2.dp),
            pressed = base.copy(panelShadowElevation = 8.dp),
            disabled = base.copy(alpha = 0.65f),
            selected = base.copy(panelBorderColor = colors.accent),
            error = base.copy(panelBorderColor = colors.danger, panelBorderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val properties = MillenniumTheme.dialogTheme.states.resolve(MillenniumControlState.Default)
    UnstyledDialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        overlay = {
            Scrim(
                scrimColor = properties.scrimColor,
                enter = fadeIn(animationSpec = tween(properties.animationMillis)),
                exit = fadeOut(animationSpec = tween(properties.animationMillis)),
            )
        },
    ) {
        Box(
            Modifier.fillMaxSize().padding(properties.overlayPadding),
            contentAlignment = androidx.compose.ui.Alignment.Center,
        ) {
            DialogPanel(
                modifier.widthIn(max = properties.panelMaxWidth).fillMaxWidth().alpha(properties.alpha)
                    .millenniumSurface(
                        background = properties.panelBackground,
                        shape = properties.shape,
                        borderColor = properties.panelBorderColor,
                        borderWidth = properties.panelBorderWidth,
                        shadowElevation = properties.panelShadowElevation,
                        innerGlowColor = properties.panelGlowColor,
                    ),
                paneTitle = title,
                enter = fadeIn(animationSpec = tween(properties.animationMillis)) +
                    scaleIn(initialScale = properties.enterScale, animationSpec = tween(properties.animationMillis)),
                exit = fadeOut(animationSpec = tween(properties.animationMillis)),
            ) {
                MillenniumStyledContent(
                    TextStyle(color = properties.foreground),
                    properties.foreground,
                ) {
                    Column(
                        Modifier.padding(properties.contentPadding),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(properties.contentSpacing),
                    ) {
                        Text(title, style = properties.titleStyle, color = properties.foreground)
                        content()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumDialogPreview() = MillenniumPreview {
    MillenniumDialog(visible = true, onDismissRequest = {}, title = "A little update") {
        MillenniumText("A familiar message panel, dressed for today.")
    }
}
