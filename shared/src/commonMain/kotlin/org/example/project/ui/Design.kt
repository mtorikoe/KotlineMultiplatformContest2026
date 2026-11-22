package org.example.project.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
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

// ─── Spacing ───────────────────────────────────────────────────

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

// ─── Colours ───────────────────────────────────────────────────

fun Long.toColor(): Color = Color(this.toInt())

val Base      get() = SpaceTheme.BASE.toColor()
val BaseDim   get() = SpaceTheme.BASE_DIM.toColor()
val Ink       get() = SpaceTheme.INK.toColor()
val InkSoft   get() = SpaceTheme.INK_SOFT.toColor()
val InkFaint  get() = SpaceTheme.INK_FAINT.toColor()
val Gold      get() = SpaceTheme.GOLD.toColor()
val GoldPale  get() = SpaceTheme.GOLD_PALE.toColor()
val GoldDeep  get() = SpaceTheme.GOLD_DEEP.toColor()
val Field     get() = SpaceTheme.FIELD.toColor()
val FieldTop  get() = SpaceTheme.FIELD_TOP.toColor()
val StarWhite get() = SpaceTheme.STAR_WHITE.toColor()

val GlassFill   get() = SpaceTheme.GLASS.toColor()
val GlassStrong get() = SpaceTheme.GLASS_STRONG.toColor()
val GlassBorder get() = SpaceTheme.GLASS_BORDER.toColor()
val GlassHi     get() = SpaceTheme.GLASS_HI.toColor()

// ─── Surfaces ──────────────────────────────────────────────────

/** Static fallback of the sky gradient (black → navy → azure → deep). */
val PageBrush: Brush
    get() = Brush.verticalGradient(
        0f    to SpaceTheme.BG_BLACK.toColor(),
        0.4f  to SpaceTheme.BG_NAVY.toColor(),
        0.74f to SpaceTheme.BG_AZURE.toColor(),
        1f    to SpaceTheme.BG_DEEP.toColor(),
    )

/** The star-field monitor background. */
val FieldBrush: Brush
    get() = Brush.verticalGradient(listOf(FieldTop, Field))

/**
 * The living page backdrop: a vertical black→navy→azure→deep gradient whose
 * mid stops drift, with a slow white bloom and an azure bloom wandering across
 * it. Draw this behind page content.
 */
@Composable
fun StellarBackground(modifier: Modifier = Modifier) {
    val tr = rememberInfiniteTransition(label = "sky")
    val drift by tr.animateFloat(
        0f, 1f, infiniteRepeatable(tween(18000, easing = LinearEasing), RepeatMode.Reverse),
        label = "drift"
    )
    val bloom by tr.animateFloat(
        0f, 1f, infiniteRepeatable(tween(14000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "bloom"
    )
    val black = SpaceTheme.BG_BLACK.toColor()
    val navy  = SpaceTheme.BG_NAVY.toColor()
    val azure = SpaceTheme.BG_AZURE.toColor()
    val deep  = SpaceTheme.BG_DEEP.toColor()

    Canvas(modifier.fillMaxSize()) {
        val d = (drift - 0.5f) * 0.12f
        drawRect(
            Brush.verticalGradient(
                0f to black,
                (0.38f + d) to navy,
                (0.72f + d) to azure,
                1f to deep,
            )
        )
        // Drifting white bloom — the moving "white" highlight.
        val c1 = Offset(size.width * (0.22f + 0.52f * bloom), size.height * (0.20f + 0.12f * drift))
        val r1 = size.minDimension * 0.8f
        drawCircle(
            Brush.radialGradient(listOf(Color.White.copy(alpha = 0.15f), Color.Transparent), center = c1, radius = r1),
            radius = r1, center = c1
        )
        // Wandering azure bloom for depth.
        val c2 = Offset(size.width * (0.82f - 0.44f * bloom), size.height * (0.56f + 0.10f * drift))
        val r2 = size.minDimension * 0.72f
        drawCircle(
            Brush.radialGradient(listOf(azure.copy(alpha = 0.45f), Color.Transparent), center = c2, radius = r2),
            radius = r2, center = c2
        )
    }
}

/**
 * Liquid glass: a translucent pane lit from the top, with a bright sheen,
 * a soft mid-body and a fine luminous edge — it borrows the sky behind it.
 */
fun Modifier.glass(
    shape: Shape = RoundedCornerShape(CornerM),
    strong: Boolean = false,
): Modifier = this
    .clip(shape)
    .background(
        Brush.verticalGradient(
            0f   to GlassHi,
            0.5f to (if (strong) GlassStrong else GlassFill),
            1f   to (if (strong) GlassFill else GlassFill.copy(alpha = GlassFill.alpha * 0.6f)),
        )
    )
    .border(BorderStroke(1.dp, GlassBorder), shape)

// ─── Fonts ─────────────────────────────────────────────────────

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

// ─── Typography (ink on white; emphasis by weight, not colour) ─

val TitleStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.display,
        fontSize = SpaceTheme.TITLE_SIZE.sp,
        letterSpacing = SpaceTheme.TITLE_TRACKING.sp,
        color = Ink
    )

val HeadingStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.display,
        fontSize = SpaceTheme.HEADING_SIZE.sp,
        letterSpacing = SpaceTheme.HEADING_TRACKING.sp,
        color = Ink
    )

val BodyStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.body,
        fontSize = SpaceTheme.BODY_SIZE.sp,
        lineHeight = (SpaceTheme.BODY_SIZE * 1.5).sp,
        color = Ink
    )

val CaptionStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.body,
        fontSize = SpaceTheme.CAPTION_SIZE.sp,
        color = InkSoft
    )

/** Space Mono, caps, wide tracking — section labels. */
val LabelStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.mono,
        fontSize = SpaceTheme.LABEL_SIZE.sp,
        letterSpacing = SpaceTheme.LABEL_TRACKING.sp,
        color = InkSoft
    )

/** Space Mono — numeric data & badges. */
val DataStyle: TextStyle
    @Composable get() = TextStyle(
        fontFamily = LocalAppFonts.current.mono,
        fontSize = SpaceTheme.DATA_SIZE.sp,
        letterSpacing = SpaceTheme.DATA_TRACKING.sp,
        color = Ink
    )
