package org.example.project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.project.model.Star

@Composable
fun HomeScreen(
    stars: List<Star>,
    today: Star,
    onOpen: (Star) -> Unit,
    modifier: Modifier = Modifier,
) {
    var picked by remember { mutableStateOf<Star?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = SpaceM),
    ) {
        Spacer(Modifier.height(SpaceXxl))

        // Slim wordmark — small, so the sky owns the top half.
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("StellarSynth", style = HeadingStyle.copy(fontSize = BodyStyle.fontSize))
            Spacer(Modifier.weight(1f))
            Text("THE NIGHT, HEARD", style = LabelStyle.copy(color = GoldDeep))
        }

        Spacer(Modifier.height(SpaceM))

        // ── The live sky (square monitor) ──────────────────────
        StarField(
            stars = stars,
            selected = picked,
            onSelect = { picked = it },
        )

        Spacer(Modifier.height(SpaceM))

        // ── Selection strip ────────────────────────────────────
        val p = picked
        if (p != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .glass(RoundedCornerShape(CornerM))
                    .padding(SpaceM),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpaceM)
            ) {
                StarDot(p, size = 30.dp)
                Column(Modifier.weight(1f)) {
                    Text(p.name, style = HeadingStyle.copy(fontSize = BodyStyle.fontSize))
                    Text("${p.spectralType}  ·  ${p.constellation}", style = CaptionStyle)
                }
                FlatButton("Open", onClick = { onOpen(p) })
            }
        } else {
            Text(
                "Drag the sky.  Tap a star to meet it.",
                style = CaptionStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = SpaceM)
            )
        }

        Spacer(Modifier.height(SpaceL))

        // ── Today's star ───────────────────────────────────────
        SectionHeader("Tonight")
        TodayStar(star = today, onOpen = { onOpen(today) })

        Spacer(Modifier.weight(1f))
    }
}
