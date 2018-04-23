package com.factory.manager;

import com.factory.domain.Role;
import com.factory.domain.type.RoleOffsetType;
import com.factory.exceptions.NotFoundException;

public interface RoleManager {
	/*
	 * company_id integer not null REFERENCES companies,
	name text not null,
	level integer not null,
	created_at timestamp without time zone NOT NULL,
	owner_id integer not null,
	updated_by integer,
	can_create_task boolean not null,
	can_create_product boolean not null
	 * */
	
	public int createRole(int companyId, String name, int level, int ownerId, boolean canCreateTask, boolean canCreateProduct);
	
	public Role getRoleById(int roleId) throws NotFoundException;
	
	public Role getRoleByIdAndLevelOffset(int roleId, RoleOffsetType offset) throws NotFoundException;

}
