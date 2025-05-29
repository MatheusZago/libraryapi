package com.matheusluizago.libraryapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Library api",
                version = "v1",
                contact = @Contact(
                        name = "Matheus",
                        email = "matheuszago134@gmail.com",
                        url = "libraryapi.com"
                )
        )
)
public class OpenApiConfiguration {
}
