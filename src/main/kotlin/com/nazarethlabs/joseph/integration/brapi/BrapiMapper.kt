package com.nazarethlabs.joseph.integration.brapi

import com.nazarethlabs.joseph.stockquote.StockQuoteQueryResponse
import java.math.BigDecimal

fun BrapiQuoteResult.toCore(): StockQuoteQueryResponse {
    return StockQuoteQueryResponse(
        openPrice = this.regularMarketOpen ?: BigDecimal.ZERO,
        highPrice = this.regularMarketDayHigh ?: BigDecimal.ZERO,
        lowPrice = this.regularMarketDayLow ?: BigDecimal.ZERO,
        closePrice = this.regularMarketPrice ?: BigDecimal.ZERO,
        volume = this.regularMarketVolume
    )
}
