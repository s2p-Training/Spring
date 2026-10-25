package com.s2p.security;

import com.s2p.config.CorsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class JobPortalSecurityConfig
{
    private List<String> publicPaths   = null;
    private List<String> securedPaths  = null;

    @Autowired
    public JobPortalSecurityConfig(@Qualifier("publicPaths") List<String> publicPaths,@Qualifier("securedPaths") List<String> securedPaths)
    {
        this.publicPaths = publicPaths;
        this.securedPaths = securedPaths;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {

        // Disable The CSRF Protection
        http.csrf(csrfConfig->csrfConfig.disable());

        // Adding The Custom Spring Security Configuration Inside Spring Context
        http.cors(corsConfigurer->corsConfigurer.configurationSource(corsConfigurationSource()));

        http.authorizeHttpRequests(
                (requests)->{
                    publicPaths.forEach(path->requests.requestMatchers(path).permitAll());
                    securedPaths.forEach(path->requests.requestMatchers(path).authenticated());
                }
        );

        http.formLogin(flc->flc.disable());
        http.httpBasic(withDefaults());
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:4200"));
        corsConfig.setAllowedHeaders(Collections.singletonList("*"));
        corsConfig.setAllowedMethods(Collections.singletonList("*"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfig);

        return source;
    }

}
