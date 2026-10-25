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
