package org.spotube.millennium.composables

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import com.composeunstyled.UnstyledAvatar
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.MillenniumVisualState
import org.spotube.millennium.theme.MillenniumVisualStyle
import org.spotube.millennium.theme.componentStyle
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.resolve
import org.spotube.millennium.theme.MillenniumPreview

@Composable
fun MillenniumAvatar(
    initials: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    size: Dp? = null,
) {
    val avatarTheme = MillenniumTheme.avatarTheme
    val avatarSize = size ?: avatarTheme.dimensions.avatarSize
    val colors = MillenniumTheme.colorsFor(avatarTheme)
    val visual = MillenniumTheme.avatarTheme.resolve(
        MillenniumVisualState.Default,
        MillenniumVisualStyle(background = gelBrush(colors.accentTop, colors.accentBottom), foregroundColor = colors.onAccent,
            shadowElevation = avatarTheme.elevation.avatar, borderColor = colors.accentBottom,
            borderWidth = avatarTheme.strokes.border, innerGlowColor = colors.highlight),
    )
    val avatarShape = visual.shape ?: avatarTheme.shapes.avatar
    UnstyledAvatar(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.size(avatarSize)
            .beveled(colors, avatarShape, accent = true).componentStyle(visual, avatarShape),
        underlay = {
            Text(initials, style = visual.textStyle ?: avatarTheme.typography.avatar,
                color = visual.foregroundColor ?: colors.onAccent,
                fontSize = visual.textStyle?.fontSize?.takeUnless { it == androidx.compose.ui.unit.TextUnit.Unspecified }
                    ?: avatarTheme.typography.avatar.fontSize.takeUnless { it == androidx.compose.ui.unit.TextUnit.Unspecified }
                    ?: (avatarSize.value * 0.3f).sp)
        },
    )
}

@Preview
@Composable
private fun MillenniumAvatarPreview() = MillenniumPreview {
    MillenniumAvatar(initials = "AJ", contentDescription = "Alex Jordan")
}
