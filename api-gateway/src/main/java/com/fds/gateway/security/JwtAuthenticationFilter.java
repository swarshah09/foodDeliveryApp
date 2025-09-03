package com.fds.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final SecretKey secretKey;
    private final List<String> publicPaths = List.of(
            "/auth/**",
            "/actuator/**",
            "/eureka/**"
    );

    public JwtAuthenticationFilter(@Value("${security.jwt.secret:supersecretjwtkeychangeme}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        boolean isPublic = publicPaths.stream().anyMatch(path::startsWith);
        if (isPublic) {
            return chain.filter(exchange);
        }

        List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null || authHeaders.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String token = authHeaders.get(0);
        if (token.toLowerCase().startsWith("bearer ")) {
            token = token.substring(7);
        }

        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            String userId = String.valueOf(claims.get("sub"));
            String role = String.valueOf(claims.getOrDefault("role", "USER"));
            Map<String, Object> extra = Map.of(
                    "X-User-Id", userId,
                    "X-User-Role", role
            );
            ServerHttpRequest mutated = exchange.getRequest().mutate()
                    .headers(httpHeaders -> extra.forEach((k, v) -> httpHeaders.set(k, String.valueOf(v))))
                    .build();
            return chain.filter(exchange.mutate().request(mutated).build());
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

