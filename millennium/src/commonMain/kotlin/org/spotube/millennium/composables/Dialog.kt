package org.spotube.millennium.composables

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.DialogPanel
import com.composeunstyled.Scrim
import com.composeunstyled.Text
import com.composeunstyled.UnstyledButton
import com.composeunstyled.UnstyledDialog
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface
import org.spotube.millennium.theme.millenniumPanelBrush

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
    val titleBarBackground: Brush? = null,
    val titleBarForeground: Color? = null,
    val titleBarHeight: Dp = 46.dp,
    val titleBarPadding: PaddingValues = PaddingValues(horizontal = 14.dp),
    val titleBarBorderColor: Color? = null,
)

@Immutable
data class MillenniumDialogTheme(
    val states: MillenniumControlStateProperty<MillenniumDialogThemeProperties>,
)

fun defaultMillenniumDialogTheme(): MillenniumDialogTheme {
    val base = MillenniumDialogThemeProperties(
        shape = RoundedCornerShape(10.dp),
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
    val panelBackground = properties.panelBackground ?: millenniumPanelBrush(colors)
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
                    Column {
                        val titleBarBackground = properties.titleBarBackground
                            ?: org.spotube.millennium.theme.gelBrush(colors.accentTop, colors.accentBottom)
                        val titleBarForeground = properties.titleBarForeground ?: colors.onAccent
                        Row(
                            Modifier.fillMaxWidth()
                                .heightIn(min = properties.titleBarHeight)
                                .background(titleBarBackground)
                                .padding(properties.titleBarPadding),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            MillenniumStyledContent(properties.titleStyle, titleBarForeground) { Text(title) }
                            Spacer(Modifier.weight(1f))
                            UnstyledButton(
                                onClick = onDismissRequest,
                                role = Role.Button,
                                indication = null,
                                contentPadding = PaddingValues(0.dp),
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(44.dp)
                                    .semantics { contentDescription = "Close dialog" },
                            ) {
                                Box(
                                    Modifier.size(28.dp).millenniumSurface(
                                        background = org.spotube.millennium.theme.gelBrush(
                                            colors.danger,
                                            colors.danger.copy(alpha = 0.78f),
                                        ),
                                        shape = RoundedCornerShape(5.dp),
                                        borderColor = colors.onAccent.copy(alpha = 0.62f),
                                        borderWidth = 1.dp,
                                        innerGlowColor = colors.highlight.copy(alpha = 0.45f),
                                    ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text("×", color = colors.surface, fontSize = 21.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                        Box(Modifier.fillMaxWidth().height(1.dp).background(
                            properties.titleBarBorderColor ?: colors.accentBottom,
                        ))
                        Column(
                            Modifier.padding(properties.contentPadding),
                            verticalArrangement = Arrangement.spacedBy(properties.contentSpacing),
                        ) {
                            content()
                        }
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
