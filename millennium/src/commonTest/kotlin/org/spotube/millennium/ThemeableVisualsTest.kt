package org.spotube.millennium

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import org.spotube.millennium.composables.MillenniumTextRole
import org.spotube.millennium.composables.cardControlState
import org.spotube.millennium.composables.defaultMillenniumAutocompleteTheme
import org.spotube.millennium.composables.defaultMillenniumAvatarTheme
import org.spotube.millennium.composables.defaultMillenniumCardTheme
import org.spotube.millennium.composables.defaultMillenniumIconTheme
import org.spotube.millennium.composables.defaultMillenniumListTileTheme
import org.spotube.millennium.composables.defaultMillenniumLoadingTheme
import org.spotube.millennium.composables.defaultMillenniumProgressTheme
import org.spotube.millennium.composables.defaultMillenniumPullToRefreshTheme
import org.spotube.millennium.composables.defaultMillenniumScrollbarTheme
import org.spotube.millennium.composables.defaultMillenniumSeparatorTheme
import org.spotube.millennium.composables.defaultMillenniumSliderTheme
import org.spotube.millennium.composables.defaultMillenniumTextFieldTheme
import org.spotube.millennium.composables.defaultMillenniumTextTheme
import org.spotube.millennium.composables.defaultMillenniumTooltipTheme
import org.spotube.millennium.composables.progressControlState
import org.spotube.millennium.composables.resolveListTileBackground
import org.spotube.millennium.composables.styleFor
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumLightColors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

/** Resolve-level checks that the themeable visuals reach the properties each component reads. */
class ThemeableVisualsTest {
    private val red = SolidColor(Color.Red)
    private val blue = SolidColor(Color.Blue)

    @Test
    fun newVisualPropertiesDefaultToTheBuiltInLook() {
        val field = defaultMillenniumTextFieldTheme().states.default
        assertNull(field.interactionOverlay)
        assertNull(field.cursorBrush)
        assertEquals(0.dp, field.shadowElevation)
        assertEquals(1.dp, field.innerGlowWidth)

        val slider = defaultMillenniumSliderTheme().states.default
        assertNull(slider.trackInteractionOverlay)
        assertNull(slider.activeTrackBorderColor)
        assertEquals(0.dp, slider.activeTrackBorderWidth)

        val progress = defaultMillenniumProgressTheme().states.default
        assertNull(progress.trackBottomRimColor)
        assertEquals(0.dp, progress.progressBorderWidth)

        val loading = defaultMillenniumLoadingTheme().states.default
        assertNull(loading.fillBrush)
        assertNull(loading.glossBrush)
        assertEquals(1.dp, loading.trackBorderWidth)
        assertEquals(3.dp, (loading.segmentShape as androidx.compose.foundation.shape.RoundedCornerShape).topStart.toPx(
            androidx.compose.ui.geometry.Size(100f, 100f), androidx.compose.ui.unit.Density(1f),
        ).dp)

        assertNull(defaultMillenniumScrollbarTheme().states.default.thumbShape)
        assertNull(defaultMillenniumSeparatorTheme().states.default.lineBrush)
        assertNull(defaultMillenniumAvatarTheme().states.default.initialsBackground)
        assertNull(defaultMillenniumCardTheme().states.default.interactionOverlay)
        assertNull(defaultMillenniumListTileTheme().states.default.scale)
        assertNull(defaultMillenniumIconTheme().states.default.tintBrush)
        assertEquals(1f / 6f, defaultMillenniumTooltipTheme().states.default.exitSlideFraction)
        assertEquals(0.85f, defaultMillenniumPullToRefreshTheme().properties.indicatorEnterScale)
        val popup = defaultMillenniumAutocompleteTheme().states.default
        assertEquals(10.dp, popup.itemContentSpacing)
        assertEquals(1.008f, popup.itemHoverScale)
        assertEquals(1f, defaultMillenniumTextTheme().states.default.alpha)
    }

