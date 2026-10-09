import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import sample.app.App
import java.awt.Dimension

fun main() = application {
    Window(
        title = "Millennium — yesterday’s charm, tomorrow’s interface",
        state = rememberWindowState(width = 1280.dp, height = 900.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        App()
    }
}
