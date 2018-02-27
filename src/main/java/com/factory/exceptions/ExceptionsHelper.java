package com.factory.exceptions;

public class ExceptionsHelper{
	/**
	 * Returns a message string specific to not finding an External Reference by value.
	 *	
	 * @param extRefValue The external reference itself not found.
	 */
	public static String extRefNotFound(String extRefValue){
		return "Reference not found: " + extRefValue;
	}

	/**
	 * Returns a message string specific to not finding an External Reference by value.
	 *	
	 * @param refType The type of reference not found.
	 * @param extRefValue The external reference itself not found.
	 * @see {@link ExternalReferenceTypeOBOLSETE#toString()}
	 */
	public static String extRefNotFoundByValue(String refType, String extRefValue){
		return refType + " reference not found: " + extRefValue;
	}
	
	/**
	 * Returns a message string specific to not finding an External Reference by database ID.
	 *	
	 * @param refType The type of reference not found.
	 * @param dbId The database ID of interest not found.
	 * @see {@link ExternalReferenceTypeOBOLSETE#toString()}
	 */
	public static String extRefNotFoundByDbId(String refType, long dbId){
		return refType + " reference not found for ID: " + dbId;
	}

	public static String accessTokenNotFound(String accessToken){
		return "Access Token, " + accessToken + ", not found";
	}

	public static String accessTokenNotFound(long partnerAppId, String accessToken){
		return "Access Token, " + accessToken + ", not found for partner application" + partnerAppId;
	}
	
	public static String accessTokenNotFound(long partnerAppId, long userId){
		return "For partner application " + partnerAppId + ", Access Token not found for user " + userId; 
	}

	public static String partnerAppNotFound(String application){
		return "Partner application not found: " + application;
	}
	
//	public static String partnerAppIdNotFound(long partnerAppId){
//		return SchemaTable.PARTNER_APPLICATIONS.getSqlTableCreateCmd() + " ID not found: " + partnerAppId;
//	}

	public static String partnerAppNotOwner(String application, String extRef){
		return "Partner application" + application + " does not own " + extRef;
	}

	public static String dbIdNotFound(String table, long id){
		return "Database table " + table + " ID not found: " + id;
	}
}

