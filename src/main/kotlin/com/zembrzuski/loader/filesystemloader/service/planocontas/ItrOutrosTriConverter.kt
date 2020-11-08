package com.zembrzuski.loader.filesystemloader.service.planocontas

import com.zembrzuski.loader.filesystemloader.domain.bovespa.InfoFinaDFin
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Conta
import com.zembrzuski.loader.filesystemloader.domain.mydomain.ContasEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.TipoContaEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.TipoDemonstrativoEnum
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * Converte contas para itrs dos trimestres 2, 3 e 4.
 * Para itrs do trimestre 1, veja ItrPrimeiroTriConverter
 */
@Component
class ItrOutrosTriConverter: ConverterInterface {

    override fun InfoFinaDFin.toConta(
            data: LocalDate, numeroDocumento: Long, conta: ContasEnum,
            tipoDemonstrativoEnum: TipoDemonstrativoEnum): List<Conta> {

        return when (conta.tipoConta) {
            TipoContaEnum.BALANCO -> balanco(conta, data, this, numeroDocumento, tipoDemonstrativoEnum)
            TipoContaEnum.DEMONSTRATIVO -> demonstrativo(conta, data, this, numeroDocumento, tipoDemonstrativoEnum)
        }

    }

    private fun demonstrativo(conta: ContasEnum, data: LocalDate, infoFinaDFin: InfoFinaDFin, numeroDocumento: Long, tipoDemonstrativo: TipoDemonstrativoEnum): List<Conta> {
        val conta2 = Conta(conta, data.monthValue/3, data.year-0, infoFinaDFin.valorConta2, numeroDocumento, tipoDemonstrativo)
        val conta5 = Conta(conta, data.monthValue/3, data.year-1, infoFinaDFin.valorConta5, numeroDocumento, tipoDemonstrativo)

        return listOf(conta2, conta5)
    }

    fun balanco(conta: ContasEnum, data: LocalDate, infoFinaDFin: InfoFinaDFin, numeroDocumento: Long, tipoDemonstrativo: TipoDemonstrativoEnum): List<Conta> {
        val conta2 = Conta(conta, data.monthValue/3, data.year-0, infoFinaDFin.valorConta2, numeroDocumento, tipoDemonstrativo)
        val conta3 = Conta(conta, 4, data.year-1, infoFinaDFin.valorConta3, numeroDocumento, tipoDemonstrativo)

        return listOf(conta2, conta3)
    }

}
