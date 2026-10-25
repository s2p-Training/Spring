package com.s2p.controller;

import com.s2p.dto.LoginRequestDto;
import com.s2p.dto.LoginResponseDto;
import com.s2p.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping(path = "/auth")
public class AuthController
{
    // http://localhost:8080/auth/public
    // Body: loginRequest JSON Structure
    @PostMapping(value = "/login/public", version = "1.0")
    public ResponseEntity<LoginResponseDto> apiLogin(@RequestBody LoginRequestDto loginRequestDto)
    {
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
}
