package com.gatorion.backend.security;

import com.gatorion.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor // Anotação do Lombok que cria um construtor com todos os campos 'final'
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/usuarios/cadastrar"
    );

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Pular autenticação JWT para endpoints públicos
        String requestPath = request.getRequestURI();
        if (isPublicUrl(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extrai o token do cabeçalho (removendo o "Bearer ")
        jwt = authHeader.substring(7);

        // 3. Extrai o email do utilizador do token usando o nosso JwtService
        userEmail = jwtService.extractUsername(jwt);

        // 4. Verifica se o email existe e se o utilizador ainda não está autenticado
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do utilizador a partir do banco de dados
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Valida o token com os detalhes do utilizador
            if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                // Se o token for válido, cria um objeto de autenticação
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Não precisamos das credenciais (password) aqui
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // 6. ATUALIZA O CONTEXTO DE SEGURANÇA
                // Informa ao Spring Security que este utilizador está agora autenticado
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Passa o pedido para o próximo filtro na cadeia
        filterChain.doFilter(request, response);
    }

    private boolean isPublicUrl(String requestPath) {
        return PUBLIC_URLS.stream().anyMatch(publicUrl ->
                requestPath.equals(publicUrl) || requestPath.startsWith(publicUrl.replace("*", ""))
        );
    }
}