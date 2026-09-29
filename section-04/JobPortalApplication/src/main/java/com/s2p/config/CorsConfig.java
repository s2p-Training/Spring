package com.s2p.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig
{
	@Bean
	public CorsFilter corsFilter()
	{
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:5137","http://localhost:4200"));
		config.setAllowedOriginPatterns(List.of("*")); // For Testing Only
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(List.of("Content-Type"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**",config);

		CorsFilter corsFilter = new CorsFilter(source);
		return corsFilter;
	}
}
