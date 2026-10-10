package sample.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import com.composeunstyled.UnstyledIcon
import com.composeunstyled.LocalTextStyle
import org.spotube.millennium.composables.*
import org.spotube.millennium.theme.*

/** Isolated art direction: deliberately does not change published component defaults. */
@Composable
internal fun AeroDesignStudy(onOpenGallery: () -> Unit) {
    var dark by rememberSaveable { mutableStateOf(false) }
    val config = remember { studyTheme() }
    MillenniumTheme(dark = dark, config = config) {
        val colors = MillenniumTheme.colors
        BoxWithConstraints(Modifier.fillMaxSize().background(colors.background)) {
            val compact = maxWidth < 760.dp
            val panelWidth = if (compact) maxWidth - 40.dp else minOf(548.dp, (maxWidth - 104.dp) / 2)
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                    .padding(if (compact) 20.dp else 40.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    Modifier.widthIn(max = 1120.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MillenniumAvatar("M", "Millennium", size = 44.dp)
                    Column(Modifier.weight(1f)) {
                        MillenniumText("Millennium", size = 22.sp, weight = FontWeight.SemiBold)
                        MillenniumText("AERO / DESIGN STUDY 01", size = 10.sp, color = colors.muted)
                    }
                    MillenniumSwitch(dark, { dark = it }, "Dark")
                }
                Column(Modifier.widthIn(max = 1120.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MillenniumText("A brighter kind of interface.", size = if (compact) 28.sp else 36.sp, weight = FontWeight.SemiBold)
                    MillenniumText("Modern proportions. Pearl, water and light. A little of tomorrow, remembered.", color = colors.muted)
                }
                FlowRow(
                    Modifier.widthIn(max = 1120.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    maxItemsInEachRow = if (compact) 1 else 2,
                ) {
                    StudyControls(Modifier.width(panelWidth))
                    StudyLibrary(Modifier.width(panelWidth))
                }
                Row(
                    Modifier.widthIn(max = 1120.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MillenniumText("Direction preview • not library defaults", size = 11.sp, color = colors.muted)
                    MillenniumButton(onOpenGallery, style = MillenniumButtonStyle.Quiet) { Text("Component gallery") }
                }
            }
        }
    }
}

@Composable
private fun StudyControls(modifier: Modifier) {
    var notifications by rememberSaveable { mutableStateOf(true) }
    var downloads by rememberSaveable { mutableStateOf(false) }
    var volume by rememberSaveable { mutableFloatStateOf(0.64f) }
    var saved by rememberSaveable { mutableStateOf(false) }
    val name = rememberTextFieldState("Alex Morgan")
    MillenniumCard(modifier) {
        MillenniumText("Make yourself at home", size = 22.sp, weight = FontWeight.SemiBold)
        MillenniumText("Your space. Just the way you like it.", color = MillenniumTheme.colors.muted)
        MillenniumTextField(name, "Display name", placeholder = "Your name")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            MillenniumText("Stay in the loop")
            MillenniumSwitch(notifications, { notifications = it }, "Notifications")
        }
        MillenniumCheckbox(downloads, { downloads = it }, "Keep my favorites available offline")
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                MillenniumText("Listening volume")
                MillenniumText("${(volume * 100).toInt()}%", color = MillenniumTheme.colors.accent)
            }
            MillenniumSlider(volume, { volume = it }, "Listening volume")
        }
        MillenniumSeparator()
        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            MillenniumButton({ saved = true }) { Text(if (saved) "Changes saved" else "Save preferences") }
            MillenniumButton({ name.edit { replace(0, length, "Alex Morgan") }; saved = false }, style = MillenniumButtonStyle.Secondary) { Text("Reset") }
        }
    }
}

