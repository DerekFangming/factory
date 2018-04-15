package com.factory.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.factory.dao.RoleDao;
import com.factory.dao.impl.CoreTableType;
import com.factory.dao.impl.NVPairList;
import com.factory.domain.Role;

@Repository
@Jdbc
public class JdbcRoleDao extends JdbcBaseDao<Role> implements RoleDao{
	public JdbcRoleDao() {
		super(CoreTableType.ROLES);
	}

	@Override
	protected NVPairList getNVPairs(Role obj) {
		NVPairList params = new NVPairList();

		params.addValue(RoleDao.Field.COMPANY_ID.name, obj.getCompanyId());
		params.addValue(RoleDao.Field.NAME.name, obj.getName());
		params.addValue(RoleDao.Field.LEVEL.name, obj.getLevel());
		params.addValue(RoleDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
		params.addValue(RoleDao.Field.OWNER_ID.name, obj.getOwnerId());
		params.addValue(RoleDao.Field.UPDATED_BY.name, obj.getUpdatedBy());

		return params;
	}

	@Override
	protected RowMapper<Role> getRowMapper() {
		RowMapper<Role> rm = new RowMapper<Role>() {
			@Override
			public Role mapRow(ResultSet rs, int row) throws SQLException {
				Role obj = new Role();

				obj.setId(rs.getInt(RoleDao.Field.ID.name));
				obj.setCompanyId((Integer)rs.getObject(RoleDao.Field.COMPANY_ID.name));
				obj.setName(rs.getString(RoleDao.Field.NAME.name));
				obj.setLevel((Integer)rs.getObject(RoleDao.Field.LEVEL.name));
				obj.setCreatedAt(rs.getTimestamp(RoleDao.Field.CREATED_AT.name).toInstant());
				obj.setOwnerId((Integer)rs.getObject(RoleDao.Field.OWNER_ID.name));
				obj.setUpdatedBy((Integer)rs.getObject(RoleDao.Field.UPDATED_BY.name));

				return obj;
			}
		};
		return rm;
	}

}