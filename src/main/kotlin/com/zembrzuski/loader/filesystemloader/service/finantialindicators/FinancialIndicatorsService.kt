package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Calcula os indicadores financeiros de uma empresa que são derivados dos dados de balanco e de cotações.
 */
@Component
class FinancialIndicatorsService @Autowired constructor(
    private val templateIndicator: TemplateIndicator,
    private val marketPriceIndicator: MarketPriceIndicator,
    private val marketPriceOverNetWealthIndicator: MarketPriceOverNetWealthIndicator,
    private val profitPerActionService: ProfitPerActionService,
    private val priceOverProfitPerAction: PriceOverProfitPerAction) {

    fun compute(
            balancesAndQuotesInformation: List<TrimesterInformation>,
            companyInformation: GeneralCompanyInformation)
            : List<TrimesterInformation> {

        return balancesAndQuotesInformation
                .map { trimester -> trimesterInformation(trimester, companyInformation, marketPriceIndicator) }
                .map { trimester -> trimesterInformation(trimester, companyInformation, marketPriceOverNetWealthIndicator) }
                .map { trimester -> profitPerActionService.computeProfitPerAction(trimester, balancesAndQuotesInformation) }
                .map { trimester -> priceOverProfitPerAction.computePriceOverProfitPerAction(trimester) }
    }

    private fun trimesterInformation(
            trimester: TrimesterInformation,
            companyInformation: GeneralCompanyInformation,
            indicator: IndicatorInterface): TrimesterInformation {

        return templateIndicator.template(IndicatorParameters(trimester, companyInformation), indicator)
    }

}
