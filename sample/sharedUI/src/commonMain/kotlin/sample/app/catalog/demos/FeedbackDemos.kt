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
internal fun ProgressDemo() {
    var progress by rememberSaveable { mutableFloatStateOf(0.64f) }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        MillenniumText("Downloading a memory", size = 12.sp)
        MillenniumText(
            "${(progress * 100).toInt()}%",
            size = 12.sp,
            color = MillenniumTheme.colors.accent
        )
    }
    MillenniumProgress(progress)
    MillenniumIndeterminateProgress()
    MillenniumButton(
        { progress = if (progress >= 1f) 0f else (progress + 0.12f).coerceAtMost(1f) },
        style = MillenniumButtonStyle.Secondary
    ) { Text(if (progress >= 1f) "Start again" else "A little further") }
}

@Composable
internal fun SnackbarDemo() {
    val host = remember { MillenniumSnackbarHostState() }
    val scope = rememberCoroutineScope()
    Box {
        MillenniumButton(
            onClick = { scope.launch { host.showSnackbar("Saved to your library", "Undo") } }
        ) { Text("Show snackbar") }
        MillenniumSnackbarHost(host, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
internal fun PullToRefreshDemo() {
    var refreshing by rememberSaveable { mutableStateOf(false) }
    var count by rememberSaveable { mutableIntStateOf(6) }
    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(900)
            count += 1
            refreshing = false
        }
    }
    Box(
        Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp))
            .background(MillenniumTheme.colors.inset)
    ) {
        MillenniumPullToRefreshBox(isRefreshing = refreshing, onRefresh = { refreshing = true }) {
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(count) { MillenniumText("Track ${it + 1}", size = 13.sp) }
            }
        }
    }
    MillenniumText(
        "Drag down to refresh · $count tracks",
        size = 11.sp,
        color = MillenniumTheme.colors.muted
    )
}


@Composable
internal fun TooltipDemo() {
    MillenniumTooltip("A hint of the past. Built for today.") {
        MillenniumButton({}, style = MillenniumButtonStyle.Secondary) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MillenniumIcon(MillenniumIcons.Sparkle)
                Text("Hover, focus or hold")
            }
        }
    }
    DemoHint("Mouse hover · keyboard focus · long press")
}
