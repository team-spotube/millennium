package sample.app.catalog.demos

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.composeunstyled.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.spotube.millennium.composables.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPalette
import org.spotube.millennium.theme.gelBrush


/**
 * Dialog, drawer and modal-sheet state lives at the catalog root: these overlays need finite,
 * screen-sized constraints, which a demo inside a scrolling page cannot provide.
 */
@Stable
internal class DemoOverlays(
    val drawer: UnstyledDrawerState<MillenniumDrawerValue>,
    val modalSheet: ModalBottomSheetState,
    val shareName: TextFieldState,
) {
    var dialog by mutableStateOf(false)
    var confirmDialog by mutableStateOf(false)
    var formDialog by mutableStateOf(false)
    var shareCollab by mutableStateOf(true)
    var shareLink by mutableStateOf(false)
}

@Composable
internal fun rememberDemoOverlays(): DemoOverlays {
    val drawer = rememberMillenniumDrawerState()
    val modalSheet = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    val shareName = rememberTextFieldState("The golden hour mix")
    return remember { DemoOverlays(drawer, modalSheet, shareName) }
}

internal val LocalDemoOverlays = staticCompositionLocalOf<DemoOverlays?> { null }

@Composable
internal fun DialogDemo() {
    val overlays = LocalDemoOverlays.current ?: return
    MillenniumButton({ overlays.dialog = true }, modifier = Modifier.fillMaxWidth()) { Text("Open dialog") }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        MillenniumButton(
            { overlays.confirmDialog = true },
            modifier = Modifier.weight(1f),
            style = MillenniumButtonStyle.Danger
        ) { Text("Alert dialog") }
        MillenniumButton(
            { overlays.formDialog = true },
            modifier = Modifier.weight(1f),
            style = MillenniumButtonStyle.Secondary
        ) { Text("Form dialog") }
    }
    DemoHint("Header, body and footer bricks. Escape or Back dismisses.")
}

@Composable
internal fun DrawerDemo() {
    val overlays = LocalDemoOverlays.current ?: return
    MillenniumButton(
        { overlays.drawer.targetValue = MillenniumDrawerValue.Open },
        modifier = Modifier.fillMaxWidth()
    ) { Text("Open drawer") }
    DemoHint("Drag it closed, tap outside, or press Escape.")
}

@Composable
internal fun ModalSheetDemo() {
    val overlays = LocalDemoOverlays.current ?: return
    MillenniumButton(
        { overlays.modalSheet.targetDetent = SheetDetent.FullyExpanded },
        modifier = Modifier.fillMaxWidth()
    ) { Text("Open modal sheet") }
    DemoHint("A real drag handle, scrim and native dismissal.")
}

