package com.zembrzuski.loader.filesystemloader.service.quotes

import com.google.cloud.Timestamp
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.QuotesTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Candle
import com.zembrzuski.loader.filesystemloader.util.DateParser
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class QuotesTimeSeriesConverterTest {

    @MockK lateinit var quotesPersistence: QuotesPersistence
    @MockK lateinit var quotesHelper: QuotesHelper
    @MockK lateinit var yahooQuotesRetriever: YahooQuotesRetriever
    @MockK lateinit var dateParser: DateParser
    @InjectMockKs lateinit var quotesTimeSeriesConverter: QuotesTimeSeriesConverter

    @Before
    fun init() {
        MockKAnnotations.init(this)
    }

    @Test
    fun filesystemTimeSeriesAreUpToDate() {
        // given
        val cvmCode = "XX"
        val stockCode = "YY"

        val date1 = BalanceTrimesterInformation(1L, cvmCode, 2020, 1, null, null, null)
        val date2 = BalanceTrimesterInformation(1L, cvmCode, 2020, 2, null, null, null)
        val sequence = listOf(date1, date2)

        every { quotesPersistence.getFilesystemTimeSeries(cvmCode, stockCode) } returns "xoxo"
        every { quotesHelper.filesystemQuotesAreUpToDate("xoxo", sequence) } returns true

        // when
        val result = quotesTimeSeriesConverter.getTimeSeries(cvmCode, stockCode, sequence)

        // then
        assertEquals(result, "xoxo")
    }

    @Test
    fun filesystemTimeSeriesAreNotUpToDate() {
        // given
        val cvmCode = "XX"
        val stockCode = "YY"

        val date1 = BalanceTrimesterInformation(1L, cvmCode, 2020, 1, null, null, null)
        val date2 = BalanceTrimesterInformation(1L, cvmCode, 2020, 2, null, null, null)
        val sequence = listOf(date1, date2)

        every { quotesPersistence.getFilesystemTimeSeries(cvmCode, stockCode) } returns "xoxo"
        every { quotesHelper.filesystemQuotesAreUpToDate("xoxo", sequence) } returns false
        every { dateParser.asString(any()) } returns "2010-01-01"
        every { yahooQuotesRetriever.retrieve("YY.SA", "2000-01-01", "2010-01-01") } returns "xaxa"

        // when
        val result = quotesTimeSeriesConverter.getTimeSeries(cvmCode, stockCode, sequence)

        // then
        assertEquals(result, "xaxa")
    }

    @Test
    fun getPresentLastQuote() {
        // given
        val cvmCode = "AE"
        val stockCode = "TT"
        val timeseries = mapOf(
                getDate("2020-04-01") to createCandleWithGivenCloseValue(1F),
                getDate("2020-06-30") to createCandleWithGivenCloseValue(2F),
                getDate("2020-09-01") to createCandleWithGivenCloseValue(3F),
                getDate("2020-09-29") to createCandleWithGivenCloseValue(2.5F),
                getDate("2020-09-30") to createCandleWithGivenCloseValue(4F),
                getDate("2020-10-01") to createCandleWithGivenCloseValue(4.5F),
                getDate("2020-12-31") to createCandleWithGivenCloseValue(5F),
                getDate("2021-01-01") to createCandleWithGivenCloseValue(6F)
        )

        val expected = QuotesTrimesterInformation(cvmCode, stockCode, 2020, 3, 4F, 0F)

        // when
        val result = quotesTimeSeriesConverter.getLastQuote(
                cvmCode, stockCode, 2020, 3, timeseries)

        // then
        assertEquals(result, expected)
    }

    private fun createCandleWithGivenCloseValue(fl: Float): Candle {
        return Candle(LocalDate.now(), 0F, 0F, 0F, fl, 0F, 0L)
    }

    @Test
    fun getAbsentLastQuote() {
        // given
        val cvmCode = "AE"
        val stockCode = "TT"
        val timeseries = mapOf(
                getDate("2020-04-01") to createCandleWithGivenCloseValue(1F), // 2020-04-01
                getDate("2020-06-30") to createCandleWithGivenCloseValue(2F), // 2020-06-30 * (date2)
                getDate("2020-09-01") to createCandleWithGivenCloseValue(3F), // 2020-07-01
                getDate("2020-09-30") to createCandleWithGivenCloseValue(4F), // 2020-09-01 * (date3)
                getDate("2020-12-31") to createCandleWithGivenCloseValue(5F), // 2020-12-31 * (date4)
                getDate("2021-04-25") to createCandleWithGivenCloseValue(6F)  // 2021-01-01
        )

        // when
        val result = quotesTimeSeriesConverter.getLastQuote(
                cvmCode, stockCode, 2004, 1, timeseries)

        // then
        assertNull(result)
    }

    @Test
    fun absentTimeSeries() {
        // given
        val cvmCode = "AE"
        val stockCode = "TT"
        val timeseries = mapOf<LocalDate, Candle>()

        // when
        val result = quotesTimeSeriesConverter.getLastQuote(
                cvmCode, stockCode, 2004, 1, timeseries)

        // then
        assertNull(result)
    }

    private fun getDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)
                ?: throw IllegalStateException("could not parse date")
    }

}