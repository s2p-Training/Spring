package com.s2p.controller;

import com.s2p.dto.ContactRequestDto;
import com.s2p.service.interfaces.IContactService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import org.apache.coyote.Response;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
public class ContactController
{
	private final IContactService contactService;

	@Autowired
	public ContactController(IContactService contactService) {
		this.contactService = contactService;
	}

	/// Applying Validation Logic Using @Valid Annotation
	@PostMapping(version = "1.0")
	public ResponseEntity<String> saveContactMsg(@Valid @RequestBody ContactRequestDto contactRequestDto)
	{
		String message = null;
		HttpStatus statusCode = null;

		boolean isSaved =  contactService.saveContact(contactRequestDto);

		if (isSaved) {
			message = "Request processed successfully";
			statusCode = HttpStatus.CREATED;
		}
		else
		{
			message = "Request processing failed";
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		ResponseEntity<String> response = new ResponseEntity<>(message,statusCode);
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
	)
	{
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
	)
	{
		String message = "Contacts Fetched From Database With Status : " + status;
		ResponseEntity<String> response = new ResponseEntity<>(message,HttpStatus.OK);
		return response;
	}



	/// Verbose Examples On Global Exception Handling
	@GetMapping(version = "1.0",path = "/exception1")
	public ResponseEntity<String> exceptionAPI1()
	{
		// Some Business Logic Which Will Throw Error

		throw new RuntimeException("It's a bad day");
	}

	@PostMapping(version = "1.0",path = "/exception2")
	public ResponseEntity<String> exceptionAPI2()
	{
		// Some Business Logic Which Will Throw Error

		String message = null;
		ResponseEntity<String> response = null;
		HttpStatus statusCode = null;

		try
		{
			int a = 10;
			int b = 0;
			int c = a / b;
			message = "Answer of division is " + c;
			statusCode = HttpStatus.OK;
		}
		catch (ArithmeticException e)
		{
			message = "Error Message : " + e.getMessage();
			statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		finally
		{
			response = new ResponseEntity<>(message,statusCode);
		}

		return response;

	}

	/// Smarter Way Of Handling Exceptions Using Global Exception Handling
	/// This Method Will Throw ArithmeticException
	/// For Handling This Exception We Will Be Writing The ExceptionHandler
	@GetMapping(version = "1.0",path = "/exception3")
	public ResponseEntity<String> exceptionAPI3()
	{
		String message = null;
		ResponseEntity<String> response = null;
		HttpStatus statusCode = null;

		int a = 10;
		int b = 0;
		int c = a / b;

		message = "Answer of division is " + c;
		statusCode = HttpStatus.OK;

		response = new ResponseEntity<>(message,statusCode);

		return response;
	}

	/// Smarter Way Of Handling Exceptions Using Global Exception Handling
	/// This Method Will Throw NullPointerException
	/// For Handling This Exception We Will Be Writing The ExceptionHandler
	@GetMapping(version = "1.0",path = "/exception4")
	public ResponseEntity<String> exceptionAPI4()
	{
		String message = null;
		ResponseEntity<String> response = null;
		HttpStatus statusCode = null;

		String sName = null;
		message = "Capitalized Name : " + sName.toUpperCase();

		response = new ResponseEntity<>(message,statusCode);

		return response;
	}



}
