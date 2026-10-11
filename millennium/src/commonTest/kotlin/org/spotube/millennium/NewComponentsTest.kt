package org.spotube.millennium

import androidx.compose.ui.unit.dp
import org.spotube.millennium.composables.MillenniumSegmentedButtonDefaults
import org.spotube.millennium.theme.MillenniumThemeConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class NewComponentsTest {
    @Test
    fun newComponentsExposeAccessibleThemeDefaults() {
        val light = MillenniumThemeConfig().light
        assertTrue(light.topAppBarTheme.properties.minimumHeight >= 44.dp)
        assertTrue(light.segmentedButtonTheme.states.default.minimumHeight >= 44.dp)
        assertTrue(light.snackbarTheme.properties.maxWidth > 0.dp)
        assertTrue(light.pullToRefreshTheme.properties.threshold < light.pullToRefreshTheme.properties.maxPull)
    }

    @Test
    fun newComponentThemesCustomizeIndependentlyOfNeighborsAndDarkDefaults() {
        val defaults = MillenniumThemeConfig()
        val light = defaults.light
        val config = defaults.copy(
            light = light.copy(
                topAppBarTheme = light.topAppBarTheme.copy(
                    properties = light.topAppBarTheme.properties.copy(minimumHeight = 64.dp),
                ),
                segmentedButtonTheme = light.segmentedButtonTheme.copy(
                    states = light.segmentedButtonTheme.states.copy(
                        default = light.segmentedButtonTheme.states.default.copy(minimumHeight = 48.dp),
                    ),
                ),
                snackbarTheme = light.snackbarTheme.copy(
                    properties = light.snackbarTheme.properties.copy(maxWidth = 480.dp),
                ),
                pullToRefreshTheme = light.pullToRefreshTheme.copy(
                    properties = light.pullToRefreshTheme.properties.copy(threshold = 80.dp),
                ),
            ),
        )

        assertEquals(64.dp, config.light.topAppBarTheme.properties.minimumHeight)
        assertEquals(48.dp, config.light.segmentedButtonTheme.states.default.minimumHeight)
        assertEquals(480.dp, config.light.snackbarTheme.properties.maxWidth)
        assertEquals(80.dp, config.light.pullToRefreshTheme.properties.threshold)

        assertNotEquals(64.dp, config.dark.topAppBarTheme.properties.minimumHeight)
        assertNotEquals(48.dp, config.dark.segmentedButtonTheme.states.default.minimumHeight)
        assertEquals(defaults.light.iconButtonTheme, config.light.iconButtonTheme)
        assertEquals(defaults.light.scaffoldTheme, config.light.scaffoldTheme)
        assertEquals(defaults.light.listTileTheme, config.light.listTileTheme)
    }

    @Test
    fun segmentedItemShapeRoundsOnlyTheOuterEdges() {
        assertNotEquals(
            MillenniumSegmentedButtonDefaults.itemShape(0, 3),
            MillenniumSegmentedButtonDefaults.itemShape(1, 3),
        )
        assertNotEquals(
            MillenniumSegmentedButtonDefaults.itemShape(2, 3),
            MillenniumSegmentedButtonDefaults.itemShape(1, 3),
        )
    }
}
