package com.nazarethlabs.joseph.integration.brapi

import com.nazarethlabs.joseph.core.client.HttpClient
import com.nazarethlabs.joseph.core.port.StockQuoteProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class BrapiClient(
    @Qualifier("brapiHttpClient")
    private val client: HttpClient,
) : StockQuoteProvider {

    override fun getQuotes(tickers: List<String>): List<BrapiQuoteResult> {
        if (tickers.isEmpty()) {
            return emptyList()
        }

        val tickersParam = tickers.joinToString(separator = ",")

        val response = client.get(
            path = "/quote/{tickers}",
            responseType = BrapiQuoteResponse::class.java,
            pathVariables = mapOf("tickers" to tickersParam)
        )

        return response?.results ?: emptyList()
    }

    override fun getQuote(ticker: String): Optional<BrapiQuoteResult> {
        return getQuotes(listOf(ticker))
            .firstOrNull()
            .let {
                Optional.ofNullable(it)
            }
    }
}
