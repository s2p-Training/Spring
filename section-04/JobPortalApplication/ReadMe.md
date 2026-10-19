# Implementing CORS Configuration Inside Spring Application

CORS Configuration Can Be Done Using 3 Ways:
1. @CrossOrigin Annotation Used With API Method
2. @CrossOrigin Annotation Used With Controller Class
3. Writing Custom CORS Configuration Inside Spring Object EcoSystem

## Methods On Handling CORS At Server Level

1. @CrossOrigin Annotation Used With API Method

Inside **CompanyController.java** file, we can use @CrossOrigin Annotation With Method, Which Will Be Invoked By The Application From Different Origin

```java
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
	@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
	@GetMapping(version = "1.0")
	public ResponseEntity<List<CompanyDto>> getAllCompanies()
	{
		List<CompanyDto> responseBody = companyService.fetchAllCompanies();
		ResponseEntity<List<CompanyDto>> response = new ResponseEntity<>(responseBody,HttpStatus.OK);
		return response;
	}
}
```

2. @CrossOrigin Annotation Used With Controller Class

```java
@RestController
@RequestMapping(path = "/companies")
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
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
```

3. Writing Custom CORS Configuration Inside Spring Object EcoSystem, **Recommended Method**
```java
@Configuration
public class CorsConfig
{
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5137","http://localhost:4200"));
        config.setAllowedOriginPatterns(List.of("*")); // For Testing Only
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(List.of("Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);

        CorsFilter corsFilter = new CorsFilter(source);
        return corsFilter;
    }
}

```