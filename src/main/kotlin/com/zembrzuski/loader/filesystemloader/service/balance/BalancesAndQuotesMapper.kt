package com.zembrzuski.loader.filesystemloader.service.balance

import com.zembrzuski.loader.filesystemloader.domain.firebase.BalanceTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.Quote
import com.zembrzuski.loader.filesystemloader.domain.firebase.QuotesTrimesterInformation
import com.zembrzuski.loader.filesystemloader.domain.firebase.TrimesterInformation
import org.springframework.stereotype.Component

/**
 * Cria as informações trimestrais mais fundamentais através da junção dos balanços
 * com as informações de cotações.
 */
@Component
class BalancesAndQuotesMapper {

    fun joinBalancesToQuotes(
            trimesterInformationSequence: List<BalanceTrimesterInformation>,
            quotes: List<QuotesTrimesterInformation>)
            : List<TrimesterInformation> {

        return trimesterInformationSequence.map { trimester ->
            TrimesterInformation(
                    balanceTrimesterInformation = trimester,
                    quoteRealPrice = getQuoteRealValue(quotes, trimester),
                    quoteAdjustedPrice = getQuoteAdjustedlValue(quotes, trimester)
            )
        }
    }

    private fun getQuoteRealValue(
            quotes: List<QuotesTrimesterInformation>,
            trimester: BalanceTrimesterInformation)
            : List<Quote> {

        return quotes
                .filter { quote -> quote.trimester == trimester.trimester && quote.year == trimester.year }
                .map { quote -> Quote(quote.stockCode, quote.realValue) }
    }

    private fun getQuoteAdjustedlValue(
            quotes: List<QuotesTrimesterInformation>,
            trimester: BalanceTrimesterInformation)
            : List<Quote> {

        return quotes
                .filter { quote -> quote.trimester == trimester.trimester && quote.year == trimester.year }
                .map { quote -> Quote(quote.stockCode, quote.adjustedValue) }
    }

}
