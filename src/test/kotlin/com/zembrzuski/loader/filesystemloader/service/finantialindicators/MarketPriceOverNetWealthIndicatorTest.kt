package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.google.cloud.Timestamp
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.Indicator
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.IndicatorsEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.SectorClassification
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class MarketPriceOverNetWealthIndicatorTest {

    @Test
    fun presentMarketPriceAndPresentNetWealth() {
        // given
        val balanceTrimesterInformation = BalanceTrimesterInformation(
                balanceId = 1L,
                cvmCode = "x",
                year = 2020,
                trimester = 1,
                netWealth = 22.2F)

        val marketPrice = Indicator(IndicatorsEnum.MARKET_PRICE.name.toLowerCase(), 55.5F)

        val trimester = TrimesterInformation(
                balanceTrimesterInformation = balanceTrimesterInformation,
                indicators = listOf(marketPrice))

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", emptyList(), "x")
        val parameters = IndicatorParameters(trimester, companyInfo)

        // when
        val result = MarketPriceOverNetWealthIndicator().valueFunction(parameters)

        // then
        assertEquals(2.5F, result)
    }

    @Test
    fun presentMarketPriceAndAbsentNetWealth() {
        // given
        val balanceTrimesterInformation = BalanceTrimesterInformation(
                balanceId = 1L,
                cvmCode = "x",
                year = 2020,
                trimester = 1,
                netWealth = null)

        val marketPrice = Indicator(IndicatorsEnum.MARKET_PRICE.name.toLowerCase(), 55.5F)

        val trimester = TrimesterInformation(
                balanceTrimesterInformation = balanceTrimesterInformation,
                indicators = listOf(marketPrice))

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", emptyList(), "x")
        val parameters = IndicatorParameters(trimester, companyInfo)

        // when
        val result = MarketPriceOverNetWealthIndicator().valueFunction(parameters)

        // then
        assertEquals(Float.NaN, result)
    }

    @Test
    fun absentMarketPriceAndPresentNetWealth() {
        // given
        val balanceTrimesterInformation = BalanceTrimesterInformation(
                balanceId = 1L,
                cvmCode = "x",
                year = 2020,
                trimester = 1,
                netWealth = 22.2F)

        val trimester = TrimesterInformation(
                balanceTrimesterInformation = balanceTrimesterInformation,
                indicators = emptyList())

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", emptyList(), "x")
        val parameters = IndicatorParameters(trimester, companyInfo)

        // when
        val result = MarketPriceOverNetWealthIndicator().valueFunction(parameters)

        // then
        assertEquals(Float.NaN, result)
    }

    @Test
    fun absentMarketPriceAndAbsentNetWealth() {
        // given
        val balanceTrimesterInformation = BalanceTrimesterInformation(
                balanceId = 1L,
                cvmCode = "x",
                year = 2020,
                trimester = 1,
                netWealth = null)

        val otherIndicator = Indicator(IndicatorsEnum.MARKET_PRICE_OVER_NET_WEALTH.name.toLowerCase(), 55.5F)

        val trimester = TrimesterInformation(
                balanceTrimesterInformation = balanceTrimesterInformation,
                indicators = listOf(otherIndicator))

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", emptyList(), "x")
        val parameters = IndicatorParameters(trimester, companyInfo)

        // when
        val result = MarketPriceOverNetWealthIndicator().valueFunction(parameters)

        // then
        assertEquals(Float.NaN, result)
    }

    @Test
    fun enum() {
        assertEquals(MarketPriceOverNetWealthIndicator().enumFunction(), IndicatorsEnum.MARKET_PRICE_OVER_NET_WEALTH)
    }
}