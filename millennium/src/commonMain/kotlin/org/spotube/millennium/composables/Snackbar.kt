package org.spotube.millennium.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.millenniumPanelBrush
import org.spotube.millennium.theme.millenniumSurface

/** How long a snackbar stays on screen. */
enum class MillenniumSnackbarDuration(val millis: Long) {
    Short(4_000),
    Long(10_000),
    Indefinite(0),
}

/** The outcome of a shown snackbar. */
enum class MillenniumSnackbarResult { Dismissed, ActionPerformed }

/** Complete appearance configuration for [MillenniumSnackbarHost]. */
@Immutable
data class MillenniumSnackbarThemeProperties(
    val background: Brush? = null,
    val foreground: Color? = null,
    val actionForeground: Color? = null,
    val shape: Shape,
    val margin: PaddingValues,
    val contentPadding: PaddingValues,
    val borderColor: Color? = null,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val innerGlowColor: Color? = null,
    val maxWidth: Dp,
    val actionSpacing: Dp,
    val animationMillis: Int,
)

@Immutable
data class MillenniumSnackbarTheme(val properties: MillenniumSnackbarThemeProperties)

fun defaultMillenniumSnackbarTheme(): MillenniumSnackbarTheme = MillenniumSnackbarTheme(
    MillenniumSnackbarThemeProperties(
        shape = RoundedCornerShape(12.dp),
        margin = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        borderWidth = 1.dp,
        shadowElevation = 10.dp,
        maxWidth = 600.dp,
        actionSpacing = 12.dp,
        animationMillis = 200,
    ),
)

/** A single queued snackbar. */
@Stable
class MillenniumSnackbarData internal constructor(
    val message: String,
    val actionLabel: String?,
    val duration: MillenniumSnackbarDuration,
    internal val continuation: CancellableContinuation<MillenniumSnackbarResult>,
) {
    fun dismiss() {
        if (continuation.isActive) continuation.resumeWith(Result.success(MillenniumSnackbarResult.Dismissed))
    }

    fun performAction() {
        if (continuation.isActive) continuation.resumeWith(Result.success(MillenniumSnackbarResult.ActionPerformed))
    }
}

/**
 * State holder for [MillenniumSnackbarHost]. Snackbars are shown one at a time; the
 * suspend call resolves when the snackbar is dismissed, times out, or its action runs.
 */
@Stable
class MillenniumSnackbarHostState {
    private val mutex = Mutex()
    private var snackbarData by mutableStateOf<MillenniumSnackbarData?>(null)

    val currentSnackbarData: MillenniumSnackbarData? get() = snackbarData

    suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: MillenniumSnackbarDuration = MillenniumSnackbarDuration.Short,
    ): MillenniumSnackbarResult = mutex.withLock {
        try {
            return suspendCancellableCoroutine { continuation ->
                snackbarData = MillenniumSnackbarData(message, actionLabel, duration, continuation)
                continuation.invokeOnCancellation { snackbarData = null }
            }
        } finally {
            snackbarData = null
        }
    }

    /** Dismisses the current snackbar, if any. */
    fun dismiss() {
        snackbarData?.dismiss()
    }
}

/**
 * Renders the active snackbar from [hostState] as a pearl/smoke glass pill with a
 * hairline, a shared shadow and a quiet accent action. Place it in the scaffold's
 * snackbar slot or align it inside a Box; it renders nothing when there is no snackbar
 * to show. Customize through [MillenniumTheme.snackbarTheme].
 */
@Composable
fun MillenniumSnackbarHost(
    hostState: MillenniumSnackbarHostState,
    modifier: Modifier = Modifier,
    background: Brush? = null,
    foreground: Color? = null,
    actionForeground: Color? = null,
    shape: Shape? = null,
    margin: PaddingValues? = null,
    contentPadding: PaddingValues? = null,
    borderColor: Color? = null,
    borderWidth: Dp? = null,
    shadowElevation: Dp? = null,
    innerGlowColor: Color? = null,
    maxWidth: Dp? = null,
    actionSpacing: Dp? = null,
    animationMillis: Int? = null,
    theme: MillenniumSnackbarTheme = MillenniumTheme.snackbarTheme,
) {
    val data = hostState.currentSnackbarData
    var lastData by remember { mutableStateOf<MillenniumSnackbarData?>(null) }
    if (data != null) lastData = data
    val visible = data != null
    val properties = theme.properties
    val resolvedMillis = animationMillis ?: properties.animationMillis
    val closeMillis = (resolvedMillis * 0.75f).toInt().coerceAtLeast(120)
    if (data != null) {
        LaunchedEffect(data) {
            if (data.duration != MillenniumSnackbarDuration.Indefinite) {
                delay(data.duration.millis)
                data.dismiss()
            }
        }
    }
    val colors = MillenniumTheme.colors
    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(resolvedMillis, easing = FastOutSlowInEasing)) +
                slideInVertically(
                    animationSpec = spring(
                        dampingRatio = 0.72f,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                    initialOffsetY = { it / 2 },
                ) +
                scaleIn(
                    initialScale = 0.92f,
                    transformOrigin = TransformOrigin(0.5f, 1f),
                    animationSpec = spring(
                        dampingRatio = 0.72f,
                        stiffness = Spring.StiffnessMediumLow,
                    ),
                ),
            exit = fadeOut(tween(closeMillis, easing = FastOutLinearInEasing)) +
                slideOutVertically(
                    animationSpec = tween(closeMillis, easing = FastOutLinearInEasing),
                    targetOffsetY = { it / 2 },
                ) +
                scaleOut(
                    targetScale = 0.95f,
                    transformOrigin = TransformOrigin(0.5f, 1f),
                    animationSpec = tween(closeMillis, easing = FastOutLinearInEasing),
                ),
        ) {
            val showing = lastData ?: return@AnimatedVisibility
            Row(
                modifier = Modifier
                    .widthIn(max = maxWidth ?: properties.maxWidth)
                    .padding(margin ?: properties.margin)
                    .millenniumSurface(
                        background = background ?: properties.background ?: millenniumPanelBrush(colors),
                        shape = shape ?: properties.shape,
                        borderColor = borderColor ?: properties.borderColor ?: colors.border,
                        borderWidth = borderWidth ?: properties.borderWidth,
                        shadowElevation = shadowElevation ?: properties.shadowElevation,
                        innerGlowColor = innerGlowColor ?: properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.4f),
                    )
                    .padding(contentPadding ?: properties.contentPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(actionSpacing ?: properties.actionSpacing),
            ) {
                Box(Modifier.weight(1f)) {
                    MillenniumText(showing.message, color = foreground ?: properties.foreground ?: colors.text)
                }
                if (showing.actionLabel != null) {
                    MillenniumButton(
                        onClick = { showing.performAction() },
                        style = MillenniumButtonStyle.Quiet,
                    ) {
                        MillenniumText(
                            showing.actionLabel,
                            color = actionForeground ?: properties.actionForeground ?: colors.accent,
                        )
                    }
                }
            }
        }
    }
}
