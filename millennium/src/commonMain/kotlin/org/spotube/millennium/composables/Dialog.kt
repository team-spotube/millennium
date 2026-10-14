package org.spotube.millennium.composables

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
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
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
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
    val exitScale: Float = 0.94f,
    val animationMillis: Int,
    val closeAnimationMillis: Int = 150,
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
        enterScale = 0.92f,
        animationMillis = 220,
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
    panelBackground: Brush? = null,
    shape: Shape? = null,
    foreground: Color? = null,
    titleStyle: TextStyle? = null,
    panelBorderColor: Color? = null,
    panelBorderWidth: Dp? = null,
    panelShadowElevation: Dp? = null,
    panelGlowColor: Color? = null,
    panelMaxWidth: Dp? = null,
    overlayPadding: Dp? = null,
    contentPadding: Dp? = null,
    contentSpacing: Dp? = null,
    scrimColor: Color? = null,
    enterScale: Float? = null,
    exitScale: Float? = null,
    animationMillis: Int? = null,
    closeAnimationMillis: Int? = null,
    alpha: Float? = null,
    titleBarBackground: Brush? = null,
    titleBarForeground: Color? = null,
    titleBarHeight: Dp? = null,
    titleBarPadding: PaddingValues? = null,
    titleBarBorderColor: Color? = null,
    theme: MillenniumDialogTheme = MillenniumTheme.dialogTheme,
    content: @Composable ColumnScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = theme.states.resolve(MillenniumControlState.Default)
    val resolvedForeground = foreground ?: properties.foreground ?: colors.text
    val resolvedPanelBorderColor = panelBorderColor ?: properties.panelBorderColor ?: colors.border
    val resolvedPanelBackground = panelBackground ?: properties.panelBackground ?: millenniumPanelBrush(colors)
    val resolvedPanelGlowColor = panelGlowColor ?: properties.panelGlowColor ?: colors.highlight.copy(alpha = 0.6f)
    val resolvedScrimColor = scrimColor ?: properties.scrimColor ?: colors.scrim
    val resolvedAnimationMillis = animationMillis ?: properties.animationMillis
    val resolvedCloseMillis = closeAnimationMillis ?: properties.closeAnimationMillis
    val closeInteraction = remember { MutableInteractionSource() }
    val closePressed by closeInteraction.collectIsPressedAsState()
    val closeHovered by closeInteraction.collectIsHoveredAsState()
    val closeFocused by closeInteraction.collectIsKeyboardFocusedAsState()
    val closeScale by animateFloatAsState(
        targetValue = when {
            closePressed -> 0.90f
            closeHovered || closeFocused -> 1.08f
            else -> 1f
        },
        animationSpec = spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessMedium),
    )
    val closeElevation by animateDpAsState(
        targetValue = when {
            closePressed -> 0.dp
            closeHovered || closeFocused -> 3.dp
            else -> 1.dp
        },
        animationSpec = tween(120),
    )
    UnstyledDialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        overlay = {
            Scrim(
                scrimColor = resolvedScrimColor,
                enter = fadeIn(animationSpec = tween(resolvedAnimationMillis, easing = FastOutSlowInEasing)),
                exit = fadeOut(animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing)),
            )
        },
    ) {
        Box(
            Modifier.fillMaxSize().padding(overlayPadding ?: properties.overlayPadding),
            contentAlignment = androidx.compose.ui.Alignment.Center,
        ) {
            DialogPanel(
                modifier.widthIn(max = panelMaxWidth ?: properties.panelMaxWidth).fillMaxWidth()
                    .alpha(alpha ?: properties.alpha)
                    .millenniumSurface(
                        background = resolvedPanelBackground,
                        shape = shape ?: properties.shape,
                        borderColor = resolvedPanelBorderColor,
                        borderWidth = panelBorderWidth ?: properties.panelBorderWidth,
                        shadowElevation = panelShadowElevation ?: properties.panelShadowElevation,
                        innerGlowColor = resolvedPanelGlowColor,
                    ),
                paneTitle = title,
                enter = fadeIn(animationSpec = tween(resolvedAnimationMillis, easing = FastOutSlowInEasing)) +
                    scaleIn(
                        initialScale = enterScale ?: properties.enterScale,
                        animationSpec = spring(
                            dampingRatio = 0.72f,
                            stiffness = Spring.StiffnessMediumLow,
                        ),
                    ) +
                    slideInVertically(
                        animationSpec = spring(
                            dampingRatio = 0.76f,
                            stiffness = Spring.StiffnessMediumLow,
                        ),
                        initialOffsetY = { it / 10 },
                    ),
                exit = fadeOut(animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing)) +
                    scaleOut(
                        targetScale = exitScale ?: properties.exitScale,
                        animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing),
                    ) +
                    slideOutVertically(
                        animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing),
                        targetOffsetY = { it / 14 },
                    ),
            ) {
                MillenniumStyledContent(
                    TextStyle(color = resolvedForeground),
                    resolvedForeground,
                ) {
                    Column {
                        val resolvedTitleBarBackground = titleBarBackground ?: properties.titleBarBackground
                        val resolvedTitleBarForeground = titleBarForeground
                            ?: properties.titleBarForeground ?: resolvedForeground
                        Row(
                            Modifier.fillMaxWidth()
                                .heightIn(min = titleBarHeight ?: properties.titleBarHeight)
                                .then(
                                    if (resolvedTitleBarBackground != null) Modifier.background(resolvedTitleBarBackground)
                                    else Modifier
                                )
                                .padding(titleBarPadding ?: properties.titleBarPadding),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            MillenniumStyledContent(titleStyle ?: properties.titleStyle, resolvedTitleBarForeground) {
                                Text(title)
                            }
                            Spacer(Modifier.weight(1f))
                            UnstyledButton(
                                onClick = onDismissRequest,
                                role = Role.Button,
                                indication = null,
                                interactionSource = closeInteraction,
                                contentPadding = PaddingValues(0.dp),
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(44.dp)
                                    .semantics { contentDescription = "Close dialog" },
                            ) {
                                Box(
                                    Modifier.size(28.dp)
                                        .graphicsLayer { scaleX = closeScale; scaleY = closeScale }
                                        .millenniumSurface(
                                            background = org.spotube.millennium.theme.gelBrush(
                                                colors.danger,
                                                colors.danger.copy(alpha = 0.78f),
                                            ),
                                            shape = RoundedCornerShape(5.dp),
                                            borderColor = if (closeFocused) colors.accent
                                            else colors.onAccent.copy(alpha = if (closeHovered) 0.85f else 0.62f),
                                            borderWidth = if (closeFocused) 2.dp else 1.dp,
                                            shadowElevation = closeElevation,
                                            innerGlowColor = colors.highlight.copy(alpha = if (closeHovered) 0.65f else 0.45f),
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text("×", color = colors.surface, fontSize = 21.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                        Box(Modifier.fillMaxWidth().height(1.dp).background(
                            titleBarBorderColor ?: properties.titleBarBorderColor ?: colors.border,
                        ))
                        Column(
                            Modifier.padding(contentPadding ?: properties.contentPadding),
                            verticalArrangement = Arrangement.spacedBy(contentSpacing ?: properties.contentSpacing),
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
