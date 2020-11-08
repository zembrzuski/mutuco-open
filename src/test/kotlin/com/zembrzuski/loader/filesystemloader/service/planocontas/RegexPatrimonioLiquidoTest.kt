package com.zembrzuski.loader.filesystemloader.service.planocontas

import com.zembrzuski.loader.filesystemloader.domain.bovespa.InfoFinaDFin
import com.zembrzuski.loader.filesystemloader.domain.bovespa.PlanoConta
import com.zembrzuski.loader.filesystemloader.domain.mydomain.ContasEnum
import org.junit.Assert
import org.junit.Test

internal class RegexPatrimonioLiquidoTest {

    val dfpConverter = DfpConverter()

    @Test
    fun truePatrimonioLuquido() {
        // given
        val planoConta = PlanoConta("2.1")

        val infoFinaDFin = InfoFinaDFin("Patrimônio Líquido",
                0f, 0f, 0f, 0f, 0f, 0f,
                planoConta)

        // when
        val result = dfpConverter.regexPredicate(ContasEnum.PATRIMONIO_LIQUIDO, infoFinaDFin)

        // then
        Assert.assertTrue(result)
    }

    @Test
    fun truePatrimonioLuquido2() {
        // given
        val planoConta = PlanoConta("2.1")

        val infoFinaDFin = InfoFinaDFin("Patrimonio Liquido",
                0f, 0f, 0f, 0f, 0f, 0f,
                planoConta)

        // when
        val result = dfpConverter.regexPredicate(ContasEnum.PATRIMONIO_LIQUIDO, infoFinaDFin)

        // then
        Assert.assertTrue(result)
    }

    @Test
    fun falseForLucro() {
        // given
        val planoConta = PlanoConta("2.1")

        val infoFinaDFin = InfoFinaDFin("Lucro/Prejuízo do Período",
                0f, 0f, 0f, 0f, 0f, 0f,
                planoConta)

        // when
        val result = dfpConverter.regexPredicate(ContasEnum.PATRIMONIO_LIQUIDO, infoFinaDFin)

        // then
        Assert.assertFalse(result)
    }

}