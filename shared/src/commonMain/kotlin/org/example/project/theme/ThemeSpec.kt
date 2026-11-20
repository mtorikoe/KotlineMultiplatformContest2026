package org.example.project.theme

import kotlin.math.pow

/** How a hobby's posts are laid out in the photo-first grid. */
enum class GridStyle { SQUARE, PORTRAIT, LANDSCAPE, MASONRY, TIMELINE }

/** Typeface mood. The actual font files are mapped in the UI layer. */
enum class FontMood { SANS_LIGHT, SANS_ROUNDED, SERIF, MONO_NUMERIC, DISPLAY_CONDENSED, HANDWRITTEN }

/** What a hobby's records should highlight. */
enum class RecordEmphasis { PHOTO, AUDIO, TEXT, NUMBERS, STEPS }

/**
 * Describes how a hobby page looks.
 *
 * Colors are stored as 0xAARRGGBB Long values so this class stays free of any
 * Compose dependency and can live in common code. Theme JSON produced by the AI
 * feature is mapped onto this class and then passed through [sanitized].
 */
data class ThemeSpec(
    val id: String,
    val displayName: String,
    val background: Long,
    val surface: Long,
    val surfaceAlpha: Float,
    val onBackground: Long,
    val accent: Long,
    val fontMood: FontMood,
    val gridStyle: GridStyle,
    val cornerRadiusDp: Int,
    val contentPaddingDp: Int,
    val recordEmphasis: RecordEmphasis,
)

/**
 * Keeps values in a safe range and guarantees readable text, so that even an
 * extreme AI-generated theme cannot produce a broken screen.
 */
fun ThemeSpec.sanitized(): ThemeSpec = copy(
    surfaceAlpha = surfaceAlpha.coerceIn(0.3f, 1f),
    cornerRadiusDp = cornerRadiusDp.coerceIn(0, 40),
    contentPaddingDp = contentPaddingDp.coerceIn(8, 40),
    onBackground = ensureReadable(onBackground, background),
)

/** WCAG contrast ratio. A value of 4.5 or more is comfortable for body text. */
fun contrastRatio(foreground: Long, background: Long): Double {
    val a = relativeLuminance(foreground)
    val b = relativeLuminance(background)
    val lighter = maxOf(a, b)
    val darker = minOf(a, b)
    return (lighter + 0.05) / (darker + 0.05)
}

private fun ensureReadable(foreground: Long, background: Long): Long {
    if (contrastRatio(foreground, background) >= 4.5) return foreground
    val white = 0xFFFFFFFFL
    val black = 0xFF000000L
    return if (contrastRatio(white, background) >= contrastRatio(black, background)) white else black
}

private fun relativeLuminance(argb: Long): Double {
    val r = channel(((argb shr 16) and 0xFF).toInt())
    val g = channel(((argb shr 8) and 0xFF).toInt())
    val b = channel((argb and 0xFF).toInt())
    return 0.2126 * r + 0.7152 * g + 0.0722 * b
}

private fun channel(value: Int): Double {
    val s = value / 255.0
    return if (s <= 0.03928) s / 12.92 else ((s + 0.055) / 1.055).pow(2.4)
}