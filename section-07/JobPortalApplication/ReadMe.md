# Introduction Global Exception Handling

1. Write Dummy APIs Inside ContactController Which Will Throw Exceptions For Demo Purpose
```java
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

    /// Dummy APIs Which Will Throw Exceptions
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

```

2. Create The ErrorResponseDto, To Send Proper Error Messages At The User End.
```java
package com.s2p.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto
{
	private String apiPath;
	private HttpStatus errorCode;
	private String errorMessage;
	private LocalDateTime errorTime;
}

```

3. Create The Package With The Name exceptions. Inside That Package Create The Class With Name GlobalExceptionHandler And Write The GlobalExceptionHandling Code.
```java
package com.s2p.exceptions;

import com.s2p.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler
{
	/// Going To Handle All Types Of Exceptions, If No Error Specific Handler Is Found
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleException(Exception e, WebRequest request)
	{
		/// Preparing Empty Error Response Object
		ErrorResponseDto errorDto = new ErrorResponseDto();

		// Fetching The Required Information For Populating The Error Response Object
		String apiPath = request.getDescription(false);
		errorDto.setApiPath(apiPath);

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		errorDto.setErrorCode(status);

		String errorMessage = e.getMessage();
		errorDto.setErrorMessage(errorMessage);

		LocalDateTime timestamp = LocalDateTime.now();
		errorDto.setErrorTime(timestamp);

		/// Preparing Response Entity Instance
		ResponseEntity<ErrorResponseDto> response = new ResponseEntity<>(errorDto,HttpStatus.INTERNAL_SERVER_ERROR);
		return response;

	}

	@ExceptionHandler(ArithmeticException.class)
	public ResponseEntity<ErrorResponseDto> handleArithmeticException(ArithmeticException e, WebRequest request)
	{
		/// Preparing Empty Error Response Object
		ErrorResponseDto errorDto = new ErrorResponseDto();

		// Fetching The Required Information For Populating The Error Response Object
		String apiPath = request.getDescription(false);
		errorDto.setApiPath(apiPath);

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		errorDto.setErrorCode(status);

		String errorMessage = e.getMessage();
		errorDto.setErrorMessage(errorMessage);

		LocalDateTime timestamp = LocalDateTime.now();
		errorDto.setErrorTime(timestamp);

		/// Preparing Response Entity Instance
		ResponseEntity<ErrorResponseDto> response = new ResponseEntity<>(errorDto,HttpStatus.INTERNAL_SERVER_ERROR);
		return response;

	}


	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponseDto> handleNullPointerException(NullPointerException e, WebRequest request)
	{
		/// Preparing Empty Error Response Object
		ErrorResponseDto errorDto = new ErrorResponseDto();

		// Fetching The Required Information For Populating The Error Response Object
		String apiPath = request.getDescription(false);
		errorDto.setApiPath(apiPath);

		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		errorDto.setErrorCode(status);

		String errorMessage = e.getMessage();
		errorDto.setErrorMessage(errorMessage);

		LocalDateTime timestamp = LocalDateTime.now();
		errorDto.setErrorTime(timestamp);

		/// Preparing Response Entity Instance
		ResponseEntity<ErrorResponseDto> response = new ResponseEntity<>(errorDto,HttpStatus.INTERNAL_SERVER_ERROR);
		return response;

	}

}

```
