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
internal fun TabsDemo() {
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
internal fun SidebarDemo() {
    var collapsed by rememberSaveable { mutableStateOf(false) }
    var libraryExpanded by rememberSaveable { mutableStateOf(true) }
    var workspaceExpanded by rememberSaveable { mutableStateOf(true) }
    var selected by rememberSaveable { mutableStateOf("Discover") }
    Row(
        Modifier.fillMaxWidth().height(360.dp).clip(RoundedCornerShape(12.dp))
            .background(MillenniumTheme.colors.inset)
    ) {
        MillenniumSidebar(
            modifier = Modifier.fillMaxHeight(),
            collapsed = collapsed,
            onCollapsedChange = { collapsed = it }
        ) {
            MillenniumSidebarHeader {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (collapsed) {
                        MillenniumIcon(
                            MillenniumIcons.Sparkle,
                            tint = MillenniumTheme.colors.accent
                        )
                    } else {
                        MillenniumText("Millennium", weight = FontWeight.Bold)
                    }
                    MillenniumSidebarTrigger {
                        MillenniumIconButton(
                            MillenniumIcons.Chevron,
                            style = MillenniumIconButtonStyle.Quiet,
                            modifier = Modifier.rotate(it.rotation),
                            contentDescription = "Toggle Sidebar",
                            onClick = { it.toggle() }
                        )
                    }
                }
            }
            MillenniumSidebarContent {
                MillenniumSidebarGroup(
                    expanded = libraryExpanded,
                    collapsible = true,
                    onExpandedChange = { libraryExpanded = it }
                ) {
                    MillenniumSidebarGroupLabel("LIBRARY")
                    MillenniumSidebarGroupContent {
                        MillenniumSidebarMenu {
                            MillenniumSidebarItem(
                                label = "Discover",
                                onClick = { selected = "Discover" },
                                selected = selected == "Discover",
                                icon = MillenniumIcons.Sparkle,
                                badge = "20"
                            )
                            MillenniumSidebarItem(
                                label = "Saved",
                                onClick = { selected = "Saved" },
                                selected = selected == "Saved",
                                icon = MillenniumIcons.Music,
                                badge = "8"
                            )
                            MillenniumSidebarItem(
                                label = "History",
                                onClick = { selected = "History" },
                                selected = selected == "History",
                                icon = MillenniumIcons.Check
                            )
                        }
                    }
                }
                MillenniumSidebarSeparator()
                MillenniumSidebarGroup(
                    expanded = workspaceExpanded,
                    collapsible = true,
                    onExpandedChange = { workspaceExpanded = it }
                ) {
                    MillenniumSidebarGroupLabel("WORKSPACE")
                    MillenniumSidebarGroupContent {
                        MillenniumSidebarMenu {
                            MillenniumSidebarItem(
                                label = "Playlists",
                                onClick = { selected = "Playlists" },
                                selected = selected == "Playlists",
                                icon = MillenniumIcons.Music
                            )
                            MillenniumSidebarItem(
                                label = "Shared",
                                onClick = { selected = "Shared" },
                                selected = selected == "Shared",
                                icon = MillenniumIcons.Sparkle
                            )
                        }
                    }
                }
            }
            MillenniumSidebarFooter {
                MillenniumText(
                    if (collapsed) "v1" else "Edition 001 / 2026",
                    color = MillenniumTheme.colors.muted,
                    size = 10.sp
                )
            }
        }
        Box(
            Modifier.weight(1f).fillMaxHeight().padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MillenniumText("Selected view", size = 11.sp, color = MillenniumTheme.colors.muted)
                MillenniumText(selected, size = 22.sp, weight = FontWeight.SemiBold)
                if (!collapsed) {
                    MillenniumText(
                        "Fold groups, collapse the rail, or pick a new view.",
                        size = 11.sp,
                        color = MillenniumTheme.colors.muted
                    )
                }
            }
        }
    }
}

