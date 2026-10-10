package org.spotube.millennium.theme

import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.Text
import com.composeunstyled.theme.*

/** Semantic palette; all surfaces and foregrounds have explicit light and dark counterparts. */
@Immutable
data class MillenniumColors(
    val background: Color, val surface: Color, val raised: Color, val inset: Color,
    val text: Color, val muted: Color, val border: Color, val highlight: Color,
    val accent: Color, val accentTop: Color, val accentBottom: Color, val onAccent: Color,
    val success: Color, val danger: Color, val scrim: Color,
)

/** Light and dark palettes are independently replaceable; no color inversion is assumed. */
@Immutable
data class MillenniumColorSchemes(
    val light: MillenniumColors = MillenniumLightColors,
    val dark: MillenniumColors = MillenniumDarkColors,
)

@Immutable
data class MillenniumTypography(
    val body: TextStyle = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, lineHeight = 21.sp),
    val label: TextStyle = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium),
    val button: TextStyle = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
    val title: TextStyle = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 22.sp, lineHeight = 29.sp, fontWeight = FontWeight.SemiBold),
    val headline: TextStyle = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 32.sp, lineHeight = 40.sp, fontWeight = FontWeight.Bold),
    val caption: TextStyle = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 12.sp, lineHeight = 17.sp),
    val avatar: TextStyle = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold),
)

@Immutable
data class MillenniumShapes(
    val control: Shape = RoundedCornerShape(10.dp),
    val panel: Shape = RoundedCornerShape(18.dp),
    val checkbox: Shape = RoundedCornerShape(5.dp),
    val pill: Shape = RoundedCornerShape(50),
    val avatar: Shape = RoundedCornerShape(50),
    val radio: Shape = RoundedCornerShape(50),
)

@Immutable
data class MillenniumDimensions(
    val touchTarget: Dp = 44.dp,
    val buttonMinWidth: Dp = 48.dp,
    val iconSize: Dp = 20.dp,
    val checkboxSize: Dp = 22.dp,
    val checkboxMarkSize: Dp = 16.dp,
    val checkboxDash: DpSize = DpSize(11.dp, 2.dp),
    val radioDotSize: Dp = 8.dp,
    val switchTrack: DpSize = DpSize(52.dp, 30.dp),
    val switchThumb: Dp = 24.dp,
    val switchThumbInset: Dp = 3.dp,
    val textFieldMinHeight: Dp = 46.dp,
    val disclosureMinHeight: Dp = 48.dp,
    val sliderTrackHeight: Dp = 8.dp,
    val sliderThumb: Dp = 22.dp,
    val sliderThumbActive: Dp = 26.dp,
    val progressHeight: Dp = 12.dp,
    val avatarSize: Dp = 48.dp,
    val tabListInset: Dp = 4.dp,
    val cardMaxWidth: Dp = 640.dp,
    val drawerMaxWidth: Dp = 320.dp,
    val dialogMaxWidth: Dp = 480.dp,
    val tooltipMaxWidth: Dp = 280.dp,
    val dropdownMinWidth: Dp = 200.dp,
    val dragHandle: DpSize = DpSize(40.dp, 5.dp),
    val scrollbarThickness: Dp = 10.dp,
)

@Immutable
data class MillenniumSpacing(
    val xSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 12.dp,
    val large: Dp = 16.dp,
    val xLarge: Dp = 20.dp,
    val xxLarge: Dp = 24.dp,
    val cardPadding: Dp = 20.dp,
    val buttonHorizontal: Dp = 18.dp,
    val buttonVertical: Dp = 10.dp,
    val sliderHorizontalInset: Dp = 16.dp,
    val overlayPadding: Dp = 24.dp,
    val panelContent: Dp = 24.dp,
    val dragHandleTop: Dp = 22.dp,
    val textFieldHorizontal: Dp = 14.dp,
    val textFieldVertical: Dp = 12.dp,
)

@Immutable
data class MillenniumElevation(
    val card: Dp = 8.dp,
    val button: Dp = 3.dp,
    val thumb: Dp = 4.dp,
    val avatar: Dp = 3.dp,
)

