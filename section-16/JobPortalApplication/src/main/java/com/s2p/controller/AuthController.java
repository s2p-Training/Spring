package com.s2p.controller;

import com.s2p.dto.LoginRequestDto;
import com.s2p.dto.LoginResponseDto;
import com.s2p.dto.UserDto;
import com.s2p.security.util.JwtUtil;
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
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil)
    {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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
            String token = jwtUtil.generateJwtToken(resultAuthentication);

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
