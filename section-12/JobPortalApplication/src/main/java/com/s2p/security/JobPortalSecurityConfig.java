package com.s2p.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class JobPortalSecurityConfig
{
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(
                (requests)->{
//                    requests.anyRequest().authenticated(); // Going to authenticate every request
//                    requests.anyRequest().permitAll(); // Going to allow every request without any credentials
//                    requests.anyRequest().denyAll(); // Going to deny every request with / without credentials

                    requests.anyRequest().permitAll(); // Going to allow every request without any credentials

                }
        );

        // Disable The CSRF Protection
//        http.csrf(csrfConfig->csrfConfig.disable());
        http.formLogin(flc->flc.disable());
        http.httpBasic(withDefaults());
        return http.build();
    }

}
