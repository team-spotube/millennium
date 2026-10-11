package org.spotube.millennium.adaptive

import androidx.compose.ui.unit.dp
import com.composeunstyled.HeightBreakpoint
import com.composeunstyled.WidthBreakpoint
import com.composeunstyled.WindowHeightBreakpoints
import com.composeunstyled.WindowWidthBreakpoints

object WindowBreakpoints {
    val Compact = WidthBreakpoint("compact")
    val Medium = WidthBreakpoint("medium")
    val Expanded = WidthBreakpoint("expanded")
    val Short = HeightBreakpoint("short")
    val Tall = HeightBreakpoint("tall")
}

internal val widthBreakpoints = WindowWidthBreakpoints {
    WindowBreakpoints.Compact startsAt 0.dp
    WindowBreakpoints.Medium startsAt 600.dp
    WindowBreakpoints.Expanded startsAt 840.dp
}


internal val heightBreakpoints = WindowHeightBreakpoints {
    WindowBreakpoints.Short startsAt 0.dp
    WindowBreakpoints.Tall startsAt 720.dp
}
