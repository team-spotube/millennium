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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.collectIsKeyboardFocusedAsState
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumActionPointer
import org.spotube.millennium.theme.millenniumInteractionSurface
import org.spotube.millennium.theme.millenniumPanelBrush
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
    val exitScale: Float = 0.94f,
    val animationMillis: Int,
    val closeAnimationMillis: Int = 150,
    val alpha: Float = 1f,
    val titleBarBackground: Brush? = null,
    val titleBarForeground: Color? = null,
    val titleBarHeight: Dp = 46.dp,
    val titleBarPadding: PaddingValues = PaddingValues(horizontal = 14.dp),
    val titleBarBorderColor: Color? = null,
    val descriptionStyle: TextStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal),
    val footerBackground: Brush? = null,
    val footerPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
    val footerSpacing: Dp = 10.dp,
    val footerBorderColor: Color? = null,
    val closeTargetSize: Dp = 44.dp,
    val closeCapSize: Dp = 28.dp,
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

// ---------------------------------------------------------------------------
// Root
// ---------------------------------------------------------------------------

/** Read by dialog bricks (e.g. [MillenniumDialogClose]) to dismiss the enclosing dialog. */
@Stable
class MillenniumDialogScope internal constructor(val dismiss: () -> Unit)

val LocalMillenniumDialog = staticCompositionLocalOf<MillenniumDialogScope?> { null }

/**
 * A themed, caller-owned modal dialog. Compose the panel from lego bricks:
 *
 * ```
 * MillenniumDialog(visible, onDismissRequest) {
 *     MillenniumDialogHeader {              // fixed
 *         MillenniumDialogTitle { Text("Share playlist") }
 *         MillenniumDialogClose()
 *     }
 *     MillenniumDialogBody {                // scrolls when tall
 *         MillenniumDialogDescription { Text("Anyone with the link can listen.") }
 *         MillenniumTextField(...)
 *     }
 *     MillenniumDialogFooter {              // fixed, end-aligned actions
 *         MillenniumButton(onDismissRequest, style = Secondary) { Text("Cancel") }
 *         MillenniumButton(onShare) { Text("Share") }
 *     }
 * }
 * ```
 *
 * Every brick is optional, and any composable may sit between them.
 */
@Composable
fun MillenniumDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    paneTitle: String = "Dialog",
    panelBackground: Brush? = null,
    shape: Shape? = null,
    foreground: Color? = null,
    panelBorderColor: Color? = null,
    panelBorderWidth: Dp? = null,
    panelShadowElevation: Dp? = null,
    panelGlowColor: Color? = null,
    panelMaxWidth: Dp? = null,
    overlayPadding: Dp? = null,
    scrimColor: Color? = null,
    enterScale: Float? = null,
    exitScale: Float? = null,
    animationMillis: Int? = null,
    closeAnimationMillis: Int? = null,
    alpha: Float? = null,
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
    val scope = remember(onDismissRequest) { MillenniumDialogScope(onDismissRequest) }
    UnstyledDialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        overlay = {
            Scrim(
                modifier = Modifier.millenniumActionPointer(),
                scrimColor = resolvedScrimColor,
                enter = fadeIn(animationSpec = tween(resolvedAnimationMillis, easing = FastOutSlowInEasing)),
                exit = fadeOut(animationSpec = tween(resolvedCloseMillis, easing = FastOutLinearInEasing)),
            )
        },
    ) {
        Box(
            Modifier.fillMaxSize().padding(overlayPadding ?: properties.overlayPadding),
            contentAlignment = Alignment.Center,
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
                paneTitle = paneTitle,
                enter = fadeIn(animationSpec = tween(resolvedAnimationMillis, easing = FastOutSlowInEasing)) +
                    scaleIn(
                        initialScale = enterScale ?: properties.enterScale,
                        animationSpec = spring(dampingRatio = 0.72f, stiffness = Spring.StiffnessMediumLow),
                    ) +
                    slideInVertically(
                        animationSpec = spring(dampingRatio = 0.76f, stiffness = Spring.StiffnessMediumLow),
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
                CompositionLocalProvider(LocalMillenniumDialog provides scope) {
                    MillenniumStyledContent(TextStyle(color = resolvedForeground), resolvedForeground) {
                        Column { content() }
                    }
                }
            }
        }
    }
}

