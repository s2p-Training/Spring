package com.s2p.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/companies")
public class CompanyController
{
	// http://localhost:8080/api/companies
	@GetMapping(version = "1.0")
	public ResponseEntity<String> getAllCompanies()
	{
		String message = "Here Is List Of All Companies";
		ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
		return response;
	}
}
