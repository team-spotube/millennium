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
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumDialogThemeProperties(
    val panelBackground: Brush? = null,
    val shape: Shape,
    val foreground: Color? = null,
    val titleStyle: TextStyle,
    val panelBorderColor: Color? = null,
    val panelBorderWidth: Dp,
    val panelShadowElevation: Dp,
    val panelGlowColor: Color? = null,
    val panelMaxWidth: Dp,
    val overlayPadding: Dp,
    val contentPadding: Dp,
    val contentSpacing: Dp,
    val scrimColor: Color? = null,
    val enterScale: Float,
    val animationMillis: Int,
    val alpha: Float = 1f,
)

@Immutable
data class MillenniumDialogTheme(
    val states: MillenniumControlStateProperty<MillenniumDialogThemeProperties>,
)

fun defaultMillenniumDialogTheme(): MillenniumDialogTheme {
    val base = MillenniumDialogThemeProperties(
        shape = RoundedCornerShape(18.dp),
        titleStyle = TextStyle(fontSize = 22.sp, lineHeight = 29.sp, fontWeight = FontWeight.SemiBold),
        panelBorderWidth = 1.dp,
        panelShadowElevation = 12.dp,
        panelMaxWidth = 480.dp,
        overlayPadding = 24.dp,
        contentPadding = 24.dp,
        contentSpacing = 16.dp,
        enterScale = 0.96f,
        animationMillis = 180,
    )
    return MillenniumDialogTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(panelBorderWidth = 2.dp),
            pressed = base.copy(panelShadowElevation = 8.dp),
            disabled = base.copy(alpha = 0.65f),
            selected = base,
            error = base.copy(panelBorderWidth = 2.dp),
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
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.dialogTheme.states.resolve(MillenniumControlState.Default)
    val foreground = properties.foreground ?: colors.text
    val panelBorderColor = properties.panelBorderColor ?: colors.border
    val panelBackground = properties.panelBackground ?: millenniumBevelBrush(colors)
    val panelGlowColor = properties.panelGlowColor ?: colors.highlight.copy(alpha = 0.6f)
    val scrimColor = properties.scrimColor ?: colors.scrim
    UnstyledDialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        overlay = {
            Scrim(
                scrimColor = scrimColor,
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
                        background = panelBackground,
                        shape = properties.shape,
                        borderColor = panelBorderColor,
                        borderWidth = properties.panelBorderWidth,
                        shadowElevation = properties.panelShadowElevation,
                        innerGlowColor = panelGlowColor,
                    ),
                paneTitle = title,
                enter = fadeIn(animationSpec = tween(properties.animationMillis)) +
                    scaleIn(initialScale = properties.enterScale, animationSpec = tween(properties.animationMillis)),
                exit = fadeOut(animationSpec = tween(properties.animationMillis)),
            ) {
                MillenniumStyledContent(
                    TextStyle(color = foreground),
                    foreground,
                ) {
                    Column(
                        Modifier.padding(properties.contentPadding),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(properties.contentSpacing),
                    ) {
                        Text(title, style = properties.titleStyle, color = foreground)
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
