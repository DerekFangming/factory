package com.factory.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.factory.domain.Role;
import com.factory.domain.type.RoleOffsetType;
import com.factory.exceptions.NotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/sdkUnitTesting.xml")
public class RoleManagerTests {
	
	@Autowired private RoleManager roleManager;
	
	@Test
	public void testCreateRole() {
		try {
			roleManager.createRole(0, "", 1, 0, false, false);
			fail();
		} catch (Exception e) {
			assertEquals(e.getClass(), DataIntegrityViolationException.class);
		}
	}
	
	@Test
	public void testGetRoleById() {
		Role role = roleManager.getRoleById(1);
		
		assertEquals(role.getLevel().intValue(), 0);
		assertEquals(role.getCompanyId().intValue(), 1);
	}
	
	@Test
	public void testGetLowerLevelForSuperAdmin() {
		try {
			roleManager.getRoleByIdAndLevelOffset(1, RoleOffsetType.ONE_LEVEL_DOWN);
			fail();
		} catch (NotFoundException e) {}
		
		try {
			Role role = roleManager.getRoleByIdAndLevelOffset(2, RoleOffsetType.ONE_LEVEL_DOWN);
			assertEquals(role.getLevel().intValue(), 1);
			assertEquals(role.getName(), "Senior VP");
		} catch (NotFoundException e) {
			fail();
		}
	}
	
	@Test
	public void testGetAllRoles() {
		try {
			roleManager.getAllRoles(1, 1000000, 1);
			fail();
		} catch (Exception e) {
			assertEquals(e.getClass(), NotFoundException.class);
		}
		
		List<Role> roleList = roleManager.getAllRoles(1, 0, 1);
		assertEquals(roleList.size(), 1);
	}

}
