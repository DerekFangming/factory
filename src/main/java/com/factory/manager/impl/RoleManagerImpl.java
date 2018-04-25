package com.factory.manager.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.factory.dao.RoleDao;
import com.factory.dao.impl.QueryTerm;
import com.factory.domain.Role;
import com.factory.domain.type.RoleOffsetType;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.NotFoundException;
import com.factory.manager.RoleManager;

@Component
public class RoleManagerImpl implements RoleManager{
	
	@Autowired private RoleDao roleDao;
	
	@Override
	public int createRole(int companyId, String name, int level, int ownerId, boolean canCreateTask, boolean canCreateProduct) {
		
		Role role = new Role();
		role.setCompanyId(companyId);
		role.setName(name);
		role.setLevel(level);
		role.setLevel(level);
		role.setCreatedAt(Instant.now());
		role.setOwnerId(ownerId);
		role.setCanCreateTask(canCreateTask);
		role.setCanCreateProduct(canCreateProduct);
		
		return roleDao.persist(role);
	}
	
	@Override
	public Role getRoleById(int roleId) throws NotFoundException {
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(RoleDao.Field.ID.getQueryTerm(roleId));
		try{
			return roleDao.findObject(terms);
		} catch(NotFoundException e){
			throw new NotFoundException(ErrorType.ROLE_NOT_FOUND);
		}
	}

	@Override
	public Role getRoleByIdAndLevelOffset(int roleId, RoleOffsetType offsetType) throws NotFoundException {
		Role role = getRoleById(roleId);
		
		List<QueryTerm> terms = new ArrayList<QueryTerm>();
		terms.add(RoleDao.Field.COMPANY_ID.getQueryTerm(role.getCompanyId()));
		terms.add(RoleDao.Field.LEVEL.getQueryTerm(role.getLevel() + offsetType.getOffset()));
		try{
			return roleDao.findObject(terms);
		} catch(NotFoundException e){
			throw new NotFoundException(ErrorType.ROLE_NOT_FOUND);
		}
	}

}
