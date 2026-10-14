package org.spotube.millennium

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import org.spotube.millennium.theme.millenniumActionPointer
import kotlin.test.Test
import kotlin.test.assertEquals

class PointerTest {
    @Test
    fun enabledActionsUseTheHandCursor() {
        assertEquals(Modifier.pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true), Modifier.millenniumActionPointer())
    }

    @Test
    fun disabledActionsUseTheDefaultCursor() {
        assertEquals(Modifier.pointerHoverIcon(PointerIcon.Default, overrideDescendants = true), Modifier.millenniumActionPointer(enabled = false))
    }

    @Test
    fun actionCursorOverridesUnstyledArrowAndChildContentAcrossTheHitArea() {
        assertEquals(
            Modifier.pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true),
            Modifier.millenniumActionPointer(),
        )
    }
}
