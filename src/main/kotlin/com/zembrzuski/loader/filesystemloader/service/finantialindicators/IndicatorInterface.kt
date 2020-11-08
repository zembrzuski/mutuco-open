package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.mydomain.IndicatorsEnum

interface IndicatorInterface {

    fun valueFunction(indicatorParameters: IndicatorParameters): Float

    fun enumFunction(): IndicatorsEnum

}
