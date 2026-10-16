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

#### Consuming Compose Unstyled

Millennium exposes Compose Unstyled as part of its own API. Declaring Millennium
is enough to use `com.composeunstyled.*` (`Text`, `UnstyledButton`, `UnstyledIcon`,
`LocalTextStyle`, theming, primitives, and the rest of the component surface) in
your project; you do not add Compose Unstyled separately. Millennium's public
components accept and return these types, so the dependency is exported
transitively on every target (Android, JVM, JS, WebAssembly, iOS) and as `compile`
scope in the published platform POMs. The sample relies on this: it declares only
`project(":millennium")` and still imports Compose Unstyled directly, so the
`build` fails if the exported API ever regresses.

```kotlin
dependencies {
    implementation("org.spotube.millennium:millennium:1.0.0")
    // No composeunstyled dependency needed; it comes from Millennium's API.
}
```

### Components

| Family | Themed components |
| --- | --- |
| Surfaces | `MillenniumCard` |
| Layout | `MillenniumScaffold` |
| Actions | `MillenniumButton` (primary, secondary, quiet, danger), `MillenniumIconButton`, `MillenniumDisclosure` |
| Inputs | `MillenniumCheckbox`, `MillenniumTriStateCheckbox`, `MillenniumSwitch`, `MillenniumTextField`, `MillenniumAutocomplete`, `MillenniumSlider`, `MillenniumRadioGroup`, `MillenniumSegmentedButton` |
| Navigation | `MillenniumTabGroup`, `MillenniumDropdownMenu` (+ bricks), `MillenniumSidebar` (+ bricks), `MillenniumBottomNavigationBar`, `MillenniumTopAppBar`, `MillenniumListTile` |
| Display | `MillenniumText`, `MillenniumAvatar`, `MillenniumIcon`, `MillenniumScrollbar`, `MillenniumSeparator`, `MillenniumTooltip` |
| Progress & loading | `MillenniumProgress`, `MillenniumStripedProgress`, `MillenniumShimmerProgress`, `MillenniumSegmentedProgress`, `MillenniumBufferedProgress`, `MillenniumIndeterminateProgress`, `MillenniumMarqueeProgress`, `MillenniumCircularProgress`, `MillenniumOrbSpinner`, `MillenniumSpokeSpinner`, `MillenniumDotsLoader`, `MillenniumBarsLoader`, `MillenniumPulseLoader`, `MillenniumSkeleton` |
| Feedback | `MillenniumSnackbarHost`, `MillenniumPullToRefreshBox` |
| Overlays | `MillenniumDialog` (+ bricks, `MillenniumAlertDialog`), `MillenniumDrawer`, `MillenniumBottomSheet`, `MillenniumModalBottomSheet` |

### Design principles

1. **Everything is a brick.** Complex components (sidebar, dropdown menu, dialog,
   list tile, top app bar, autocomplete) are assembled from small composables rather
   than configured with strings and flags. Anything that can be composed accepts a
   composable slot; `String` overloads exist only as conveniences over the slotted form.
2. **State is caller-owned.** Components never hide state you might need: pass
   `expanded`/`checked`/`selected`/`TextFieldState` in and react to callbacks.
   Hoisted helpers (`rememberMillenniumSidebarState`, `rememberMillenniumDrawerState`, …)
   are optional.
3. **Theme, then override.** Every composable takes a `theme` (default
   `MillenniumTheme.<component>Theme`) and nullable named overrides for its common
   visual knobs, resolved as `parameter ?: themeValue ?: fallback`.
4. **Accessibility is inherited, not imitated.** Controls wrap Compose Unstyled
   primitives; Millennium only adds visuals. Targets are 44dp and focus rings appear
   for keyboard focus only.

`MillenniumTopAppBar`, `MillenniumSegmentedButton`, `MillenniumSnackbarHost` and
`MillenniumPullToRefreshBox` are themed like the rest: each owns a
`<Component>Theme` under `MillenniumTheme` (`.topAppBarTheme`,
`.segmentedButtonTheme`, `.snackbarTheme`, `.pullToRefreshTheme`) and uses the shared
pearl/smoke surfaces, gel selection and hairline bevels. For window-size dispatch
(without pulling in Material), `org.spotube.millennium.adaptive.currentWindowAdaptiveInfo`
returns compact/medium/expanded width and height buckets.

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

