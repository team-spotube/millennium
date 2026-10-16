package sample.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import sample.app.catalog.CatalogApp

/** Entry point: the design study, with a link to the component catalog (and back). */
@Composable
fun App() {
    var study by rememberSaveable { mutableStateOf(true) }
    if (study) {
        AeroDesignStudy(onOpenGallery = { study = false })
    } else {
        CatalogApp(onOpenStudy = { study = true })
    }
}
