# Introduction To JPA Relationships

1. Create The Job Database Table From /sql/schema.sql
```sql
CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    company_id BIGINT NOT NULL,
    location VARCHAR(255) NOT NULL,
    work_type VARCHAR(50) NOT NULL, -- On-site, Remote, Hybrid
    job_type VARCHAR(50) NOT NULL, -- Full-time, Part-time, Contract, Freelance
    category VARCHAR(100) NOT NULL, -- Technology, Design, Marketing, Sales, Finance, Healthcare, Education, Operations
    experience_level VARCHAR(50) NOT NULL, -- Entry Level, Mid Level, Senior Level, Executive Level
    salary_min DECIMAL(12,2) NOT NULL,
    salary_max DECIMAL(12,2) NOT NULL,
    salary_currency VARCHAR(10) DEFAULT 'USD' NOT NULL,
    salary_period VARCHAR(20) DEFAULT 'year' NOT NULL,
    description TEXT NOT NULL,
    requirements TEXT, -- JSON array stored as text
    benefits TEXT, -- JSON array stored as text
    posted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    application_deadline TIMESTAMP,
    applications_count INT DEFAULT 0,
    featured BOOLEAN DEFAULT FALSE,
    urgent BOOLEAN DEFAULT FALSE,
    remote BOOLEAN DEFAULT FALSE,
    status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL, -- ACTIVE, CLOSED, DRAFT
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL,
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE
    );
```

2. Flush The Data Inside The Jobs Table
```sql
USE JobPortalDB;
-- Use Insert Commands From JobsData.sql
```

3. Create The Job Entity Class For Mapping With Database Table jobs
```java
package com.s2p.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "jobs")
public class Job extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 255)
    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @Size(max = 50)
    @NotNull
    @Column(name = "work_type", nullable = false, length = 50)
    private String workType;

    @Size(max = 50)
    @NotNull
    @Column(name = "job_type", nullable = false, length = 50)
    private String jobType;

    @Size(max = 100)
    @NotNull
    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @Size(max = 50)
    @NotNull
    @Column(name = "experience_level", nullable = false, length = 50)
    private String experienceLevel;

    @NotNull
    @Column(name = "salary_min", nullable = false, precision = 12, scale = 2)
    private BigDecimal salaryMin;

    @NotNull
    @Column(name = "salary_max", nullable = false, precision = 12, scale = 2)
    private BigDecimal salaryMax;

    @Size(max = 10)
    @NotNull
    @ColumnDefault("'USD'")
    @Column(name = "salary_currency", nullable = false, length = 10)
    private String salaryCurrency;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'year'")
    @Column(name = "salary_period", nullable = false, length = 20)
    private String salaryPeriod;

    @NotNull
    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "requirements")
    private String requirements;

    @Lob
    @Column(name = "benefits")
    private String benefits;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "posted_date", nullable = false)
    private Instant postedDate;

    @Column(name = "application_deadline")
    private Instant applicationDeadline;

    @ColumnDefault("0")
    @Column(name = "applications_count")
    private Integer applicationsCount;

    @ColumnDefault("0")
    @Column(name = "featured")
    private Boolean featured;

    @ColumnDefault("0")
    @Column(name = "urgent")
    private Boolean urgent;

    @ColumnDefault("0")
    @Column(name = "remote")
    private Boolean remote;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'ACTIVE'")
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    
}

```

3. Create Relationship Between Companies And Jobs, One Company Can Have Multiple Jobs, One Jobs Belongs To A Single Company
- Relation Mapping For Company Entity
```java
package com.s2p.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMPANIES")
@Getter
@Setter
public class Company extends BaseEntity
{
    /// Other Fields
    /// Relation: One Company Will Have Many Jobs
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Job> jobs = new ArrayList<>();

}
```

