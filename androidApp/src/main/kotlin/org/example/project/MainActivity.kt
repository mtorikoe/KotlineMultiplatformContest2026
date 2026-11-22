package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.platform.AndroidAudioPlayer
import org.example.project.ui.StellarSynthApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            // Create the Android audio player and release it when the
            // composition leaves the screen.
            val audio = remember { AndroidAudioPlayer() }
            DisposableEffect(Unit) {
                onDispose { audio.stop() }
            }
            StellarSynthApp(audio = audio)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    // Preview uses the default no-op audio (NoAudio) so it renders without
    // touching the Android audio stack.
    StellarSynthApp()
}