@Composable
private fun StudyLibrary(modifier: Modifier) {
    var destination by rememberSaveable { mutableStateOf("Discover") }
    var playing by rememberSaveable { mutableStateOf(false) }
    MillenniumCard(modifier) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(Modifier.weight(1f)) {
                MillenniumText("Your daily escape", size = 22.sp, weight = FontWeight.SemiBold)
                MillenniumText("A soundtrack for a clearer head.", color = MillenniumTheme.colors.muted)
            }
            MillenniumAvatar("AM", "Alex Morgan", size = 44.dp)
        }
        // Concentrate the ambient colour in an artwork-like panel, not every control.
        Box(
            Modifier.fillMaxWidth().height(180.dp).millenniumSurface(
                background = Brush.horizontalGradient(listOf(Color(0xFF064481), Color(0xFF178CBA), Color(0xFF39BEBF))),
                shape = RoundedCornerShape(20.dp),
                borderColor = Color(0xFF298AAF), borderWidth = 1.dp,
                innerGlowColor = Color.White.copy(alpha = 0.75f),
            ), contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier.size(112.dp).millenniumSurface(
                    background = Brush.verticalGradient(0f to Color(0xFFBCF6F1), 0.46f to Color(0xFF50BFD0), 0.49f to Color(0xFF076699), 1f to Color(0xFF179FB7)),
                    shape = RoundedCornerShape(50), borderColor = Color(0xFF5DCADA), borderWidth = 1.dp,
                    shadowElevation = 16.dp, innerGlowColor = Color.White.copy(alpha = 0.85f),
                    bottomRimColor = Color(0xFF7DEADD),
                ), contentAlignment = Alignment.Center,
            ) { UnstyledIcon(MillenniumIcons.Music, "Playlist artwork", Modifier.size(42.dp), Color.White) }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                MillenniumText(if (destination == "Discover") "Blue hour" else destination, size = 20.sp, weight = FontWeight.SemiBold)
                MillenniumText("12 tracks · 48 minutes", size = 12.sp, color = MillenniumTheme.colors.muted)
            }
            MillenniumButton({ playing = !playing }, style = MillenniumButtonStyle.Secondary) { Text(if (playing) "Pause" else "Play") }
        }
        MillenniumProgress(if (playing) 0.68f else 0.32f)
        MillenniumBottomNavigationBar(windowInsets = WindowInsets(0, 0, 0, 0)) {
            listOf("Discover", "Library", "Saved").forEach { label ->
                MillenniumBottomNavigationItem(
                    selected = destination == label, onClick = { destination = label }, contentDescription = label,
                    icon = { UnstyledIcon(if (label == "Discover") MillenniumIcons.Sparkle else MillenniumIcons.Music, null, tint = LocalTextStyle.current.color) },
                    label = { Text(label) },
                )
            }
        }
    }
}

private fun studyTheme(): MillenniumThemeConfig {
    val base = MillenniumThemeConfig()
    fun components(original: MillenniumThemeComponents, colors: MillenniumColors): MillenniumThemeComponents {
        val pearl = Brush.verticalGradient(listOf(colors.raised, colors.surface))
        val enamel = Brush.verticalGradient(0f to colors.raised, 0.48f to colors.surface, 0.51f to colors.inset, 1f to colors.surface)
        val gel = Brush.verticalGradient(0f to colors.accentTop, 0.44f to colors.accentTop, 0.48f to colors.accentBottom, 1f to colors.accentBottom)
        fun buttons(states: MillenniumControlStateProperty<MillenniumButtonThemeProperties>, primary: Boolean) =
            MillenniumControlStateProperty.resolveWith { state ->
                states.resolve(state).copy(
                    shape = RoundedCornerShape(10.dp), background = if (primary) gel else enamel,
                    shadowElevation = if (state == MillenniumControlState.Pressed) 0.dp else 2.dp,
                    borderColor = if (primary) colors.accentBottom else colors.border,
                    innerGlowColor = colors.highlight.copy(alpha = 0.65f), bottomRimColor = Color.Transparent,
                    textStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
                    motion = MillenniumButtonMotion(hoverScale = 1f, pressedScale = 0.99f),
                )
            }
        return original.copy(
            cardTheme = original.cardTheme.copy(states = MillenniumControlStateProperty.resolveWith { state ->
                original.cardTheme.states.resolve(state).copy(
                    background = pearl, shape = RoundedCornerShape(28.dp), contentPadding = PaddingValues(28.dp),
                    contentSpacing = 22.dp, shadowElevation = 3.dp, borderColor = colors.border.copy(alpha = 0.6f),
                    innerGlowColor = colors.highlight.copy(alpha = 0.6f), bottomRimColor = Color.Transparent,
                )
            }),
            buttonTheme = original.buttonTheme.copy(primary = buttons(original.buttonTheme.primary, true), secondary = buttons(original.buttonTheme.secondary, false)),
            textFieldTheme = original.textFieldTheme.copy(states = MillenniumControlStateProperty.resolveWith { state ->
                original.textFieldTheme.states.resolve(state).copy(
                    shape = RoundedCornerShape(16.dp), background = Brush.verticalGradient(listOf(colors.inset, colors.surface)),
                    borderColor = if (state == MillenniumControlState.Focused) colors.accent else colors.border.copy(alpha = 0.5f),
                    insetShadeColor = colors.text.copy(alpha = 0.06f), bottomRimColor = colors.highlight.copy(alpha = 0.6f),
                )
            }),
            bottomNavigationBarTheme = original.bottomNavigationBarTheme.copy(
                states = MillenniumControlStateProperty.all(original.bottomNavigationBarTheme.states.default.copy(
                    background = enamel, shape = RoundedCornerShape(20.dp), shadowElevation = 0.dp,
                    borderColor = colors.border.copy(alpha = 0.5f),
                )),
                item = MillenniumControlStateProperty.resolveWith { state -> original.bottomNavigationBarTheme.item.resolve(state).copy(
                    shape = RoundedCornerShape(14.dp), shadowElevation = 0.dp,
                ) },
            ),
        )
    }
    return base.copy(light = components(base.light, base.colorSchemes.light), dark = components(base.dark, base.colorSchemes.dark))
}
