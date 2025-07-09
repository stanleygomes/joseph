package com.nazarethlabs.joseph.stockquote

import com.nazarethlabs.joseph.core.port.StockQuoteProvider
import com.nazarethlabs.joseph.stock.StockEntity
import com.nazarethlabs.joseph.stock.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class StockQuoteService(
    private val stockQuoteRepository: StockQuoteRepository,
    private val stockRepository: StockRepository,
    private val stockQuoteProvider: StockQuoteProvider,
) {
    @Transactional
    fun updatePendingDayQuote(): StockQuotePendingResponse {
        val stockWithoutQuote =
            this.getStockPendingQuoteForToday()
                ?: return StockQuotePendingResponse(
                    message = "No stock found without quote for today.",
                )

        val quoteResponse =
            this.getQuoteFromProvider(stockWithoutQuote)
                ?: return StockQuotePendingResponse(
                    message = "No quote found for stock: ${stockWithoutQuote.ticker}.",
                )

        val stockQuote =
            this.buildStockQuote(
                stockEntity = stockWithoutQuote,
                quoteResponse = quoteResponse,
            )

        this.save(stockQuote)

        return StockQuotePendingResponse(
            message = "Stock quote for ${stockWithoutQuote.ticker} updated successfully.",
        )
    }

    private fun getDateNow(): LocalDate = LocalDate.now()

    private fun save(stockQuoteEntity: StockQuoteEntity) {
        stockQuoteRepository.save(stockQuoteEntity)
    }

    private fun buildStockQuote(
        stockEntity: StockEntity,
        quoteResponse: StockQuoteQueryResponse,
    ): StockQuoteEntity =
        StockQuoteEntity(
            stockEntity = stockEntity,
            quoteDate = this.getDateNow(),
            openPrice = quoteResponse.openPrice,
            highPrice = quoteResponse.highPrice,
            lowPrice = quoteResponse.lowPrice,
            closePrice = quoteResponse.closePrice,
            volume = quoteResponse.volume,
        )

    private fun getStockPendingQuoteForToday(): StockEntity? {
        val today = this.getDateNow()
        val stocks = stockRepository.findAll()
        val stockQuotes = this.getStockQuotesByDateAndStockIds(date = today, stockEntities = stocks)

        return stocks.firstOrNull { stock ->
            stockQuotes
                .none { quote -> quote.stockEntity!!.id == stock.id }
        }
    }

    private fun getStockQuotesByDateAndStockIds(
        date: LocalDate,
        stockEntities: List<StockEntity>,
    ): List<StockQuoteEntity> {
        val stockIds = stockEntities.mapNotNull { it.id }
        return stockQuoteRepository.findByStockIdInAndQuoteDate(stockIds, date)
    }

    private fun getQuoteFromProvider(stockEntity: StockEntity): StockQuoteQueryResponse? =
        stockQuoteProvider
            .getQuote(stockEntity.ticker)
            .orElse(null)
}
