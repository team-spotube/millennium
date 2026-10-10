package org.spotube.millennium.composables

import androidx.compose.animation.*
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.composeunstyled.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview
import org.spotube.millennium.theme.MillenniumText
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumComponentContent

@Composable
fun MillenniumDisclosure(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val disclosureTheme = MillenniumTheme.disclosureTheme
    val colors = MillenniumTheme.colorsFor(disclosureTheme)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val focused by interaction.collectIsFocusedAsState()
    val hovered by interaction.collectIsHoveredAsState()
    val state = when {
        pressed -> MillenniumVisualState.Clicked
        focused -> MillenniumVisualState.Focused
        hovered -> MillenniumVisualState.Hovered
        expanded -> MillenniumVisualState.Selected
        else -> MillenniumVisualState.Default
    }
    val visual = MillenniumTheme.disclosureTheme.resolve(
        state,
        MillenniumVisualStyle(backgroundColor = colors.surface, foregroundColor = colors.text, borderColor = colors.border,
            borderWidth = disclosureTheme.strokes.border, innerGlowColor = colors.highlight),
    )
    val shape = visual.shape ?: disclosureTheme.shapes.control
    UnstyledDisclosure(expanded, onExpandedChange, modifier.beveled(colors, shape).componentStyle(visual, shape)) {
        Column {
            DisclosureButton(
                Modifier.fillMaxWidth().heightIn(min = disclosureTheme.dimensions.disclosureMinHeight)
                    .componentStyle(visual, shape),
                contentPadding = visual.contentPadding ?: PaddingValues(disclosureTheme.spacing.medium),
                indication = LocalIndication.current,
                interactionSource = interaction,
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(title, modifier = Modifier.weight(1f), color = visual.foregroundColor ?: colors.text)
                    MillenniumIcon(MillenniumIcons.Chevron, modifier = Modifier.rotate(if (expanded) 180f else 0f))
                }
            }
            DisclosedContent(enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
                Column(Modifier.padding(disclosureTheme.spacing.large), verticalArrangement = Arrangement.spacedBy(disclosureTheme.spacing.medium)) {
                    MillenniumComponentContent(visual) { content() }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MillenniumDisclosurePreview() = MillenniumPreview {
    MillenniumDisclosure(expanded = true, onExpandedChange = {}, title = "Why the glossy details?") {
        MillenniumText("Familiar depth and a little shine, with the accessibility and flexibility of modern Compose.")
    }
}
