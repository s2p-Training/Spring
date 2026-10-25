package com.s2p.service.interfaces;

import com.s2p.dto.ContactRequestDto;

public interface IContactService
{
	boolean saveContact(ContactRequestDto contactRequestDto);
}
