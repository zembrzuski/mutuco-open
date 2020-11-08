package com.zembrzuski.loader.filesystemloader.service.quotes

import com.google.cloud.Timestamp
import com.google.common.collect.Maps
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Candle
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.Month


class QuotesHelperTest {

    @MockK lateinit var parser: YahooQuotesParser
    @InjectMockKs lateinit var quotesHelper: QuotesHelper

    @Before
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun yearAndTrimesterToLocalDateTests() {
        assertEquals(
                quotesHelper.yearAndTrimesterToLocalDate(2020, 1),
                LocalDate.of(2020, Month.MARCH, 31))

        assertEquals(
                quotesHelper.yearAndTrimesterToLocalDate(2020, 2),
                LocalDate.of(2020, Month.JUNE, 30))

        assertEquals(
                quotesHelper.yearAndTrimesterToLocalDate(2020, 3),
                LocalDate.of(2020, Month.SEPTEMBER, 30))

        assertEquals(
                quotesHelper.yearAndTrimesterToLocalDate(2020, 4),
                LocalDate.of(2020, Month.DECEMBER, 31))

    }

    @Test
    fun fsIsNotUpToDateForSameMonthFirstDay() {
        // given
        val filesystemInfo = mapOf(LocalDate.of(2020, Month.MARCH, 1) to candle32())
        val balanceSequence = listOf(createBalance(2020, 1))

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertFalse(result)
    }

    private fun candle32(): Candle {
        return Candle(
                LocalDate.now(),
                1F,
                2F,
                3F,
                32F,
                33F,
                1L
        )
    }

    @Test
    fun fsIsNotUpToDateForSameMonthDay30() {
        // given
        val filesystemInfo = mapOf(LocalDate.of(2020, Month.MARCH, 30) to candle32())
        val balanceSequence = listOf(createBalance(2020, 1))

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertFalse(result)
    }

    @Test
    fun fsIsUpToDateForSameMonthLastDay() {
        // given
        val filesystemInfo = mapOf(LocalDate.of(2020, Month.MARCH, 31) to candle32())
        val balanceSequence = listOf(createBalance(2020, 1))

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertTrue(result)
    }

    @Test
    fun fsIsNotUpToDateForSameMonthFirstDaySecondTrimester() {
        // given
        val filesystemInfo = mapOf(LocalDate.of(2020, Month.JUNE, 29) to candle32())
        val balanceSequence = listOf(createBalance(2020, 2))

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertFalse(result)
    }

    @Test
    fun fsIsUpToDateForSameMonthLastDaySecondTrimester() {
        // given
        val filesystemInfo = mapOf(LocalDate.of(2020, Month.JUNE, 30) to candle32())
        val balanceSequence = listOf(createBalance(2020, 2))

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertTrue(result)
    }

    @Test
    fun fsIsNotUpToDateForSameMonthFirstDayThirdTrimester() {
        // given
        val filesystemInfo = mapOf(LocalDate.of(2020, Month.SEPTEMBER, 29) to candle32())
        val balanceSequence = listOf(createBalance(2020, 3))

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertFalse(result)
    }

    @Test
    fun fsIsUpToDateForSameMonthLastDayThirdTrimester() {
        // given
        val filesystemInfo = mapOf(LocalDate.of(2020, Month.SEPTEMBER, 30) to candle32())
        val balanceSequence = listOf(createBalance(2020, 3))

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertTrue(result)
    }

    @Test
    fun fsIsNotUpToDateForSameMonthFirstDayFourthTrimester() {
        // given
        val filesystemInfo = mapOf(LocalDate.of(2020, Month.DECEMBER, 30) to candle32())
        val balanceSequence = listOf(createBalance(2020, 4))

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertFalse(result)
    }

    @Test
    fun fsIsUpToDateForSameMonthLastDayFourthTrimester() {
        // given
        val filesystemInfo = mapOf(LocalDate.of(2020, Month.DECEMBER, 31) to candle32())
        val balanceSequence = listOf(createBalance(2020, 4))

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertTrue(result)
    }

    @Test
    fun fsIsUpToDate() {
        // given
        val filesystemInfo = mapOf(LocalDate.of(2030, Month.DECEMBER, 31) to candle32())
        val balanceSequence = listOf(createBalance(2021, 1))

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertTrue(result)
    }

    @Test
    fun fsIsUpToDateManyInformation() {
        // given
        val filesystemInfo = mapOf(
                LocalDate.of(2020, Month.JUNE, 30) to candle32(),
                LocalDate.of(2004, Month.DECEMBER, 31) to candle32())

        val balanceSequence = listOf(
                createBalance(2020, 1),
                createBalance(2020, 2)
        )

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertTrue(result)
    }

    @Test
    fun fsIsNotUpToDateManyInformation() {
        // given
        val filesystemInfo = mapOf(
                LocalDate.of(2004, Month.DECEMBER, 31) to candle32(),
                LocalDate.of(2020, Month.JUNE, 29) to candle32())

        val balanceSequence = listOf(
                createBalance(2020, 2),
                createBalance(2020, 1)
        )

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, balanceSequence)

        // then
        assertFalse(result)
    }

    @Test
    fun exceptionForEmptyQuotes() {
        // given
        val balanceSequence = listOf(
                createBalance(2020, 2),
                createBalance(2020, 1)
        )

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(emptyMap(), balanceSequence)

        // then
        assertFalse(result)
    }

    @Test
    fun falseForEmptyBalances() {
        // given
        val filesystemInfo = mapOf(
                LocalDate.of(2004, Month.DECEMBER, 31) to candle32(),
                LocalDate.of(2020, Month.JUNE, 29) to candle32())

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(filesystemInfo, emptyList())

        // then
        assertFalse(result)
    }


    @Test
    fun getTimeSeriesDelegator() {
        // given
        val quotesJson = "fakeJson"
        val pair: List<Pair<LocalDate, Candle>> = listOf(Pair(LocalDate.of(1, 2, 3), candle32()))
        val expected = pair.toMap()
        every { parser.parseResponse(quotesJson) } returns expected

        // when
        val result = quotesHelper.getTimeSeries(quotesJson)

        // then
        assertEquals(result, expected)
    }

    @Test
    fun getEmptyTimeSeries() {
        // given, when
        val result = quotesHelper.getTimeSeries(null)

        // then
        assertEquals(result, Maps.newHashMap<LocalDate, Float>())
    }

    @Test
    fun filesystemQuotesAreUpToDateJson() {
        // given
        val quotesJson = "fakeJson"
        val pair: List<Pair<LocalDate, Candle>> = listOf(Pair(LocalDate.of(2000, 1, 1), candle32()))
        val expected = pair.toMap()
        every { parser.parseResponse(quotesJson) } returns expected
        val balances = listOf(createBalance(1998, 1))

        // when
        val result = quotesHelper.filesystemQuotesAreUpToDate(quotesJson, balances)

        // then
        assertTrue(result)
    }

    private fun createBalance(year: Int, trimester: Int): BalanceTrimesterInformation {
        return BalanceTrimesterInformation(
                1L,
                "123",
                year,
                trimester,
                0F,
                0F,
                null)
    }

}
