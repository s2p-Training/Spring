package com.s2p.controller;

import com.s2p.dto.ContactRequestDto;
import com.s2p.service.interfaces.IContactService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@PostMapping(version = "1.0")
	public ResponseEntity<String> saveContactMsg(@RequestBody ContactRequestDto contactRequestDto)
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
