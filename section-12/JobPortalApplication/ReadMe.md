# Introduction To Spring Security

1. Add The Spring Security Dependency
```xml
    <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
```

2. Adding Static User Credentials For Accessing The APIs
```properties
spring.security.user.name=root
spring.security.user.password=root
spring.security.user.roles=USER,ADMIN
```

3. Pre-Defined Spring Security Configuration With Zero Configuration
```java
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnDefaultWebSecurity
    static class SecurityFilterChainConfiguration {
    
        @Bean
        @Order(SecurityFilterProperties.BASIC_AUTH_ORDER)
        SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
            http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
            http.formLogin(withDefaults());
            http.httpBasic(withDefaults());
            return http.build();
        }
    
    }
```

4. Define The SpringSecurityFilterChain Bean With Custom Configuration
```java

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

        http.formLogin(flc->flc.disable());
        http.httpBasic(withDefaults());
        return http.build();
    }

}
```

5. For Invoking The POST APIs, Disable The CSRF Protection, Using SpringSecurity's SecurityFilterChain Bean Configuration
```java
    @Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
    
    
    // Before Returning The Instance Of SecurityFilterChain.
    http.csrf(csrfConfig->csrfConfig.disable());
    
}
```