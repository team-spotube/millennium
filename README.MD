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
| Inputs | `MillenniumCheckbox`, `MillenniumTriStateCheckbox`, `MillenniumSwitch`, `MillenniumTextField`, `MillenniumSlider`, `MillenniumRadioGroup` |
| Navigation | `MillenniumTabGroup`, `MillenniumDropdownMenu`, `MillenniumSidebar` |
| Feedback | `MillenniumAvatar`, `MillenniumIcon`, `MillenniumProgress`, `MillenniumIndeterminateProgress`, `MillenniumScrollbar`, `MillenniumSeparator`, `MillenniumTooltip` |
| Overlays | `MillenniumDialog`, `MillenniumDrawer`, `MillenniumBottomSheet`, `MillenniumModalBottomSheet` |

Read semantic colors with `MillenniumTheme.colors`. Use `MillenniumText` for text
outside component content, or Compose Unstyled `Text` inside controls to inherit
their foreground style. `MillenniumCard` provides a beveled panel. Install
`MillenniumTooltipHost` once above tooltip-bearing content.

`MillenniumSidebar` is a shadcn-style composition of independent bricks rather than
one monolithic control. `MillenniumSidebarHeader` and `MillenniumSidebarFooter` stay
fixed while `MillenniumSidebarContent` scrolls (with a themed scrollbar).
`MillenniumSidebarGroup` holds a `MillenniumSidebarGroupLabel` and a collapsible
`MillenniumSidebarGroupContent`; `MillenniumSidebarMenu` stacks `MillenniumSidebarItem`
rows with optional icons and `MillenniumSidebarBadge` counters.
`MillenniumSidebarTrigger` folds the rail to icon width, driven by
`rememberMillenniumSidebarState` (or plain hoisted `Boolean` state).

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

### Hackable theme configuration

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

- Explicit semantic palettes for both light and dark; no inverted light-theme shortcut.
- Interactive controls use 44dp minimum targets and visible interaction states.
- Text contrast tests cover neutral surfaces, semantic text, and the entire gel gradient.
- State stays with the caller; sample preferences and control values use saveable state.
- Rounded surfaces and restrained motion soften the retro styling without copying an OS.

### Run Sample App

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
