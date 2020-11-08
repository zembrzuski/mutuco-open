package com.zembrzuski.loader.filesystemloader.util

import com.google.cloud.Timestamp
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.time.LocalDate
import java.time.Month

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = [DateParser::class])
class DateParserTest {

    @Autowired
    private lateinit var dateParser: DateParser

    @Test
    fun parseTimestampTest() {
        /**
         * Date and time (GMT): Sunday, October 11, 2020 0:00:00
         * Date and time (your time zone): Saturday, October 10, 2020 21:00:00 GMT-03:00
         */
        val result = dateParser.parseTimestamp("2020-10-11")
        val expected = Timestamp.ofTimeMicroseconds(1602374400000000L)
        assertEquals(result, expected)
    }

    @Test
    fun parseTest() {
        val result = dateParser.parse("2020-10-11")

        assertEquals(2020, result.year)
        assertEquals(Month.OCTOBER, result.month)
        assertEquals(11, result.dayOfMonth)
    }

    @Test
    fun asStringTest() {
        val result = dateParser.asString(LocalDate.of(2020, Month.OCTOBER, 11))
        assertEquals(result, "2020-10-11")
    }
}