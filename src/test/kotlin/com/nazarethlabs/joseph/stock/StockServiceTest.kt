package com.nazarethlabs.joseph.stock

import com.nazarethlabs.joseph.core.exceptions.ResourceNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class StockServiceTest {

    @Mock
    private lateinit var stockRepository: StockRepository

    @InjectMocks
    private lateinit var stockService: StockService

    @Nested
    @DisplayName("createStock")
    inner class CreateStock {
        @Test
        fun `deve criar e salvar uma ação com sucesso`() {
            val request = CreateStockRequest(ticker = "MGLU3", companyName = "Magazine Luiza")
            val stockToBeSaved = Stock(
                id = UUID.randomUUID(),
                ticker = request.ticker,
                companyName = request.companyName
            )

            `when`(stockRepository.save(any())).thenReturn(stockToBeSaved)

            val result = stockService.createStock(request)

            assertEquals(stockToBeSaved.id, result.id)
            assertEquals("MGLU3", result.ticker)
            assertEquals("Magazine Luiza", result.companyName)

            verify(stockRepository).save(any())
        }
    }

    @Nested
    @DisplayName("getAllStocks")
    inner class GetAllStocks {
        @Test
        fun `deve retornar uma lista de ações quando houver ações cadastradas`() {
            val stocksFromRepo = listOf(
                Stock(id = UUID.randomUUID(), ticker = "PETR4", companyName = "Petrobras"),
                Stock(id = UUID.randomUUID(), ticker = "VALE3", companyName = "Vale")
            )
            whenever(stockRepository.findAll()).thenReturn(stocksFromRepo)

            val result = stockService.getAllStocks()

            assertEquals(2, result.size, "A lista de resultados deve conter 2 ações")
            assertEquals("PETR4", result[0].ticker)
            assertEquals("Vale", result[1].companyName)
            assertEquals(stocksFromRepo[0].id, result[0].id)
            assertEquals(stocksFromRepo[1].id, result[1].id)

            verify(stockRepository).findAll()
        }

        @Test
        fun `deve retornar uma lista vazia quando não houver ações cadastradas`() {
            whenever(stockRepository.findAll()).thenReturn(emptyList())

            val result = stockService.getAllStocks()

            assertEquals(0, result.size, "A lista de resultados deve estar vazia")
            verify(stockRepository).findAll()
        }
    }

    @Nested
    @DisplayName("getStockById")
    inner class GetStockById {
        @Test
        fun `deve retornar uma ação quando o ID existir`() {
            val stockId = UUID.randomUUID()
            val stock = Stock(id = stockId, ticker = "ITSA4", companyName = "Itaúsa")
            whenever(stockRepository.findById(stockId)).thenReturn(Optional.of(stock))

            val result = stockService.getStockById(stockId)

            assertEquals(stockId, result.id)
            assertEquals("ITSA4", result.ticker)
        }

        @Test
        fun `deve lançar ResourceNotFoundException quando o ID não existir`() {
            val nonExistentId = UUID.randomUUID()
            whenever(stockRepository.findById(nonExistentId)).thenReturn(Optional.empty())

            val exception = assertThrows<ResourceNotFoundException> {
                stockService.getStockById(nonExistentId)
            }
            assertEquals("Stock with ID $nonExistentId not found", exception.message)
        }
    }

    @Nested
    @DisplayName("deleteStock")
    inner class DeleteStock {
        @Test
        fun `deve marcar uma ação como deletada quando o ID existir`() {
            val stockId = UUID.randomUUID()
            val stock = Stock(id = stockId, ticker = "BBDC4", companyName = "Bradesco")
            whenever(stockRepository.findById(stockId)).thenReturn(Optional.of(stock))

            stockService.deleteStock(stockId)

            val stockCaptor = ArgumentCaptor.forClass(Stock::class.java)
            verify(stockRepository).save(stockCaptor.capture())

            val savedStock = stockCaptor.value
            assertNotNull(savedStock.deletedAt)
            assertEquals(stockId, savedStock.id)
        }

        @Test
        fun `deve lançar ResourceNotFoundException ao tentar deletar um ID que não existe`() {
            val nonExistentId = UUID.randomUUID()
            whenever(stockRepository.findById(nonExistentId)).thenReturn(Optional.empty())

            assertThrows<ResourceNotFoundException> {
                stockService.deleteStock(nonExistentId)
            }
        }
    }
}
