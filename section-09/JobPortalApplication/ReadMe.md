# Introduction To Auditing In Spring Boot 

### 1. Creating The BaseEntity Class

1. Write The BaseEntity Class With All Audit Fields
2. Create The Class With Name BaseEntity Inside Entity Package.
3. Add All The Auditing Fields Inside It.

```java

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity
{
    @CreatedDate
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private String createdBy;

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, length = 20, updatable = false)
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "UPDATED_BY", length = 20, insertable = false)
    private String updatedBy;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "UPDATED_AT", insertable = false)
    private Instant updatedAt;
}

```

4. Extend The Company Entity With BaseEntity Class, For Borrowing The Auditing Fields

```java
package com.s2p.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "COMPANIES")
@Getter
@Setter
public class Company extends BaseEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID",nullable = false)
    private Long id;

    @Column(name = "NAME",nullable = false,unique = true)
    private String name;

    @Column(name = "LOGO", length = 500)
    private String logo;

    @Column(name = "INDUSTRY", nullable = false, length = 100)
    private String industry;

    @Column(name = "SIZE", nullable = false, length = 50)
    private String size;

    @Column(name = "RATING", nullable = false, precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(name = "LOCATIONS", length = 1000)
    private String locations;

    @Column(name = "FOUNDED", nullable = false)
    private Integer founded;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EMPLOYEES")
    private Integer employees;

    @Column(name = "WEBSITE", length = 500)
    private String website;
    
//	These Auditing Fields Will Be Borrowed From BaseEntity Class
//	@Column(name = "CREATED_AT", nullable = false)
//	private Instant createdAt;
//
//	@Column(name = "CREATED_BY", nullable = false, length = 20)
//	private String createdBy;
//
//	@Column(name = "UPDATED_AT")
//	private Instant updatedAt;
//
//	@Column(name = "UPDATED_BY", length = 20)
//	private String updatedBy;

}

```

5. Extend The Contact Entity With BaseEntity Class, For Borrowing The Auditing Fields.
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
public class Contact extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

//    @Column(name = "created_at", nullable = false)
//    private Instant createdAt;

//    @Column(name = "created_by", nullable = false, length = 20)
//    private String createdBy;

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

//    @Column(name = "updated_at")
//    private Instant updatedAt;

//    @Column(name = "updated_by", length = 20)
//    private String updatedBy;

    @Column(name = "user_type", nullable = false, length = 50)
    private String userType;

}
```

### 2. Creating AuditAware Bean

1. Create the package with name audit.
2. Create The Class With The Name AuditAwareImpl, Which Will Have The Login Details Or Username Of A User By Whom API is invoked.

```java
package com.s2p.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String>
{
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("Anonymous User");
    }
}

```

### 3. Removing Hardcoded Auditing Values
```java
	private Contact transformToEntity(ContactRequestDto contactRequestDto) {
		Contact contact = new Contact();
		BeanUtils.copyProperties(contactRequestDto, contact);
		contact.setCreatedAt(Instant.now());
//		contact.setCreatedBy("System"); // Created By Will Be Supplied By The AuditAwareInstance.
		contact.setStatus("NEW");
		return contact;
	}
```

### 4. Enabling The Auditing At The Start Of Application
1. Annotate The Application Starter Class, To Enable The JPA Auditing From The Start Of The Application
```java
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class JobPortalApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(JobPortalApplication.class, args);
    }

}
```

### 5. Create The Database Table Contacts, So That Entity Class Will Be Mapped With Database Table
```sql
CREATE TABLE IF NOT EXISTS contacts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    subject VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL,

    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    created_by VARCHAR(255) NOT NULL,

    updated_at TIMESTAMP(6) NULL DEFAULT NULL
    ON UPDATE CURRENT_TIMESTAMP(6),
    updated_by VARCHAR(20) DEFAULT NULL,
    PRIMARY KEY (id)
    );
```

### 6. Test The Contacts APIs
# Create Contact

Creates a new contact submission.

## Endpoint

`POST /api/contacts`

## Request Headers

| Header | Value |
|---|---|
| `Content-Type` | `application/json` |
| `Accept` | `application/vnd.s2p+json;v=1.0` |

## Request Body

```json
{
  "email": "pranayramteke613@gmail.com",
  "message": "This is some useless message for testing the API",
  "name": "Pranay Ramteke",
  "subject": "System Issue At Backend Server",
  "userType": "Job Seeker"
}
```

## Example Request

```bash
curl --location 'http://localhost:8080/api/contacts' \
  --header 'Content-Type: application/json' \
  --header 'Accept: application/vnd.s2p+json;v=1.0' \
  --data '{
    "email": "pranayramteke613@gmail.com",
    "message": "This is some useless message for testing the API",
    "name": "Pranay Ramteke",
    "subject": "System Issue At Backend Server",
    "userType": "Job Seeker"
  }'
```

## Responses

Document the actual HTTP status codes and response bodies returned by your implementation.

- Success: contact created.
- Validation failure: request contains invalid or missing fields.
- Server error: unexpected failure while processing the request.
