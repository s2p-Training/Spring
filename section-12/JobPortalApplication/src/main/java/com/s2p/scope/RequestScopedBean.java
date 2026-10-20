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
