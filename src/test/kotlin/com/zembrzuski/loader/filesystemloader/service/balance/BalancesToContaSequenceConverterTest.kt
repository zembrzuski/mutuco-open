package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.mydomain.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class BalancesToContaSequenceConverterTest {

    @Test
    fun scaleBalanceWithScaleReais() {
        // given
        val socialCapital = SocialCapital(StockQuantity(0, 0, 0), StockQuantity(0, 0, 0))
        val conta1 = Conta(ContasEnum.LUCRO_LIQUIDO, 1, 2020, 32.4F, 33L, TipoDemonstrativoEnum.DFP)
        val conta2 = Conta(ContasEnum.PATRIMONIO_LIQUIDO, 1, 2020, 32.8F, 33L, TipoDemonstrativoEnum.DFP)
        val planoContas = listOf(conta1, conta2)
        val balance = Balanco(
                "cvm", "empresa", LocalDate.now(), TipoDemonstrativoEnum.DFP, 42L,
                planoContas, socialCapital, Scale.REAIS, Scale.REAIS)

        val expected = Balanco(
                "cvm", "empresa", LocalDate.now(), TipoDemonstrativoEnum.DFP, 42L,
                planoContas, socialCapital, Scale.REAIS, Scale.REAIS)

        // when
        val result = BalancesToContaSequenceConverter().scaleAccountValue(balance)

        // then
        assertEquals(result, expected)
    }

    @Test
    fun scaleBalanceWithScaleMilReais() {
        // given
        val socialCapital = SocialCapital(StockQuantity(0, 0, 0), StockQuantity(0, 0, 0))
        val conta1 = Conta(ContasEnum.LUCRO_LIQUIDO, 1, 2020, 32.4F, 33L, TipoDemonstrativoEnum.DFP)
        val conta2 = Conta(ContasEnum.PATRIMONIO_LIQUIDO, 1, 2020, 32.8F, 33L, TipoDemonstrativoEnum.DFP)
        val planoContas = listOf(conta1, conta2)
        val date = LocalDate.now()
        val balance = Balanco(
                "cvm", "empresa", date, TipoDemonstrativoEnum.DFP, 42L,
                planoContas, socialCapital, Scale.MIL_REAIS, Scale.REAIS)

        val conta1expected = Conta(ContasEnum.LUCRO_LIQUIDO, 1, 2020, 32400.002F, 33L, TipoDemonstrativoEnum.DFP)
        val conta2expected = Conta(ContasEnum.PATRIMONIO_LIQUIDO, 1, 2020, 32800F, 33L, TipoDemonstrativoEnum.DFP)
        val planoContasExpected = listOf(conta1expected, conta2expected)

        val expected = Balanco(
                "cvm", "empresa", date, TipoDemonstrativoEnum.DFP, 42L,
                planoContasExpected, socialCapital, Scale.MIL_REAIS, Scale.REAIS)

        // when
        val result = BalancesToContaSequenceConverter().scaleAccountValue(balance)

        // then
        assertEquals(result, expected)
    }

}
