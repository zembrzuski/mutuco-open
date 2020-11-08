package com.zembrzuski.loader.filesystemloader.service.quotes

import com.zembrzuski.loader.filesystemloader.domain.mydomain.HttpResponse
import com.zembrzuski.loader.filesystemloader.repository.GeneralHttpRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Component
class YahooQuotesRetrieverHelper @Autowired constructor(private val httpRepository: GeneralHttpRepository){

    private val yahooCrumbRegex = """"CrumbStore":\{"crumb":"(.+)?"},"StreamStore"""".toRegex()
    private val utcId = ZoneId.of("UTC")

    /**
     * Verifica, para um determinado símbolo, se o Yahoo possui cotações para esse símbolo.
     */
    fun hasQuotes(text: String): Boolean {
        val result = yahooCrumbRegex.findAll(text)

        return result.iterator().hasNext()
    }

    /**
     * Extrai um atributo chamado crumb do Yahoo. Utilizado para requisições posteriores.
     */
    fun getYahooCrumb(text: String): String {
        val result = yahooCrumbRegex.findAll(text)

        val next = result.iterator().next()
        return next.groups[1]?.value ?: throw IllegalStateException("Could not extract crumb")
    }

    /**
     * Converte uma data para uma epoch. Utilizado para requisições de cotações posteriormente.
     */
    fun epochConverter(dateInput: String): Long {
        val localDate = LocalDate. parse(dateInput, DateTimeFormatter. ISO_DATE) ?:
                throw IllegalArgumentException("Could not parse dateInput: $dateInput")

        return localDate.atStartOfDay(utcId).toEpochSecond()
    }

    /**
     * Primeira requisição serve apenas para pegar o crumb, necessário para requisições posteriores
     */
    fun doFirstRequest(quote: String, periodStart: Long, periodEnd: Long): HttpResponse {
        val requestUrl = "https://finance.yahoo.com/quote/$quote/history" +
                "?period1=$periodStart" +
                "&period2=$periodEnd" +
                "&interval=1d&filter=history&frequency=1d"

        return httpRepository.doRequest(requestUrl)
    }

    /**
     * Segunda requisicaoo: é a que realmente pega as cotações.
     */
    fun doSecondRequest(quote: String, periodStart: Long, periodEnd: Long, crumb: String, bCookie: String): HttpResponse {
        val requestUrl = "https://query1.finance.yahoo.com/v7/finance/download/$quote" +
                "?period1=$periodStart" +
                "&period2=$periodEnd" +
                "&interval=1d" +
                "&events=history" +
                "&crumb=$crumb"

        val headers = mapOf(
                "authority" to "query1.finance.yahoo.com",
                "user-agent" to "Mozilla/5.0 (X11; Linux x86_64) AppleWeb	Kit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36",
                "accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
                "accept-encoding" to "gzip, deflate, br",
                "accept-language" to "en-US,en;q=0.9'",
                "cookie" to "B=\"$bCookie\""
        )

        return httpRepository.doRequest(requestUrl, headers)
    }

}
