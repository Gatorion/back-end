package com.gatorion.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed.origins:http://localhost,http://localhost:80,http://localhost:3000,http://localhost:5173}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // Mais flexível que allowedOrigins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir origens específicas baseadas em propriedades
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins);
        
        // Adicionar origens padrão para desenvolvimento
        configuration.addAllowedOrigin("http://localhost");
        configuration.addAllowedOrigin("http://localhost:80");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://127.0.0.1");
        configuration.addAllowedOrigin("http://127.0.0.1:80");
        configuration.addAllowedOrigin("http://127.0.0.1:3000");
        
        // Para produção AWS Beanstalk
        configuration.addAllowedOrigin("http://gatorion.us-east-1.elasticbeanstalk.com");
        configuration.addAllowedOrigin("https://gatorion.us-east-1.elasticbeanstalk.com");

        // Para produção Vercel
        configuration.addAllowedOrigin("https://gatorion-frontend.vercel.app");
        configuration.addAllowedOrigin("http://gatorion-frontend.vercel.app");
        configuration.addAllowedOrigin("https://gatorion-frontend-nktfi6lu1-rafaels-projects-8b5a84d3.vercel.app/");
        configuration.addAllowedOrigin("http://gatorion-frontend-nktfi6lu1-rafaels-projects-8b5a84d3.vercel.app/");

        // Permitir métodos HTTP essenciais
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        
        // Headers essenciais para APIs REST
        configuration.setAllowedHeaders(Arrays.asList(
            "Origin",
            "Content-Type", 
            "Accept",
            "Authorization",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "X-Requested-With",
            "Cache-Control"
        ));
        
        // Headers expostos para o frontend
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Authorization",
            "Content-Disposition"
        ));
        
        // Permitir credenciais (importante para autenticação)
        configuration.setAllowCredentials(true);
        
        // Cache preflight requests por 1 hora
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}