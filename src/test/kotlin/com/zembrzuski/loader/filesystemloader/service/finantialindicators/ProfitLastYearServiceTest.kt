package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

internal class ProfitLastYearServiceTest {

    @MockK
    private lateinit var lastBalancesFilter: LastBalancesFilter

    @InjectMockKs
    private lateinit var profitLastYear: ProfitLastYearService

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun computeProfitCorrectly() {
        // given
        val balance2019_01 = createBalance(1L, "cvmCode", 2019, 1, 1.5F)
        val balance2019_02 = createBalance(2L, "cvmCode", 2019, 2, 2.5F)
        val balance2019_03 = createBalance(3L, "cvmCode", 2019, 3, 3.5F)
        val balance2019_04 = createBalance(4L, "cvmCode", 2019, 4, 4.5F)
        val balance2020_01 = createBalance(5L, "cvmCode", 2020, 1, 5.5F)
        val balance2020_02 = createBalance(6L, "cvmCode", 2020, 2, 6.5F)

        val balancesAndQuotesInformation = listOf(
                balance2019_01, balance2019_02, balance2019_03, balance2019_04, balance2020_01, balance2020_02)

        every { lastBalancesFilter.getLastFourBalances(balancesAndQuotesInformation, 2020, 1) } returns
                listOf(balance2019_02, balance2019_03, balance2019_04, balance2020_01)

        val expected = 5.5 + 4.5 + 3.5 + 2.5

        // when
        val result = profitLastYear.computeProfitLastYear(balancesAndQuotesInformation, 2020, 1)

        // then
        assertEquals(result, expected)
    }

    @Test
    fun notANumberWhenInsufficientData() {
        // given
        val balance2019_03 = createBalance(3L, "cvmCode", 2019, 3, 3.5F)
        val balance2019_04 = createBalance(4L, "cvmCode", 2019, 4, 4.5F)
        val balance2020_01 = createBalance(5L, "cvmCode", 2020, 1, 5.5F)
        val balance2020_02 = createBalance(6L, "cvmCode", 2020, 2, 6.5F)

        val balancesAndQuotesInformation = listOf(
                balance2019_03, balance2019_04, balance2020_01, balance2020_02)

        every { lastBalancesFilter.getLastFourBalances(balancesAndQuotesInformation, 2020, 1) } returns
                listOf(balance2019_03, balance2019_04, balance2020_01)
        // when
        val result = profitLastYear.computeProfitLastYear(balancesAndQuotesInformation, 2020, 1)

        // then
        assertTrue(result.isNaN())
    }

    @Test
    fun notANumberWhenNetProfitIsNull() {
        // given
        val balance2019_01 = createBalance(1L, "cvmCode", 2019, 1, 1.5F)
        val balance2019_02 = createBalance(2L, "cvmCode", 2019, 2, 2.5F)
        val balance2019_03 = createBalance(3L, "cvmCode", 2019, 3, 3.5F)
        val balance2019_04 = createBalance(4L, "cvmCode", 2019, 4, 4.5F)
        val balance2020_01 = createBalance(5L, "cvmCode", 2020, 1, null)
        val balance2020_02 = createBalance(6L, "cvmCode", 2020, 2, 6.5F)

        val balancesAndQuotesInformation = listOf(
                balance2019_01, balance2019_02, balance2019_03, balance2019_04, balance2020_01, balance2020_02)

        every { lastBalancesFilter.getLastFourBalances(balancesAndQuotesInformation, 2020, 1) } returns
                listOf(balance2019_02, balance2019_03, balance2019_04, balance2020_01)

        // when
        val result = profitLastYear.computeProfitLastYear(balancesAndQuotesInformation, 2020, 1)

        // then
        assertTrue(result.isNaN())
    }

    private fun createBalance(balanceId: Long, cvmCode: String, year: Int, trimester: Int, netProfit: Float?): TrimesterInformation {
        return TrimesterInformation(
                BalanceTrimesterInformation(
                        balanceId = balanceId,
                        cvmCode = cvmCode,
                        year = year,
                        trimester = trimester,
                        netProfit = netProfit
                )
        )
    }

}
