package org.example.project.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

/** Page background: a near-flat warm-white with a barely-there foot. */
val PageBrush: Brush
    get() = Brush.verticalGradient(0f to Base, 1f to BaseDim)

/** The star-field monitor background. */
val FieldBrush: Brush
    get() = Brush.verticalGradient(listOf(FieldTop, Field))

/**
 * Frosted glass: translucent white, a soft top highlight, a hairline edge.
 * No solid card colour — the surface borrows what's behind it.
 */
fun Modifier.glass(
    shape: Shape = RoundedCornerShape(CornerM),
    strong: Boolean = false,
): Modifier = this
    .clip(shape)
    .background(
        Brush.verticalGradient(
            listOf(GlassHi, if (strong) GlassStrong else GlassFill)
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
