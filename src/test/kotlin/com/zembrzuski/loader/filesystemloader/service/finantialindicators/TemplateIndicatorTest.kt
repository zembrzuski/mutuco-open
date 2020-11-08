package com.zembrzuski.loader.filesystemloader.service.finantialindicators

import com.google.cloud.Timestamp
import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.Indicator
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.GeneralCompanyInformation
import com.zembrzuski.loader.filesystemloader.domain.mydomain.IndicatorsEnum
import com.zembrzuski.loader.filesystemloader.domain.mydomain.SectorClassification
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class TemplateIndicatorTest {

    @Test
    fun addIndicatorToTrimesterForValidNumber() {
        // given
        val balanceTrimesterInformation = BalanceTrimesterInformation(1L, "cvm", 2020, 1, 1F, 2F)

        val previousIndiator = Indicator("qualquer1", -1F)
        val previousIndicators = listOf(previousIndiator)
        val trimester = TrimesterInformation(
                balanceTrimesterInformation = balanceTrimesterInformation,
                indicators = previousIndicators)

        val sector = SectorClassification("a", "b", "c")
        val quotes = listOf("XOXO4")
        val companyInfo = GeneralCompanyInformation("cvm", sector, "site", "activity", quotes, "xoxo")
        val parameters = IndicatorParameters(trimester, companyInfo)
        val itface = ValidNumberIndicator()

        val expected = trimester.copy(
                indicators = previousIndicators + Indicator("market_price", 123F)
        )

        // when
        val result = TemplateIndicator().template(parameters, itface)

        // then
        assertEquals(result, expected)
    }

    @Test
    fun dontAddIndicatorToTrimesterForInValidNumber() {
        // given
        val balanceTrimesterInformation = BalanceTrimesterInformation(1L, "cvm", 2020, 1, 1F, 2F)

        val previousIndiator = Indicator("qualquer1", -1F)
        val previousIndicators = listOf(previousIndiator)
        val trimester = TrimesterInformation(
                balanceTrimesterInformation = balanceTrimesterInformation,
                indicators = previousIndicators)

        val sector = SectorClassification("a", "b", "c")
        val quotes = listOf("XOXO4")
        val companyInfo = GeneralCompanyInformation("cvm", sector, "site", "activity", quotes, "xoxo")
        val parameters = IndicatorParameters(trimester, companyInfo)
        val itface = NanIndicator()

        // when
        val result = TemplateIndicator().template(parameters, itface)

        // then
        assertEquals(result, trimester)
    }

}

class ValidNumberIndicator : IndicatorInterface {

    override fun valueFunction(indicatorParameters: IndicatorParameters): Float {
        return 123F
    }

    override fun enumFunction(): IndicatorsEnum {
        return IndicatorsEnum.MARKET_PRICE
    }

}


class NanIndicator : IndicatorInterface {

    override fun valueFunction(indicatorParameters: IndicatorParameters): Float {
        return Float.NaN
    }

    override fun enumFunction(): IndicatorsEnum {
        return IndicatorsEnum.MARKET_PRICE
    }

}
