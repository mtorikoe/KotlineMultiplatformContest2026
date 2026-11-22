package org.example.project.platform

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack

/**
 * Android audio player using AudioTrack in streaming mode.
 *
 * Place in: androidApp/src/main/kotlin/org/example/project/platform/
 * (or shared/src/androidMain/ if using expect/actual)
 */
class AndroidAudioPlayer : PlatformAudio {

    private var audioTrack: AudioTrack? = null
    private var playbackThread: Thread? = null
    @Volatile private var playing = false

    override fun playSamples(samples: FloatArray, sampleRate: Int) {
        stop()  // Stop any previous playback

        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_FLOAT
        )

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(maxOf(bufferSize, samples.size * 4))
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        track.write(samples, 0, samples.size, AudioTrack.WRITE_BLOCKING)
        track.setNotificationMarkerPosition(samples.size)
        track.setPlaybackPositionUpdateListener(
            object : AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onMarkerReached(t: AudioTrack?) {
                    playing = false
                }
                override fun onPeriodicNotification(t: AudioTrack?) {}
            }
        )

        audioTrack = track
        playing = true
        track.play()
    }

    override fun stop() {
        playing = false
        audioTrack?.let { track ->
            try {
                if (track.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    track.stop()
                }
                track.release()
            } catch (_: IllegalStateException) {
                // Already released
            }
        }
        audioTrack = null
    }

    override fun isPlaying(): Boolean = playing
}
