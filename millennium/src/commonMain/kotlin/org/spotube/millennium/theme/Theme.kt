package org.spotube.millennium.theme

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.theme.ColorScheme
import com.composeunstyled.theme.ColoredIndication
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.composeunstyled.theme.buildThemeV2
import org.spotube.millennium.composables.*

/** Global semantic palette roles. Component themes do not own palette copies. */
@Immutable
data class MillenniumColors(
    val background: Color,
    val surface: Color,
    val raised: Color,
    val inset: Color,
    val text: Color,
    val muted: Color,
    val border: Color,
    val highlight: Color,
    val accent: Color,
    val accentTop: Color,
    val accentBottom: Color,
    val onAccent: Color,
    val success: Color,
    val danger: Color,
    val scrim: Color,
)

/** Pearl surfaces and clear navy text for light appearances. */
val MillenniumLightColors = MillenniumColors(
    background = Color(0xFFE9EFF5), surface = Color(0xFFF8FAFD), raised = Color.White,
    inset = Color(0xFFE1E8F0), text = Color(0xFF182C43), muted = Color(0xFF52667C),
    border = Color(0xFFB3C3D4), highlight = Color.White, accent = Color(0xFF0064C8),
    accentTop = Color(0xFF3375B5), accentBottom = Color(0xFF0051AF), onAccent = Color.White,
    success = Color(0xFF257B51), danger = Color(0xFFBC334A), scrim = Color(0x99101E30),
)

/** Smoked navy surfaces and bright accessible foregrounds for dark appearances. */
val MillenniumDarkColors = MillenniumColors(
    background = Color(0xFF0D1522), surface = Color(0xFF182536), raised = Color(0xFF26374B),
    inset = Color(0xFF101C2A), text = Color(0xFFEAF2FC), muted = Color(0xFFA5B7CC),
    border = Color(0xFF43566F), highlight = Color(0xFF63768C), accent = Color(0xFF79C4FF),
    accentTop = Color(0xFF2F75B9), accentBottom = Color(0xFF003E83), onAccent = Color.White,
    success = Color(0xFF7AE2AE), danger = Color(0xFFFF98A9), scrim = Color(0xBB030912),
)

@Immutable
data class MillenniumColorSchemes(
    val light: MillenniumColors = MillenniumLightColors,
    val dark: MillenniumColors = MillenniumDarkColors,
)

/** The complete set of component-owned themes for one appearance. */
@Immutable
data class MillenniumThemeComponents(
    val textTheme: MillenniumTextTheme,
    val buttonTheme: MillenniumButtonTheme,
    val cardTheme: MillenniumCardTheme,
    val checkboxTheme: MillenniumCheckboxTheme,
    val triStateCheckboxTheme: MillenniumTriStateCheckboxTheme,
    val toggleSwitchTheme: MillenniumToggleSwitchTheme,
    val textFieldTheme: MillenniumTextFieldTheme,
    val sliderTheme: MillenniumSliderTheme,
    val radioGroupTheme: MillenniumRadioGroupTheme,
    val tabGroupTheme: MillenniumTabGroupTheme,
    val avatarTheme: MillenniumAvatarTheme,
    val iconTheme: MillenniumIconTheme,
    val disclosureTheme: MillenniumDisclosureTheme,
    val dialogTheme: MillenniumDialogTheme,
    val dropdownMenuTheme: MillenniumDropdownMenuTheme,
    val tooltipTheme: MillenniumTooltipTheme,
    val progressTheme: MillenniumProgressTheme,
    val scrollbarTheme: MillenniumScrollbarTheme,
    val separatorTheme: MillenniumSeparatorTheme,
    val bottomSheetTheme: MillenniumBottomSheetTheme,
    val modalBottomSheetTheme: MillenniumModalBottomSheetTheme,
    val drawerTheme: MillenniumDrawerTheme,
    val sidebarTheme: MillenniumSidebarTheme,
    val bottomNavigationBarTheme: MillenniumBottomNavigationBarTheme = defaultMillenniumBottomNavigationBarTheme(),
)

