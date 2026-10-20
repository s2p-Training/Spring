package com.s2p.service.implementation;

import com.s2p.dto.CompanyDto;
import com.s2p.dto.JobDto;
import com.s2p.entity.Company;
import com.s2p.entity.Job;
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
			CompanyDto companyDto = mapToDto(c);

			// One Company Can Have Multiple Jobs
			// For Each Company We Need To Convert Multiple Jobs Entities Into Jobs Dto
			List<Job> jobsList =  c.getJobs();
			List<JobDto> jobDtoList = new LinkedList<>();

			for(Job j : jobsList)
			{
				JobDto jobDto = transformJobToDto(j);
				jobDtoList.add(jobDto);
			}

			// Setting JobDtoList Inside The CompanyDto
			companyDto.setJobs(jobDtoList);

			// Adding The CompanyDto Inside The CompanyDto List
			companyDtoList.add(companyDto);
		}

		return companyDtoList;
	}

	private JobDto transformJobToDto(Job job)
	{
		JobDto dto = new JobDto();

		long jobId = job.getId();
		dto.setId(jobId);

		String title = job.getTitle();
		dto.setTitle(title);

		// Start Of Company Related  {
		long companyId = job.getCompany().getId();
		dto.setCompanyId(companyId);

		String companyName = job.getCompany().getName();
		dto.setCompanyName(companyName);

		String companyLogo = job.getCompany().getLogo();
		dto.setCompanyLogo(companyLogo);

		// } End Of Company Related Fields

		String location = job.getLocation();
		dto.setLocation(location);

		String workType = job.getWorkType();
		dto.setWorkType(workType);

		String jobType = job.getJobType();
		dto.setJobType(jobType);

		String category = job.getCategory();
		dto.setCategory(category);

		String experience = job.getExperienceLevel();
		dto.setExperienceLevel(experience);

		BigDecimal minSalary = job.getSalaryMin();
		dto.setSalaryMin(minSalary);

		BigDecimal maxSalary = job.getSalaryMax();
		dto.setSalaryMax(maxSalary);

		String salaryCurrency = job.getSalaryCurrency();
		dto.setSalaryCurrency(salaryCurrency);

		String salaryPeriod = job.getSalaryPeriod();
		dto.setSalaryPeriod(salaryPeriod);

		String description = job.getDescription();
		dto.setDescription(description);

		String requirements = job.getRequirements();
		dto.setRequirements(requirements);

		String benefits = job.getBenefits();
		dto.setBenefits(benefits);

		Instant postedDate = job.getPostedDate();
		dto.setPostedDate(postedDate);

		Instant applicationDeadline = job.getApplicationDeadline();
		dto.setApplicationDeadline(applicationDeadline);

		int applicationsCount = job.getApplicationsCount();
		dto.setApplicationsCount(applicationsCount);

		boolean featured = job.getFeatured();
		dto.setFeatured(featured);

		boolean urgent = job.getUrgent();
		dto.setUrgent(urgent);

		boolean remote = job.getRemote();
		dto.setRemote(remote);

		String status = job.getStatus();
		dto.setStatus(status);

		return dto;
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