@Immutable
data class MillenniumStrokes(
    val border: Dp = 1.dp,
    val focus: Dp = 2.dp,
    val separator: Dp = 1.dp,
    val highlight: Dp = 1.dp,
    val highlightInset: Dp = 8.dp,
    val highlightTop: Dp = 1.5.dp,
)

@Immutable
data class MillenniumMotion(
    val themeTransitionMillis: Int = 180,
    val indicationEnterMillis: Int = 90,
    val indicationExitMillis: Int = 120,
    val switchMillis: Int = 160,
    val progressMillis: Int = 1400,
    val tooltipDelayMillis: Long = 350,
    val indeterminateProgressFraction: Float = 0.30f,
    val hoverIndicationAlpha: Float = 0.08f,
    val pressedIndicationAlpha: Float = 0.10f,
    val focusedIndicationAlpha: Float = 0.10f,
    val draggedIndicationAlpha: Float = 0.16f,
)

enum class MillenniumVisualState { Default, Hovered, Focused, Clicked, Disabled, Selected, Error }

/** Per-state visual overrides. Null values inherit the component's palette-based defaults. */
@Immutable
data class MillenniumVisualStyle(
    val background: Brush? = null,
    val backgroundColor: Color? = null,
    val foregroundColor: Color? = null,
    val borderColor: Color? = null,
    val borderWidth: Dp? = null,
    val innerGlowColor: Color? = null,
    val innerGlowWidth: Dp? = null,
    val innerGlowInset: Dp? = null,
    val innerGlowTop: Dp? = null,
    val shadowElevation: Dp? = null,
    val shadowColor: Color? = null,
    val alpha: Float? = null,
    val shape: Shape? = null,
    val textStyle: TextStyle? = null,
    val contentPadding: PaddingValues? = null,
    val contentSpacing: Dp? = null,
)

/** Hover, focus, click/press, disabled, and selected appearances for one component. */
@Immutable
data class MillenniumComponentTheme(
    /** Component-local semantic palette. Null means inherit the active light/dark scheme. */
    val colors: MillenniumColors? = null,
    val default: MillenniumVisualStyle = MillenniumVisualStyle(),
    val hovered: MillenniumVisualStyle = MillenniumVisualStyle(innerGlowColor = Color.White.copy(alpha = 0.16f)),
    val focused: MillenniumVisualStyle = MillenniumVisualStyle(borderWidth = 2.dp, innerGlowColor = Color.White.copy(alpha = 0.22f)),
    val clicked: MillenniumVisualStyle = MillenniumVisualStyle(shadowElevation = 0.dp, innerGlowColor = Color.Black.copy(alpha = 0.16f)),
    val disabled: MillenniumVisualStyle = MillenniumVisualStyle(alpha = 0.45f),
    val selected: MillenniumVisualStyle = MillenniumVisualStyle(innerGlowColor = Color.White.copy(alpha = 0.18f)),
    val error: MillenniumVisualStyle = MillenniumVisualStyle(borderWidth = 2.dp),
    val typography: MillenniumTypography = MillenniumTypography(),
    val shapes: MillenniumShapes = MillenniumShapes(),
    val dimensions: MillenniumDimensions = MillenniumDimensions(),
    val spacing: MillenniumSpacing = MillenniumSpacing(),
    val elevation: MillenniumElevation = MillenniumElevation(),
    val strokes: MillenniumStrokes = MillenniumStrokes(),
    val motion: MillenniumMotion = MillenniumMotion(),
)

