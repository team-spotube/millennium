package org.spotube.millennium.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composeunstyled.Indicator
import com.composeunstyled.UnstyledProgress
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.beveled
import org.spotube.millennium.theme.gelBrush
import org.spotube.millennium.theme.MillenniumPreview
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MillenniumProgress(progress: Float, modifier: Modifier = Modifier) {
    val colors = MillenniumTheme.colors
    UnstyledProgress(
        progress.coerceIn(0f, 1f),
        modifier.fillMaxWidth().height(12.dp).beveled(colors, CircleShape, sunken = true)
    ) {
        Indicator(
            Modifier.fillMaxHeight().background(
                gelBrush(
                    colors.accentTop,
                    colors.accentBottom
                ), CircleShape
            )
        )
    }
}

@Composable
fun MillenniumIndeterminateProgress(modifier: Modifier = Modifier) {
    val colors = MillenniumTheme.colors
    val transition = rememberInfiniteTransition()
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1400), RepeatMode.Reverse),
    )
    UnstyledProgress(
        modifier = modifier.fillMaxWidth().height(12.dp).beveled(colors, CircleShape, sunken = true)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = BiasAlignment(offset * 2f - 1f, 0f)) {
            Box(
                Modifier.fillMaxWidth(0.3f).fillMaxHeight().background(
                    gelBrush(
                        colors.accentTop,
                        colors.accentBottom
                    ), CircleShape
                )
            )
        }
    }
}

@Preview
@Composable
private fun MillenniumProgressPreview() = MillenniumPreview {
    MillenniumProgress(0.68f)
    MillenniumIndeterminateProgress()
}
