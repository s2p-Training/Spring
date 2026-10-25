# Spring Security Configuration: API Paths & CORS Configuration

### 0. Making Companies And Contacts API As Public

- Add public suffix to GET API of companies inside CompanyController.java
```java
@GetMapping(path = "/public", version = "1.0")
	public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        // Previously Written Code
    }
```
-- Add public suffix to POST API of contacts inside ContactController.java
```java

	/// Applying Validation Logic Using @Valid Annotation
	@PostMapping(path = "/public",version = "1.0")
	public ResponseEntity<String> saveContactMsg(@Valid @RequestBody ContactRequestDto contactRequestDto) {
        //  Previously Written Code
    }
```


### 1. Separating The Public APIs & Secured APIs

- Create the file with name PathConfig.java, which will have all the API URI of public paths & private paths. 

```java
package com.s2p.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathsConfig {

    @Bean(name = "publicPaths")
    public List<String> publicPaths() {
        return List.of(
                "/api/companies/public",
                "/api/contacts/public",
                "/api/swagger-ui.html",
                "/swagger-ui/**",
                "/api/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }

    @Bean(name = "securedPaths")
    public List<String> securedPaths() {
        return List.of(
                "/api/**"
        );
    }

}
```

### 2. Register The API Paths Inside Custom Spring Security Filter Chain Bean
```java
@Configuration
@EnableWebSecurity
public class JobPortalSecurityConfig
{
    private List<String> publicPaths   = null;
    private List<String> securedPaths  = null;

    // Injecting The Beans Of Public API Paths And Secured API Paths
    @Autowired
    public JobPortalSecurityConfig(@Qualifier("publicPaths") List<String> publicPaths,@Qualifier("securedPaths") List<String> securedPaths)
    {
        this.publicPaths = publicPaths;
        this.securedPaths = securedPaths;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
        
        // Registering The Public API Paths & Secured API Paths
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

```

### 3. Disable Custom Spring CORS Filter, And Set Cors Configuration Via Spring Security Beans

- Go to config folder and comment, @Configueration & @Bean method, As Result, No Object Of CorsFilter will be provided to the Spring context.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;

// Disabled This Configuration, As Spring Security Has Its Own CORS Filter Bean, Which Overrides This Bean Inside Context.
//@Configuration
public class CorsConfig
{
//	@Bean
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
```

- Go to security/JobPortalSecurityConfig, Add New Configuration For CorsConfiguration By Writing The Private Method
```java
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
```

- For receiving data from forms, We need disable the CSRF protection.
- Inject the CORS Configuration inside spring ecosystem by adding the instance provided by the **corsConfigurationSource()** private method.
```java
 @Configuration
@EnableWebSecurity
public class JobPortalSecurityConfig {
    private List<String> publicPaths = null;
    private List<String> securedPaths = null;

    @Autowired
    public JobPortalSecurityConfig(@Qualifier("publicPaths") List<String> publicPaths, @Qualifier("securedPaths") List<String> securedPaths) {
        this.publicPaths = publicPaths;
        this.securedPaths = securedPaths;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {

        // Disable The CSRF Protection
        http.csrf(csrfConfig -> csrfConfig.disable());

        // Adding The Custom Spring Security Configuration Inside Spring Context
        http.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()));

        http.authorizeHttpRequests(
                (requests) -> {
                    publicPaths.forEach(path -> requests.requestMatchers(path).permitAll());
                    securedPaths.forEach(path -> requests.requestMatchers(path).authenticated());
                }
        );

        http.formLogin(flc -> flc.disable());
        http.httpBasic(withDefaults());
        return http.build();
    }
    
    // Private method to inject the bean of CorsConfiguration
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:4200"));
        corsConfig.setAllowedHeaders(Collections.singletonList("*"));
        corsConfig.setAllowedMethods(Collections.singletonList("*"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }
}

```