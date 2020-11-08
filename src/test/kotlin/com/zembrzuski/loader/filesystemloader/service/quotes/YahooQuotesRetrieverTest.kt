package com.zembrzuski.loader.filesystemloader.service.quotes

import com.zembrzuski.loader.filesystemloader.domain.mydomain.HttpResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class YahooQuotesRetrieverTest {

    @MockK lateinit var yahooQuotesRetrieverHelper: YahooQuotesRetrieverHelper
    @InjectMockKs lateinit var yahooQuotesRetriever: YahooQuotesRetriever

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun retrieveSuccess() {
        // given
        val stock = "STK"
        val start = "2001-01-01"
        val end = "2002-02-02"

        val startLong = 45L
        val endLong = 46L

        val firstResponse = HttpResponse("firstText", mapOf("B" to "BBB"))
        val secondResponse = HttpResponse("asjdfakjsd", emptyMap())

        every { yahooQuotesRetrieverHelper.epochConverter(start) } returns startLong
        every { yahooQuotesRetrieverHelper.epochConverter(end) } returns endLong
        every { yahooQuotesRetrieverHelper.doFirstRequest(stock, startLong, endLong) } returns firstResponse
        every { yahooQuotesRetrieverHelper.getYahooCrumb(firstResponse.text) } returns "crumb"
        every { yahooQuotesRetrieverHelper.doSecondRequest(stock, startLong, endLong, "crumb", "BBB") } returns secondResponse
        every { yahooQuotesRetrieverHelper.hasQuotes(firstResponse.text) } returns true

        // when
        val retrieved = yahooQuotesRetriever.retrieve(stock, start, end)

        // then
        Assert.assertEquals(retrieved, secondResponse.text)
    }

    @Test
    fun nullForAbsentQuotes() {
        // given
        val stock = "STK"
        val start = "2001-01-01"
        val end = "2002-02-02"

        val startLong = 45L
        val endLong = 46L

        val firstResponse = HttpResponse("firstText", mapOf("B" to "BBB"))

        every { yahooQuotesRetrieverHelper.epochConverter(start) } returns startLong
        every { yahooQuotesRetrieverHelper.epochConverter(end) } returns endLong
        every { yahooQuotesRetrieverHelper.doFirstRequest(stock, startLong, endLong) } returns firstResponse
        every { yahooQuotesRetrieverHelper.hasQuotes(firstResponse.text) } returns false

        // when
        val retrieved = yahooQuotesRetriever.retrieve(stock, start, end)

        // then
        Assert.assertNull(retrieved)
    }

    @Test
    fun fourFailAndFifthWithSuccess() {
        // given
        val stock = "STK"
        val start = "2001-01-01"
        val end = "2002-02-02"

        val startLong = 45L
        val endLong = 46L

        val firstResponse = HttpResponse("firstText", mapOf("B" to "BBB"))
        val secondResponse = HttpResponse("asjdfakjsd", emptyMap())

        every { yahooQuotesRetrieverHelper.epochConverter(start) } returns startLong
        every { yahooQuotesRetrieverHelper.epochConverter(end) } returns endLong

        every { yahooQuotesRetrieverHelper.doFirstRequest(stock, startLong, endLong)
        } throws IllegalStateException("ae") andThenThrows IllegalStateException("ae") andThenThrows IllegalStateException("ae") andThenThrows IllegalStateException("ae") andThen firstResponse

        every { yahooQuotesRetrieverHelper.getYahooCrumb(firstResponse.text) } returns "crumb"
        every { yahooQuotesRetrieverHelper.doSecondRequest(stock, startLong, endLong, "crumb", "BBB") } returns secondResponse
        every { yahooQuotesRetrieverHelper.hasQuotes(firstResponse.text) } returns true

        // when
        val retrieved = yahooQuotesRetriever.retrieve(stock, start, end)

        // then
        Assert.assertEquals(retrieved, secondResponse.text)
    }

    @Test(expected = IllegalStateException::class)
    fun fiveFail() {
        // given
        val stock = "STK"
        val start = "2001-01-01"
        val end = "2002-02-02"

        val startLong = 45L
        val endLong = 46L

        val firstResponse = HttpResponse("firstText", mapOf("B" to "BBB"))
        val secondResponse = HttpResponse("asjdfakjsd", emptyMap())

        every { yahooQuotesRetrieverHelper.epochConverter(start) } returns startLong
        every { yahooQuotesRetrieverHelper.epochConverter(end) } returns endLong

        every { yahooQuotesRetrieverHelper.doFirstRequest(stock, startLong, endLong)
        } throws IllegalStateException("ae") andThenThrows IllegalStateException("ae") andThenThrows IllegalStateException("ae") andThenThrows IllegalStateException("ae") andThenThrows IllegalStateException("ae")

        every { yahooQuotesRetrieverHelper.getYahooCrumb(firstResponse.text) } returns "crumb"
        every { yahooQuotesRetrieverHelper.doSecondRequest(stock, startLong, endLong, "crumb", "BBB") } returns secondResponse
        every { yahooQuotesRetrieverHelper.hasQuotes(firstResponse.text) } returns true

        // when
        yahooQuotesRetriever.retrieve(stock, start, end)

        // then
        // exception
    }

    @Test(expected = IllegalStateException::class)
    fun retrieveNoCookie() {
        // given
        val stock = "STK"
        val start = "2001-01-01"
        val end = "2002-02-02"

        val startLong = 45L
        val endLong = 46L

        val firstResponse = HttpResponse("firstText", emptyMap())

        every { yahooQuotesRetrieverHelper.epochConverter(start) } returns startLong
        every { yahooQuotesRetrieverHelper.epochConverter(end) } returns endLong
        every { yahooQuotesRetrieverHelper.doFirstRequest(stock, startLong, endLong) } returns firstResponse
        every { yahooQuotesRetrieverHelper.getYahooCrumb(firstResponse.text) } returns "crumb"
        every { yahooQuotesRetrieverHelper.hasQuotes(firstResponse.text) } returns true

        // when
        yahooQuotesRetriever.retrieve(stock, start, end)

        // then
        // exception
    }

}
