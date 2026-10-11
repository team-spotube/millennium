package org.spotube.millennium.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
        animationMillis = 160,
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
) {
    val data = hostState.currentSnackbarData
    var lastData by remember { mutableStateOf<MillenniumSnackbarData?>(null) }
    if (data != null) lastData = data
    val visible = data != null
    val properties = MillenniumTheme.snackbarTheme.properties
    val alpha by animateFloatAsState(if (visible) 1f else 0f, tween(properties.animationMillis))

    val showing = lastData
    if (showing != null && (visible || alpha > 0.01f)) {
        LaunchedEffect(showing) {
            if (showing.duration != MillenniumSnackbarDuration.Indefinite) {
                delay(showing.duration.millis)
                showing.dismiss()
            }
        }
        val colors = MillenniumTheme.colors
        Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier
                    .widthIn(max = properties.maxWidth)
                    .padding(properties.margin)
                    .alpha(alpha)
                    .millenniumSurface(
                        background = properties.background ?: millenniumPanelBrush(colors),
                        shape = properties.shape,
                        borderColor = properties.borderColor ?: colors.border,
                        borderWidth = properties.borderWidth,
                        shadowElevation = properties.shadowElevation,
                        innerGlowColor = properties.innerGlowColor ?: colors.highlight.copy(alpha = 0.4f),
                    )
                    .padding(properties.contentPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(properties.actionSpacing),
            ) {
                Box(Modifier.weight(1f)) {
                    MillenniumText(showing.message, color = properties.foreground ?: colors.text)
                }
                if (showing.actionLabel != null) {
                    MillenniumButton(
                        onClick = { showing.performAction() },
                        style = MillenniumButtonStyle.Quiet,
                    ) {
                        MillenniumText(showing.actionLabel, color = properties.actionForeground ?: colors.accent)
                    }
                }
            }
        }
    }
}
