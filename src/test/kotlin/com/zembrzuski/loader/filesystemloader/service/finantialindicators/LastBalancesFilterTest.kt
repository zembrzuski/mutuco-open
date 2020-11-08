package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import junit.framework.TestCase
import org.junit.Test

internal class LastBalancesFilterTest {

    private val lastBalancesFilter = LastBalancesFilter()

    @Test
    fun getBalances() {
        // given
        val balance2019_01 = createBalance(1L, "cvmCode", 2019, 1, 1.5F)
        val balance2019_02 = createBalance(2L, "cvmCode", 2019, 2, 2.5F)
        val balance2019_03 = createBalance(3L, "cvmCode", 2019, 3, 3.5F)
        val balance2019_04 = createBalance(4L, "cvmCode", 2019, 4, 4.5F)
        val balance2020_01 = createBalance(5L, "cvmCode", 2020, 1, 5.5F)
        val balance2020_02 = createBalance(6L, "cvmCode", 2020, 2, 6.5F)

        val expected = listOf(balance2020_01, balance2019_04, balance2019_03, balance2019_02).reversed()

        val balancesAndQuotesInformation = listOf(
                balance2019_01, balance2019_02, balance2019_03, balance2019_04, balance2020_01, balance2020_02)

        // when
        val result = lastBalancesFilter.getLastFourBalances(balancesAndQuotesInformation, 2020, 1)

        // then
        TestCase.assertEquals(result, expected)
    }

    private fun createBalance(balanceId: Long, cvmCode: String, year: Int, trimester: Int, netProfit: Float): TrimesterInformation {
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
