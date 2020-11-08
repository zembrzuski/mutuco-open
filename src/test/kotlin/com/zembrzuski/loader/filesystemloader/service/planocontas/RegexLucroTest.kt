package com.zembrzuski.loader.filesystemloader.service.planocontas

import com.zembrzuski.loader.filesystemloader.domain.bovespa.InfoFinaDFin
import com.zembrzuski.loader.filesystemloader.domain.bovespa.InformacoesFinanceiras
import com.zembrzuski.loader.filesystemloader.domain.bovespa.PlanoConta
import com.zembrzuski.loader.filesystemloader.domain.mydomain.ContasEnum
import org.junit.Assert
import org.junit.Test

internal class RegexLucroTest {

    val dfpConverter = DfpConverter()

    @Test
    fun trueForLucroPeriodo() {
        // given
        val planoConta = PlanoConta("3.11")

        val infoFinaDFin = InfoFinaDFin("Lucro/Prejuízo do Período",
                0f, 0f, 0f, 0f, 0f, 0f,
                planoConta)

        // when
        val result = dfpConverter.regexPredicate(ContasEnum.LUCRO_LIQUIDO, infoFinaDFin)

        // then
        Assert.assertTrue(result)
    }

    @Test
    fun trueForLucroPeriodoAgain() {
        // given
        val planoConta = PlanoConta("3.11")

        val infoFinaDFin = InfoFinaDFin("Lucro/prejuizo Do período",
                0f, 0f, 0f, 0f, 0f, 0f,
                planoConta)

        // when
        val result = dfpConverter.regexPredicate(ContasEnum.LUCRO_LIQUIDO, infoFinaDFin)

        // then
        Assert.assertTrue(result)
    }

    @Test
    fun falseForLucroConsolidado() {
        // given
        val planoConta = PlanoConta("3.11")

        val infoFinaDFin = InfoFinaDFin("Lucro/prejuizo Consolidado Do período",
                0f, 0f, 0f, 0f, 0f, 0f,
                planoConta)

        // when
        val result = dfpConverter.regexPredicate(ContasEnum.LUCRO_LIQUIDO, infoFinaDFin)

        // then
        Assert.assertFalse(result)
    }


    // FAZER TESTE PARA OUTRA REGEX
    // FAZER TESTE DA MINHA REFATORACAO MAROTA
}