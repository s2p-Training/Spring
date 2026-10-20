package com.s2p.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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