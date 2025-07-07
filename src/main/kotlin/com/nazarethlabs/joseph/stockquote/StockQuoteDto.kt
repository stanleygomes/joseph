package com.nazarethlabs.joseph.stockquote

import java.math.BigDecimal

data class StockQuoteQueryResponse(
    val openPrice: BigDecimal?,
    val highPrice: BigDecimal?,
    val lowPrice: BigDecimal?,
    val closePrice: BigDecimal?,
    val volume: Long?,
)

data class StockQuotePendingResponse(
    val message: String,
)
