package com.factory.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.factory.dao.ErrorLogDao;
import com.factory.dao.impl.CoreTableType;
import com.factory.dao.impl.NVPairList;
import com.factory.domain.ErrorLog;

@Repository
@Jdbc
public class JdbcErrorLogDao extends JdbcBaseDao<ErrorLog> implements ErrorLogDao{
	public JdbcErrorLogDao() {
		super(CoreTableType.ERROR_LOGS);
	}

	@Override
	protected NVPairList getNVPairs(ErrorLog obj) {
		NVPairList params = new NVPairList();

		params.addValue(ErrorLogDao.Field.URL.name, obj.getUrl());
		params.addValue(ErrorLogDao.Field.PARAM.name, obj.getParam());
		params.addValue(ErrorLogDao.Field.TRACE.name, obj.getTrace());
		params.addValue(ErrorLogDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));

		return params;
	}

	@Override
	protected RowMapper<ErrorLog> getRowMapper() {
		RowMapper<ErrorLog> rm = new RowMapper<ErrorLog>() {
			@Override
			public ErrorLog mapRow(ResultSet rs, int row) throws SQLException {
				ErrorLog obj = new ErrorLog();

				obj.setId(rs.getInt(ErrorLogDao.Field.ID.name));
				obj.setUrl(rs.getString(ErrorLogDao.Field.URL.name));
				obj.setParam(rs.getString(ErrorLogDao.Field.PARAM.name));
				obj.setTrace(rs.getString(ErrorLogDao.Field.TRACE.name));
				obj.setCreatedAt(rs.getTimestamp(ErrorLogDao.Field.CREATED_AT.name).toInstant());

				return obj;
			}
		};
		return rm;
	}

}