package org.spotube.millennium

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import org.spotube.millennium.composables.MillenniumButtonMotion
import org.spotube.millennium.composables.MillenniumSegmentedButtonDefaults
import org.spotube.millennium.composables.defaultMillenniumButtonTheme
import org.spotube.millennium.composables.defaultMillenniumCheckboxTheme
import org.spotube.millennium.composables.defaultMillenniumDisclosureTheme
import org.spotube.millennium.composables.defaultMillenniumIconButtonTheme
import org.spotube.millennium.composables.defaultMillenniumRadioGroupTheme
import org.spotube.millennium.composables.defaultMillenniumSegmentedButtonTheme
import org.spotube.millennium.composables.defaultMillenniumToggleSwitchTheme
import org.spotube.millennium.composables.defaultMillenniumTriStateCheckboxTheme
import org.spotube.millennium.composables.millenniumResolveControlState
import org.spotube.millennium.composables.withSelectedFallback
import org.spotube.millennium.theme.MillenniumControlState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/** Theme-level coverage: brushes and visuals added to the control themes are what the components read. */
class ControlThemeBrushTest {
    private val overlay: Brush = SolidColor(Color.Red)
    private val rim: Brush = SolidColor(Color.Green)

    @Test
    fun statePrecedenceMatchesDocumentedOrder() {
        assertEquals(MillenniumControlState.Disabled, millenniumResolveControlState(enabled = false, pressed = true))
        assertEquals(MillenniumControlState.Pressed, millenniumResolveControlState(pressed = true, focused = true))
        assertEquals(MillenniumControlState.Focused, millenniumResolveControlState(focused = true, error = true))
        assertEquals(MillenniumControlState.Error, millenniumResolveControlState(error = true, hovered = true))
        assertEquals(MillenniumControlState.Hovered, millenniumResolveControlState(hovered = true, selected = true))
        assertEquals(MillenniumControlState.Selected, millenniumResolveControlState(selected = true))
        assertEquals(MillenniumControlState.Default, millenniumResolveControlState())
    }

    @Test
    fun buttonInteractionBrushesAreThemedPerState() {
        val base = defaultMillenniumButtonTheme()
        assertNull(base.primary.hovered.interactionOverlay)
        assertNull(base.primary.default.loadingTrackColor)
        assertEquals(18.dp, base.primary.default.loadingSpinnerSize)
        assertTrue(base.primary.default.labelShadow)
        val themed = base.copy(
            primary = base.primary.copy(
                hovered = base.primary.hovered.copy(interactionOverlay = overlay, interactionRim = rim),
                error = base.primary.error.copy(borderColor = Color.Magenta, loadingSpinnerSize = 24.dp),
            ),
        )
        assertEquals(overlay, themed.primary.resolve(MillenniumControlState.Hovered).interactionOverlay)
        assertEquals(rim, themed.primary.resolve(MillenniumControlState.Hovered).interactionRim)
        assertNull(themed.primary.resolve(MillenniumControlState.Pressed).interactionOverlay)
        assertEquals(Color.Magenta, themed.primary.resolve(MillenniumControlState.Error).borderColor)
        assertEquals(24.dp, themed.primary.resolve(MillenniumControlState.Error).loadingSpinnerSize)
    }

    @Test
    fun buttonMotionCarriesSpringParameters() {
        val motion = MillenniumButtonMotion()
        assertEquals(0.65f, motion.scaleDampingRatio)
        assertEquals(0.78f, motion.elevationDampingRatio)
        val icon = defaultMillenniumIconButtonTheme().primary.default.motion
        assertEquals(0.62f, icon.scaleDampingRatio)
        assertEquals(1.04f, icon.hoverScale)
    }

    @Test
    fun iconButtonOrbBrushesAndPaddingAreThemeDriven() {
        val base = defaultMillenniumIconButtonTheme()
        val p = base.primary.default
        assertNull(p.orbDomeBrush)
        assertNull(p.orbRimBrush)
        assertNull(p.orbLowerGlowBrush)
        assertNull(p.orbInnerRimBrush)
        assertNull(p.orbLowerGlowColor)
        assertEquals(PaddingValues(0.dp), p.contentPadding)
        val themed = base.copy(
            danger = base.danger.copy(
                default = p.copy(orbDomeBrush = overlay, orbRimBrush = rim, orbLowerGlowColor = Color.Cyan),
            ),
        )
        assertEquals(overlay, themed.danger.resolve(MillenniumControlState.Default).orbDomeBrush)
        assertEquals(rim, themed.danger.resolve(MillenniumControlState.Default).orbRimBrush)
        assertEquals(Color.Cyan, themed.danger.resolve(MillenniumControlState.Default).orbLowerGlowColor)
        assertEquals(CircleShape, themed.danger.default.shape)
    }

