package com.example.user2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableDiscoveryClient
@SpringBootApplication
public class User2Application {

    public static void main(String[] args) {
        SpringApplication.run(User2Application.class, args);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200"); // Remplacez par l'URL de votre application Angular
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
/*En résumé, CorsFilter est une méthode plus directe pour configurer CORS
de manière autonome, tandis que CorsConfigurationSource
est utilisé pour une configuration plus intégrée avec la sécurité de Spring.
 L'utilisation de CorsConfigurationSource avec SecurityFilterChain
 est souvent préférée pour une gestion plus fluide et centralisée des configurations de sécurité et CORS.*/