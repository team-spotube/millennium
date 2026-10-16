package sample.app.catalog.demos

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.composeunstyled.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.spotube.millennium.composables.*
import org.spotube.millennium.theme.MillenniumTheme
import org.spotube.millennium.theme.MillenniumPalette
import org.spotube.millennium.theme.gelBrush


@Composable
internal fun DemoHint(text: String) {
    MillenniumText(text, size = 11.sp, color = MillenniumTheme.colors.muted)
}

/** A labelled specimen: a small caption above the thing being demonstrated. */
@Composable
internal fun Variant(name: String, content: @Composable ColumnScope.() -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        MillenniumText(name, size = 11.sp, weight = FontWeight.SemiBold, color = MillenniumTheme.colors.muted)
        content()
    }
}
