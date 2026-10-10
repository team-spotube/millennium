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
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.theme.ColorScheme
import com.composeunstyled.theme.ColoredIndication
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.composeunstyled.theme.buildThemeV2
import org.spotube.millennium.composables.*

/** The complete set of component-owned themes for one appearance. */
@Immutable
data class MillenniumThemeComponents(
    val colors: MillenniumColors,
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
)

/** Build the component-local default themes for a light or dark palette. */
fun defaultMillenniumThemeComponents(colors: MillenniumColors) = MillenniumThemeComponents(
    colors = colors,
    textTheme = defaultMillenniumTextTheme(colors),
    buttonTheme = defaultMillenniumButtonTheme(colors),
    cardTheme = defaultMillenniumCardTheme(colors),
    checkboxTheme = defaultMillenniumCheckboxTheme(colors),
    triStateCheckboxTheme = defaultMillenniumTriStateCheckboxTheme(colors),
    toggleSwitchTheme = defaultMillenniumToggleSwitchTheme(colors),
    textFieldTheme = defaultMillenniumTextFieldTheme(colors),
    sliderTheme = defaultMillenniumSliderTheme(colors),
    radioGroupTheme = defaultMillenniumRadioGroupTheme(colors),
    tabGroupTheme = defaultMillenniumTabGroupTheme(colors),
    avatarTheme = defaultMillenniumAvatarTheme(colors),
    iconTheme = defaultMillenniumIconTheme(colors),
    disclosureTheme = defaultMillenniumDisclosureTheme(colors),
    dialogTheme = defaultMillenniumDialogTheme(colors),
    dropdownMenuTheme = defaultMillenniumDropdownMenuTheme(colors),
    tooltipTheme = defaultMillenniumTooltipTheme(colors),
    progressTheme = defaultMillenniumProgressTheme(colors),
    scrollbarTheme = defaultMillenniumScrollbarTheme(colors),
    separatorTheme = defaultMillenniumSeparatorTheme(colors),
    bottomSheetTheme = defaultMillenniumBottomSheetTheme(colors),
    modalBottomSheetTheme = defaultMillenniumModalBottomSheetTheme(colors),
    drawerTheme = defaultMillenniumDrawerTheme(colors),
)

val MillenniumLightThemeComponents = defaultMillenniumThemeComponents(MillenniumLightColors)
val MillenniumDarkThemeComponents = defaultMillenniumThemeComponents(MillenniumDarkColors)

/** Light and dark component themes are independently replaceable as a unit. */
data class MillenniumThemeConfig(
    val light: MillenniumThemeComponents = MillenniumLightThemeComponents,
    val dark: MillenniumThemeComponents = MillenniumDarkThemeComponents,
    val themeTransitionMillis: Int = 180,
    val extensions: Map<String, Any> = emptyMap(),
)

@Immutable
data class MillenniumColorSchemes(
    val light: MillenniumColors = MillenniumLightColors,
    val dark: MillenniumColors = MillenniumDarkColors,
)

private val paletteProperty = ThemeProperty<MillenniumColors>("millennium.palette")
private val paletteToken = ThemeToken<MillenniumColors>("palette")
private val LocalMillenniumConfig = staticCompositionLocalOf { MillenniumThemeConfig() }
private val LocalMillenniumComponents = staticCompositionLocalOf { MillenniumLightThemeComponents }

/** Read the active grouped component themes from content. */
object MillenniumTheme {
    val config: MillenniumThemeConfig @Composable get() = LocalMillenniumConfig.current
    val components: MillenniumThemeComponents @Composable get() = LocalMillenniumComponents.current
    val colors: MillenniumColors @Composable get() = components.colors
    val colorSchemes: MillenniumColorSchemes @Composable
        get() = MillenniumColorSchemes(config.light.colors, config.dark.colors)

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
    val activeColors = activeComponents.colors
    val unstyledTheme = remember(config) {
        buildThemeV2 {
            name = "Millennium / Aero refined"
            colorSchemeTransitionSpec = tween(config.themeTransitionMillis)
            colorScheme(ColorScheme.Light) {
                properties[paletteProperty] = mapOf(paletteToken to config.light.colors)
            }
            colorScheme(ColorScheme.Dark) {
                properties[paletteProperty] = mapOf(paletteToken to config.dark.colors)
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
        ) {
            CompositionLocalProvider(
                LocalIndication provides indication,
                LocalTextStyle provides bodyStyle.copy(color = activeColors.text),
                content = content,
            )
        }
    }
}
