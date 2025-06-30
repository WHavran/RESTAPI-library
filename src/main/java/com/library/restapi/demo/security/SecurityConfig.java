package com.library.restapi.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                """
                SELECT username, password, enabled
                FROM Users
                WHERE username=?
                """
        );

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                """
                SELECT u.username, r.role
                FROM Users u
                JOIN Role r ON r.user_id = u.user_id
                WHERE u.username =?
                """
        );

        return jdbcUserDetailsManager;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/api/user/myProfile", "/api/user/myProfile/*").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/api/reservation/reserve").hasAnyRole("ADMIN", "USER")

                                .requestMatchers("/api/user/**").hasRole("ADMIN")
                                .requestMatchers("/api/reservation/**").hasRole("ADMIN")
                                .requestMatchers("/api/*/all=archived").hasRole("ADMIN")

                                .requestMatchers(
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs.yaml"
                                ).permitAll()

                                .requestMatchers(
                                        "/api/book/*",
                                        "/api/author/*",
                                        "/api/*/all=active",
                                        "/api/*/all"
                                ).permitAll()

                                .anyRequest().authenticated()
                );
        return http.build();
    }
}
