package com.s2p.dto;

import com.s2p.entity.Company;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@ToString
public class JobDto
{
    private Long id;
    private String title;

    // Start Of Company Related Fields {
    private Long companyId;
    private String companyName;
    private String companyLogo;
    // } End Of Company Related Fields

    private String location;
    private String workType;
    private String jobType;
    private String category;
    private String experienceLevel;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryCurrency;
    private String salaryPeriod;
    private String description;
    private String requirements;
    private String benefits;
    private Instant postedDate;
    private Instant applicationDeadline;
    private Integer applicationsCount;
    private Boolean featured;
    private Boolean urgent;
    private Boolean remote;
    private String status;
}
