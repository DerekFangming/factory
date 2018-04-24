package com.factory.domain;

public interface Principal {
	
	//Commonly shared getter setter methods among User and UserDetails
	
	public int getId();
	
	public String getPassword();
	
	public String getAccessToken();
	
	public void setAccessToken(String accessToken);
	
	public Boolean getRemember();
	
	public String getSalt();
	
	public void setTokenUpdated();

}
