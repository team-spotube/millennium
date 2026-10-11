package org.spotube.millennium

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import org.spotube.millennium.composables.listTileBackground
import org.spotube.millennium.composables.listTileControlState
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumLightColors
import org.spotube.millennium.theme.MillenniumPalette
import org.spotube.millennium.theme.MillenniumThemeConfig
import org.spotube.millennium.theme.millenniumGelStops
import org.spotube.millennium.theme.millenniumSelectedGelStops
import org.spotube.millennium.theme.resolve
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ListTileTest {
    @Test
    fun listTileOnlyBecomesInteractiveWhenClickable() {
        assertEquals(
            MillenniumControlState.Disabled,
            listTileControlState(enabled = false, clickable = true, pressed = false, focused = false, hovered = false, selected = false),
        )
        assertEquals(
            MillenniumControlState.Pressed,
            listTileControlState(enabled = true, clickable = true, pressed = true, focused = false, hovered = false, selected = false),
        )
        assertEquals(
            MillenniumControlState.Focused,
            listTileControlState(enabled = true, clickable = true, pressed = false, focused = true, hovered = false, selected = false),
        )
        assertEquals(
            MillenniumControlState.Hovered,
            listTileControlState(enabled = true, clickable = true, pressed = false, focused = false, hovered = true, selected = false),
        )
        assertEquals(
            MillenniumControlState.Selected,
            listTileControlState(enabled = true, clickable = true, pressed = false, focused = false, hovered = false, selected = true),
        )
        assertEquals(
            MillenniumControlState.Default,
            listTileControlState(enabled = true, clickable = true, pressed = false, focused = false, hovered = false, selected = false),
        )
        // A tile without an onClick callback ignores pointer interaction flags.
        assertEquals(
            MillenniumControlState.Default,
            listTileControlState(enabled = true, clickable = false, pressed = true, focused = true, hovered = true, selected = false),
        )
        assertEquals(
            MillenniumControlState.Selected,
            listTileControlState(enabled = true, clickable = false, pressed = false, focused = false, hovered = false, selected = true),
        )
    }

    @Test
    fun selectedTileSurfaceIsAQuieterSurfaceBlendedGel() {
        val schemes = MillenniumThemeConfig().colorSchemes
        MillenniumPalette.entries.forEach { palette ->
            listOf(false, true).forEach { dark ->
                val colors = schemes.resolve(palette, dark)
                val full = millenniumGelStops(colors.accentTop, colors.accentBottom)
                val selected = millenniumSelectedGelStops(colors, 0.35f)
                assertEquals(full.map { it.first }, selected.map { it.first })
                val surfaceLuminance = colors.surface.luminance()
                full.zip(selected).forEach { (fullStop, selectedStop) ->
                    assertNotEquals(fullStop.second, selectedStop.second, "Selected gel must soften the full gel")
                    val fullDistance = abs(fullStop.second.luminance() - surfaceLuminance)
                    val selectedDistance = abs(selectedStop.second.luminance() - surfaceLuminance)
                    assertTrue(
                        selectedDistance <= fullDistance,
                        "$palette dark=$dark selected stop should sit closer to the surface than the full gel",
                    )
                }
            }
        }
    }

    @Test
    fun selectedTileKeepsTitleAndSubtitleLegibleAcrossPalettes() {
        val schemes = MillenniumThemeConfig().colorSchemes
        MillenniumPalette.entries.forEach { palette ->
            listOf(false, true).forEach { dark ->
                val colors = schemes.resolve(palette, dark)
                millenniumSelectedGelStops(colors, 0.35f).forEach { (_, surface) ->
                    assertTrue(contrast(colors.text, surface) >= 4.5, "$palette dark=$dark title on selected gel")
                    // The default selected subtitle is the text color at 82% alpha, composited over the fill.
                    val subtitle = lerp(surface, colors.text, 0.82f)
                    assertTrue(contrast(subtitle, surface) >= 4.5, "$palette dark=$dark subtitle on selected gel")
                }
            }
        }
    }

    @Test
    fun selectedAndHoverSurfacesResolveToDifferentBrushes() {
        val colors = MillenniumLightColors
        val selected: Brush = listTileBackground(colors, selected = true, state = MillenniumControlState.Default, selectedGelStrength = 0.35f)
        val selectedHover: Brush = listTileBackground(colors, selected = true, state = MillenniumControlState.Hovered, selectedGelStrength = 0.35f)
        val hover: Brush = listTileBackground(colors, selected = false, state = MillenniumControlState.Hovered, selectedGelStrength = 0.35f)
        val resting: Brush = listTileBackground(colors, selected = false, state = MillenniumControlState.Default, selectedGelStrength = 0.35f)
        assertNotEquals(selected, hover)
        assertNotEquals(selected, selectedHover)
        assertNotEquals(hover, resting)
    }

    @Test
    fun listTileThemeCustomizesWithoutChangingNeighborsOrDarkDefaults() {
        val defaults = MillenniumThemeConfig()
        val light = defaults.light
        assertTrue(light.listTileTheme.states.default.minimumHeight >= 44.dp)
        val custom = light.listTileTheme.copy(
            states = light.listTileTheme.states.copy(
                default = light.listTileTheme.states.default.copy(minimumHeight = 64.dp, selectedGelStrength = 0.5f),
            ),
        )
        val config = defaults.copy(light = light.copy(listTileTheme = custom))

        assertEquals(64.dp, config.light.listTileTheme.states.default.minimumHeight)
        assertEquals(0.5f, config.light.listTileTheme.states.default.selectedGelStrength)
        assertNotEquals(64.dp, config.dark.listTileTheme.states.default.minimumHeight)
        assertEquals(defaults.light.buttonTheme, config.light.buttonTheme)
        assertEquals(defaults.light.cardTheme, config.light.cardTheme)
    }

    @Test
    fun disabledListTileThemeLowersAlphaAndFocusedAddsBorder() {
        val theme = MillenniumThemeConfig().light.listTileTheme
        assertTrue(theme.states.disabled.alpha < theme.states.default.alpha)
        assertTrue(theme.states.focused.borderWidth > theme.states.default.borderWidth)
    }

    private fun contrast(foreground: Color, background: Color): Float {
        val a = foreground.luminance()
        val b = background.luminance()
        return (maxOf(a, b) + 0.05f) / (minOf(a, b) + 0.05f)
    }
}
