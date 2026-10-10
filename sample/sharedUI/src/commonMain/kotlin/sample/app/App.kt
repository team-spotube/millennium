package sample.app

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.composeunstyled.*
import org.spotube.millennium.composables.*
import org.spotube.millennium.composables.MillenniumAvatar
import org.spotube.millennium.composables.MillenniumBottomSheet
import org.spotube.millennium.composables.MillenniumButton
import org.spotube.millennium.composables.MillenniumButtonStyle
import org.spotube.millennium.composables.MillenniumCard
import org.spotube.millennium.composables.MillenniumCheckbox
import org.spotube.millennium.composables.MillenniumDisclosure
import org.spotube.millennium.composables.MillenniumDropdownMenu
import org.spotube.millennium.composables.MillenniumIndeterminateProgress
import org.spotube.millennium.composables.MillenniumModalBottomSheet
import org.spotube.millennium.composables.MillenniumProgress
import org.spotube.millennium.composables.MillenniumScrollbar
import org.spotube.millennium.composables.MillenniumSeparator
import org.spotube.millennium.composables.MillenniumSwitch
import org.spotube.millennium.composables.MillenniumTriStateCheckbox
import org.spotube.millennium.composables.MillenniumText
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.gelBrush

private val categories =
    listOf("All components", "Actions", "Inputs", "Navigation", "Feedback", "Overlays")

@Composable
fun App() {
    var dark by rememberSaveable { mutableStateOf(false) }
    var category by rememberSaveable { mutableStateOf("All components") }
    var dialog by rememberSaveable { mutableStateOf(false) }
    val drawer = rememberMillenniumDrawerState()
    val modalSheet = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    MillenniumTheme(dark) {
        MillenniumTooltipHost {
            val c = MillenniumTheme.colors
            BoxWithConstraints(Modifier.fillMaxSize().background(c.background)) {
                val wide = maxWidth >= 900.dp
                Column(Modifier.fillMaxSize()) {
                    Header(dark, { dark = it }, wide)
                    MillenniumSeparator()
                    Row(Modifier.weight(1f)) {
                        if (wide) Sidebar(category) { category = it }
                        BoxWithConstraints(Modifier.weight(1f).fillMaxHeight()) {
                            val compact = maxWidth < 620.dp
                            val scroll = rememberScrollState()
                            LaunchedEffect(category) { scroll.scrollTo(0) }
                            Column(
                                Modifier.fillMaxSize().verticalScroll(scroll)
                                    .padding(
                                        horizontal = if (compact) 20.dp else 36.dp,
                                        vertical = 30.dp
                                    ),
                                verticalArrangement = Arrangement.spacedBy(28.dp)
                            ) {
                                Hero(compact)
                                if (!wide) {
                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        categories.forEach { item ->
                                            MillenniumButton(
                                                { category = item },
                                                style = if (category == item) MillenniumButtonStyle.Primary else MillenniumButtonStyle.Secondary
                                            ) { Text(item) }
                                        }
                                    }
                                }
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        MillenniumText(
                                            if (category == "All components") "The component collection" else category,
                                            size = 22.sp,
                                            weight = FontWeight.SemiBold
                                        )
                                        MillenniumText(
                                            "Real controls. A little nostalgia. Entirely Compose.",
                                            color = c.muted,
                                            size = 13.sp
                                        )
                                    }
                                    if (!compact) Badge("20 COMPONENTS", c.muted)
                                }
                                Gallery(
                                    category,
                                    compact,
                                    { dialog = true },
                                    { modalSheet.targetDetent = SheetDetent.FullyExpanded },
                                    { drawer.targetValue = MillenniumDrawerValue.Open })
                                MillenniumSeparator()
                                FlowRow(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    MillenniumText(
                                        "Millennium / Built on Compose Unstyled",
                                        color = c.muted,
                                        size = 12.sp
                                    )
                                    MillenniumText(
                                        "Android · iOS · Desktop · Web",
                                        color = c.muted,
                                        size = 12.sp
                                    )
                                    MillenniumText(
                                        "Not a screenshot. Try everything.",
                                        color = c.muted,
                                        size = 12.sp
                                    )
                                }
                            }
                            MillenniumScrollbar(
                                scroll,
                                Modifier.align(Alignment.CenterEnd).fillMaxHeight()
                                    .padding(vertical = 12.dp)
                            )
                        }
                    }
                }
                MillenniumDialog(dialog, { dialog = false }, "Good design never gets old.") {
                    MillenniumText(
                        "The familiar warmth of 2007, with room for what comes next. This dialog keeps focus inside and dismisses with Escape or Back.",
                        color = c.muted
                    )
                    MillenniumSeparator()
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MillenniumButton({ dialog = false }) { Text("Sounds good") }
                        MillenniumButton(
                            { dialog = false },
                            style = MillenniumButtonStyle.Secondary
                        ) { Text("Close") }
                    }
                }
                MillenniumModalBottomSheet(modalSheet) {
                    MillenniumIcon(
                        MillenniumIcons.Music,
                        modifier = Modifier.size(32.dp),
                        tint = c.accent
                    )
                    MillenniumText(
                        "Your next favorite track",
                        size = 24.sp,
                        weight = FontWeight.SemiBold
                    )
                    MillenniumText(
                        "A modal sheet with a real drag handle, scrim, and native dismissal behavior.",
                        color = c.muted
                    )
                    MillenniumProgress(0.68f)
                    MillenniumButton({
                        modalSheet.targetDetent = SheetDetent.Hidden
                    }) { Text("Back to the collection") }
                }
                MillenniumDrawer(drawer) {
                    Brand()
                    MillenniumSeparator()
                    MillenniumText(
                        "A little more space",
                        size = 24.sp,
                        weight = FontWeight.SemiBold
                    )
                    MillenniumText(
                        "An edge-attached drawer, themed in smoked glass and pearl. Drag it closed, tap outside, or press Escape.",
                        color = c.muted
                    )
                    categories.forEach { item ->
                        MillenniumButton(
                            {
                                category = item; drawer.targetValue = MillenniumDrawerValue.Closed
                            },
                            style = MillenniumButtonStyle.Secondary,
                            modifier = Modifier.fillMaxWidth()
                        ) { Text(item) }
                    }
                    MillenniumButton({
                        drawer.targetValue = MillenniumDrawerValue.Closed
                    }) { Text("Close drawer") }
                }
            }
        }
    }
}