    @Test
    fun segmentedSelectedStateIsReachableAndShapeRadiusIsConfigurable() {
        val base = defaultMillenniumSegmentedButtonTheme().states
        val themed = base.copy(
            selected = base.selected.copy(background = overlay, bottomRimColor = Color.Blue, shadowElevation = 3.dp),
            error = base.error.copy(borderColor = Color.Red),
        )
        assertEquals(overlay, themed.resolve(MillenniumControlState.Selected).background)
        assertEquals(Color.Blue, themed.resolve(MillenniumControlState.Selected).bottomRimColor)
        assertEquals(3.dp, themed.resolve(MillenniumControlState.Selected).shadowElevation)
        assertNull(themed.resolve(MillenniumControlState.Hovered).background)
        assertEquals(Color.Red, themed.resolve(MillenniumControlState.Error).borderColor)
        assertEquals(
            RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp, topEnd = 0.dp, bottomEnd = 0.dp),
            MillenniumSegmentedButtonDefaults.itemShape(0, 3, cornerRadius = 4.dp),
        )
        assertEquals(
            MillenniumSegmentedButtonDefaults.itemShape(1, 3, cornerRadius = 10.dp),
            MillenniumSegmentedButtonDefaults.itemShape(1, 3),
        )
    }

    @Test
    fun switchThumbAndTrackVisualsComeFromTheme() {
        val base = defaultMillenniumToggleSwitchTheme().states
        val d = base.default
        assertNull(d.selectedThumbBackground)
        assertEquals(1.12f, d.thumbPressedScaleX)
        assertEquals(0.92f, d.thumbPressedScaleY)
        assertEquals(0.68f, d.thumbSlideDampingRatio)
        val themed = base.copy(
            pressed = base.pressed.copy(thumbPressedScaleX = 1.3f, thumbInteractionOverlay = overlay),
            selected = base.selected.copy(selectedThumbBackground = rim, trackBottomRimColor = Color.Yellow),
        )
        assertEquals(1.3f, themed.resolve(MillenniumControlState.Pressed).thumbPressedScaleX)
        assertEquals(overlay, themed.resolve(MillenniumControlState.Pressed).thumbInteractionOverlay)
        assertEquals(rim, themed.resolve(MillenniumControlState.Selected).selectedThumbBackground)
        assertEquals(Color.Yellow, themed.resolve(MillenniumControlState.Selected).trackBottomRimColor)
    }

    @Test
    fun checkboxFaceStyleDefaultsKeepTheOriginalLookAndThemeOverrides() {
        val d = defaultMillenniumCheckboxTheme().states.default
        assertEquals(1.05f, d.faceHoverScale)
        assertEquals(0.90f, d.facePressedScale)
        assertEquals(0.4f, d.markEnterScale)
        assertEquals(CircleShape, d.dashShape)
        assertNull(d.dashBrush)
        assertNull(d.faceInteractionOverlay)
        val tri = defaultMillenniumTriStateCheckboxTheme().states.default
        assertEquals(0.52f, tri.markEnterDampingRatio)
        val custom = d.copy(dashBrush = overlay, selectedFaceBorderColor = Color.Cyan, facePressedScale = 0.8f)
        assertEquals(overlay, custom.dashBrush)
        assertEquals(Color.Cyan, custom.selectedFaceBorderColor)
        assertEquals(0.8f, custom.facePressedScale)
    }

    @Test
    fun checkedHoveredBoxFallsBackToTheSelectedEntry() {
        val base = defaultMillenniumCheckboxTheme().states
        val selected = base.selected.copy(
            selectedFaceBackground = overlay,
            selectedFaceForeground = Color.Yellow,
            faceInteractionOverlay = rim,
        )
        val hovered = base.hovered.copy(selectedFaceForeground = Color.Black)
        val merged = hovered.withSelectedFallback(selected)
        assertEquals(overlay, merged.selectedFaceBackground)
        assertEquals(Color.Black, merged.selectedFaceForeground)
        assertEquals(rim, merged.faceInteractionOverlay)
        val tri = defaultMillenniumTriStateCheckboxTheme().states
        val triMerged = tri.hovered.withSelectedFallback(tri.selected.copy(dashBrush = overlay))
        assertEquals(overlay, triMerged.dashBrush)
    }

    @Test
    fun radioDotAndIndicatorVisualsComeFromTheme() {
        val base = defaultMillenniumRadioGroupTheme().states
        val d = base.default
        assertNull(d.dotBrush)
        assertEquals(CircleShape, d.dotShape)
        assertEquals(0.3f, d.dotEnterScale)
        assertEquals(0.90f, d.indicatorPressedScale)
        val themed = base.copy(selected = base.selected.copy(dotBrush = overlay, indicatorHoverScale = 1.2f))
        assertEquals(overlay, themed.resolve(MillenniumControlState.Selected).dotBrush)
        assertEquals(1.2f, themed.resolve(MillenniumControlState.Selected).indicatorHoverScale)
        assertNotEquals(overlay, themed.resolve(MillenniumControlState.Default).dotBrush)
    }

    @Test
    fun disclosureChevronBackgroundsAndSpringsComeFromTheme() {
        val base = defaultMillenniumDisclosureTheme().states
        val d = base.default
        assertEquals(180f, d.chevronRotation)
        assertEquals(0.992f, d.headerPressedScale)
        assertNull(d.headerBackground)
        assertNull(d.chevronSize)
        val themed = base.copy(
            selected = base.selected.copy(
                headerBackground = overlay,
                contentBackground = rim,
                chevronForeground = Color.Magenta,
                chevronRotation = 90f,
                chevronSize = 12.dp,
            ),
        )
        val s = themed.resolve(MillenniumControlState.Selected)
        assertEquals(overlay, s.headerBackground)
        assertEquals(rim, s.contentBackground)
        assertEquals(Color.Magenta, s.chevronForeground)
        assertEquals(90f, s.chevronRotation)
        assertEquals(12.dp, s.chevronSize)
    }
}
