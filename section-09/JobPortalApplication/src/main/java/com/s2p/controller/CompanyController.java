package com.s2p.controller;

import com.s2p.dto.CompanyDto;
import com.s2p.service.interfaces.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(path = "/companies")
public class CompanyController
{
	private ICompanyService companyService = null;

	@Autowired
	public CompanyController(ICompanyService companyService)
	{
		this.companyService = companyService;
	}

	// http://localhost:8080/api/companies
	@GetMapping(version = "1.0")
	public ResponseEntity<List<CompanyDto>> getAllCompanies()
	{
		List<CompanyDto> responseBody = companyService.fetchAllCompanies();
		ResponseEntity<List<CompanyDto>> response = new ResponseEntity<>(responseBody,HttpStatus.OK);
		return response;
	}
}
