package com.zembrzuski.loader.filesystemloader.service.balance

import com.google.cloud.Timestamp
import com.zembrzuski.loader.filesystemloader.domain.mydomain.*
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class SocialCapitalScalerTest {

    @Test
    fun scaleReais() {
        // given
        val general = StockQuantity(1, 2, 3)
        val treasury = StockQuantity(4, 5, 6)
        val socialCapital = SocialCapital(general, treasury)
        val balance1 = Balanco("x", "x", LocalDate.now(), TipoDemonstrativoEnum.DFP,
                1L, emptyList(), socialCapital, Scale.MIL_REAIS, Scale.REAIS)

        // when
        val result = SocialCapitalScaler().scale(balance1)

        // then
        assertEquals(result, balance1)
    }

    @Test
    fun scaleMilReais() {
        // given
        val general = StockQuantity(1, 2, 3)
        val treasury = StockQuantity(4, 5, 6)
        val socialCapital = SocialCapital(general, treasury)
        val balance1 = Balanco("x", "x", LocalDate.now(), TipoDemonstrativoEnum.DFP,
                1L, emptyList(), socialCapital, Scale.REAIS, Scale.MIL_REAIS)

        val generalExpected = StockQuantity(1000, 2000, 3000)
        val treasuryExpected = StockQuantity(4000, 5000, 6000)
        val socialCapitalExpected = SocialCapital(generalExpected, treasuryExpected)

        val expected = balance1.copy(socialCapital = socialCapitalExpected)

        // when
        val result = SocialCapitalScaler().scale(balance1)

        // then
        assertEquals(result, expected)
    }
}
