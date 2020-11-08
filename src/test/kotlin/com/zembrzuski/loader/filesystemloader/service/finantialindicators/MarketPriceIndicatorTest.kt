package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.google.cloud.Timestamp
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.Quote
import com.zembrzuski.loader.filesystemloader.domain.firebase.SocialCapitalEntity
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.IndicatorsEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.SectorClassification
import com.zembrzuski.loader.filesystemloader.domain.mydomain.StockQuantity
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class MarketPriceIndicatorTest {

    private val marketPriceService = MarketPriceIndicator()

    @Test
    fun marketValueForOrdinaryOnly() {
        // given'
        val general = StockQuantity(10, 15, 25)
        val treasury = StockQuantity(0, 0, 0)
        val socialCapital = SocialCapitalEntity(general, treasury)
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, socialCapital)

        val strangeRealQuote = Quote("QUOT33", 2.1F)
        val strangeAdjustedQuote = Quote("QUOT33", 1.1F)
        val strangeRealQuote2 = Quote("QUOT34", 3.1F)
        val strangeAdjustedQuote2 = Quote("QUOT34", .1F)
        val ordinaryRealQuote = Quote("QUOT3", 32.1F)
        val ordinaryAdjustedQuote = Quote("QUOT3", 21.1F)
        val preferredRealQuote = Quote("QUOT4", 12.1F)
        val preferredAdjustedQuote = Quote("QUOT4", 18.1F)

        val quoteReal = listOf(ordinaryRealQuote, preferredRealQuote, strangeRealQuote, strangeRealQuote2)
        val quoteAdjusted = listOf(ordinaryAdjustedQuote, preferredAdjustedQuote, strangeAdjustedQuote, strangeAdjustedQuote2)

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = listOf("QUOT3", "QUOT33", "QUOT34")

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when

        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, 321F)
    }

    @Test
    fun marketValueForPreferredOnly() {
        // given
        val general = StockQuantity(10, 15, 25)
        val treasury = StockQuantity(0, 0, 0)
        val socialCapital = SocialCapitalEntity(general, treasury)
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, socialCapital)

        val strangeRealQuote = Quote("QUOT33", 2.1F)
        val strangeAdjustedQuote = Quote("QUOT33", 1.1F)
        val strangeRealQuote2 = Quote("QUOT34", 3.1F)
        val strangeAdjustedQuote2 = Quote("QUOT34", .1F)
        val ordinaryRealQuote = Quote("QUOT3", 32.1F)
        val ordinaryAdjustedQuote = Quote("QUOT3", 21.1F)
        val preferredRealQuote = Quote("QUOT4", 12.1F)
        val preferredAdjustedQuote = Quote("QUOT4", 18.1F)

        val quoteReal = listOf(ordinaryRealQuote, preferredRealQuote, strangeRealQuote, strangeRealQuote2)
        val quoteAdjusted = listOf(ordinaryAdjustedQuote, preferredAdjustedQuote, strangeAdjustedQuote, strangeAdjustedQuote2)

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = listOf("QUOT4", "QUOT33", "QUOT34")

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when
        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, 181.5F)
    }

    @Test
    fun marketValueForCompanyWithoutQuotes() {
        // given
        val general = StockQuantity(10, 15, 25)
        val treasury = StockQuantity(0, 0, 0)
        val socialCapital = SocialCapitalEntity(general, treasury)
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, socialCapital)

        val strangeRealQuote = Quote("QUOT33", 2.1F)
        val strangeAdjustedQuote = Quote("QUOT33", 1.1F)
        val strangeRealQuote2 = Quote("QUOT34", 3.1F)
        val strangeAdjustedQuote2 = Quote("QUOT34", .1F)
        val ordinaryRealQuote = Quote("QUOT3", 32.1F)
        val ordinaryAdjustedQuote = Quote("QUOT3", 21.1F)
        val preferredRealQuote = Quote("QUOT4", 12.1F)
        val preferredAdjustedQuote = Quote("QUOT4", 18.1F)

        val quoteReal = listOf(ordinaryRealQuote, preferredRealQuote, strangeRealQuote, strangeRealQuote2)
        val quoteAdjusted = listOf(ordinaryAdjustedQuote, preferredAdjustedQuote, strangeAdjustedQuote, strangeAdjustedQuote2)

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = emptyList<String>()

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when
        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, 0F)
    }

    @Test
    fun marketValueForOrdinaryAndPreferredAndStupidStock() {
        // given
        val general = StockQuantity(10, 15, 25)
        val treasury = StockQuantity(0, 0, 0)
        val socialCapital = SocialCapitalEntity(general, treasury)
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, socialCapital)

        val strangeRealQuote = Quote("QUOT33", 2.1F)
        val strangeAdjustedQuote = Quote("QUOT33", 1.1F)
        val strangeRealQuote2 = Quote("QUOT34", 3.1F)
        val strangeAdjustedQuote2 = Quote("QUOT34", .1F)
        val ordinaryRealQuote = Quote("QUOT3", 32.1F)
        val ordinaryAdjustedQuote = Quote("QUOT3", 21.1F)
        val preferredRealQuote = Quote("QUOT4", 12.1F)
        val preferredAdjustedQuote = Quote("QUOT4", 18.1F)

        val quoteReal = listOf(ordinaryRealQuote, preferredRealQuote, strangeRealQuote, strangeRealQuote2)
        val quoteAdjusted = listOf(ordinaryAdjustedQuote, preferredAdjustedQuote, strangeAdjustedQuote, strangeAdjustedQuote2)

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = listOf("QUOT4", "QUOT3", "QUOT34", "QUOT33")

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when
        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, 502.5F)
    }

    @Test
    fun marketValueForOrdinaryPreferredStocks() {
        // given
        val general = StockQuantity(10, 15, 25)
        val treasury = StockQuantity(0, 0, 0)
        val socialCapital = SocialCapitalEntity(general, treasury)
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, socialCapital)

        val ordinaryRealQuote = Quote("QUOT3", 32.1F)
        val ordinaryAdjustedQuote = Quote("QUOT3", 21.1F)
        val preferredRealQuote = Quote("QUOT4", 12.1F)
        val preferredAdjustedQuote = Quote("QUOT4", 18.1F)

        val quoteReal = listOf(ordinaryRealQuote, preferredRealQuote)
        val quoteAdjusted = listOf(ordinaryAdjustedQuote, preferredAdjustedQuote)

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = listOf("QUOT4", "QUOT3")

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when
        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, 502.5F)
    }

    @Test
    fun exceptionForAbsentQuote() {
        // given
        val general = StockQuantity(10, 15, 25)
        val treasury = StockQuantity(0, 0, 0)
        val socialCapital = SocialCapitalEntity(general, treasury)
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, socialCapital)

        val preferredRealQuote = Quote("QUOT4", 12.1F)
        val preferredAdjustedQuote = Quote("QUOT4", 18.1F)

        val quoteReal = listOf(preferredRealQuote)
        val quoteAdjusted = listOf(preferredAdjustedQuote)

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = listOf("QUOT4", "QUOT3")

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when
        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, Float.NaN)
    }

    @Test
    fun exceptionForAbsentSocialCapital() {
        // given
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, null)

        val ordinaryRealQuote = Quote("QUOT3", 32.1F)
        val ordinaryAdjustedQuote = Quote("QUOT3", 21.1F)
        val preferredRealQuote = Quote("QUOT4", 12.1F)
        val preferredAdjustedQuote = Quote("QUOT4", 18.1F)

        val quoteReal = listOf(ordinaryRealQuote, preferredRealQuote)
        val quoteAdjusted = listOf(ordinaryAdjustedQuote, preferredAdjustedQuote)

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = listOf("QUOT4", "QUOT3")

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when
        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, Float.NaN)
    }


    /**
     * A empres tem ações de classes ordinárias e duas classes preferenciais.
     * Tenho todas cotações, então calculo o valor de mercado bem certinho.
     */
    @Test
    fun marketPriceForCompaniesLikeSpringer() {
        // given
        val general = StockQuantity(10, 16, 25)
        val treasury = StockQuantity(0, 0, 0)
        val socialCapital = SocialCapitalEntity(general, treasury)
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, socialCapital)

        val spri3real = Quote("SPRI3", 1.2F)
        val spri5real = Quote("SPRI5", 2.3F)
        val spri6real = Quote("SPRI6", 3.4F)

        val spri3adjusted = Quote("SPRI3", 91.2F)
        val spri5adjusted = Quote("SPRI5", 92.3F)
        val spri6adjusted = Quote("SPRI6", 93.4F)

        val quoteReal = listOf(spri3real, spri5real, spri6real)
        val quoteAdjusted = listOf(spri3adjusted, spri5adjusted, spri6adjusted)

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = listOf("SPRI3", "SPRI5", "SPRI6")

        val expected = (1.2*10 + 2.3*8 + 3.4*8).toFloat()

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when
        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, expected)
    }

    /**
     * Se a empresa tem ações ordinárias e 2 classes preferenciais.
     * Mas eu só tenho a cotação de 1 classe preferencial.
     * Então eu posso aproximar o valor de mercaado utilizando essa ação preferencial.
     */
    @Test
    fun marketPriceForCompaniesLikeSpringerWithAbsentQuoteSpri5() {
        // given
        val general = StockQuantity(10, 16, 25)
        val treasury = StockQuantity(0, 0, 0)
        val socialCapital = SocialCapitalEntity(general, treasury)
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, socialCapital)

        val spri3real = Quote("SPRI3", 1.2F)
        val spri6real = Quote("SPRI6", 3.4F)

        val spri3adjusted = Quote("SPRI3", 91.2F)
        val spri5adjusted = Quote("SPRI5", 92.3F)
        val spri6adjusted = Quote("SPRI6", 93.4F)

        val quoteReal = listOf(spri3real, spri6real)
        val quoteAdjusted = listOf(spri3adjusted, spri5adjusted, spri6adjusted)

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = listOf("SPRI3", "SPRI5", "SPRI6")

        val expected = (1.2*10 + 3.4*16).toFloat()

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when
        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, expected)
    }

    /**
     * Se, por algum motivo, a empresa tem ações ordinárias e preferenciais,
     * mas eu não tenho cotações das ações preferenciais, é um erro. Eu preciso
     * dessa informação vital.
     */
    @Test
    fun marketPriceForCompaniesLikeSpringerWithAbsentPreferredQuotes() {
        // given
        val general = StockQuantity(10, 16, 25)
        val treasury = StockQuantity(0, 0, 0)
        val socialCapital = SocialCapitalEntity(general, treasury)
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, socialCapital)

        val spri3real = Quote("SPRI3", 1.2F)

        val spri3adjusted = Quote("SPRI3", 91.2F)
        val spri5adjusted = Quote("SPRI5", 92.3F)
        val spri6adjusted = Quote("SPRI6", 93.4F)

        val quoteReal = listOf(spri3real)
        val quoteAdjusted = listOf(spri3adjusted, spri5adjusted, spri6adjusted)

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = listOf("SPRI3", "SPRI5", "SPRI6")

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when
        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, Float.NaN)
    }


    /**
     * Se, por algum motivo, a empresa tem ações ordinárias e preferenciais,
     * mas eu não tenho cotações das ações ordinárias, é um erro. Eu preciso
     * dessa informação vital.
     */
    @Test
    fun marketPriceForCompaniesLikeSpringerWithAbsentQuoteSpri3() {
        // given
        val general = StockQuantity(10, 16, 25)
        val treasury = StockQuantity(0, 0, 0)
        val socialCapital = SocialCapitalEntity(general, treasury)
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, socialCapital)

        val spri5real = Quote("SPRI5", 2.3F)
        val spri6real = Quote("SPRI6", 3.4F)

        val spri3adjusted = Quote("SPRI3", 91.2F)
        val spri5adjusted = Quote("SPRI5", 92.3F)
        val spri6adjusted = Quote("SPRI6", 93.4F)

        val quoteReal = listOf(spri5real, spri6real)
        val quoteAdjusted = listOf(spri3adjusted, spri5adjusted, spri6adjusted)

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = listOf("SPRI3", "SPRI5", "SPRI6")

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when
        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, Float.NaN)
    }

    /**
     * Se não existe nenhuma cotação, então claramente existe um erro: está faltando informações vitais de cotações,
     * e eu tenho de importá-las adequadamente
     */
    @Test
    fun marketPriceForCompaniesLikeSpringerWithAbsentQuotes() {
        // given
        val general = StockQuantity(10, 16, 25)
        val treasury = StockQuantity(0, 0, 0)
        val socialCapital = SocialCapitalEntity(general, treasury)
        val balanceInfo = BalanceTrimesterInformation(1L, "x", 2020, 1, null, null, socialCapital)

        val quoteReal = listOf<Quote>()
        val quoteAdjusted = listOf<Quote>()

        val trimesterInformation = TrimesterInformation(balanceInfo, quoteReal, quoteAdjusted)
        val stockCodes = listOf("SPRI3", "SPRI5", "SPRI6")

        val sector = SectorClassification("a", "b", "c")
        val companyInfo = GeneralCompanyInformation("x", sector, "x", "x", stockCodes, "x")
        val params = IndicatorParameters(trimesterInformation, companyInfo)

        // when
        val result = marketPriceService.valueFunction(params)

        // then
        assertEquals(result, Float.NaN)
    }

    @Test
    fun enum() {
        assertEquals(marketPriceService.enumFunction(), IndicatorsEnum.MARKET_PRICE)
    }

}
