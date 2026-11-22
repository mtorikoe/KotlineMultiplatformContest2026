package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.model.Star
import org.example.project.model.StarCatalog
import org.example.project.platform.PlatformAudio
import org.example.project.platform.NoAudio
import org.example.project.sonification.SonificationEngine
import org.example.project.sonification.StarSound
import org.example.project.sonification.WaveformRenderer

enum class Tab { HOME, CATALOG, ORIGINAL }

private const val SAMPLE_RATE = 44100

@Composable
fun StellarSynthApp(audio: PlatformAudio = NoAudio) {
    var tab by remember { mutableStateOf(Tab.HOME) }
    var openStar by remember { mutableStateOf<Star?>(null) }

    var isPlaying by remember { mutableStateOf(false) }
    var aiInterpretation by remember { mutableStateOf("") }
    var playingStarId by remember { mutableStateOf<String?>(null) }
    var combinedPlaying by remember { mutableStateOf(false) }
    var starsHeard by remember { mutableStateOf(0) }

    val today = remember { StarCatalog.findById("betelgeuse") ?: StarCatalog.stars.first() }

    val scope = rememberCoroutineScope()
    var revertJob by remember { mutableStateOf<Job?>(null) }

    // When playback naturally ends, flip controls back to their resting state.
    fun scheduleRevert(sampleCount: Int, onDone: () -> Unit) {
        revertJob?.cancel()
        revertJob = scope.launch {
            delay(sampleCount.toLong() * 1000 / SAMPLE_RATE + 150)
            onDone()
        }
    }

    fun stopPlaying() {
        revertJob?.cancel()
        audio.stop()
        isPlaying = false
        playingStarId = null
        combinedPlaying = false
    }

    fun playStar(star: Star) {
        stopPlaying()
        val sound = SonificationEngine.parameterize(star)
        val samples = WaveformRenderer.render(sound)
        audio.playSamples(samples, SAMPLE_RATE)
        isPlaying = true
        playingStarId = star.id
        aiInterpretation = generateLocalInterpretation(star, sound)
        starsHeard += 1
        scheduleRevert(samples.size) { isPlaying = false; playingStarId = null }
    }

    fun playCombined(stars: List<Star>) {
        if (stars.isEmpty()) return
        stopPlaying()
        val sounds = stars.map { SonificationEngine.parameterize(it) }
        val samples = WaveformRenderer.renderChord(sounds)
        audio.playSamples(samples, SAMPLE_RATE)
        combinedPlaying = true
        starsHeard += stars.size
        scheduleRevert(samples.size) { combinedPlaying = false }
    }

    ProvideAppFonts {
        Box(Modifier.fillMaxSize().background(PageBrush)) {

            Column(Modifier.fillMaxSize()) {
                Box(Modifier.fillMaxSize().weight(1f)) {
                    when (tab) {
                        Tab.HOME -> HomeScreen(
                            stars = StarCatalog.stars,
                            today = today,
                            onOpen = { openStar = it }
                        )
                        Tab.CATALOG -> CatalogScreen(
                            playingStarId = playingStarId,
                            onOpen = { openStar = it }
                        )
                        Tab.ORIGINAL -> OriginalScreen(
                            stars = StarCatalog.stars,
                            combinedPlaying = combinedPlaying,
                            onPlayCombined = { playCombined(it) },
                            onStop = { stopPlaying() }
                        )
                    }
                }
                BottomNav(
                    items = listOf(
                        NavItem("Home", IconHomeOutline, IconHomeFilled),
                        NavItem("Catalog", IconCatalogOutline, IconCatalogFilled),
                        NavItem("Original", IconOriginalOutline, IconOriginalFilled),
                    ),
                    selectedIndex = tab.ordinal,
                    onSelect = { tab = Tab.entries[it] }
                )
            }

            // Detail / player pushed over the tabs
            openStar?.let { star ->
                Box(Modifier.fillMaxSize().background(PageBrush)) {
                    PlayerScreen(
                        star = star,
                        isPlaying = isPlaying && playingStarId == star.id,
                        aiInterpretation = if (playingStarId == star.id) aiInterpretation.ifBlank { null } else null,
                        onPlay = { playStar(star) },
                        onStop = { stopPlaying() },
                        onBack = {
                            stopPlaying()
                            openStar = null
                        }
                    )
                }
            }
        }
    }
}

private fun generateLocalInterpretation(star: Star, sound: StarSound): String {
    val pitchDesc = when {
        sound.fundamentalHz < 130 -> "deep, resonant tone"
        sound.fundamentalHz < 330 -> "warm, mid-range voice"
        sound.fundamentalHz < 660 -> "bright, clear pitch"
        else -> "high, crystalline ring"
    }
    val timbreDesc = when (sound.waveShape.name) {
        "SINE" -> "pure and smooth"
        "TRIANGLE" -> "gentle with soft edges"
        "SQUARE" -> "bold and buzzy"
        "SAWTOOTH" -> "rich and brassy"
        else -> "unique"
    }
    val sizeDesc = when {
        star.radiusSolar > 100 -> "As a supergiant spanning ${formatRadius(star.radiusSolar)} solar radii, ${star.name} sings slowly — its enormous size stretches each note into a long, lingering drone."
        star.radiusSolar > 10 -> "${star.name} is a giant star, and its sound stretches out leisurely over ${formatRadius(star.radiusSolar)} solar radii of stellar atmosphere."
        star.radiusSolar > 1.5 -> "Larger than our Sun at ${formatRadius(star.radiusSolar)} solar radii, ${star.name} produces a comfortably paced melody."
        else -> "Compact at ${formatRadius(star.radiusSolar)} solar radii, ${star.name} plays quick, snappy notes."
    }
    return "${star.name} plays a $pitchDesc at ${sound.fundamentalHz.toInt()} Hz — its surface temperature of ${star.temperatureK}K places it in the ${star.spectralClass.name}-class, giving it a $timbreDesc timbre. $sizeDesc"
}

private fun formatRadius(r: Double): String = when {
    r >= 100 -> "${r.toInt()}"
    r >= 1   -> "${(r * 10).toInt() / 10.0}"
    else     -> "${(r * 100).toInt() / 100.0}"
}
