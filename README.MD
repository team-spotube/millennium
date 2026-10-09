# Millennium

A Compose Multiplatform design system with a **2007 soul and 2026 standards**.

Pearl surfaces in light mode. Smoked navy in dark mode. Blue gel controls, subtle
bevels, tactile depth, and modern spacing. Built on **Compose Unstyled 2.10.0**—the
underlying components retain their keyboard, pointer, touch, and accessibility behavior.

### Usage

```kotlin
import org.spotube.millennium.*
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
| Navigation | `MillenniumTabGroup`, `MillenniumDropdownMenu` |
| Feedback | `MillenniumAvatar`, `MillenniumIcon`, `MillenniumProgress`, `MillenniumIndeterminateProgress`, `MillenniumScrollbar`, `MillenniumSeparator`, `MillenniumTooltip` |
| Overlays | `MillenniumDialog`, `MillenniumDrawer`, `MillenniumBottomSheet`, `MillenniumModalBottomSheet` |

Read semantic colors with `MillenniumTheme.colors`. Use `MillenniumText` for text
outside component content, or Compose Unstyled `Text` inside controls to inherit
their foreground style. `MillenniumCard` provides a beveled panel. Install
`MillenniumTooltipHost` once above tooltip-bearing content.

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
