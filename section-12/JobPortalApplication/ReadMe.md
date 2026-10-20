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

4. 