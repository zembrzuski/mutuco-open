package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.Indicator
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProfitPerActionService @Autowired constructor(private val profitLastYearService: ProfitLastYearService) {

    fun computeProfitPerAction(
            trimester: TrimesterInformation,
            balancesAndQuotesInformation: List<TrimesterInformation>)
            : TrimesterInformation {

        val profitPerAction = getProfitPerActionInternal(balancesAndQuotesInformation, trimester)
        val newIndicators = listOf(trimester.indicators).flatten() + Indicator("profitPerAction", profitPerAction.toFloat())

        return trimester.copy(indicators = newIndicators)
    }

    fun getProfitPerActionInternal(
            balancesAndQuotesInformation: List<TrimesterInformation>,
            trimester: TrimesterInformation): Double {

        val profitLastYear = profitLastYearService.computeProfitLastYear(
                balancesAndQuotesInformation,
                trimester.balanceTrimesterInformation.year,
                trimester.balanceTrimesterInformation.trimester)

        val ordinary = trimester.balanceTrimesterInformation.socialCapitalEntity?.general?.ordinary ?: 0
        val preferred = trimester.balanceTrimesterInformation.socialCapitalEntity?.general?.preferred ?: 0

        val quantityActions = ordinary + preferred

        if (profitLastYear.isNaN()) {
            return Double.NaN
        }

        return profitLastYear / quantityActions
    }

}
