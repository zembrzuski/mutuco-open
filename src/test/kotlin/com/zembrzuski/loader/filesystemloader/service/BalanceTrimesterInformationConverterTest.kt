package com.zembrzuski.loader.filesystemloader.service

import com.google.cloud.Timestamp
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.SocialCapitalEntity
import com.zembrzuski.loader.filesystemloader.domain.mydomain.*
import com.zembrzuski.loader.filesystemloader.service.balance.TrimesterInformationConverter
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDate

class BalanceTrimesterInformationConverterTest {

    @Test
    fun convert() {
        // given
        val contas: List<ContaAjustada> = listOf(
                ContaAjustada(ContasEnum.PATRIMONIO_LIQUIDO, 1, 2018, 201801F, "1", false, false, 20181),
                ContaAjustada(ContasEnum.PATRIMONIO_LIQUIDO, 2, 2018, 201802F, "1", false, false, 20301),
                ContaAjustada(ContasEnum.LUCRO_LIQUIDO, 1, 2018, 2018011F, "1", false, false, 20181),
                ContaAjustada(ContasEnum.LUCRO_LIQUIDO, 1, 2020, 2020011F, "1", false, false, 20301)
        )

        val g2000 = StockQuantity(1, 2, 3)
        val t2000 = StockQuantity(4, 5, 6)
        val g2018 = StockQuantity(7, 8, 9)
        val t2018 = StockQuantity(11, 12, 13)
        val g2030 = StockQuantity(21, 22, 23)
        val t2030 = StockQuantity(31, 32, 33)

        val givenSocialCapital20001 = SocialCapital(g2000, t2000)
        val givenSocialCapital20181 = SocialCapital(g2018, t2018)
        val givenSocialCapital20301 = SocialCapital(g2030, t2030)

        val givenBalances = listOf(
                createBalance("2000", givenSocialCapital20001, 20001),
                createBalance("2018", givenSocialCapital20181, 20181),
                createBalance("2030", givenSocialCapital20301, 20301)
        )

        val expectedSocialCapital20001 = SocialCapitalEntity(g2000, t2000)
        val expectedSocialCapital20181 = SocialCapitalEntity(g2018, t2018)
        val expectedSocialCapital20301 = SocialCapitalEntity(g2030, t2030)

        val expected = listOf(
                BalanceTrimesterInformation(20001, "1", 2000, 1, null, null, expectedSocialCapital20001),
                BalanceTrimesterInformation(20181, "1", 2018, 1, 2018011F, 201801F, expectedSocialCapital20181),
                BalanceTrimesterInformation(20301, "1", 2018, 2, null, 201802F, null),
                BalanceTrimesterInformation(20301, "1", 2020, 1, 2020011F, null, null),
                BalanceTrimesterInformation(20301, "1", 2030, 1, null, null, expectedSocialCapital20301)
        )

        // when
        val result = TrimesterInformationConverter().convert("1", contas, givenBalances)

        /// then
        Assert.assertEquals(result, expected)
    }

    private fun createBalance(year: String, socialCapital: SocialCapital, id: Long): Balanco {
        return Balanco(
                "cvmCode",
                "nomeEmpresa",
                LocalDate.of(year.toInt(), 3, 22),
                TipoDemonstrativoEnum.ITR,
                id,
                emptyList(),
                socialCapital,
                Scale.MIL_REAIS,
                Scale.MIL_REAIS
        )
    }

}
