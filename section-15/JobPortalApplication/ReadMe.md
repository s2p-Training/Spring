# Creating the Dummy Login API in Spring Boot

## 1. Create All Required DTOs

### 1.1. Create `LoginRequestDto.java`

Create the `LoginRequestDto.java` file inside the `dto` package.

```java
package com.s2p.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String username;
    private String password;
}
```

### 1.2. Create `UserDto.java`

Create the `UserDto.java` file inside the `dto` package.

```java
package com.s2p.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class UserDto {
    private Long userId;
    private String name;
    private String email;
    private String mobileNumber;
    private String role;
    private Long companyId;
    private String companyName;
    private Instant createdAt;
}
```

### 1.3. Create `LoginResponseDto.java`

Create the `LoginResponseDto.java` file inside the `dto` package.

```java
package com.s2p.dto;

public record LoginResponseDto(
        String message,
        UserDto user,
        String jwtToken
) {
}
```

The `LoginResponseDto` record contains the following fields:

- `message`: Represents the login response message.
- `user`: Contains the user details returned by the API.
- `jwtToken`: Contains the dummy authentication token.

## 2. Create the `AuthController` with Dummy Behavior

Create the `AuthController.java` file inside the controller package.

```java
package com.s2p.controller;

import com.s2p.dto.LoginRequestDto;
import com.s2p.dto.LoginResponseDto;
import com.s2p.dto.UserDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    // POST http://localhost:8080/api/auth/login/public
    @PostMapping(value = "/login/public", version = "1.0")
    public ResponseEntity<LoginResponseDto> apiLogin(
            @RequestBody LoginRequestDto loginRequestDto) {

        UserDto userDto = new UserDto();

        // Sending dummy user data
        userDto.setUserId(104L);
        userDto.setName("John Doe");
        userDto.setEmail("john@gmail.com");
        userDto.setMobileNumber("+1 4155550143");
        userDto.setCompanyId(2L);
        userDto.setCompanyName("Microsoft Corporation");
        userDto.setRole("ROLE_JOBSEEKER");
        userDto.setCreatedAt(Instant.now());

        // Sending a dummy JWT token
        String token = "TestToken@aexdsyr@34fsd500";

        // Creating the response message
        String message = "Login Successful";

        LoginResponseDto responseDto =
                new LoginResponseDto(message, userDto, token);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
```

**Note:** This controller returns the same dummy user details and token regardless of the username and password supplied. It does not perform actual authentication or generate a real JWT.

## 3. Update the Spring Security Path Configuration

Open `PathsConfig.java` and add the login endpoint to the list of public paths.

```java
package com.s2p.config;

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
                "/api/auth/login/public",
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

The `/api/auth/login/public` path is added to the public paths so that users can access the login API without prior authentication.

**Note:** Ensure that your Spring Security configuration processes public paths before secured paths. The configured paths must also match the paths seen by Spring Security.

## 4. Test the Login API Using Postman

### 4.1. Configure the Request

**HTTP Method:** `POST`

**Request URL:**

```text
http://localhost:8080/api/auth/login/public
```

**Headers:**

| Key | Value |
|---|---|
| Content-Type | application/vnd.s2p+json;v=1.0 |
| Accept | application/vnd.s2p+json;v=1.0 |

**Request Body:**

In Postman, select **Body → raw → JSON** and enter the following JSON:

```json
{
    "username": "john",
    "password": "john123"
}
```

### 4.2. Expected Response

**HTTP Status:** `200 OK`

```json
{
    "message": "Login Successful",
    "user": {
        "userId": 104,
        "name": "John Doe",
        "email": "john@gmail.com",
        "mobileNumber": "+1 4155550143",
        "role": "ROLE_JOBSEEKER",
        "companyId": 2,
        "companyName": "Microsoft Corporation",
        "createdAt": "2026-10-25T10:09:30.123Z"
    },
    "jwtToken": "TestToken@aexdsyr@34fsd500"
}
```

**Note:** The `createdAt` timestamp will vary because it is generated using `Instant.now()`.

### 4.3. Test Using cURL

```bash
curl -X POST \
  http://localhost:8080/api/auth/login/public \
  -H "Accept: application/vnd.s2p+json;v=1.0" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "john123"
  }' \
  -o output.json
```

**Expected Result:** The API returns the dummy user details, a success message, and the dummy token.