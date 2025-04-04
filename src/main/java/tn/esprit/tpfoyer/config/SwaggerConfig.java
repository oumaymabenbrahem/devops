package tn.esprit.tpfoyer.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "TP Foyer API",
                version = "1.0",
                description = "Documentation de l'API pour la gestion des foyers"
        )
)
public class SwaggerConfig {
        // Pas besoin de code supplémentaire ici pour Swagger de base
}
