package tn.esprit.tpfoyer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Autoriser toutes les origines (vous pouvez restreindre cela à votre domaine frontend)
        config.addAllowedOrigin("*");
        
        // Autoriser tous les en-têtes
        config.addAllowedHeader("*");
        
        // Autoriser toutes les méthodes (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
