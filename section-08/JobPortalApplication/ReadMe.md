# Getting Started With Spring Boot Request Validation

1. Update the pom.xml file for adding the validation library.
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
```

2. Update DTO, with Validation Annotations.
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestDto
{
	@NotBlank(message = "Email Can Not Empty")
	@Email(message = "Invalid Email Address")
	private String email;

	@NotBlank(message = "Message Can Not Be Empty")
	@Size(min = 5, max = 500, message = "Message Must Be Between 5 And 500 Characters")
	private String message;

	@NotBlank(message = "Name Can Not Be Empty")
	@Size(min = 2, max = 30, message = "Name Must Be Between 2 And 30 Characters")
	private String name;

	@NotBlank(message = "Subject Can Not Be Blank")
	@Size(min = 5, max = 120, message = "Subject Must Be Between 5 And 120 Characters")
	private String subject;

	@NotBlank(message = "UserType can not be empty")
	@Pattern(regexp = "Job Seeker|Employer|Other", message = "UserType must be one of: Job Seeker, Employer, Other")
	private String userType;
}
```

3. Update The Company Controller For Validation

```java

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final IContactService contactService;

    @Autowired
    public ContactController(IContactService contactService) {
        this.contactService = contactService;
    }

    /// Applying Validation Logic Using @Valid Annotation
    @PostMapping(version = "1.0")
    public ResponseEntity<String> saveContactMsg(@Valid @RequestBody ContactRequestDto contactRequestDto) {
        String message = null;
        HttpStatus statusCode = null;

        boolean isSaved = contactService.saveContact(contactRequestDto);

        if (isSaved) {
            message = "Request processed successfully";
            statusCode = HttpStatus.CREATED;
        } else {
            message = "Request processing failed";
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ResponseEntity<String> response = new ResponseEntity<>(message, statusCode);
        return response;
    }

    /// Applying Validation On Path Variables
    /// URI: http://localhost:8080/api/contacts/12000
    @GetMapping(version = "1.0", path = {"/{contact_id}"})
    public ResponseEntity<String> fetchContactInformationById(
            @PathVariable("contact_id")
            @Validated
            @Min(0)
            @Max(1200000L)
            @Positive(message = "Contact ID Should Be Positive") Long contactId
    ) {
        String message = "Contact Fetched From Database With Contact ID : " + contactId;
        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }

    /// Applying Validation On Query Params
    /// URI: http://localhost:8080/api/contacts?status=OPEN
    /// URI: http://localhost:8080/api/contacts?status=CLOSE
    /// URI: http://localhost:8080/api/contacts?status=READ
    /// URI: http://localhost:8080/api/contacts?status=NEW
    @GetMapping(version = "1.0")
    public ResponseEntity<String> fetchOpenContacts(
            @RequestParam
            @Validated
            @NotBlank(message = "Status Can Be Empty")
            @Size(min = 3, max = 15, message = "Status Length Should Be Between 3 And 15")
            @Pattern(regexp = "OPEN|CLOSE|READ|NEW", message = "Status Must Be One Of : OPEN | CLOSE | READ | NEW ")
            String status
    ) {
        String message = "Contacts Fetched From Database With Status : " + status;
        ResponseEntity<String> response = new ResponseEntity<>(message, HttpStatus.OK);
        return response;
    }


}
```