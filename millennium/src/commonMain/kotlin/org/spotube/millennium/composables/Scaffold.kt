package org.spotube.millennium.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.millenniumPanelBrush

@Immutable
data class MillenniumScaffoldThemeProperties(
    val titleStyle: TextStyle,
    val titleForeground: Color? = null,
    val topBarBackground: Brush? = null,
    val topBarPadding: PaddingValues,
    val topBarMinimumHeight: Dp,
    val leadingSpacing: Dp,
    val actionsSpacing: Dp,
)

@Immutable
data class MillenniumScaffoldTheme(
    val properties: MillenniumScaffoldThemeProperties,
)

fun defaultMillenniumScaffoldTheme(): MillenniumScaffoldTheme = MillenniumScaffoldTheme(
    MillenniumScaffoldThemeProperties(
        titleStyle = TextStyle(fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold),
        topBarPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        topBarMinimumHeight = 56.dp,
        leadingSpacing = 10.dp,
        actionsSpacing = 8.dp,
    ),
)

/** Defaults used by [MillenniumScaffold]. */
object MillenniumScaffoldDefaults {
    /** System bars, display cutouts and the IME combined. */
    val windowInsets: WindowInsets
        @Composable get() = WindowInsets.safeDrawing
}

/**
 * Hoists the extra space that nested [MillenniumScaffoldPadding] calls reserve
 * around a [MillenniumScaffold]'s content. Keep the state with
 * [rememberMillenniumScaffoldState] or let the scaffold create one.
 */
@Stable
class MillenniumScaffoldState {
    private val registered = mutableStateMapOf<Any, PaddingValues>()

    /** Extra padding reserved by nested [MillenniumScaffoldPadding] calls. */
    val padding: PaddingValues
        get() = registered.values.fold(PaddingValues(0.dp)) { acc, value -> acc + value }

    internal fun register(key: Any, padding: PaddingValues) {
        if (registered[key] != padding) registered[key] = padding
    }

    internal fun unregister(key: Any) {
        registered.remove(key)
    }
}

@Composable
fun rememberMillenniumScaffoldState(): MillenniumScaffoldState = remember { MillenniumScaffoldState() }

private val LocalMillenniumScaffoldState = staticCompositionLocalOf<MillenniumScaffoldState?> { null }

/**
 * Reserves [padding] around the enclosing [MillenniumScaffold]'s content while
 * [content] is composed. Wrap a fixed composable (for example a bottom bar or a
 * player dock) so scrolling content stays clear of it; the reserved space is added
 * to the scaffold's `innerPadding` and released automatically when [content] leaves
 * the composition.
 */
@Composable
fun MillenniumScaffoldPadding(
    padding: PaddingValues,
    content: @Composable () -> Unit,
) {
    val state = LocalMillenniumScaffoldState.current
    if (state != null) {
        val key = remember { Any() }
        DisposableEffect(key, padding) {
            state.register(key, padding)
            onDispose { state.unregister(key) }
        }
    }
    content()
}

/**
 * A top-app-bar scaffold with `leading`, `title` and `actions` slots, safe-area
 * handling and an optional bottom bar.
 *
 * [content] receives `innerPadding`: the space used by the top bar, the bottom bar
 * and the system-safe insets. Apply it (for example `Modifier.padding(innerPadding)`)
 * to keep content clear of system overlays and the scaffold's fixed regions. Nested
 * [MillenniumScaffoldPadding] calls add to this value, so a fixed composable anywhere
 * in the subtree can reserve its own space.
 */
