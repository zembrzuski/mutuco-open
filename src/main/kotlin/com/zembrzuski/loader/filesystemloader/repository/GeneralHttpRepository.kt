package com.zembrzuski.loader.filesystemloader.repository

import com.zembrzuski.loader.filesystemloader.domain.mydomain.HttpResponse
import org.springframework.stereotype.Component
import java.lang.IllegalStateException

/**
 * Classe para fazer chamadas http.
 *
 * TODO Essa classe poderia ser muito mais elegante. Refatorar em caso de necessidade.
 * Sugestões:
 *   - Usar spring retry em vez de de for.
 *   - Injetar esse khttp para tornar a classe testável.
 */
@Component
class GeneralHttpRepository {

    fun doRequest(url: String, headers: Map<String, String> = emptyMap()): HttpResponse {
        for (i in 0 until 3) {
            try {
                val response = khttp.get(url, headers)

                val cookiesMapped = response.cookies
                        .map { Pair(it.key, it.value) }
                        .toMap()

                return HttpResponse(response.text, cookiesMapped)
            } catch (e: Exception) {
                Thread.sleep(1_000L)
            }
        }

        throw IllegalStateException("Could not retrieve information on url: $url")
    }

}
