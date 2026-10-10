package org.spotube.millennium.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledHorizontalSeparator
import com.composeunstyled.UnstyledVerticalSeparator
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview

@Immutable
data class MillenniumSeparatorThemeProperties(
    val colors: MillenniumColors,
    val color: androidx.compose.ui.graphics.Color,
    val thickness: Dp,
    val alpha: Float,
)

@Immutable
data class MillenniumSeparatorTheme(
    val states: MillenniumControlStateProperty<MillenniumSeparatorThemeProperties>,
)

fun defaultMillenniumSeparatorTheme(colors: MillenniumColors): MillenniumSeparatorTheme {
    val base = MillenniumSeparatorThemeProperties(colors, colors.border, 1.dp, 1f)
    return MillenniumSeparatorTheme(
        MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(color = colors.accent),
            focused = base.copy(color = colors.accent, thickness = 2.dp),
            pressed = base.copy(alpha = 0.7f),
            disabled = base.copy(alpha = 0.4f),
            selected = base.copy(color = colors.accent),
            error = base.copy(color = colors.danger),
        ),
    )
}

@Composable
fun MillenniumSeparator(modifier: Modifier = Modifier, vertical: Boolean = false) {
    val properties = MillenniumTheme.separatorTheme.states.resolve(MillenniumControlState.Default)
    if (vertical) {
        UnstyledVerticalSeparator(properties.color, modifier.alpha(properties.alpha), properties.thickness)
    } else {
        UnstyledHorizontalSeparator(properties.color, modifier.alpha(properties.alpha), properties.thickness)
    }
}

@Preview
@Composable
private fun MillenniumSeparatorPreview() = MillenniumPreview {
    MillenniumText("Above the line")
    MillenniumSeparator()
    MillenniumText("Below the line")
    Column {
        MillenniumText("Leading edge")
        MillenniumSeparator(Modifier.height(28.dp), vertical = true)
        MillenniumText("Trailing edge")
    }
}
