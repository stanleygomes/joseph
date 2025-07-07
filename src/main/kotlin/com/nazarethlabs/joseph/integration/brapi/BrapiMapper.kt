package com.nazarethlabs.joseph.integration.brapi

import com.nazarethlabs.joseph.stockquote.StockQuoteQueryResponse

fun BrapiQuoteResult.toCore(): StockQuoteQueryResponse {
    return StockQuoteQueryResponse(
        openPrice = this.regularMarketOpen,
        highPrice = this.regularMarketDayHigh,
        lowPrice = this.regularMarketDayLow,
        closePrice = this.regularMarketPrice,
        volume = this.regularMarketVolume
    )
}