@Composable
fun MillenniumScaffold(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    bottomBar: (@Composable () -> Unit)? = null,
    windowInsets: WindowInsets = MillenniumScaffoldDefaults.windowInsets,
    containerColor: Color = Color.Unspecified,
    state: MillenniumScaffoldState = rememberMillenniumScaffoldState(),
    content: @Composable (PaddingValues) -> Unit,
) {
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.scaffoldTheme.properties
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val insets = windowInsets.asPaddingValues()
    var topBarSize by remember { mutableStateOf(IntSize.Zero) }
    var bottomBarSize by remember { mutableStateOf(IntSize.Zero) }
    val innerPadding = millenniumScaffoldInnerPadding(
        insets = insets,
        topBarHeight = with(density) { topBarSize.height.toDp() },
        bottomBarHeight = if (bottomBar != null) with(density) { bottomBarSize.height.toDp() } else null,
        extraPadding = state.padding,
        layoutDirection = layoutDirection,
    )
    Box(
        modifier
            .fillMaxSize()
            .background(if (containerColor == Color.Unspecified) colors.background else containerColor),
    ) {
        CompositionLocalProvider(LocalMillenniumScaffoldState provides state) {
            Box(Modifier.fillMaxSize().consumeWindowInsets(innerPadding)) {
                content(innerPadding)
            }
            ScaffoldTopBar(
                title = title,
                leading = leading,
                actions = actions,
                properties = properties,
                windowInsets = windowInsets.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .onSizeChanged { topBarSize = it },
            )
            if (bottomBar != null) {
                Box(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .onSizeChanged { bottomBarSize = it },
                ) {
                    Box(
                        Modifier.windowInsetsPadding(
                            windowInsets.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
                        ),
                    ) { bottomBar() }
                }
            }
        }
    }
}

@Composable
private fun ScaffoldTopBar(
    title: @Composable () -> Unit,
    leading: (@Composable () -> Unit)?,
    actions: (@Composable RowScope.() -> Unit)?,
    properties: MillenniumScaffoldThemeProperties,
    windowInsets: WindowInsets,
    modifier: Modifier = Modifier,
) {
    val colors = MillenniumTheme.colors
    Column(
        modifier
            .background(properties.topBarBackground ?: millenniumPanelBrush(colors))
            .windowInsetsPadding(windowInsets),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(min = properties.topBarMinimumHeight)
                .padding(properties.topBarPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leading != null) {
                leading()
                Spacer(Modifier.width(properties.leadingSpacing))
            }
            Box(Modifier.weight(1f)) {
                MillenniumStyledContent(properties.titleStyle, properties.titleForeground ?: colors.text) { title() }
            }
            if (actions != null) {
                Spacer(Modifier.width(properties.actionsSpacing))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(properties.actionsSpacing),
                    verticalAlignment = Alignment.CenterVertically,
                ) { actions() }
            }
        }
        MillenniumSeparator()
    }
}

/** Combines safe-area insets, bar heights and reserved padding into content padding. */
internal fun millenniumScaffoldInnerPadding(
    insets: PaddingValues,
    topBarHeight: Dp,
    bottomBarHeight: Dp?,
    extraPadding: PaddingValues,
    layoutDirection: LayoutDirection,
): PaddingValues {
    val top = maxOf(topBarHeight, insets.calculateTopPadding()) + extraPadding.calculateTopPadding()
    val bottomInset = insets.calculateBottomPadding()
    val bottom = (if (bottomBarHeight != null) maxOf(bottomBarHeight, bottomInset) else bottomInset) +
        extraPadding.calculateBottomPadding()
    return PaddingValues(
        start = insets.calculateStartPadding(layoutDirection) + extraPadding.calculateStartPadding(layoutDirection),
        top = top,
        end = insets.calculateEndPadding(layoutDirection) + extraPadding.calculateEndPadding(layoutDirection),
        bottom = bottom,
    )
}

/** Adds two padding values, resolving start/end against the ambient layout direction. */
internal operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = SummedPaddingValues(this, other)

private class SummedPaddingValues(
    private val first: PaddingValues,
    private val second: PaddingValues,
) : PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
        first.calculateLeftPadding(layoutDirection) + second.calculateLeftPadding(layoutDirection)

    override fun calculateTopPadding(): Dp = first.calculateTopPadding() + second.calculateTopPadding()

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
        first.calculateRightPadding(layoutDirection) + second.calculateRightPadding(layoutDirection)

    override fun calculateBottomPadding(): Dp = first.calculateBottomPadding() + second.calculateBottomPadding()
}

@Preview
@Composable
private fun MillenniumScaffoldPreview() = MillenniumPreview {
    MillenniumScaffold(
        title = { Text("Millennium") },
        leading = { MillenniumIcon(MillenniumIcons.Sparkle) },
        actions = { MillenniumButton(onClick = {}, style = MillenniumButtonStyle.Quiet) { Text("Save") } },
    ) { innerPadding ->
        Column(
            Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MillenniumText("Safe areas and bars are handled for you.")
        }
    }
}
