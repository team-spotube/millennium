package sample.app.catalog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import sample.app.catalog.demos.*

/** One page of the catalog: a single Millennium component and an interactive demo of it. */
@Immutable
internal class CatalogEntry(
    val id: String,
    val title: String,
    val description: String,
    /** The building blocks a lego-style component is assembled from. */
    val parts: List<String> = emptyList(),
    val content: @Composable ColumnScope.() -> Unit,
)

internal const val OverviewId = "overview"

/** Every component in the library, alphabetically. */
internal val catalogEntries: List<CatalogEntry> = listOf(
    CatalogEntry(
        "autocomplete", "Autocomplete",
        "Type, move through results with the arrow keys, and keep focus in the editor.",
        parts = listOf("leadingContent", "trailingContent", "menuHeader", "menuFooter", "emptyContent"),
    ) { AutocompleteDemo() },
    CatalogEntry("avatar", "Avatar", "A familiar face, or initials when there is none.") { AvatarDemo() },
    CatalogEntry(
        "bottom-navigation", "Bottom navigation",
        "A gel-lit destination, always within reach.",
        parts = listOf("BottomNavigationBar", "BottomNavigationItem", "icon", "label", "badge"),
    ) { BottomNavigationDemo() },
    CatalogEntry("bottom-sheet", "Bottom sheet", "An in-place, draggable sheet.") { BottomSheetDemo() },
    CatalogEntry("button", "Button", "A little gloss. A satisfying click.") { ButtonDemo() },
    CatalogEntry("card", "Card", "A raised glass surface for grouping content.") { CardDemo() },
    CatalogEntry("checkbox", "Checkbox", "Small details. Clear decisions.") { CheckboxDemo() },
    CatalogEntry(
        "dialog", "Dialog",
        "Header, body and footer bricks. Compose the focused moment.",
        parts = listOf(
            "Dialog", "DialogHeader", "DialogTitle", "DialogClose", "DialogBody",
            "DialogDescription", "DialogFooter", "AlertDialog",
        ),
    ) { DialogDemo() },
    CatalogEntry("disclosure", "Disclosure", "A little more, only when you need it.") { DisclosureDemo() },
    CatalogEntry("drawer", "Drawer", "An edge-attached panel in smoked glass and pearl.") { DrawerDemo() },
    CatalogEntry(
        "dropdown-menu", "Dropdown menu",
        "Everything, right where you expect it: icons, toggles, radios and filters.",
        parts = listOf(
            "DropdownMenu", "Header", "Content", "Footer", "Filter", "Group", "Label",
            "Separator", "Empty", "Item", "CheckboxItem", "RadioGroup", "RadioItem",
        ),
    ) { MenuDemo() },
    CatalogEntry("icon", "Icon", "Vector sharp, tintable and sized to fit.") { IconDemo() },
    CatalogEntry(
        "icon-button", "Icon button",
        "One symbol, a satisfying press. Filled ones carry the orb.",
    ) { IconButtonDemo() },
    CatalogEntry(
        "list-tile", "List tile",
        "Leading, title, subtitle and trailing, with a quieter selected gel.",
        parts = listOf("leading", "title", "subtitle", "trailing"),
    ) { ListTileDemo() },
    CatalogEntry(
        "modal-bottom-sheet", "Modal bottom sheet",
        "A sheet with a scrim, a drag handle and native dismissal.",
    ) { ModalSheetDemo() },
    CatalogEntry(
        "loading", "Loading indicators",
        "Endless spinners, loaders and skeletons for when the wait has no end in sight.",
        parts = listOf(
            "CircularProgress", "OrbSpinner", "SpokeSpinner", "DotsLoader", "BarsLoader",
            "PulseLoader", "Skeleton",
        ),
    ) { LoadingDemo() },
    CatalogEntry(
        "progress", "Progress bars",
        "Gel, barber pole, glint, blocks and buffered bars, determinate or endless.",
        parts = listOf(
            "Progress", "StripedProgress", "ShimmerProgress", "SegmentedProgress",
            "BufferedProgress", "IndeterminateProgress", "MarqueeProgress",
        ),
    ) { ProgressDemo() },
    CatalogEntry("pull-to-refresh", "Pull to refresh", "Drag down to fetch what's new.") { PullToRefreshDemo() },
    CatalogEntry("radio-group", "Radio group", "One choice from a few.") { RadioGroupDemo() },
    CatalogEntry(
        "scaffold", "Scaffold",
        "Leading, title and actions, aware of safe areas and fixed overlays.",
        parts = listOf("leading", "title", "actions", "bottomBar", "ScaffoldPadding"),
    ) { ScaffoldDemo() },
    CatalogEntry("scrollbar", "Scrollbar", "Subtle structure. Tactile navigation.") { ScrollbarDemo() },
    CatalogEntry(
        "segmented-button", "Segmented button",
        "One choice, split across a connected row.",
    ) { SegmentedButtonDemo() },
    CatalogEntry("separator", "Separator", "A paired hairline, horizontal or vertical.") { SeparatorDemo() },
    CatalogEntry(
        "sidebar", "Sidebar",
        "A fixed header, a scrolling middle, a fixed footer.",
        parts = listOf(
            "Sidebar", "Header", "Content", "Footer", "Group", "GroupLabel", "GroupContent",
            "Menu", "Item", "Badge", "Separator", "Trigger",
        ),
    ) { SidebarDemo() },
    CatalogEntry("slider", "Slider", "Find exactly the right setting.") { SliderDemo() },
    CatalogEntry("snackbar", "Snackbar", "A short, friendly confirmation.") { SnackbarDemo() },
    CatalogEntry("switch", "Switch", "On, off, and a satisfying squish in between.") { SwitchDemo() },
    CatalogEntry("tab-group", "Tab group", "One place. A few perspectives.") { TabsDemo() },
    CatalogEntry("text", "Text", "Themed type with configurable size, weight and colour.") { TextDemo() },
    CatalogEntry("text-field", "Text field", "A welcoming place for your thoughts.") { TextFieldDemo() },
    CatalogEntry("tooltip", "Tooltip", "Helpful words, without the clutter.") { TooltipDemo() },
    CatalogEntry(
        "top-app-bar", "Top app bar",
        "A glass header with a subtitle and a hairline.",
        parts = listOf("navigationIcon", "title", "subtitle", "actions"),
    ) { TopAppBarDemo() },
    CatalogEntry(
        "tri-state-checkbox", "Tri-state checkbox",
        "A parent that reflects its children: on, off or in between.",
    ) { TriStateCheckboxDemo() },
).sortedBy { it.title }

internal fun catalogEntry(id: String): CatalogEntry? = catalogEntries.firstOrNull { it.id == id }
