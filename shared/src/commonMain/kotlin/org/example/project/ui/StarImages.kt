package org.example.project.ui

import org.example.project.model.SpectralClass
import org.example.project.model.Star
import org.jetbrains.compose.resources.DrawableResource
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.star_blue
import kotlinproject.shared.generated.resources.star_white
import kotlinproject.shared.generated.resources.star_yellow
import kotlinproject.shared.generated.resources.star_red
import kotlinproject.shared.generated.resources.star_spica
import kotlinproject.shared.generated.resources.star_bellatrix
import kotlinproject.shared.generated.resources.star_regulus
import kotlinproject.shared.generated.resources.star_achernar
import kotlinproject.shared.generated.resources.star_vega
import kotlinproject.shared.generated.resources.star_fomalhaut
import kotlinproject.shared.generated.resources.star_deneb
import kotlinproject.shared.generated.resources.star_canopus
import kotlinproject.shared.generated.resources.star_procyon
import kotlinproject.shared.generated.resources.star_polaris
import kotlinproject.shared.generated.resources.star_aldebaran
import kotlinproject.shared.generated.resources.star_antares

/**
 * The real image of each individual star, where a genuine photo exists.
 *
 * Sources (public domain / open licence): NASA/ESA (Sun, Sirius, Fomalhaut),
 * ESO (Antares VLTI surface, Betelgeuse/ALMA), the Digitized Sky Survey
 * (Spica, Bellatrix, Regulus, Achernar, Deneb) and Wikimedia Commons
 * astrophotographers (Rigel, Vega, Polaris, Aldebaran, Canopus, Procyon).
 * Credited collectively in Account ▸ Credits.
 *
 * A handful of stars have no suitable real photo on hand; they fall back to a
 * real star image of the same colour class rather than ship a wrong picture.
 */
fun imageFor(star: Star): DrawableResource = when (star.id) {
    "rigel"      -> Res.drawable.star_blue      // Rigel, blue supergiant
    "sirius"     -> Res.drawable.star_white     // Sirius A & B (Hubble)
    "sun"        -> Res.drawable.star_yellow    // The Sun
    "betelgeuse" -> Res.drawable.star_red       // Betelgeuse (ALMA)
    "spica"      -> Res.drawable.star_spica
    "bellatrix"  -> Res.drawable.star_bellatrix
    "regulus"    -> Res.drawable.star_regulus
    "achernar"   -> Res.drawable.star_achernar
    "vega"       -> Res.drawable.star_vega
    "fomalhaut"  -> Res.drawable.star_fomalhaut
    "deneb"      -> Res.drawable.star_deneb
    "canopus"    -> Res.drawable.star_canopus
    "procyon"    -> Res.drawable.star_procyon
    "polaris"    -> Res.drawable.star_polaris
    "aldebaran"  -> Res.drawable.star_aldebaran
    "antares"    -> Res.drawable.star_antares
    // Capella, Pollux, Arcturus, Altair — no clean real photo yet; colour-matched fallback below.
    else -> when (star.spectralClass) {
        SpectralClass.O, SpectralClass.B -> Res.drawable.star_blue
        SpectralClass.A, SpectralClass.F -> Res.drawable.star_white
        SpectralClass.G, SpectralClass.K -> Res.drawable.star_yellow
        SpectralClass.M                  -> Res.drawable.star_red
    }
}
