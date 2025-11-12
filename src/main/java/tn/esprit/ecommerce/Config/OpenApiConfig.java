package tn.esprit.ecommerce.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationServiceOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8085");
        devServer.setDescription("Serveur de développement");

        Contact contact = new Contact();
        contact.setEmail("amine@tunishop.com");
        contact.setName("Amine - Tuni Shop");
        contact.setUrl("https://www.tunishop.com");

        License license = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Microservice Gestion Notifications - API Documentation")
                .version("1.0.0")
                .contact(contact)
                .description("API REST pour le microservice de gestion des notifications. " +
                        "Ce service gère l'envoi automatique d'emails pour les promotions et la disponibilité des produits.")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}

