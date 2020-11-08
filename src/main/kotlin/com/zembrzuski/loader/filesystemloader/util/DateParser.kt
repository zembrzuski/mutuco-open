package com.zembrzuski.loader.filesystemloader.util

import com.google.cloud.Timestamp
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.annotation.PostConstruct

@Component
class DateParser {

    private val sdf = SimpleDateFormat("yyyy-MM-dd")

    @PostConstruct
    fun init() {
        sdf.timeZone = TimeZone.getTimeZone("GMT")
    }

    fun parse(date: String): LocalDate {
        val yyyyMMdd = date.split('T')[0]

        return parseIso(yyyyMMdd)
    }

    fun parseIso(dateString: String): LocalDate {
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE)
    }

    fun asString(localDate: LocalDate): String {
        return localDate.format(DateTimeFormatter.ISO_DATE)
    }

    fun parseTimestamp(date: String): Timestamp {
        return Timestamp.of(sdf.parse(date))
    }

}
