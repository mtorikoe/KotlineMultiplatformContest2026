package org.example.project.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.model.SpectralClass
import org.example.project.model.Star
import org.jetbrains.compose.resources.painterResource

// ─── Circular star photo with a coloured halo ──────────────────

@Composable
fun StarImageCircle(
    star: Star,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    val halo = star.spectralClass.starColor.toColor()
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(halo.copy(alpha = 0.55f), Color.Transparent)
                        ),
                        radius = this.size.minDimension / 2f
                    )
                }
        )
        Image(
            painter = painterResource(imageFor(star)),
            contentDescription = star.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size * 0.78f)
                .clip(CircleShape)
                .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)), CircleShape)
        )
    }
}

// ─── Simple glow dot (list rows, small marks) ──────────────────

@Composable
fun StarDot(
    star: Star,
    size: Dp = 32.dp,
    modifier: Modifier = Modifier
) {
    val starColor = star.spectralClass.starColor.toColor()
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            listOf(starColor.copy(alpha = 0.55f), Color.Transparent)
                        )
                    )
                }
        )
        Box(
            Modifier
                .size(size * 0.42f)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(Color.White, starColor)))
        )
    }
}

// ─── Today's Star hero (glass, with the real photo) ────────────

@Composable
fun TodayStar(
    star: Star,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .glass(RoundedCornerShape(CornerL))
            .clickable(onClick = onOpen)
            .padding(SpaceL),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpaceL)
    ) {
        StarImageCircle(star, size = 76.dp)
        Column(Modifier.weight(1f)) {
            Text("TONIGHT'S STAR", style = LabelStyle.copy(color = GoldDeep))
            Spacer(Modifier.height(SpaceXs))
            Text(star.name, style = TitleStyle.copy(fontSize = TitleStyle.fontSize * 0.66f))
            Spacer(Modifier.height(SpaceXs))
            Text("${star.spectralType}  ·  ${star.constellation}", style = CaptionStyle)
        }
    }
}

// ─── Star list row (glass) ─────────────────────────────────────

@Composable
fun StarListItem(
    star: Star,
    isPlaying: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .glass(RoundedCornerShape(CornerM), strong = isPlaying)
            .then(
                if (isPlaying)
                    Modifier.border(BorderStroke(1.dp, Gold.copy(alpha = 0.6f)), RoundedCornerShape(CornerM))
                else Modifier
            )
            .clickable(onClick = onClick)
            .padding(SpaceM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpaceM)
    ) {
        StarDot(star, size = 38.dp)
        Column(Modifier.weight(1f)) {
            Text(star.name, style = HeadingStyle)
            Spacer(Modifier.height(SpaceXs))
            Text("${star.spectralType}  ·  ${star.constellation}", style = CaptionStyle)
        }
        Text(
            "${star.temperatureK} K",
            style = DataStyle.copy(color = GoldDeep, fontSize = CaptionStyle.fontSize)
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
        FilterChip("All", selected == null) { onSelect(null) }
        SpectralClass.entries.forEach { sc ->
            FilterChip(sc.label, selected == sc) {
                onSelect(if (selected == sc) null else sc)
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = label,
        style = LabelStyle.copy(
            color = if (isSelected) GoldDeep else InkFaint,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            letterSpacing = DataStyle.letterSpacing
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(CornerS))
            .then(if (isSelected) Modifier.background(GoldPale.copy(alpha = 0.5f)) else Modifier)
            .border(
                BorderStroke(1.dp, if (isSelected) Gold.copy(alpha = 0.6f) else GlassBorder),
                RoundedCornerShape(CornerS)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = SpaceM, vertical = SpaceS)
    )
}

// ─── Section header (mono caps + trailing rule) ────────────────

@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(bottom = SpaceS),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpaceM)
    ) {
        Text(text.uppercase(), style = LabelStyle.copy(color = InkSoft))
        Box(
            Modifier
                .weight(1f)
                .height(1.dp)
                .background(Brush.horizontalGradient(listOf(GlassBorder, Color.Transparent)))
        )
    }
}

// ─── Pill button (glass + gold edge) ───────────────────────────

@Composable
fun FlatButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    emphasised: Boolean = true,
) {
    Text(
        text = label.uppercase(),
        style = LabelStyle.copy(
            color = GoldDeep,
            fontWeight = FontWeight.Bold,
            fontSize = DataStyle.fontSize,
            letterSpacing = DataStyle.letterSpacing
        ),
        textAlign = TextAlign.Center,
        modifier = modifier
            .clip(RoundedCornerShape(CornerM))
            .then(if (emphasised) Modifier.background(GoldPale.copy(alpha = 0.55f)) else Modifier.glass(RoundedCornerShape(CornerM)))
            .border(BorderStroke(1.dp, Gold.copy(alpha = 0.7f)), RoundedCornerShape(CornerM))
            .clickable(onClick = onClick)
            .padding(horizontal = SpaceXxl, vertical = SpaceM)
    )
}

// ─── Bottom navigation (glass bar, Heroicons) ──────────────────

@Composable
fun BottomNav(
    items: List<NavItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .glass(RoundedCornerShape(topStart = CornerL, topEnd = CornerL), strong = true)
            .padding(top = SpaceS, bottom = SpaceM),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { i, item ->
            val active = i == selectedIndex
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(CornerS))
                    .clickable { onSelect(i) }
                    .padding(horizontal = SpaceL, vertical = SpaceXs)
            ) {
                Icon(
                    imageVector = if (active) item.filled else item.outline,
                    contentDescription = item.label,
                    tint = if (active) Ink else InkFaint,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.height(SpaceXs))
                Text(
                    item.label.uppercase(),
                    style = LabelStyle.copy(
                        color = if (active) Ink else InkFaint,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
                    )
                )
            }
        }
    }
}
