package org.spotube.millennium.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.DrawerPlacement
import com.composeunstyled.DrawerSnapPoint
import com.composeunstyled.DrawerSnapPoints
import com.composeunstyled.Overlay
import com.composeunstyled.Panel
import com.composeunstyled.UnstyledDrawer
import com.composeunstyled.UnstyledDrawerState
import com.composeunstyled.Viewport
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.MillenniumStyledContent
import org.spotube.millennium.theme.millenniumBevelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumDrawerThemeProperties(
    val colors: MillenniumColors,
    val background: Brush,
    val foreground: Color,
    val shape: Shape,
    val maximumWidth: Dp,
    val contentPadding: PaddingValues,
    val contentSpacing: Dp,
    val borderColor: Color,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val innerGlowColor: Color,
    val scrimColor: Color,
    val alpha: Float,
)

@Immutable
data class MillenniumDrawerTheme(
    val states: MillenniumControlStateProperty<MillenniumDrawerThemeProperties>,
)

fun defaultMillenniumDrawerTheme(colors: MillenniumColors): MillenniumDrawerTheme {
    val base = MillenniumDrawerThemeProperties(
        colors = colors,
        background = millenniumBevelBrush(colors),
        foreground = colors.text,
        shape = RoundedCornerShape(topEnd = 18.dp, bottomEnd = 18.dp),
        maximumWidth = 320.dp,
        contentPadding = PaddingValues(24.dp),
        contentSpacing = 20.dp,
        borderColor = colors.border,
        borderWidth = 1.dp,
        shadowElevation = 12.dp,
        innerGlowColor = colors.highlight.copy(alpha = 0.55f),
        scrimColor = colors.scrim,
        alpha = 1f,
    )
    return MillenniumDrawerTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(borderColor = colors.accent),
            focused = base.copy(borderColor = colors.accent, borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 8.dp),
            disabled = base.copy(alpha = 0.55f),
            selected = base.copy(borderColor = colors.accent),
            error = base.copy(borderColor = colors.danger, borderWidth = 2.dp),
        ),
    )
}

enum class MillenniumDrawerValue { Closed, Open }

@Composable
fun rememberMillenniumDrawerState(): UnstyledDrawerState<MillenniumDrawerValue> = remember {
    UnstyledDrawerState(
        initialValue = MillenniumDrawerValue.Closed,
        snapPoints = DrawerSnapPoints {
            MillenniumDrawerValue.Closed at DrawerSnapPoint.Zero
            MillenniumDrawerValue.Open at DrawerSnapPoint.ContentSize
        },
    )
}

@Composable
fun MillenniumDrawer(
    state: UnstyledDrawerState<MillenniumDrawerValue>,
    content: @Composable ColumnScope.() -> Unit,
) {
    val properties = MillenniumTheme.drawerTheme.states.resolve(MillenniumControlState.Default)
    UnstyledDrawer(
        state = state,
        placement = DrawerPlacement.Start,
        presentation = com.composeunstyled.DrawerPresentation.Modal,
        overlay = { Overlay { androidx.compose.foundation.layout.Box(Modifier.fillMaxSize().background(properties.scrimColor)) } },
    ) {
        Viewport(Modifier.fillMaxSize()) {
            Panel(
                Modifier.widthIn(max = properties.maximumWidth).fillMaxWidth().fillMaxHeight()
                    .millenniumSurface(
                        background = properties.background,
                        shape = properties.shape,
                        borderColor = properties.borderColor,
                        borderWidth = properties.borderWidth,
                        shadowElevation = properties.shadowElevation,
                        innerGlowColor = properties.innerGlowColor,
                    ).alpha(properties.alpha),
            ) {
                Column(
                    Modifier.padding(properties.contentPadding).systemBarsPadding(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(properties.contentSpacing),
                ) {
                    MillenniumStyledContent(TextStyle(color = properties.foreground), properties.foreground) {
                        content()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumDrawerPreview() = MillenniumPreview {
    val state = rememberMillenniumDrawerState()
    LaunchedEffect(state) { state.targetValue = MillenniumDrawerValue.Open }
    MillenniumDrawer(state) {
        MillenniumText("A little more space", weight = FontWeight.SemiBold)
        MillenniumText("A softly raised edge drawer.", color = MillenniumTheme.drawerTheme.states.default.colors.muted)
    }
}
