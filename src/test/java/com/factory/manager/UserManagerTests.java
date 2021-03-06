package com.factory.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.factory.domain.User;
import com.factory.domain.UserDetail;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.InvalidParamException;
import com.factory.exceptions.InvalidStateException;
import com.factory.exceptions.NotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/sdkUnitTesting.xml")
public class UserManagerTests {
	
	@Autowired private UserManager userManager;
	
	@Test
	public void testCreateUser() {
		try {
			userManager.createUser("synfm@factory.com", "", null, null, null, true, "", null,
					null, null, null, true, "", null, null, null, null, null);
			fail();
		} catch (Exception e) {
			assertEquals(e.getClass(), DataIntegrityViolationException.class);
		}
		
		try {
			userManager.createUser("synfm@factory.com", "", null, true, null, true, "", null,
					1, null, 1, true, "", null, null, null, null, null);
			fail();
		} catch (Exception e) {
			assertEquals(e.getClass(), DuplicateKeyException.class);
		}
	}
	
	@Test
	public void testGetUserByUsername() {
		User user = userManager.getUserByUsername("syNfm@factory.com");
		
		assertEquals(user.getId(), 1);
		assertEquals(user.getName(), "Super Admin");
		assertTrue(user.getActivated());
		
	}
	
	@Test
	public void testGetUserByRegCode() {
		try {
			userManager.getUserByRegCode("fake");
			fail();
		} catch (NotFoundException e) {
			assertEquals(e.getErrorType(), ErrorType.USER_NOT_FOUND);
		}
		
		try {
			userManager.getUserByRegCode("fake", ErrorType.UNKNOWN_ERROR);
			fail();
		} catch (NotFoundException e) {
			assertEquals(e.getErrorType(), ErrorType.UNKNOWN_ERROR);
		}
		
		User user = userManager.getUserByRegCode("11B111");
		assertEquals(user.getId(), 2);
		assertEquals(user.getName(), "Xiao ming");
	}
	
	@Test
	public void testValidateAccessToken() {
		try {
			userManager.validateAccessToken(new HashMap<>());
			fail();
		} catch (Exception e) {
			assertEquals(e.getClass(), InvalidParamException.class);
		}
		
		try {
			Map<String, Object> request = new HashMap<>();
			request.put("accessToken", "");
			userManager.validateAccessToken(request);
			fail();
		} catch (Exception e) {
			assertEquals(e.getClass(), InvalidStateException.class);
		}
	}
	
	@Test
	public void testUpdateInvalidUser() {
		try {
			userManager.updateUserNotNull(0, "", null, null, null, null, null, null,
					null, null, null, null, null, null, null, null, null, null);
			fail();
		} catch (Exception e) {
			assertEquals(e.getClass(), NotFoundException.class);
		}
	}
	
	@Test
	public void testGetUserDetails() {
		UserDetail userDetail = userManager.getUserDetailById(1);
		assertEquals(userDetail.getName(), "Super Admin");
		assertEquals(userDetail.getRoleName(), "Super Admin");
		assertEquals(userDetail.getCompanyName(), "Factory");
		assertEquals(userDetail.getCompanyIndustry(), "IT");
		assertNull(userDetail.getPhone());
		assertNull(userDetail.getWorkId());
		assertNull(userDetail.getPhone());
	}
	
	@Test
	public void testSaveUserActivation() {
		try {
			userManager.createUserActivation(0, 1);
			fail();
		} catch (Exception e) {
			assertEquals(e.getClass(), DataIntegrityViolationException.class);
		}
		
		try {
			userManager.createUserActivation(1, 0);
			fail();
		} catch (Exception e) {
			assertEquals(e.getClass(), DataIntegrityViolationException.class);
		}
	}
	
	@Test
	public void testGetAllUsers() {
		try {
			userManager.getAllUserDetails(1, 1000000, 1);
			fail();
		} catch (Exception e) {
			assertEquals(e.getClass(), NotFoundException.class);
		}
		
		List<UserDetail> userList = userManager.getAllUserDetails(1, 0, 1);
		assertEquals(userList.size(), 1);
	}

}
