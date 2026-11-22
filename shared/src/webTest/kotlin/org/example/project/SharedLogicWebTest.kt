package org.example.project

import org.example.project.model.SpectralClass
import org.example.project.model.StarCatalog
import org.example.project.sonification.SonificationEngine
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharedLogicWebTest {

    @Test
    fun hotStarsPlayHigherThanCoolStars() {
        val hot = SonificationEngine.parameterize(StarCatalog.findById("spica")!!)   // B-class, ~25000 K
        val cool = SonificationEngine.parameterize(StarCatalog.findById("betelgeuse")!!) // M-class, ~3600 K
        assertTrue(
            hot.fundamentalHz > cool.fundamentalHz,
            "hotter stars should map to higher pitches"
        )
    }

    @Test
    fun spectralClassFromTemperatureIsConsistent() {
        assertEquals(SpectralClass.G, SpectralClass.fromTemperature(5_778))
        assertEquals(SpectralClass.M, SpectralClass.fromTemperature(3_000))
    }
}
