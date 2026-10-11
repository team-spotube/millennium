package org.spotube.millennium

import androidx.compose.ui.input.InputMode
import org.spotube.millennium.theme.millenniumIsKeyboardFocusRing
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FocusTest {
    @Test
    fun focusRingIsReservedForKeyboardInput() {
        assertTrue(millenniumIsKeyboardFocusRing(focused = true, inputMode = InputMode.Keyboard))
        assertFalse(
            millenniumIsKeyboardFocusRing(focused = true, inputMode = InputMode.Touch),
            "Pointer/touch focus must not show a focus ring",
        )
        assertFalse(millenniumIsKeyboardFocusRing(focused = false, inputMode = InputMode.Keyboard))
        assertFalse(millenniumIsKeyboardFocusRing(focused = false, inputMode = InputMode.Touch))
    }
}
