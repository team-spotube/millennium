# Millennium — implementation state

## Intent and conventions
- Theme Compose Unstyled 2.10.0 components; do not replace their interaction/accessibility behavior with decorative imitations.
- Visual direction: 2007 Aero / Web 2.0, refined for 2026. Pearl light surfaces, smoked navy dark surfaces, blue gel gradients, hairline bevels, generous spacing, accessible controls and restrained motion.
- Library, Gradle module, and Maven artifact are named `millennium`; the public Kotlin package is `org.spotube.millennium`.
- Library code belongs in `millennium/src/commonMain`; interactive cross-platform gallery belongs in `sample/sharedUI`.
- State is caller-owned. Gallery preferences use saveable state where supported.
- Preserve existing scaffold functions/tests unless necessary for compatibility.

## Current progress
- Implemented explicit light/dark palettes and a configurable theme in `millennium/src/commonMain/kotlin/org/spotube/millennium/theme/Theme.kt`.
- Every themed component has its own source file in `millennium/src/commonMain/kotlin/org/spotube/millennium/composables/`: Text, Button, Card, Checkbox, TriStateCheckbox, ToggleSwitch, TextField, Slider, RadioGroup, TabGroup, Avatar, Icon, Disclosure, Dialog, DropdownMenu, Tooltip, Progress, Separator, Scrollbar, BottomSheet, ModalBottomSheet, and Drawer.
- `Card.kt` provides the public `MillenniumCard` raised Aero/glass surface. Shared state/palette/surface primitives live in `theme/Utils.kt`; `Theme.kt` only groups light/dark component themes and installs the active defaults.
- Every component file defines its own `<Component>ThemeProperties`, `<Component>Theme`, state variations via `MillenniumControlStateProperty`, and default factory. Component-local properties include the dimensions, spacing, typography, colors, shapes, elevation/strokes, and motion that component actually needs. `Theme.kt` only groups the light/dark component sets, assembles their defaults, and provides the active set; no global dimensions/spacing/shape/stroke/elevation/typography buckets remain. Button variants have separately configurable primary/secondary/quiet/danger state properties, including animation settings.
- `theme/Utils.kt` contains shared state-property and palette primitives plus the single-layer surface renderer. `theme/Preview.kt` owns the shared `MillenniumPreview` frame; component `@Preview`s remain in their own files.
- Android Studio's `ComposeViewAdapter` preview failure was fixed by adding AndroidX `androidx.compose.ui:ui-tooling` to the sample app's `debugImplementation`; keep this tooling dependency in the sample app, not in the published common library.
- Shared interactive gallery in `sample/sharedUI/src/commonMain/kotlin/sample/app/App.kt`: category filtering, responsive 1–3 column layouts, light/dark switch, parent/child tri-state selection, input validation, tabs, menus, progress and overlays.
- Sample state uses `rememberSaveable`, `rememberTextFieldState` and per-card `SaveableStateProvider` so control values survive category filtering; it is not persisted to disk across app restarts. Overlay state follows Compose Unstyled's state APIs.
- Renamed the module and artifact from the numeric `2000s` name to `millennium`; Gradle project references, Maven coordinates, publishing paths, README, sample branding and legacy IDEA artifact configs now use Millennium.
- Android namespaces and Maven coordinates use `org.spotube.millennium`; Maven artifact and Gradle module are now `millennium`.
- Corrected switch anatomy to use Unstyled `Track`/`Thumb` directly; parent-data placement must not be nested in a regular Box.
- Added explicit ColoredIndication dependency and theme-aware hover/press/focus feedback. Buttons, fields and sliders also provide dedicated focus states.
- Gel gradient endpoints were adjusted to keep white labels above 4.5:1 at every stop.
- README documents the Millennium API, each component, design intent, sample commands, publishing coordinates and validation.

## Validation / tooling
- **User privacy constraint: do not capture, view or inspect the user's screen or live UI. Do not use screenshot, semantic-tree, browser or other screen-inspection tools. Continue with source/build/test checks only.**
- Android Studio MCP was used for initial build validation and starting the desktop sample. Compose Hot Reload run configuration was started; do not inspect its UI further.
- Known run configurations: `sample [hot] 🔥`, `sample [js]`, `sample.androidApp`.
- A full repository `build` exposed missing `binaries.executable()` declarations on both JS targets; those are fixed. It then exposed a host browser mismatch (only Firefox is installed, while Karma defaulted to ChromeHeadless). Both JS targets now use Firefox for Karma tests, and `kotlinUpgradeYarnLock` updated the checked-in lock file.
- Kotlin sources moved to match the valid `org.spotube.millennium` package directory, removing the IDE's package-location warnings. Exported the primitive/colored-indication dependencies needed by public wrappers and declared Compose Unstyled directly in the sample for IDE classpath resolution.
- Full `./gradlew build` passes after the component-local `MillenniumControlStateProperty` migration. Seven JVM tests and Firefox-based JS tests pass; Android lint/build, desktop/web compilation and iOS target compilation also pass. iOS simulator tests are skipped on Linux. Verify Android Studio previews in the IDE when available (do not use screen capture or UI-inspection tools).
- Android Studio MCP's project model is stale after renaming the Gradle root/module: its inspections still report unresolved library imports in the sample, while Gradle successfully compiles those same sources. A Gradle project reload/sync in Android Studio is needed to refresh the IDE model; no sync action is exposed through the available IDE MCP tools.
- `MainViewController` has two expected IDE warnings (iOS-exported entry point has an uppercase platform-required name and is referenced from Swift, so Kotlin inspection sees no Kotlin caller); leave its name intact.
- Tests cover normal/muted text contrast on every neutral surface, semantic foreground contrast, gel-label contrast through both gradients, each control-state resolution, light/dark default separation, and independent per-component theme customization.
- No completed visual verification. Earlier screenshot-tool requests timed out; do not retry, per the user's explicit constraint.
- iOS runtime/simulator verification is unavailable on Linux and still requires macOS/Xcode.
- Seven JVM tests pass with no failures/skips; Firefox-based JS tests pass.
- Final Android Studio MCP build request timed out with limited diagnostics; direct Gradle validation subsequently succeeded. Gradle still reports pre-existing deprecation warnings and disabled iOS simulator tests on Linux.
- Remaining follow-up: user-led visual/interaction checks on target devices; iOS compilation/runtime checks on macOS. Do not inspect the user's screen to perform these.
- This workspace is a Git repository. No issue or pull request was supplied for this work.

## Run commands
- Desktop: `./gradlew :sample:desktopApp:run`
- Web: `./gradlew :sample:webApp:jsBrowserDevelopmentRun`
- Validate: `./gradlew build`
