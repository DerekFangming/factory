package com.factory.manager;

import com.factory.domain.Company;

public interface CompanyManager {
	
	public int createCompany (String name, String description, String industry, int licenseLevel);
	
	public Company getCompanyByName(String name);

}
