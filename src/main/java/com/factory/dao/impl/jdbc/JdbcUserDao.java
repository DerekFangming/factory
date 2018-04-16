package com.factory.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.factory.dao.UserDao;
import com.factory.dao.impl.CoreTableType;
import com.factory.dao.impl.NVPairList;
import com.factory.domain.User;
import com.factory.utils.Utils;

@Repository
@Jdbc
public class JdbcUserDao extends JdbcBaseDao<User> implements UserDao{
	public JdbcUserDao() {
		super(CoreTableType.USERS);
	}

	@Override
	protected NVPairList getNVPairs(User obj) {
		NVPairList params = new NVPairList();

		params.addValue(UserDao.Field.USERNAME.name, obj.getUsername());
		params.addValue(UserDao.Field.PASSWORD.name, obj.getPassword());
		params.addValue(UserDao.Field.ACCESS_TOKEN.name, obj.getAccessToken());
		params.addValue(UserDao.Field.VERIFICATION_CODE.name, obj.getVerificationCode());
		params.addValue(UserDao.Field.CONFIRMED.name, obj.getConfirmed());
		params.addValue(UserDao.Field.SALT.name, obj.getSalt());
		params.addValue(UserDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
		params.addValue(UserDao.Field.UPDATED_BY.name, obj.getUpdatedBy());
		params.addValue(UserDao.Field.ROLE_ID.name, obj.getRoleId());
		params.addValue(UserDao.Field.MANAGER_ID.name, obj.getManagerId());
		params.addValue(UserDao.Field.COMPANY_ID.name, obj.getCompanyId());
		params.addValue(UserDao.Field.REGISTRATION_CODE.name, obj.getRegistrationCode());
		params.addValue(UserDao.Field.VERIFICATION_NEEDED.name, obj.getVerificationNeeded());
		params.addValue(UserDao.Field.ACTIVATED.name, obj.getActivated());
		params.addValue(UserDao.Field.NAME.name, obj.getName());
		params.addValue(UserDao.Field.PHONE.name, obj.getPhone());
		params.addValue(UserDao.Field.WORK_ID.name, obj.getWorkId());
		params.addValue(UserDao.Field.AVATAR_ID.name, obj.getAvatarId());
		params.addValue(UserDao.Field.BIRTHDAY.name, Utils.parseDate(obj.getBirthday()));
		params.addValue(UserDao.Field.JOINED_DATE.name, Utils.parseDate(obj.getJoinedDate()));

		return params;
	}

	@Override
	protected RowMapper<User> getRowMapper() {
		RowMapper<User> rm = new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int row) throws SQLException {
				User obj = new User();

				obj.setId(rs.getInt(UserDao.Field.ID.name));
				obj.setUsername(rs.getString(UserDao.Field.USERNAME.name));
				obj.setPassword(rs.getString(UserDao.Field.PASSWORD.name));
				obj.setAccessToken(rs.getString(UserDao.Field.ACCESS_TOKEN.name));
				obj.setVerificationCode(rs.getString(UserDao.Field.VERIFICATION_CODE.name));
				obj.setConfirmed((Boolean)rs.getObject(UserDao.Field.CONFIRMED.name));
				obj.setSalt(rs.getString(UserDao.Field.SALT.name));
				obj.setCreatedAt(rs.getTimestamp(UserDao.Field.CREATED_AT.name).toInstant());
				obj.setUpdatedBy((Integer)rs.getObject(UserDao.Field.UPDATED_BY.name));
				obj.setRoleId((Integer)rs.getObject(UserDao.Field.ROLE_ID.name));
				obj.setManagerId((Integer)rs.getObject(UserDao.Field.MANAGER_ID.name));
				obj.setCompanyId((Integer)rs.getObject(UserDao.Field.COMPANY_ID.name));
				obj.setRegistrationCode((Integer)rs.getObject(UserDao.Field.REGISTRATION_CODE.name));
				obj.setVerificationNeeded((Boolean)rs.getObject(UserDao.Field.VERIFICATION_NEEDED.name));
				obj.setActivated((Boolean)rs.getObject(UserDao.Field.ACTIVATED.name));
				obj.setName(rs.getString(UserDao.Field.NAME.name));
				obj.setPhone(rs.getString(UserDao.Field.PHONE.name));
				obj.setWorkId(rs.getString(UserDao.Field.WORK_ID.name));
				obj.setAvatarId((Integer)rs.getObject(UserDao.Field.AVATAR_ID.name));
				obj.setBirthday(Utils.parseTimestamp(rs.getTimestamp(UserDao.Field.BIRTHDAY.name)));
				obj.setJoinedDate(Utils.parseTimestamp(rs.getTimestamp(UserDao.Field.JOINED_DATE.name)));

				return obj;
			}
		};
		return rm;
	}

}