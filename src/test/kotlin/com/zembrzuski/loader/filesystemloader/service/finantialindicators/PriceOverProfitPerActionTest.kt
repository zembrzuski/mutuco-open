package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.*
import com.zembrzuski.loader.filesystemloader.domain.mydomain.StockQuantity
import org.junit.Assert.assertEquals
import org.junit.Test


internal class PriceOverProfitPerActionTest {

    private val priceOverProfitPerAction = PriceOverProfitPerAction()

    @Test
    fun noActionForAbsentProfitPerAction() {
        // given
        val currentTrimester = TrimesterInformation(
                balanceTrimesterInformation()
        )
        val balancesAndQuotesInformation = listOf(currentTrimester)

        // when
        val result = priceOverProfitPerAction.computePriceOverProfitPerAction(currentTrimester)

        // then
        assertEquals(result, currentTrimester)
    }

    @Test
    fun appendPriceOverProfitPerActionSuccessfully() {
        // given
        val currentTrimester = TrimesterInformation(
                balanceTrimesterInformation = balanceTrimesterInformation(),
                indicators = listOf(Indicator("profitPerAction", 2.5f)),
                quoteAdjustedPrice = listOf(
                        Quote("AAAA1", 5f),
                        Quote("BBBB2", 10f)
                )
        )

        val expected = TrimesterInformation(
                balanceTrimesterInformation = balanceTrimesterInformation(),
                indicators = listOf(
                        Indicator("profitPerAction", 2.5f),
                        Indicator("priceOverProfitPerAction_AAAA1", 2f),
                        Indicator("priceOverProfitPerAction_BBBB2", 4f)
                ),
                quoteAdjustedPrice = listOf(
                        Quote("AAAA1", 5f),
                        Quote("BBBB2", 10f)
                )
        )

        val balancesAndQuotesInformation = listOf(currentTrimester)

        // when
        val result = priceOverProfitPerAction.computePriceOverProfitPerAction(currentTrimester)

        // then
        assertEquals(result, expected)
    }

    private fun balanceTrimesterInformation(): BalanceTrimesterInformation {
        return BalanceTrimesterInformation(
                balanceId = 1L,
                cvmCode = "ae",
                year = 2020,
                trimester = 1,
                socialCapitalEntity = SocialCapitalEntity(
                        general = StockQuantity(
                                ordinary = 10,
                                preferred = 20,
                                total = 30
                        ),
                        treasury = StockQuantity(
                                ordinary = 1,
                                preferred = 2,
                                total = 3
                        )
                )
        )
    }

}