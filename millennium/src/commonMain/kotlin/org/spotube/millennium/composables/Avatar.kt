package org.spotube.millennium.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import com.composeunstyled.UnstyledAvatar
import org.spotube.millennium.theme.MillenniumColors
import org.spotube.millennium.theme.MillenniumControlState
import org.spotube.millennium.theme.MillenniumControlStateProperty
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.millenniumSurface

@Immutable
data class MillenniumAvatarThemeProperties(
    val colors: MillenniumColors,
    val background: Brush,
    val foreground: Color,
    val shape: Shape,
    val size: Dp,
    val textStyle: TextStyle,
    val borderColor: Color,
    val borderWidth: Dp,
    val shadowElevation: Dp,
    val shadowColor: Color,
    val innerGlowColor: Color,
    val sizeAnimationMillis: Int,
    val alpha: Float = 1f,
)

@Immutable
data class MillenniumAvatarTheme(
    val states: MillenniumControlStateProperty<MillenniumAvatarThemeProperties>,
)

fun defaultMillenniumAvatarTheme(colors: MillenniumColors): MillenniumAvatarTheme {
    val base = MillenniumAvatarThemeProperties(
        colors = colors,
        background = gelBrush(colors.accentTop, colors.accentBottom),
        foreground = colors.onAccent,
        shape = RoundedCornerShape(50),
        size = 48.dp,
        textStyle = TextStyle(fontWeight = FontWeight.Bold),
        borderColor = colors.accentBottom,
        borderWidth = 1.dp,
        shadowElevation = 3.dp,
        shadowColor = Color.Black.copy(alpha = 0.16f),
        innerGlowColor = colors.highlight.copy(alpha = 0.55f),
        sizeAnimationMillis = 140,
    )
    return MillenniumAvatarTheme(
        states = MillenniumControlStateProperty(
            default = base,
            hovered = base.copy(innerGlowColor = colors.highlight),
            focused = base.copy(borderColor = colors.accent, borderWidth = 2.dp),
            pressed = base.copy(shadowElevation = 1.dp),
            disabled = base.copy(alpha = 0.45f),
            selected = base.copy(borderColor = colors.accent),
            error = base.copy(borderColor = colors.danger, borderWidth = 2.dp),
        ),
    )
}

@Composable
fun MillenniumAvatar(
    initials: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    size: Dp? = null,
) {
    val properties = MillenniumTheme.avatarTheme.states.resolve(MillenniumControlState.Default)
    val targetSize = size ?: properties.size
    val animatedSize = animateDpAsState(targetSize, tween(properties.sizeAnimationMillis)).value
    UnstyledAvatar(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier.size(animatedSize).millenniumSurface(
            background = properties.background,
            shape = properties.shape,
            borderColor = properties.borderColor,
            borderWidth = properties.borderWidth,
            shadowElevation = properties.shadowElevation,
            shadowColor = properties.shadowColor,
            innerGlowColor = properties.innerGlowColor,
        ).alpha(properties.alpha),
        underlay = {
            val fontSize = properties.textStyle.fontSize.takeUnless { it == androidx.compose.ui.unit.TextUnit.Unspecified }
                ?: (animatedSize.value * 0.3f).sp
            Text(
                initials,
                style = properties.textStyle,
                color = properties.foreground,
                fontSize = fontSize,
            )
        },
    )
}

@Preview
@Composable
private fun MillenniumAvatarPreview() = MillenniumPreview {
    MillenniumAvatar(initials = "AJ", contentDescription = "Alex Jordan")
}
