package com.factory.manager.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.factory.dao.CompanyDao;
import com.factory.dao.impl.QueryTerm;
import com.factory.domain.Company;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.NotFoundException;
import com.factory.manager.CompanyManager;

@Component
public class CompanyManagerImpl implements CompanyManager {
	
	@Autowired private CompanyDao companyDao;

	@Override
	public int createCompany(String name, String description, String industry, int licenseLevel) {
		
		Company company = new Company();
		company.setName(name);
		company.setDescription(description);
		company.setIndustry(industry);
		company.setLicenseLevel(licenseLevel);
		company.setCreatedAt(Instant.now());
		company.setOwnerId(1);
		
		return companyDao.persist(company);
	}

	@Override
	public Company getCompanyByName(String name) {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(CompanyDao.Field.NAME.getQueryTerm(name));
		try{
			return companyDao.findObject(terms);
		} catch(NotFoundException e){
			throw new NotFoundException(ErrorType.COMPANY_NOT_FOUND);
		}
	}

}
