package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.Indicator
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.IndicatorsEnum
import org.springframework.stereotype.Component

@Component
class TemplateIndicator {

    fun template(
            indicatorParameters: IndicatorParameters,
            indicatorInterface: IndicatorInterface)
            : TrimesterInformation {

        val trimester = indicatorParameters.trimester

        val indicators = getIndicator(
                trimester,
                indicatorInterface.valueFunction(indicatorParameters),
                indicatorInterface.enumFunction())

        return trimester.copy(indicators = indicators)
    }

    private fun getIndicator(
            trimester: TrimesterInformation,
            indicatorValue: Float,
            indicatorEnum: IndicatorsEnum)
            : List<Indicator> {

        return if (indicatorValue.isNaN().not()) {
            listOf(trimester.indicators).flatten() + Indicator(indicatorEnum.name.toLowerCase(), indicatorValue)
        } else {
            trimester.indicators
        }
    }

}
