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
