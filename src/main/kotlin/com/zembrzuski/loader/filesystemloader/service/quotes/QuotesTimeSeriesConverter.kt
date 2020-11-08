package com.zembrzuski.loader.filesystemloader.service.quotes

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.QuotesTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Candle
import com.zembrzuski.loader.filesystemloader.util.DateParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Component
class QuotesTimeSeriesConverter @Autowired constructor(
        private val quotesPersistence: QuotesPersistence,
        private val quotesHelper: QuotesHelper,
        private val yahooQuotesRetriever: YahooQuotesRetriever,
        private val dateParser: DateParser) {

    fun getTimeSeries(
            cvmCode: String,
            stockCode: String,
            balanceTrimesterInformationSequence: List<BalanceTrimesterInformation>)
            : String? {

        val retrievedFromFilesystem =
                quotesPersistence.getFilesystemTimeSeries(cvmCode, stockCode)

        val filesystemQuotesAreUpToDate = quotesHelper.filesystemQuotesAreUpToDate(
                retrievedFromFilesystem, balanceTrimesterInformationSequence)

        return if (filesystemQuotesAreUpToDate) {
            retrievedFromFilesystem
        } else {
            return yahooQuotesRetriever.retrieve("$stockCode.SA", "2000-01-01", dateParser.asString(LocalDate.now()))
        }
    }

    fun getLastQuote(
            cvmCode: String, stockCode: String, year: Int, trimester: Int,
            timeSeries: Map<LocalDate, Candle>): QuotesTrimesterInformation? {

        val reduced = timeSeries
                .keys
                .filter { key ->
                    val lastDayOfTrimester = LocalDate.of(year, trimester * 3, 1)
                            .with(TemporalAdjusters.lastDayOfMonth()).plusDays(1)

                    key.isBefore(lastDayOfTrimester)
                }
                .max()

        val candle = timeSeries[reduced]

        return if (candle != null) {
            QuotesTrimesterInformation(cvmCode, stockCode, year, trimester, candle.close, candle.adjustedClose)
        } else {
            null
        }
    }

}
