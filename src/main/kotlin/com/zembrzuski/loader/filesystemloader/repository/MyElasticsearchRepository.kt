package com.zembrzuski.loader.filesystemloader.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.zembrzuski.loader.filesystemloader.domain.mydomain.ContaAjustada
import com.zembrzuski.loader.filesystemloader.domain.mydomain.ContaEntity
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MyElasticsearchRepository @Autowired constructor(
        val restHighLevelClient: RestHighLevelClient,
        val objectMapper: ObjectMapper) {

    fun persist(contasAjustadas: List<ContaAjustada>): Boolean {
        contasAjustadas.forEach { conta ->
            val id = "${conta.conta.name}-${conta.year}-${conta.trimester}-${conta.codigoCvm}"
            val indexRequest = IndexRequest("conta", "conta", id)

            val request = indexRequest.source(conta.toPersist(), XContentType.JSON)
            restHighLevelClient.index(request, RequestOptions.DEFAULT)
        }

        return true
    }

    fun ContaAjustada.toPersist(): String {
        val conta = ContaEntity(conta.contaName, trimester, year, value, codigoCvm, valorDerivado, valorExato)

        return objectMapper.writeValueAsString(conta)
    }

}
