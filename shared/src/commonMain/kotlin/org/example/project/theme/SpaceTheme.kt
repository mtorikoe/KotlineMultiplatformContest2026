package org.example.project.theme

/**
 * StellarSynth design tokens — a light "observatory" palette.
 *
 * The mood: a bright paper field notebook for the night sky. One dominant
 * warm-white surface, near-black ink, a single whitish-gold accent used
 * sparingly, and a true-black "monitor" for the live star field. Emphasis
 * comes from weight and space, not from colour.
 */
object SpaceTheme {

    // ── Dominant surface (warm white) ──────────────────────────
    const val BASE: Long       = 0xFFF6F4EFL   // page background
    const val BASE_DIM: Long   = 0xFFEDE9DFL   // subtle gradient foot

    // ── Ink (one colour, three weights via alpha) ──────────────
    const val INK: Long        = 0xFF17151CL   // primary text
    const val INK_SOFT: Long   = 0x9917151CL   // secondary (~60%)
    const val INK_FAINT: Long  = 0x5517151CL   // tertiary (~33%)

    // ── Accent: whitish gold, used sparingly ───────────────────
    const val GOLD: Long       = 0xFFB89457L   // readable gold for marks/active
    const val GOLD_PALE: Long  = 0xFFE7D9BBL   // pale wash / Today's Star tint
    const val GOLD_DEEP: Long  = 0xFF8A6D34L   // text on pale gold

    // ── The star-field "monitor" (true dark) ───────────────────
    const val FIELD: Long      = 0xFF06070BL   // field background
    const val FIELD_TOP: Long  = 0xFF0C0E16L   // faint top lift in the field
    const val STAR_WHITE: Long = 0xFFF4F6FFL   // filler star colour

    // ── Glass (frosted translucent surface) ────────────────────
    const val GLASS: Long        = 0x8CFFFFFFL // ~55% white
    const val GLASS_STRONG: Long = 0xC4FFFFFFL // ~77% white (over the field)
    const val GLASS_BORDER: Long = 0x1F17151CL // ink hairline ~12%
    const val GLASS_HI: Long     = 0x80FFFFFFL // top highlight

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
    const val TITLE_SIZE   = 36   // DM Serif Display — hero titles & star names
    const val HEADING_SIZE = 23   // DM Serif Display — list item names
    const val BODY_SIZE    = 16   // Spectral — descriptions & prose
    const val CAPTION_SIZE = 13   // Spectral — secondary meta
    const val LABEL_SIZE   = 11   // Space Mono — section labels
    const val DATA_SIZE    = 15   // Space Mono — numeric data & badges

    const val TITLE_TRACKING   = -0.5f
    const val HEADING_TRACKING = -0.3f
    const val LABEL_TRACKING   = 2.0f
    const val DATA_TRACKING    = 0.5f
}
