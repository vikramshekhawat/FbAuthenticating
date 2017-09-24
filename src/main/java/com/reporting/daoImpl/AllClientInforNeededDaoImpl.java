package com.reporting.daoImpl;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.reporting.dao.AllClientInfoNeededDao;

public class AllClientInforNeededDaoImpl implements AllClientInfoNeededDao {

	DataSource dataSource;

	JdbcTemplate jdbcTemplate;

	@Autowired
	Environment envBean;

	public AllClientInforNeededDaoImpl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Map<String, Object>> getAllClientInfo() {

		String query = "Select " + envBean.getProperty("client_stamp_column") + " ,"
				+ envBean.getProperty("account_id_column") + " , " + envBean.getProperty("customer_name_column") + " , "
				+ envBean.getProperty("business_id_column") + " , " + envBean.getProperty("access_token_column")
				+ " FROM " + envBean.getProperty("facebook_table_client_config_table") + " ;";

		List<Map<String, Object>> results = jdbcTemplate.queryForList(query);
		return results;
	}

}
