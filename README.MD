# Millennium

A Compose Multiplatform design system with a **2007 soul and 2026 standards**.

Pearl surfaces in light mode. Smoked navy in dark mode. Blue gel controls, subtle
bevels, tactile depth, and modern spacing. Built on **Compose Unstyled 2.10.0**—the
underlying components retain their keyboard, pointer, touch, and accessibility behavior.

### Usage

```kotlin
import org.spotube.millennium.composables.*
import org.spotube.millennium.theme.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composeunstyled.Text

@Composable
fun MyApp() {
    MillenniumTheme { // follows the system, or pass dark = true / false
        MillenniumTooltipHost {
            MillenniumCard {
                MillenniumButton(onClick = { /* your action */ }) {
                    Text("Hello again")
                }
            }
        }
    }
}
```

The library, Gradle module, and Maven artifact are all named **Millennium**. Its
Kotlin package is `org.spotube.millennium`. Public components accept caller-owned
state and modifiers; there is no Material dependency.

### Components

| Family | Themed components |
| --- | --- |
| Surfaces | `MillenniumCard` |
| Actions | `MillenniumButton` (primary, secondary, quiet, danger), `MillenniumDisclosure` |
| Inputs | `MillenniumCheckbox`, `MillenniumTriStateCheckbox`, `MillenniumSwitch`, `MillenniumTextField`, `MillenniumAutocomplete`, `MillenniumSlider`, `MillenniumRadioGroup` |
| Navigation | `MillenniumTabGroup`, `MillenniumDropdownMenu`, `MillenniumSidebar`, `MillenniumBottomNavigationBar` |
| Feedback | `MillenniumAvatar`, `MillenniumIcon`, `MillenniumProgress`, `MillenniumIndeterminateProgress`, `MillenniumScrollbar`, `MillenniumSeparator`, `MillenniumTooltip` |
| Overlays | `MillenniumDialog`, `MillenniumDrawer`, `MillenniumBottomSheet`, `MillenniumModalBottomSheet` |

Read semantic colors with `MillenniumTheme.colors`. Use `MillenniumText` for text
outside component content, or Compose Unstyled `Text` inside controls to inherit
their foreground style. `MillenniumCard` provides a beveled panel. Install
`MillenniumTooltipHost` once above tooltip-bearing content.

Built-in accent families are `MillenniumPalette.Blue` (the default blue/gray),
`MillenniumPalette.Orange` (bright apricot gel / neutral-white light / zinc night) and
`MillenniumPalette.Lime` (bright citron gel / neutral-white light / zinc night). The sample's palette menu switches
among them in both the reference study and component gallery. Choose one in
`MillenniumThemeConfig.palette`; each family includes light and dark appearances.
All six appearance palettes are covered by AA contrast tests, including text on
gel-gradient stops.

```kotlin
var palette by rememberSaveable { mutableStateOf(MillenniumPalette.Orange) }
MillenniumTheme(config = MillenniumThemeConfig(palette = palette)) {
    // Components resolve their active roles from MillenniumTheme.colors.
}
```

`MillenniumSidebar` is a shadcn-style composition of independent bricks rather than
one monolithic control. `MillenniumSidebarHeader` and `MillenniumSidebarFooter` stay
fixed while `MillenniumSidebarContent` scrolls (with a themed scrollbar).
`MillenniumSidebarGroup` holds a `MillenniumSidebarGroupLabel` and a collapsible
`MillenniumSidebarGroupContent`; `MillenniumSidebarMenu` stacks `MillenniumSidebarItem`
rows with optional icons and `MillenniumSidebarBadge` counters.
`MillenniumSidebarTrigger` folds the rail to icon width, driven by
`rememberMillenniumSidebarState` (or plain hoisted `Boolean` state).
The default sidebar uses pearl/smoked-glass chrome, beveled group headers,
blue gel selection with white labels, inset counter pills, and a raised collapse
button. Selection persists through hover/focus/press. Group label surfaces and
badge/trigger borders, glows, and trigger elevation are independently overridable
through `MillenniumTheme.sidebarTheme`; all defaults follow the active palette.

```kotlin
val sidebar = rememberMillenniumSidebarState()
Row(Modifier.fillMaxSize()) {
    MillenniumSidebar(sidebar) {
        MillenniumSidebarHeader { MillenniumSidebarTrigger() }
        MillenniumSidebarContent {
            MillenniumSidebarGroup(id = "library", state = sidebar) {
                MillenniumSidebarGroupLabel("Library")
                MillenniumSidebarGroupContent {
                    MillenniumSidebarMenu {
                        MillenniumSidebarItem(
                            label = "Discover",
                            icon = MillenniumIcons.Sparkle,
                            selected = true,
                            badge = "20",
                            onClick = {},
                        )
                    }
                }
            }
        }
        MillenniumSidebarFooter { MillenniumText("v1.0") }
    }
    // Main content
}
```

#### Autocomplete

