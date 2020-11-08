package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.Indicator
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import org.springframework.stereotype.Component

/**
 * Preço / LPA
 */
@Component
class PriceOverProfitPerAction {

    fun computePriceOverProfitPerAction(trimester: TrimesterInformation): TrimesterInformation {
        val profitPerAction = trimester.indicators.firstOrNull { it.name == "profitPerAction" }?.value ?: Float.NaN

        val priceOverProfitPerActionList = trimester.quoteAdjustedPrice
                .mapNotNull { quote ->
                    if (profitPerAction.isNaN()) {
                        null
                    } else {
                        val code = quote.stockCode
                        val value = quote.quote / profitPerAction

                        Indicator("priceOverProfitPerAction_$code", value)
                    }
                }

        val newIndicators = listOf(trimester.indicators).flatten() + priceOverProfitPerActionList

        return trimester.copy(indicators = newIndicators)
    }

}
