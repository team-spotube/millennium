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
internal fun TextFieldDemo() {
    val name = rememberTextFieldState("Alex")
    val email = rememberTextFieldState()
    MillenniumTextField(name, "Display name", placeholder = "Your name")
    MillenniumTextField(
        email, "Email address", placeholder = "you@the-internet.com",
        error = if (email.text.isNotEmpty() && '@' !in email.text) "Add an @ to complete your email." else null
    )
}

@Composable
internal fun AutocompleteDemo() {
    val artists = listOf(
        "A-ha", "Air", "Al Green", "Amy Winehouse", "Björk", "Daft Punk", "Enya",
        "Fleetwood Mac", "Massive Attack", "Portishead", "The Cure", "The xx",
    )
    val state = rememberTextFieldState()
    var selectedArtist by rememberSaveable { mutableStateOf("") }
    var favorites by remember { mutableStateOf(emptySet<String>()) }
    val query = state.text.toString()
    val suggestions = if (query.isBlank()) emptyList() else artists
        .filter { it.contains(query, ignoreCase = true) }
        .take(6)

    MillenniumAutocomplete(
        state = state,
        label = "Find an artist",
        placeholder = "Try “da”, “the” or “air”",
        suggestions = suggestions,
        suggestionLabel = { it },
        onSuggestionSelected = { selectedArtist = it },
        leadingContent = { artist ->
            MillenniumAvatar(
                artist.take(2).uppercase(),
                artist,
                size = 30.dp
            )
        },
        trailingContent = { artist ->
            MillenniumButton(
                onClick = {
                    favorites = if (artist in favorites) favorites - artist else favorites + artist
                },
                style = MillenniumButtonStyle.Quiet,
            ) { Text(if (artist in favorites) "Saved" else "Save") }
        },
        menuHeader = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MillenniumText("MATCHING ARTISTS", size = 10.sp, weight = FontWeight.Bold)
                MillenniumText(
                    "${suggestions.size} found",
                    size = 10.sp,
                    color = MillenniumTheme.colors.muted
                )
            }
        },
        menuFooter = {
            MillenniumText(
                "↑ ↓ move   ·   Enter choose   ·   Esc close",
                size = 10.sp,
                color = MillenniumTheme.colors.muted
            )
        },
        emptyContent = {
            MillenniumText(
                "No artists found. Try another search.",
                color = MillenniumTheme.colors.muted
            )
        },
    )
    MillenniumText(
        if (selectedArtist.isEmpty()) "Keep typing to search the collection." else "Selected: $selectedArtist",
        color = MillenniumTheme.colors.muted,
        size = 11.sp,
    )
}

@Composable
internal fun SegmentedButtonDemo() {
    var range by rememberSaveable { mutableStateOf(0) }
    val ranges = listOf("Day", "Week", "Month")
    MillenniumSegmentedButtonRow {
        ranges.forEachIndexed { index, label ->
            MillenniumSegmentedButton(
                selected = range == index,
                onClick = { range = index },
                shape = MillenniumSegmentedButtonDefaults.itemShape(index, ranges.size)
            ) { Text(label) }
        }
    }
    MillenniumText(
        "Selected · ${ranges[range]}",
        size = 11.sp,
        color = MillenniumTheme.colors.muted
    )
}


@Composable
internal fun CheckboxDemo() {
    var nostalgia by rememberSaveable { mutableStateOf(true) }
    var shine by rememberSaveable { mutableStateOf(false) }
    MillenniumCheckbox(nostalgia, { nostalgia = it }, "A little nostalgia")
    MillenniumCheckbox(shine, { shine = it }, "A little extra shine")
    MillenniumCheckbox(true, {}, "Not available right now", enabled = false)
    DemoHint("Space toggles when focused.")
}

@Composable
internal fun TriStateCheckboxDemo() {
    var first by rememberSaveable { mutableStateOf(true) }
    var second by rememberSaveable { mutableStateOf(false) }
    val parent = when {
        first && second -> ToggleableState.On
        !first && !second -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
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
    DemoHint("The parent reflects its children: on, off or in between.")
}

@Composable
internal fun SwitchDemo() {
    var vibes by rememberSaveable { mutableStateOf(true) }
    var gloss by rememberSaveable { mutableStateOf(false) }
    MillenniumSwitch(vibes, { vibes = it }, "Keep the good vibes on")
    MillenniumSwitch(gloss, { gloss = it }, "Extra gloss")
    DemoHint("Drag or tap the thumb.")
}

@Composable
internal fun SliderDemo() {
    var volume by rememberSaveable { mutableFloatStateOf(0.65f) }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        MillenniumText("Turn up the nostalgia")
        MillenniumText(
            "${(volume * 100).toInt()}%",
            color = MillenniumTheme.colors.accent,
            weight = FontWeight.SemiBold
        )
    }
    MillenniumSlider(volume, { volume = it }, "Volume")
    DemoHint("Arrow keys nudge the value.")
}

@Composable
internal fun RadioGroupDemo() {
    var quality by rememberSaveable { mutableStateOf("Original") }
    MillenniumRadioGroup(
        quality,
        { quality = it },
        listOf("Original", "Remastered", "Live"),
        "Playback quality"
    )
    DemoHint("Selected · $quality")
}
