package org.spotube.millennium.theme

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.platform.LocalInputModeManager

/** Whether a focus ring should be shown: focus is real and the last input was the keyboard. */
fun millenniumIsKeyboardFocusRing(focused: Boolean, inputMode: InputMode): Boolean =
    focused && inputMode == InputMode.Keyboard

/**
 * Collects focus that is eligible for a focus ring. Compose focuses a control on a
 * pointer press (mouse click, touch); that focus is intentionally reported as
 * `false` here so focus rings stay reserved for keyboard and accessibility
 * navigation. Focus gained from Tab / directional keys reports `true`.
 */
@Composable
fun MutableInteractionSource.collectIsKeyboardFocusedAsState(): State<Boolean> {
    val focused = collectIsFocusedAsState()
    val inputModeManager = LocalInputModeManager.current
    return remember(this, inputModeManager) {
        derivedStateOf { focused.value && inputModeManager.inputMode == InputMode.Keyboard }
    }
}