@Immutable
data class MillenniumButtonTheme(
    val primary: MillenniumComponentTheme = MillenniumComponentTheme(),
    val secondary: MillenniumComponentTheme = MillenniumComponentTheme(),
    val quiet: MillenniumComponentTheme = MillenniumComponentTheme(),
    val danger: MillenniumComponentTheme = MillenniumComponentTheme(),
    /** Optional palette shared by variants unless a variant supplies its own. */
    val colors: MillenniumColors? = null,
    val typography: MillenniumTypography = MillenniumTypography(),
    val shapes: MillenniumShapes = MillenniumShapes(),
    val dimensions: MillenniumDimensions = MillenniumDimensions(),
    val spacing: MillenniumSpacing = MillenniumSpacing(),
    val elevation: MillenniumElevation = MillenniumElevation(),
    val strokes: MillenniumStrokes = MillenniumStrokes(),
    val motion: MillenniumMotion = MillenniumMotion(),
)
typealias MillenniumCardTheme = MillenniumComponentTheme
typealias MillenniumCheckboxTheme = MillenniumComponentTheme
typealias MillenniumTriStateCheckboxTheme = MillenniumComponentTheme
typealias MillenniumToggleSwitchTheme = MillenniumComponentTheme
typealias MillenniumTextFieldTheme = MillenniumComponentTheme
typealias MillenniumSliderTheme = MillenniumComponentTheme
typealias MillenniumRadioGroupTheme = MillenniumComponentTheme
typealias MillenniumTabGroupTheme = MillenniumComponentTheme
typealias MillenniumAvatarTheme = MillenniumComponentTheme
typealias MillenniumIconTheme = MillenniumComponentTheme
typealias MillenniumDisclosureTheme = MillenniumComponentTheme
typealias MillenniumDialogTheme = MillenniumComponentTheme
typealias MillenniumDropdownMenuTheme = MillenniumComponentTheme
typealias MillenniumTooltipTheme = MillenniumComponentTheme
typealias MillenniumProgressTheme = MillenniumComponentTheme
typealias MillenniumScrollbarTheme = MillenniumComponentTheme
typealias MillenniumSeparatorTheme = MillenniumComponentTheme
typealias MillenniumBottomSheetTheme = MillenniumComponentTheme
typealias MillenniumModalBottomSheetTheme = MillenniumComponentTheme
typealias MillenniumDrawerTheme = MillenniumComponentTheme

private fun MillenniumComponentTheme.styleFor(state: MillenniumVisualState): MillenniumVisualStyle = when (state) {
    MillenniumVisualState.Default -> default
    MillenniumVisualState.Hovered -> hovered
    MillenniumVisualState.Focused -> focused
    MillenniumVisualState.Clicked -> clicked
    MillenniumVisualState.Disabled -> disabled
    MillenniumVisualState.Selected -> selected
    MillenniumVisualState.Error -> error
}

private fun MillenniumVisualStyle.overlay(override: MillenniumVisualStyle) = copy(
    background = override.background ?: if (override.backgroundColor != null) null else background,
    backgroundColor = override.backgroundColor ?: if (override.background != null) null else backgroundColor,
    foregroundColor = override.foregroundColor ?: foregroundColor,
    borderColor = override.borderColor ?: borderColor,
    borderWidth = override.borderWidth ?: borderWidth,
    innerGlowColor = override.innerGlowColor ?: innerGlowColor,
    innerGlowWidth = override.innerGlowWidth ?: innerGlowWidth,
    innerGlowInset = override.innerGlowInset ?: innerGlowInset,
    innerGlowTop = override.innerGlowTop ?: innerGlowTop,
    shadowElevation = override.shadowElevation ?: shadowElevation,
    shadowColor = override.shadowColor ?: shadowColor,
    alpha = override.alpha ?: alpha,
    shape = override.shape ?: shape,
    textStyle = override.textStyle ?: textStyle,
    contentPadding = override.contentPadding ?: contentPadding,
    contentSpacing = override.contentSpacing ?: contentSpacing,
)

internal fun MillenniumComponentTheme.resolve(
    state: MillenniumVisualState,
    base: MillenniumVisualStyle = MillenniumVisualStyle(),
): MillenniumVisualStyle = MillenniumVisualStyle(
    borderWidth = strokes.border,
    innerGlowWidth = strokes.highlight,
    innerGlowInset = strokes.highlightInset,
    innerGlowTop = strokes.highlightTop,
).overlay(base).overlay(default).overlay(styleFor(state))

internal fun MillenniumButtonTheme.forVariant(variant: MillenniumButtonVariant): MillenniumComponentTheme = when (variant) {
    MillenniumButtonVariant.Primary -> primary
    MillenniumButtonVariant.Secondary -> secondary
    MillenniumButtonVariant.Quiet -> quiet
    MillenniumButtonVariant.Danger -> danger
}.copy(colors = colors ?: when (variant) {
    MillenniumButtonVariant.Primary -> primary.colors
    MillenniumButtonVariant.Secondary -> secondary.colors
    MillenniumButtonVariant.Quiet -> quiet.colors
    MillenniumButtonVariant.Danger -> danger.colors
}, typography = typography, shapes = shapes, dimensions = dimensions, spacing = spacing,
    elevation = elevation, strokes = strokes, motion = motion)

