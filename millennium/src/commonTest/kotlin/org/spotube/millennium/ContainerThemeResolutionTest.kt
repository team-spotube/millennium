package org.spotube.millennium

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import org.spotube.millennium.composables.MillenniumDialogThemeProperties
import org.spotube.millennium.composables.MillenniumSidebarTriggerState
import org.spotube.millennium.composables.dialogDividerBrush
import org.spotube.millennium.composables.defaultMillenniumBottomNavigationBarTheme
import org.spotube.millennium.composables.defaultMillenniumBottomSheetTheme
import org.spotube.millennium.composables.defaultMillenniumDialogTheme
import org.spotube.millennium.composables.defaultMillenniumDrawerTheme
import org.spotube.millennium.composables.defaultMillenniumDropdownMenuTheme
import org.spotube.millennium.composables.defaultMillenniumModalBottomSheetTheme
import org.spotube.millennium.composables.defaultMillenniumScaffoldTheme
import org.spotube.millennium.composables.defaultMillenniumSidebarTheme
import org.spotube.millennium.composables.defaultMillenniumSnackbarTheme
import org.spotube.millennium.composables.defaultMillenniumTabGroupTheme
import org.spotube.millennium.composables.defaultMillenniumTopAppBarTheme
import org.spotube.millennium.composables.scaffoldContainerBrush
import org.spotube.millennium.composables.scaffoldSeparatorBrush
import org.spotube.millennium.composables.sidebarDividerBrush
import org.spotube.millennium.composables.sidebarDividerHighlightBrush
import org.spotube.millennium.composables.topAppBarSeparatorBrush
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumLightColors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

class ContainerThemeResolutionTest {
    private val colors = MillenniumLightColors
    private val gloss: Brush = Brush.verticalGradient(listOf(Color.Red, Color.Blue))

    @Test
    fun newVisualPropertiesDefaultToTheLegacyLook() {
        val dialog = defaultMillenniumDialogTheme().states.default
        assertNull(dialog.scrimBrush)
        assertNull(dialog.panelOverlayBrush)
        assertEquals(0.72f, dialog.enterSpringDamping)
        assertEquals(1f / 14f, dialog.exitSlideFraction)
        assertEquals(RoundedCornerShape(5.dp), dialog.closeCapShape)
        assertEquals(12.dp, dialog.titleBarSpacing)

        val tabs = defaultMillenniumTabGroupTheme().states.default
        assertEquals(0.96f, tabs.tabPressedScale)
        assertEquals(1.015f, tabs.tabHoverScale)
        assertNull(tabs.tabHoverBackground)

        val menu = defaultMillenniumDropdownMenuTheme().states.default
        assertEquals(0.975f, menu.itemPressedScale)
        assertEquals(8.dp, menu.scrollbarGutter)
        assertNull(menu.itemActiveBackground)

        val bar = defaultMillenniumBottomNavigationBarTheme()
        assertEquals(0.96f, bar.item.default.pressedScale)
        assertNull(bar.states.default.overlayBrush)

        val sidebar = defaultMillenniumSidebarTheme()
        assertEquals(0.97f, sidebar.item.default.pressedScale)
        assertEquals(RoundedCornerShape(50), sidebar.item.default.selectedIndicatorShape)
        assertNull(sidebar.header.default.dividerBrush)
        assertEquals(6.dp, sidebar.group.default.labelLeadingSpacing)

        assertNull(defaultMillenniumDrawerTheme().states.default.scrimBrush)
        assertEquals(CircleShape, defaultMillenniumBottomSheetTheme().states.default.handleShape)
        assertEquals(CircleShape, defaultMillenniumModalBottomSheetTheme().states.default.handleShape)
        assertEquals(0.92f, defaultMillenniumSnackbarTheme().properties.enterInitialScale)
        assertEquals(1.dp, defaultMillenniumTopAppBarTheme().properties.separatorThickness)
        assertNull(defaultMillenniumScaffoldTheme().properties.containerBrush)
    }

    @Test
    fun topAppBarSeparatorPrefersOverrideThenThemeBrushThenColor() {
        val base = defaultMillenniumTopAppBarTheme().properties
        assertEquals(SolidColor(colors.border), topAppBarSeparatorBrush(base, null, colors))
        assertEquals(SolidColor(Color.Green), topAppBarSeparatorBrush(base.copy(separatorColor = Color.Green), null, colors))
        assertSame(gloss, topAppBarSeparatorBrush(base.copy(separatorBrush = gloss, separatorColor = Color.Green), null, colors))
        assertEquals(SolidColor(Color.Cyan), topAppBarSeparatorBrush(base.copy(separatorBrush = gloss), Color.Cyan, colors))
    }

