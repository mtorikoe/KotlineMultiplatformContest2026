package org.example.project.model

/**
 * Spectral classification of stars.
 * Each class maps to a characteristic color temperature and
 * drives the waveform/timbre selection in sonification.
 *
 * Colors here are the *actual astronomical colors* of each class,
 * used for the star glow in the UI.
 */
enum class SpectralClass(
    val label: String,
    val starColor: Long,       // visual glow color (ARGB)
    val tempRangeLow: Int,     // K, inclusive
    val tempRangeHigh: Int     // K, exclusive (O has no upper bound)
) {
    O("O",  0xFF7E9EFF, 30000, 100000),
    B("B",  0xFF9BB0FF, 10000, 30000),
    A("A",  0xFFC8D5FF, 7500,  10000),
    F("F",  0xFFFFF4E8, 6000,  7500),
    G("G",  0xFFFFE8A0, 5200,  6000),
    K("K",  0xFFFFBF6B, 3700,  5200),
    M("M",  0xFFFF7B5A, 2400,  3700);

    companion object {
        fun fromTemperature(tempK: Int): SpectralClass =
            entries.firstOrNull { tempK in it.tempRangeLow until it.tempRangeHigh }
                ?: if (tempK >= 30000) O else M
    }
}

/**
 * A single star with real astronomical data.
 *
 * Every field uses standard astrophysical units so the
 * sonification engine can apply meaningful mappings.
 */
data class Star(
    val id: String,
    val name: String,
    val constellation: String,
    val spectralType: String,       // full MK classification e.g. "G2V"
    val spectralClass: SpectralClass,
    val temperatureK: Int,          // effective surface temperature
    val luminositySolar: Double,    // bolometric, in L☉
    val distanceLy: Double,         // distance in light-years
    val apparentMag: Double,        // apparent visual magnitude
    val radiusSolar: Double,        // equatorial radius in R☉
    val description: String         // one-line flavour text
)
