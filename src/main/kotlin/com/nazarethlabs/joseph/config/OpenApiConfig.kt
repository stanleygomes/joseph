package com.nazarethlabs.joseph.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig(
    @Value("\${application.info.title}")
    private val title: String,

    @Value("\${application.info.version}")
    private val version: String,

    @Value("\${application.info.description}")
    private val description: String,
) {
    private fun buildApiInfo(): Info {
        return Info()
            .title(title)
            .version(version)
            .description(description)
    }

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI().info(this.buildApiInfo())
    }
}
