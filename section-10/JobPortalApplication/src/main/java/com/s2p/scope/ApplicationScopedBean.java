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
