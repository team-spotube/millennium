package org.spotube.millennium

import androidx.compose.ui.graphics.luminance
import org.spotube.millennium.theme.MillenniumColorSchemes
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumPalette
import org.spotube.millennium.theme.MillenniumThemeConfig
import org.spotube.millennium.theme.millenniumInteractionLevels
import org.spotube.millennium.theme.millenniumQuietEnamelStops
import org.spotube.millennium.theme.resolve
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class InteractionTest {
    @Test
    fun hoverAndPressHaveDistinctBevelLightingAndIdleDisabledAreUnchanged() {
        assertEquals(1f to 0f, millenniumInteractionLevels(MillenniumControlState.Hovered))
        assertEquals(0f to 1f, millenniumInteractionLevels(MillenniumControlState.Pressed))
        assertEquals(1f to 0f, millenniumInteractionLevels(MillenniumControlState.Focused))
        listOf(MillenniumControlState.Default, MillenniumControlState.Disabled, MillenniumControlState.Selected).forEach {
            assertEquals(0f to 0f, millenniumInteractionLevels(it))
        }
    }

    @Test
    fun quietHoverIsBeveledEnamelAndPressReversesTheLightingAcrossPalettes() {
        MillenniumPalette.entries.forEach { palette ->
            listOf(false, true).forEach { dark ->
                val colors = MillenniumColorSchemes().resolve(palette, dark)
                val hover = millenniumQuietEnamelStops(colors, pressed = false)
                val press = millenniumQuietEnamelStops(colors, pressed = true)
                assertNotEquals(hover, press)
                assertNotEquals(hover[0].second, hover[2].second)
                assertEquals(listOf(0f, 0.46f, 0.49f, 1f), hover.map { it.first })
                assertEquals(listOf(0f, 0.35f, 1f), press.map { it.first })
                (hover + press).forEach { (_, fill) ->
                    assertEquals(1f, fill.alpha)
                    val lighter = maxOf(colors.accent.luminance(), fill.luminance())
                    val darker = minOf(colors.accent.luminance(), fill.luminance())
                    assertTrue((lighter + 0.05f) / (darker + 0.05f) >= 3f, "Quiet icons stay legible on enamel")
                }
            }
        }
    }

    @Test
    fun buttonsAndQuietIconButtonsHaveRaisedHoverAndRecessedPressStates() {
        val config = MillenniumThemeConfig()
        listOf(config.light, config.dark).forEach { theme ->
            listOf(theme.buttonTheme.primary, theme.buttonTheme.secondary, theme.buttonTheme.quiet, theme.buttonTheme.danger).forEach {
                assertTrue(it.hovered.shadowElevation > it.default.shadowElevation)
                assertEquals(0f, it.pressed.shadowElevation.value)
            }
            assertTrue(theme.iconButtonTheme.quiet.hovered.borderWidth > theme.iconButtonTheme.quiet.default.borderWidth)
            assertTrue(theme.iconButtonTheme.quiet.hovered.shadowElevation > theme.iconButtonTheme.quiet.default.shadowElevation)
        }
    }
}
