package com.s2p.service.implementation;

import com.s2p.dto.ContactRequestDto;
import com.s2p.entity.Contact;
import com.s2p.repostiory.ContactRepository;
import com.s2p.service.interfaces.IContactService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ContactServiceImpl implements IContactService
{
	private final ContactRepository contactRepository;

	public ContactServiceImpl(ContactRepository contactRepository) {
		this.contactRepository = contactRepository;
	}

	@Override
	public boolean saveContact(ContactRequestDto contactRequestDto) {
		boolean result = false;
		Contact contact = contactRepository.save(transformToEntity(contactRequestDto));
		if(contact != null && contact.getId() != null) {
			result = true;
		}
		return result;
	}

	private Contact transformToEntity(ContactRequestDto contactRequestDto) {
		Contact contact = new Contact();
		BeanUtils.copyProperties(contactRequestDto, contact);
		contact.setCreatedAt(Instant.now());
		contact.setCreatedBy("System");
		contact.setStatus("NEW");
		return contact;
	}
}