/** Renders the overlays opened by [DialogDemo], [DrawerDemo] and [ModalSheetDemo]. Place at the root. */
@Composable
internal fun DemoOverlayHost(overlays: DemoOverlays) {
    val c = MillenniumTheme.colors
    MillenniumDialog(overlays.dialog, { overlays.dialog = false }) {
        MillenniumDialogHeader {
            MillenniumIcon(MillenniumIcons.Sparkle, tint = c.accent)
            MillenniumDialogTitle { Text("Good design never gets old.") }
            MillenniumDialogClose()
        }
        MillenniumDialogBody {
            MillenniumDialogDescription {
                Text("The familiar warmth of 2007, with room for what comes next. This dialog keeps focus inside and dismisses with Escape or Back.")
            }
            MillenniumSeparator()
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MillenniumAvatar("MK", "Morgan Kim", size = 32.dp)
                MillenniumText("Morgan liked this design", color = c.muted, size = 13.sp)
            }
        }
        MillenniumDialogFooter {
            MillenniumButton({ overlays.dialog = false }, style = MillenniumButtonStyle.Secondary) { Text("Close") }
            MillenniumButton({ overlays.dialog = false }) { Text("Sounds good") }
        }
    }
    MillenniumAlertDialog(
        visible = overlays.confirmDialog,
        onDismissRequest = { overlays.confirmDialog = false },
        icon = { MillenniumIcon(MillenniumIcons.Music, tint = c.danger) },
        title = { Text("Delete this playlist?") },
        description = { Text("“The golden hour mix” and its 12 tracks will be removed from your library. This can’t be undone.") },
        actions = {
            MillenniumButton({ overlays.confirmDialog = false }, style = MillenniumButtonStyle.Secondary) { Text("Keep it") }
            MillenniumButton({ overlays.confirmDialog = false }, style = MillenniumButtonStyle.Danger) { Text("Delete") }
        },
    )
    MillenniumDialog(overlays.formDialog, { overlays.formDialog = false }, paneTitle = "Share playlist") {
        MillenniumDialogHeader(background = Brush.verticalGradient(listOf(c.raised, c.surface))) {
            MillenniumDialogTitle("Share playlist")
            MillenniumDialogClose(content = {
                MillenniumIcon(MillenniumIcons.Chevron, modifier = Modifier.rotate(90f), tint = c.muted)
            })
        }
        MillenniumDialogBody {
            MillenniumTextField(overlays.shareName, "Playlist name", placeholder = "Name it")
            MillenniumCheckbox(overlays.shareCollab, { overlays.shareCollab = it }, "Let friends add tracks")
            MillenniumSwitch(overlays.shareLink, { overlays.shareLink = it }, "Anyone with the link can listen")
        }
        MillenniumDialogFooter(horizontalArrangement = Arrangement.SpaceBetween, showDivider = false) {
            MillenniumText("Footer slots can be laid out freely", color = c.muted, size = 11.sp)
            MillenniumButton({ overlays.formDialog = false }) { Text("Share") }
        }
    }
    MillenniumDrawer(overlays.drawer) {
        MillenniumText("A little more space", size = 24.sp, weight = FontWeight.SemiBold)
        MillenniumText("An edge-attached drawer, themed in smoked glass and pearl.", color = c.muted)
        MillenniumSeparator()
        MillenniumButton({ overlays.drawer.targetValue = MillenniumDrawerValue.Closed }) { Text("Close drawer") }
    }
    MillenniumModalBottomSheet(overlays.modalSheet) {
        MillenniumIcon(MillenniumIcons.Music, modifier = Modifier.size(32.dp), tint = c.accent)
        MillenniumText("Your next favorite track", size = 24.sp, weight = FontWeight.SemiBold)
        MillenniumText(
            "A modal sheet with a real drag handle, scrim, and native dismissal behavior.",
            color = c.muted
        )
        MillenniumProgress(0.68f)
        MillenniumButton({ overlays.modalSheet.targetDetent = SheetDetent.Hidden }) { Text("Back to the collection") }
    }
}

@Composable
internal fun BottomSheetDemo() {
    val state = rememberBottomSheetState(initialDetent = SheetDetent.Hidden)
    Box(
        Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(12.dp)).background(
            MillenniumTheme.colors.inset
        )
    ) {
        Column(
            Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MillenniumText(
                "An in-place, draggable sheet",
                color = MillenniumTheme.colors.muted,
                size = 12.sp
            )
            MillenniumButton({
                state.targetDetent = SheetDetent.FullyExpanded
            }) { Text("Slide it up ↑") }
        }
        MillenniumBottomSheet(state, Modifier.fillMaxSize()) {
            MillenniumText("Hello from down here.", weight = FontWeight.SemiBold)
            MillenniumText(
                "Drag the handle to move me.",
                color = MillenniumTheme.colors.muted,
                size = 12.sp
            )
            MillenniumButton(
                { state.targetDetent = SheetDetent.Hidden },
                style = MillenniumButtonStyle.Secondary
            ) { Text("Slide it down ↓") }
        }
    }
}
