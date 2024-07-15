package ru.skillbox.mcpost.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openApiDescription() {
        Contact devContacts = new Contact();
        devContacts.setEmail("dorzhogun@yandex.ru");
        devContacts.setName("Dorzho Tsyrenov");
        devContacts.setUrl("https://github.com/dorzhogun");
        Info info = new Info();
        info.setTitle("Post microservice, part of the social network");
        info.setVersion("1.0.0");
        info.setContact(devContacts);
        return new OpenAPI().info(info);
    }
}


