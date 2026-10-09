package org.spotube.millennium.composables

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledIcon
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MillenniumIcon(
    imageVector: ImageVector,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = MillenniumTheme.colors.text,
) = UnstyledIcon(imageVector, contentDescription, modifier.size(20.dp), tint)

/** Small, dependency-free vector set used by the gallery and themed controls. */
object MillenniumIcons {
    val Check = icon("Check") { moveTo(4f, 12f); lineTo(9f, 17f); lineTo(20f, 6f) }
    val Chevron = icon("Chevron") { moveTo(6f, 9f); lineTo(12f, 15f); lineTo(18f, 9f) }
    val Sparkle = icon("Sparkle") {
        moveTo(12f, 2f); lineTo(15f, 9f); lineTo(22f, 12f); lineTo(15f, 15f)
        lineTo(12f, 22f); lineTo(9f, 15f); lineTo(2f, 12f); lineTo(9f, 9f); close()
    }
    val Music = icon("Music") {
        moveTo(9f, 18f); lineTo(9f, 5f); lineTo(20f, 3f); lineTo(20f, 16f)
        moveTo(9f, 16f); curveTo(2f, 13f, 2f, 23f, 9f, 19f)
        moveTo(20f, 14f); curveTo(13f, 11f, 13f, 21f, 20f, 17f)
    }
    private fun icon(name: String, block: PathBuilder.() -> Unit) =
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f).apply {
            path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round, pathBuilder = block)
        }.build()
}

@Preview
@Composable
private fun MillenniumIconPreview() = MillenniumPreview {
    androidx.compose.foundation.layout.Row(
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(20.dp),
    ) {
        MillenniumIcon(MillenniumIcons.Sparkle, contentDescription = "Sparkle", tint = MillenniumTheme.colors.accent)
        MillenniumIcon(MillenniumIcons.Music, contentDescription = "Music")
        MillenniumIcon(MillenniumIcons.Check, contentDescription = "Check", tint = MillenniumTheme.colors.success)
    }
}
