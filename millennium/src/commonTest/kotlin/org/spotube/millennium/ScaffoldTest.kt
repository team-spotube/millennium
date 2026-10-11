package org.spotube.millennium

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.spotube.millennium.composables.MillenniumScaffoldState
import org.spotube.millennium.composables.millenniumScaffoldInnerPadding
import org.spotube.millennium.theme.MillenniumThemeConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class ScaffoldTest {
    @Test
    fun innerPaddingCombinesSafeAreaBarsAndReservedSpace() {
        val insets = PaddingValues(start = 10.dp, top = 20.dp, end = 30.dp, bottom = 40.dp)
        val reserved = PaddingValues(5.dp)
        val withBar = millenniumScaffoldInnerPadding(
            insets = insets,
            topBarHeight = 56.dp,
            bottomBarHeight = 72.dp,
            extraPadding = reserved,
            layoutDirection = LayoutDirection.Ltr,
        )
        assertEquals(15.dp, withBar.calculateStartPadding(LayoutDirection.Ltr))
        assertEquals(61.dp, withBar.calculateTopPadding(), "Top uses the taller of the bar and the inset")
        assertEquals(35.dp, withBar.calculateEndPadding(LayoutDirection.Ltr))
        assertEquals(77.dp, withBar.calculateBottomPadding(), "Bottom uses the taller of the bar and the inset")

        val withoutBottomBar = millenniumScaffoldInnerPadding(
            insets = insets,
            topBarHeight = 56.dp,
            bottomBarHeight = null,
            extraPadding = reserved,
            layoutDirection = LayoutDirection.Ltr,
        )
        assertEquals(45.dp, withoutBottomBar.calculateBottomPadding(), "Without a bottom bar the safe inset still applies")
    }

    @Test
    fun stateSumsAndReleasesReservedPadding() {
        val state = MillenniumScaffoldState()
        assertEquals(0.dp, state.padding.calculateBottomPadding())

        val first = Any()
        val second = Any()
        state.register(first, PaddingValues(bottom = 60.dp, start = 4.dp))
        state.register(second, PaddingValues(bottom = 20.dp, start = 6.dp))
        assertEquals(80.dp, state.padding.calculateBottomPadding())
        assertEquals(10.dp, state.padding.calculateStartPadding(LayoutDirection.Ltr))

        state.unregister(first)
        assertEquals(20.dp, state.padding.calculateBottomPadding())
        assertEquals(6.dp, state.padding.calculateStartPadding(LayoutDirection.Ltr))

        state.unregister(second)
        assertEquals(0.dp, state.padding.calculateBottomPadding())
    }

    @Test
    fun scaffoldThemeCustomizesWithoutChangingNeighborsOrDarkDefaults() {
        val defaults = MillenniumThemeConfig()
        val light = defaults.light
        assertTrue(light.scaffoldTheme.properties.topBarMinimumHeight >= 44.dp)
        val custom = light.scaffoldTheme.copy(
            properties = light.scaffoldTheme.properties.copy(topBarMinimumHeight = 72.dp),
        )
        val config = defaults.copy(light = light.copy(scaffoldTheme = custom))

        assertEquals(72.dp, config.light.scaffoldTheme.properties.topBarMinimumHeight)
        assertNotEquals(72.dp, config.dark.scaffoldTheme.properties.topBarMinimumHeight)
        assertEquals(defaults.light.listTileTheme, config.light.listTileTheme)
        assertEquals(defaults.light.buttonTheme, config.light.buttonTheme)
    }
}
