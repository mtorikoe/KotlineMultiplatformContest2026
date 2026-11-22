package org.example.project.platform

/**
 * Platform-specific audio playback.
 *
 * The common sonification engine produces PCM float arrays;
 * each platform implements this interface to push those samples
 * to its native audio output.
 *
 * Place in: shared/src/commonMain/kotlin/org/example/project/platform/
 */
interface PlatformAudio {
    /** Play mono PCM samples (values in [-1, 1]) at the given sample rate. */
    fun playSamples(samples: FloatArray, sampleRate: Int)

    /** Stop any currently playing audio. */
    fun stop()

    /** Whether audio is currently playing. */
    fun isPlaying(): Boolean
}

/**
 * No-op implementation for platforms without audio support.
 * Web can use this as a fallback if Web Audio API is unavailable.
 */
object NoAudio : PlatformAudio {
    override fun playSamples(samples: FloatArray, sampleRate: Int) {}
    override fun stop() {}
    override fun isPlaying(): Boolean = false
}
