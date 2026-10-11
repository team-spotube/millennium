package org.spotube.millennium.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.millenniumPanelBrush

/** Complete, theme-owned appearance and layout configuration for [MillenniumTopAppBar]. */
@Immutable
data class MillenniumTopAppBarThemeProperties(
    val titleStyle: TextStyle,
    val subtitleStyle: TextStyle,
    val titleForeground: Color? = null,
    val subtitleForeground: Color? = null,
    val navigationForeground: Color? = null,
    val actionForeground: Color? = null,
    val background: Brush? = null,
    val contentPadding: PaddingValues,
    val minimumHeight: Dp,
    val navigationSpacing: Dp,
    val actionSpacing: Dp,
    val separatorColor: Color? = null,
)

@Immutable
data class MillenniumTopAppBarTheme(
    val properties: MillenniumTopAppBarThemeProperties,
)

fun defaultMillenniumTopAppBarTheme(): MillenniumTopAppBarTheme = MillenniumTopAppBarTheme(
    MillenniumTopAppBarThemeProperties(
        titleStyle = TextStyle(fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold),
        subtitleStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        minimumHeight = 56.dp,
        navigationSpacing = 10.dp,
        actionSpacing = 4.dp,
    ),
)

/**
 * A pearl/smoked-glass app bar with a title, optional subtitle, a navigation slot and an
 * actions row, finished with a hairline. Colors, typography, spacing, the glass fill and
 * the separator all resolve from [MillenniumTheme.topAppBarTheme], so it matches
 * [MillenniumScaffold]'s integrated bar and the rest of the system.
 */
@Composable
fun MillenniumTopAppBar(
    title: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    subtitle: (@Composable () -> Unit)? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = WindowInsets(0, 0, 0, 0),
) {
    val colors = MillenniumTheme.colors
    val properties = MillenniumTheme.topAppBarTheme.properties
    Column(
        modifier
            .fillMaxWidth()
            .background(properties.background ?: millenniumPanelBrush(colors))
            .windowInsetsPadding(windowInsets),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .heightIn(min = properties.minimumHeight)
                .padding(properties.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MillenniumStyledContent(
                properties.titleStyle,
                properties.navigationForeground ?: colors.text,
            ) { navigationIcon() }
            Spacer(Modifier.width(properties.navigationSpacing))
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(1.dp),
            ) {
                MillenniumStyledContent(properties.titleStyle, properties.titleForeground ?: colors.text) { title() }
                if (subtitle != null) {
                    MillenniumStyledContent(
                        properties.subtitleStyle,
                        properties.subtitleForeground ?: colors.muted,
                    ) { subtitle() }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(properties.actionSpacing),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MillenniumStyledContent(
                    properties.titleStyle,
                    properties.actionForeground ?: colors.text,
                ) { actions() }
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(properties.separatorColor ?: colors.border),
        )
    }
}

@Preview
@Composable
private fun MillenniumTopAppBarPreview() = MillenniumPreview {
    MillenniumTopAppBar(
        title = { MillenniumText("Millennium") },
        subtitle = { MillenniumText("A familiar feeling.") },
        navigationIcon = { MillenniumIconButton(MillenniumIcons.Sparkle, "Home", {}, style = MillenniumIconButtonStyle.Quiet) },
        actions = {
            MillenniumIconButton(MillenniumIcons.Sparkle, "Favorite", {}, style = MillenniumIconButtonStyle.Quiet)
            MillenniumIconButton(MillenniumIcons.Chevron, "More", {}, style = MillenniumIconButtonStyle.Quiet)
        },
    )
}
