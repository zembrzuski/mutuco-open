package com.zembrzuski.loader.filesystemloader.service.balance

import com.google.cloud.Timestamp
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.*
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDate

class LastBalanceReducerTest {

    @Test
    fun reduceCorrectly() {
        // given
        val codigoCvm = "sdf"
        val nomeEmpresa = "34324"
        val data1 = LocalDate.of(2000, 1, 1)
        val data2 = LocalDate.of(2000, 1, 3)
        val tipo = TipoDemonstrativoEnum.DFP
        val general = StockQuantity(1L, 2L, 3L)
        val treasury = StockQuantity(1L, 2L, 3L)
        val socialCapital = SocialCapital(general, treasury)
        val balancea1 = Balanco(codigoCvm, nomeEmpresa, data1, tipo, 21, emptyList(), socialCapital, Scale.MIL_REAIS, Scale.REAIS)
        val balancea2 = Balanco(codigoCvm, nomeEmpresa, data1, tipo, 23, emptyList(), socialCapital, Scale.MIL_REAIS, Scale.REAIS)
        val balanceb = Balanco(codigoCvm, nomeEmpresa, data2, tipo, 28, emptyList(), socialCapital, Scale.MIL_REAIS, Scale.REAIS)
        val balances = listOf(balancea1, balancea2, balanceb)

        val expected = listOf(balancea2, balanceb)

        // when
        val result = LastBalanceReducer().reduceLastBalanceForEachTrimester(balances)

        // then
        assertEquals(result, expected)
    }

    @Test
    fun reduceById() {
        // given
        val bal1 = BalanceTrimesterInformation(1L, "c", 2020, 1)
        val bal2 = BalanceTrimesterInformation(2L, "c", 2021, 1)
        val bal3 = BalanceTrimesterInformation(3L, "c", 2022, 1)

        val balances = listOf(bal1, bal2, bal3)

        // when
        val result = LastBalanceReducer().reduceBalanceById(balances)

        // then
        assertEquals(result, bal3)
    }

    @Test(expected = IllegalStateException::class)
    fun reduceByIdEmpty() {
        LastBalanceReducer().reduceBalanceById(emptyList())
    }

}