@Composable
private fun Brand() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                .background(
                    gelBrush(
                        MillenniumTheme.colors.accentTop,
                        MillenniumTheme.colors.accentBottom
                    )
                )
                .border(1.dp, MillenniumTheme.colors.accentBottom, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            MillenniumIcon(MillenniumIcons.Sparkle, tint = Color.White)
        }
        MillenniumText("Millennium", size = 24.sp, weight = FontWeight.Bold)
    }
}

@Composable
private fun Header(dark: Boolean, onDarkChange: (Boolean) -> Unit, wide: Boolean) {
    val c = MillenniumTheme.colors
    Row(
        Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(c.raised, c.surface)))
            .padding(horizontal = if (wide) 28.dp else 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Brand()
        if (wide) {
            MillenniumSeparator(Modifier.height(24.dp), vertical = true)
            MillenniumText(
                "A familiar feeling. A fresh perspective.",
                color = c.muted,
                size = 13.sp
            )
        }
        Spacer(Modifier.weight(1f))
        MillenniumSwitch(dark, onDarkChange, if (dark) "Dark" else "Light")
    }
}

@Composable
private fun Sidebar(category: String, onSelect: (String) -> Unit) {
    val c = MillenniumTheme.colors
    Column(
        Modifier.width(212.dp).fillMaxHeight().background(c.surface.copy(alpha = 0.7f))
            .verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MillenniumText(
            "EXPLORER",
            size = 10.sp,
            weight = FontWeight.Bold,
            color = c.muted,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        categories.forEachIndexed { index, item ->
            MillenniumButton(
                { onSelect(item) },
                modifier = Modifier.fillMaxWidth(),
                style = if (category == item) MillenniumButtonStyle.Primary else MillenniumButtonStyle.Quiet
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(item, fontSize = 13.sp)
                    Text(
                        if (index == 0) "20" else listOf("", "2", "6", "2", "6", "4")[index],
                        fontSize = 11.sp
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        MillenniumSeparator()
        MillenniumText(
            "THE DESIGN LANGUAGE",
            size = 10.sp,
            weight = FontWeight.Bold,
            color = c.muted,
            modifier = Modifier.padding(top = 16.dp)
        )
        listOf(
            "01   Light you can feel",
            "02   Depth with purpose",
            "03   Familiar, not dated"
        ).forEach {
            MillenniumText(
                it,
                color = c.muted,
                size = 12.sp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
        Spacer(Modifier.height(24.dp))
        MillenniumCard(contentPadding = PaddingValues(14.dp)) {
            MillenniumIcon(MillenniumIcons.Sparkle, tint = c.accent)
            MillenniumText("2007 soul.\n2026 standards.", weight = FontWeight.SemiBold)
            MillenniumText(
                "Accessible primitives.\nMultiplatform by nature.",
                color = c.muted,
                size = 11.sp
            )
        }
        MillenniumText(
            "EDITION 001 / 2026",
            color = c.muted,
            size = 10.sp,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

@Composable
private fun Hero(compact: Boolean) {
    val c = MillenniumTheme.colors
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Badge("THE NEXT WAVE OF NOSTALGIA", c.accent)
            MillenniumText(
                "Yesterday’s charm.\nTomorrow’s interface.",
                size = if (compact) 32.sp else 40.sp,
                weight = FontWeight.Bold
            )
            MillenniumText(
                "A Compose design system with a 2007 heartbeat.\nGlossy surfaces, tactile details, and nothing holding you back.",
                color = c.muted,
                size = 14.sp
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Badge("COMPOSE UNSTYLED", c.muted)
                Badge("LIGHT + DARK", c.muted)
            }
        }
        if (!compact) {
            Box(
                Modifier.size(174.dp).clip(CircleShape).background(
                    Brush.radialGradient(
                        listOf(
                            c.accentTop.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                ), contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier.size(116.dp).clip(CircleShape).background(
                        gelBrush(
                            c.accentTop,
                            c.accentBottom
                        )
                    ).border(1.dp, c.accentBottom, CircleShape), contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier.align(Alignment.TopCenter).padding(top = 7.dp).size(88.dp, 48.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.45f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    MillenniumIcon(
                        MillenniumIcons.Sparkle,
                        modifier = Modifier.size(48.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun Badge(text: String, color: Color) {
    MillenniumText(
        text, color = color, size = 9.sp, weight = FontWeight.Bold,
        modifier = Modifier.clip(RoundedCornerShape(5.dp)).background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.18f), RoundedCornerShape(5.dp))
            .padding(horizontal = 9.dp, vertical = 5.dp)
    )
}

@Composable
private fun Gallery(
    category: String,
    compact: Boolean,
    openDialog: () -> Unit,
    openSheet: () -> Unit,
    openDrawer: () -> Unit
) {
    val stateHolder = rememberSaveableStateHolder()
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val columns = if (compact) 1 else if (maxWidth >= 1100.dp) 3 else 2
        val cardWidth = (maxWidth - 20.dp * (columns - 1)) / columns
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            maxItemsInEachRow = columns
        ) {
            @Composable
            fun Card(
                group: String,
                number: String,
                title: String,
                description: String,
                content: @Composable ColumnScope.() -> Unit
            ) {
                if (category == "All components" || category == group) {
                    MillenniumCard(
                        Modifier.width(cardWidth),
                        contentPadding = PaddingValues(22.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MillenniumText(title, size = 17.sp, weight = FontWeight.SemiBold)
                            MillenniumText(
                                number,
                                size = 11.sp,
                                color = MillenniumTheme.colors.muted
                            )
                        }
                        MillenniumText(
                            description,
                            size = 12.sp,
                            color = MillenniumTheme.colors.muted
                        )
                        MillenniumSeparator()
                        stateHolder.SaveableStateProvider(title) { content() }
                    }
                }
            }
            Card("Actions", "01", "Buttons", "A little gloss. A satisfying click.") { ButtonDemo() }
            Card(
                "Inputs",
                "02–04",
                "Selection controls",
                "Small details. Clear decisions."
            ) { SelectionDemo() }
            Card(
                "Inputs",
                "05",
                "Text fields",
                "A welcoming place for your thoughts."
            ) { TextFieldDemo() }
            Card(
                "Inputs",
                "06–07",
                "Sliders & radio groups",
                "Find exactly the right setting."
            ) { RangeDemo() }
            Card("Navigation", "08", "Tab group", "One place. A few perspectives.") { TabsDemo() }
            Card(
                "Feedback",
                "09–10",
                "Avatars & icons",
                "A familiar face. A recognizable sign."
            ) { IdentityDemo() }
            Card(
                "Feedback",
                "11",
                "Progress indicators",
                "Make the waiting feel lighter."
            ) { ProgressDemo() }
            Card(
                "Actions",
                "12",
                "Disclosure",
                "A little more, only when you need it."
            ) { DisclosureDemo() }
            Card(
                "Navigation",
                "13",
                "Dropdown menu",
                "Everything, right where you expect it."
            ) { MenuDemo() }
            Card(
                "Feedback",
                "14–15",
                "Scrollbars & separators",
                "Subtle structure. Tactile navigation."
            ) { ScrollDemo() }
            Card("Feedback", "16", "Tooltip", "Helpful words, without the clutter.") {
                MillenniumTooltip("A hint of the past. Built for today.") {
                    MillenniumButton(openDialog, style = MillenniumButtonStyle.Secondary) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MillenniumIcon(MillenniumIcons.Sparkle)
                            Text("Hover, focus or hold")
                        }
                    }
                }
                MillenniumText(
                    "Mouse hover · keyboard focus · long press",
                    size = 11.sp,
                    color = MillenniumTheme.colors.muted
                )
            }
            Card(
                "Overlays",
                "17–19",
                "Dialog, drawer & modal sheet",
                "A focused moment, beautifully framed."
            ) {
                MillenniumButton(
                    openDialog,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Open dialog") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    MillenniumButton(
                        openDrawer,
                        modifier = Modifier.weight(1f),
                        style = MillenniumButtonStyle.Secondary
                    ) { Text("Drawer") }
                    MillenniumButton(
                        openSheet,
                        modifier = Modifier.weight(1f),
                        style = MillenniumButtonStyle.Secondary
                    ) { Text("Modal sheet") }
                }
            }
            Card(
                "Overlays",
                "20",
                "Bottom sheet",
                "Slide into something a little more useful."
            ) { BottomSheetDemo() }
        }
    }
}

@Composable
private fun ButtonDemo() {
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
private fun SelectionDemo() {
    var first by rememberSaveable { mutableStateOf(true) }
    var second by rememberSaveable { mutableStateOf(false) }
    var enabled by rememberSaveable { mutableStateOf(true) }
    val parent = when {
        first && second -> ToggleableState.On; !first && !second -> ToggleableState.Off; else -> ToggleableState.Indeterminate
    }
    MillenniumTriStateCheckbox(
        parent,
        { val next = parent != ToggleableState.On; first = next; second = next },
        "The whole collection"
    )
    Column(Modifier.padding(start = 16.dp)) {
        MillenniumCheckbox(first, { first = it }, "A little nostalgia")
        MillenniumCheckbox(second, { second = it }, "A little extra shine")
    }
    MillenniumSeparator()
    MillenniumSwitch(enabled, { enabled = it }, "Keep the good vibes on")
}

@Composable
private fun TextFieldDemo() {
    val name = rememberTextFieldState("Alex")
    val email = rememberTextFieldState()
    MillenniumTextField(name, "Display name", placeholder = "Your name")
    MillenniumTextField(
        email, "Email address", placeholder = "you@the-internet.com",
        error = if (email.text.isNotEmpty() && '@' !in email.text) "Add an @ to complete your email." else null
    )
}

@Composable
private fun RangeDemo() {
    var volume by rememberSaveable { mutableFloatStateOf(0.65f) }
    var quality by rememberSaveable { mutableStateOf("Original") }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        MillenniumText("Turn up the nostalgia")
        MillenniumText(
            "${(volume * 100).toInt()}%",
            color = MillenniumTheme.colors.accent,
            weight = FontWeight.SemiBold
        )
    }
    MillenniumSlider(volume, { volume = it }, "Volume")
    MillenniumRadioGroup(
        quality,
        { quality = it },
        listOf("Original", "Remastered"),
        "Playback quality"
    )
}

@Composable
private fun TabsDemo() {
    var tab by rememberSaveable { mutableStateOf("Discover") }
    MillenniumTabGroup(tab, { tab = it }, listOf("Discover", "Saved")) { selected ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MillenniumAvatar(if (selected == "Discover") "07" else "26", "Playlist cover")
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                MillenniumText(
                    if (selected == "Discover") "The golden hour mix" else "Your timeless favorites",
                    weight = FontWeight.SemiBold
                )
                MillenniumText(
                    if (selected == "Discover") "12 tracks · a simpler time" else "8 tracks · always with you",
                    size = 12.sp,
                    color = MillenniumTheme.colors.muted
                )
            }
        }
    }
}

@Composable
private fun IdentityDemo() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MillenniumAvatar("AJ", "Alex Jordan", size = 36.dp)
        MillenniumAvatar("MK", "Morgan Kim")
        MillenniumAvatar("YOU", "Your profile", size = 60.dp)
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MillenniumIcon(MillenniumIcons.Sparkle, "Sparkle", tint = MillenniumTheme.colors.accent)
        MillenniumIcon(MillenniumIcons.Music, "Music")
        MillenniumIcon(MillenniumIcons.Check, "Check", tint = MillenniumTheme.colors.success)
        MillenniumText(
            "Vector sharp. Any screen.",
            size = 11.sp,
            color = MillenniumTheme.colors.muted
        )
    }
}

@Composable
private fun ProgressDemo() {
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
private fun DisclosureDemo() {
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
private fun MenuDemo() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var choice by rememberSaveable { mutableStateOf("Choose your mood") }
    MillenniumDropdownMenu(
        expanded,
        { expanded = it },
        listOf("Golden hour", "Midnight drive", "Back to 2007"),
        { choice = it },
        anchor = {
            MillenniumButton(
                { expanded = !expanded },
                style = MillenniumButtonStyle.Secondary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(choice)
                    MillenniumIcon(MillenniumIcons.Chevron)
                }
            }
        })
    MillenniumText(
        "Arrow keys to explore. Enter to choose.",
        color = MillenniumTheme.colors.muted,
        size = 11.sp
    )
}

@Composable
private fun ScrollDemo() {
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

@Composable
private fun BottomSheetDemo() {
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
