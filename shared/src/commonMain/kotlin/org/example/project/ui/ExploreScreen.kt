package org.example.project.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.example.project.model.SpectralClass
import org.example.project.model.Star
import org.example.project.model.StarCatalog

@Composable
fun ExploreScreen(
    playingStarId: String?,
    onStarSelect: (Star) -> Unit,
    modifier: Modifier = Modifier
) {
    var filterClass by remember { mutableStateOf<SpectralClass?>(null) }
    val searchQuery by remember { mutableStateOf("") }

    val stars = remember(filterClass, searchQuery) {
        var list = if (searchQuery.isBlank()) {
            StarCatalog.stars
        } else {
            StarCatalog.search(searchQuery)
        }
        if (filterClass != null) {
            list = list.filter { it.spectralClass == filterClass }
        }
        list
    }

    val showFeatured = filterClass == null && searchQuery.isBlank()
    val featured = remember { StarCatalog.findById("betelgeuse") ?: StarCatalog.stars.first() }

    // ── Hero entrance: the header + featured card rise and fade in ──
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { shown = true }
    val enter by animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label = "heroEnter"
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = SpaceM, end = SpaceM, top = SpaceXxl, bottom = 96.dp
        )
    ) {
        // ── Masthead ──────────────────────────────────────────
        item {
            Column(
                Modifier.graphicsLayer {
                    alpha = enter
                    translationY = (1f - enter) * 48f
                }
            ) {
                Text("Tonight's Sky", style = TitleStyle)
                Spacer(Modifier.height(SpaceXs))
                Text(
                    "${StarCatalog.stars.size} stars, each with a voice of its own",
                    style = BodyStyle.copy(color = Dust, fontSize = CaptionStyle.fontSize)
                )

                if (showFeatured) {
                    Spacer(Modifier.height(SpaceXl))
                    FeaturedStar(star = featured, onClick = { onStarSelect(featured) })
                }
            }
        }

        // ── Spectral class filter ─────────────────────────────
        item {
            Spacer(Modifier.height(SpaceXl))
            SectionHeader("Spectral class")
            SpectralFilter(selected = filterClass, onSelect = { filterClass = it })
            Spacer(Modifier.height(SpaceXl))
            SectionHeader(if (showFeatured) "All stars" else "Filtered")
            Spacer(Modifier.height(SpaceS))
        }

        // ── Star list ─────────────────────────────────────────
        items(stars, key = { it.id }) { star ->
            StarListItem(
                star = star,
                isPlaying = star.id == playingStarId,
                onClick = { onStarSelect(star) }
            )
            Spacer(Modifier.height(SpaceS))
        }

        if (stars.isEmpty()) {
            item {
                Text(
                    "No stars match this filter",
                    style = BodyStyle.copy(color = Dust),
                    modifier = Modifier.padding(SpaceL)
                )
            }
        }
    }
}
