package com.factory.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.factory.dao.ImageDao;
import com.factory.dao.impl.CoreTableType;
import com.factory.dao.impl.NVPairList;
import com.factory.domain.Image;

@Repository
@Jdbc
public class JdbcImageDao extends JdbcBaseDao<Image> implements ImageDao{
	public JdbcImageDao() {
		super(CoreTableType.IMAGES);
	}

	@Override
	protected NVPairList getNVPairs(Image obj) {
		NVPairList params = new NVPairList();

		params.addValue(ImageDao.Field.COMPANY_ID.name, obj.getCompanyId());
		params.addValue(ImageDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
		params.addValue(ImageDao.Field.OWNER_ID.name, obj.getOwnerId());

		return params;
	}

	@Override
	protected RowMapper<Image> getRowMapper() {
		RowMapper<Image> rm = new RowMapper<Image>() {
			@Override
			public Image mapRow(ResultSet rs, int row) throws SQLException {
				Image obj = new Image();

				obj.setId(rs.getInt(ImageDao.Field.ID.name));
				obj.setCompanyId((Integer)rs.getObject(ImageDao.Field.COMPANY_ID.name));
				obj.setCreatedAt(rs.getTimestamp(ImageDao.Field.CREATED_AT.name).toInstant());
				obj.setOwnerId((Integer)rs.getObject(ImageDao.Field.OWNER_ID.name));

				return obj;
			}
		};
		return rm;
	}

}