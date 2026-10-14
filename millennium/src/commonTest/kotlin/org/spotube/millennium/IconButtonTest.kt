package org.spotube.millennium

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import org.spotube.millennium.composables.millenniumIconOrbStops
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumColorSchemes
import org.spotube.millennium.theme.MillenniumPalette
import org.spotube.millennium.theme.MillenniumThemeConfig
import org.spotube.millennium.theme.resolve
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class IconButtonTest {
    @Test
    fun filledOrbsUseAHairlineRimAndKeepKeyboardFocusDistinct() {
        val theme = MillenniumThemeConfig().light.iconButtonTheme
        listOf(theme.primary, theme.danger).forEach { variant ->
            assertEquals(1.dp, variant.default.borderWidth)
            assertTrue(variant.focused.borderWidth > variant.default.borderWidth)
            assertTrue(variant.pressed.orbStrength < variant.default.orbStrength)
        }
    }

    @Test
    fun orbBodyKeepsIconsLegibleAcrossAccentFamilies() {
        MillenniumPalette.entries.forEach { palette ->
            listOf(false, true).forEach { dark ->
                val colors = MillenniumColorSchemes().resolve(palette, dark)
                val stops = millenniumIconOrbStops(colors.accentTop, colors.accentBottom)
                assertEquals(listOf(0f, 0.28f, 0.60f, 0.82f, 1f), stops.map { it.first })
                assertEquals(stops[1].second, stops[2].second)
                stops.filter { it.first in 0.28f..0.60f }.forEach { (_, body) ->
                    assertTrue((1.05f) / (body.luminance() + 0.05f) >= 4.5f, "Every orb uses a white icon over deep jewel glass")
                }
                assertTrue(stops[1].second.luminance() < colors.accentBottom.luminance() * 0.4f)
                val dangerBody = millenniumIconOrbStops(colors.danger, lerp(colors.danger, Color.Black, 0.20f))[1].second
                assertTrue(1.05f / (dangerBody.luminance() + 0.05f) >= 4.5f)
            }
        }
    }

    @Test
    fun onlyFilledVariantsCarryTheOrbSheen() {
        val theme = MillenniumThemeConfig().light.iconButtonTheme
        assertTrue(theme.primary.default.orb)
        assertTrue(theme.danger.default.orb)
        assertFalse(theme.secondary.default.orb)
        assertFalse(theme.quiet.default.orb)
        assertTrue(theme.primary.default.orbStrength > 0f)
        assertEquals(CircleShape, theme.primary.default.shape)
        assertEquals(CircleShape, theme.danger.default.shape)
    }

    @Test
    fun everyIconButtonVariantKeepsAnAccessibleTarget() {
        val theme = MillenniumThemeConfig().light.iconButtonTheme
        listOf(theme.primary, theme.secondary, theme.quiet, theme.danger).forEach { variant ->
            assertTrue(variant.default.size.width >= 44.dp)
            assertTrue(variant.default.size.height >= 44.dp)
            assertTrue(variant.default.iconSize < variant.default.size.width)
        }
    }

    @Test
    fun focusedIconButtonsAddABorderAndDisabledLowersAlpha() {
        val theme = MillenniumThemeConfig().light.iconButtonTheme
        assertTrue(theme.secondary.focused.borderWidth > theme.secondary.default.borderWidth)
        assertTrue(theme.quiet.focused.borderWidth > theme.quiet.default.borderWidth)
        assertTrue(theme.primary.disabled.alpha < theme.primary.default.alpha)
        assertTrue(theme.primary.focused.borderWidth >= theme.primary.default.borderWidth)
    }

    @Test
    fun iconButtonThemeCustomizesWithoutChangingNeighborsOrDarkDefaults() {
        val defaults = MillenniumThemeConfig()
        val light = defaults.light
        val custom = light.iconButtonTheme.copy(
            primary = light.iconButtonTheme.primary.copy(
                default = light.iconButtonTheme.primary.default.copy(orbStrength = 0.8f),
            ),
        )
        val config = defaults.copy(light = light.copy(iconButtonTheme = custom))

        assertEquals(0.8f, config.light.iconButtonTheme.primary.default.orbStrength)
        assertNotEquals(0.8f, config.dark.iconButtonTheme.primary.default.orbStrength)
        assertEquals(defaults.light.buttonTheme, config.light.buttonTheme)
        assertEquals(defaults.light.listTileTheme, config.light.listTileTheme)
        assertEquals(defaults.light.scaffoldTheme, config.light.scaffoldTheme)
    }
}