enum class MillenniumButtonVariant { Primary, Secondary, Quiet, Danger }

/** Apply a resolved state style over a component's default Millennium surface. */
internal fun Modifier.componentStyle(
    style: MillenniumVisualStyle,
    fallbackShape: Shape,
): Modifier {
    val shape = style.shape ?: fallbackShape
    var result = this
    style.shadowElevation?.let { elevation ->
        result = result.shadow(elevation, shape, ambientColor = style.shadowColor ?: Color.Black, spotColor = style.shadowColor ?: Color.Black)
    }
    (style.background ?: style.backgroundColor?.let { Brush.verticalGradient(listOf(it, it)) })?.let {
        result = result.background(it, shape)
    }
    if (style.borderColor != null && style.borderWidth != null) {
        result = result.border(style.borderWidth, style.borderColor, shape)
    }
    style.innerGlowColor?.let { glow ->
        result = result.drawWithContent {
            drawContent()
            drawLine(
                glow,
                Offset((style.innerGlowInset ?: 0.dp).toPx(), (style.innerGlowTop ?: 0.dp).toPx()),
                Offset(size.width - (style.innerGlowInset ?: 0.dp).toPx(), (style.innerGlowTop ?: 0.dp).toPx()),
                (style.innerGlowWidth ?: 1.dp).toPx(),
            )
        }
    }
    return result
}

@Composable
internal fun MillenniumComponentContent(
    style: MillenniumVisualStyle,
    content: @Composable () -> Unit,
) {
    val inherited = LocalTextStyle.current
    val resolved = style.textStyle ?: inherited
    CompositionLocalProvider(
        LocalTextStyle provides resolved.copy(color = style.foregroundColor ?: resolved.color),
    ) { content() }
}

/**
 * The complete set of Millennium design tokens. Pass a copied config to [MillenniumTheme]
 * to customize any built-in style, or add app-owned tokens via [extensions].
 */
@Immutable
data class MillenniumThemeConfig(
    val colorSchemes: MillenniumColorSchemes = MillenniumColorSchemes(),
    /** Settings for MillenniumTheme and its system/light/dark color-scheme transition. */
    val themeTransitionMillis: Int = 180,
    val textTheme: MillenniumComponentTheme = MillenniumComponentTheme(),
    val buttonTheme: MillenniumButtonTheme = MillenniumButtonTheme(),
    val cardTheme: MillenniumCardTheme = MillenniumCardTheme(),
    val checkboxTheme: MillenniumCheckboxTheme = MillenniumCheckboxTheme(),
    val triStateCheckboxTheme: MillenniumTriStateCheckboxTheme = MillenniumTriStateCheckboxTheme(),
    val toggleSwitchTheme: MillenniumToggleSwitchTheme = MillenniumToggleSwitchTheme(),
    val textFieldTheme: MillenniumTextFieldTheme = MillenniumTextFieldTheme(),
    val sliderTheme: MillenniumSliderTheme = MillenniumSliderTheme(),
    val radioGroupTheme: MillenniumRadioGroupTheme = MillenniumRadioGroupTheme(),
    val tabGroupTheme: MillenniumTabGroupTheme = MillenniumTabGroupTheme(),
    val avatarTheme: MillenniumAvatarTheme = MillenniumAvatarTheme(),
    val iconTheme: MillenniumIconTheme = MillenniumIconTheme(),
    val disclosureTheme: MillenniumDisclosureTheme = MillenniumDisclosureTheme(),
    val dialogTheme: MillenniumDialogTheme = MillenniumDialogTheme(),
    val dropdownMenuTheme: MillenniumDropdownMenuTheme = MillenniumDropdownMenuTheme(),
    val tooltipTheme: MillenniumTooltipTheme = MillenniumTooltipTheme(),
    val progressTheme: MillenniumProgressTheme = MillenniumProgressTheme(),
    val scrollbarTheme: MillenniumScrollbarTheme = MillenniumScrollbarTheme(),
    val separatorTheme: MillenniumSeparatorTheme = MillenniumSeparatorTheme(),
    val bottomSheetTheme: MillenniumBottomSheetTheme = MillenniumBottomSheetTheme(),
    val modalBottomSheetTheme: MillenniumModalBottomSheetTheme = MillenniumModalBottomSheetTheme(),
    val drawerTheme: MillenniumDrawerTheme = MillenniumDrawerTheme(),
    val extensions: Map<String, Any> = emptyMap(),
)

