package com.zembrzuski.loader.filesystemloader.service.planocontas

import com.google.api.client.util.Lists
import com.zembrzuski.loader.filesystemloader.domain.bovespa.InfoFinaDFin
import com.zembrzuski.loader.filesystemloader.domain.mydomain.BalancoRaw
import com.zembrzuski.loader.filesystemloader.domain.mydomain.Conta
import com.zembrzuski.loader.filesystemloader.domain.mydomain.ContasEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.TipoDemonstrativoEnum
import java.time.LocalDate

interface ConverterInterface {

    fun InfoFinaDFin.toConta(
            data: LocalDate,
            numeroDocumento: Long,
            conta: ContasEnum,
            tipoDemonstrativoEnum: TipoDemonstrativoEnum
    ): List<Conta>

    fun convertConta(raw: BalancoRaw, dataReferenciaDocumento: LocalDate): List<Conta> {
        return ContasEnum
                .values()
                .flatMap { conta -> retrieveConta(raw, dataReferenciaDocumento, conta) }
    }

    fun retrieveConta(raw: BalancoRaw, date: LocalDate, conta: ContasEnum): List<Conta> {
        val retrievedAccount = raw.infoFinanceiras
                .firstOrNull{ regexPredicate(conta, it) }
                ?.toConta(date, raw.numeroDocumento, conta, raw.tipoDemonstrativoEnum)
                ?.filter { it.value.equals(0).not() }

        return retrievedAccount ?: Lists.newArrayList()
    }

    fun regexPredicate(conta: ContasEnum, it: InfoFinaDFin) =
            (conta.regex.containsMatchIn(it.descricaoConta)
                    && it.planoConta.numeroConta.startsWith(conta.contaPrimeiroDigito))

}