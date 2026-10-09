package org.spotube.millennium

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import org.spotube.millennium.theme.MillenniumDarkColors
import org.spotube.millennium.theme.MillenniumLightColors
import kotlin.test.Test
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
                assertTrue(contrast(palette.onAccent, lerp(palette.accentTop, palette.accentBottom, step / 20f)) >= 4.5,
                    "Gel labels must remain legible even at the brightest gradient stop")
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

    private fun contrast(a: Color, b: Color): Float {
        val light = maxOf(a.luminance(), b.luminance())
        val dark = minOf(a.luminance(), b.luminance())
        return (light + 0.05f) / (dark + 0.05f)
    }
}
