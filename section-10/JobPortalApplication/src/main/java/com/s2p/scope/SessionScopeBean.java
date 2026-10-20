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
