package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.theme.BaseDesign
import org.example.project.theme.FontMood
import org.example.project.theme.GridStyle
import org.example.project.theme.Hobbies
import org.example.project.theme.ThemeSpec
import org.example.project.theme.sanitized

private fun Long.toColor(): Color = Color(this)

private fun FontMood.toFontFamily(): FontFamily = when (this) {
    FontMood.SANS_LIGHT, FontMood.SANS_ROUNDED, FontMood.DISPLAY_CONDENSED -> FontFamily.SansSerif
    FontMood.SERIF -> FontFamily.Serif
    FontMood.MONO_NUMERIC -> FontFamily.Monospace
    FontMood.HANDWRITTEN -> FontFamily.Cursive
}

private fun FontMood.titleWeight(): FontWeight = when (this) {
    FontMood.SANS_LIGHT -> FontWeight.Light
    FontMood.DISPLAY_CONDENSED -> FontWeight.Bold
    else -> FontWeight.Normal
}

@Composable
@Preview
fun App() {
    var selectedId by remember { mutableStateOf(Hobbies.astronomy.id) }
    val hobby = Hobbies.find(selectedId) ?: Hobbies.astronomy
    val theme = hobby.theme.sanitized()

    val background = theme.background.toColor()
    val textColor = theme.onBackground.toColor()
    val accent = theme.accent.toColor()
    val fontFamily = theme.fontMood.toFontFamily()
    val cardShape = RoundedCornerShape(theme.cornerRadiusDp.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .safeContentPadding()
            .padding(horizontal = theme.contentPaddingDp.dp),
    ) {
        Spacer(Modifier.height(12.dp))

        // Hobby picker: each chip previews its own theme.
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Hobbies.all) { item ->
                val chipTheme = item.theme.sanitized()
                val selected = item.id == selectedId
                val chipShape = RoundedCornerShape(50)
                Text(
                    text = item.nameEn,
                    color = chipTheme.onBackground.toColor(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clip(chipShape)
                        .background(chipTheme.background.toColor())
                        .border(
                            width = if (selected) 2.dp else 1.dp,
                            color = chipTheme.accent.toColor().copy(alpha = if (selected) 1f else 0.5f),
                            shape = chipShape,
                        )
                        .clickable { selectedId = item.id }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Title
        Text(
            text = hobby.nameEn,
            color = textColor,
            fontSize = 36.sp,
            fontFamily = fontFamily,
            fontWeight = theme.fontMood.titleWeight(),
        )
        Box(
            Modifier
                .padding(top = 8.dp)
                .width(48.dp)
                .height(3.dp)
                .background(accent),
        )

        Spacer(Modifier.height(20.dp))

        // "First step" card: translucent surface instead of a real blur.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(cardShape)
                .background(theme.surface.toColor().copy(alpha = theme.surfaceAlpha))
                .border(1.dp, textColor.copy(alpha = BaseDesign.GLASS_BORDER_ALPHA), cardShape)
                .padding(18.dp),
        ) {
            Text(
                text = "YOUR FIRST STEP",
                color = accent,
                fontSize = 12.sp,
                fontFamily = fontFamily,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = hobby.firstStep,
                color = textColor,
                fontSize = 16.sp,
                fontFamily = fontFamily,
            )
        }

        Spacer(Modifier.height(20.dp))

        PhotoGrid(theme)
    }
}

/** Placeholder tiles for the photo-first feed. Real photos will replace the gradients later. */
@Composable
private fun PhotoGrid(theme: ThemeSpec) {
    val columns = if (theme.gridStyle == GridStyle.TIMELINE) 1 else 3
    val tileShape = RoundedCornerShape((theme.cornerRadiusDp / 2).dp)
    val surface = theme.surface.toColor()
    val accent = theme.accent.toColor()

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(12) { index ->
            Box(
                Modifier
                    .aspectRatio(tileRatio(theme.gridStyle, index))
                    .clip(tileShape)
                    .background(Brush.linearGradient(listOf(surface, accent.copy(alpha = 0.35f)))),
            )
        }
    }
}

private fun tileRatio(style: GridStyle, index: Int): Float = when (style) {
    GridStyle.SQUARE -> 1f
    GridStyle.PORTRAIT -> 3f / 4f
    GridStyle.LANDSCAPE -> 4f / 3f
    GridStyle.MASONRY -> if (index % 3 == 0) 0.8f else 1.2f
    GridStyle.TIMELINE -> 3f
}