- Relation Mapping For Job Entity
```java
package com.s2p.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "jobs")
public class Job extends BaseEntity
{
    /// Other Fields
    /// Relation: Many Jobs Belongs To A Single Company
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}

```

4. Create The Job Dto For Sending Response Through APIs.
```java
package com.s2p.dto;

import com.s2p.entity.Company;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@ToString
public class JobDto
{
    private Long id;
    private String title;

    // Start Of Company Related Fields {
    private Long companyId;
    private String companyName;
    private String companyLogo;
    // } End Of Company Related Fields

    private String location;
    private String workType;
    private String jobType;
    private String category;
    private String experienceLevel;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryCurrency;
    private String salaryPeriod;
    private String description;
    private String requirements;
    private String benefits;
    private Instant postedDate;
    private Instant applicationDeadline;
    private Integer applicationsCount;
    private Boolean featured;
    private Boolean urgent;
    private Boolean remote;
    private String status;
}

```

5. Update The Company Dto, To Create The Association Between Company And Job
```java
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CompanyDto
{
    /// Other Fields
    /// Association Between Company Dto And Jobs Dto.
    private List<JobDto> jobs;
}
```

6. Write The Method For Converting Job Entity Into Job Dto, Inside CompanyServiceImpl Class
```java
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

    String companyLocation = job.getCompany().getLogo();
    dto.setLocation(companyLocation);

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
```

7. Update The FetchCompanies Logic At Service Level, Inside CompanyServiceImpl Class
```java
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
```

8. Test The API
# Testing Updated Companies API

**API Endpoint:** `GET http://localhost:8080/api/companies`

## 1. Execute API Using cURL

```bash
curl -i http://localhost:8080/api/companies
```

If your API requires the custom media type used in your application, use:

```bash
curl -i \
  -H "Accept: application/vnd.s2p+json;v=1.0" \
  http://localhost:8080/api/companies
```

## 2. Expected Response

**HTTP Status:** `200 OK`

The response should contain a list of companies. Each company object includes its company details and a `jobs` array containing the associated jobs.

Example response structure:

```json
[
  {
    "id": 1,
    "name": "TechCorp Inc.",
    "logo": "/logos/techcorp.png",
    "industry": "Technology",
    "size": "Large",
    "rating": 4.5,
    "locations": "San Francisco,New York,Austin",
    "founded": 2010,
    "description": "A leading technology company.",
    "employees": 15000,
    "website": "https://www.techcorpinc.com",
    "jobs": [
      {
        "id": 48,
        "title": "Investment Analyst",
        "companyId": 1,
        "companyName": "TechCorp Inc.",
        "companyLogo": "/logos/techcorp.png",
        "location": "Orlando, FL",
        "workType": "Hybrid",
        "jobType": "Contract",
        "category": "Finance",
        "experienceLevel": "Senior Level",
        "salaryMin": 113000,
        "salaryMax": 198000,
        "salaryCurrency": "USD",
        "salaryPeriod": "year",
        "description": "Job description goes here.",
        "requirements": "[\"Excel\", \"Financial Modeling\"]",
        "benefits": "[\"Health Insurance\", \"Paid Time Off\"]",
        "postedDate": "2025-10-11T18:27:59Z",
        "applicationDeadline": "2025-12-20T11:27:59Z",
        "applicationsCount": 82,
        "featured": true,
        "urgent": false,
        "remote": false,
        "status": "ACTIVE"
      }
    ]
  }
]
```

*Note: This is an illustrative response structure. Actual values depend on your database and DTO mapping.*

## 3 Optional: Save and Format the Response

Save the API response to a JSON file:

```bash
curl -s \
  -H "Accept: application/vnd.s2p+json;v=1.0" \
  http://localhost:8080/api/companies \
  -o companies-response.json
```

If `jq` is installed, format and validate the JSON:

```bash
jq . companies-response.json
```

**Expected result:** The command displays formatted JSON. If the response is invalid JSON, `jq` reports a parsing error.