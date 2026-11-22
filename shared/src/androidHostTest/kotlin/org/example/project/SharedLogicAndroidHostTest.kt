package org.example.project

import org.example.project.model.StarCatalog
import org.example.project.sonification.SonificationEngine
import org.example.project.sonification.WaveformRenderer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharedLogicAndroidHostTest {

    @Test
    fun renderProducesExpectedSampleCount() {
        val star = StarCatalog.findById("sun")!!
        val sound = SonificationEngine.parameterize(star)
        val samples = WaveformRenderer.render(sound, sampleRate = 44_100)
        val expected = (sound.durationSec * 44_100).toInt()
        assertEquals(expected, samples.size)
    }

    @Test
    fun samplesStayWithinRange() {
        val star = StarCatalog.findById("betelgeuse")!!
        val sound = SonificationEngine.parameterize(star)
        val samples = WaveformRenderer.render(sound)
        assertTrue(samples.all { it in -1f..1f }, "samples must be normalized to [-1, 1]")
    }
}
