package com.factory.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.factory.dao.UserActivationDao;
import com.factory.dao.impl.CoreTableType;
import com.factory.dao.impl.NVPairList;
import com.factory.domain.UserActivation;

@Repository
@Jdbc
public class JdbcUserActivationDao extends JdbcBaseDao<UserActivation> implements UserActivationDao{
	public JdbcUserActivationDao() {
		super(CoreTableType.USER_ACTIVATIONS);
	}

	@Override
	protected NVPairList getNVPairs(UserActivation obj) {
		NVPairList params = new NVPairList();

		params.addValue(UserActivationDao.Field.REQUESTER_ID.name, obj.getRequesterId());
		params.addValue(UserActivationDao.Field.RESPONDER_ID.name, obj.getResponderId());
		params.addValue(UserActivationDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));

		return params;
	}

	@Override
	protected RowMapper<UserActivation> getRowMapper() {
		RowMapper<UserActivation> rm = new RowMapper<UserActivation>() {
			@Override
			public UserActivation mapRow(ResultSet rs, int row) throws SQLException {
				UserActivation obj = new UserActivation();

				obj.setId(rs.getInt(UserActivationDao.Field.ID.name));
				obj.setRequesterId((Integer)rs.getObject(UserActivationDao.Field.REQUESTER_ID.name));
				obj.setResponderId((Integer)rs.getObject(UserActivationDao.Field.RESPONDER_ID.name));
				obj.setCreatedAt(rs.getTimestamp(UserActivationDao.Field.CREATED_AT.name).toInstant());

				return obj;
			}
		};
		return rm;
	}

}