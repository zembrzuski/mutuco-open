package com.zembrzuski.loader.filesystemloader.service.quotes

import com.google.cloud.Timestamp
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.QuotesTrimesterInformation
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class QuotesImporterServiceTest {

    @MockK lateinit var quotesTrimesterImporter: QuotesTrimesterImporter
    @InjectMockKs lateinit var quotesImporterService: QuotesImporterService

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun importAll() {
        // given
        val cvmCode = "123"
        val stockCodes = listOf("petr3", "petr4")
        val date1 = BalanceTrimesterInformation(1L, cvmCode, 2020, 1, null, null, null)
        val date2 = BalanceTrimesterInformation(1L, cvmCode, 2020, 2, null, null, null)

        val sequence = listOf(date1, date2)

        val petr320201Monthly = QuotesTrimesterInformation(cvmCode, "petr3", 2020, 1, 11F, 33F)
        val petr320202Monthly = QuotesTrimesterInformation(cvmCode, "petr3", 2020, 2, 22F, 44F)
        val petr420202Monthly = QuotesTrimesterInformation(cvmCode, "petr4", 2020, 2, 55F, 66F)

        val petr3monthly = listOf(petr320201Monthly, petr320202Monthly)
        val petr4monthly = listOf(petr420202Monthly)

        every { quotesTrimesterImporter.import(cvmCode, "petr3", sequence) } returns petr3monthly
        every { quotesTrimesterImporter.import(cvmCode, "petr4", sequence) } returns petr4monthly

        val expected = listOf(petr320201Monthly, petr320202Monthly, petr420202Monthly)

        // when
        val result = quotesImporterService.importAll(cvmCode, stockCodes, sequence)

        // then
        Assert.assertEquals(result, expected)
    }

}
