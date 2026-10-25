package com.s2p;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class JobPortalApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(JobPortalApplication.class, args);
	}

}