#### Dropdown menu

`MillenniumDropdownMenu` is the same style of composition as the sidebar. The root owns
the anchored, animated panel; everything inside is a brick:

| Brick | Role |
| --- | --- |
| `MillenniumDropdownMenuHeader` / `…Footer` | Fixed regions above and below the scrolling rows, with an optional divider |
| `MillenniumDropdownMenuContent` | The scrolling region; a themed scrollbar appears only on overflow |
| `MillenniumDropdownMenuFilter` | A search field wired to your own `TextFieldState` |
| `MillenniumDropdownMenuGroup` / `…Label` / `…Separator` / `…Empty` | Structure, headings and empty states (label and empty take composables) |
| `MillenniumDropdownMenuItem` | Slotted row: `leading`, `label`, `description`, `shortcut`, `trailing`, `selectedIndicator`; `selected` and `destructive` flags |
| `MillenniumDropdownMenuCheckboxItem` | Toggle row that stays open by default |
| `MillenniumDropdownMenuRadioGroup` + `…RadioItem` | One shared value among several rows |
| `MillenniumDropdownMenuItemContainer` | The raw row surface, for completely custom rows |

Inside any custom row, `MillenniumDropdownMenuItemIcon` and
`LocalMillenniumDropdownMenuItemVisuals` follow the row's hover/press/destructive
colours. The older `items: List<String>` overload remains as a one-liner convenience.

```kotlin
var expanded by remember { mutableStateOf(false) }
var showLyrics by remember { mutableStateOf(true) }
var quality by remember { mutableStateOf("High") }
val filter = rememberTextFieldState()

MillenniumDropdownMenu(
    expanded = expanded,
    onExpandedChange = { expanded = it },
    anchor = { MillenniumButton({ expanded = !expanded }) { Text("Track options") } },
) {
    MillenniumDropdownMenuHeader { MillenniumDropdownMenuFilter(filter, placeholder = "Search") }
    MillenniumDropdownMenuContent(maximumHeight = 280.dp) {
        MillenniumDropdownMenuGroup {
            MillenniumDropdownMenuLabel("TRACK")
            MillenniumDropdownMenuItem(
                label = "Add to playlist", onClick = {},
                icon = MillenniumIcons.Music, shortcut = "Ctrl+P",
            )
            MillenniumDropdownMenuItem(
                onClick = {},
                leading = { MillenniumAvatar("KR", "Kingkor", size = 28.dp) },
                description = { Text("Following") },
                trailing = { MillenniumSidebarBadge("3") },
            ) { Text("Kingkor Roy") }
        }
        MillenniumDropdownMenuSeparator()
        MillenniumDropdownMenuCheckboxItem(showLyrics, { showLyrics = it }) { Text("Show lyrics") }
        MillenniumDropdownMenuRadioGroup(quality, { quality = it }) {
            listOf("Standard", "High", "Lossless").forEach { level ->
                MillenniumDropdownMenuRadioItem(level) { Text(level) }
            }
        }
        MillenniumDropdownMenuSeparator()
        MillenniumDropdownMenuItem("Remove from library", onClick = {}, destructive = true)
    }
    MillenniumDropdownMenuFooter { MillenniumText("Quality: $quality") }
}
```

Known limits: rows inside the scrolling region do not register with Compose Unstyled's
panel focus list, so Home/End and keyboard-open initial focus do not land on the first
row (arrow keys still move between rows once one is focused). Submenus are not
implemented.

#### Dialog

`MillenniumDialog` is the scrim plus the animated panel; header, body and footer are
bricks. The title bar, close cap and footer are all optional.

| Brick | Role |
| --- | --- |
| `MillenniumDialogHeader` | Fixed title bar (a `RowScope`) with a hairline |
| `MillenniumDialogTitle` | Takes the remaining header width (composable or `String`) |
| `MillenniumDialogClose` | 44dp target that dismisses the dialog; custom content slot, default `MillenniumDialogCloseCap` |
| `MillenniumDialogBody` | The region that scrolls when the dialog is taller than the screen |
| `MillenniumDialogDescription` | Muted supporting text (composable or `String`) |
| `MillenniumDialogFooter` | End-aligned action row, any `horizontalArrangement` |
| `MillenniumAlertDialog` | Fully slotted confirmation: `icon`, `title`, `description`, `content`, `actions` |