@Composable
internal fun BottomNavigationDemo() {
    var destination by rememberSaveable { mutableStateOf("Discover") }
    Column(
        Modifier.fillMaxWidth().height(220.dp).clip(RoundedCornerShape(12.dp))
            .background(MillenniumTheme.colors.inset)
    ) {
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            MillenniumText(destination, size = 22.sp, weight = FontWeight.SemiBold)
        }
        MillenniumBottomNavigationBar(windowInsets = WindowInsets(0, 0, 0, 0)) {
            listOf("Discover", "Library", "Saved").forEachIndexed { index, label ->
                MillenniumBottomNavigationItem(
                    selected = destination == label,
                    onClick = { destination = label },
                    contentDescription = label,
                    icon = {
                        UnstyledIcon(
                            if (index == 0) MillenniumIcons.Sparkle else MillenniumIcons.Music,
                            contentDescription = null,
                            tint = LocalTextStyle.current.color
                        )
                    },
                    label = { Text(label) }
                )
            }
        }
    }
    MillenniumText(
        "Tab to focus · Enter to select. The bar stays outside the scrolling body.",
        size = 11.sp,
        color = MillenniumTheme.colors.muted
    )
}

@Composable
internal fun TopAppBarDemo() {
    MillenniumTopAppBar(
        title = { Text("Your library") },
        subtitle = { Text("128 tracks") },
        navigationIcon = {
            MillenniumIconButton(
                MillenniumIcons.Sparkle,
                "Home",
                {},
                style = MillenniumIconButtonStyle.Quiet
            )
        },
        actions = {
            MillenniumIconButton(
                MillenniumIcons.Music,
                "Play",
                {},
                style = MillenniumIconButtonStyle.Quiet
            )
            MillenniumIconButton(
                MillenniumIcons.Chevron,
                "More",
                {},
                style = MillenniumIconButtonStyle.Quiet
            )
        }
    )
}

@Composable
internal fun ScaffoldDemo() {
    var showDock by rememberSaveable { mutableStateOf(true) }
    Box(Modifier.fillMaxWidth().height(280.dp).clip(RoundedCornerShape(12.dp))) {
        MillenniumScaffold(
            title = { MillenniumText("Now playing") },
            leading = { MillenniumIcon(MillenniumIcons.Music) },
            actions = {
                MillenniumButton(
                    onClick = { showDock = !showDock },
                    style = MillenniumButtonStyle.Quiet
                ) { Text(if (showDock) "Hide dock" else "Show dock") }
            },
            modifier = Modifier.fillMaxSize()
        ) { inner ->
            Box(Modifier.fillMaxSize()) {
                Column(
                    Modifier.fillMaxSize().padding(inner).padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) {
                        MillenniumText("Track ${it + 1}", size = 13.sp)
                    }
                }
                if (showDock) {
                    MillenniumScaffoldPadding(PaddingValues(bottom = 56.dp)) {
                        Box(Modifier.fillMaxSize()) {
                            Box(
                                Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(56.dp)
                                    .background(MillenniumTheme.colors.surface)
                            ) {
                                MillenniumText(
                                    "Fixed dock reserves bottom space",
                                    Modifier.align(Alignment.Center),
                                    size = 11.sp,
                                    color = MillenniumTheme.colors.muted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    MillenniumText(
        "Toggle the dock to add and remove the global padding it reserves.",
        size = 11.sp,
        color = MillenniumTheme.colors.muted
    )
}

@Composable
internal fun ListTileDemo() {
    var selected by rememberSaveable { mutableStateOf("Air") }
    var rightClick by rememberSaveable { mutableStateOf("") }
    val tracks = listOf(
        Triple("Air", "La Femme d'Argent", "7:09"),
        Triple("A-ha", "Take On Me", "3:46"),
        Triple("Björk", "Jóga", "5:05"),
    )
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        tracks.forEach { (artist, song, length) ->
            MillenniumListTile(
                title = { Text(song) },
                leading = { MillenniumAvatar(artist.take(2).uppercase(), artist) },
                subtitle = { Text("$artist · remastered") },
                trailing = {
                    MillenniumText(
                        length,
                        color = MillenniumTheme.colors.muted,
                        size = 11.sp
                    )
                },
                selected = selected == artist,
                onClick = { selected = artist },
                onRightClick = { offset ->
                    rightClick = "Right-clicked “$song” at ${offset.x.toInt()}, ${offset.y.toInt()}"
                },
            )
        }
    }
    MillenniumText(
        rightClick.ifEmpty { "Click to select · right-click for positional context." },
        size = 11.sp,
        color = MillenniumTheme.colors.muted
    )
}
