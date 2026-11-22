package org.example.project.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.theme.SpaceTheme
import org.jetbrains.compose.resources.Font
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.dmserifdisplay_regular
import kotlinproject.shared.generated.resources.spectral_regular
import kotlinproject.shared.generated.resources.spectral_medium
import kotlinproject.shared.generated.resources.spectral_semibold
import kotlinproject.shared.generated.resources.spacemono_regular
import kotlinproject.shared.generated.resources.spacemono_bold

// ─── Spacing constants as dp ───────────────────────────────────

val SpaceXs  = SpaceTheme.SPACE_XS.dp
val SpaceS   = SpaceTheme.SPACE_S.dp
val SpaceM   = SpaceTheme.SPACE_M.dp
val SpaceL   = SpaceTheme.SPACE_L.dp
val SpaceXl  = SpaceTheme.SPACE_XL.dp
val SpaceXxl = SpaceTheme.SPACE_XXL.dp

val CornerS = SpaceTheme.CORNER_S.dp
val CornerM = SpaceTheme.CORNER_M.dp
val CornerL = SpaceTheme.CORNER_L.dp

val MaxContentWidth = SpaceTheme.MAX_CONTENT_WIDTH.dp

// ─── Color helpers ─────────────────────────────────────────────

fun Long.toColor(): Color = Color(this.toInt())

val DeepSky     get() = SpaceTheme.DEEP_SKY.toColor()
val Surface     get() = SpaceTheme.SURFACE.toColor()
val SurfaceHigh get() = SpaceTheme.SURFACE_HIGH.toColor()
val Starlight   get() = SpaceTheme.STARLIGHT.toColor()
val Dust        get() = SpaceTheme.DUST.toColor()
val Faint       get() = SpaceTheme.FAINT.toColor()
val Amber       get() = SpaceTheme.AMBER.toColor()
val Copper      get() = SpaceTheme.COPPER.toColor()
val Nebula      get() = SpaceTheme.NEBULA.toColor()
val Active      get() = SpaceTheme.ACTIVE.toColor()

val Hairline       get() = SpaceTheme.HAIRLINE.toColor()
val HairlineStrong get() = SpaceTheme.HAIRLINE_STRONG.toColor()

// ─── Depth: layered gradient brushes ───────────────────────────
// One dominant surface tone, lit from the top, sinking into warm
// black — never a flat solid fill, never a purple→blue wash.

/** Full-screen night-sky backdrop: a vertical warm-plum → black gradient. */
val SkyBrush: Brush
    get() = Brush.verticalGradient(
        0f   to SpaceTheme.SKY_TOP.toColor(),
        0.45f to DeepSky,
        1f   to SpaceTheme.SKY_BOTTOM.toColor()
    )

/** Top-lit card surface — subtle highlight at the top edge. */
val CardBrush: Brush
    get() = Brush.verticalGradient(
        listOf(SpaceTheme.CARD_TOP.toColor(), SpaceTheme.CARD_BOTTOM.toColor())
    )

/** A soft wash tinted by an accent/star colour, for hero panels. */
fun accentWash(accent: Color): Brush = Brush.verticalGradient(
    listOf(
        accent.copy(alpha = 0.14f),
        SpaceTheme.CARD_BOTTOM.toColor()
    )
)

// ─── Fonts ─────────────────────────────────────────────────────
// Distinctive, brand-fitting families — no system defaults.
//   • DM Serif Display → editorial, high-contrast headline serif (celestial)
//   • Spectral         → warm literary body serif (cozy, readable)
//   • Space Mono       → instrument-readout monospace for data & labels

class AppFonts(
    val display: FontFamily,
    val body: FontFamily,
    val mono: FontFamily,
)

val LocalAppFonts = staticCompositionLocalOf<AppFonts> {
    error("AppFonts not provided — wrap content in ProvideAppFonts { }")
}

@Composable
fun ProvideAppFonts(content: @Composable () -> Unit) {
    val fonts = AppFonts(
        display = FontFamily(Font(Res.font.dmserifdisplay_regular)),
        body = FontFamily(
            Font(Res.font.spectral_regular, FontWeight.Normal),
            Font(Res.font.spectral_medium, FontWeight.Medium),
            Font(Res.font.spectral_semibold, FontWeight.SemiBold),
        ),
        mono = FontFamily(
            Font(Res.font.spacemono_regular, FontWeight.Normal),
            Font(Res.font.spacemono_bold, FontWeight.Bold),
        ),
    )
    CompositionLocalProvider(LocalAppFonts provides fonts, content = content)
}

// ─── Typography (composable getters bind the provided families) ─

val TitleStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.display,
        fontWeight = FontWeight.Normal,
        fontSize = SpaceTheme.TITLE_SIZE.sp,
        letterSpacing = SpaceTheme.TITLE_TRACKING.sp,
        color = Starlight
    )

val HeadingStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.display,
        fontWeight = FontWeight.Normal,
        fontSize = SpaceTheme.HEADING_SIZE.sp,
        letterSpacing = SpaceTheme.HEADING_TRACKING.sp,
        color = Starlight
    )

val BodyStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.body,
        fontWeight = FontWeight.Normal,
        fontSize = SpaceTheme.BODY_SIZE.sp,
        lineHeight = (SpaceTheme.BODY_SIZE * 1.5).sp,
        color = Starlight
    )

val CaptionStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.body,
        fontWeight = FontWeight.Normal,
        fontSize = SpaceTheme.CAPTION_SIZE.sp,
        color = Dust
    )

/** Space Mono, uppercase, wide tracking — for section labels. */
val LabelStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.mono,
        fontWeight = FontWeight.Normal,
        fontSize = SpaceTheme.LABEL_SIZE.sp,
        letterSpacing = SpaceTheme.LABEL_TRACKING.sp,
        color = Dust
    )

/** Space Mono — numeric data values and badges (Hz, K, L☉). */
val DataStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.mono,
        fontWeight = FontWeight.Normal,
        fontSize = SpaceTheme.DATA_SIZE.sp,
        letterSpacing = SpaceTheme.DATA_TRACKING.sp,
        color = Starlight
    )