/**
 * Convenience dialog with a title bar, close cap and scrolling body, built from the
 * same bricks as the composable overload.
 */
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
    MillenniumDialog(
        visible = visible,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        paneTitle = title,
        panelBackground = panelBackground,
        shape = shape,
        foreground = foreground,
        panelBorderColor = panelBorderColor,
        panelBorderWidth = panelBorderWidth,
        panelShadowElevation = panelShadowElevation,
        panelGlowColor = panelGlowColor,
        panelMaxWidth = panelMaxWidth,
        overlayPadding = overlayPadding,
        scrimColor = scrimColor,
        enterScale = enterScale,
        exitScale = exitScale,
        animationMillis = animationMillis,
        closeAnimationMillis = closeAnimationMillis,
        alpha = alpha,
        theme = theme,
    ) {
        MillenniumDialogHeader(
            background = titleBarBackground,
            foreground = titleBarForeground,
            minimumHeight = titleBarHeight,
            contentPadding = titleBarPadding,
            dividerColor = titleBarBorderColor,
            theme = theme,
        ) {
            MillenniumDialogTitle(textStyle = titleStyle, theme = theme) { Text(title) }
            MillenniumDialogClose(theme = theme)
        }
        MillenniumDialogBody(
            contentPadding = contentPadding?.let { PaddingValues(it) },
            contentSpacing = contentSpacing,
            theme = theme,
            content = content,
        )
    }
}

/**
 * A ready-made confirmation dialog: optional [icon], [title], [description], an
 * optional custom [content] body and caller-supplied [actions] in the footer.
 * Every part is a slot.
 */
@Composable
fun MillenniumAlertDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    description: (@Composable () -> Unit)? = null,
    paneTitle: String = "Alert",
    showClose: Boolean = true,
    theme: MillenniumDialogTheme = MillenniumTheme.dialogTheme,
    content: (@Composable ColumnScope.() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit,
) {
    MillenniumDialog(visible, onDismissRequest, modifier, paneTitle = paneTitle, theme = theme) {
        MillenniumDialogHeader(theme = theme) {
            if (icon != null) icon()
            MillenniumDialogTitle(theme = theme, content = title)
            if (showClose) MillenniumDialogClose(theme = theme)
        }
        if (description != null || content != null) {
            MillenniumDialogBody(theme = theme) {
                if (description != null) MillenniumDialogDescription(theme = theme, content = description)
                content?.invoke(this)
            }
        }
        MillenniumDialogFooter(theme = theme, content = actions)
    }
}

// ---------------------------------------------------------------------------
// Header, title, close
// ---------------------------------------------------------------------------

/** Fixed title bar. Place [MillenniumDialogTitle], icons and a [MillenniumDialogClose] inside. */
@Composable
fun MillenniumDialogHeader(
    modifier: Modifier = Modifier,
    background: Brush? = null,
    foreground: Color? = null,
    minimumHeight: Dp? = null,
    contentPadding: PaddingValues? = null,
    contentSpacing: Dp = 12.dp,
    showDivider: Boolean = true,
    dividerColor: Color? = null,
    theme: MillenniumDialogTheme = MillenniumTheme.dialogTheme,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = theme.states.default
    val resolvedBackground = background ?: properties.titleBarBackground
    val resolvedForeground = foreground ?: properties.titleBarForeground ?: properties.foreground ?: colors.text
    Column(modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth()
                .heightIn(min = minimumHeight ?: properties.titleBarHeight)
                .then(if (resolvedBackground != null) Modifier.background(resolvedBackground) else Modifier)
                .padding(contentPadding ?: properties.titleBarPadding),
            horizontalArrangement = Arrangement.spacedBy(contentSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MillenniumStyledContent(TextStyle.Default, resolvedForeground) { content() }
        }
        if (showDivider) {
            Box(
                Modifier.fillMaxWidth().height(1.dp)
                    .background(dividerColor ?: properties.titleBarBorderColor ?: colors.border),
            )
        }
    }
}

