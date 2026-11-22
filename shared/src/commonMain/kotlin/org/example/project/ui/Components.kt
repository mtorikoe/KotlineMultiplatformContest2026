package org.example.project.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.model.SpectralClass
import org.example.project.model.Star

// ─── Star glow dot ─────────────────────────────────────────────

/**
 * A circular dot coloured by the star's spectral class,
 * with a soft radial glow behind it.
 */
@Composable
fun StarDot(
    star: Star,
    size: Dp = 32.dp,
    isPlaying: Boolean = false,
    modifier: Modifier = Modifier
) {
    val starColor = star.spectralClass.starColor.toColor()
    val glowAlpha by if (isPlaying) {
        val inf = rememberInfiniteTransition()
        inf.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.7f,
            animationSpec = infiniteRepeatable(
                tween(800, easing = FastOutSlowInEasing),
                RepeatMode.Reverse
            )
        )
    } else {
        remember { mutableStateOf(0.4f) }
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Outer glow
        Box(
            Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                starColor.copy(alpha = glowAlpha),
                                Color.Transparent
                            )
                        )
                    )
                }
        )
        // Core dot — a white-hot centre fading to the star's colour
        Box(
            Modifier
                .size(size * 0.45f)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(Color.White, starColor)
                    )
                )
        )
    }
}

// ─── Featured "star of the night" hero ─────────────────────────

/**
 * A large, editorial hero card that differs structurally from the
 * compact list rows below it — a deliberate change of visual pacing.
 */
@Composable
fun FeaturedStar(
    star: Star,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val starColor = star.spectralClass.starColor.toColor()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CornerL))
            .background(accentWash(starColor))
            .border(BorderStroke(1.dp, Hairline), RoundedCornerShape(CornerL))
            .clickable(onClick = onClick)
            .padding(SpaceL),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpaceL)
    ) {
        StarDot(star, size = 84.dp)

        Column(modifier = Modifier.weight(1f)) {
            Text("STAR OF THE NIGHT", style = LabelStyle.copy(color = Copper))
            Spacer(Modifier.height(SpaceS))
            Text(star.name, style = TitleStyle.copy(fontSize = TitleStyle.fontSize * 0.72f))
            Spacer(Modifier.height(SpaceXs))
            Text(
                "${star.spectralType}  ·  ${star.constellation}",
                style = CaptionStyle
            )
            Spacer(Modifier.height(SpaceS))
            Text(
                star.description,
                style = BodyStyle.copy(color = Dust, fontSize = CaptionStyle.fontSize)
            )
        }
    }
}

// ─── Star list item ────────────────────────────────────────────

@Composable
fun StarListItem(
    star: Star,
    isPlaying: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val starColor = star.spectralClass.starColor.toColor()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CornerM))
            .background(if (isPlaying) accentWash(starColor) else CardBrush)
            .border(
                BorderStroke(1.dp, if (isPlaying) HairlineStrong else Hairline),
                RoundedCornerShape(CornerM)
            )
            .clickable(onClick = onClick)
            .padding(SpaceM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpaceM)
    ) {
        StarDot(star, size = 40.dp, isPlaying = isPlaying)

        Column(modifier = Modifier.weight(1f)) {
            Text(star.name, style = HeadingStyle)
            Spacer(Modifier.height(SpaceXs))
            Text(
                "${star.spectralType}  ·  ${star.constellation}",
                style = CaptionStyle
            )
        }

        // Temperature badge — monospace, like an instrument readout
        Text(
            "${star.temperatureK} K",
            style = DataStyle.copy(color = Amber, fontSize = CaptionStyle.fontSize)
        )
    }
}

// ─── Spectral class filter chips ───────────────────────────────

@Composable
fun SpectralFilter(
    selected: SpectralClass?,
    onSelect: (SpectralClass?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(SpaceS)
    ) {
        // "All" chip
        FilterChip(
            label = "All",
            isSelected = selected == null,
            onClick = { onSelect(null) }
        )
        SpectralClass.entries.forEach { sc ->
            FilterChip(
                label = sc.label,
                isSelected = selected == sc,
                chipColor = sc.starColor.toColor(),
                onClick = { onSelect(if (selected == sc) null else sc) }
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    chipColor: Color = Amber,
    onClick: () -> Unit
) {
    Text(
        text = label,
        style = LabelStyle.copy(
            color = if (isSelected) chipColor else Dust,
            letterSpacing = DataStyle.letterSpacing
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(CornerS))
            .background(if (isSelected) CardBrush else SolidColor(Color.Transparent))
            .border(
                BorderStroke(1.dp, if (isSelected) chipColor.copy(alpha = 0.5f) else Hairline),
                RoundedCornerShape(CornerS)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = SpaceM, vertical = SpaceS)
    )
}

// ─── Section header ────────────────────────────────────────────

@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(bottom = SpaceS),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpaceS)
    ) {
        Text(
            text = text.uppercase(),
            style = LabelStyle.copy(color = Copper)
        )
        // A thin rule that trails off — adds structure without a boxy divider
        Box(
            Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(listOf(Hairline, Color.Transparent))
                )
        )
    }
}

// ─── Flat text button ──────────────────────────────────────────

@Composable
fun FlatButton(
    label: String,
    color: Color = Amber,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = label.uppercase(),
        style = LabelStyle.copy(
            color = color,
            fontSize = DataStyle.fontSize,
            letterSpacing = DataStyle.letterSpacing
        ),
        textAlign = TextAlign.Center,
        modifier = modifier
            .clip(RoundedCornerShape(CornerM))
            .background(
                Brush.verticalGradient(
                    listOf(color.copy(alpha = 0.22f), color.copy(alpha = 0.08f))
                )
            )
            .border(BorderStroke(1.dp, color.copy(alpha = 0.45f)), RoundedCornerShape(CornerM))
            .clickable(onClick = onClick)
            .padding(horizontal = SpaceXxl, vertical = SpaceM)
    )
}

// ─── Bottom navigation ────────────────────────────────────────

@Composable
fun BottomBar(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(CardBrush)
            .border(BorderStroke(1.dp, Hairline))
            .padding(vertical = SpaceM),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEachIndexed { i, label ->
            Text(
                text = label,
                style = CaptionStyle.copy(
                    color = if (i == selectedIndex) Amber else Dust
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable { onSelect(i) }
                    .padding(horizontal = SpaceM, vertical = SpaceXs)
            )
        }
    }
}
