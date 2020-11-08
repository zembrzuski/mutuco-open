package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.zembrzuski.loader.filesystemloader.domain.firebase.Quote
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.IndicatorsEnum
import org.springframework.stereotype.Component

/**
 * Computa o valor de mercado de uma empresa.
 * Basicamente, ele multiplica a quantidade de ações pelo valor de cada uma das cotações e soma.
 *
 * Em caso de dúvida, veja os testes. Lá, todos comportamentos previstos estão descritos em junits. Existem
 * javadocs que descrevem comportamentos extremos.
 *
 * Atenção: será que devo descontar as ações em tesouraria para o cálculo do valor de mercado? Atualmente, informações
 * de tesouraria são ignoradas.
 *
 * TODO quebrar essa classe em 3 classes: essa aqui deve ficar sendo a classe orquestradora. Criar duas classes
 * especializadas: uma em valor de mercado para ações ordinárias e outra para ações preferenciais.
 */
@Component
class MarketPriceIndicator : IndicatorInterface {

    private val ordinaryQuotesRegex = """[a-zA-Z]+3$""".toRegex()
    private val preferredQuotesRegex = """[a-zA-Z]+[456789]$""".toRegex()

    private val notAQuote = Quote("Foo", Float.NaN)

    override fun enumFunction(): IndicatorsEnum {
        return IndicatorsEnum.MARKET_PRICE
    }

    override fun valueFunction(indicatorParameters: IndicatorParameters): Float {
        val trimesterInformation: TrimesterInformation = indicatorParameters.trimester
        val stockCodes: List<String> = indicatorParameters.companyInformation.stockCodes

        val generalSocialCapital = trimesterInformation.balanceTrimesterInformation.socialCapitalEntity?.general

        val ordinary = getOrdinaryPart(
                stockCodes, trimesterInformation, generalSocialCapital?.ordinary)

        val preferred = getPreferredPart(
                stockCodes, trimesterInformation, generalSocialCapital?.preferred)

        return (ordinary + preferred).fold(0F) { acc, fl -> acc + fl }
    }

    private fun getOrdinaryPart(
            stockCodes: List<String>,
            trimesterInformation: TrimesterInformation,
            quantity: Long?)
            : List<Float> {

        val filteredQuotes = stockCodes.filter { stockCode -> ordinaryQuotesRegex.matches(stockCode) }

        return filteredQuotes
                .map { stockCode ->
                    val qtd = quantity?.toFloat() ?: Float.NaN

                    multiplyQuantityForPrice(qtd/filteredQuotes.size, stockCode, trimesterInformation.quoteRealPrice)
                }
    }

    private fun getPreferredPart(
            stockCodes: List<String>,
            trimesterInformation: TrimesterInformation,
            quantity: Long?)
            : List<Float> {

        val filteredQuotes = stockCodes.filter { stockCode -> preferredQuotesRegex.matches(stockCode) }

        if (filteredQuotes.isEmpty()) {
            return listOf(0F)
        }

        val existentQuotes = filteredQuotes.mapNotNull {
            quote -> findQuote(trimesterInformation.quoteRealPrice, quote)
        }

        val map = existentQuotes
                .map { quote ->
                    val qtd = quantity?.toFloat() ?: Float.NaN

                    multiplyQuantityForPrice(qtd / existentQuotes.size, quote.stockCode, trimesterInformation.quoteRealPrice)
                }

        if (map.isEmpty()) {
            return map + Float.NaN
        }

        return map
    }

    private fun multiplyQuantityForPrice(
            quantity: Float, stockCode: String, quoteRealPrice: List<Quote>): Float {

        val first = findQuote(quoteRealPrice, stockCode) ?: notAQuote

        return first.quote * quantity
    }

    private fun findQuote(quoteRealPrice: List<Quote>, stockCode: String) =
            quoteRealPrice.firstOrNull { quote -> quote.stockCode == stockCode }

}
