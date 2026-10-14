package org.spotube.millennium.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon

/** Covers the complete action hit area, including Unstyled's inner arrow cursor and content. */
fun Modifier.millenniumActionPointer(enabled: Boolean = true): Modifier =
    pointerHoverIcon(if (enabled) PointerIcon.Hand else PointerIcon.Default, overrideDescendants = true)
