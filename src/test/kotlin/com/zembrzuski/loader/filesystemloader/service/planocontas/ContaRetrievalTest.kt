package com.zembrzuski.loader.filesystemloader.service.planocontas

import com.zembrzuski.loader.filesystemloader.domain.bovespa.ComposicaoCapital
import com.zembrzuski.loader.filesystemloader.domain.bovespa.ComposicaoCapitalSocial
import com.zembrzuski.loader.filesystemloader.domain.bovespa.InfoFinaDFin
import com.zembrzuski.loader.filesystemloader.domain.bovespa.PlanoConta
import com.zembrzuski.loader.filesystemloader.domain.mydomain.*
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

internal class ContaRetrievalTest {

    val dfpConverter = DfpConverter()

    @Test
    fun retrievePatrimonioLiquidoSuccessfully() {
        // given
        val infoFinanceiras = listOf(createPatrimonioLiquido(), createLucro())
        val raw = createBalancoRaw(infoFinanceiras)
        val date = LocalDate.parse("2020-12-31")
        val conta = ContasEnum.PATRIMONIO_LIQUIDO

        val conta1 = Conta(ContasEnum.PATRIMONIO_LIQUIDO, 4, 2020, 1f, 32L, TipoDemonstrativoEnum.DFP)
        val conta2 = Conta(ContasEnum.PATRIMONIO_LIQUIDO, 4, 2019, 2f, 32L, TipoDemonstrativoEnum.DFP)
        val expected = listOf(conta1, conta2)

        // when
        val retrievedConta = dfpConverter.retrieveConta(raw, date, conta)

        // then
        assertEquals(expected, retrievedConta)
    }

    @Test
    fun retrieveLucroSuccessfully() {
        // given
        val infoFinanceiras = listOf(createPatrimonioLiquido(), createLucro())
        val raw = createBalancoRaw(infoFinanceiras)
        val date = LocalDate.parse("2020-12-31")
        val conta = ContasEnum.LUCRO_LIQUIDO

        val conta1 = Conta(ContasEnum.LUCRO_LIQUIDO, 4, 2020, 11f, 32L, TipoDemonstrativoEnum.DFP)
        val conta2 = Conta(ContasEnum.LUCRO_LIQUIDO, 4, 2019, 22f, 32L, TipoDemonstrativoEnum.DFP)
        val expected = listOf(conta1, conta2)

        // when
        val retrievedConta = dfpConverter.retrieveConta(raw, date, conta)

        // then
        assertEquals(expected, retrievedConta)
    }

    @Test
    fun emptyListWhenAbsentAccount() {
        // given
        val infoFinanceiras = listOf(createPatrimonioLiquido())
        val raw = createBalancoRaw(infoFinanceiras)
        val date = LocalDate.parse("2020-12-31")
        val conta = ContasEnum.LUCRO_LIQUIDO

        val expectedEmptyList = listOf<Conta>()

        // when
        val retrievedConta = dfpConverter.retrieveConta(raw, date, conta)

        // then
        assertEquals(expectedEmptyList, retrievedConta)
    }

    @Test
    fun convertContaWithPresentPatrimonioAndAbsentLucro() {
        // given
        val infoFinanceiras = listOf(createPatrimonioLiquido(), createLucro())
        val raw = createBalancoRaw(infoFinanceiras)
        val date = LocalDate.parse("2020-12-31")

        val patrLiq1 = Conta(ContasEnum.PATRIMONIO_LIQUIDO, 4, 2020, 1f, 32L, TipoDemonstrativoEnum.DFP)
        val patrLiq2 = Conta(ContasEnum.PATRIMONIO_LIQUIDO, 4, 2019, 2f, 32L, TipoDemonstrativoEnum.DFP)
        val lucro1 = Conta(ContasEnum.LUCRO_LIQUIDO, 4, 2020, 11f, 32L, TipoDemonstrativoEnum.DFP)
        val lucro2 = Conta(ContasEnum.LUCRO_LIQUIDO, 4, 2019, 22f, 32L, TipoDemonstrativoEnum.DFP)
        val expected = listOf(patrLiq1, patrLiq2, lucro1, lucro2)

        // when
        val retrievedConta = dfpConverter.convertConta(raw, date)

        // then
        assertEquals(expected, retrievedConta)
    }

    @Test
    fun convertContaWithPresentPatrimonioAndPresentLucro() {
        // given
        val infoFinanceiras = listOf(createPatrimonioLiquido())
        val raw = createBalancoRaw(infoFinanceiras)
        val date = LocalDate.parse("2020-12-31")

        val patrLiq1 = Conta(ContasEnum.PATRIMONIO_LIQUIDO, 4, 2020, 1f, 32L, TipoDemonstrativoEnum.DFP)
        val patrLiq2 = Conta(ContasEnum.PATRIMONIO_LIQUIDO, 4, 2019, 2f, 32L, TipoDemonstrativoEnum.DFP)
        val expected = listOf(patrLiq1, patrLiq2)

        // when
        val retrievedConta = dfpConverter.convertConta(raw, date)

        // then
        assertEquals(expected, retrievedConta)
    }

    private fun createBalancoRaw(infoFinanceiras: List<InfoFinaDFin>): BalancoRaw {
        val capitalSocial = ComposicaoCapital(0, 0, 0, 0, 0, 0)
        return BalancoRaw("123", "empresa", "31-12-2020", TipoDemonstrativoEnum.DFP, infoFinanceiras, 32L, capitalSocial, 1, 1, "fakeDate")
    }

    private fun createLucro(): InfoFinaDFin {
        return InfoFinaDFin(
                "Lucro/Prejuízo do Período",
                11f,
                22f,
                0f,
                0f,
                0f,
                0f,
                PlanoConta("3.1")
        )
    }

    private fun createPatrimonioLiquido(): InfoFinaDFin {
        return InfoFinaDFin(
                "Patrimônio Líquido",
                1f,
                2f,
                0f,
                0f,
                0f,
                0f,
                PlanoConta("2.3")
        )
    }

}