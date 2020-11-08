package com.zembrzuski.loader.filesystemloader.service.quotes

import com.google.api.client.util.Lists
import com.zembrzuski.loader.filesystemloader.domain.firebase.QuotesTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Candle
import com.zembrzuski.loader.filesystemloader.repository.FirebaseRepository
import com.zembrzuski.loader.filesystemloader.util.DateParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Month

/**
 * Código vergonhosamente lixoso. Refatorar com urgência.
 */
@Component
class IbovQuotesImporter @Autowired constructor(
        private val yahooQuotesRetrieverHelper: YahooQuotesRetrieverHelper,
        private val dateParser: DateParser,
        private val quotesHelper: QuotesHelper,
        private val firebaseRepository: FirebaseRepository) {

    fun doImport() {
        val startDate = yahooQuotesRetrieverHelper.epochConverter("2000-01-01")
        val finishDate = yahooQuotesRetrieverHelper.epochConverter(dateParser.asString(LocalDate.now()))

        val quotesTimeSeriesText = yahooQuotesRetrieverHelper.doSecondRequest("%5EBVSP", startDate, finishDate, "", "").text

        val quotesTimeSeries: Map<LocalDate, Candle> = quotesHelper.getTimeSeries(quotesTimeSeriesText)
        val quotesTimeSeriesTrimester: Map<LocalDate, Candle> = filterTrimester(quotesTimeSeries)
        val trimestralQuote: List<QuotesTrimesterInformation> = convert(quotesTimeSeriesTrimester)
        val filtered: List<QuotesTrimesterInformation> = trimestralQuote.filter { filterGreaterThan2008Trimester4(it) }
        val carteira: List<QuotesTrimesterInformation> = asCarteira(filtered)

        firebaseRepository.persistIbov(carteira)
    }

    private fun asCarteira(filtered: List<QuotesTrimesterInformation>): List<QuotesTrimesterInformation> {
        val carteira = mutableListOf<QuotesTrimesterInformation>()

        carteira.add(QuotesTrimesterInformation(
                cvmCode = "IBOV",
                stockCode = "IBOV",
                year = filtered.first().year,
                trimester = filtered.first().trimester,
                realValue = -1F,
                adjustedValue = 100F
        ))

        for (i in 1 until filtered.size) {
            val previous = filtered[i-1]
            val current = filtered[i]

            val percentual = (current.adjustedValue/previous.adjustedValue-1)
            val valorAnterior = carteira.last().adjustedValue

            val novoValor = valorAnterior + valorAnterior*percentual

            carteira.add(QuotesTrimesterInformation(
                    cvmCode = "IBOV",
                    stockCode = "IBOV",
                    year = current.year,
                    trimester = current.trimester,
                    realValue = -1F,
                    adjustedValue = novoValor
            ))
        }

        return carteira
    }

    private fun filterGreaterThan2008Trimester4(it: QuotesTrimesterInformation): Boolean {
        if (it.year > 2008) {
            return true
        }

        if (it.year < 2008) {
            return false
        }

        // se chegou aqui, eh pq o ano eh 2008
        return it.trimester == 4
    }

    private fun convert(quotesTimeSeriesTrimester: Map<LocalDate, Candle>): List<QuotesTrimesterInformation> {
        return quotesTimeSeriesTrimester.map {
            QuotesTrimesterInformation(
                    cvmCode = "IBOV",
                    stockCode = "IBOV",
                    year = it.key.year,
                    trimester = it.key.monthValue/3,
                    realValue = -1F,
                    adjustedValue = it.value.adjustedClose
            )
        }
    }

    private fun filterTrimester(quotesTimeSeries: Map<LocalDate, Candle>): Map<LocalDate, Candle> {
        val filter: List<LocalDate> = quotesTimeSeries.keys.filter { filterLastDayOfTrimester(it) }

        return quotesTimeSeries.filter { filter.contains(it.key) }
    }

    private fun filterLastDayOfTrimester(date: LocalDate): Boolean {
        if (date.month == Month.MARCH && date.dayOfMonth == 31) {
            return true
        }

        if (date.month == Month.JUNE && date.dayOfMonth == 30) {
            return true
        }

        if (date.month == Month.SEPTEMBER && date.dayOfMonth == 30) {
            return true
        }

        if (date.month == Month.DECEMBER && date.dayOfMonth == 30) {
            return true
        }

        return false
    }

}