```kotlin
var open by remember { mutableStateOf(false) }

MillenniumDialog(visible = open, onDismissRequest = { open = false }) {
    MillenniumDialogHeader {
        MillenniumIcon(MillenniumIcons.Sparkle, tint = MillenniumTheme.colors.accent)
        MillenniumDialogTitle { Text("Share playlist") }
        MillenniumDialogClose()
    }
    MillenniumDialogBody {
        MillenniumDialogDescription("Anyone with the link can listen.")
        MillenniumTextField(rememberTextFieldState("The golden hour mix"), "Name")
    }
    MillenniumDialogFooter {
        MillenniumButton({ open = false }, style = MillenniumButtonStyle.Secondary) { Text("Cancel") }
        MillenniumButton({ open = false }) { Text("Share") }
    }
}

MillenniumAlertDialog(
    visible = confirm,
    onDismissRequest = { confirm = false },
    title = { Text("Delete this playlist?") },
    description = { Text("This can’t be undone.") },
    actions = {
        MillenniumButton({ confirm = false }, style = MillenniumButtonStyle.Secondary) { Text("Keep it") }
        MillenniumButton({ confirm = false }, style = MillenniumButtonStyle.Danger) { Text("Delete") }
    },
)
```

Overlays (`MillenniumDialog`, `MillenniumDrawer`, the modal bottom sheet) need
finite, screen-sized constraints. Compose Unstyled's drawer viewport throws
`Drawer Viewport requires finite maximum width and height constraints` when it is
placed inside a scrolling column, so keep their state in a screen-sized root, as the
sample catalog does with `DemoOverlays`.

#### Progress and loading indicators

Linear bars share the recessed track and gel fill of `MillenniumProgress`; round
indicators and loaders draw from the same accent gel, so they follow the palette and
dark mode automatically. All of them accept a `color` override (e.g.
`MillenniumTheme.colors.success`), a `size`/`height`, and report
`progressBarRangeInfo` (indeterminate when there is no value) plus a
`contentDescription` to accessibility services. Shared knobs live in
`MillenniumTheme.loadingTheme`.

| Indicator | Kind | Notes |
| --- | --- | --- |
| `MillenniumProgress` | determinate | The gel bar |
| `MillenniumStripedProgress` | determinate / endless | Barber pole; `progress = null` marches the whole track |
| `MillenniumShimmerProgress` | determinate | A glint sweeps across the fill |
| `MillenniumSegmentedProgress` | determinate / endless | Windows-style blocks; endless chases a lit group |
| `MillenniumBufferedProgress` | determinate | Pale buffered region ahead of the playhead |
| `MillenniumIndeterminateProgress` | endless | Bouncing gel chunk |
| `MillenniumMarqueeProgress` | endless | Two bars sweeping at different speeds |
| `MillenniumCircularProgress` | determinate / endless | Recessed ring with a gel arc; `content` slot for a label |
| `MillenniumOrbSpinner` | endless | Glass orb with an orbiting comet |
| `MillenniumSpokeSpinner` | endless | Classic fading ticks |
| `MillenniumDotsLoader` | endless | Gel beads bouncing in a wave |
| `MillenniumBarsLoader` | endless | Equalizer bars |
| `MillenniumPulseLoader` | endless | Aqua ripples from a breathing bead |
| `MillenniumSkeleton` | endless | Placeholder with a pearly glint |

```kotlin
MillenniumProgress(progress)
MillenniumStripedProgress(progress = progress)          // determinate barber pole
MillenniumStripedProgress()                              // endless barber pole
MillenniumSegmentedProgress(progress = progress, segments = 10)
MillenniumBufferedProgress(progress = 0.4f, buffered = 0.7f)

MillenniumCircularProgress(progress = 0.72f, size = 56.dp) {
    MillenniumText("72", weight = FontWeight.SemiBold)
}
MillenniumOrbSpinner(color = MillenniumTheme.colors.success)

// A button that shows it is busy
MillenniumButton(onClick = save, enabled = !saving) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        if (saving) MillenniumSpokeSpinner(size = 18.dp, color = MillenniumTheme.colors.onAccent)
        Text(if (saving) "Saving…" else "Save")
    }
}

// Placeholders shaped like the content that is loading
Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
    MillenniumSkeleton(Modifier.size(40.dp), shape = CircleShape)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        MillenniumSkeleton(Modifier.width(180.dp).height(14.dp))
        MillenniumSkeleton(Modifier.width(110.dp).height(10.dp))
    }
}
```

