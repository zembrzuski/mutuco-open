package com.zembrzuski.loader.filesystemloader.service

import com.zembrzuski.loader.filesystemloader.domain.mydomain.AnnualSum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Conta
import com.zembrzuski.loader.filesystemloader.domain.mydomain.ContasEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.TipoContaEnum
import org.springframework.stereotype.Component

/**
 * Pega as contas do tipo Demonstrativo (que representam um periodo) e faz a soma anual
 * (de cada um dos trimestres) para aquela conta
 */
@Component
class AnnualSumService {

    fun getValoresAnuaisSomados(contasPorTrimestre: List<Conta>): List<Triple<ContasEnum, AnnualSum, Int>> {
        return contasPorTrimestre
                .filter { it.conta.tipoConta == TipoContaEnum.DEMONSTRATIVO }
                .groupBy { Pair(it.conta, it.year) }
                .map { computaValoresAnuais(it) }
    }

    private fun computaValoresAnuais(it: Map.Entry<Pair<ContasEnum, Int>, List<Conta>>): Triple<ContasEnum, AnnualSum, Int> {
        val somaAno = it.value
                .reduce { acc, conta ->
                    Conta(acc.conta,
                            -1,
                            acc.year,
                            acc.value+conta.value,
                            -1,
                            acc.tipoDemonstrativo
                    )
                }
                .toAnnualSum()

        return Triple(it.value.first().conta, somaAno, it.value.size)
    }

    fun Conta.toAnnualSum(): AnnualSum {
        return AnnualSum(conta, year, value, tipoDemonstrativo)
    }

}
