package org.spotube.millennium

import androidx.compose.ui.graphics.luminance
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.spotube.millennium.composables.millenniumTextLineHeight
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
}