#### Icons

`MillenniumIcons` is a small dependency-free vector set (`Sparkle`, `Music`, `Check`,
`Chevron`). `MillenniumIcon` tints and sizes any `ImageVector`, following the
control-state colours; inside rows of dropdown menus, use
`MillenniumDropdownMenuItemIcon`, which follows the row's colours.

```kotlin
MillenniumIcon(MillenniumIcons.Sparkle, "Sparkle", tint = MillenniumTheme.colors.accent, size = 28.dp)
```

#### Sample catalog

The sample's component catalog (`sample/sharedUI/.../catalog`) has one page per
component, a flat searchable sidebar of all components, and per-page lists of the
bricks a lego component is built from. To add a component page: write a demo in
`catalog/demos/…Demos.kt` and register it in `CatalogRegistry.kt`.

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

#### List tile

`MillenniumListTile` composes a `leading`, `title`, optional `subtitle` and
`trailing` slot into one row. It becomes an accessible, keyboard-focusable button
only when `onClick` is non-null; without it the row is a plain surface. `selected`
uses a quieter, surface-blended pour of the accent gel rather than a primary
button's full-strength surface, and it responds to hover, focus and press.
`onRightClick` (pointer/desktop) receives the position of a secondary click,
measured relative to the tile. Customize fill, typography, spacing, shape and
motion through `MillenniumTheme.listTileTheme.states`; `selectedBackground` and
`selectedGelStrength` override the selected fill.

```kotlin
var selected by rememberSaveable { mutableStateOf("Air") }
MillenniumListTile(
    title = { Text("La Femme d'Argent") },
    leading = { MillenniumAvatar("AI", "Air") },
    subtitle = { Text("Air · remastered") },
    trailing = { MillenniumText("7:09", color = MillenniumTheme.colors.muted) },
    selected = selected == "Air",
    onClick = { selected = "Air" },
    onRightClick = { position -> /* open a context menu at position */ },
)
```

#### Icon buttons

`MillenniumIconButton` is a compact 44dp circle holding a single icon. Pass an
`ImageVector` and a `contentDescription`, or use the content overload for a custom
glyph. `MillenniumIconButtonStyle` offers `Primary`, `Secondary`, `Quiet` and
`Danger`: the filled variants (`Primary`, `Danger`) reuse the exact `MillenniumButton`
gel and add the Aqua glass orb on top — a translucent dome with a curved meniscus, a
bright top rim and a lower reflection — while
`Secondary` is enamel and `Quiet` is transparent. Focus rings stay keyboard-only,
presses animate a subtle scale, and every variant shares the button bevel language.
Customize through `MillenniumTheme.iconButtonTheme`.

```kotlin
MillenniumIconButton(MillenniumIcons.Sparkle, "Add to favorites", onClick = { /* … */ })
MillenniumIconButton(
    MillenniumIcons.Check,
    "Delete",
    onClick = { /* … */ },
    style = MillenniumIconButtonStyle.Danger,
)
```

#### Scaffold

`MillenniumScaffold` lays out a `leading`, `title` and `actions` top bar plus an
optional `bottomBar`, and hands its `content` an `innerPadding` covering the bars
and the system-safe area. Apply it (`Modifier.padding(innerPadding)`) so content
never sits under system overlays or the scaffold's fixed regions. `windowInsets`
defaults to `WindowInsets.safeDrawing`; pass `WindowInsets(0, 0, 0, 0)` when a parent
already handles insets.

Wrap a fixed composable (a bottom navigation bar, a player dock, a floating toolbar)
in `MillenniumScaffoldPadding(padding)`: while it is composed it reserves that space
around the scaffold content, adding to `innerPadding`, and releases it when removed.
`MillenniumScaffoldState` / `rememberMillenniumScaffoldState` hoist those reservations
when you need to read or drive them yourself.