val MillenniumLightColors = MillenniumColors(
    Color(0xFFE9EFF5), Color(0xFFF8FAFD), Color.White, Color(0xFFE1E8F0),
    Color(0xFF182C43), Color(0xFF52667C), Color(0xFFB3C3D4), Color.White,
    Color(0xFF0064C8), Color(0xFF3375B5), Color(0xFF0051AF), Color.White,
    Color(0xFF257B51), Color(0xFFBC334A), Color(0x99101E30),
)
val MillenniumDarkColors = MillenniumColors(
    Color(0xFF0D1522), Color(0xFF182536), Color(0xFF26374B), Color(0xFF101C2A),
    Color(0xFFEAF2FC), Color(0xFFA5B7CC), Color(0xFF43566F), Color(0xFF63768C),
    Color(0xFF79C4FF), Color(0xFF2F75B9), Color(0xFF003E83), Color.White,
    Color(0xFF7AE2AE), Color(0xFFFF98A9), Color(0xBB030912),
)

private val paletteProperty = ThemeProperty<MillenniumColors>("millennium.palette")
private val paletteToken = ThemeToken<MillenniumColors>("palette")

private val LocalMillenniumConfig = staticCompositionLocalOf { MillenniumThemeConfig() }
private val LocalMillenniumColors = staticCompositionLocalOf { MillenniumLightColors }

object MillenniumTheme {
    val config: MillenniumThemeConfig @Composable get() = LocalMillenniumConfig.current
    val colors: MillenniumColors @Composable get() = LocalMillenniumColors.current
    val colorSchemes: MillenniumColorSchemes @Composable get() = config.colorSchemes
    val textTheme: MillenniumComponentTheme @Composable get() = config.textTheme
    val buttonTheme: MillenniumButtonTheme @Composable get() = config.buttonTheme
    val cardTheme: MillenniumCardTheme @Composable get() = config.cardTheme
    val checkboxTheme: MillenniumCheckboxTheme @Composable get() = config.checkboxTheme
    val triStateCheckboxTheme: MillenniumTriStateCheckboxTheme @Composable get() = config.triStateCheckboxTheme
    val toggleSwitchTheme: MillenniumToggleSwitchTheme @Composable get() = config.toggleSwitchTheme
    val textFieldTheme: MillenniumTextFieldTheme @Composable get() = config.textFieldTheme
    val sliderTheme: MillenniumSliderTheme @Composable get() = config.sliderTheme
    val radioGroupTheme: MillenniumRadioGroupTheme @Composable get() = config.radioGroupTheme
    val tabGroupTheme: MillenniumTabGroupTheme @Composable get() = config.tabGroupTheme
    val avatarTheme: MillenniumAvatarTheme @Composable get() = config.avatarTheme
    val iconTheme: MillenniumIconTheme @Composable get() = config.iconTheme
    val disclosureTheme: MillenniumDisclosureTheme @Composable get() = config.disclosureTheme
    val dialogTheme: MillenniumDialogTheme @Composable get() = config.dialogTheme
    val dropdownMenuTheme: MillenniumDropdownMenuTheme @Composable get() = config.dropdownMenuTheme
    val tooltipTheme: MillenniumTooltipTheme @Composable get() = config.tooltipTheme
    val progressTheme: MillenniumProgressTheme @Composable get() = config.progressTheme
    val scrollbarTheme: MillenniumScrollbarTheme @Composable get() = config.scrollbarTheme
    val separatorTheme: MillenniumSeparatorTheme @Composable get() = config.separatorTheme
    val bottomSheetTheme: MillenniumBottomSheetTheme @Composable get() = config.bottomSheetTheme
    val modalBottomSheetTheme: MillenniumModalBottomSheetTheme @Composable get() = config.modalBottomSheetTheme
    val drawerTheme: MillenniumDrawerTheme @Composable get() = config.drawerTheme
    @Composable
    fun colorsFor(component: MillenniumComponentTheme): MillenniumColors = component.colors ?: colors
    @Composable
    fun colorsFor(component: MillenniumButtonTheme, variant: MillenniumButtonVariant): MillenniumColors =
        component.forVariant(variant).colors ?: colors
    @Composable
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> token(key: String, default: T): T = config.extensions[key] as? T ?: default
}

