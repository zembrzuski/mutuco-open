package com.zembrzuski.loader.filesystemloader.service.planocontas

import com.zembrzuski.loader.filesystemloader.domain.mydomain.BalancoRaw
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Conta
import com.zembrzuski.loader.filesystemloader.domain.mydomain.TipoDemonstrativoEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class PlanoContasConverter @Autowired constructor(
        val dfpConverter: DfpConverter,
        val itrPrimeiroTriConverter: ItrPrimeiroTriConverter,
        val itrOutrosTriConverter: ItrOutrosTriConverter) {

    fun convert(raw: BalancoRaw, date: LocalDate): List<Conta> {
        return when (raw.tipoDemonstrativoEnum) {
            TipoDemonstrativoEnum.DFP -> dfpConverter.convertConta(raw, date)
            TipoDemonstrativoEnum.ITR -> convertItr(raw, date)
        }
    }

    private fun convertItr(raw: BalancoRaw, date: LocalDate): List<Conta> {
        return when (date.monthValue) {
            3 -> itrPrimeiroTriConverter.convertConta(raw, date)
            else -> itrOutrosTriConverter.convertConta(raw, date)
        }
    }

}
