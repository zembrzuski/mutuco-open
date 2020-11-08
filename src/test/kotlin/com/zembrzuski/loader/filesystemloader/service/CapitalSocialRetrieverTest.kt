package com.zembrzuski.loader.filesystemloader.service

import com.zembrzuski.loader.filesystemloader.domain.bovespa.ComposicaoCapital
import com.zembrzuski.loader.filesystemloader.domain.bovespa.ComposicaoCapitalSocial
import org.junit.Assert.assertEquals
import org.junit.Test

class CapitalSocialRetrieverTest {

    private val capitalSocialRetriever = CapitalSocialRetriever()

    @Test
    fun successForUniqueSocialCapital() {
        // given
        val composicaoCapital = ComposicaoCapital(1, 2, 3, 4, 5, 6)
        val capitalSocial = ComposicaoCapitalSocial(listOf(composicaoCapital))

        // when
        val result = capitalSocialRetriever.getCapitalSocial(capitalSocial)

        // then
        assertEquals(result, composicaoCapital)
    }

    @Test
    fun successForManyIdenticalSocialCapital() {
        // given
        val composicaoCapital1 = ComposicaoCapital(1, 2, 3, 4, 5, 6)
        val composicaoCapital2 = ComposicaoCapital(1, 2, 3, 4, 5, 6)
        val composicaoCapital3 = ComposicaoCapital(1, 2, 3, 4, 5, 6)

        val capitalSocial = ComposicaoCapitalSocial(listOf(composicaoCapital1, composicaoCapital2, composicaoCapital3))

        // when
        val result = capitalSocialRetriever.getCapitalSocial(capitalSocial)

        // then
        assertEquals(result, composicaoCapital1)
    }

    @Test
    fun successForTwoSimilarCapitalSocial() {
        // given
        val composicaoCapital1 = ComposicaoCapital(1000, 2, 3, 4, 5, 6)
        val composicaoCapital2 = ComposicaoCapital(1001, 2, 3, 4, 5, 6)

        val capitalSocial = ComposicaoCapitalSocial(listOf(composicaoCapital1, composicaoCapital2))

        // when
        val result = capitalSocialRetriever.getCapitalSocial(capitalSocial)

        // then
        assertEquals(result, composicaoCapital2)
    }

    @Test
    fun successAllZeros() {
        // given
        val composicaoCapital1 = ComposicaoCapital(0, 0, 0, 0, 0, 0)
        val composicaoCapital2 = ComposicaoCapital(1001, 2, 3, 4, 5, 6)

        val capitalSocial = ComposicaoCapitalSocial(listOf(composicaoCapital1, composicaoCapital2))

        // when
        val result = capitalSocialRetriever.getCapitalSocial(capitalSocial)

        // then
        assertEquals(result, composicaoCapital2)
    }

}