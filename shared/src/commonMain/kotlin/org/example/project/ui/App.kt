package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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

enum class Screen { EXPLORE, PLAYER }

@Composable
fun StellarSynthApp(audio: PlatformAudio = NoAudio) {
    var screen by remember { mutableStateOf(Screen.EXPLORE) }
    var selectedStar by remember { mutableStateOf<Star?>(null) }
    var currentSound by remember { mutableStateOf<StarSound?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var aiInterpretation by remember { mutableStateOf("") }
    var playingStarId by remember { mutableStateOf<String?>(null) }

    fun playStar(star: Star) {
        val sound = SonificationEngine.parameterize(star)
        currentSound = sound
        val samples = WaveformRenderer.render(sound)
        audio.playSamples(samples, 44100)
        isPlaying = true
        playingStarId = star.id
        aiInterpretation = generateLocalInterpretation(star, sound)
    }

    fun stopPlaying() {
        audio.stop()
        isPlaying = false
        playingStarId = null
    }

    ProvideAppFonts {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SkyBrush)
    ) {
        when (screen) {
            Screen.EXPLORE -> ExploreScreen(
                playingStarId = playingStarId,
                onStarSelect = { star ->
                    selectedStar = star
                    screen = Screen.PLAYER
                }
            )
            Screen.PLAYER -> selectedStar?.let { star ->
                PlayerScreen(
                    star = star,
                    isPlaying = isPlaying,
                    aiInterpretation = aiInterpretation,
                    onPlay = { playStar(star) },
                    onStop = { stopPlaying() },
                    onBack = {
                        stopPlaying()
                        screen = Screen.EXPLORE
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