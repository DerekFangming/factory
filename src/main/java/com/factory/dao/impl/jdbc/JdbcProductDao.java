package com.factory.dao.impl.jdbc;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.factory.dao.ProductDao;
import com.factory.dao.impl.CoreTableType;
import com.factory.dao.impl.NVPairList;
import com.factory.domain.Product;

@Repository
@Jdbc
public class JdbcProductDao extends JdbcBaseDao<Product> implements ProductDao{
	public JdbcProductDao() {
		super(CoreTableType.PRODUCTS);
	}

	@Override
	protected NVPairList getNVPairs(Product obj) {
		NVPairList params = new NVPairList();

		params.addValue(ProductDao.Field.COMPANY_ID.name, obj.getCompanyId());
		params.addValue(ProductDao.Field.MODEL.name, obj.getModel());
		params.addValue(ProductDao.Field.NAME.name, obj.getName());
		params.addValue(ProductDao.Field.DESCRIPTION.name, obj.getDescription());
		params.addValue(ProductDao.Field.COMBINED_PRODUCT.name, obj.getCombinedProduct());
		params.addValue(ProductDao.Field.NET_COST.name, obj.getNetCost());
		params.addValue(ProductDao.Field.MARKET_PRICE.name, obj.getMarketPrice());
		params.addValue(ProductDao.Field.PRICE_VISIBLE_ROLE_ID.name, obj.getPriceVisibleRoleId());
		params.addValue(ProductDao.Field.LABOR_COST.name, obj.getLaborCost());
		params.addValue(ProductDao.Field.VISIBLE_ROLE_ID.name, obj.getVisibleRoleId());
		params.addValue(ProductDao.Field.CREATED_AT.name, Date.from(obj.getCreatedAt()));
		params.addValue(ProductDao.Field.OWNER_ID.name, obj.getOwnerId());
		params.addValue(ProductDao.Field.UPDATED_BY.name, obj.getUpdatedBy());

		return params;
	}

	@Override
	protected RowMapper<Product> getRowMapper() {
		RowMapper<Product> rm = new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int row) throws SQLException {
				Product obj = new Product();

				obj.setId(rs.getInt(ProductDao.Field.ID.name));
				obj.setCompanyId((Integer)rs.getObject(ProductDao.Field.COMPANY_ID.name));
				obj.setModel(rs.getString(ProductDao.Field.MODEL.name));
				obj.setName(rs.getString(ProductDao.Field.NAME.name));
				obj.setDescription(rs.getString(ProductDao.Field.DESCRIPTION.name));
				obj.setCombinedProduct((Boolean)rs.getObject(ProductDao.Field.COMBINED_PRODUCT.name));
				obj.setNetCost((Double)rs.getObject(ProductDao.Field.NET_COST.name));
				obj.setMarketPrice((Double)rs.getObject(ProductDao.Field.MARKET_PRICE.name));
				obj.setPriceVisibleRoleId((Integer)rs.getObject(ProductDao.Field.PRICE_VISIBLE_ROLE_ID.name));
				obj.setLaborCost((Double)rs.getObject(ProductDao.Field.LABOR_COST.name));
				obj.setVisibleRoleId((Integer)rs.getObject(ProductDao.Field.VISIBLE_ROLE_ID.name));
				obj.setCreatedAt(rs.getTimestamp(ProductDao.Field.CREATED_AT.name).toInstant());
				obj.setOwnerId((Integer)rs.getObject(ProductDao.Field.OWNER_ID.name));
				obj.setUpdatedBy((Integer)rs.getObject(ProductDao.Field.UPDATED_BY.name));

				return obj;
			}
		};
		return rm;
	}

}