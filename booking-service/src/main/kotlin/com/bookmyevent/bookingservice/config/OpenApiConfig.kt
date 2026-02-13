package com.bookmyevent.bookingservice.config

import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("BookMyEvent Booking API")
                    .version("v1")
                    .description("Booking service APIs for the BookMyEvent assignment")
            )
    }
}

