package org.spotube.millennium.composables

import androidx.compose.animation.core.Animatable
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
    content: @Composable BoxScope.() -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.pullToRefreshTheme.properties
    val density = LocalDensity.current
    val threshold = with(density) { properties.threshold.toPx() }
    val maxPull = with(density) { properties.maxPull.toPx() }
    val pullOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val currentOnRefresh by rememberUpdatedState(onRefresh)
    val currentRefreshing by rememberUpdatedState(isRefreshing)

    val connection = remember(enabled, threshold, maxPull, properties.pullResistance) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                if (!enabled || currentRefreshing) return Offset.Zero
                if (source == NestedScrollSource.UserInput && available.y > 0f) {
                    val next = (pullOffset.value + available.y * properties.pullResistance).coerceAtMost(maxPull)
                    scope.launch { pullOffset.snapTo(next) }
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (pullOffset.value >= threshold) currentOnRefresh()
                scope.launch { pullOffset.animateTo(0f) }
                return Velocity.Zero
            }
        }
    }

    Box(modifier = modifier.nestedScroll(connection)) {
        content()
        val showIndicator = isRefreshing || pullOffset.value > 0f
        if (showIndicator) {
            Box(
                Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = with(density) { pullOffset.value.toDp() } + if (isRefreshing) properties.indicatorTopPadding else 0.dp)
                    .padding(top = properties.indicatorTopPadding)
                    .millenniumSurface(
                        background = properties.indicatorBackground ?: millenniumPanelBrush(colors),
                        shape = properties.indicatorShape,
                        borderColor = properties.indicatorBorderColor ?: colors.border,
                        borderWidth = properties.indicatorBorderWidth,
                        shadowElevation = properties.indicatorShadowElevation,
                        innerGlowColor = properties.indicatorInnerGlowColor ?: colors.highlight.copy(alpha = 0.4f),
                    )
                    .padding(properties.indicatorPadding),
            ) {
                MillenniumIndeterminateProgress(Modifier.width(properties.indicatorWidth))
            }
        }
    }
}