/** The title; takes the remaining header width so trailing items sit at the end. */
@Composable
fun RowScope.MillenniumDialogTitle(
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null,
    foreground: Color? = null,
    theme: MillenniumDialogTheme = MillenniumTheme.dialogTheme,
    content: @Composable () -> Unit,
) {
    val properties = theme.states.default
    Box(modifier.weight(1f)) {
        MillenniumStyledContent(
            textStyle ?: properties.titleStyle,
            foreground ?: properties.titleBarForeground ?: properties.foreground ?: MillenniumTheme.colors.text,
            content,
        )
    }
}

@Composable
fun RowScope.MillenniumDialogTitle(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null,
    foreground: Color? = null,
    theme: MillenniumDialogTheme = MillenniumTheme.dialogTheme,
) {
    MillenniumDialogTitle(modifier, textStyle, foreground, theme) { Text(text) }
}

/**
 * A 44dp close target. By default it dismisses the enclosing dialog and draws the red
 * glass cap; pass [content] to draw anything else. The content gets the spring
 * press/hover scale and receives the resolved control state.
 */
@Composable
fun MillenniumDialogClose(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentDescription: String = "Close dialog",
    targetSize: Dp? = null,
    theme: MillenniumDialogTheme = MillenniumTheme.dialogTheme,
    content: @Composable (MillenniumControlState) -> Unit = { MillenniumDialogCloseCap(it, theme = theme) },
) {
    val dismiss = LocalMillenniumDialog.current?.dismiss
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val focused by interaction.collectIsKeyboardFocusedAsState()
    val state = when {
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        else -> MillenniumControlState.Default
    }
    val scale by animateFloatAsState(
        targetValue = when {
            pressed -> 0.90f
            hovered || focused -> 1.08f
            else -> 1f
        },
        animationSpec = spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessMedium),
    )
    UnstyledButton(
        onClick = { (onClick ?: dismiss)?.invoke() },
        role = Role.Button,
        indication = null,
        interactionSource = interaction,
        contentPadding = PaddingValues(0.dp),
        contentAlignment = Alignment.Center,
        modifier = modifier.millenniumActionPointer()
            .size(targetSize ?: theme.states.default.closeTargetSize)
            .semantics { this.contentDescription = contentDescription },
    ) {
        Box(Modifier.graphicsLayer { scaleX = scale; scaleY = scale }) { content(state) }
    }
}

/** The default red glass close cap; reusable inside a custom [MillenniumDialogClose]. */
@Composable
fun MillenniumDialogCloseCap(
    state: MillenniumControlState = MillenniumControlState.Default,
    modifier: Modifier = Modifier,
    size: Dp? = null,
    theme: MillenniumDialogTheme = MillenniumTheme.dialogTheme,
    content: @Composable () -> Unit = {
        Text("×", color = MillenniumTheme.colors.surface, fontSize = 21.sp, fontWeight = FontWeight.Medium)
    },
) {
    val colors = MillenniumTheme.colors
    val hovered = state == MillenniumControlState.Hovered
    val focused = state == MillenniumControlState.Focused
    val pressed = state == MillenniumControlState.Pressed
    val elevation by animateDpAsState(
        targetValue = when {
            pressed -> 0.dp
            hovered || focused -> 3.dp
            else -> 1.dp
        },
        animationSpec = tween(120),
    )
    val capShape = RoundedCornerShape(5.dp)
    Box(
        modifier.size(size ?: theme.states.default.closeCapSize)
            .millenniumSurface(
                background = gelBrush(colors.danger, colors.danger.copy(alpha = 0.78f)),
                shape = capShape,
                borderColor = if (focused) colors.accent
                else colors.onAccent.copy(alpha = if (hovered) 0.85f else 0.62f),
                borderWidth = if (focused) 2.dp else 1.dp,
                shadowElevation = elevation,
                innerGlowColor = colors.highlight.copy(alpha = if (hovered) 0.65f else 0.45f),
            )
            .millenniumInteractionSurface(state, capShape, tint = colors.danger),
        contentAlignment = Alignment.Center,
    ) { content() }
}

