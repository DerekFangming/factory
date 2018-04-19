package com.factory.manager;

import com.factory.domain.Role;
import com.factory.domain.type.RoleOffsetType;
import com.factory.exceptions.NotFoundException;

public interface RoleManager {
	
	public Role getRoleById(int roleId) throws NotFoundException;
	
	public Role getRoleByIdAndLevelOffset(int roleId, RoleOffsetType offset) throws NotFoundException;

}
