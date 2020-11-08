package com.zembrzuski.loader.filesystemloader.service.quotes

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.QuotesTrimesterInformation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Serviço para importar cotações.
 */
@Component
class QuotesImporterService @Autowired constructor(private val quotesTrimesterImporter: QuotesTrimesterImporter) {

    fun importAll(
            cvmCode: String,
            stockCodes: List<String>,
            balanceTrimesterInformationSequence: List<BalanceTrimesterInformation>)
            : List<QuotesTrimesterInformation> {

        return stockCodes.flatMap { stockCode ->
            quotesTrimesterImporter.import(cvmCode, stockCode, balanceTrimesterInformationSequence)
        }
    }

}
