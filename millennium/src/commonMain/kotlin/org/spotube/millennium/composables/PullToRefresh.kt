package org.spotube.millennium.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.millenniumPanelBrush
import org.spotube.millennium.theme.millenniumSurface

/** Complete appearance and gesture configuration for [MillenniumPullToRefreshBox]. */
@Immutable
data class MillenniumPullToRefreshThemeProperties(
    val threshold: Dp,
    val maxPull: Dp,
    val pullResistance: Float,
    val indicatorShape: Shape,
    val indicatorBackground: Brush? = null,
    val indicatorBorderColor: Color? = null,
    val indicatorBorderWidth: Dp,
    val indicatorShadowElevation: Dp,
    val indicatorInnerGlowColor: Color? = null,
    val indicatorPadding: PaddingValues,
    val indicatorWidth: Dp,
    val indicatorTopPadding: Dp,
)

@Immutable
data class MillenniumPullToRefreshTheme(val properties: MillenniumPullToRefreshThemeProperties)

fun defaultMillenniumPullToRefreshTheme(): MillenniumPullToRefreshTheme = MillenniumPullToRefreshTheme(
    MillenniumPullToRefreshThemeProperties(
        threshold = 64.dp,
        maxPull = 120.dp,
        pullResistance = 0.5f,
        indicatorShape = RoundedCornerShape(50),
        indicatorBorderWidth = 1.dp,
        indicatorShadowElevation = 8.dp,
        indicatorPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        indicatorWidth = 96.dp,
        indicatorTopPadding = 10.dp,
    ),
)

/**
 * A themed pull-to-refresh container. Dragging the content down past a threshold while it
 * is scrolled to the top calls [onRefresh]; a pearl/smoke glass indicator pod is shown
 * while pulling and while [isRefreshing]. Customize through
 * [MillenniumTheme.pullToRefreshTheme].
 */
@Composable
fun MillenniumPullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    threshold: Dp? = null,
    maxPull: Dp? = null,
    pullResistance: Float? = null,
    indicatorShape: Shape? = null,
    indicatorBackground: Brush? = null,
    indicatorBorderColor: Color? = null,
    indicatorBorderWidth: Dp? = null,
    indicatorShadowElevation: Dp? = null,
    indicatorInnerGlowColor: Color? = null,
    indicatorPadding: PaddingValues? = null,
    indicatorWidth: Dp? = null,
    indicatorTopPadding: Dp? = null,
    theme: MillenniumPullToRefreshTheme = MillenniumTheme.pullToRefreshTheme,
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = theme.properties
    val density = LocalDensity.current
    val thresholdPx = with(density) { (threshold ?: properties.threshold).toPx() }
    val maxPullPx = with(density) { (maxPull ?: properties.maxPull).toPx() }
    val resolvedResistance = pullResistance ?: properties.pullResistance
    val resolvedTopPadding = indicatorTopPadding ?: properties.indicatorTopPadding
    val pullOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val currentOnRefresh by rememberUpdatedState(onRefresh)
    val currentRefreshing by rememberUpdatedState(isRefreshing)

    val connection = remember(enabled, thresholdPx, maxPullPx, resolvedResistance) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                if (!enabled || currentRefreshing) return Offset.Zero
                if (source == NestedScrollSource.UserInput && available.y > 0f) {
                    val next = (pullOffset.value + available.y * resolvedResistance).coerceAtMost(maxPullPx)
                    scope.launch { pullOffset.snapTo(next) }
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (pullOffset.value >= thresholdPx) currentOnRefresh()
                scope.launch {
                    pullOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(dampingRatio = 0.72f, stiffness = Spring.StiffnessMediumLow),
                    )
                }
                return Velocity.Zero
            }
        }
    }

    Box(modifier = modifier.nestedScroll(connection)) {
        content()
        val showIndicator = isRefreshing || pullOffset.value > 0f
        AnimatedVisibility(
            visible = showIndicator,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = with(density) { pullOffset.value.toDp() } + if (isRefreshing) resolvedTopPadding else 0.dp)
                .padding(top = resolvedTopPadding),
            enter = fadeIn(tween(150, easing = FastOutSlowInEasing)) +
                scaleIn(
                    initialScale = 0.85f,
                    animationSpec = spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessMedium),
                ),
            exit = fadeOut(tween(130, easing = FastOutLinearInEasing)) +
                scaleOut(
                    targetScale = 0.85f,
                    animationSpec = tween(130, easing = FastOutLinearInEasing),
                ),
        ) {
            Box(
                Modifier
                    .millenniumSurface(
                        background = indicatorBackground ?: properties.indicatorBackground ?: millenniumPanelBrush(colors),
                        shape = indicatorShape ?: properties.indicatorShape,
                        borderColor = indicatorBorderColor ?: properties.indicatorBorderColor ?: colors.border,
                        borderWidth = indicatorBorderWidth ?: properties.indicatorBorderWidth,
                        shadowElevation = indicatorShadowElevation ?: properties.indicatorShadowElevation,
                        innerGlowColor = indicatorInnerGlowColor ?: properties.indicatorInnerGlowColor
                            ?: colors.highlight.copy(alpha = 0.4f),
                    )
                    .padding(indicatorPadding ?: properties.indicatorPadding),
            ) {
                MillenniumIndeterminateProgress(Modifier.width(indicatorWidth ?: properties.indicatorWidth))
            }
        }
    }
}
