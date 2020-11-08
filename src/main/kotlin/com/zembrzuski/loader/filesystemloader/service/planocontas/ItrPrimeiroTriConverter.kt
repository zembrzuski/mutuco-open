package com.zembrzuski.loader.filesystemloader.service.planocontas

import com.zembrzuski.loader.filesystemloader.domain.bovespa.InfoFinaDFin
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Conta
import com.zembrzuski.loader.filesystemloader.domain.mydomain.ContasEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.TipoContaEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.TipoDemonstrativoEnum
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * Converte contas para itrs do trimestre 1.
 * Para itrs dos trimestres 2, 3 e 4, veja ItrOutrosTriConverter
 */
@Component
class ItrPrimeiroTriConverter: ConverterInterface {

    override fun InfoFinaDFin.toConta(
            data: LocalDate, numeroDocumento: Long, conta: ContasEnum,
            tipoDemonstrativoEnum: TipoDemonstrativoEnum): List<Conta> {

        return when (conta.tipoConta) {
            TipoContaEnum.BALANCO -> balanco(conta, data, this, numeroDocumento, tipoDemonstrativoEnum)
            TipoContaEnum.DEMONSTRATIVO -> demonstrativo(conta, data, this, numeroDocumento, tipoDemonstrativoEnum)
        }

    }

    fun balanco(conta: ContasEnum, data: LocalDate, infoFinaDFin: InfoFinaDFin, numeroDocumento: Long, tipoDemonstrativo: TipoDemonstrativoEnum): List<Conta> {
        val conta2 = Conta(conta, data.monthValue/3, data.year-0, infoFinaDFin.valorConta2, numeroDocumento, tipoDemonstrativo)
        val conta3 = Conta(conta, 4, data.year-1, infoFinaDFin.valorConta3, numeroDocumento, tipoDemonstrativo)

        return listOf(conta2, conta3)
    }

    private fun demonstrativo(conta: ContasEnum, data: LocalDate, infoFinaDFin: InfoFinaDFin, numeroDocumento: Long, tipoDemonstrativo: TipoDemonstrativoEnum): List<Conta> {
        val conta4 = Conta(conta, data.monthValue/3, data.year-0, infoFinaDFin.valorConta4, numeroDocumento, tipoDemonstrativo)
        val conta6 = Conta(conta, data.monthValue/3, data.year-1, infoFinaDFin.valorConta6, numeroDocumento, tipoDemonstrativo)

        return listOf(conta4, conta6)
    }

}
