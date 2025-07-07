package com.nazarethlabs.joseph.stockquote

import com.nazarethlabs.joseph.core.port.StockQuoteProvider
import com.nazarethlabs.joseph.stock.Stock
import com.nazarethlabs.joseph.stock.StockRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class StockQuoteServiceTest {
    @Mock
    private lateinit var stockQuoteRepository: StockQuoteRepository

    @Mock
    private lateinit var stockRepository: StockRepository

    @Mock
    private lateinit var stockQuoteProvider: StockQuoteProvider

    @InjectMocks
    private lateinit var stockQuoteService: StockQuoteService

    private val stockId = UUID.randomUUID()
    private val stock = Stock(id = stockId, ticker = "PETR4", companyName = "Petrobras")

    @Nested
    @DisplayName("updatePendingDayQuote")
    inner class UpdatePendingDayQuote {
        @Test
        fun `deve retornar mensagem de sucesso ao atualizar cotação pendente`() {
            val today = LocalDate.now()
            val quoteResponse =
                StockQuoteQueryResponse(
                    openPrice = BigDecimal("10.00"),
                    highPrice = BigDecimal("12.00"),
                    lowPrice = BigDecimal("9.50"),
                    closePrice = BigDecimal("11.00"),
                    volume = 1000L,
                )
            val stockQuote =
                StockQuote(
                    stock = stock,
                    quoteDate = today,
                    openPrice = quoteResponse.openPrice,
                    highPrice = quoteResponse.highPrice,
                    lowPrice = quoteResponse.lowPrice,
                    closePrice = quoteResponse.closePrice,
                    volume = quoteResponse.volume,
                )
            `when`(stockRepository.findAll()).thenReturn(listOf(stock))
            `when`(stockQuoteRepository.findByStockIdInAndQuoteDate(listOf(stockId), today)).thenReturn(emptyList())
            `when`(stockQuoteProvider.getQuote(stock.ticker)).thenReturn(Optional.of(quoteResponse))
            `when`(stockQuoteRepository.save(any())).thenReturn(stockQuote)

            val result = stockQuoteService.updatePendingDayQuote()

            assertEquals("Stock quote for PETR4 updated successfully.", result.message)
            verify(stockQuoteRepository).save(any())
        }

        @Test
        fun `deve retornar mensagem quando não há ação pendente para cotação`() {
            whenever(stockRepository.findAll()).thenReturn(emptyList())

            val result = stockQuoteService.updatePendingDayQuote()

            assertEquals("No stock found without quote for today.", result.message)
        }

        @Test
        fun `deve retornar mensagem quando não encontra cotação para a ação`() {
            val today = LocalDate.now()
            whenever(stockRepository.findAll()).thenReturn(listOf(stock))
            whenever(stockQuoteRepository.findByStockIdInAndQuoteDate(listOf(stockId), today)).thenReturn(emptyList())
            whenever(stockQuoteProvider.getQuote(stock.ticker)).thenReturn(Optional.empty())

            val result = stockQuoteService.updatePendingDayQuote()

            assertEquals("No quote found for stock: PETR4.", result.message)
        }

        @Test
        fun `deve retornar mensagem quando todas as ações já possuem cotação para hoje`() {
            val today = LocalDate.now()
            val quoteResponse =
                StockQuoteQueryResponse(
                    openPrice = BigDecimal("10.00"),
                    highPrice = BigDecimal("12.00"),
                    lowPrice = BigDecimal("9.50"),
                    closePrice = BigDecimal("11.00"),
                    volume = 1000L,
                )
            val stockQuote =
                StockQuote(
                    stock = stock,
                    quoteDate = today,
                    openPrice = quoteResponse.openPrice,
                    highPrice = quoteResponse.highPrice,
                    lowPrice = quoteResponse.lowPrice,
                    closePrice = quoteResponse.closePrice,
                    volume = quoteResponse.volume,
                )
            whenever(stockRepository.findAll()).thenReturn(listOf(stock))
            whenever(
                stockQuoteRepository.findByStockIdInAndQuoteDate(listOf(stockId), today),
            ).thenReturn(listOf(stockQuote))

            val result = stockQuoteService.updatePendingDayQuote()

            assertEquals("No stock found without quote for today.", result.message)
        }
    }
}
