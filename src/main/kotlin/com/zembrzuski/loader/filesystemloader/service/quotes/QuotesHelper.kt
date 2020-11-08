package com.zembrzuski.loader.filesystemloader.service.quotes

import com.google.api.client.util.Maps
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Candle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Component
class QuotesHelper @Autowired constructor(private val parser: YahooQuotesParser) {

    fun filesystemQuotesAreUpToDate(
            retrievedFromFilesystem: String?,
            balanceTrimesterInformationSequence: List<BalanceTrimesterInformation>): Boolean {

        val timeSeries = getTimeSeries(retrievedFromFilesystem)

        return filesystemQuotesAreUpToDate(timeSeries, balanceTrimesterInformationSequence)
    }

    fun filesystemQuotesAreUpToDate(
            timeSeries: Map<LocalDate, Candle>,
            balanceTrimesterInformationSequence: List<BalanceTrimesterInformation>): Boolean {

        val maxFilesystem = timeSeries.keys.max()

        val lastBalanceDate = balanceTrimesterInformationSequence
                .map { yearAndTrimesterToLocalDate(it.year, it.trimester) }
                .max()

        return if (maxFilesystem != null && lastBalanceDate != null) {
            maxFilesystem.isAfter(lastBalanceDate) || maxFilesystem.isEqual(lastBalanceDate)
        } else {
            false
        }
    }

    fun getTimeSeries(
            retrievedQuotesFromFilesystem: String?)
            : Map<LocalDate, Candle> {

        return if (retrievedQuotesFromFilesystem != null) {
            parser.parseResponse(retrievedQuotesFromFilesystem)
        } else {
            Maps.newHashMap()
        }
    }

    fun yearAndTrimesterToLocalDate(year: Int, trimester: Int): LocalDate =
            LocalDate.of(year, trimester * 3, 1).with(TemporalAdjusters.lastDayOfMonth())

}
