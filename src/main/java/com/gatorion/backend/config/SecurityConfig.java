package com.gatorion.backend.config;

import com.gatorion.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Desabilitar o CSRF (Cross-Site Request Forgery)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Definir as regras de autorização para cada endpoint
                .authorizeHttpRequests(auth -> auth
                        // Nossos endpoints públicos que não precisam de autenticação
                        .requestMatchers("/auth/**", "/usuarios/cadastrar").permitAll()

                        // Qualquer outro pedido (anyRequest) precisa de ser autenticado
                        .anyRequest().authenticated()
                )

                // 3. Configurar a gestão da sessão para ser "sem estado" (stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Ligar o nosso provedor de autenticação
                .authenticationProvider(authenticationProvider)

                // 5. Adicionar o nosso filtro de JWT para ser executado ANTES do filtro padrão de autenticação
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}