package com.zembrzuski.loader.filesystemloader.service.quotes

import com.zembrzuski.loader.filesystemloader.domain.mydomain.Candle
import com.zembrzuski.loader.filesystemloader.util.DateParser
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class YahooQuotesParserTest {

    @MockK lateinit var dateParser: DateParser
    @InjectMockKs lateinit var yahooQuotesParser: YahooQuotesParser

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun parse() {
        // given
        val csvInput =
                "Date,Open,High,Low,Close,Adj Close,Volume\n" +
                "2018-01-02,16.190001,16.549999,16.190001,16.549999,15.670517,33461800\n" +
                "2011-03-24,null,null,null,null,null,null\n" +
                "2018-12-28,22.110001,22.830000,22.080000,22.680000,22.402489,61634700\n"

        val janDate = LocalDate.parse("2018-01-02", DateTimeFormatter.ISO_DATE)
        val decDate = LocalDate.parse("2018-12-28", DateTimeFormatter.ISO_DATE)

        every { dateParser.parseIso("2018-01-02") } returns janDate
        every { dateParser.parseIso("2018-12-28") } returns decDate

        val expected = mapOf(
                janDate to Candle(janDate, 16.190001F, 16.549999F, 16.190001F, 16.549999F, 15.670517F, 33461800),
                decDate to Candle(decDate, 22.110001F, 22.830000F, 22.080000F, 22.680000F, 22.402489F, 61634700)
        )

        // when
        val response = yahooQuotesParser.parseResponse(csvInput)

        // then
        assertEquals(response, expected)
    }

}