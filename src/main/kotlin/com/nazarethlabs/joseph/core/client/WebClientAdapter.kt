package com.nazarethlabs.joseph.core.client

import com.nazarethlabs.joseph.core.exceptions.IntegrationException
import org.slf4j.LoggerFactory
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class WebClientAdapter(
    private val webClient: WebClient,
) : HttpClient {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun <T> get(
        path: String,
        responseType: Class<T>,
        pathVariables: Map<String, Any>,
        queryParams: MultiValueMap<String, String>
    ): T? {
        try {
            return webClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .path(path)
                        .queryParams(queryParams)
                        .build(pathVariables)
                }
                .retrieve()
                .onStatus(
                    { it.isError },
                    { clientResponse ->
                        clientResponse.bodyToMono<Map<String, Any>>()
                            .flatMap { errorBody ->
                                val message = errorBody["message"]?.toString() ?: "Unknown API error"
                                Mono.error(IntegrationException("API Error: $message"))
                            }
                    }
                )
                .bodyToMono(responseType)
                .block()
        } catch (e: Exception) {
            val errorMessage = "Failed to execute GET request to path '$path'"
            logger.error(errorMessage, e)

            if (e is IntegrationException) {
                throw e
            }

            throw IntegrationException(errorMessage, e)
        }
    }
}
