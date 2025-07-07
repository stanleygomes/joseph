package com.nazarethlabs.joseph.stockquote

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class StockQuoteQueryResponse(
    val openPrice: BigDecimal?,
    val highPrice: BigDecimal?,
    val lowPrice: BigDecimal?,
    val closePrice: BigDecimal,
    val volume: Long?,
)

@Schema(description = "Representa uma cotação de ação existente.")
data class StockQuoteResponse(
    val id: UUID,
    val quoteDate: LocalDate,
    val openPrice: BigDecimal?,
    val highPrice: BigDecimal?,
    val lowPrice: BigDecimal?,
    val closePrice: BigDecimal,
    val volume: Long?,
)

data class StockQuotePendingResponse(
    val message: String,
)
