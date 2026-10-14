package com.s2p.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestDto
{
	private String email;
	private String message;
	private String name;
	private String subject;
	private String userType;
}