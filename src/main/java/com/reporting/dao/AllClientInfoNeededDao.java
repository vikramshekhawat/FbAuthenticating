package com.reporting.dao;

import java.util.List;
import java.util.Map;

public interface AllClientInfoNeededDao {

	List<Map<String, Object>> getAllClientInfo();

	// List<Map<String, Object>> getAllClientInfo(Long accountId);

}