/** Assemble component-local defaults without embedding palette values in them. */
fun defaultMillenniumThemeComponents() = MillenniumThemeComponents(
    textTheme = defaultMillenniumTextTheme(),
    buttonTheme = defaultMillenniumButtonTheme(),
    cardTheme = defaultMillenniumCardTheme(),
    checkboxTheme = defaultMillenniumCheckboxTheme(),
    triStateCheckboxTheme = defaultMillenniumTriStateCheckboxTheme(),
    toggleSwitchTheme = defaultMillenniumToggleSwitchTheme(),
    textFieldTheme = defaultMillenniumTextFieldTheme(),
    sliderTheme = defaultMillenniumSliderTheme(),
    radioGroupTheme = defaultMillenniumRadioGroupTheme(),
    tabGroupTheme = defaultMillenniumTabGroupTheme(),
    avatarTheme = defaultMillenniumAvatarTheme(),
    iconTheme = defaultMillenniumIconTheme(),
    disclosureTheme = defaultMillenniumDisclosureTheme(),
    dialogTheme = defaultMillenniumDialogTheme(),
    dropdownMenuTheme = defaultMillenniumDropdownMenuTheme(),
    tooltipTheme = defaultMillenniumTooltipTheme(),
    progressTheme = defaultMillenniumProgressTheme(),
    scrollbarTheme = defaultMillenniumScrollbarTheme(),
    separatorTheme = defaultMillenniumSeparatorTheme(),
    bottomSheetTheme = defaultMillenniumBottomSheetTheme(),
    modalBottomSheetTheme = defaultMillenniumModalBottomSheetTheme(),
    drawerTheme = defaultMillenniumDrawerTheme(),
    sidebarTheme = defaultMillenniumSidebarTheme(),
    bottomNavigationBarTheme = defaultMillenniumBottomNavigationBarTheme(),
)

val MillenniumLightThemeComponents = defaultMillenniumThemeComponents()
val MillenniumDarkThemeComponents = defaultMillenniumThemeComponents()

/** Light and dark component themes are independently replaceable as a unit. */
data class MillenniumThemeConfig(
    val colorSchemes: MillenniumColorSchemes = MillenniumColorSchemes(),
    val light: MillenniumThemeComponents = MillenniumLightThemeComponents,
    val dark: MillenniumThemeComponents = MillenniumDarkThemeComponents,
    val themeTransitionMillis: Int = 180,
    val extensions: Map<String, Any> = emptyMap(),
)

private val paletteProperty = ThemeProperty<MillenniumColors>("millennium.palette")
private val paletteToken = ThemeToken<MillenniumColors>("palette")
private val LocalMillenniumConfig = staticCompositionLocalOf { MillenniumThemeConfig() }
private val LocalMillenniumComponents = staticCompositionLocalOf { MillenniumLightThemeComponents }
private val LocalMillenniumColors = staticCompositionLocalOf { MillenniumLightColors }

/** Read the active grouped component themes from content. */
object MillenniumTheme {
    val config: MillenniumThemeConfig @Composable get() = LocalMillenniumConfig.current
    val components: MillenniumThemeComponents @Composable get() = LocalMillenniumComponents.current
    val colors: MillenniumColors @Composable get() = LocalMillenniumColors.current
    val colorSchemes: MillenniumColorSchemes @Composable get() = config.colorSchemes

