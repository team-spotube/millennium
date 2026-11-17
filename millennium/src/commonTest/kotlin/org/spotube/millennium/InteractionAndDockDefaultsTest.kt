package org.spotube.millennium

import androidx.compose.ui.unit.dp
import org.spotube.millennium.composables.MillenniumBottomNavigationBarThemeProperties
import org.spotube.millennium.theme.MillenniumInteractionStyle
import org.spotube.millennium.theme.MillenniumSurfaceStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InteractionAndDockDefaultsTest {
    @Test
    fun interactionLightingIsOnByDefault() {
        val style = MillenniumInteractionStyle()
        assertTrue(style.enabled)
        assertNull(style.hoverBrush)
        assertNull(style.pressBrush)
    }

    @Test
    fun dockDefaultsKeepTheDockedLook() {
        val bar = MillenniumBottomNavigationBarThemeProperties()
        assertEquals(androidx.compose.foundation.layout.PaddingValues(0.dp), bar.margin)
        assertTrue(bar.insetsInsideSurface)
    }

    @Test
    fun surfaceEffectsAreOnByDefault() {
        val style = MillenniumSurfaceStyle()
        assertTrue(style.shadows)
        assertTrue(style.glow)
    }
}
