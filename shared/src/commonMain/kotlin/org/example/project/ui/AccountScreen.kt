package org.example.project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AccountScreen(
    starsHeard: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = SpaceM),
    ) {
        Spacer(Modifier.height(SpaceXxl))
        Text("You", style = TitleStyle)

        Spacer(Modifier.height(SpaceXl))

        // Identity
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(SpaceL)) {
            Box(
                Modifier.size(64.dp).glass(CircleShape, strong = true),
                contentAlignment = Alignment.Center
            ) {
                Text("S", style = TitleStyle.copy(fontSize = HeadingStyle.fontSize, color = GoldDeep))
            }
            Column {
                Text("Stargazer", style = HeadingStyle)
                Text("Local listener", style = CaptionStyle)
            }
        }

        Spacer(Modifier.height(SpaceXl))

        // A couple of real counters
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(SpaceM)) {
            StatCell("Stars heard", starsHeard.toString(), Modifier.weight(1f))
            StatCell("In the sky", "20", Modifier.weight(1f))
        }

        Spacer(Modifier.height(SpaceXl))

        SectionHeader("About")
        Text(
            "Every star here is sonified from its real temperature, size, and light — " +
                "hotter stars ring higher, giants sustain longer. Nothing is faked; the sound " +
                "is generated on your device from the numbers.",
            style = BodyStyle.copy(color = InkSoft)
        )

        Spacer(Modifier.height(SpaceXl))

        SectionHeader("Credits")
        CreditLine("Type", "DM Serif Display · Spectral · Space Mono (SIL OFL)")
        CreditLine("Imagery", "Each star's own photo — NASA/ESA, ESO, the Digitized Sky Survey & Wikimedia Commons")
        CreditLine("", "public domain / CC BY — credited on each file's Commons page")
        CreditLine("Data", "SIMBAD & standard astrophysical references")

        Spacer(Modifier.height(SpaceXxl))
    }
}

@Composable
private fun StatCell(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .glass(RoundedCornerShape(CornerM))
            .padding(SpaceL),
    ) {
        Text(value, style = TitleStyle.copy(fontSize = HeadingStyle.fontSize, fontWeight = FontWeight.Bold))
        Spacer(Modifier.height(SpaceXs))
        Text(label.uppercase(), style = LabelStyle)
    }
}

@Composable
private fun CreditLine(tag: String, text: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = SpaceXs), horizontalArrangement = Arrangement.spacedBy(SpaceM)) {
        Text(
            tag.uppercase(),
            style = LabelStyle.copy(color = GoldDeep),
            modifier = Modifier.width(72.dp)
        )
        Text(text, style = CaptionStyle, modifier = Modifier.weight(1f))
    }
}
