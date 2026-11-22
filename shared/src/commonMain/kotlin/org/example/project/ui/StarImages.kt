package org.example.project.ui

import org.example.project.model.SpectralClass
import org.example.project.model.Star
import org.jetbrains.compose.resources.DrawableResource
import kotlinproject.shared.generated.resources.Res
import kotlinproject.shared.generated.resources.star_blue
import kotlinproject.shared.generated.resources.star_white
import kotlinproject.shared.generated.resources.star_yellow
import kotlinproject.shared.generated.resources.star_orange
import kotlinproject.shared.generated.resources.star_red

/**
 * Real public-domain / open-licence imagery, chosen by spectral class:
 *   O,B → Rigel (blue supergiant)        — Wikimedia Commons
 *   A   → Sirius A & B (Hubble, NASA/ESA, public domain)
 *   F,G → The Sun (NASA, public domain)
 *   K   → Mira (Hubble, NASA/ESA, public domain)
 *   M   → Betelgeuse (ALMA, ESO, CC BY 4.0 — attributed in Account ▸ Credits)
 */
fun imageFor(star: Star): DrawableResource = when (star.spectralClass) {
    SpectralClass.O, SpectralClass.B -> Res.drawable.star_blue
    SpectralClass.A                  -> Res.drawable.star_white
    SpectralClass.F, SpectralClass.G -> Res.drawable.star_yellow
    SpectralClass.K                  -> Res.drawable.star_orange
    SpectralClass.M                  -> Res.drawable.star_red
}
