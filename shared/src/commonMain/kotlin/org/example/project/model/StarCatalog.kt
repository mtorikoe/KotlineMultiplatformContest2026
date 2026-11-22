package org.example.project.model

/**
 * Catalog of 20 real stars spanning every spectral class (O through M),
 * multiple constellations, and a wide range of luminosities and sizes.
 *
 * Data sourced from the SIMBAD database and standard astrophysical
 * references. Values are rounded for readability.
 */
object StarCatalog {

    val stars: List<Star> = listOf(

        // ── O / B  (hot, blue-white) ───────────────────────────

        Star(
            id = "spica", name = "Spica",
            constellation = "Virgo",
            spectralType = "B1III", spectralClass = SpectralClass.B,
            temperatureK = 25_300, luminositySolar = 20_500.0,
            distanceLy = 250.0, apparentMag = 0.97, radiusSolar = 7.47,
            description = "A blue giant whose rapid spin distorts it into an oblate shape"
        ),
        Star(
            id = "rigel", name = "Rigel",
            constellation = "Orion",
            spectralType = "B8Ia", spectralClass = SpectralClass.B,
            temperatureK = 12_100, luminositySolar = 120_000.0,
            distanceLy = 860.0, apparentMag = 0.13, radiusSolar = 78.9,
            description = "Orion's brilliant blue foot, one of the most luminous stars visible"
        ),
        Star(
            id = "bellatrix", name = "Bellatrix",
            constellation = "Orion",
            spectralType = "B2III", spectralClass = SpectralClass.B,
            temperatureK = 22_000, luminositySolar = 9_211.0,
            distanceLy = 250.0, apparentMag = 1.64, radiusSolar = 5.75,
            description = "The Amazon Star, marking Orion's left shoulder"
        ),
        Star(
            id = "regulus", name = "Regulus",
            constellation = "Leo",
            spectralType = "B7V", spectralClass = SpectralClass.B,
            temperatureK = 12_460, luminositySolar = 316.0,
            distanceLy = 79.3, apparentMag = 1.40, radiusSolar = 3.09,
            description = "Heart of the lion, spinning so fast it nearly tears itself apart"
        ),
        Star(
            id = "achernar", name = "Achernar",
            constellation = "Eridanus",
            spectralType = "B6V", spectralClass = SpectralClass.B,
            temperatureK = 15_000, luminositySolar = 3_150.0,
            distanceLy = 139.0, apparentMag = 0.46, radiusSolar = 6.78,
            description = "The flattest star known, at the river's end"
        ),

        // ── A  (white) ────────────────────────────────────────

        Star(
            id = "sirius", name = "Sirius",
            constellation = "Canis Major",
            spectralType = "A1V", spectralClass = SpectralClass.A,
            temperatureK = 9_940, luminositySolar = 25.4,
            distanceLy = 8.6, apparentMag = -1.46, radiusSolar = 1.71,
            description = "The brightest star in Earth's sky, the Dog Star"
        ),
        Star(
            id = "vega", name = "Vega",
            constellation = "Lyra",
            spectralType = "A0V", spectralClass = SpectralClass.A,
            temperatureK = 9_602, luminositySolar = 40.1,
            distanceLy = 25.0, apparentMag = 0.03, radiusSolar = 2.36,
            description = "Once our North Star, surrounded by a dusty debris disk"
        ),
        Star(
            id = "fomalhaut", name = "Fomalhaut",
            constellation = "Piscis Austrinus",
            spectralType = "A3V", spectralClass = SpectralClass.A,
            temperatureK = 8_590, luminositySolar = 16.6,
            distanceLy = 25.1, apparentMag = 1.16, radiusSolar = 1.84,
            description = "The lonely one, ringed by a sharp dust belt"
        ),
        Star(
            id = "deneb", name = "Deneb",
            constellation = "Cygnus",
            spectralType = "A2Ia", spectralClass = SpectralClass.A,
            temperatureK = 8_525, luminositySolar = 196_000.0,
            distanceLy = 2_615.0, apparentMag = 1.25, radiusSolar = 203.0,
            description = "A distant supergiant whose true power dwarfs most visible stars"
        ),
        Star(
            id = "altair", name = "Altair",
            constellation = "Aquila",
            spectralType = "A7V", spectralClass = SpectralClass.A,
            temperatureK = 7_670, luminositySolar = 10.6,
            distanceLy = 16.7, apparentMag = 0.76, radiusSolar = 1.63,
            description = "One of the Summer Triangle, spinning once every nine hours"
        ),
        Star(
            id = "canopus", name = "Canopus",
            constellation = "Carina",
            spectralType = "A9II", spectralClass = SpectralClass.A,
            temperatureK = 7_400, luminositySolar = 10_700.0,
            distanceLy = 310.0, apparentMag = -0.74, radiusSolar = 71.0,
            description = "The second-brightest star, a navigation beacon for spacecraft"
        ),

        // ── F  (yellow-white) ─────────────────────────────────

        Star(
            id = "procyon", name = "Procyon",
            constellation = "Canis Minor",
            spectralType = "F5IV", spectralClass = SpectralClass.F,
            temperatureK = 6_530, luminositySolar = 6.93,
            distanceLy = 11.5, apparentMag = 0.34, radiusSolar = 2.05,
            description = "The little dog star, with a faint white dwarf companion"
        ),
        Star(
            id = "polaris", name = "Polaris",
            constellation = "Ursa Minor",
            spectralType = "F7Ib", spectralClass = SpectralClass.F,
            temperatureK = 6_015, luminositySolar = 1_260.0,
            distanceLy = 323.0, apparentMag = 1.98, radiusSolar = 37.5,
            description = "The current North Star, a Cepheid variable that gently pulses"
        ),

        // ── G  (yellow, sun-like) ─────────────────────────────

        Star(
            id = "sun", name = "Sun",
            constellation = "—",
            spectralType = "G2V", spectralClass = SpectralClass.G,
            temperatureK = 5_778, luminositySolar = 1.0,
            distanceLy = 0.0000158, apparentMag = -26.74, radiusSolar = 1.0,
            description = "Our star, an ordinary yellow dwarf in its middle age"
        ),
        Star(
            id = "capella", name = "Capella",
            constellation = "Auriga",
            spectralType = "G8III", spectralClass = SpectralClass.G,
            temperatureK = 4_970, luminositySolar = 78.7,
            distanceLy = 42.9, apparentMag = 0.08, radiusSolar = 11.98,
            description = "A pair of golden giants orbiting each other every 104 days"
        ),

        // ── K  (orange) ──────────────────────────────────────

        Star(
            id = "arcturus", name = "Arcturus",
            constellation = "Boötes",
            spectralType = "K1.5III", spectralClass = SpectralClass.K,
            temperatureK = 4_286, luminositySolar = 170.0,
            distanceLy = 36.7, apparentMag = -0.05, radiusSolar = 25.4,
            description = "An ancient orange giant racing through the galaxy at unusual speed"
        ),
        Star(
            id = "pollux", name = "Pollux",
            constellation = "Gemini",
            spectralType = "K0III", spectralClass = SpectralClass.K,
            temperatureK = 4_666, luminositySolar = 32.7,
            distanceLy = 33.8, apparentMag = 1.14, radiusSolar = 9.06,
            description = "The brighter twin, with a confirmed Jupiter-mass planet"
        ),
        Star(
            id = "aldebaran", name = "Aldebaran",
            constellation = "Taurus",
            spectralType = "K5III", spectralClass = SpectralClass.K,
            temperatureK = 3_910, luminositySolar = 518.0,
            distanceLy = 65.3, apparentMag = 0.86, radiusSolar = 44.2,
            description = "The fiery eye of the bull, an aging giant slowly expanding"
        ),

        // ── M  (red, cool) ───────────────────────────────────

        Star(
            id = "betelgeuse", name = "Betelgeuse",
            constellation = "Orion",
            spectralType = "M1Ia", spectralClass = SpectralClass.M,
            temperatureK = 3_600, luminositySolar = 126_000.0,
            distanceLy = 700.0, apparentMag = 0.50, radiusSolar = 887.0,
            description = "A red supergiant nearing the end, destined to explode as a supernova"
        ),
        Star(
            id = "antares", name = "Antares",
            constellation = "Scorpius",
            spectralType = "M1Ib", spectralClass = SpectralClass.M,
            temperatureK = 3_660, luminositySolar = 75_000.0,
            distanceLy = 550.0, apparentMag = 1.09, radiusSolar = 680.0,
            description = "Rival of Mars, a crimson heart beating in the scorpion's chest"
        )
    )

    /** Stars grouped by constellation (excluding singletons for cleaner groupings). */
    val constellationGroups: Map<String, List<Star>> by lazy {
        stars.groupBy { it.constellation }
            .filter { it.value.size >= 2 }
    }

    /** Stars grouped by spectral class. */
    val spectralGroups: Map<SpectralClass, List<Star>> by lazy {
        stars.groupBy { it.spectralClass }
    }

    fun findById(id: String): Star? = stars.find { it.id == id }

    fun search(query: String): List<Star> {
        val q = query.lowercase()
        return stars.filter {
            it.name.lowercase().contains(q) ||
            it.constellation.lowercase().contains(q) ||
            it.spectralType.lowercase().contains(q)
        }
    }
}
