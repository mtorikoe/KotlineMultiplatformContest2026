package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.example.project.model.Star
import org.example.project.model.StarCatalog
import org.example.project.platform.PlatformAudio
import org.example.project.platform.NoAudio
import org.example.project.sonification.SonificationEngine
import org.example.project.sonification.StarSound
import org.example.project.sonification.WaveformRenderer

enum class Tab { HOME, CATALOG, ACCOUNT }

@Composable
fun StellarSynthApp(audio: PlatformAudio = NoAudio) {
    var tab by remember { mutableStateOf(Tab.HOME) }
    var openStar by remember { mutableStateOf<Star?>(null) }

    var isPlaying by remember { mutableStateOf(false) }
    var aiInterpretation by remember { mutableStateOf("") }
    var playingStarId by remember { mutableStateOf<String?>(null) }
    var starsHeard by remember { mutableStateOf(0) }

    val today = remember { StarCatalog.findById("betelgeuse") ?: StarCatalog.stars.first() }

    fun playStar(star: Star) {
        val sound = SonificationEngine.parameterize(star)
        audio.playSamples(WaveformRenderer.render(sound), 44100)
        isPlaying = true
        playingStarId = star.id
        aiInterpretation = generateLocalInterpretation(star, sound)
        starsHeard += 1
    }

    fun stopPlaying() {
        audio.stop()
        isPlaying = false
        playingStarId = null
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
                        Tab.ACCOUNT -> AccountScreen(starsHeard = starsHeard)
                    }
                }
                BottomNav(
                    tabs = listOf("Home", "Catalog", "Account"),
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
