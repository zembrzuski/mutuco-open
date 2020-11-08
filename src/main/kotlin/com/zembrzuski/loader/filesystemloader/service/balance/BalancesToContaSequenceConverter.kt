package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.mydomain.Balanco
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Conta
import org.springframework.stereotype.Component

/**
 * Dada uma lista de balanços, cria sequencias temporais (trimestrais) de contas.
 */
@Component
class BalancesToContaSequenceConverter {

    fun getContasPorTrimestre(converted: List<Balanco>): List<Conta> {
        return converted
                .map { balance -> scaleAccountValue(balance) }
                .flatMap { balance -> balance.planoContas }
                .groupBy { Triple(it.conta, it.trimester, it.year) }
                .mapValues { reduceLastDocumentForGivenAccount(it.value) }
                .map { it.value }
                .sortedBy { it.trimester }
                .sortedBy { it.year }
                .sortedBy { it.conta.name }
    }

    fun scaleAccountValue(balanco: Balanco): Balanco {
        val multipliedPlanoContas = balanco.planoContas.map { conta ->
            conta.copy(value = conta.value * balanco.valueScale.multiplicationFactor)
        }

        return balanco.copy(
                planoContas = multipliedPlanoContas
        )
    }

    private fun reduceLastDocumentForGivenAccount(contas: List<Conta>): Conta {
        return contas.reduce { acc, conta ->
            return if (acc.numeroDocumento > conta.numeroDocumento) acc else conta
        }
    }

}
