package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.IllegalStateException

@Component
class ProfitLastYearService @Autowired constructor(private val lastBalancesFilter: LastBalancesFilter) {

    fun computeProfitLastYear(
            balancesAndQuotesInformation: List<TrimesterInformation>, year: Int, trimester: Int): Double {

        val lastBalances = lastBalancesFilter.getLastFourBalances(balancesAndQuotesInformation, year, trimester)

        if (lastBalances.size != 4) {
            return Double.NaN
        }

        return lastBalances
                .sumByDouble {
                    it.balanceTrimesterInformation.netProfit?.toDouble() ?: Double.NaN
                }
    }

}
