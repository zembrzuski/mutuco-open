package com.zembrzuski.loader.filesystemloader.service.quotes

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.QuotesTrimesterInformation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Componente que recupera as cotações e filtra as cotações por trimestre.
 */
@Component
class QuotesTrimesterImporter @Autowired constructor(
        private val quotesPersistence: QuotesPersistence,
        private val quotesHelper: QuotesHelper,
        private val quotesTimeSeriesConverter: QuotesTimeSeriesConverter) {

    fun import(
            cvmCode: String, stockCode: String,
            balanceTrimesterInformationSequence: List<BalanceTrimesterInformation>)
            : List<QuotesTrimesterInformation> {

        val quotesTimeSeriesText = quotesTimeSeriesConverter.getTimeSeries(
                cvmCode, stockCode, balanceTrimesterInformationSequence)

        quotesTimeSeriesText?.let { json ->
            quotesPersistence.persist(cvmCode, stockCode, json)
        }

        val quotesTimeSeries = quotesHelper.getTimeSeries(quotesTimeSeriesText)

        return balanceTrimesterInformationSequence
                .mapNotNull { quotesTimeSeriesConverter.getLastQuote(
                        cvmCode, stockCode, it.year, it.trimester, quotesTimeSeries) }
    }

}
