# Understanding Authentication Process In SpringBoot

1. Creating The Bean Of AuthenticationManager, To See How Authentication Handled In Spring Security
```java
    @Bean
    public AuthenticationManager authenticationManager()
    {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        return providerManager;
    }
```

2. Inject The Bean Of AuthenticationManager Inside AuthController & Use It To Authenticate The User, When Login API Is Called.
```java
    package com.s2p.controller;

import com.s2p.dto.LoginRequestDto;
import com.s2p.dto.LoginResponseDto;
import com.s2p.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping(path = "/auth")
public class AuthController
{

    private AuthenticationManager authenticationManager = null;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    // http://localhost:8080/auth/public
    // Body: loginRequest JSON Structure
    @PostMapping(value = "/login/public", version = "1.0")
    public ResponseEntity<LoginResponseDto> apiLogin(@RequestBody LoginRequestDto loginRequestDto)
    {
        try
        {
            String username = loginRequestDto.getUsername();
            String password = loginRequestDto.getPassword();

            Authentication authentication = new UsernamePasswordAuthenticationToken(username,password);

            Authentication resultAuthentication = authenticationManager.authenticate(authentication);

            UserDto userDto = new UserDto();

            // Sending The Dummy User Data
            userDto.setUserId(104L);
            userDto.setName("John Doe");
            userDto.setEmail("john@gmail.com");
            userDto.setMobileNumber("+1 4155550143");
            userDto.setCompanyId(2L);
            userDto.setCompanyName("Microsoft Corporation");
            userDto.setRole("ROLE_JOBSEEKER");
            userDto.setCreatedAt(Instant.now());

            // Sending Dummy JWT Token
            String token = "TestToken@aexdsyr@34fsd500";

            // Dummy Message At User End
            String message  = "Login Successful";
            LoginResponseDto responseDto = new LoginResponseDto(message,userDto,token);

            HttpStatus statusCode = HttpStatus.OK;
            ResponseEntity<LoginResponseDto> response = new ResponseEntity<>(responseDto,statusCode);

            return response;
        }
        catch (BadCredentialsException e)
        {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,"Invalid username or password");
        }
        catch (AuthenticationException e)
        {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,"Authentication failed");
        }
        catch (Exception e)
        {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred");
        }

    }

    private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status,String message)
    {
        LoginResponseDto responseDto = new LoginResponseDto(message,null,null);
        ResponseEntity<LoginResponseDto> response = new ResponseEntity<>(responseDto,status);
        return response;
    }
}
```

# Understanding The JWT Tokens In Spring Boot

1. Update pom.xml file, These dependencies are needed for JWT Token Generation.
```xml
<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-api</artifactId>
			<version>0.13.0</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-impl</artifactId>
			<version>0.13.0</version>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId>
			<version>0.13.0</version>
			<scope>runtime</scope>
		</dependency>
```

2. Inside security package create new package with name util, inside util package create the JwtUtil file & Write the code for JWT Token Generation
```java
package com.s2p.security.util;

import com.s2p.constants.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtUtil
{
    private Environment environment = null;

    @Autowired
    public JwtUtil(Environment environment)
    {
        this.environment = environment;
    }

    public String generateJwtToken(Authentication authentication)
    {
        String jwtToken = null;

        String secret = environment.getProperty(ApplicationConstants.JWT_SECRET_KEY,ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);

        byte[] secretByteBuffer = secret.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = Keys.hmacShaKeyFor(secretByteBuffer);

        User fetchedUser = (User) authentication.getPrincipal();

        String username = fetchedUser.getUsername();
        Collection<GrantedAuthority> rolesList =  fetchedUser.getAuthorities();
        String sAuthorities =  commaSeparatedRoles(rolesList);

        jwtToken =  Jwts
                .builder()
                .subject("JWT Token")
                .claim("username",username)
                .claim("roles",sAuthorities)
                .issuer("S2P EduTech")
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 86400000L))
                .signWith(secretKey)
                .compact();

        return jwtToken;
    }

    private String commaSeparatedRoles(Collection<GrantedAuthority> authorities)
    {
        LinkedList<String> rolesList = new LinkedList<>();

        for (GrantedAuthority authority : authorities) {
            String sRole = authority.getAuthority();
            rolesList.add(sRole);
        }

        String sAuthorities = String.join(",", rolesList);
        return sAuthorities;
    }

}
```

3. Creating The Filter, Which Will Fetch The Token From Request And Validate.
```java
package com.s2p.security.filter;

import com.s2p.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter
{
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Qualifier("publicPaths")
    private final List<String> publicPaths;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String authHeader = request.getHeader(ApplicationConstants.JWT_HEADER);
        if(null != authHeader)
        {
            try
            {
                // Extract the JWT token
                // Whoever bears (holds) the token is trusted and can access the protected resource.
                String jwtToken = authHeader.substring(7); // Remove 'Bearer ' prefix
                Environment env = getEnvironment();

                if(null != env)
                {
                    String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

                    if(null != secretKey)
                    {
                        Claims claims = Jwts.parser()
                                .verifyWith(secretKey)
                                .build().parseSignedClaims(jwtToken)
                                .getPayload();

                        String username = String.valueOf(claims.get("username"));
                        String sRoles   = String.valueOf(claims.get("roles"));

                        Collection<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(sRoles);
                        Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
            catch(ExpiredJwtException exception)
            {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token Expired");
                return;
            }
            catch (Exception exception) {
                throw new BadCredentialsException("Invalid Token received!");
            }

        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return publicPaths.stream().anyMatch(publicPath ->
                pathMatcher.match(publicPath, path));
    }
}

```

4. Register The Filter Inside Spring Security Filters Chain
```java
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
        
        // [@Added] Register The Filter
        http.addFilterBefore(new JwtValidationFilter(publicPaths), BasicAuthenticationFilter.class);
        
        http.formLogin(flc->flc.disable());
        http.httpBasic(withDefaults());
        return http.build();
    }

```