    val textTheme: MillenniumTextTheme @Composable get() = components.textTheme
    val buttonTheme: MillenniumButtonTheme @Composable get() = components.buttonTheme
    val cardTheme: MillenniumCardTheme @Composable get() = components.cardTheme
    val checkboxTheme: MillenniumCheckboxTheme @Composable get() = components.checkboxTheme
    val triStateCheckboxTheme: MillenniumTriStateCheckboxTheme @Composable get() = components.triStateCheckboxTheme
    val toggleSwitchTheme: MillenniumToggleSwitchTheme @Composable get() = components.toggleSwitchTheme
    val textFieldTheme: MillenniumTextFieldTheme @Composable get() = components.textFieldTheme
    val sliderTheme: MillenniumSliderTheme @Composable get() = components.sliderTheme
    val radioGroupTheme: MillenniumRadioGroupTheme @Composable get() = components.radioGroupTheme
    val tabGroupTheme: MillenniumTabGroupTheme @Composable get() = components.tabGroupTheme
    val avatarTheme: MillenniumAvatarTheme @Composable get() = components.avatarTheme
    val iconTheme: MillenniumIconTheme @Composable get() = components.iconTheme
    val disclosureTheme: MillenniumDisclosureTheme @Composable get() = components.disclosureTheme
    val dialogTheme: MillenniumDialogTheme @Composable get() = components.dialogTheme
    val dropdownMenuTheme: MillenniumDropdownMenuTheme @Composable get() = components.dropdownMenuTheme
    val tooltipTheme: MillenniumTooltipTheme @Composable get() = components.tooltipTheme
    val progressTheme: MillenniumProgressTheme @Composable get() = components.progressTheme
    val scrollbarTheme: MillenniumScrollbarTheme @Composable get() = components.scrollbarTheme
    val separatorTheme: MillenniumSeparatorTheme @Composable get() = components.separatorTheme
    val bottomSheetTheme: MillenniumBottomSheetTheme @Composable get() = components.bottomSheetTheme
    val modalBottomSheetTheme: MillenniumModalBottomSheetTheme @Composable get() = components.modalBottomSheetTheme
    val drawerTheme: MillenniumDrawerTheme @Composable get() = components.drawerTheme
    val sidebarTheme: MillenniumSidebarTheme @Composable get() = components.sidebarTheme
    val bottomNavigationBarTheme: MillenniumBottomNavigationBarTheme @Composable get() = components.bottomNavigationBarTheme

    @Composable
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> token(key: String, default: T): T = config.extensions[key] as? T ?: default
}

/** Install the selected light/dark component themes at an application or preview root. */
@Composable
fun MillenniumTheme(
    dark: Boolean = isSystemInDarkTheme(),
    config: MillenniumThemeConfig = MillenniumThemeConfig(),
    content: @Composable () -> Unit,
) {
    val activeComponents = if (dark) config.dark else config.light
    val activeColors = if (dark) config.colorSchemes.dark else config.colorSchemes.light
    val unstyledTheme = remember(config) {
        buildThemeV2 {
            name = "Millennium / Aero refined"
            colorSchemeTransitionSpec = tween(config.themeTransitionMillis)
            colorScheme(ColorScheme.Light) {
                properties[paletteProperty] = mapOf(paletteToken to config.colorSchemes.light)
            }
            colorScheme(ColorScheme.Dark) {
                properties[paletteProperty] = mapOf(paletteToken to config.colorSchemes.dark)
            }
        }
    }
    unstyledTheme(colorScheme = if (dark) ColorScheme.Dark else ColorScheme.Light) {
        val indication: Indication = remember(activeColors) {
            ColoredIndication(
                hoveredColor = activeColors.accent.copy(alpha = 0.08f),
                pressedColor = activeColors.accent.copy(alpha = 0.10f),
                focusedColor = activeColors.accent.copy(alpha = 0.10f),
                draggedColor = activeColors.accent.copy(alpha = 0.16f),
                animationSpecEnter = tween(90),
                animationSpecExit = tween(120),
            )
        }
        val bodyStyle = activeComponents.textTheme.states.default.body
        CompositionLocalProvider(
            LocalMillenniumConfig provides config,
            LocalMillenniumComponents provides activeComponents,
            LocalMillenniumColors provides activeColors,
        ) {
            CompositionLocalProvider(
                LocalIndication provides indication,
                LocalTextStyle provides bodyStyle.copy(color = activeColors.text),
                content = content,
            )
        }
    }
}
