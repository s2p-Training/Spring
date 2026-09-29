package com.s2p.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CompanyDto
{
    private Long id;
    private String name;
    private String logo;
    private String industry;
    private String size;
    private BigDecimal rating;
    private String locations;
    private Integer founded;
    private String description;
    private Integer employees;
    private String website;
    private Instant createdAt;
}