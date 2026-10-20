# Introduction To Bean Scopes

1. Create The Application Scoped Bean Inside scope package
```
package com.s2p.scope;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
@Getter
@Setter
public class ApplicationScopedBean
{
    private int count = 0;

    public ApplicationScopedBean()
    {
        System.out.println("ApplicationScopedBean Is Created");
    }

    public void increaseCount()
    {
        this.count++;
    }

}
```

2. Create The Session Scope Bean Inside scope package
```java
package com.s2p.scope;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Getter
@Setter
public class SessionScopeBean
{
    private String username;

    public SessionScopeBean()
    {
        System.out.println("SessionScopedBean Is Created");
    }

}
```

3. Create The Request Scope Bean Inside scope package
```java
package com.s2p.scope;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Random;

@Component
@RequestScope
@Getter
@Setter
public class RequestScopedBean
{
    private String username = "Rajesh";

    public RequestScopedBean()
    {
        System.out.println("RequestScopedBean Is Created");

        // Always Adding New Random Number Inside The Name/
        Random random = new Random();
        int randomInteger = random.nextInt(0,1200000);
        String sRandomInteger = String.valueOf(randomInteger);
        username = username.concat(sRandomInteger);
    }
}
```

4. Create The Scope Controller To Test All Scoped Beans
```java
package com.s2p.scope;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scopes")
public class ScopeController
{
    private ApplicationScopedBean applicationScopedBean = null;
    private SessionScopeBean sessionScopeBean = null;
    private RequestScopedBean requestScopedBean = null;

    @Autowired
    public ScopeController(
            ApplicationScopedBean applicationScopedBean,
            SessionScopeBean sessionScopeBean,
            RequestScopedBean requestScopedBean
        )
    {
        this.applicationScopedBean = applicationScopedBean;
        this.sessionScopeBean = sessionScopeBean;
        this.requestScopedBean = requestScopedBean;
    }

    // http://localhost:8080/api/scopes/application
    @GetMapping("/application")
    public ResponseEntity<Integer> testApplicationScope() {

        applicationScopedBean.increaseCount();
        int visitors = applicationScopedBean.getCount();

        ResponseEntity<Integer> response = new ResponseEntity<>(visitors, HttpStatus.OK);
        return response;
    }

    @GetMapping("/session/{username}")
    public ResponseEntity<String> testSessionScope(@PathVariable("username") String username)
    {
        sessionScopeBean.setUsername(username);
        String sessionUsername = sessionScopeBean.getUsername();

        ResponseEntity<String> response = new ResponseEntity<>(sessionUsername, HttpStatus.OK);
        return response;
    }

    @GetMapping("/request")
    public ResponseEntity<String> testRequestScope()
    {
        String requestScopedUsername = requestScopedBean.getUsername();

        ResponseEntity<String> response = new ResponseEntity<>(requestScopedUsername, HttpStatus.OK);
        return response;
    }
}

```

# Testing Spring Bean Scopes Using `ScopeController`

**Base URL:** `http://localhost:8080/api/scopes`

## 1. Test Application Scope

**Endpoint:** `GET /scopes/application`

### API Call

```bash
curl -i http://localhost:8080/api/scopes/application
```

### Expected Responses

Execute the command multiple times.

| Request | Expected Response Body |
|---|---|
| 1st request | `1` |
| 2nd request | `2` |
| 3rd request | `3` |
| 4th request | `4` |

**Observation:** The application-scoped bean shares the same instance across requests within the application, so the counter increases with every call.

## 2. Test Session Scope

**Endpoint:** `GET /scopes/session/{username}`

To test session scope, use separate browser sessions or separate cookie files with `curl`. Each cookie file represents an independent client session.

### Session 1 — Pranay

```bash
curl -i -c session1.txt -b session1.txt \
  http://localhost:8080/api/scopes/session/Pranay
```

**Expected response body:**

```text
Pranay
```

### Session 2 — Rajesh

```bash
curl -i -c session2.txt -b session2.txt \
  http://localhost:8080/api/scopes/session/Rajesh
```

**Expected response body:**

```text
Rahul
```

### Session 3 — Diya

```bash
curl -i -c session3.txt -b session3.txt \
  http://localhost:8080/api/scopes/session/Diya
```

**Expected response body:**

```text
Diya
```

### Verify Session Persistence

Call the same endpoint again using each session's cookie file:

```bash
curl -i -b session1.txt \
  http://localhost:8080/api/scopes/session/Pranay
```

```bash
curl -i -b session2.txt \
  http://localhost:8080/api/scopes/session/Rajesh
```

```bash
curl -i -b session3.txt \
  http://localhost:8080/api/scopes/session/Diya
```

**Expected results:**

| Cookie File | Expected Response |
|---|---|
| `session1.txt` | `Pranay` |
| `session2.txt` | `Rajesh` |
| `session3.txt` | `Diya` |

**Observation:** Each independent HTTP session has its own session-scoped bean instance. The cookie files preserve each session's `JSESSIONID` across requests.

**Important:** Your current endpoint calls `setUsername(username)` on every request. Therefore, these tests demonstrate that separate sessions can maintain separate values, but they do not independently prove persistence of the username across requests. To test that, add an endpoint that only reads the stored username without changing it.

## 3. Test Request Scope

**Endpoint:** `GET /scopes/request`

### API Call

```bash
curl -i http://localhost:8080/api/scopes/request
```

Execute the command multiple times to test separate HTTP requests.

To demonstrate that each request receives a new bean instance, initialize a unique value in `RequestScopedBean`:

```java
@Component
@RequestScope
@Getter
@Setter
public class RequestScopedBean
{
    private String username = "Rajesh";

    public RequestScopedBean()
    {
        System.out.println("RequestScopedBean Is Created");

        // Always Adding New Random Number Inside The Name
        Random random = new Random();
        int randomInteger = random.nextInt(0,1200000);
        String sRandomInteger = String.valueOf(randomInteger);
        username = username.concat(sRandomInteger);
    }
}
```

Add the required import:

```java
import java.util.Random;
```

### Expected Behavior

Each call should return a different Integer as ID, for example:

```text
Rajesh1128325
```

A subsequent call should return another random Integer.

**Observation:** A new request-scoped bean instance is created for every HTTP request.

## Summary

| Scope | API Testing Method | Expected Behavior |
|---|---|---|
| `@ApplicationScope` | Call `/scopes/application` repeatedly | Shared counter across requests |
| `@SessionScope` | Use separate cookie files | Separate state for each session |
| `@RequestScope` | Call `/scopes/request` repeatedly | New bean instance for each request |

**Note:** `ScopeController` is a singleton bean by default. Spring uses scoped proxies to provide the appropriate request-scoped or session-scoped bean instance when handling each API request.