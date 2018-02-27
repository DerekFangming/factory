package com.factory.dao.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.factory.dao.AboutDao;
import com.factory.dao.impl.CoreTableType;
import com.factory.dao.impl.NVPairList;
import com.factory.domain.About;

@Repository
@Jdbc
public class JdbcAboutDao extends JdbcBaseDao<About> implements AboutDao{
	public JdbcAboutDao() {
		super(CoreTableType.ABOUT);
	}

	@Override
	protected NVPairList getNVPairs(About obj) {
		NVPairList params = new NVPairList();

		params.addValue(AboutDao.Field.TEST.name, obj.getTest());
		params.addValue(AboutDao.Field.NUMBER.name, obj.getNumber());
		params.addValue(AboutDao.Field.BOOL.name, obj.getBool());

		return params;
	}

	@Override
	protected RowMapper<About> getRowMapper() {
		RowMapper<About> rm = new RowMapper<About>() {
			@Override
			public About mapRow(ResultSet rs, int row) throws SQLException {
				About obj = new About();

				obj.setId(rs.getInt(AboutDao.Field.ID.name));
				obj.setTest(rs.getString(AboutDao.Field.TEST.name));
				obj.setNumber((Integer)rs.getObject(AboutDao.Field.NUMBER.name));
				obj.setBool((Boolean)rs.getObject(AboutDao.Field.BOOL.name));

				return obj;
			}
		};
		return rm;
	}

}
