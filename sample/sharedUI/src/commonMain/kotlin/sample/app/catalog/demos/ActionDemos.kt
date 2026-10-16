package sample.app.catalog.demos

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
internal fun ButtonDemo() {
    var clicks by rememberSaveable { mutableIntStateOf(0) }
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MillenniumButton({ clicks++ }) { Text("Let’s go →") }
        MillenniumButton(
            { clicks++ },
            style = MillenniumButtonStyle.Secondary
        ) { Text("Take it easy") }
        MillenniumButton({}, enabled = false) { Text("Not just yet") }
        MillenniumButton({ clicks = 0 }, style = MillenniumButtonStyle.Danger) { Text("Reset") }
    }
    MillenniumText(
        if (clicks == 0) "Try a click. Or navigate with Tab + Enter." else "$clicks clicks. Still feels good.",
        color = MillenniumTheme.colors.muted,
        size = 11.sp
    )
}

@Composable
internal fun IconButtonDemo() {
    var saved by rememberSaveable { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MillenniumIconButton(MillenniumIcons.Sparkle, "Add to favorites", { saved = !saved })
        MillenniumIconButton(
            MillenniumIcons.Music,
            "Play",
            {},
            style = MillenniumIconButtonStyle.Secondary
        )
        MillenniumIconButton(
            MillenniumIcons.Chevron,
            "More options",
            {},
            style = MillenniumIconButtonStyle.Quiet
        )
        MillenniumIconButton(
            MillenniumIcons.Check,
            "Delete",
            {},
            style = MillenniumIconButtonStyle.Danger
        )
        MillenniumIconButton(MillenniumIcons.Sparkle, "Disabled", {}, enabled = false)
    }
    MillenniumText(
        if (saved) "Filled variants wear the orb sheen." else "Primary, secondary, quiet, danger — all 44dp targets.",
        size = 11.sp,
        color = MillenniumTheme.colors.muted
    )
}
