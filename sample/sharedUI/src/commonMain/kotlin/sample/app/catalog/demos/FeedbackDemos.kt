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
    var buffered by rememberSaveable { mutableFloatStateOf(0.82f) }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        MillenniumText("Downloading a memory", size = 12.sp)
        MillenniumText(
            "${(progress * 100).toInt()}%",
            size = 12.sp,
            color = MillenniumTheme.colors.accent
        )
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        MillenniumButton(
            { progress = if (progress >= 1f) 0f else (progress + 0.12f).coerceAtMost(1f); buffered = maxOf(buffered, progress) },
            style = MillenniumButtonStyle.Secondary
        ) { Text(if (progress >= 1f) "Start again" else "A little further") }
        MillenniumButton({ progress = 0.64f; buffered = 0.82f }, style = MillenniumButtonStyle.Quiet) { Text("Reset") }
    }
    MillenniumSeparator()
    MillenniumText("DETERMINATE", size = 10.sp, weight = FontWeight.Bold, color = MillenniumTheme.colors.muted)
    Variant("Gel") { MillenniumProgress(progress) }
    Variant("Barber pole") { MillenniumStripedProgress(progress = progress) }
    Variant("Glint") { MillenniumShimmerProgress(progress) }
    Variant("Blocks") { MillenniumSegmentedProgress(progress = progress) }
    Variant("Buffered") { MillenniumBufferedProgress(progress, buffered) }
    MillenniumSeparator()
    MillenniumText("INDETERMINATE", size = 10.sp, weight = FontWeight.Bold, color = MillenniumTheme.colors.muted)
    Variant("Bounce") { MillenniumIndeterminateProgress() }
    Variant("Marquee") { MillenniumMarqueeProgress() }
    Variant("Marching barber pole") { MillenniumStripedProgress() }
    Variant("Chasing blocks") { MillenniumSegmentedProgress() }
    MillenniumSeparator()
    MillenniumText("COLOURS & SHAPES", size = 10.sp, weight = FontWeight.Bold, color = MillenniumTheme.colors.muted)
    Variant("Success") { MillenniumStripedProgress(progress = 0.8f, color = MillenniumTheme.colors.success) }
    Variant("Danger, squared") {
        MillenniumShimmerProgress(
            0.35f,
            color = MillenniumTheme.colors.danger,
            shape = RoundedCornerShape(3.dp),
        )
    }
    Variant("Slim") { MillenniumProgress(progress, height = 6.dp) }
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

@Composable
internal fun LoadingDemo() {
    val c = MillenniumTheme.colors
    var progress by rememberSaveable { mutableFloatStateOf(0.72f) }
    var saving by rememberSaveable { mutableStateOf(false) }
    var loaded by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(saving) {
        if (saving) {
            delay(1800)
            saving = false
        }
    }
    LaunchedEffect(loaded) {
        if (!loaded) {
            delay(2200)
            loaded = true
        }
    }

    @Composable
    fun Tile(name: String, content: @Composable () -> Unit) {
        Column(
            Modifier.width(112.dp).height(112.dp).clip(RoundedCornerShape(12.dp)).background(c.inset),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
        ) {
            Box(Modifier.height(52.dp), contentAlignment = Alignment.Center) { content() }
            MillenniumText(name, size = 11.sp, color = c.muted)
        }
    }

    MillenniumText("CIRCULAR", size = 10.sp, weight = FontWeight.Bold, color = c.muted)
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Tile("Ring · determinate") {
            MillenniumCircularProgress(progress = progress, size = 52.dp) {
                MillenniumText("${(progress * 100).toInt()}", size = 12.sp, weight = FontWeight.SemiBold)
            }
        }
        Tile("Ring · endless") { MillenniumCircularProgress(size = 52.dp) }
        Tile("Orb") { MillenniumOrbSpinner(size = 52.dp) }
    }
    MillenniumSlider(progress, { progress = it }, "Ring progress")
    MillenniumSeparator()
    MillenniumText("LOADERS", size = 10.sp, weight = FontWeight.Bold, color = c.muted)
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Tile("Spokes") { MillenniumSpokeSpinner(size = 36.dp) }
        Tile("Beads") { MillenniumDotsLoader() }
        Tile("Equalizer") { MillenniumBarsLoader(size = 40.dp) }
        Tile("Ripple") { MillenniumPulseLoader(size = 52.dp) }
        Tile("Success") { MillenniumCircularProgress(size = 40.dp, color = c.success) }
        Tile("Danger") { MillenniumOrbSpinner(size = 44.dp, color = c.danger) }
    }
    MillenniumSeparator()
    MillenniumText("SIZES", size = 10.sp, weight = FontWeight.Bold, color = c.muted)
    Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
        listOf(20.dp, 28.dp, 40.dp, 64.dp).forEach { MillenniumCircularProgress(size = it, thickness = (it.value / 7f).dp) }
    }
    MillenniumSeparator()
    MillenniumText("IN CONTEXT", size = 10.sp, weight = FontWeight.Bold, color = c.muted)
    MillenniumButton({ saving = true }, enabled = !saving) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            if (saving) MillenniumSpokeSpinner(size = 18.dp, color = c.onAccent)
            Text(if (saving) "Saving…" else "Save playlist")
        }
    }
    MillenniumCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(14.dp)) {
        if (loaded) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                MillenniumAvatar("GH", "Golden hour")
                Column(Modifier.weight(1f)) {
                    MillenniumText("The golden hour mix", weight = FontWeight.SemiBold)
                    MillenniumText("12 tracks · a simpler time", size = 12.sp, color = c.muted)
                }
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                MillenniumSkeleton(Modifier.size(40.dp), shape = CircleShape)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MillenniumSkeleton(Modifier.fillMaxWidth(0.7f).height(14.dp))
                    MillenniumSkeleton(Modifier.fillMaxWidth(0.45f).height(10.dp))
                }
            }
        }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        MillenniumButton({ loaded = false }, style = MillenniumButtonStyle.Secondary) { Text("Reload skeleton") }
        DemoHint(if (loaded) "Loaded" else "Fetching…")
    }
}
