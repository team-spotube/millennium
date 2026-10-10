package org.spotube.millennium

import androidx.compose.ui.graphics.luminance
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.spotube.millennium.composables.millenniumTextLineHeight
import org.spotube.millennium.composables.nextAutocompleteIndex
import org.spotube.millennium.composables.autocompleteMenuShouldShow
import org.spotube.millennium.theme.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VisualRefinementTest {
    @Test
    fun sculptedGelReflectionKeepsAAContrastAtEveryStop() {
        listOf(MillenniumLightColors, MillenniumDarkColors).forEach { colors ->
            val stops = millenniumGelStops(colors.accentTop, colors.accentBottom)
            assertTrue(stops[2].second.luminance() > stops[3].second.luminance(), "Gel must have a visible reflection break")
            assertTrue(stops.last().second.luminance() > stops[4].second.luminance(), "Gel must have a luminous lower lip")
            stops.forEach { (_, background) ->
                val a = colors.onAccent.luminance()
                val b = background.luminance()
                assertTrue((maxOf(a, b) + 0.05f) / (minOf(a, b) + 0.05f) >= 4.5f)
            }
        }
    }

    @Test
    fun explicitTextSizesScaleConfiguredLeading() {
        assertEquals(21.sp, millenniumTextLineHeight(14.sp, 14.sp, 21.sp))
        assertEquals(60.sp, millenniumTextLineHeight(40.sp, 14.sp, 21.sp))
        assertEquals(18.sp, millenniumTextLineHeight(12.sp, 14.sp, 21.sp))
        assertEquals(48.sp, millenniumTextLineHeight(32.sp, 16.sp, 24.sp))
    }

    @Test
    fun neutralBevelStopsRetainTextContrastInBothAppearances() {
        listOf(MillenniumLightColors, MillenniumDarkColors).forEach { colors ->
            listOf(true, false).forEach { sunken ->
                millenniumNeutralStops(colors, sunken).forEach { background ->
                    listOf(colors.text, colors.muted).forEach { foreground ->
                        val a = foreground.luminance()
                        val b = background.luminance()
                        assertTrue((maxOf(a, b) + 0.05f) / (minOf(a, b) + 0.05f) >= 4.5f)
                    }
                }
            }
        }
    }

    @Test
    fun primaryButtonsUseRoundedRectanglesAndMenusShareFullRowWidth() {
        val theme = MillenniumThemeConfig().light
        assertEquals(RoundedCornerShape(10.dp), theme.buttonTheme.primary.default.shape)
        assertEquals(RoundedCornerShape(8.dp), theme.buttonTheme.danger.default.shape)
        assertEquals(192.dp, theme.dropdownMenuTheme.states.default.itemWidth)
        assertTrue(theme.dialogTheme.states.default.titleBarHeight >= 44.dp)
        assertEquals(1f, theme.radioGroupTheme.states.hovered.alpha)
        assertEquals(1f, theme.radioGroupTheme.states.pressed.alpha)
    }

    @Test
    fun autocompleteHighlightNavigationClampsAndHandlesEmptyResults() {
        assertEquals(0, nextAutocompleteIndex(-1, 4, forward = true))
        assertEquals(3, nextAutocompleteIndex(-1, 4, forward = false))
        assertEquals(2, nextAutocompleteIndex(1, 4, forward = true))
        assertEquals(1, nextAutocompleteIndex(2, 4, forward = false))
        assertEquals(3, nextAutocompleteIndex(3, 4, forward = true))
        assertEquals(0, nextAutocompleteIndex(0, 4, forward = false))
        assertEquals(-1, nextAutocompleteIndex(-1, 0, forward = true))
    }

    @Test
    fun autocompletePopupTracksFocusAndEscapeDismissesOnlyCurrentQuery() {
        assertTrue(autocompleteMenuShouldShow(true, true, "air", true, dismissedQuery = null))
        assertTrue(!autocompleteMenuShouldShow(true, false, "air", true, dismissedQuery = null))
        assertTrue(!autocompleteMenuShouldShow(false, true, "air", true, dismissedQuery = null))
        assertTrue(!autocompleteMenuShouldShow(true, true, "air", true, dismissedQuery = "air"))
        assertTrue(autocompleteMenuShouldShow(true, true, "air m", true, dismissedQuery = "air"))
        assertTrue(!autocompleteMenuShouldShow(true, true, "air", false, dismissedQuery = null))
    }

    @Test
    fun autocompleteMenuThemeIsCustomizablePerAppearance() {
        val defaults = MillenniumThemeConfig()
        val lightAutocomplete = defaults.light.autocompleteTheme
        assertTrue(lightAutocomplete.states.default.itemMinimumHeight >= 44.dp)
        assertEquals(320.dp, lightAutocomplete.states.default.menuMaxHeight)
        val custom = defaults.copy(
            light = defaults.light.copy(
                autocompleteTheme = lightAutocomplete.copy(
                    states = lightAutocomplete.states.copy(
                        default = lightAutocomplete.states.default.copy(
                            minimumMenuWidth = 300.dp,
                            menuMaxHeight = 420.dp,
                        ),
                    ),
                ),
            ),
        )
        assertEquals(300.dp, custom.light.autocompleteTheme.states.default.minimumMenuWidth)
        assertEquals(420.dp, custom.light.autocompleteTheme.states.default.menuMaxHeight)
        assertEquals(defaults.dark.autocompleteTheme, custom.dark.autocompleteTheme)
        assertEquals(defaults.light.buttonTheme, custom.light.buttonTheme)
    }

    @Test
    fun autocompleteMenuAndHoverTransitionsUseDistinctDurations() {
        val properties = MillenniumThemeConfig().light.autocompleteTheme.states.default
        assertTrue(properties.openAnimationMillis >= 150)
        assertTrue(properties.closeAnimationMillis in 100..properties.openAnimationMillis)
        assertTrue(properties.itemStateAnimationMillis >= 100)
        assertTrue(properties.itemMinimumHeight >= 44.dp)
    }
}
