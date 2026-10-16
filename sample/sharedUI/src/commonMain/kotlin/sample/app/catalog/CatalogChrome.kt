package sample.app.catalog

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
internal fun Brand() {
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
internal fun Hero(compact: Boolean) {
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
internal fun Badge(text: String, color: Color) {
    MillenniumText(
        text, color = color, size = 9.sp, weight = FontWeight.Bold,
        modifier = Modifier.clip(RoundedCornerShape(5.dp)).background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.18f), RoundedCornerShape(5.dp))
            .padding(horizontal = 9.dp, vertical = 5.dp)
    )
}

internal fun paletteBlurb(palette: MillenniumPalette): String = when (palette) {
    MillenniumPalette.Blue -> "Classic Aqua gel"
    MillenniumPalette.Orange -> "Warm apricot glow"
    MillenniumPalette.Lime -> "Fresh citron shine"
}

internal fun paletteSwatch(palette: MillenniumPalette): Color = when (palette) {
    MillenniumPalette.Blue -> Color(0xFF2F7DE1)
    MillenniumPalette.Orange -> Color(0xFFF08A24)
    MillenniumPalette.Lime -> Color(0xFF7BC62D)
}

@Composable
internal fun CatalogHeader(
    dark: Boolean,
    onDarkChange: (Boolean) -> Unit,
    palette: MillenniumPalette,
    onPaletteChange: (MillenniumPalette) -> Unit,
    wide: Boolean,
    onOpenNavigation: () -> Unit,
    onOpenStudy: () -> Unit,
) {
    val c = MillenniumTheme.colors
    var paletteMenuExpanded by remember { mutableStateOf(false) }
    Row(
        Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(c.raised, c.surface)))
            .padding(horizontal = if (wide) 28.dp else 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (wide) 24.dp else 12.dp)
    ) {
        if (!wide) {
            MillenniumIconButton(
                MillenniumIcons.Chevron,
                "Browse components",
                onOpenNavigation,
                style = MillenniumIconButtonStyle.Secondary,
                modifier = Modifier.rotate(-90f)
            )
        }
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
        if (wide) {
            MillenniumButton(onOpenStudy, style = MillenniumButtonStyle.Quiet) { Text("Design study") }
        }
        MillenniumDropdownMenu(
            expanded = paletteMenuExpanded,
            onExpandedChange = { paletteMenuExpanded = it },
            width = 200.dp,
            alignment = AnchorAlignment.End,
            anchor = {
                MillenniumButton(
                    onClick = { paletteMenuExpanded = !paletteMenuExpanded },
                    style = MillenniumButtonStyle.Secondary,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MillenniumIcon(MillenniumIcons.Sparkle)
                        Text(if (wide) "${palette.name} palette" else palette.name)
                    }
                }
            },
        ) {
            MillenniumDropdownMenuLabel { Text("ACCENT PALETTE") }
            MillenniumDropdownMenuRadioGroup(palette, onPaletteChange) {
                MillenniumPalette.entries.forEach { entry ->
                    MillenniumDropdownMenuRadioItem(
                        entry,
                        closeOnClick = true,
                        description = { Text(paletteBlurb(entry)) },
                        trailing = {
                            Box(
                                Modifier.size(14.dp).clip(CircleShape)
                                    .background(paletteSwatch(entry))
                            )
                        },
                    ) { Text(entry.name) }
                }
            }
            if (!wide) {
                MillenniumDropdownMenuSeparator()
                MillenniumDropdownMenuItem(label = "Design study", onClick = onOpenStudy, icon = MillenniumIcons.Sparkle)
            }
        }
        MillenniumSwitch(dark, onDarkChange, if (dark) "Dark" else "Light")
    }
}
