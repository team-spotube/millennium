package org.spotube.millennium

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import org.spotube.millennium.composables.defaultMillenniumButtonTheme
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumDarkColors
import org.spotube.millennium.theme.MillenniumLightColors
import org.spotube.millennium.theme.MillenniumThemeConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ThemeTest {
    @Test
    fun bodyAndMutedTextMeetAAInBothThemes() {
        listOf(MillenniumLightColors, MillenniumDarkColors).forEach { palette ->
            listOf(palette.background, palette.surface, palette.raised, palette.inset).forEach { surface ->
                listOf(palette.text, palette.muted).forEach { foreground ->
                    assertTrue(contrast(foreground, surface) >= 4.5, "Text must reach 4.5:1 on every neutral surface")
                }
            }
        }
    }

    @Test
    fun gelButtonLabelsMeetAAAcrossTheEntireGradient() {
        listOf(MillenniumLightColors, MillenniumDarkColors).forEach { palette ->
            (0..20).forEach { step ->
                assertTrue(
                    contrast(palette.onAccent, lerp(palette.accentTop, palette.accentBottom, step / 20f)) >= 4.5,
                    "Gel labels must remain legible even at the brightest gradient stop",
                )
            }
        }
    }

    @Test
    fun semanticAccentAndErrorTextMeetAAOnPanels() {
        listOf(MillenniumLightColors, MillenniumDarkColors).forEach { palette ->
            listOf(palette.accent, palette.danger, palette.success).forEach { foreground ->
                assertTrue(contrast(foreground, palette.surface) >= 4.5, "Semantic text must reach 4.5:1")
            }
        }
    }

    @Test
    fun dangerButtonTextMeetsAAInBothPalettes() {
        listOf(MillenniumLightColors, MillenniumDarkColors).forEach { palette ->
            val labelColor = defaultMillenniumButtonTheme(palette).danger.default.foreground
            assertTrue(
                contrast(labelColor, palette.danger) >= 4.5,
                "Danger button labels use the component surface color over the danger fill",
            )
        }
    }

    @Test
    fun controlStatePropertyResolvesEveryStateAndCopiesIndependently() {
        val property = MillenniumControlStateProperty(
            default = "default",
            hovered = "hovered",
            focused = "focused",
            pressed = "pressed",
            disabled = "disabled",
            selected = "selected",
            error = "error",
        )
        assertEquals("default", property.resolve(MillenniumControlState.Default))
        assertEquals("hovered", property.resolve(MillenniumControlState.Hovered))
        assertEquals("focused", property.resolve(MillenniumControlState.Focused))
        assertEquals("pressed", property.resolve(MillenniumControlState.Pressed))
        assertEquals("disabled", property.resolve(MillenniumControlState.Disabled))
        assertEquals("selected", property.resolve(MillenniumControlState.Selected))
        assertEquals("error", property.resolve(MillenniumControlState.Error))
        assertEquals("default", property.copy(hovered = "custom hover").default)
    }

    @Test
    fun componentThemesCanBeCustomizedWithoutChangingNeighborsOrDarkDefaults() {
        val defaults = MillenniumThemeConfig()
        val light = defaults.light
        val customButton = light.buttonTheme.copy(
            primary = light.buttonTheme.primary.copy(
                default = light.buttonTheme.primary.default.copy(shadowElevation = 12.dp),
            ),
        )
        val customCheckbox = light.checkboxTheme.copy(
            states = light.checkboxTheme.states.copy(
                disabled = light.checkboxTheme.states.disabled.copy(faceAlpha = 0.25f),
            ),
        )
        val customCard = light.cardTheme.copy(
            states = light.cardTheme.states.copy(
                default = light.cardTheme.states.default.copy(maxWidth = 520.dp),
            ),
        )
        val config = defaults.copy(
            light = light.copy(
                buttonTheme = customButton,
                checkboxTheme = customCheckbox,
                cardTheme = customCard,
            ),
        )

        assertEquals(12.dp, config.light.buttonTheme.primary.default.shadowElevation)
        assertEquals(0.25f, config.light.checkboxTheme.states.disabled.faceAlpha)
        assertEquals(520.dp, config.light.cardTheme.states.default.maxWidth)
        assertNotEquals(0.25f, config.light.triStateCheckboxTheme.states.disabled.faceAlpha)
        assertNotEquals(12.dp, config.dark.buttonTheme.primary.default.shadowElevation)
    }

    @Test
    fun lightAndDarkDefaultsAreSeparateComponentThemeSets() {
        val config = MillenniumThemeConfig()
        assertEquals(MillenniumLightColors, config.light.colors)
        assertEquals(MillenniumDarkColors, config.dark.colors)
        assertNotEquals(
            config.light.buttonTheme.primary.default.colors.accentTop,
            config.dark.buttonTheme.primary.default.colors.accentTop,
        )
    }

    private fun contrast(a: Color, b: Color): Float {
        val light = maxOf(a.luminance(), b.luminance())
        val dark = minOf(a.luminance(), b.luminance())
        return (light + 0.05f) / (dark + 0.05f)
    }
}
