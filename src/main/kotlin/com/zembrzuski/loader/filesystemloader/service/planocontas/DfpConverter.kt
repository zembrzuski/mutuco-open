package com.zembrzuski.loader.filesystemloader.service.planocontas

import com.zembrzuski.loader.filesystemloader.domain.bovespa.InfoFinaDFin
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Conta
import com.zembrzuski.loader.filesystemloader.domain.mydomain.ContasEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.TipoDemonstrativoEnum
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DfpConverter: ConverterInterface {

    override fun InfoFinaDFin.toConta(
            data: LocalDate, numeroDocumento: Long,
            conta: ContasEnum, tipoDemonstrativoEnum: TipoDemonstrativoEnum): List<Conta> {

        val conta1 = Conta(conta, data.monthValue/3, data.year-0, valorConta1, numeroDocumento, tipoDemonstrativoEnum)
        val conta2 = Conta(conta, data.monthValue/3, data.year-1, valorConta2, numeroDocumento, tipoDemonstrativoEnum)
        val conta3 = Conta(conta, data.monthValue/3, data.year-2, valorConta3, numeroDocumento, tipoDemonstrativoEnum)

        return listOf(conta1, conta2, conta3).filter { it.value != 0f }
    }

}
