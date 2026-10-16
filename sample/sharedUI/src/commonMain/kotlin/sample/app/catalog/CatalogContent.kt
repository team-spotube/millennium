package sample.app.catalog

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.composeunstyled.*
import org.spotube.millennium.composables.*
import org.spotube.millennium.theme.MillenniumPalette
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumThemeConfig
import sample.app.catalog.demos.*

/** The right-hand pane: the overview grid or a single component page. */
@Composable
internal fun CatalogContent(
    selectedId: String,
    onSelect: (String) -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    val c = MillenniumTheme.colors
    val scroll = rememberScrollState()
    val stateHolder = rememberSaveableStateHolder()
    LaunchedEffect(selectedId) { scroll.scrollTo(0) }
    val entry = catalogEntry(selectedId)
    Box(modifier) {
        Column(
            Modifier.fillMaxSize().verticalScroll(scroll)
                .padding(horizontal = if (compact) 20.dp else 36.dp, vertical = 30.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            if (entry == null) {
                Overview(compact, onSelect)
            } else {
                ComponentPage(entry, compact, onSelect) { content ->
                    stateHolder.SaveableStateProvider(entry.id) { content() }
                }
            }
            MillenniumSeparator()
            FlowRow(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MillenniumText("Millennium / Built on Compose Unstyled", color = c.muted, size = 12.sp)
                MillenniumText("Android · iOS · Desktop · Web", color = c.muted, size = 12.sp)
                MillenniumText("Not a screenshot. Try everything.", color = c.muted, size = 12.sp)
            }
        }
        MillenniumScrollbar(
            scroll,
            Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(vertical = 12.dp)
        )
    }
}

@Composable
private fun Overview(compact: Boolean, onSelect: (String) -> Unit) {
    val c = MillenniumTheme.colors
    Hero(compact)
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            MillenniumText("The component collection", size = 22.sp, weight = FontWeight.SemiBold)
            MillenniumText(
                "Real controls. A little nostalgia. Entirely Compose.",
                color = c.muted,
                size = 13.sp
            )
        }
        if (!compact) Badge("${catalogEntries.size} COMPONENTS", c.muted)
    }
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val columns = if (compact) 1 else if (maxWidth >= 1100.dp) 3 else 2
        val tileWidth = (maxWidth - 16.dp * (columns - 1)) / columns
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            maxItemsInEachRow = columns
        ) {
            catalogEntries.forEach { entry ->
                MillenniumCard(Modifier.width(tileWidth), contentPadding = PaddingValues(6.dp)) {
                    MillenniumListTile(
                        title = { Text(entry.title) },
                        subtitle = { Text(entry.description) },
                        trailing = {
                            MillenniumIcon(
                                MillenniumIcons.Chevron,
                                modifier = Modifier.rotate(-90f),
                                tint = c.muted
                            )
                        },
                        onClick = { onSelect(entry.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ComponentPage(
    entry: CatalogEntry,
    compact: Boolean,
    onSelect: (String) -> Unit,
    scoped: @Composable (@Composable () -> Unit) -> Unit,
) {
    val c = MillenniumTheme.colors
    val index = catalogEntries.indexOf(entry)
    val previous = catalogEntries.getOrNull(index - 1)
    val next = catalogEntries.getOrNull(index + 1)
    Column(Modifier.widthIn(max = 760.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MillenniumButton({ onSelect(OverviewId) }, style = MillenniumButtonStyle.Quiet) { Text("Components") }
                MillenniumText("/", color = c.muted)
                MillenniumText(entry.title, color = c.muted)
            }
            MillenniumText(entry.title, size = if (compact) 28.sp else 34.sp, weight = FontWeight.Bold)
            MillenniumText(entry.description, color = c.muted, size = 14.sp)
        }
        MillenniumCard(Modifier.fillMaxWidth(), contentPadding = PaddingValues(if (compact) 18.dp else 26.dp)) {
            scoped {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) { entry.content(this) }
            }
        }
        if (entry.parts.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                MillenniumText("BUILDING BLOCKS", size = 11.sp, weight = FontWeight.Bold, color = c.muted)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) { entry.parts.forEach { Badge(it, c.accent) } }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            if (previous != null) {
                MillenniumButton({ onSelect(previous.id) }, style = MillenniumButtonStyle.Secondary) {
                    Text("← ${previous.title}")
                }
            } else Spacer(Modifier)
            if (next != null) {
                MillenniumButton({ onSelect(next.id) }, style = MillenniumButtonStyle.Secondary) {
                    Text("${next.title} →")
                }
            }
        }
    }
}
