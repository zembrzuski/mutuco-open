package com.zembrzuski.loader.filesystemloader.service

import com.google.cloud.Timestamp
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.Quote
import com.zembrzuski.loader.filesystemloader.domain.firebase.QuotesTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.service.balance.BalancesAndQuotesMapper
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDate

class BalancesAndQuotesMapperTest {

    @Test
    fun enrich() {
        // given
        val cvmCode = "454"

        val trimester1 = BalanceTrimesterInformation(1L, cvmCode, 2020, 1, 32F, 33F, null)
        val trimester2 = BalanceTrimesterInformation(1L, cvmCode, 2020, 2, 32F, 35F, null)

        val petr4Tri1 = QuotesTrimesterInformation(cvmCode, "PETR4", 2020, 1, 1F, 2F)

        val petr3Tri1 = QuotesTrimesterInformation(cvmCode, "PETR3", 2020, 1, 3F, 4F)

        val petr3Tri2 = QuotesTrimesterInformation(cvmCode, "PETR3", 2020, 2, 33F, 44F)

        val trimesterSequence = listOf(trimester1, trimester2)
        val quotes = listOf(petr4Tri1, petr3Tri1, petr3Tri2)

        val real1 = listOf(Quote("PETR4", 1F), Quote("PETR3", 3F))
        val adjusted1 = listOf(Quote("PETR4", 2F), Quote("PETR3", 4F))
        val real2 = listOf(Quote("PETR3", 33F))
        val adjusted2 = listOf(Quote("PETR3", 44F))

        val expected1 = TrimesterInformation(
                BalanceTrimesterInformation(1L, cvmCode, 2020, 1, 32F, 33F, null),
                real1,
                adjusted1
        )

        val expected2 = TrimesterInformation(
                BalanceTrimesterInformation(1L, cvmCode, 2020, 2, 32F, 35F, null),
                real2,
                adjusted2
        )

        val expected = listOf(expected1, expected2)

        // when
        val result = BalancesAndQuotesMapper().joinBalancesToQuotes(trimesterSequence, quotes)

        // then
        assertEquals(result, expected)
    }

}
