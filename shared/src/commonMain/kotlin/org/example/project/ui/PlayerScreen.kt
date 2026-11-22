package org.example.project.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    val starColor = star.spectralClass.starColor.toColor()
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SpaceXxl),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                "← Back",
                style = LabelStyle.copy(color = Dust, letterSpacing = DataStyle.letterSpacing),
                modifier = Modifier
                    .clip(RoundedCornerShape(CornerS))
                    .clickable(onClick = onBack)
                    .padding(SpaceS)
            )
        }

        Spacer(Modifier.height(SpaceXl))

        // ── Star visualization (fixed footprint; pulse scales within) ──
        StarVisualization(
            star = star,
            isPlaying = isPlaying,
            starColor = starColor
        )

        Spacer(Modifier.height(SpaceXl))

        // Star name & classification
        Text(star.name, style = TitleStyle, textAlign = TextAlign.Center)
        Spacer(Modifier.height(SpaceXs))
        Text(
            "${star.spectralType}  ·  ${star.constellation}",
            style = CaptionStyle,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(SpaceL))

        // Description
        Text(
            star.description,
            style = BodyStyle.copy(color = Dust),
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = MaxContentWidth)
        )

        Spacer(Modifier.height(SpaceXl))

        // ── Play / Stop button ─────────────────────────────────
        FlatButton(
            label = if (isPlaying) "Stop" else "Listen",
            color = if (isPlaying) Copper else Amber,
            onClick = { if (isPlaying) onStop() else onPlay() }
        )

        Spacer(Modifier.height(SpaceXl))

        // ── Star data cards ────────────────────────────────────
        SectionHeader("Physical properties")
        DataPanel(modifier = Modifier.widthIn(max = MaxContentWidth)) {
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

        // ── Sonification info ──────────────────────────────────
        SectionHeader("Sound mapping")
        DataPanel(modifier = Modifier.widthIn(max = MaxContentWidth)) {
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

        // ── AI interpretation ──────────────────────────────────
        if (aiInterpretation != null) {
            Spacer(Modifier.height(SpaceXl))
            SectionHeader("Field notes")
            Text(
                aiInterpretation,
                style = BodyStyle.copy(color = Starlight),
                modifier = Modifier
                    .widthIn(max = MaxContentWidth)
                    .clip(RoundedCornerShape(CornerM))
                    .background(accentWash(starColor))
                    .border(BorderStroke(1.dp, Hairline), RoundedCornerShape(CornerM))
                    .padding(SpaceL)
            )
        }

        Spacer(Modifier.height(96.dp))
    }
}

// ─── Star visualization ────────────────────────────────────────
// Fixed-size box so the pulse (and entrance) never reflow the page.

@Composable
private fun StarVisualization(
    star: Star,
    isPlaying: Boolean,
    starColor: Color
) {
    val fieldSize = 220.dp

    // Entrance: scale + fade in whenever the star changes.
    var shown by remember(star) { mutableStateOf(false) }
    LaunchedEffect(star) { shown = true }
    val appear by animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "starAppear"
    )

    // Pulse while playing — applied as a transform, not a size change.
    val pulse by if (isPlaying) {
        val inf = rememberInfiniteTransition()
        inf.animateFloat(
            initialValue = 1f,
            targetValue = 1.14f,
            animationSpec = infiniteRepeatable(
                tween(1200, easing = FastOutSlowInEasing),
                RepeatMode.Reverse
            )
        )
    } else {
        remember { mutableStateOf(1f) }
    }

    val glowAlpha by if (isPlaying) {
        val inf = rememberInfiniteTransition()
        inf.animateFloat(
            initialValue = 0.2f,
            targetValue = 0.5f,
            animationSpec = infiniteRepeatable(
                tween(1200, easing = FastOutSlowInEasing),
                RepeatMode.Reverse
            )
        )
    } else {
        remember { mutableStateOf(0.25f) }
    }

    Box(
        modifier = Modifier
            .size(fieldSize)
            .graphicsLayer {
                alpha = appear
                scaleX = 0.85f + 0.15f * appear
                scaleY = 0.85f + 0.15f * appear
            },
        contentAlignment = Alignment.Center
    ) {
        // Outer glow — scales with the pulse but stays inside the field.
        Box(
            Modifier
                .fillMaxSize(0.8f)
                .graphicsLayer { scaleX = pulse; scaleY = pulse }
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                starColor.copy(alpha = glowAlpha * 0.5f),
                                starColor.copy(alpha = glowAlpha * 0.15f),
                                Color.Transparent
                            )
                        ),
                        radius = size.minDimension / 2f
                    )
                }
        )
        // Inner glow
        Box(
            Modifier
                .size(fieldSize * 0.42f)
                .graphicsLayer { scaleX = pulse; scaleY = pulse }
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                starColor.copy(alpha = glowAlpha + 0.25f),
                                starColor.copy(alpha = glowAlpha * 0.4f),
                                Color.Transparent
                            )
                        ),
                        radius = size.minDimension / 2f
                    )
                }
        )
        // Core — white-hot centre
        Box(
            Modifier
                .size(fieldSize * 0.18f)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(Color.White, starColor)))
        )
    }
}

// ─── Data layout helpers ───────────────────────────────────────

/** A raised, top-lit panel with a hairline border — not a flat box. */
@Composable
private fun DataPanel(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CornerM))
            .background(CardBrush)
            .border(BorderStroke(1.dp, Hairline), RoundedCornerShape(CornerM))
            .padding(SpaceL),
        content = content
    )
}

@Composable
private fun DataRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