`MillenniumAutocomplete` accepts caller-filtered suggestions and a
`TextFieldState`. Focus remains in the editor while a non-focusable popup is open:
Up/Down changes the highlighted result, Enter commits, Escape closes the menu and
keeps the query, and clicking outside dismisses it. Pointer selection works without moving focus into the popup. The
menu has fixed `menuHeader`/`menuFooter` slots, a scrollable suggestions area,
per-item `leadingContent`/`trailingContent` slots, and an optional fully custom
`suggestionContent` row. Customize panel and item appearances through
`MillenniumTheme.autocompleteTheme`.
Menu entry/exit, row hover, highlight border, elevation, and subtle hover scale
are animated independently. Configure `openAnimationMillis`,
`closeAnimationMillis`, and `itemStateAnimationMillis` in the autocomplete theme.

```kotlin
val query = rememberTextFieldState()
val names = listOf("A-ha", "Air", "Björk", "Daft Punk")
val matches = names.filter { it.contains(query.text, ignoreCase = true) }
MillenniumAutocomplete(
    state = query,
    label = "Artist",
    placeholder = "Search artists",
    suggestions = matches,
    suggestionLabel = { it },
    onSuggestionSelected = { selected -> /* use selected */ },
    leadingContent = { MillenniumAvatar(it.take(2), it, size = 30.dp) },
    trailingContent = { item -> /* e.g. a save action */ },
    menuHeader = { MillenniumText("MATCHES") },
    menuFooter = { MillenniumText("↑ ↓ move · Enter choose · Esc close") },
)
```

### Hackable theme configuration

#### Bottom navigation

`MillenniumBottomNavigationBar` supplies pearl/smoked-glass chrome and system
navigation-bar insets. `MillenniumBottomNavigationItem` supplies an equal-width,
keyboard-focusable destination with selected semantics and a persistent blue gel
selection. Selection/routing stay with the caller. Icon, label and badge are
composable slots; a second overload exposes a fully custom `ColumnScope` body
with `MillenniumBottomNavigationItemVisuals`.

```kotlin
var destination by rememberSaveable { mutableStateOf("Discover") }
Column(Modifier.fillMaxSize()) {
    Box(Modifier.weight(1f)) { /* Scrollable destination content */ }
    MillenniumBottomNavigationBar {
        listOf("Discover", "Library", "Saved").forEach { label ->
            MillenniumBottomNavigationItem(
                selected = destination == label,
                onClick = { destination = label },
                contentDescription = label,
                icon = {
                    com.composeunstyled.UnstyledIcon(
                        MillenniumIcons.Music,
                        contentDescription = null,
                        tint = com.composeunstyled.LocalTextStyle.current.color,
                    )
                },
                label = { Text(label) },
            )
        }
    }
}
```

Keep the bar outside scrolling content, normally with 3–5 destinations. Pass
`windowInsets = WindowInsets(0, 0, 0, 0)` for embedded bars or when a parent already
handles system insets. Icon-only items need a `contentDescription`. Use inherited
`LocalTextStyle.current.color` for custom icons to follow selected/disabled colors.
Customize chrome through `MillenniumTheme.bottomNavigationBarTheme.states`, and
item shapes, padding, typography, surfaces and motion through its `.item` states.

#### Component theme configuration

Each component's source file defines its own `<Component>ThemeProperties`,
`<Component>Theme`, state variations and default factory. For example, `Button.kt`
owns button typography, colors, dimensions, shape, padding, bevel, shadow, hover/
press feedback, and transition behavior. Every state variation is represented by
`MillenniumControlStateProperty` (`Default`, `Hovered`, `Focused`, `Pressed`,
`Disabled`, `Selected`, and `Error`); dimensions, spacing, typography, motion, and
other component-specific tokens are not collected into global style buckets.
`MillenniumThemeConfig.colorSchemes` is the single global source for light/dark
palette roles. Component themes do not copy or own that palette: they resolve their
defaults from `MillenniumTheme.colors` at composition time. A component theme can
still set a local override, such as a custom button gradient, without changing the
global palette or neighboring components.

```kotlin
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.spotube.millennium.theme.*
import org.spotube.millennium.composables.*

val customLightColors = MillenniumLightColors.copy(
    accent = Color(0xFFCC2255),
    accentTop = Color(0xFFE570A0),
    accentBottom = Color(0xFFCC2255),
)
val defaults = MillenniumThemeConfig()
val light = defaults.light
val appTheme = defaults.copy(
    colorSchemes = defaults.colorSchemes.copy(light = customLightColors),
    light = light.copy(
        buttonTheme = light.buttonTheme.copy(
            primary = light.buttonTheme.primary.copy(
                default = light.buttonTheme.primary.default.copy(
                    background = gelBrush(Color(0xFFFF9A44), Color(0xFF9C2FCA)),
                    foreground = Color.White,
                    shadowElevation = 8.dp,
                    innerGlowColor = Color.White.copy(alpha = 0.32f),
                    motion = MillenniumButtonMotion(stateChangeMillis = 190, pressedScale = 0.96f),
                ),
                hovered = light.buttonTheme.primary.hovered.copy(
                    innerGlowColor = Color.White.copy(alpha = 0.55f),
                ),
            ),
        ),
        checkboxTheme = light.checkboxTheme.copy(
            states = light.checkboxTheme.states.copy(
                default = light.checkboxTheme.states.default.copy(
                    faceSize = androidx.compose.ui.unit.DpSize(26.dp, 26.dp),
                    selectedFaceBackground = gelBrush(Color(0xFFE570A0), Color(0xFFCC2255)),
                ),
            ),
        ),
    ),
)

MillenniumTheme(config = appTheme) {
    // App content
}
```

