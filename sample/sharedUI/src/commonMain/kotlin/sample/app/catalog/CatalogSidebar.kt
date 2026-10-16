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

/** The catalog's own navigation: a flat, searchable, alphabetical list of every component. */
@Composable
internal fun CatalogSidebar(
    selectedId: String,
    onSelect: (String) -> Unit,
    search: TextFieldState,
    modifier: Modifier = Modifier,
) {
    MillenniumSidebar(modifier = modifier.fillMaxHeight()) {
        MillenniumSidebarHeader { Brand() }
        MillenniumSidebarContent { CatalogNavigation(selectedId, onSelect, search) }
        MillenniumSidebarFooter {
            MillenniumText(
                "${catalogEntries.size} COMPONENTS · EDITION 001 / 2026",
                color = MillenniumTheme.colors.muted,
                size = 10.sp
            )
        }
    }
}

/** Search field, Overview and one row per component. Usable inside a sidebar or a drawer. */
@Composable
internal fun CatalogNavigation(
    selectedId: String,
    onSelect: (String) -> Unit,
    search: TextFieldState,
) {
    val query = search.text.toString().trim()
    val visible = catalogEntries.filter { it.title.contains(query, ignoreCase = true) }
    MillenniumTextField(search, placeholder = "Search components", modifier = Modifier.fillMaxWidth())
    MillenniumSidebarMenu {
        MillenniumSidebarItem(
            label = "Overview",
            onClick = { onSelect(OverviewId) },
            selected = selectedId == OverviewId,
            icon = MillenniumIcons.Sparkle,
        )
    }
    MillenniumSidebarGroup {
        MillenniumSidebarGroupLabel(
            "COMPONENTS",
            trailing = {
                MillenniumSidebarBadge(
                    if (query.isEmpty()) "${catalogEntries.size}" else "${visible.size}/${catalogEntries.size}"
                )
            },
        )
        MillenniumSidebarMenu {
            visible.forEach { entry ->
                MillenniumSidebarItem(
                    label = entry.title,
                    onClick = { onSelect(entry.id) },
                    selected = selectedId == entry.id,
                )
            }
            if (visible.isEmpty()) {
                MillenniumText(
                    "No component matches “$query”.",
                    color = MillenniumTheme.colors.muted,
                    size = 12.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }
        }
    }
}