    @Test
    fun cardResolvesEveryStateItReallyHas() {
        assertEquals(MillenniumControlState.Default, cardControlState(true, false, false, false))
        assertEquals(MillenniumControlState.Hovered, cardControlState(true, false, false, true))
        assertEquals(MillenniumControlState.Selected, cardControlState(true, false, true, true))
        assertEquals(MillenniumControlState.Error, cardControlState(true, true, true, true))
        assertEquals(MillenniumControlState.Disabled, cardControlState(false, true, true, true))

        val base = defaultMillenniumCardTheme().states
        val theme = base.copy(
            hovered = base.hovered.copy(background = red, interactionOverlay = blue),
            selected = base.selected.copy(background = blue),
        )
        assertSame(red, theme.resolve(cardControlState(true, false, false, true)).background)
        assertSame(blue, theme.resolve(cardControlState(true, false, true, false)).background)
        assertSame(blue, theme.resolve(MillenniumControlState.Hovered).interactionOverlay)
        assertNull(theme.resolve(MillenniumControlState.Default).background)
    }

    @Test
    fun progressResolvesDisabledAndErrorEntries() {
        assertEquals(MillenniumControlState.Default, progressControlState(true, false))
        assertEquals(MillenniumControlState.Error, progressControlState(true, true))
        assertEquals(MillenniumControlState.Disabled, progressControlState(false, true))
        val base = defaultMillenniumProgressTheme().states
        val states = base.copy(error = base.error.copy(progressBackground = red))
        assertSame(red, states.resolve(progressControlState(true, true)).progressBackground)
        assertNull(states.resolve(progressControlState(true, false)).progressBackground)
    }

    @Test
    fun listTileReadsPerStateBackgroundsAndSelectedFallbacks() {
        val colors = MillenniumLightColors
        val explicit = resolveListTileBackground(colors, false, MillenniumControlState.Hovered, 0.35f, red, null, blue)
        assertSame(red, explicit)
        // The theme's per-state background (previously never read) wins over the derived wash...
        val themed = resolveListTileBackground(colors, false, MillenniumControlState.Hovered, 0.35f, null, null, blue)
        assertSame(blue, themed)
        // ...but is not used for selected tiles, which use selectedBackground or the gel.
        val selectedThemed = resolveListTileBackground(colors, true, MillenniumControlState.Selected, 0.35f, null, red, blue)
        assertSame(red, selectedThemed)
        val gel = resolveListTileBackground(colors, true, MillenniumControlState.Selected, 0.35f, null, null, blue)
        assertNotEquals<Brush>(blue, gel)
        val derived = resolveListTileBackground(colors, false, MillenniumControlState.Hovered, 0.35f, null, null, null)
        assertNotEquals<Brush>(blue, derived)
    }

    @Test
    fun textRolesResolveTheirOwnThemeStyle() {
        val base = defaultMillenniumTextTheme().states.default
        assertSame(base.title, base.styleFor(MillenniumTextRole.Title))
        assertSame(base.caption, base.styleFor(MillenniumTextRole.Caption))
        assertSame(base.body, base.styleFor(MillenniumTextRole.Body))
        assertEquals(MillenniumTextRole.entries.size, 7)
        val dimmed = base.copy(alpha = 0.4f)
        val states = defaultMillenniumTextTheme().states.copy(disabled = dimmed)
        assertEquals(0.4f, states.resolve(MillenniumControlState.Disabled).alpha)
        assertEquals(1f, states.resolve(MillenniumControlState.Default).alpha)
    }

    @Test
    fun brushPropertiesSurviveCopyPerState() {
        val loading = defaultMillenniumLoadingTheme().states
        val themed = loading.copy(default = loading.default.copy(fillBrush = red, skeletonBackground = blue))
        assertSame(red, themed.resolve(MillenniumControlState.Default).fillBrush)
        assertSame(blue, themed.resolve(MillenniumControlState.Default).skeletonBackground)

        val scroll = defaultMillenniumScrollbarTheme().states
        val scrollThemed = scroll.copy(hovered = scroll.hovered.copy(thumbBackground = red))
        assertSame(red, scrollThemed.resolve(MillenniumControlState.Hovered).thumbBackground)
        assertNull(scrollThemed.resolve(MillenniumControlState.Default).thumbBackground)

        val pull = defaultMillenniumPullToRefreshTheme().properties
        val pullThemed = pull.copy(indicatorProgressBackground = red)
        assertSame(red, pullThemed.indicatorProgressBackground)
    }
}
