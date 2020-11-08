package com.zembrzuski.loader.filesystemloader.service.quotes

import com.zembrzuski.loader.filesystemloader.domain.mydomain.Candle
import com.zembrzuski.loader.filesystemloader.util.DateParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * Faz parse das cotações que retornaram da Yahoo.
 */
@Component
class YahooQuotesParser @Autowired constructor(private val dateParser: DateParser) {

    fun parseResponse(csv: String): Map<LocalDate, Candle> {
        return csv
                .split("\n")
                .filter { !it.startsWith("Date")  && it != "" && ! it.contains("null")}
                .map { lineMapper(it) }
                .map { Pair(it.date, it) }
                .toMap()
    }

    private fun lineMapper(it: String): Candle {
        val splittedLine = it.replace("\n", "").split(",")

        return Candle(
                dateParser.parseIso(splittedLine[0]),
                splittedLine[1].toFloat(),
                splittedLine[2].toFloat(),
                splittedLine[3].toFloat(),
                splittedLine[4].toFloat(),
                splittedLine[5].toFloat(),
                splittedLine[6].toLong()
        )
    }

}
