package com.nazarethlabs.joseph.stockquote

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
interface StockQuoteRepository : JpaRepository<StockQuote, UUID> {
    fun findByStockIdInAndQuoteDate(
        stockIds: List<UUID>,
        date: LocalDate,
    ): List<StockQuote>
}