```kotlin
MillenniumScaffold(
    title = { MillenniumText("Library") },
    leading = { MillenniumIcon(MillenniumIcons.Sparkle) },
    actions = { MillenniumButton(onClick = {}, style = MillenniumButtonStyle.Quiet) { Text("Save") } },
) { innerPadding ->
    Box(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize().padding(innerPadding)) { /* rows */ }
        if (dockVisible) {
            MillenniumScaffoldPadding(PaddingValues(bottom = 72.dp)) {
                PlayerDock(Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}
```

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

#### Per-instance overrides

Everything a theme controls is also overridable at a single call site. Every
composable accepts a `theme` parameter that defaults to the active
`MillenniumTheme.<component>Theme`, so one instance can use a fully custom theme
without touching the app theme:

```kotlin
MillenniumButton(
    onClick = { /* … */ },
    theme = MillenniumTheme.buttonTheme.copy(
        primary = MillenniumTheme.buttonTheme.primary.copy(
            default = MillenniumTheme.buttonTheme.primary.default.copy(
                shape = RoundedCornerShape(4.dp),
            ),
        ),
    ),
) { Text("Fine print") }
```

For the common knobs — backgrounds, colors, shapes, borders, elevation, padding,
spacing, typography, sizes and animation durations — composables also take
optional named parameters. Passing one overrides that value for the single call;
leaving it `null` (the default) keeps the theme value, so existing call sites are
unchanged:

```kotlin
MillenniumCard(
    shape = RoundedCornerShape(4.dp),
    background = gelBrush(Color(0xFFE570A0), Color(0xFFCC2255)),
    contentSpacing = 8.dp,
) { /* … */ }

MillenniumSwitch(
    checked = true,
    onCheckedChange = {},
    label = "Live",
    selectedTrackBackground = gelBrush(Color(0xFF34D399), Color(0xFF059669)),
    thumbElevation = 1.dp,
)
```

`MillenniumAutocomplete` additionally takes its own `textFieldTheme`, and the
Sidebar bricks (`MillenniumSidebarHeader`, `...Content`, `...Footer`,
`...GroupLabel`, `...GroupContent`, `...Menu`, `...Item`, `...Badge`,
`...Trigger`) each take a `theme` plus their section's parameters.

Bottom sheets use Compose Unstyled's `rememberBottomSheetState` and
`rememberModalBottomSheetState`; drawers use `rememberMillenniumDrawerState`.
The sample catalog demonstrates every component, both themes, parent/child
tri-state selection, input validation, progress and loading indicators, and real overlays.

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
- Focus rings are reserved for keyboard and accessibility navigation: pointer/touch focus (for example a mouse click) never shows one. Custom controls can use the public `collectIsKeyboardFocusedAsState` helper to match.
- Text contrast tests cover neutral surfaces, semantic text, and the entire gel gradient.
- State stays with the caller; sample preferences and control values use saveable state.
- Rounded surfaces and restrained motion soften the retro styling without copying an OS.

### Run Sample App

The sample initially opens **Aero / Design Study 01**, an isolated reference-led
composition with interactive preferences, a music panel and bottom navigation.
Its light/dark component overrides live in `sample/sharedUI/.../AeroDesignStudy.kt`,
not the library defaults. Open the **component catalog** from the study to browse
every component (one page each, searchable, with live demos in light/dark and all
palettes); the header's **Design study** button returns to the specimen.

 - Desktop JVM: `./gradlew :sample:desktopApp:run`
 - Android: `open project in Android Studio and run the sample app`
 - iOS: `open 'sample/iosApp/iosApp.xcodeproj' in Xcode and run the sample app`
 - JavaScript: `./gradlew :sample:webApp:jsBrowserDevelopmentRun`
 - WebAssembly: `./gradlew :sample:webApp:wasmJsBrowserDevelopmentRun`

### Validate

```sh
./gradlew build
```

`build` runs the common/JVM and Firefox-based JS/WebAssembly tests, Android
lint/build, and compiles the desktop/web and iOS target sources. iOS simulator
runtime tests require macOS/Xcode and are skipped on Linux.

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
