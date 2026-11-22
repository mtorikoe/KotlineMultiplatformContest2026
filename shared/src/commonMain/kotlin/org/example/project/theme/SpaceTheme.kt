package org.example.project.theme

/**
 * StellarSynth design tokens — a "deep sky" palette.
 *
 * The page is a living gradient that sinks from black through dark navy and
 * azure toward a drifting white bloom. Content floats on it as frosted
 * "liquid glass". Text is pale; emphasis comes from weight and space, with a
 * single whitish-gold accent.
 */
object SpaceTheme {

    // ── Background gradient stops (black → navy → azure → deep) ─
    const val BG_BLACK: Long   = 0xFF05070EL
    const val BG_NAVY: Long    = 0xFF0A1733L   // dark navy blue
    const val BG_AZURE: Long   = 0xFF2A6CB8L   // azure
    const val BG_DEEP: Long    = 0xFF0E2E54L   // deep azure foot (keeps the nav readable)

    // Kept for any incidental use; the gradient above is the real surface.
    const val BASE: Long       = BG_NAVY
    const val BASE_DIM: Long   = BG_DEEP

    // ── Ink (one pale colour, three weights via alpha) ─────────
    const val INK: Long        = 0xFFEAF1FBL   // primary text
    const val INK_SOFT: Long   = 0xB8EAF1FBL   // secondary (~72%)
    const val INK_FAINT: Long  = 0x7AEAF1FBL   // tertiary (~48%)

    // ── Accent: whitish gold ───────────────────────────────────
    const val GOLD: Long       = 0xFFE8C987L   // marks / active edges
    const val GOLD_PALE: Long  = 0xFFE8C987L   // used with alpha for fills
    const val GOLD_DEEP: Long  = 0xFFF2DBA4L   // bright gold for accent text on dark

    // ── The star-field "monitor" (true dark, inset in the sky) ─
    const val FIELD: Long      = 0xFF04060CL
    const val FIELD_TOP: Long  = 0xFF0A1022L
    const val STAR_WHITE: Long = 0xFFF4F6FFL

    // ── Liquid glass (frosted translucent, lit from the top) ───
    const val GLASS: Long        = 0x1AFFFFFFL // ~10% white
    const val GLASS_STRONG: Long = 0x2EFFFFFFL // ~18% white
    const val GLASS_BORDER: Long = 0x3DFFFFFFL // ~24% white edge
    const val GLASS_HI: Long     = 0x5CFFFFFFL // ~36% top sheen

    // ── Spacing (multiples of 8) ───────────────────────────────
    const val SPACE_XS  = 4
    const val SPACE_S   = 8
    const val SPACE_M   = 16
    const val SPACE_L   = 24
    const val SPACE_XL  = 32
    const val SPACE_XXL = 48

    const val GRID_GAP    = 2
    const val CORNER_S    = 10
    const val CORNER_M    = 16
    const val CORNER_L    = 24

    const val MAX_CONTENT_WIDTH = 560

    // ── Typography sizes/tracking ──────────────────────────────
    const val TITLE_SIZE   = 36
    const val HEADING_SIZE = 23
    const val BODY_SIZE    = 16
    const val CAPTION_SIZE = 13
    const val LABEL_SIZE   = 11
    const val DATA_SIZE    = 15

    const val TITLE_TRACKING   = -0.5f
    const val HEADING_TRACKING = -0.3f
    const val LABEL_TRACKING   = 2.0f
    const val DATA_TRACKING    = 0.5f
}
