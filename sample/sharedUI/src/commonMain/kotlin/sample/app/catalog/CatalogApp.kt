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

/** The component catalog: header, a flat searchable sidebar, and one page per component. */
@Composable
fun CatalogApp(onOpenStudy: () -> Unit) {
    var dark by rememberSaveable { mutableStateOf(false) }
    var palette by rememberSaveable { mutableStateOf(MillenniumPalette.Blue) }
    var selectedId by rememberSaveable { mutableStateOf(OverviewId) }
    val search = rememberTextFieldState()
    val navigationDrawer = rememberMillenniumDrawerState()
    val overlays = rememberDemoOverlays()
    MillenniumTheme(dark, config = MillenniumThemeConfig(palette = palette)) {
        CompositionLocalProvider(LocalDemoOverlays provides overlays) {
        MillenniumTooltipHost {
            val c = MillenniumTheme.colors
            BoxWithConstraints(Modifier.fillMaxSize().background(c.background)) {
                val wide = maxWidth >= 900.dp
                val compact = maxWidth < 620.dp
                Column(Modifier.fillMaxSize()) {
                    CatalogHeader(
                        dark, { dark = it }, palette, { palette = it }, wide,
                        onOpenNavigation = { navigationDrawer.targetValue = MillenniumDrawerValue.Open },
                        onOpenStudy = onOpenStudy,
                    )
                    MillenniumSeparator()
                    Row(Modifier.weight(1f)) {
                        if (wide) CatalogSidebar(selectedId, { selectedId = it }, search)
                        CatalogContent(
                            selectedId, { selectedId = it },
                            compact = compact,
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                        )
                    }
                }
                MillenniumDrawer(navigationDrawer) {
                    Brand()
                    MillenniumSeparator()
                    Column(
                        Modifier.weight(1f).verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CatalogNavigation(
                            selectedId,
                            { selectedId = it; navigationDrawer.targetValue = MillenniumDrawerValue.Closed },
                            search,
                        )
                    }
                }
                DemoOverlayHost(overlays)
            }
        }
        }
    }
}
