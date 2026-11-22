package org.example.project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.model.SpectralClass
import org.example.project.model.Star
import org.example.project.model.StarCatalog

@Composable
fun CatalogScreen(
    playingStarId: String?,
    onOpen: (Star) -> Unit,
    modifier: Modifier = Modifier
) {
    var filterClass by remember { mutableStateOf<SpectralClass?>(null) }

    val stars = remember(filterClass) {
        if (filterClass == null) StarCatalog.stars
        else StarCatalog.stars.filter { it.spectralClass == filterClass }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = SpaceM, end = SpaceM, top = SpaceXxl, bottom = SpaceXl)
    ) {
        item {
            Text("Catalogue", style = TitleStyle)
            Spacer(Modifier.height(SpaceXs))
            Text(
                "${StarCatalog.stars.size} stars, every spectral class",
                style = CaptionStyle
            )
            Spacer(Modifier.height(SpaceXl))
            SectionHeader("Spectral class")
            SpectralFilter(selected = filterClass, onSelect = { filterClass = it })
            Spacer(Modifier.height(SpaceXl))
            SectionHeader(if (filterClass == null) "All stars" else "Class ${filterClass!!.label}")
            Spacer(Modifier.height(SpaceS))
        }

        items(stars, key = { it.id }) { star ->
            StarListItem(
                star = star,
                isPlaying = star.id == playingStarId,
                onClick = { onOpen(star) }
            )
            Spacer(Modifier.height(SpaceS))
        }

        if (stars.isEmpty()) {
            item {
                Text("No stars in this class", style = CaptionStyle, modifier = Modifier.padding(SpaceL))
            }
        }
    }
}
