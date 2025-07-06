package com.nazarethlabs.joseph.core.port

import com.nazarethlabs.joseph.integration.brapi.BrapiQuoteResult
import java.util.Optional

interface StockQuoteProvider {
    fun getQuotes(tickers: List<String>): List<BrapiQuoteResult>

    fun getQuote(ticker: String): Optional<BrapiQuoteResult>
}
