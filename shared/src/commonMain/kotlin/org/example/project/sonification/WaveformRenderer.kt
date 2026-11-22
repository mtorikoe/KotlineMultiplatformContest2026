package org.example.project.sonification

import kotlin.math.*

/**
 * Pure-Kotlin PCM sample generator.
 *
 * Takes a [StarSound] and renders it to a [FloatArray] of mono samples
 * at the given sample rate. This is 100 % shared code — no platform
 * dependencies — which is the core KMP value of the project.
 */
object WaveformRenderer {

    const val SAMPLE_RATE = 44_100

    /**
     * Render a full note (attack → decay → sustain → release) for one star.
     * Returns mono PCM samples in the range [−1, 1].
     */
    fun render(sound: StarSound, sampleRate: Int = SAMPLE_RATE): FloatArray {
        val totalSamples = (sound.durationSec * sampleRate).toInt()
        val samples = FloatArray(totalSamples)

        for (i in 0 until totalSamples) {
            val t = i.toFloat() / sampleRate           // time in seconds
            val envGain = envelopeAt(t, sound)

            // Fundamental
            var value = oscillator(sound.waveShape, sound.fundamentalHz, t)

            // Add harmonics
            for (h in sound.harmonics) {
                val hFreq = sound.fundamentalHz * h.multiplier
                // Use sine for harmonics to keep them smooth
                value += h.amplitude * sin(2f * PI.toFloat() * hFreq * t)
            }

            // Apply envelope and master amplitude
            samples[i] = (value * envGain * sound.amplitude)
                .coerceIn(-1f, 1f)
        }

        // Fade-out last 64 samples to avoid click
        val fadeLen = minOf(64, totalSamples)
        for (i in 0 until fadeLen) {
            val idx = totalSamples - fadeLen + i
            samples[idx] *= (fadeLen - i).toFloat() / fadeLen
        }

        return samples
    }

    /**
     * Render multiple stars as a chord (summed and normalized).
     * Stars are staggered by [staggerSec] for a more musical onset.
     */
    fun renderChord(
        sounds: List<StarSound>,
        staggerSec: Float = 0.3f,
        sampleRate: Int = SAMPLE_RATE
    ): FloatArray {
        if (sounds.isEmpty()) return FloatArray(0)

        val totalDuration = sounds.maxOf { it.durationSec } +
            staggerSec * (sounds.size - 1)
        val totalSamples = (totalDuration * sampleRate).toInt()
        val mixed = FloatArray(totalSamples)

        sounds.forEachIndexed { index, sound ->
            val offset = (staggerSec * index * sampleRate).toInt()
            val rendered = render(sound, sampleRate)
            for (i in rendered.indices) {
                val dest = offset + i
                if (dest < totalSamples) {
                    mixed[dest] += rendered[i]
                }
            }
        }

        // Normalize to prevent clipping
        val peak = mixed.maxOfOrNull { abs(it) } ?: 1f
        if (peak > 0.9f) {
            val scale = 0.85f / peak
            for (i in mixed.indices) mixed[i] *= scale
        }

        return mixed
    }

    // ── Oscillators ────────────────────────────────────────────

    private fun oscillator(shape: WaveShape, freqHz: Float, t: Float): Float {
        val phase = freqHz * t
        return when (shape) {
            WaveShape.SINE     -> sin(2f * PI.toFloat() * phase)
            WaveShape.TRIANGLE -> triangle(phase)
            WaveShape.SQUARE   -> square(phase)
            WaveShape.SAWTOOTH -> sawtooth(phase)
        }
    }

    /** Band-limited-ish triangle via arcsine of sine. */
    private fun triangle(phase: Float): Float =
        (2f / PI.toFloat()) * asin(sin(2f * PI.toFloat() * phase))

    /** Naive square: sign of sine. Softened slightly. */
    private fun square(phase: Float): Float {
        val s = sin(2f * PI.toFloat() * phase)
        // Soft-clip for less harsh sound
        return (2f / PI.toFloat()) * atan(s * 4f)
    }

    /** Sawtooth: 2 × (phase mod 1) − 1. */
    private fun sawtooth(phase: Float): Float {
        val p = phase - floor(phase)
        return 2f * p - 1f
    }

    // ── ADSR envelope ──────────────────────────────────────────

    private fun envelopeAt(t: Float, sound: StarSound): Float {
        val env = sound.envelope
        val attackEnd  = env.attack
        val decayEnd   = attackEnd + env.decay
        val sustainEnd = sound.durationSec - env.release

        return when {
            t < 0f         -> 0f
            t < attackEnd  -> t / env.attack                                    // ramp up
            t < decayEnd   -> 1f - (1f - env.sustain) * (t - attackEnd) / env.decay  // ramp down
            t < sustainEnd -> env.sustain                                       // hold
            t < sound.durationSec -> {
                val releaseProgress = (t - sustainEnd) / env.release
                env.sustain * (1f - releaseProgress)                            // ramp to 0
            }
            else -> 0f
        }
    }
}
