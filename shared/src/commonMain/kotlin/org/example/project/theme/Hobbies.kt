package org.example.project.theme

/**
 * Base design of the web app. The mood comes from the reference images
 * (plaster/stone backgrounds, moss green, soft light and shadow, frosted-glass cards).
 * The reference images themselves are not stored in the repository.
 */
object BaseDesign {
    const val STONE_BACKGROUND = 0xFFEEEBE4L
    const val INK = 0xFF2E3329L
    const val MOSS = 0xFF6B7B5AL
    const val SAGE = 0xFF9AA88AL

    /** Frosted-glass look is a translucent surface, not a real blur, so it looks the same on every platform. */
    const val GLASS_ALPHA = 0.55f
    const val GLASS_BORDER_ALPHA = 0.35f
    const val CARD_RADIUS_DP = 28
    const val PAGE_PADDING_DP = 24
}

/** One hobby. [related] holds the ids of close hobbies, used for "next hobby" suggestions. */
data class Hobby(
    val id: String,
    val nameJa: String,
    val nameEn: String,
    val theme: ThemeSpec,
    val firstStep: String,
    val related: List<String>,
)

object Hobbies {

    val astronomy = Hobby(
        id = "astronomy",
        nameJa = "天体観測",
        nameEn = "Stargazing",
        theme = ThemeSpec(
            id = "astronomy", displayName = "Night Sky",
            background = 0xFF0B1220L, surface = 0xFF141C2EL, surfaceAlpha = 0.7f,
            onBackground = 0xFFE8ECF4L, accent = 0xFFD9736BL,
            fontMood = FontMood.SANS_LIGHT, gridStyle = GridStyle.SQUARE,
            cornerRadiusDp = 20, contentPaddingDp = 24, recordEmphasis = RecordEmphasis.PHOTO,
        ),
        firstStep = "Tonight, look up for 5 minutes and take one photo of the moon.",
        related = listOf("reading", "diving"),
    )

    val running = Hobby(
        id = "running",
        nameJa = "ランニング",
        nameEn = "Running",
        theme = ThemeSpec(
            id = "running", displayName = "Morning Road",
            background = 0xFFF7F7F5L, surface = 0xFFFFFFFFL, surfaceAlpha = 0.9f,
            onBackground = 0xFF1C1C1EL, accent = 0xFFE8503CL,
            fontMood = FontMood.MONO_NUMERIC, gridStyle = GridStyle.TIMELINE,
            cornerRadiusDp = 12, contentPaddingDp = 20, recordEmphasis = RecordEmphasis.NUMBERS,
        ),
        firstStep = "Put on your shoes and walk or jog for just 10 minutes around your neighborhood.",
        related = listOf("diving", "bread"),
    )

    val diving = Hobby(
        id = "diving",
        nameJa = "ダイビング",
        nameEn = "Diving",
        theme = ThemeSpec(
            id = "diving", displayName = "Deep Blue",
            background = 0xFF0A3D4AL, surface = 0xFF11525FL, surfaceAlpha = 0.6f,
            onBackground = 0xFFE6F6F5L, accent = 0xFF5FD3C8L,
            fontMood = FontMood.SANS_ROUNDED, gridStyle = GridStyle.MASONRY,
            cornerRadiusDp = 32, contentPaddingDp = 28, recordEmphasis = RecordEmphasis.PHOTO,
        ),
        firstStep = "Look up an introductory dive near you and book one.",
        related = listOf("astronomy", "running"),
    )

    val reading = Hobby(
        id = "reading",
        nameJa = "読書",
        nameEn = "Reading",
        theme = ThemeSpec(
            id = "reading", displayName = "Paper & Ink",
            background = 0xFFF3EDE0L, surface = 0xFFFAF6ECL, surfaceAlpha = 0.9f,
            onBackground = 0xFF2B2622L, accent = 0xFF8A5A3CL,
            fontMood = FontMood.SERIF, gridStyle = GridStyle.PORTRAIT,
            cornerRadiusDp = 8, contentPaddingDp = 28, recordEmphasis = RecordEmphasis.TEXT,
        ),
        firstStep = "Read just 10 pages of a book you already own before bed.",
        related = listOf("movies", "astronomy", "bread"),
    )

    val movies = Hobby(
        id = "movies",
        nameJa = "映画",
        nameEn = "Movies",
        theme = ThemeSpec(
            id = "movies", displayName = "Dark Theater",
            background = 0xFF16110FL, surface = 0xFF241B17L, surfaceAlpha = 0.85f,
            onBackground = 0xFFF1E6D8L, accent = 0xFFE0A458L,
            fontMood = FontMood.DISPLAY_CONDENSED, gridStyle = GridStyle.LANDSCAPE,
            cornerRadiusDp = 6, contentPaddingDp = 20, recordEmphasis = RecordEmphasis.PHOTO,
        ),
        firstStep = "Pick one film for this weekend, then record one sentence about it afterward.",
        related = listOf("reading", "bread"),
    )

    val bread = Hobby(
        id = "bread",
        nameJa = "パン作り",
        nameEn = "Bread baking",
        theme = ThemeSpec(
            id = "bread", displayName = "Wheat Kitchen",
            background = 0xFFF6E9D2L, surface = 0xFFFFF7E8L, surfaceAlpha = 0.9f,
            onBackground = 0xFF4A3524L, accent = 0xFFB86F2AL,
            fontMood = FontMood.HANDWRITTEN, gridStyle = GridStyle.SQUARE,
            cornerRadiusDp = 24, contentPaddingDp = 24, recordEmphasis = RecordEmphasis.STEPS,
        ),
        firstStep = "Bake one simple loaf with just flour, water, yeast, and salt.",
        related = listOf("reading", "movies", "running"),
    )

    val all: List<Hobby> = listOf(astronomy, running, diving, reading, movies, bread)

    private val byId: Map<String, Hobby> = all.associateBy { it.id }

    fun find(id: String): Hobby? = byId[id]

    /**
     * Rule-based "next hobby" suggestions: follows the links from the hobbies
     * the user already records and returns the most-linked ones first.
     */
    fun suggestNext(current: Set<String>, limit: Int = 3): List<Hobby> {
        val scores = mutableMapOf<String, Int>()
        for (id in current) {
            val hobby = byId[id] ?: continue
            for (relatedId in hobby.related) {
                if (relatedId !in current) scores[relatedId] = (scores[relatedId] ?: 0) + 1
            }
        }
        return scores.entries
            .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }.thenBy { it.key })
            .mapNotNull { byId[it.key] }
            .take(limit)
    }
}