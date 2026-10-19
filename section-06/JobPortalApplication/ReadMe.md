# Creating Contacts API

1. Define The Contact Entity, To Map The Java Class With Database Table
```java
package com.s2p.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, length = 20)
    private String createdBy;

    @Column(name = "email", nullable = false)
    private String email;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "name", nullable = false)
    private String name;

    @ColumnDefault("'NEW'")
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 20)
    private String updatedBy;

    @Column(name = "user_type", nullable = false, length = 50)
    private String userType;

}
```

2. Create The DTO Object For Contact Entity, For Defining The Structure Of API Request And Response
```java
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
```

3. Create The Repository Layer, Which Will Be Going To Represent Database Operations For A Contact Entity
```java
package com.s2p.repostiory;

import com.s2p.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>
{

}
```

4. Create The Interface With The Name IContactService, Here We Will Be Writing The Contracts, Which Will BeFollowed By The Implementation Service Class
```java
package com.s2p.service.interfaces;

import com.s2p.dto.ContactRequestDto;

public interface IContactService
{
	boolean saveContact(ContactRequestDto contactRequestDto);
}

```

5. Write The Implementation Class, For ICompanyService Interface By Implementing The Standards Imposed By The IContactService Interface.
    1. Inside ContactServiceImpl class, Autowire The Bean Of ContactRepository As, We Need To Perform Database Level Operations.
    2. Perform Constructor Based Dependency Injection
    3. Implement The Contract Imposed By The Interface On Implementation Class
    4. Since Repository Layer Is Compatible With Entity Objects, But Entities Are Tightly Coupled To Repository Object, For Transferring The Database Across Multiple Layers, client -> controller -> service -> repository or vice versa, we need Data Transfer Object Conversion Method.
    5. Write One Private Method, For The Conversion Of DTO Object To Entity Object.

```java
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

```