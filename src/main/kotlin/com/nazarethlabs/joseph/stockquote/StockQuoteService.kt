package com.nazarethlabs.joseph.stockquote

import com.nazarethlabs.joseph.core.port.StockQuoteProvider
import com.nazarethlabs.joseph.stock.Stock
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
        val stockWithoutQuote = this.getStockPendingQuoteForToday()
            ?: return StockQuotePendingResponse(
                message = "No stock found without quote for today."
            )

        val quoteResponse = this.getQuoteFromProvider(stockWithoutQuote)
            ?: return StockQuotePendingResponse(
                message = "No quote found for stock: ${stockWithoutQuote.ticker}."
            )

        val stockQuote = this.buildStockQuote(
            stock = stockWithoutQuote,
            quoteResponse = quoteResponse
        )

        this.save(stockQuote)

        return StockQuotePendingResponse(
            message = "Stock quote for ${stockWithoutQuote.ticker} updated successfully.",
        )
    }

    private fun getDateNow(): LocalDate {
        return LocalDate.now()
    }

    private fun save(stockQuote: StockQuote) {
        stockQuoteRepository.save(stockQuote)
    }

    private fun buildStockQuote(
        stock: Stock,
        quoteResponse: StockQuoteQueryResponse
    ): StockQuote {
        return StockQuote(
            stock = stock,
            quoteDate = this.getDateNow(),
            openPrice = quoteResponse.openPrice,
            highPrice = quoteResponse.highPrice,
            lowPrice = quoteResponse.lowPrice,
            closePrice = quoteResponse.closePrice,
            volume = quoteResponse.volume
        )
    }

    private fun getStockPendingQuoteForToday(): Stock? {
        val today = this.getDateNow()
        val stocks = stockRepository.findAll()
        val stockQuotes = this.getStockQuotesByDateAndStockIds(date = today, stocks = stocks)

        return stocks.firstOrNull { stock ->
            stockQuotes
                .none { quote -> quote.stock?.id == stock.id }
        }
    }

    private fun getStockQuotesByDateAndStockIds(date: LocalDate, stocks: List<Stock>): List<StockQuote> {
        val stockIds = stocks.mapNotNull { it.id }
        return stockQuoteRepository.findByStockIdInAndQuoteDate(stockIds, date)
    }

    private fun getQuoteFromProvider(stock: Stock): StockQuoteQueryResponse? {
        return stockQuoteProvider.getQuote(stock.ticker)
            .orElse(null)
    }
}
