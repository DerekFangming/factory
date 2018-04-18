package com.factory.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.factory.dao.CompanyDao;
import com.factory.dao.impl.CoreTableType;
import com.factory.dao.impl.NVPairList;
import com.factory.domain.Company;

@Repository
@Jdbc
public class JdbcCompanyDao extends JdbcBaseDao<Company> implements CompanyDao{
	public JdbcCompanyDao() {
		super(CoreTableType.COMPANIES);
	}

	@Override
	protected NVPairList getNVPairs(Company obj) {
		NVPairList params = new NVPairList();

		params.addValue(CompanyDao.Field.NAME.name, obj.getName());
		params.addValue(CompanyDao.Field.DESCRIPTION.name, obj.getDescription());
		params.addValue(CompanyDao.Field.INDUSTRY.name, obj.getIndustry());
		params.addValue(CompanyDao.Field.LICENSE_LEVEL.name, obj.getLicenseLevel());
		params.addValue(CompanyDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
		params.addValue(CompanyDao.Field.OWNER_ID.name, obj.getOwnerId());
		params.addValue(CompanyDao.Field.UPDATED_BY.name, obj.getUpdatedBy());

		return params;
	}

	@Override
	protected RowMapper<Company> getRowMapper() {
		RowMapper<Company> rm = new RowMapper<Company>() {
			@Override
			public Company mapRow(ResultSet rs, int row) throws SQLException {
				Company obj = new Company();

				obj.setId(rs.getInt(CompanyDao.Field.ID.name));
				obj.setName(rs.getString(CompanyDao.Field.NAME.name));
				obj.setDescription(rs.getString(CompanyDao.Field.DESCRIPTION.name));
				obj.setIndustry(rs.getString(CompanyDao.Field.INDUSTRY.name));
				obj.setLicenseLevel((Integer)rs.getObject(CompanyDao.Field.LICENSE_LEVEL.name));
				obj.setCreatedAt(rs.getTimestamp(CompanyDao.Field.CREATED_AT.name).toInstant());
				obj.setOwnerId((Integer)rs.getObject(CompanyDao.Field.OWNER_ID.name));
				obj.setUpdatedBy((Integer)rs.getObject(CompanyDao.Field.UPDATED_BY.name));

				return obj;
			}
		};
		return rm;
	}

}