/** Install once at the app root; nested themes are supported for previews. */
@Composable
fun MillenniumTheme(
    dark: Boolean = isSystemInDarkTheme(),
    config: MillenniumThemeConfig = MillenniumThemeConfig(),
    content: @Composable () -> Unit,
) {
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
        val themeMotion = config.textTheme.motion
        val indication: Indication = remember(activeColors, themeMotion) {
            ColoredIndication(
                activeColors.accent.copy(alpha = themeMotion.hoverIndicationAlpha),
                activeColors.accent.copy(alpha = themeMotion.pressedIndicationAlpha),
                activeColors.accent.copy(alpha = themeMotion.focusedIndicationAlpha),
                activeColors.accent.copy(alpha = themeMotion.draggedIndicationAlpha),
                tween(themeMotion.indicationEnterMillis),
                tween(themeMotion.indicationExitMillis),
            )
        }
        CompositionLocalProvider(LocalMillenniumConfig provides config, LocalMillenniumColors provides activeColors) {
            CompositionLocalProvider(
                LocalIndication provides indication,
                LocalTextStyle provides config.textTheme.typography.body.copy(color = activeColors.text),
            ) { content() }
        }
    }
}

@Composable
fun MillenniumText(
    text: String, modifier: Modifier = Modifier, color: Color = MillenniumTheme.colors.text,
    size: TextUnit = MillenniumTheme.textTheme.typography.body.fontSize,
    weight: FontWeight = MillenniumTheme.textTheme.typography.body.fontWeight ?: FontWeight.Normal,
) = Text(
    text,
    modifier = modifier,
    color = color,
    fontSize = size,
    lineHeight = MillenniumTheme.textTheme.typography.body.lineHeight,
    fontWeight = weight,
    fontFamily = MillenniumTheme.textTheme.typography.body.fontFamily,
)

/** Shared light-theme frame for the component previews; keep preview setup in one place. */
@Composable
fun MillenniumPreview(content: @Composable () -> Unit) {
    MillenniumTheme(dark = false) {
        Box(
            Modifier.fillMaxWidth()
                .background(MillenniumTheme.colors.background)
                .padding(MillenniumTheme.cardTheme.spacing.xxLarge),
        ) {
            Column(Modifier.widthIn(max = MillenniumTheme.dialogTheme.dimensions.dialogMaxWidth)) { content() }
        }
    }
}

@Preview
@Composable
private fun MillenniumDefaultPreview() {
    MillenniumPreview {
        MillenniumText("Millennium preview", size = 22.sp, weight = FontWeight.SemiBold)
        MillenniumText("Shared light palette · Aero-inspired surfaces", color = MillenniumTheme.colors.muted)
    }
}

/** Four-stop gel highlight: the hard midpoint is a deliberate 2007 reference. */
fun gelBrush(top: Color, bottom: Color): Brush = Brush.verticalGradient(
    0f to top, 0.48f to lerp(top, bottom, 0.30f),
    0.50f to lerp(top, bottom, 0.64f), 1f to bottom,
)

internal fun Modifier.beveled(
    colors: MillenniumColors,
    shape: Shape,
    accent: Boolean = false, sunken: Boolean = false,
): Modifier = clip(shape).background(
        if (accent) gelBrush(colors.accentTop, colors.accentBottom)
        else Brush.verticalGradient(
            listOf(
                if (sunken) colors.inset else colors.raised, colors.surface
            )
        )
    )
