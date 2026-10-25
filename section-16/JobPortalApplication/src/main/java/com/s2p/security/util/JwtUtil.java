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
