package org.example.project.platform

/**
 * Web audio player using the Web Audio API.
 *
 * Place in: shared/src/jsMain/kotlin/org/example/project/platform/
 * (or webApp/src/jsMain/ — adjust for your module structure)
 *
 * For wasmJsMain, you may need to adjust the external declarations
 * depending on the Kotlin/Wasm JS interop available in your version.
 *
 * NOTE: This file uses Kotlin/JS interop. For Kotlin/Wasm targets,
 * use the JsFun/JsExport approach shown in WebAudioPlayerWasm.kt.
 */

// External declarations for Web Audio API
external class AudioContext {
    val sampleRate: Float
    val destination: dynamic
    fun createBuffer(numChannels: Int, length: Int, sampleRate: Int): dynamic
    fun createBufferSource(): dynamic
    fun close()
}

class WebAudioPlayer : PlatformAudio {

    private var audioContext: dynamic = null
    private var sourceNode: dynamic = null
    private var playing = false

    override fun playSamples(samples: FloatArray, sampleRate: Int) {
        stop()

        try {
            val ctx = js("new AudioContext()")
            audioContext = ctx

            val buffer = ctx.createBuffer(1, samples.size, sampleRate)
            val channelData = buffer.getChannelData(0)

            // Copy samples into the AudioBuffer
            for (i in samples.indices) {
                channelData[i] = samples[i]
            }

            val source = ctx.createBufferSource()
            source.buffer = buffer
            source.connect(ctx.destination)

            source.onended = {
                playing = false
            }

            sourceNode = source
            playing = true
            source.start(0)
        } catch (e: Exception) {
            console.log("WebAudioPlayer error: ${e.message}")
            playing = false
        }
    }

    override fun stop() {
        playing = false
        try {
            sourceNode?.stop(0)
        } catch (_: dynamic) {}
        sourceNode = null
        try {
            audioContext?.close()
        } catch (_: dynamic) {}
        audioContext = null
    }

    override fun isPlaying(): Boolean = playing
}

private external val console: dynamic
