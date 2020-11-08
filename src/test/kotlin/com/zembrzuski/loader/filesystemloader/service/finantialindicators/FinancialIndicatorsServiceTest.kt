package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.Indicator
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.IndicatorsEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.SectorClassification
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FinancialIndicatorsServiceTest {

    @MockK lateinit var templateIndicator: TemplateIndicator
    @MockK lateinit var marketPriceIndicator: MarketPriceIndicator
    @MockK lateinit var marketPriceOverNetWealthIndicator: MarketPriceOverNetWealthIndicator
    @MockK lateinit var profitPerActionService: ProfitPerActionService
    @MockK lateinit var priceOverProfitPerAction: PriceOverProfitPerAction
    @InjectMockKs lateinit var financialIndicatorsService: FinancialIndicatorsService

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun testFlow() {
        // given
        val balanceTrimesterInforamation = BalanceTrimesterInformation(
                balanceId = 1L,
                cvmCode = "cvm",
                year = 2020,
                trimester = 1)

        val trimesterInformation = TrimesterInformation(balanceTrimesterInforamation)
        val balancesAndQuotesInformation = listOf(trimesterInformation)
        val sector = SectorClassification("a", "b", "c")
        val companyInformation = GeneralCompanyInformation("x", sector, "x", "x", emptyList(), "x")

        val indicatorParameters = IndicatorParameters(trimesterInformation, companyInformation)


        val expectedIndicator1 = Indicator("market_price", 1F)

        val trimesterWithMarketPrice = trimesterInformation.copy(
                indicators = listOf(expectedIndicator1)
        )

        every { marketPriceIndicator.valueFunction(indicatorParameters) } returns 1F
        every { marketPriceIndicator.enumFunction() } returns IndicatorsEnum.MARKET_PRICE

        every {
            templateIndicator.template(indicatorParameters, marketPriceIndicator)
        } returns trimesterWithMarketPrice

        val expectedIndicator2 = Indicator("market_price_over_net_wealth", 2F)
        val indicatorParameters2 = IndicatorParameters(trimesterWithMarketPrice, companyInformation)

        val trimesterWithMarketPriceAndMarketPriceOverNetWealth = trimesterInformation.copy(
                indicators = listOf(expectedIndicator1, expectedIndicator2)
        )

        every { marketPriceOverNetWealthIndicator.valueFunction(indicatorParameters) } returns 2F
        every { marketPriceOverNetWealthIndicator.enumFunction() } returns IndicatorsEnum.MARKET_PRICE_OVER_NET_WEALTH

        every {
            templateIndicator.template(indicatorParameters2, marketPriceOverNetWealthIndicator)
        } returns trimesterWithMarketPriceAndMarketPriceOverNetWealth


        val expectedIndicator3 = Indicator("lucro por acao", 66F)
        val expectedIndicator4 = Indicator("plpa", 222.2F)

        val trimesterWithMarketPriceAndMarketPriceOverNetWealthAndProfitPerAction = trimesterInformation.copy(
                indicators = listOf(expectedIndicator1, expectedIndicator2, expectedIndicator3)
        )

        every { profitPerActionService.computeProfitPerAction(trimesterWithMarketPriceAndMarketPriceOverNetWealth, balancesAndQuotesInformation) } returns
                trimesterWithMarketPriceAndMarketPriceOverNetWealthAndProfitPerAction

        val trimesterWithMarketPriceAndMarketPriceOverNetWealthAndProfitPerActionAndPlPA = trimesterInformation.copy(
                indicators = listOf(expectedIndicator1, expectedIndicator2, expectedIndicator3, expectedIndicator4)
        )

        every { priceOverProfitPerAction.computePriceOverProfitPerAction(trimesterWithMarketPriceAndMarketPriceOverNetWealthAndProfitPerAction) } returns
                trimesterWithMarketPriceAndMarketPriceOverNetWealthAndProfitPerActionAndPlPA

        // when
        val result = financialIndicatorsService.compute(balancesAndQuotesInformation, companyInformation)

        // then
        assertEquals(result, listOf(trimesterWithMarketPriceAndMarketPriceOverNetWealthAndProfitPerActionAndPlPA))
    }

}