// ---------------------------------------------------------------------------
// Body, description, footer
// ---------------------------------------------------------------------------

/**
 * The main region. Scrolls when the dialog is taller than the screen, so the header
 * and footer stay put. Must be placed directly inside [MillenniumDialog].
 */
@Composable
fun ColumnScope.MillenniumDialogBody(
    modifier: Modifier = Modifier,
    scrollable: Boolean = true,
    contentPadding: PaddingValues? = null,
    contentSpacing: Dp? = null,
    theme: MillenniumDialogTheme = MillenniumTheme.dialogTheme,
    content: @Composable ColumnScope.() -> Unit,
) {
    val properties = theme.states.default
    Column(
        modifier.weight(1f, fill = false).fillMaxWidth()
            .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier)
            .padding(contentPadding ?: PaddingValues(properties.contentPadding)),
        verticalArrangement = Arrangement.spacedBy(contentSpacing ?: properties.contentSpacing),
    ) { content() }
}

/** Muted supporting text for a dialog body; any composable may go inside. */
@Composable
fun MillenniumDialogDescription(
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null,
    foreground: Color? = null,
    theme: MillenniumDialogTheme = MillenniumTheme.dialogTheme,
    content: @Composable () -> Unit,
) {
    Box(modifier) {
        MillenniumStyledContent(
            textStyle ?: theme.states.default.descriptionStyle,
            foreground ?: MillenniumTheme.colors.muted,
            content,
        )
    }
}

@Composable
fun MillenniumDialogDescription(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle? = null,
    foreground: Color? = null,
    theme: MillenniumDialogTheme = MillenniumTheme.dialogTheme,
) {
    MillenniumDialogDescription(modifier, textStyle, foreground, theme) { Text(text) }
}

/** Fixed action row, end-aligned by default. */
@Composable
fun MillenniumDialogFooter(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal? = null,
    background: Brush? = null,
    contentPadding: PaddingValues? = null,
    showDivider: Boolean = true,
    dividerColor: Color? = null,
    theme: MillenniumDialogTheme = MillenniumTheme.dialogTheme,
    content: @Composable RowScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = theme.states.default
    val resolvedBackground = background ?: properties.footerBackground
    Column(modifier.fillMaxWidth()) {
        if (showDivider) {
            Box(
                Modifier.fillMaxWidth().height(1.dp)
                    .background(dividerColor ?: properties.footerBorderColor ?: colors.border),
            )
        }
        Row(
            Modifier.fillMaxWidth()
                .then(if (resolvedBackground != null) Modifier.background(resolvedBackground) else Modifier)
                .padding(contentPadding ?: properties.footerPadding),
            horizontalArrangement = horizontalArrangement
                ?: Arrangement.spacedBy(properties.footerSpacing, Alignment.End),
            verticalAlignment = Alignment.CenterVertically,
        ) { content() }
    }
}

@Preview
@Composable
private fun MillenniumDialogPreview() = MillenniumPreview {
    MillenniumDialog(visible = true, onDismissRequest = {}) {
        MillenniumDialogHeader {
            MillenniumDialogTitle { Text("A little update") }
            MillenniumDialogClose()
        }
        MillenniumDialogBody {
            MillenniumDialogDescription("A familiar message panel, dressed for today.")
        }
        MillenniumDialogFooter {
            MillenniumButton(onClick = {}, style = MillenniumButtonStyle.Secondary) { Text("Later") }
            MillenniumButton(onClick = {}) { Text("Update") }
        }
    }
}
