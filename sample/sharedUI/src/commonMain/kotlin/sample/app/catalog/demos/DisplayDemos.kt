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
internal fun AvatarDemo() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MillenniumAvatar("AJ", "Alex Jordan", size = 36.dp)
        MillenniumAvatar("MK", "Morgan Kim")
        MillenniumAvatar("YOU", "Your profile", size = 60.dp)
    }
    DemoHint("Initials fall back gracefully when there is no image.")
}

@Composable
internal fun IconDemo() {
    val c = MillenniumTheme.colors
    MillenniumText("Icon set", size = 12.sp, weight = FontWeight.SemiBold)
    val icons = listOf(
        "Sparkle" to MillenniumIcons.Sparkle,
        "Music" to MillenniumIcons.Music,
        "Check" to MillenniumIcons.Check,
        "Chevron" to MillenniumIcons.Chevron,
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        icons.forEach { (name, icon) ->
            Column(
                Modifier.width(68.dp).clip(RoundedCornerShape(10.dp))
                    .background(c.inset)
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MillenniumIcon(icon, name, size = 26.dp)
                MillenniumText(name, size = 10.sp, color = c.muted)
            }
        }
    }
    MillenniumText("Tints", size = 12.sp, weight = FontWeight.SemiBold)
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MillenniumIcon(MillenniumIcons.Sparkle, "Accent", tint = c.accent)
        MillenniumIcon(MillenniumIcons.Check, "Success", tint = c.success)
        MillenniumIcon(MillenniumIcons.Music, "Danger", tint = c.danger)
        MillenniumIcon(MillenniumIcons.Chevron, "Muted", tint = c.muted)
    }
    MillenniumText("Sizes", size = 12.sp, weight = FontWeight.SemiBold)
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        listOf(14.dp, 20.dp, 28.dp, 40.dp).forEach {
            MillenniumIcon(MillenniumIcons.Music, "${it.value.toInt()}dp", size = it, tint = c.accent)
        }
    }
    MillenniumText("In buttons", size = 12.sp, weight = FontWeight.SemiBold)
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
        MillenniumButton({}) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                MillenniumIcon(MillenniumIcons.Music, tint = Color.White)
                Text("Play")
            }
        }
        MillenniumIconButton(MillenniumIcons.Sparkle, "Favorite", {}, style = MillenniumIconButtonStyle.Secondary)
        MillenniumIconButton(MillenniumIcons.Check, "Done", {}, style = MillenniumIconButtonStyle.Quiet)
    }
    DemoHint("Vector sharp. Any screen.")
}

@Composable
internal fun CardDemo() {
    MillenniumCard(contentPadding = PaddingValues(16.dp)) {
        MillenniumIcon(MillenniumIcons.Sparkle, tint = MillenniumTheme.colors.accent)
        MillenniumText("2007 soul.\n2026 standards.", weight = FontWeight.SemiBold)
        MillenniumText(
            "Accessible primitives.\nMultiplatform by nature.",
            color = MillenniumTheme.colors.muted,
            size = 12.sp
        )
    }
    DemoHint("A raised glass surface for grouping content.")
}

@Composable
internal fun TextDemo() {
    val c = MillenniumTheme.colors
    MillenniumText("Yesterday’s charm.", size = 28.sp, weight = FontWeight.Bold)
    MillenniumText("Section heading", size = 18.sp, weight = FontWeight.SemiBold)
    MillenniumText("Body copy sits comfortably at the default size with generous leading.")
    MillenniumText("Muted supporting text", color = c.muted, size = 12.sp)
    MillenniumText("Accent emphasis", color = c.accent, weight = FontWeight.SemiBold)
}

@Composable
internal fun SeparatorDemo() {
    MillenniumText("Above the line")
    MillenniumSeparator()
    MillenniumText("Below the line")
    Row(
        Modifier.height(32.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MillenniumText("Leading")
        MillenniumSeparator(Modifier.fillMaxHeight(), vertical = true)
        MillenniumText("Trailing")
    }
}

@Composable
internal fun DisclosureDemo() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    MillenniumDisclosure(expanded, { expanded = it }, "Why Millennium?") {
        MillenniumText(
            "Because interfaces used to feel like objects. We’re bringing back that warmth, without bringing back the limitations.",
            color = MillenniumTheme.colors.muted,
            size = 13.sp
        )
    }
}

@Composable
internal fun ScrollbarDemo() {
    val scroll = rememberScrollState()
    Box(
        Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(8.dp)).background(
            MillenniumTheme.colors.inset
        ).padding(12.dp)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(end = 18.dp).verticalScroll(scroll),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf(
                "AIM away messages",
                "The first iPhone",
                "Mixtapes for the internet",
                "Your favorite wallpaper",
                "One more song",
                "The future looked glossy",
                "Some things stay with us"
            ).forEachIndexed { index, item ->
                MillenniumText("0${index + 1}   $item", size = 12.sp)
                MillenniumSeparator()
            }
        }
        MillenniumScrollbar(scroll, Modifier.align(Alignment.CenterEnd).fillMaxHeight())
    }
}
