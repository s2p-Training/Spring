package com.s2p.repostiory;

import com.s2p.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long>
{
}
