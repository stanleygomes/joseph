package com.nazarethlabs.joseph.config

import com.nazarethlabs.joseph.core.client.HttpClient
import com.nazarethlabs.joseph.core.client.WebClientAdapter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Value("\${integration.brapi.base-url}")
    private lateinit var brapiBaseUrl: String

    @Value("\${integration.brapi.token}")
    private lateinit var brapiToken: String

    @Bean
    fun brapiWebClient(builder: WebClient.Builder): WebClient {
        return builder
            .baseUrl(brapiBaseUrl)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer $brapiToken")
            .build()
    }

    @Bean
    @Qualifier("brapiHttpClient")
    fun brapiHttpClient(
        @Qualifier("brapiWebClient") webClient: WebClient,
    ): HttpClient {
        return WebClientAdapter(webClient)
    }
}
