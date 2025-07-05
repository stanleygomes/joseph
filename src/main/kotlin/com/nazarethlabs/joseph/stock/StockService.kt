package com.nazarethlabs.joseph.stock

import com.nazarethlabs.joseph.core.exceptions.ResourceNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant.now
import java.util.UUID

@Service
class StockService(
    private val stockRepository: StockRepository,
) {
    fun createStock(request: CreateStockRequest): StockResponse {
        val stock = Stock(ticker = request.ticker, companyName = request.companyName)
        val savedStock = stockRepository.save(stock)
        return savedStock.toResponse()
    }

    fun getAllStocks(): List<StockResponse> = stockRepository.findAll().map { it.toResponse() }

    fun getStockById(id: UUID): StockResponse {
        val stock = stockRepository.findByIdOrNull(id)
            ?: throw ResourceNotFoundException("Stock with ID $id not found")
        return stock.toResponse()
    }

    fun deleteStock(id: UUID) {
        val stockToDelete = stockRepository.findByIdOrNull(id)
            ?: throw ResourceNotFoundException("Stock with ID $id not found")

        stockToDelete.deletedAt = now()
        stockRepository.save(stockToDelete)
    }

    private fun Stock.toResponse(): StockResponse =
        StockResponse(
            id = this.id!!,
            ticker = this.ticker,
            companyName = this.companyName
        )
}
