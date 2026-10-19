package com.s2p.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

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
