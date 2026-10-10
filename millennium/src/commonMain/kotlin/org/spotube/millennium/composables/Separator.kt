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
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview

@Immutable
data class MillenniumSeparatorThemeProperties(
    val color: androidx.compose.ui.graphics.Color? = null,
    val thickness: Dp,
    val alpha: Float,
)

@Immutable
data class MillenniumSeparatorTheme(
    val states: MillenniumControlStateProperty<MillenniumSeparatorThemeProperties>,
)

fun defaultMillenniumSeparatorTheme(): MillenniumSeparatorTheme {
    val base = MillenniumSeparatorThemeProperties(thickness = 1.dp, alpha = 1f)
    return MillenniumSeparatorTheme(
        MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(thickness = 2.dp),
            pressed = base.copy(alpha = 0.7f),
            disabled = base.copy(alpha = 0.4f),
            selected = base,
            error = base,
        ),
    )
}

@Composable
fun MillenniumSeparator(modifier: Modifier = Modifier, vertical: Boolean = false) {
    val properties = MillenniumTheme.separatorTheme.states.resolve(MillenniumControlState.Default)
    val color = properties.color ?: MillenniumTheme.colors.border
    if (vertical) {
        UnstyledVerticalSeparator(color, modifier.alpha(properties.alpha), properties.thickness)
    } else {
        UnstyledHorizontalSeparator(color, modifier.alpha(properties.alpha), properties.thickness)
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
