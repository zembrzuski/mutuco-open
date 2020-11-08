package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import org.springframework.stereotype.Component

@Component
class LastBalancesFilter {

    fun getLastFourBalances(
            balancesAndQuotesInformation: List<TrimesterInformation>, year: Int, trimester: Int)
            : List<TrimesterInformation> {

        return balancesAndQuotesInformation
                .filter {
                    it.balanceTrimesterInformation.year * 4 + it.balanceTrimesterInformation.trimester <= year * 4 + trimester
                }
                .takeLast(4)
    }

}