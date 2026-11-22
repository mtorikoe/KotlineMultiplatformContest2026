package org.example.project.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.project.model.Star
import org.example.project.sonification.SonificationEngine

@Composable
fun PlayerScreen(
    star: Star,
    isPlaying: Boolean,
    aiInterpretation: String?,
    onPlay: () -> Unit,
    onStop: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sound = remember(star) { SonificationEngine.parameterize(star) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = SpaceM),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = SpaceXxl),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                "← Back",
                style = LabelStyle.copy(color = InkSoft, letterSpacing = DataStyle.letterSpacing),
                modifier = Modifier
                    .clip(RoundedCornerShape(CornerS))
                    .clickable(onClick = onBack)
                    .padding(SpaceS)
            )
        }

        Spacer(Modifier.height(SpaceXl))

        StarVisualization(star = star, isPlaying = isPlaying)

        Spacer(Modifier.height(SpaceXl))

        Text(star.name, style = TitleStyle, textAlign = TextAlign.Center)
        Spacer(Modifier.height(SpaceXs))
        Text("${star.spectralType}  ·  ${star.constellation}", style = CaptionStyle, textAlign = TextAlign.Center)

        Spacer(Modifier.height(SpaceL))

        Text(
            star.description,
            style = BodyStyle.copy(color = InkSoft),
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = MaxContentWidth)
        )

        Spacer(Modifier.height(SpaceXl))

        FlatButton(
            label = if (isPlaying) "Stop" else "Listen",
            onClick = { if (isPlaying) onStop() else onPlay() }
        )

        Spacer(Modifier.height(SpaceXl))

        SectionHeader("Physical properties")
        DataPanel(Modifier.widthIn(max = MaxContentWidth)) {
            DataRow {
                DataCell("Temperature", "${star.temperatureK} K")
                DataCell("Luminosity", "${formatNumber(star.luminositySolar)} L☉")
                DataCell("Radius", "${formatNumber(star.radiusSolar)} R☉")
            }
            Spacer(Modifier.height(SpaceM))
            DataRow {
                DataCell("Distance", "${formatNumber(star.distanceLy)} ly")
                DataCell("Magnitude", "${star.apparentMag}")
                DataCell("Class", star.spectralType)
            }
        }

        Spacer(Modifier.height(SpaceXl))

        SectionHeader("Sound mapping")
        DataPanel(Modifier.widthIn(max = MaxContentWidth)) {
            DataRow {
                DataCell("Pitch", "${sound.fundamentalHz.toInt()} Hz")
                DataCell("Waveform", sound.waveShape.name.lowercase())
                DataCell("Duration", "${(sound.durationSec * 10).toInt() / 10.0} s")
            }
            Spacer(Modifier.height(SpaceM))
            DataRow {
                DataCell("Harmonics", "${sound.harmonics.size}")
                DataCell("Attack", "${(sound.envelope.attack * 100).toInt() / 100.0} s")
                DataCell("Sustain", "${(sound.envelope.sustain * 100).toInt()}%")
            }
        }

        if (aiInterpretation != null) {
            Spacer(Modifier.height(SpaceXl))
            SectionHeader("Field notes")
            Text(
                aiInterpretation,
                style = BodyStyle,
                modifier = Modifier
                    .widthIn(max = MaxContentWidth)
                    .glass(RoundedCornerShape(CornerM))
                    .padding(SpaceL)
            )
        }

        Spacer(Modifier.height(SpaceXxl))
    }
}

@Composable
private fun StarVisualization(star: Star, isPlaying: Boolean) {
    val fieldSize = 220.dp

    var shown by remember(star) { mutableStateOf(false) }
    LaunchedEffect(star) { shown = true }
    val appear by animateFloatAsState(
        if (shown) 1f else 0f, tween(600, easing = FastOutSlowInEasing), label = "appear"
    )

    val pulse by if (isPlaying) {
        val inf = rememberInfiniteTransition()
        inf.animateFloat(
            1f, 1.08f,
            infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse)
        )
    } else remember { mutableStateOf(1f) }

    Box(Modifier.size(fieldSize), contentAlignment = Alignment.Center) {
        StarImageCircle(
            star = star,
            size = fieldSize,
            modifier = Modifier.graphicsLayer {
                alpha = appear
                val s = (0.85f + 0.15f * appear) * pulse
                scaleX = s; scaleY = s
            }
        )
    }
}

@Composable
private fun DataPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .glass(RoundedCornerShape(CornerM))
            .padding(SpaceL),
        content = content
    )
}

@Composable
private fun DataRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        content = content
    )
}

@Composable
private fun RowScope.DataCell(label: String, value: String) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, style = DataStyle, textAlign = TextAlign.Center)
        Spacer(Modifier.height(SpaceXs))
        Text(label.uppercase(), style = LabelStyle, textAlign = TextAlign.Center)
    }
}

private fun formatNumber(n: Double): String = when {
    n >= 1_000_000 -> "${(n / 1_000_000 * 10).toInt() / 10.0}M"
    n >= 1_000     -> "${(n / 1_000 * 10).toInt() / 10.0}K"
    n >= 10        -> "${n.toInt()}"
    n >= 1         -> "${(n * 10).toInt() / 10.0}"
    else           -> "${(n * 10000).toInt() / 10000.0}"
}
