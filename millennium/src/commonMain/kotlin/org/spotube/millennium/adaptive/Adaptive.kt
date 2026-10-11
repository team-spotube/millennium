package org.spotube.millennium.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.LocalWindowInfo

/**
 * Window width buckets, mirroring the Material 3 adaptive size classes without pulling
 * the Material component library into Millennium.
 */
enum class MillenniumWindowWidthSizeClass { Compact, Medium, Expanded }

/** Window height buckets, mirroring the Material 3 adaptive size classes. */
enum class MillenniumWindowHeightSizeClass { Compact, Medium, Expanded }

/** The window's minimum dimensions and their size buckets. */
@Immutable
data class MillenniumWindowSizeClass(
    val minWidthDp: Int,
    val minHeightDp: Int,
    val widthSizeClass: MillenniumWindowWidthSizeClass,
    val heightSizeClass: MillenniumWindowHeightSizeClass,
)

/** Adaptive information resolved for the current window. */
@Immutable
data class MillenniumWindowAdaptiveInfo(
    val windowSizeClass: MillenniumWindowSizeClass,
)

/**
 * Resolves the current window's adaptive size information from the container size.
 *
 * This is the Millennium replacement for `androidx.compose.material3.adaptive.currentWindowAdaptiveInfo`,
 * so applications can keep the same adaptive-dispatch pattern after dropping Material.
 */
@Composable
fun currentWindowAdaptiveInfo(): MillenniumWindowAdaptiveInfo {
    val size = LocalWindowInfo.current.containerDpSize
    val widthDp = size.width.value.toInt()
    val heightDp = size.height.value.toInt()
    return MillenniumWindowAdaptiveInfo(
        windowSizeClass = MillenniumWindowSizeClass(
            minWidthDp = widthDp,
            minHeightDp = heightDp,
            widthSizeClass = when {
                widthDp < 600 -> MillenniumWindowWidthSizeClass.Compact
                widthDp < 840 -> MillenniumWindowWidthSizeClass.Medium
                else -> MillenniumWindowWidthSizeClass.Expanded
            },
            heightSizeClass = when {
                heightDp < 480 -> MillenniumWindowHeightSizeClass.Compact
                heightDp < 900 -> MillenniumWindowHeightSizeClass.Medium
                else -> MillenniumWindowHeightSizeClass.Expanded
            },
        ),
    )
}
