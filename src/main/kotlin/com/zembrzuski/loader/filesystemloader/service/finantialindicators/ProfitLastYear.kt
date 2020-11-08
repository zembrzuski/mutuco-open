package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import org.springframework.stereotype.Component
import java.lang.IllegalStateException

@Component
class ProfitLastYear {

    fun computeProfitLastYear(
            balancesAndQuotesInformation: List<TrimesterInformation>, year: Int, trimester: Int): Double {

        return balancesAndQuotesInformation
                .filter { it.balanceTrimesterInformation.year <= year && it.balanceTrimesterInformation.trimester <= trimester }
                .takeLast(4)
                .sumByDouble {
                    it.balanceTrimesterInformation.netProfit?.toDouble() ?: throw IllegalStateException("errow")
                }
    }

}