`MillenniumThemeConfig` groups complete `light` and `dark` component-theme sets.
Within each set, `MillenniumTheme.buttonTheme`, `MillenniumTheme.cardTheme`, and
`MillenniumTheme.textFieldTheme` expose the active component themes. Copy only the
component (and the state/variant you want to change) to leave neighboring components
and the other appearance untouched. `Theme.kt` assembles the light/dark defaults;
component properties and variants live next to their composables.

Bottom sheets use Compose Unstyled's `rememberBottomSheetState` and
`rememberModalBottomSheetState`; drawers use `rememberMillenniumDrawerState`.
The shared gallery demonstrates every component, both themes, parent/child
tri-state selection, input validation, indeterminate progress, and real overlays.

### Design & accessibility

The material language combines Aqua gel, pearl enamel and Vista-like smoked
glass. Dark mode uses neutral zinc surfaces with the same restrained blue/cyan
accents, rather than tinting the entire interface navy. Primary actions are
rounded rectangles with a sharp reflection break and
luminous lower lip; secondary controls have an enamel bevel. Panels use a quieter
glass gradient and tighter framing. Fields and slider tracks have recessed inner
edges. Gel stops remain within contrast-tested endpoints rather than washing white
over labels. Menus, tabs, selection controls and navigation share the jewel-blue
accent. `millenniumSurface` can opt into
the legacy straight highlight with `innerGlowFollowsShape = false` (also exposed
by button theme properties), and accepts optional `bottomRimColor` and
`insetShadeColor`. Explicit `MillenniumText` sizes scale the configured
line height so multiline titles remain readable.

- Explicit semantic palettes for both light and dark; no inverted light-theme shortcut.
- Interactive controls use 44dp minimum targets and visible interaction states.
- Text contrast tests cover neutral surfaces, semantic text, and the entire gel gradient.
- State stays with the caller; sample preferences and control values use saveable state.
- Rounded surfaces and restrained motion soften the retro styling without copying an OS.

### Run Sample App

The sample initially opens **Aero / Design Study 01**, an isolated reference-led
composition with interactive preferences, a music panel and bottom navigation.
Its light/dark component overrides live in `sample/sharedUI/.../AeroDesignStudy.kt`,
not the library defaults. Choose **Component gallery** to access the full gallery;
**Back to design study** returns to the specimen. This direction is pending visual
approval before it is applied throughout the library.

 - Desktop JVM: `./gradlew :sample:desktopApp:run`
 - Android: `open project in Android Studio and run the sample app`
 - iOS: `open 'sample/iosApp/iosApp.xcodeproj' in Xcode and run the sample app`
 - JavaScript: `./gradlew :sample:webApp:jsBrowserDevelopmentRun`

### Validate

```sh
./gradlew build
```

`build` runs the common/JVM and Firefox-based JS tests, Android lint/build, and
compiles the desktop/web and iOS target sources. iOS simulator runtime tests require
macOS/Xcode and are skipped on Linux.

### Publish to MavenLocal

1) Run `./gradlew :millennium:publishToMavenLocal`
2) Open `~/.m2/repository/org/spotube/millennium/`

### Publish to MavenCentral

1) Create an account and a namespace on Sonatype:  
   https://central.sonatype.org/register/central-portal/#create-an-account
2) Add the developer ID, name, email, and project URL to  
   `./millennium/build.gradle.kts`
3) Generate a GPG key:  
   https://getstream.io/blog/publishing-libraries-to-mavencentral-2021/#generating-a-gpg-key-pair
   ```
   gpg --full-gen-key
   gpg --keyserver keyserver.ubuntu.com --send-keys XXXXXXXX
   gpg --export-secret-key XXXXXXXX > XXXXXXXX.gpg
   ```
4) Add these lines to `gradle.properties`:
   ```
   signing.keyId=XXXXXXXX
   signing.password=[key password]
   signing.secretKeyRingFile=../XXXXXXXX.gpg
   mavenCentralUsername=[generated username]
   mavenCentralPassword=[generated password]
   ```
5) Run `./gradlew :millennium:publishAndReleaseToMavenCentral --no-configuration-cache`
