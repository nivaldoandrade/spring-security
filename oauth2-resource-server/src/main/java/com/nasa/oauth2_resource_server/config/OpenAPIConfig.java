package com.nasa.oauth2_resource_server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {

        Contact contact = new Contact();

        contact.setName("Nivaldo Andrade");
        contact.setEmail("nivaldoandradef@gmail.com");
        contact.setUrl("https://nivaldoandrade.dev.br");

        Info info = new Info();
        info.setTitle("Authentication API");
        info.version("1.0");
        info.contact(contact);

        String securitySchemaName = "Authorization";


        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemaName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemaName, new SecurityScheme()
                                .name(securitySchemaName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
    }
}
