package org.example.project.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.project.model.Star

/**
 * "Original" — a small studio: pick any number of stars and hear their
 * sonified voices layered into one chord you composed.
 */
@Composable
fun OriginalScreen(
    stars: List<Star>,
    combinedPlaying: Boolean,
    onPlayCombined: (List<Star>) -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = remember { mutableStateListOf<String>() }
    val chosen = stars.filter { it.id in selected }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = SpaceM),
    ) {
        Spacer(Modifier.height(SpaceXxl))
        Text("Original", style = TitleStyle)
        Spacer(Modifier.height(SpaceXs))
        Text("Layer stars into a sound of your own.", style = CaptionStyle)

        Spacer(Modifier.height(SpaceXl))

        SectionHeader("Choose stars")
        Spacer(Modifier.height(SpaceS))
        StarPicker(
            stars = stars,
            selected = selected,
            onToggle = { id -> if (id in selected) selected.remove(id) else selected.add(id) }
        )

        Spacer(Modifier.height(SpaceXl))

        // ── The mix ────────────────────────────────────────────
        Column(
            Modifier.fillMaxWidth().glass(RoundedCornerShape(CornerL)).padding(SpaceL)
        ) {
            Text(
                when (chosen.size) {
                    0 -> "No stars yet"
                    1 -> "1 voice"
                    else -> "${chosen.size} voices"
                },
                style = HeadingStyle
            )
            Spacer(Modifier.height(SpaceXs))
            Text(
                if (chosen.isEmpty()) "Tap a few stars above to build a chord."
                else chosen.joinToString("  ·  ") { it.name },
                style = CaptionStyle
            )

            Spacer(Modifier.height(SpaceL))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(SpaceM)) {
                if (combinedPlaying) {
                    FlatButton("Stop", onClick = onStop)
                } else {
                    FlatButton(
                        label = if (chosen.size < 2) "Play" else "Play together",
                        onClick = { if (chosen.isNotEmpty()) onPlayCombined(chosen) },
                        emphasised = chosen.isNotEmpty()
                    )
                }
                if (selected.isNotEmpty()) {
                    Text(
                        "Clear",
                        style = LabelStyle.copy(color = InkFaint, letterSpacing = DataStyle.letterSpacing),
                        modifier = Modifier
                            .clip(RoundedCornerShape(CornerS))
                            .clickable { selected.clear() }
                            .padding(horizontal = SpaceM, vertical = SpaceS)
                    )
                }
            }
        }

        Spacer(Modifier.height(SpaceXl))

        SectionHeader("Credits")
        CreditLine("Type", "DM Serif Display · Spectral · Space Mono (SIL OFL)")
        CreditLine("Icons", "Heroicons (MIT)")
        CreditLine("Imagery", "Each star's own photo — NASA/ESA, ESO, DSS & Wikimedia Commons")
        CreditLine("", "public domain / CC BY — credited on each file's Commons page")
        CreditLine("Data", "SIMBAD & standard astrophysical references")

        Spacer(Modifier.height(SpaceXxl))
    }
}

@Composable
private fun StarPicker(
    stars: List<Star>,
    selected: List<String>,
    onToggle: (String) -> Unit
) {
    // A simple wrapping grid of toggleable chips (mobile-first, no fixed columns).
    Column(verticalArrangement = Arrangement.spacedBy(SpaceS)) {
        stars.chunked(2).forEach { pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(SpaceS)) {
                pair.forEach { star ->
                    StarChip(
                        star = star,
                        isSelected = star.id in selected,
                        onClick = { onToggle(star.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StarChip(
    star: Star,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(CornerM))
            .then(if (isSelected) Modifier.background(GoldPale.copy(alpha = 0.5f)) else Modifier.glass(RoundedCornerShape(CornerM)))
            .then(if (isSelected) Modifier.border(BorderStroke(1.dp, Gold.copy(alpha = 0.7f)), RoundedCornerShape(CornerM)) else Modifier)
            .clickable(onClick = onClick)
            .padding(horizontal = SpaceM, vertical = SpaceM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpaceS)
    ) {
        StarDot(star, size = 22.dp)
        Text(
            star.name,
            style = BodyStyle.copy(
                fontSize = CaptionStyle.fontSize,
                color = if (isSelected) GoldDeep else Ink,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            ),
            maxLines = 1
        )
    }
}

@Composable
private fun CreditLine(tag: String, text: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = SpaceXs), horizontalArrangement = Arrangement.spacedBy(SpaceM)) {
        Text(tag.uppercase(), style = LabelStyle.copy(color = GoldDeep), modifier = Modifier.width(72.dp))
        Text(text, style = CaptionStyle, modifier = Modifier.weight(1f))
    }
}
