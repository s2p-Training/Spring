package com.s2p.controller;

import com.s2p.dto.ContactRequestDto;
import com.s2p.service.interfaces.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
