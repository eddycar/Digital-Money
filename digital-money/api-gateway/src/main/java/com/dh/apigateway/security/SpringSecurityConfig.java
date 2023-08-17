package com.dh.apigateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtAuthConverter authConverter;

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        http
                .authorizeExchange(authExch -> authExch.pathMatchers(HttpMethod.POST, "/users", "/login/**").permitAll()
                        .pathMatchers("/cards/**").hasRole("CLIENT_USER")
                        .pathMatchers("/accounts/**").hasRole("CLIENT_USER")
                        .pathMatchers("/transactions/**").hasRole("CLIENT_USER")
                        .anyExchange().permitAll())
                //.oauth2ResourceServer(oAuth -> oAuth.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(authConverter)))
                .oauth2ResourceServer(oAuth -> oAuth.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(new ReactiveJwtAuthenticationConverterAdapter(authConverter))))
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
