package com.s2p.service.interfaces;
import com.s2p.dto.CompanyDto;
import java.util.List;

public interface ICompanyService
{
    public abstract List<CompanyDto> fetchAllCompanies();
}
