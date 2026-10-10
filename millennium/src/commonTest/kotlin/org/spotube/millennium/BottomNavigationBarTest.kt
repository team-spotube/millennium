package org.spotube.millennium

import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import org.spotube.millennium.composables.bottomNavigationItemForeground
import org.spotube.millennium.theme.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BottomNavigationBarTest {
    @Test
    fun selectedLabelsMeetContrastAcrossBothGelPalettes() {
        listOf(MillenniumLightColors, MillenniumDarkColors).forEach { colors ->
            listOf(true, false).forEach { enabled ->
                val foreground = bottomNavigationItemForeground(colors, selected = true, enabled = enabled)
                assertEquals(colors.onAccent, foreground)
                (0..20).forEach { step ->
                    val background = lerp(colors.accentTop, colors.accentBottom, step / 20f)
                    val a = foreground.luminance()
                    val b = background.luminance()
                    assertTrue((maxOf(a, b) + 0.05f) / (minOf(a, b) + 0.05f) >= 4.5f)
                }
            }
            assertEquals(colors.muted, bottomNavigationItemForeground(colors, selected = false, enabled = false))
            assertEquals(colors.text, bottomNavigationItemForeground(colors, selected = false, enabled = true))
        }
    }

    @Test
    fun stateThemesKeepMinimumTargetsAndCustomizeIndependently() {
        val defaults = MillenniumThemeConfig()
        val theme = defaults.light.bottomNavigationBarTheme
        MillenniumControlState.entries.forEach { state ->
            val item = theme.item.resolve(state)
            assertTrue(item.minimumWidth >= 44.dp)
            assertTrue(item.minimumHeight >= 44.dp)
            assertEquals(null, item.background)
            assertEquals(null, item.foreground)
        }
        assertTrue(theme.item.focused.borderWidth > theme.item.default.borderWidth)
        val custom = defaults.copy(light = defaults.light.copy(
            bottomNavigationBarTheme = theme.copy(item = theme.item.copy(
                selected = theme.item.selected.copy(shadowElevation = 7.dp),
            )),
        ))
        assertEquals(7.dp, custom.light.bottomNavigationBarTheme.item.selected.shadowElevation)
        assertEquals(defaults.dark, custom.dark)
        assertEquals(defaults.light.sidebarTheme, custom.light.sidebarTheme)
        assertEquals(theme.states, custom.light.bottomNavigationBarTheme.states)
    }
}
