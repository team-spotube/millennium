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

private data class Mood(val name: String, val hint: String, val icon: ImageVector)

@Composable
internal fun MenuDemo() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var choice by rememberSaveable { mutableStateOf("") }
    var lyrics by rememberSaveable { mutableStateOf(true) }
    var crossfade by rememberSaveable { mutableStateOf(false) }
    var quality by rememberSaveable { mutableStateOf("Lossless") }
    val filter = rememberTextFieldState()
    LaunchedEffect(expanded) { if (!expanded) filter.clearText() }
    val moods = remember {
        listOf(
            Mood("Golden hour", "Warm, unhurried", MillenniumIcons.Sparkle),
            Mood("Midnight drive", "Neon and asphalt", MillenniumIcons.Music),
            Mood("Back to 2007", "Glossy nostalgia", MillenniumIcons.Sparkle),
            Mood("Rainy window", "Slow and soft", MillenniumIcons.Music),
            Mood("Road trip", "Windows down", MillenniumIcons.Sparkle),
            Mood("Sunday morning", "Coffee and vinyl", MillenniumIcons.Music),
            Mood("Late night coding", "Quiet focus", MillenniumIcons.Sparkle),
            Mood("Beach bonfire", "Acoustic embers", MillenniumIcons.Music),
        )
    }
    val query = filter.text.toString().trim()
    val visible = moods.filter { it.name.contains(query, ignoreCase = true) }
    val current = moods.firstOrNull { it.name == choice }
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = spring(dampingRatio = 0.68f, stiffness = Spring.StiffnessMediumLow),
    )
    MillenniumDropdownMenu(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        width = 248.dp,
        anchor = {
            MillenniumButton(
                { expanded = !expanded },
                style = MillenniumButtonStyle.Secondary,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MillenniumIcon(current?.icon ?: MillenniumIcons.Music)
                        Text(current?.name ?: "Choose your mood")
                    }
                    MillenniumIcon(
                        MillenniumIcons.Chevron,
                        modifier = Modifier.rotate(chevronRotation)
                    )
                }
            }
        },
    ) {
        MillenniumDropdownMenuHeader {
            MillenniumDropdownMenuFilter(filter, placeholder = "Search moods")
        }
        MillenniumDropdownMenuContent(maximumHeight = 280.dp) {
            MillenniumDropdownMenuGroup {
                MillenniumDropdownMenuLabel {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("MOODS")
                        Text("${visible.size}")
                    }
                }
                if (visible.isEmpty()) {
                    MillenniumDropdownMenuEmpty { Text("No mood matches “$query”") }
                }
                visible.forEach { mood ->
                    MillenniumDropdownMenuItem(
                        label = mood.name,
                        onClick = { choice = mood.name },
                        icon = mood.icon,
                        description = mood.hint,
                        selected = choice == mood.name,
                    )
                }
            }
            MillenniumDropdownMenuSeparator()
            MillenniumDropdownMenuGroup {
                MillenniumDropdownMenuLabel("PLAYBACK")
                MillenniumDropdownMenuCheckboxItem(
                    lyrics, { lyrics = it },
                    shortcut = { Text("L") },
                ) { Text("Show lyrics") }
                MillenniumDropdownMenuCheckboxItem(
                    crossfade, { crossfade = it },
                    description = { Text("Blend tracks for 6 seconds") },
                ) { Text("Crossfade") }
            }
            MillenniumDropdownMenuSeparator()
            MillenniumDropdownMenuRadioGroup(quality, { quality = it }) {
                MillenniumDropdownMenuLabel("STREAMING QUALITY")
                listOf("Standard", "High", "Lossless").forEach { level ->
                    MillenniumDropdownMenuRadioItem(
                        level,
                        trailing = if (level == "Lossless") {
                            { MillenniumSidebarBadge("HD", highlighted = quality == level) }
                        } else null,
                    ) { Text(level) }
                }
            }
            MillenniumDropdownMenuSeparator()
            MillenniumDropdownMenuItem(
                label = "Clear mood",
                onClick = { choice = "" },
                icon = MillenniumIcons.Check,
                destructive = true,
                enabled = choice.isNotEmpty(),
            )
        }
        MillenniumDropdownMenuFooter {
            MillenniumText(
                "${if (lyrics) "Lyrics on" else "Lyrics off"} · $quality",
                color = MillenniumTheme.colors.muted,
                size = 11.sp
            )
        }
    }
    MillenniumText(
        "Header, scrolling body, footer · icons, toggles, radios and a destructive row.",
        color = MillenniumTheme.colors.muted,
        size = 11.sp
    )
}
