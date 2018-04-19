package com.factory.exceptions;

public enum ErrorType {
	//General
	UNKNOWN_ERROR(100, "Unknown error. Please contact support."),
	INTERNAL_SERVER_ERROR(101, "Internal server error. Please contact support."),
	INVALID_PARAMS(102, "The input paramters are not correct. Please contact support."),
	INTERNAL_ERROR(103, "Internal error. Please contact support."),
	//User
	USER_NOT_FOUND(201, "The user does not exist."),
	INVALID_REG_CODE(202, "The registration code you entered is invalid."),
	USERNAME_UNAVAILABLE(203, "The username is already taken. Please try a different one."),
	//Role
	ROLE_NOT_FOUND(201, "The role does not exist."),
	;
	
	
	/*
	 * NO_PERMISSION("You do not have permission to do this."),
	//Security related
	INVALID_ACCESS_TOKEN("The access token you are using is not valid. Please login again."),
	INVALID_VERIFICATION_CODE("The verification code does not match what's on the record."),
	NO_USER_LOGGED_IN("Please login first before using this feature."),
	//HTTP related
	INCORRECT_PARAM("Request parameters incorrect."),
	//User related
	USERNAME_TOO_LONG("Username cannot be logger than 32 characters."),
	USERNAME_NOT_EMAIL("Username must be an email."),
	USER_NOT_FOUND("The user does not exist."),
	USERNAME_OR_PASSWORD_INCORRECT("Username or password is not correct. Please double check and try again."),
	SESSION_EXPIRED("Session expired. Please login again."),
	USER_INTERN_ERROR("Internal error. Please only use methods provided in SDK."),
	LOGIN_FAIL("The username or password you entered is not correct. Please try again."),
	INTERNAL_LOGIC_ERROR("Internal error. Please report to admin@fmning.com."),
	EMAIL_ALREADY_VERIFIED("Your email address is already verified. Please restart your app or log out and log back in again."),
	EMAIL_NOT_CONFIRMED("Please confirm your email first."),
	REQUESTED_USER_EMAIL_VERIFIED("The email for this user is already verified. Please refresh this page."),
	INCORRECT_PASSWORD("The password you enterred is not correct."),
	CHANGE_PWD_BUT_NOT_VERIFIED("You have to confirm your email before changing password. A email to confirm your email address has been sent to your inbox. Please check junk of trash folder if you can\' see it."),
	//User detail related
	USER_DETAIL_NOT_FOUND("The user does not have detail information."),
	//Image related
	IMAGE_NOT_FOUND("The image you are looking for does not exist."),
	NO_IMAGE_TO_DELETE("The image you are trying to delete does not exist."),
	INCORRECT_INTER_IMG_PATH("Internal error, image path not found."),
	INCORRECT_INTER_IMG_IO("Internal error, cannot write image file."),
	UNAUTHORIZED_IMAGE_DELETE("Cannot delete an image that is not yours."),
	SINGLETON_IMG_NOT_FOUND("The user does not have image for the given type."),
	AVATAR_NOT_FOUND("The user does not have an avatar."),
	INVALID_TYPE_UNIQUE_IMG("There are more than one images found for the given type."),
	//Relationship related
	NOT_FRIEND("You are not a friend of him/her."),
	ALREADY_REQUESTED("You have already sent a request to him/her."),
	ALREADY_FRIEND("You are already friend of him/her."),
	ALREADY_DENIED("You have already denied him/her."),
	NO_MORE_USER("There are no more users. Please try again later."),
	//Feed related
	FEED_NOT_FOUND("The feed you are looking for does not exist."),
	NO_MORE_FEEDS_FOUND("There are no more feeds."),
	NO_FEED_TO_DELETE("The feed you are trying to delete does not exist."),
	UNAUTHORIZED_FEED_DELETE("Cannot delete an feed that is not yours."),
	INVALID_FEED_COVER("This feed has an invalid cover image."),
	INVALID_FEED_INPUT("Can not create feed with empty title, body or type."),
	FEED_NO_PERMISSION("You do not have permission to create an event. You do not have an admin role."),
	//Comment related
	COMMENT_NOT_FOUND("The comment you are looking for does not exist."),
	NO_COMMENT_TO_DELETE("The comment you are trying to delete does not exist."),
	UNAUTHORIZED_COMMENT_DELETE("Cannot delete a comment that is not yours."),
	COMMENT_ALREADY_EXISTS("Cannot create this comment because it already exists."),
	//Event related
	EVENT_NOT_FOUND("The event you are looking for does not exist."),
	EVENT_NOT_ACTIVE("The event is finished. No payment accepted."),
	NO_MORE_EVENTS_FOUND("There are no more events."),
	CHANGE_EVENT_NOT_ALLOWED("You do not have permission to edit the event."),
	VIEW_PARTICIPANTS_NOT_ALLOWED("You do not have permission to view paticipants list."),
	CHANGE_STATUS_NOT_ALLOWED("You do not have permission to change event status."),
	CHANGE_BALANCE_NOT_ALLOWED("You do not have permission to change event balance."),
	TICKET_BALANCE_RANGE_ERROR("Ticket balance has to be in range 0 to 500."),
	EVENT_WITHOUT_TICKET_TEMPLATE("Payment is processed but there is no ticket template. Please contact support."),
	EVENT_NO_PERMISSION("Article is created but you do not have permission to create event."),
	EVENT_NOT_CREATED("Article created but event failed to be created due to incorrect parameters. Please go to Admin portal and try adding event again to this article."),
	//Ticket related
	TICKET_NOT_FOUND("The ticket you are looking for does not exist."),
	TICKET_NOT_OWNED("You do not own this ticket."),
	TICKET_SOLD_OUT("Tickets have been sold out."),
	TICKET_TEMPLATE_NOT_FOUND("The ticket template you are looking for does not exist."),
	TICKET_INTERNAL_ERROR("Payment is processed successfully but ticket is not generated due to error. Please contact support."),
	TICKET_CREATION_FAILED("Payment is processed but ticket failed to be created. Please contact support."),
	FREE_TICKET_INVALID_EMAIL("You can only get free ticket using wpi email account."),
	//Payment related
	PAYMENT_NOT_FOUND("The payment you are looking for does not exist."),
	PAYMENT_NOT_SUPPORTED("The payment type is not supported."),
	INVALID_PAYMENT_TOKEN("The payment token is invalid."),
	PAYMENT_AMOUNT_INVALID("Payment amount is not correct."),
	PAYMENT_REJECTED("The payment is rejected for your payment method. Please double check your payment info."),
	//Survival Guide related
	SURVIVAL_GUIDE_NOT_FOUND("The survival guide article you are looking for does not exist."),
	SG_INVALID_EMAIL("You can only edit or create survival guide articles using wpi email account."),
	INVALID_SG_INPUT("Can not create survival guide article with empty title or body."),
	//Test case related
	SHOULD_NOT_PASS_ERROR("This method should fail, but passed.");
	 */
	
	private int code;
	private final String message;
	
	ErrorType(int code, String message) {
		this.code = code;
        this.message = message;
    }
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}

}

