package org.spotube.millennium

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import org.spotube.millennium.theme.MillenniumComponentTheme
import org.spotube.millennium.theme.MillenniumButtonTheme
import org.spotube.millennium.theme.MillenniumCardTheme
import org.spotube.millennium.theme.MillenniumCheckboxTheme
import org.spotube.millennium.theme.MillenniumDarkColors
import org.spotube.millennium.theme.MillenniumDimensions
import org.spotube.millennium.theme.MillenniumLightColors
import org.spotube.millennium.theme.MillenniumThemeConfig
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.resolve
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

    @Test
    fun componentStateStylesInheritDefaultsAndOverrideIndependently() {
        val base = MillenniumVisualStyle(
            background = SolidColor(Color.Black),
            foregroundColor = Color.White,
            shadowElevation = 2.dp,
        )
        val theme = MillenniumComponentTheme(
            default = MillenniumVisualStyle(foregroundColor = Color(0xFF224466), shadowElevation = 6.dp),
            hovered = MillenniumVisualStyle(backgroundColor = Color(0xFF112233), innerGlowColor = Color.White),
        )

        val hovered = theme.resolve(MillenniumVisualState.Hovered, base)
        assertTrue(hovered.background == null, "A color override replaces the inherited brush")
        assertTrue(hovered.backgroundColor == Color(0xFF112233))
        assertTrue(hovered.foregroundColor == Color(0xFF224466), "Hover inherits the component default foreground")
        assertTrue(hovered.shadowElevation == 6.dp, "Hover inherits the component default elevation")
        assertTrue(hovered.innerGlowColor == Color.White)
        assertTrue(theme.resolve(MillenniumVisualState.Disabled, base).background == SolidColor(Color.Black),
            "Unspecified states leave their base appearance unchanged")
    }

    @Test
    fun themeConfigKeepsComponentThemesIndependentlyHackable() {
        val customButtonTheme = MillenniumButtonTheme(
            primary = MillenniumComponentTheme(
                hovered = MillenniumVisualStyle(innerGlowColor = Color(0xFFFFCC55), shadowElevation = 12.dp),
            ),
        )
        val customCheckboxTheme = MillenniumCheckboxTheme(
            colors = MillenniumLightColors.copy(accent = Color(0xFFCC2255)),
            disabled = MillenniumVisualStyle(alpha = 0.25f),
        )
        val customCardTheme = MillenniumCardTheme(
            colors = MillenniumLightColors.copy(surface = Color(0xFFFFF3E8)),
            dimensions = MillenniumDimensions(cardMaxWidth = 520.dp),
        )
        val config = MillenniumThemeConfig(
            colorSchemes = org.spotube.millennium.theme.MillenniumColorSchemes(
                light = MillenniumLightColors.copy(accent = Color(0xFF8A3FFC)),
                dark = MillenniumDarkColors.copy(accent = Color(0xFFC6A0FF)),
            ),
            buttonTheme = customButtonTheme,
            checkboxTheme = customCheckboxTheme,
            cardTheme = customCardTheme,
        )

        assertTrue(config.colorSchemes.light.accent == Color(0xFF8A3FFC))
        assertTrue(config.buttonTheme.primary.hovered.innerGlowColor == Color(0xFFFFCC55))
        assertTrue(config.buttonTheme.primary.hovered.shadowElevation == 12.dp)
        assertTrue(config.checkboxTheme.disabled.alpha == 0.25f)
        assertTrue(config.checkboxTheme.colors?.accent == Color(0xFFCC2255))
        assertTrue(config.cardTheme.colors?.surface == Color(0xFFFFF3E8))
        assertTrue(config.cardTheme.dimensions.cardMaxWidth == 520.dp)
        assertTrue(config.radioGroupTheme.disabled.alpha != 0.25f, "Component themes remain independently configurable")
    }

    private fun contrast(a: Color, b: Color): Float {
        val light = maxOf(a.luminance(), b.luminance())
        val dark = minOf(a.luminance(), b.luminance())
        return (light + 0.05f) / (dark + 0.05f)
    }
}
