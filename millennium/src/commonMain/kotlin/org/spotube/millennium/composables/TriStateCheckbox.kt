package org.spotube.millennium.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.composeunstyled.LocalTextStyle
import com.composeunstyled.Text
import com.composeunstyled.UnstyledTriStateCheckbox
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumBevelBrush

@Immutable
data class MillenniumTriStateCheckboxThemeProperties(
    override val faceBackground: Brush? = null,
    override val selectedFaceBackground: Brush? = null,
    override val selectedFaceForeground: Color? = null,
    override val faceShape: Shape,
    override val faceSize: DpSize,
    override val markSize: Dp,
    override val dashSize: DpSize,
    override val faceBorderColor: Color? = null,
    override val faceBorderWidth: Dp,
    override val faceGlowColor: Color? = null,
    override val faceElevation: Dp,
    override val faceAlpha: Float,
    override val stateChangeMillis: Int,
    override val labelForeground: Color? = null,
    override val labelStyle: TextStyle,
    override val minimumTouchHeight: Dp,
    override val contentPadding: PaddingValues,
    override val contentSpacing: Dp,
) : MillenniumCheckFaceStyle

@Immutable
data class MillenniumTriStateCheckboxTheme(
    val states: MillenniumControlStateProperty<MillenniumTriStateCheckboxThemeProperties>,
)

fun defaultMillenniumTriStateCheckboxTheme(): MillenniumTriStateCheckboxTheme {
    val base = MillenniumTriStateCheckboxThemeProperties(
        faceShape = androidx.compose.foundation.shape.RoundedCornerShape(5.dp),
        faceSize = DpSize(22.dp, 22.dp),
        markSize = 16.dp,
        dashSize = DpSize(11.dp, 2.dp),
        faceBorderWidth = 1.dp,
        faceElevation = 0.dp,
        faceAlpha = 1f,
        stateChangeMillis = 120,
        labelStyle = TextStyle(fontWeight = FontWeight.Normal),
        minimumTouchHeight = 44.dp,
        contentPadding = PaddingValues(0.dp),
        contentSpacing = 12.dp,
    )
    return MillenniumTriStateCheckboxTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base,
            focused = base.copy(faceBorderWidth = 2.dp),
            pressed = base.copy(faceElevation = 0.dp),
            disabled = base.copy(faceAlpha = 0.45f),
            selected = base,
            error = base.copy(faceBorderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumTriStateCheckbox(
    value: ToggleableState,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val focused by source.collectIsFocusedAsState()
    val hovered by source.collectIsHoveredAsState()
    val state = when {
        !enabled -> MillenniumControlState.Disabled
        pressed -> MillenniumControlState.Pressed
        focused -> MillenniumControlState.Focused
        hovered -> MillenniumControlState.Hovered
        value != ToggleableState.Off -> MillenniumControlState.Selected
        else -> MillenniumControlState.Default
    }
    val properties = MillenniumTheme.triStateCheckboxTheme.states.resolve(state)
    val labelColor = properties.labelForeground ?: when (state) {
        MillenniumControlState.Disabled -> MillenniumTheme.colors.muted
        else -> MillenniumTheme.colors.text
    }
    UnstyledTriStateCheckbox(
        value = value,
        onClick = onClick,
        modifier = modifier.heightIn(min = properties.minimumTouchHeight),
        enabled = enabled,
        accessibilityLabel = label,
        indication = LocalIndication.current,
        interactionSource = source,
    ) {
        Row(
            modifier = Modifier.padding(properties.contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(properties.contentSpacing),
        ) {
            MillenniumCheckFace(value, properties, state)
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.merge(properties.labelStyle)
                    .copy(color = labelColor),
            ) { Text(label) }
        }
    }
}

@Preview
@Composable
private fun MillenniumTriStateCheckboxPreview() = MillenniumPreview {
    MillenniumTriStateCheckbox(value = ToggleableState.Indeterminate, onClick = {}, label = "Select the collection")
}
