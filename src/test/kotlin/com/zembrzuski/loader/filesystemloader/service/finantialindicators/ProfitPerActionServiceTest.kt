package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.SocialCapitalEntity
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.StockQuantity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class ProfitPerActionServiceTest {

    @MockK
    private lateinit var profitLastYearService: ProfitLastYearService

    @InjectMockKs
    private lateinit var profitPerActionService: ProfitPerActionService

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun getProfitPerAction() {
        // given
        val currentTrimester = TrimesterInformation(
                BalanceTrimesterInformation(
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
        )
        val balancesAndQuotesInformation = listOf(currentTrimester)

        every {
            profitLastYearService.computeProfitLastYear(balancesAndQuotesInformation, 2020, 1)
        } returns 1000.0


        val expected = 1000.0/30

        // when
        val result = profitPerActionService.getProfitPerActionInternal(balancesAndQuotesInformation, currentTrimester)

        // then
        Assert.assertEquals(result, expected, 0.001)
    }


    @Test
    fun notANumberWhenThereAreNo4Balances() {
        // given
        val currentTrimester = TrimesterInformation(
                BalanceTrimesterInformation(
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
        )
        val balancesAndQuotesInformation = listOf(currentTrimester)

        every {
            profitLastYearService.computeProfitLastYear(balancesAndQuotesInformation, 2020, 1)
        } returns Double.NaN

        // when
        val result = profitPerActionService.getProfitPerActionInternal(balancesAndQuotesInformation, currentTrimester)

        // then
        assertTrue(result.isNaN())
    }

}
