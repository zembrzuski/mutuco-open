package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.Indicator
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.IndicatorsEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Cria o indicador IndicatorsEnum.MARKET_PRICE_OVER_NET_WEALTH
 *
 * Consiste no valor de mercado dividido pelo patrimônio líquido.
 *
 * Se algum dos valores da divisão estiver ausente, simplesmente não cria
 * o indicador, e retorna um trimester igual ao recebido como parametro.
 */
@Component
class MarketPriceOverNetWealthIndicator : IndicatorInterface {

    override fun valueFunction(indicatorParameters: IndicatorParameters): Float {
        val trimester = indicatorParameters.trimester

        val netWealth = trimester.balanceTrimesterInformation.netWealth ?: Float.NaN

        val marketPrice = trimester
                .indicators
                .filter { it.name == IndicatorsEnum.MARKET_PRICE.name.toLowerCase() }
                .map { it.value }
                .firstOrNull() ?: Float.NaN

        return marketPrice / netWealth
    }

    override fun enumFunction(): IndicatorsEnum {
        return IndicatorsEnum.MARKET_PRICE_OVER_NET_WEALTH
    }

}
