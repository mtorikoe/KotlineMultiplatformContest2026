package org.example.project.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.project.model.Star
import kotlin.math.roundToInt
import kotlin.random.Random

private const val SKY_FACTOR = 1.9f   // sky is this much bigger than the window

/** A deterministic 0..1 fraction from a string + salt (stable every launch). */
private fun frac(id: String, salt: Int): Float {
    var h = salt * 2654435761u.toInt()
    for (c in id) h = h * 31 + c.code
    val x = (h xor (h ushr 15)) and 0x7fffffff
    return 0.06f + (x % 10000) / 10000f * 0.88f   // keep inside [0.06, 0.94]
}

private class Filler(val fx: Float, val fy: Float, val r: Float, val a: Float)

/**
 * The live night-sky "monitor": a square black window you can drag to pan
 * around, with the catalog stars placed across a larger sky. Tap a star to
 * select it; its photo scales in where the dot was.
 */
@Composable
fun StarField(
    stars: List<Star>,
    selected: Star?,
    onSelect: (Star) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // Faint background stars for depth (stable).
    val fillers = remember {
        val rnd = Random(42)
        List(170) {
            Filler(
                fx = rnd.nextFloat(), fy = rnd.nextFloat(),
                r = 0.6f + rnd.nextFloat() * 1.6f,
                a = 0.15f + rnd.nextFloat() * 0.5f
            )
        }
    }
    // Catalog star positions as fractions of the sky.
    val positions = remember(stars) {
        stars.associate { it.id to Offset(frac(it.id, 1), frac(it.id, 2)) }
    }

    val pan = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    var centred by remember { mutableStateOf(false) }

    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { shown = true }
    val enter by animateFloatAsState(
        if (shown) 1f else 0f, tween(900, easing = FastOutSlowInEasing), label = "fieldEnter"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(CornerM))
            .border(BorderStroke(1.dp, GlassBorder), RoundedCornerShape(CornerM))
    ) {
        val side = constraints.maxWidth.toFloat()
        val sky = side * SKY_FACTOR
        val minPan = side - sky    // most-negative translation
        fun clamp(o: Offset) = Offset(o.x.coerceIn(minPan, 0f), o.y.coerceIn(minPan, 0f))
        fun screenOf(id: String): Offset {
            val f = positions[id] ?: Offset(0.5f, 0.5f)
            return Offset(f.x * sky + pan.value.x, f.y * sky + pan.value.y)
        }

        // Centre the sky once we know the size; recentre when selection changes.
        LaunchedEffect(side, selected) {
            if (side <= 0f) return@LaunchedEffect
            if (!centred) { pan.snapTo(clamp(Offset(minPan / 2f, minPan / 2f))); centred = true }
            selected?.let { s ->
                val f = positions[s.id] ?: return@let
                pan.animateTo(
                    clamp(Offset(side / 2f - f.x * sky, side / 2f - f.y * sky)),
                    tween(500, easing = FastOutSlowInEasing)
                )
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(FieldBrush)
                .pointerInput(Unit) {
                    detectDragGestures { change, drag ->
                        change.consume()
                        scope.launch { pan.snapTo(clamp(pan.value + drag)) }
                    }
                }
                .pointerInput(stars) {
                    detectTapGestures { tap ->
                        val hit = stars.minByOrNull { (screenOf(it.id) - tap).getDistance() }
                        if (hit != null && (screenOf(hit.id) - tap).getDistance() < 60f) onSelect(hit)
                    }
                }
        ) {
            // faint filler field
            fillers.forEach { f ->
                val p = Offset(f.fx * sky + pan.value.x, f.fy * sky + pan.value.y)
                if (p.x in -8f..size.width + 8f && p.y in -8f..size.height + 8f) {
                    drawCircle(StarWhite.copy(alpha = f.a * enter), radius = f.r, center = p)
                }
            }
            // catalog stars (brighter ones are bigger); selected one is shown as a photo overlay
            stars.forEach { star ->
                if (star == selected) return@forEach
                val p = screenOf(star.id)
                if (p.x !in -20f..size.width + 20f || p.y !in -20f..size.height + 20f) return@forEach
                val mag = star.apparentMag.toFloat()
                val r = (7.5f - mag * 1.6f).coerceIn(3.2f, 11f)
                val c = star.spectralClass.starColor.toColor()
                drawCircle(c.copy(alpha = 0.22f * enter), radius = r * 2.6f, center = p) // glow
                drawCircle(Color.White.copy(alpha = enter), radius = r * 0.5f, center = p) // core
                drawCircle(c.copy(alpha = 0.9f * enter), radius = r, center = p, style = androidx.compose.ui.graphics.drawscope.Stroke(1.2f))
            }
        }

        // Selected star: its real photo, scaled in at the star's position.
        selected?.let { s ->
            val imgSize = 96.dp
            val halfPx = with(density) { imgSize.toPx() / 2f }
            val p = screenOf(s.id)
            var appeared by remember(s) { mutableStateOf(false) }
            LaunchedEffect(s) { appeared = true }
            val appear by animateFloatAsState(
                if (appeared) 1f else 0f, tween(420, easing = FastOutSlowInEasing), label = "pick"
            )
            StarImageCircle(
                star = s,
                size = imgSize,
                modifier = Modifier
                    .offset { IntOffset((p.x - halfPx).roundToInt(), (p.y - halfPx).roundToInt()) }
                    .graphicsLayer {
                        scaleX = 0.6f + 0.4f * appear; scaleY = 0.6f + 0.4f * appear; alpha = appear
                    }
            )
        }

        // Soft vignette at the top edge for a little depth inside the monitor.
        Canvas(Modifier.fillMaxSize()) {
            drawRect(
                Brush.verticalGradient(
                    0f to FieldTop.copy(alpha = 0.6f),
                    0.25f to Color.Transparent,
                    1f to Color.Transparent
                )
            )
        }
    }
}
