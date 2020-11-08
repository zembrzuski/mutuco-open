package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.SocialCapitalEntity
import com.zembrzuski.loader.filesystemloader.domain.mydomain.*
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate
import java.time.Month

@SpringBootTest
@RunWith(SpringRunner::class)
class CompanyBalanceInformationConverterTest {

    @Autowired
    private lateinit var companyBalanceInformationConverter: CompanyBalanceInformationConverter

    @Test
    fun convertBalances() {
        // given
        val balances = listOf(getBalance1(), getBalance2())
        val expected: List<BalanceTrimesterInformation> = expectedValues()

        // when
        val result = companyBalanceInformationConverter.convert("cvmCode", balances)

        // then
        assertEquals(result, expected)
    }

    private fun expectedValues(): List<BalanceTrimesterInformation> {
        val tri1 = BalanceTrimesterInformation(
                111L, "cvmCode", 2019, 1, null, 5F, null)

        val tri2 = BalanceTrimesterInformation(
                222L,
                "cvmCode",
                2020,
                1,
                23200F,
                32500F,
                SocialCapitalEntity(
                        StockQuantity(1000, 2000, 3000),
                        StockQuantity(0, 0, 0)
                )
        )

        val tri3 = BalanceTrimesterInformation(
                222L,
                "cvmCode",
                2020,
                2,
                1000F,
                44600F,
                SocialCapitalEntity(
                        StockQuantity(3, 4, 7),
                        StockQuantity(1, 1, 2)
                )
        )

        return listOf(tri1, tri2, tri3)
    }

    private fun getBalance2(): Balanco {
        val numeroDocumento = 222L

        val general = StockQuantity(3, 4, 7)
        val treasury = StockQuantity(1, 1, 2)
        val capitalSocial = SocialCapital(general, treasury)

        val contaAtri2 = Conta(
                ContasEnum.PATRIMONIO_LIQUIDO,
                1,
                2020,
                32.5F,
                numeroDocumento,
                TipoDemonstrativoEnum.ITR)

        val contaAtri3 = Conta(
                ContasEnum.PATRIMONIO_LIQUIDO,
                2,
                2020,
                44.6F,
                numeroDocumento,
                TipoDemonstrativoEnum.ITR)

        val contaBtri2 = Conta(
                ContasEnum.LUCRO_LIQUIDO,
                1,
                2020,
                23.2F,
                numeroDocumento,
                TipoDemonstrativoEnum.ITR)

        val contaBtri3 = Conta(
                ContasEnum.LUCRO_LIQUIDO,
                2,
                2020,
                1F,
                numeroDocumento,
                TipoDemonstrativoEnum.ITR)

        val contas = listOf(contaAtri2, contaAtri3, contaBtri2, contaBtri3)

        return Balanco(
                "cvmCode",
                "empresa",
                LocalDate.of(2020, Month.JUNE, 30),
                TipoDemonstrativoEnum.ITR,
                numeroDocumento,
                contas,
                capitalSocial,
                Scale.MIL_REAIS,
                Scale.REAIS
        )
    }

    private fun getBalance1(): Balanco {
        val numeroDocumento = 111L

        val general = StockQuantity(1, 2, 3)
        val treasury = StockQuantity(0, 0, 0)
        val capitalSocial = SocialCapital(general, treasury)

        val contaAtri1 = Conta(
                ContasEnum.PATRIMONIO_LIQUIDO,
                1,
                2019,
                5F,
                numeroDocumento,
                TipoDemonstrativoEnum.ITR)

        val contaAtri2 = Conta(
                ContasEnum.PATRIMONIO_LIQUIDO,
                1,
                2020,
                32F,
                numeroDocumento,
                TipoDemonstrativoEnum.ITR)

        val contas = listOf(contaAtri1, contaAtri2)

        return Balanco(
                "cvmCode",
                "empresa",
                LocalDate.of(2020, Month.MARCH, 31),
                TipoDemonstrativoEnum.ITR,
                numeroDocumento,
                contas,
                capitalSocial,
                Scale.REAIS,
                Scale.MIL_REAIS
        )
    }

}