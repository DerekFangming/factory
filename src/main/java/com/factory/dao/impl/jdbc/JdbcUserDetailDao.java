package com.factory.dao.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.factory.dao.UserDetailDao;
import com.factory.dao.impl.CoreTableType;
import com.factory.dao.impl.NVPairList;
import com.factory.domain.UserDetail;
import com.factory.exceptions.ErrorType;
import com.factory.exceptions.InternalServerException;
import com.factory.utils.Utils;

@Repository
@Jdbc
public class JdbcUserDetailDao extends JdbcBaseDao<UserDetail> implements UserDetailDao{

	public JdbcUserDetailDao() {
		super(CoreTableType.USER_DETAILS);
	}

	@Override
	protected NVPairList getNVPairs(UserDetail obj) {
		throw new InternalServerException(ErrorType.INSERT_ON_TABLE_JOIN);
	}

	@Override
	protected RowMapper<UserDetail> getRowMapper() {
		RowMapper<UserDetail> rm = new RowMapper<UserDetail>() {
			@Override
			public UserDetail mapRow(ResultSet rs, int row) throws SQLException {
				UserDetail obj = new UserDetail();

				obj.setId(rs.getInt(UserDetailDao.Field.ID.name));
				obj.setPassword(rs.getString(UserDetailDao.Field.PASSWORD.name));
				obj.setAccessToken(rs.getString(UserDetailDao.Field.ACCESS_TOKEN.name));
				obj.setRemember((Boolean)rs.getObject(UserDetailDao.Field.REMEMBER.name));
				obj.setConfirmed((Boolean)rs.getObject(UserDetailDao.Field.CONFIRMED.name));
				obj.setSalt(rs.getString(UserDetailDao.Field.SALT.name));
				obj.setActivated((Boolean)rs.getObject(UserDetailDao.Field.ACTIVATED.name));
				obj.setName(rs.getString(UserDetailDao.Field.NAME.name));
				obj.setPhone(rs.getString(UserDetailDao.Field.PHONE.name));
				obj.setWorkId(rs.getString(UserDetailDao.Field.WORK_ID.name));
				obj.setAvatarId((Integer)rs.getObject(UserDetailDao.Field.AVATAR_ID.name));
				obj.setBirthday(Utils.parseTimestamp(rs.getTimestamp(UserDetailDao.Field.BIRTHDAY.name)));
				obj.setJoinedDate(Utils.parseTimestamp(rs.getTimestamp(UserDetailDao.Field.JOINED_DATE.name)));
				obj.setCompanyName(rs.getString(UserDetailDao.Field.COMPANY_NAME.name));
				obj.setCompanyIndustry(rs.getString(UserDetailDao.Field.INDUSTRY.name));
				obj.setRoleName(rs.getString(UserDetailDao.Field.ROLE_NAME.name));
				obj.setCanCreateTask(rs.getBoolean(UserDetailDao.Field.CAN_CREATE_TASK.name));
				obj.setCanCreateProduct(rs.getBoolean(UserDetailDao.Field.CAN_CREATE_PRODUCT.name));

				return obj;
			}
		};
		return rm;
	}

}
