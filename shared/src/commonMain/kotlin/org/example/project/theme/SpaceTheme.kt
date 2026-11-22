package org.example.project.theme

/**
 * Warm astronomical color palette.
 *
 * The mood is "cozy night under a blanket, watching stars" —
 * deep plum-navy sky with amber starlight and copper accents.
 * Not cold sci-fi; warm, inviting, human.
 */
object SpaceTheme {

    // ── Backgrounds ────────────────────────────────────────────
    /** Deep warm purple-black, like a moonless sky with warm air. */
    const val DEEP_SKY: Long       = 0xFF13111CL
    /** Slightly lifted surface for cards and panels. */
    const val SURFACE: Long        = 0xFF1E1B28L
    /** Elevated surface (modals, selected cards). */
    const val SURFACE_HIGH: Long   = 0xFF292538L

    // ── Text ───────────────────────────────────────────────────
    /** Primary text: warm cream like old starlight. */
    const val STARLIGHT: Long      = 0xFFF2E8D5L
    /** Secondary text: dusty lavender. */
    const val DUST: Long           = 0xFF8B8099L
    /** Disabled / tertiary text. */
    const val FAINT: Long          = 0xFF5D5570L

    // ── Accents ────────────────────────────────────────────────
    /** Primary accent: warm amber-gold. */
    const val AMBER: Long          = 0xFFE8A838L
    /** Secondary accent: copper. */
    const val COPPER: Long         = 0xFFC4845CL
    /** Tertiary: soft nebula purple. */
    const val NEBULA: Long         = 0xFF8B6B9BL

    // ── Functional ─────────────────────────────────────────────
    /** Active / playing state. */
    const val ACTIVE: Long         = AMBER
    /** Error / warning. */
    const val ALERT: Long          = 0xFFE85A5AL

    // ── Surface with alpha (overlays, dimming) ─────────────────
    const val OVERLAY_DIM: Long    = 0x9913111CL   // 60% DEEP_SKY
    const val SURFACE_ALPHA: Float = 0.92f

    // ── Depth: layered gradients & hairline borders ────────────
    /** Top of the sky gradient — a touch of warm lift. */
    const val SKY_TOP: Long        = 0xFF1C1826L
    /** Bottom of the sky gradient — sinks toward warm black. */
    const val SKY_BOTTOM: Long     = 0xFF0C0A11L
    /** Top-lit card surface (catches the light). */
    const val CARD_TOP: Long       = 0xFF272231L
    /** Card base, in shadow. */
    const val CARD_BOTTOM: Long    = 0xFF191622L
    /** Fine warm hairline for borders (cream at ~10%). */
    const val HAIRLINE: Long       = 0x1AF2E8D5L
    /** Brighter hairline for emphasis / active edges. */
    const val HAIRLINE_STRONG: Long = 0x33E8A838L

    // ── Spacing (multiples of 8) ───────────────────────────────
    const val SPACE_XS  = 4
    const val SPACE_S   = 8
    const val SPACE_M   = 16
    const val SPACE_L   = 24
    const val SPACE_XL  = 32
    const val SPACE_XXL = 48

    const val GRID_GAP    = 2
    const val CORNER_S    = 8
    const val CORNER_M    = 12
    const val CORNER_L    = 16

    const val MAX_CONTENT_WIDTH = 560

    // ── Typography guidance ────────────────────────────────────
    // Font families will be set per-platform; these are size/weight specs.
    const val TITLE_SIZE   = 36   // DM Serif Display — hero titles & star names
    const val HEADING_SIZE = 23   // DM Serif Display — list item names
    const val BODY_SIZE    = 16   // Spectral — descriptions & prose
    const val CAPTION_SIZE = 13   // Spectral — secondary meta
    const val LABEL_SIZE   = 11   // Space Mono — section labels
    const val DATA_SIZE    = 15   // Space Mono — numeric data & badges

    // Letter spacing in sp
    const val TITLE_TRACKING   = -0.5f
    const val HEADING_TRACKING = -0.3f
    const val LABEL_TRACKING   = 2.0f    // generous tracking for the mono caps labels
    const val DATA_TRACKING    = 0.5f
}
