package com.s2p.service.implementation;

import com.s2p.dto.CompanyDto;
import com.s2p.entity.Company;
import com.s2p.repostiory.CompanyRepository;
import com.s2p.service.interfaces.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Service
public class CompanyServiceImpl implements ICompanyService
{
	private CompanyRepository companyRepository = null;

	@Autowired
	public CompanyServiceImpl(CompanyRepository companyRepository)
	{
		this.companyRepository = companyRepository;
	}

	@Override
	public List<CompanyDto> fetchAllCompanies() {
		List<Company> companies = companyRepository.findAll();

		List<CompanyDto> companyDtoList = new LinkedList<>();

		for(Company c : companies)
		{
			CompanyDto dto = mapToDto(c);
			companyDtoList.add(dto);
		}

		return companyDtoList;
	}

	private CompanyDto mapToDto(Company company)
	{
		CompanyDto companyDto = new CompanyDto();

		Long id = company.getId();
		companyDto.setId(id);

		String name = company.getName();
		companyDto.setName(name);

		String logo = company.getLogo();
		companyDto.setLogo(logo);

		String industry = company.getIndustry();
		companyDto.setIndustry(industry);

		String size = company.getSize();
		companyDto.setSize(size);

		BigDecimal rating = company.getRating();
		companyDto.setRating(rating);

		String location = company.getLocations();
		companyDto.setLocations(location);

		Integer founded = company.getFounded();
		companyDto.setFounded(founded);

		String description = company.getDescription();
		companyDto.setDescription(description);

		Integer employees = company.getEmployees();
		companyDto.setEmployees(employees);

		String website = company.getWebsite();
		companyDto.setWebsite(website);

		Instant createdAt = company.getCreatedAt();
		companyDto.setCreatedAt(createdAt);

		return companyDto;
	}
}
