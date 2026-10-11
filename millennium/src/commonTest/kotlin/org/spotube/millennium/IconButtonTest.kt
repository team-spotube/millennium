package org.spotube.millennium

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.dp
import org.spotube.millennium.theme.MillenniumThemeConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class IconButtonTest {
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
