package com.zembrzuski.loader.filesystemloader.service.quotes

import com.google.cloud.Timestamp
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.QuotesTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Candle
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class QuotesTrimesterImporterTest {

    @MockK lateinit var quotesPersistence: QuotesPersistence
    @MockK lateinit var quotesHelper: QuotesHelper
    @MockK lateinit var quotesTimeSeriesConverter: QuotesTimeSeriesConverter
    @InjectMockKs lateinit var quotesTrimesterImporter: QuotesTrimesterImporter

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun filledTimeSeries() {
        // given
        val cvmCode = "CVM"
        val stockCode = "STOCK"

        val date1 = BalanceTrimesterInformation(1L, cvmCode, 2020, 1, null, null, null)
        val date2 = BalanceTrimesterInformation(1L, cvmCode, 2020, 2, null, null, null)
        val date3 = BalanceTrimesterInformation(1L, cvmCode, 2020, 3, null, null, null)
        val date4 = BalanceTrimesterInformation(1L, cvmCode, 2020, 4, null, null, null)

        val sequence = listOf(date1, date2, date3, date4)

        val timeSeries = mapOf(
                getDate("2020-04-01") to createCandleWithGivenCloseValue(1F), // 2020-04-01
                getDate("2020-06-30") to createCandleWithGivenCloseValue(2F), // 2020-06-30 * (date2)
                getDate("2020-09-01") to createCandleWithGivenCloseValue(3F), // 2020-07-01
                getDate("2020-09-30") to createCandleWithGivenCloseValue(4F), // 2020-09-01 * (date3)
                getDate("2020-12-31") to createCandleWithGivenCloseValue(5F), // 2020-12-31 * (date4)
                getDate("2021-01-01") to createCandleWithGivenCloseValue(6F)  // 2021-01-01
        )

        val secodTrimesterInformation = QuotesTrimesterInformation(cvmCode, stockCode, 2020, 2, 2F, 0F)
        val thirdTrimesterInformation = QuotesTrimesterInformation(cvmCode, stockCode, 2020, 3, 4F, 0F)
        val fourthTrimesterInformation = QuotesTrimesterInformation(cvmCode, stockCode, 2020, 4, 5F, 0F)

        val expected = listOf(secodTrimesterInformation, thirdTrimesterInformation, fourthTrimesterInformation)

        every { quotesTimeSeriesConverter.getTimeSeries(cvmCode, stockCode, sequence) } returns "json"
        every { quotesPersistence.persist(cvmCode, stockCode, "json") } returns true
        every { quotesHelper.getTimeSeries("json") } returns timeSeries
        every { quotesTimeSeriesConverter.getLastQuote(cvmCode, stockCode, 2020, 1, timeSeries) } returns null
        every { quotesTimeSeriesConverter.getLastQuote(cvmCode, stockCode, 2020, 2, timeSeries) } returns secodTrimesterInformation
        every { quotesTimeSeriesConverter.getLastQuote(cvmCode, stockCode, 2020, 3, timeSeries) } returns thirdTrimesterInformation
        every { quotesTimeSeriesConverter.getLastQuote(cvmCode, stockCode, 2020, 4, timeSeries) } returns fourthTrimesterInformation

        // when
        val result = quotesTrimesterImporter.import(cvmCode, stockCode, sequence)

        // then
        assertEquals(result, expected)
    }

    @Test
    fun emptyTimeSeries() {
        // given
        val cvmCode = "CVM"
        val stockCode = "STOCK"

        val date1 = BalanceTrimesterInformation(1L, cvmCode, 2020, 1, null, null, null)
        val date2 = BalanceTrimesterInformation(1L, cvmCode, 2020, 2, null, null, null)
        val date3 = BalanceTrimesterInformation(1L, cvmCode, 2020, 3, null, null, null)
        val date4 = BalanceTrimesterInformation(1L, cvmCode, 2020, 4, null, null, null)

        val sequence = listOf(date1, date2, date3, date4)

        every { quotesTimeSeriesConverter.getTimeSeries(cvmCode, stockCode, sequence) } returns null
        every { quotesHelper.getTimeSeries(null) } returns emptyMap()
        every { quotesTimeSeriesConverter.getLastQuote(cvmCode, stockCode, 2020, 1, emptyMap()) } returns null
        every { quotesTimeSeriesConverter.getLastQuote(cvmCode, stockCode, 2020, 2, emptyMap()) } returns null
        every { quotesTimeSeriesConverter.getLastQuote(cvmCode, stockCode, 2020, 3, emptyMap()) } returns null
        every { quotesTimeSeriesConverter.getLastQuote(cvmCode, stockCode, 2020, 4, emptyMap()) } returns null

        // when
        val result = quotesTrimesterImporter.import(cvmCode, stockCode, sequence)

        // then
        assertEquals(result, listOf<QuotesTrimesterInformation>())
    }

    private fun getDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)
                ?: throw IllegalStateException("could not parse date")
    }

    private fun createCandleWithGivenCloseValue(fl: Float): Candle {
        return Candle(LocalDate.now(), 0F, 0F, 0F, fl, 0F, 0L)
    }

}
