package com.factory.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.factory.dao.ProductCombinationDao;
import com.factory.dao.impl.CoreTableType;
import com.factory.dao.impl.NVPairList;
import com.factory.domain.ProductCombination;

@Repository
@Jdbc
public class JdbcProductCombinationDao extends JdbcBaseDao<ProductCombination> implements ProductCombinationDao{
	public JdbcProductCombinationDao() {
		super(CoreTableType.PRODUCT_COMBINATIONS);
	}

	@Override
	protected NVPairList getNVPairs(ProductCombination obj) {
		NVPairList params = new NVPairList();

		params.addValue(ProductCombinationDao.Field.PARENT_ID.name, obj.getParentId());
		params.addValue(ProductCombinationDao.Field.CHILD_ID.name, obj.getChildId());
		params.addValue(ProductCombinationDao.Field.COUNT.name, obj.getCount());
		params.addValue(ProductCombinationDao.Field.STEP.name, obj.getStep());
		params.addValue(ProductCombinationDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
		params.addValue(ProductCombinationDao.Field.OWNER_ID.name, obj.getOwnerId());
		params.addValue(ProductCombinationDao.Field.UPDATED_BY.name, obj.getUpdatedBy());

		return params;
	}

	@Override
	protected RowMapper<ProductCombination> getRowMapper() {
		RowMapper<ProductCombination> rm = new RowMapper<ProductCombination>() {
			@Override
			public ProductCombination mapRow(ResultSet rs, int row) throws SQLException {
				ProductCombination obj = new ProductCombination();

				obj.setId(rs.getInt(ProductCombinationDao.Field.ID.name));
				obj.setParentId((Integer)rs.getObject(ProductCombinationDao.Field.PARENT_ID.name));
				obj.setChildId((Integer)rs.getObject(ProductCombinationDao.Field.CHILD_ID.name));
				obj.setCount((Integer)rs.getObject(ProductCombinationDao.Field.COUNT.name));
				obj.setStep((Integer)rs.getObject(ProductCombinationDao.Field.STEP.name));
				obj.setCreatedAt(rs.getTimestamp(ProductCombinationDao.Field.CREATED_AT.name).toInstant());
				obj.setOwnerId((Integer)rs.getObject(ProductCombinationDao.Field.OWNER_ID.name));
				obj.setUpdatedBy((Integer)rs.getObject(ProductCombinationDao.Field.UPDATED_BY.name));

				return obj;
			}
		};
		return rm;
	}

}