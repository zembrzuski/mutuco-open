package com.zembrzuski.loader.filesystemloader.service.quotes

import com.zembrzuski.loader.filesystemloader.domain.mydomain.HttpResponse
import com.zembrzuski.loader.filesystemloader.repository.GeneralHttpRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class YahooQuotesRetrieverHelperTest {

    @MockK lateinit var generalHttpRepository: GeneralHttpRepository
    @InjectMockKs lateinit var yahooRetrieverHelper: YahooQuotesRetrieverHelper

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun extractYahooBreadCrumb() {
        // given
        val input = "}},\"CrumbStore\":{\"crumb\":\"Ix5wrXlRGkP\"},\"StreamStore\":{\"ar"

        // when
        val result = yahooRetrieverHelper.getYahooCrumb(input)

        // then
        assertEquals(result, "Ix5wrXlRGkP")
    }

    @Test(expected = NoSuchElementException::class)
    fun failExtractingYahooBreadCrumb() {
        // given
        val input = "nada a ver esse input"

        // when
        yahooRetrieverHelper.getYahooCrumb(input)

        // then
        // exception
    }

    @Test
    fun convertEpoch() {
        // given
        val date = "2000-01-01"

        // when
        val result = yahooRetrieverHelper.epochConverter(date)

        // then
        assertEquals(result, 946684800L)
    }

    @Test
    fun convertEpoch2() {
        // given
        val date = "2020-07-17"

        // when
        val result = yahooRetrieverHelper.epochConverter(date)

        // then
        assertEquals(result, 1594944000L)
    }

    @Test(expected = java.time.format.DateTimeParseException::class)
    fun convertEpochWithInvalidDate() {
        // given
        val date = "2020-07-33"

        // when
        yahooRetrieverHelper.epochConverter(date)

        // then
        // exception
    }

    @Test
    fun firstRequest() {
        // given
        val start = yahooRetrieverHelper.epochConverter("2005-01-01")
        val end = yahooRetrieverHelper.epochConverter("2009-03-03")
        val url = "https://finance.yahoo.com/quote/PETR4.SA/history" +
                "?period1=1104537600" +
                "&period2=1236038400" +
                "&interval=1d&filter=history&frequency=1d"

        val response = HttpResponse("gremio campeao", mapOf("cookieA" to "valueA"))

        every { generalHttpRepository.doRequest(url)
        } returns response

        // when
        val result = yahooRetrieverHelper.doFirstRequest("PETR4.SA", start, end)

        assertEquals(result, response)
    }

    @Test
    fun secondRequest() {
        // given
        val start = yahooRetrieverHelper.epochConverter("2005-01-01")
        val end = yahooRetrieverHelper.epochConverter("2009-03-03")
        val url = "https://query1.finance.yahoo.com/v7/finance/download/PETR4.SA" +
                "?period1=1104537600" +
                "&period2=1236038400" +
                "&interval=1d" +
                "&events=history" +
                "&crumb=crumb"

        val response = HttpResponse("gremio campeao", mapOf("cookieA" to "valueA"))

        val headers = mapOf(
                "authority" to "query1.finance.yahoo.com",
                "user-agent" to "Mozilla/5.0 (X11; Linux x86_64) AppleWeb	Kit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36",
                "accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
                "accept-encoding" to "gzip, deflate, br",
                "accept-language" to "en-US,en;q=0.9'",
                "cookie" to "B=\"cookie\""
        )

        every { generalHttpRepository.doRequest(url, headers) } returns response

        // when
        val result = yahooRetrieverHelper.doSecondRequest("PETR4.SA", start, end, "crumb", "cookie")

        assertEquals(result, response)
    }

    @Test
    fun hasQuotes() {
        // given
        val input = "}},\"CrumbStore\":{\"crumb\":\"Ix5wrXlRGkP\"},\"StreamStore\":{\"ar"

        // when
        val result = yahooRetrieverHelper.hasQuotes(input)

        // then
        assertTrue(result)
    }

    @Test
    fun dontHaveQuotes() {
        // given
        val input = "outracoisa"

        // when
        val result = yahooRetrieverHelper.hasQuotes(input)

        // then
        assertFalse(result)
    }

}