    @Test
    fun scaffoldBrushesFollowTheTheme() {
        val base = defaultMillenniumScaffoldTheme().properties
        assertEquals(SolidColor(colors.background), scaffoldContainerBrush(base, Color.Unspecified, colors))
        assertEquals(SolidColor(Color.Magenta), scaffoldContainerBrush(base, Color.Magenta, colors))
        assertSame(gloss, scaffoldContainerBrush(base.copy(containerBrush = gloss), Color.Magenta, colors))

        assertNull(scaffoldSeparatorBrush(base, colors), "Untouched theme keeps the themed separator")
        assertNull(scaffoldSeparatorBrush(base.copy(topBarSeparatorThickness = 0.dp, topBarSeparatorBrush = gloss), colors))
        assertSame(gloss, scaffoldSeparatorBrush(base.copy(topBarSeparatorBrush = gloss), colors))
        assertEquals(SolidColor(Color.Red), scaffoldSeparatorBrush(base.copy(topBarSeparatorColor = Color.Red), colors))
        assertEquals(SolidColor(colors.border), scaffoldSeparatorBrush(base.copy(topBarSeparatorThickness = 2.dp), colors))
    }

    @Test
    fun dialogAndSidebarDividersResolveBrushes() {
        assertEquals(SolidColor(colors.border), dialogDividerBrush(null, null, null, colors))
        assertEquals(SolidColor(Color.Gray), dialogDividerBrush(null, null, Color.Gray, colors))
        assertSame(gloss, dialogDividerBrush(null, gloss, Color.Gray, colors))
        assertEquals(SolidColor(Color.Black), dialogDividerBrush(Color.Black, gloss, Color.Gray, colors))

        assertEquals(SolidColor(colors.border), sidebarDividerBrush(null, null, null, colors))
        assertSame(gloss, sidebarDividerBrush(null, gloss, null, colors))
        assertEquals(
            SolidColor(colors.highlight.copy(alpha = 0.45f)),
            sidebarDividerHighlightBrush(null, null, colors),
        )
        assertEquals(SolidColor(Color.White), sidebarDividerHighlightBrush(null, Color.White, colors))
        assertSame(gloss, sidebarDividerHighlightBrush(gloss, Color.White, colors))
    }

    @Test
    fun perStateOverridesAreVisibleThroughTheStateProperty() {
        val dialog = defaultMillenniumDialogTheme()
        val custom = dialog.copy(
            states = dialog.states.copy(
                hovered = dialog.states.hovered.copy(closeCapBackground = gloss, closeScaleHover = 1.3f),
            ),
        )
        assertSame(gloss, custom.states.resolve(MillenniumControlState.Hovered).closeCapBackground)
        assertNull(custom.states.resolve(MillenniumControlState.Default).closeCapBackground)
        assertEquals(1.3f, custom.states.resolve(MillenniumControlState.Hovered).closeScaleHover)

        val sidebar = defaultMillenniumSidebarTheme()
        val themed = sidebar.copy(
            item = sidebar.item.copy(
                hovered = sidebar.item.hovered.copy(hoverBackground = gloss),
                selected = sidebar.item.selected.copy(selectedBackground = gloss),
            ),
        )
        assertSame(gloss, themed.item.resolve(MillenniumControlState.Hovered).hoverBackground)
        assertSame(gloss, themed.item.resolve(MillenniumControlState.Selected).selectedBackground)
        assertNull(themed.item.resolve(MillenniumControlState.Pressed).hoverBackground)

        val menu = defaultMillenniumDropdownMenuTheme()
        val menuThemed = menu.copy(
            states = menu.states.copy(selected = menu.states.selected.copy(itemSelectedBackground = gloss)),
        )
        assertSame(gloss, menuThemed.states.resolve(MillenniumControlState.Selected).itemSelectedBackground)

        val tabs = defaultMillenniumTabGroupTheme()
        val tabsThemed = tabs.copy(
            states = tabs.states.copy(hovered = tabs.states.hovered.copy(tabHoverBackground = gloss)),
        )
        assertSame(gloss, tabsThemed.states.resolve(MillenniumControlState.Hovered).tabHoverBackground)
    }

    @Test
    fun triggerStateExposesResolvedProperties() {
        val theme = defaultMillenniumSidebarTheme()
        val hovered = theme.trigger.resolve(MillenniumControlState.Hovered)
        val state = MillenniumSidebarTriggerState(false, 90f, {}, MillenniumControlState.Hovered, hovered)
        assertEquals(2.dp, state.properties?.shadowElevation)
        assertNotEquals(theme.trigger.default.shadowElevation, state.properties?.shadowElevation)
        assertTrue(theme.trigger.default.background == null && theme.trigger.default.borderColor == null)
    }
}
