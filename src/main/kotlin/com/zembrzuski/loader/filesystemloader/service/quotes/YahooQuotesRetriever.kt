package com.zembrzuski.loader.filesystemloader.service.quotes

import com.zembrzuski.loader.filesystemloader.domain.mydomain.HttpResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class YahooQuotesRetriever @Autowired constructor(
        private val yahooQuotesRetrieverHelper: YahooQuotesRetrieverHelper) {

    /**
     * Retorna, para um determinado papel, suas cotações em forma de texto.
     * Se não existem cotações para esse papel, retorna null.
     */
    fun retrieve(codeStock: String, startDate: String, endDate: String): String? {
        val start = yahooQuotesRetrieverHelper.epochConverter(startDate)
        val end = yahooQuotesRetrieverHelper.epochConverter(endDate)

        for (i in 0 until 5) {
            try {
                val response = doRequests(codeStock, start, end)

                if (response?.contains("ookie") == true) {
                    throw java.lang.IllegalStateException("Invalid cookie")
                }

                return response
            } catch (e: java.lang.IllegalStateException) { }
        }

        throw IllegalStateException("tentei muitas vezes mas nao consegui")
    }


    fun doRequests(codeStock: String, start: Long, end: Long): String? {
        val firstResponse = yahooQuotesRetrieverHelper.doFirstRequest(codeStock, start, end)

        return if (yahooQuotesRetrieverHelper.hasQuotes(firstResponse.text)) {
            secondPart(firstResponse, codeStock, start, end)
        } else {
            null
        }
    }

    fun secondPart(firstResponse: HttpResponse, codeStock: String, start: Long, end: Long): String {
        val crumb = yahooQuotesRetrieverHelper.getYahooCrumb(firstResponse.text)

        val bCookie = firstResponse.cookies["B"]
                ?: throw IllegalStateException("Could not find cookie to retrieve quotes on yahoo")

        val theCookieParam = bCookie.split(";")[0]

        val secondResponse =
                yahooQuotesRetrieverHelper.doSecondRequest(codeStock, start, end, crumb, theCookieParam)

        return secondResponse.text
    }

}
