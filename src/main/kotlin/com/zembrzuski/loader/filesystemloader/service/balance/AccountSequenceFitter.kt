package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.mydomain.*
import com.zembrzuski.loader.filesystemloader.service.AnnualSumService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Ajusta os valores de contas do tipo DEMONSTRATIVO.
 *
 * Vejamos o lucro líquido, por exemplo: nos its, ele exibe o lucro naquele
 * trimestre. No DFP, ele exibe o lucro do ano.
 *
 * Nessa classe, então, ele vai computar o lucro do quarto trimestre individualmente.
 *
 * TODO essa classe podia ser bem melhor, acho.
 */
@Component
class AccountSequenceFitter @Autowired constructor (val annualSumService: AnnualSumService){

    fun fit(contasPorTrimestre: List<Conta>, codigoCvm: String): List<ContaAjustada> {

        val annualSum = annualSumService.getValoresAnuaisSomados(contasPorTrimestre)
        return contasPorTrimestre.map { adjustConta(it, annualSum, codigoCvm) }
    }

    private fun adjustConta(
            conta: Conta, annualSum: List<Triple<ContasEnum, AnnualSum, Int>>, codigoCvm: String): ContaAjustada {

        return if (conta.tipoDemonstrativo == TipoDemonstrativoEnum.DFP
                && conta.conta.tipoConta == TipoContaEnum.DEMONSTRATIVO) {

            doAdjust(conta, annualSum, codigoCvm)
        } else {
            ContaAjustada(
                    conta = conta.conta,
                    trimester = conta.trimester,
                    year = conta.year,
                    value = conta.value,
                    valorDerivado = false,
                    valorExato = true,
                    codigoCvm = codigoCvm,
                    balanceId = conta.numeroDocumento
            )
        }
    }

    private fun doAdjust(conta: Conta, annualSums: List<Triple<ContasEnum, AnnualSum, Int>>, codigoCvm: String): ContaAjustada {

        val annualSum = annualSums
                .single { it.first == conta.conta && conta.year == it.second.year }

        if (annualSum.third == 1) {
            return ContaAjustada(
                    conta = conta.conta,
                    trimester = conta.trimester,
                    year = conta.year,
                    value = conta.value/4,
                    valorDerivado = true,
                    valorExato = false,
                    codigoCvm = codigoCvm,
                    balanceId = conta.numeroDocumento
            )
        } else {
            return ContaAjustada(
                    conta = conta.conta,
                    trimester = conta.trimester,
                    year = conta.year,
                    value = conta.value - (annualSum.second.value - conta.value),
                    valorDerivado = true,
                    valorExato = true,
                    codigoCvm = codigoCvm,
                    balanceId = conta.numeroDocumento
            )
        }

    }

}
