package com.reporting.dao;

import java.io.IOException;
import java.util.List;

import com.reporting.bean.AccountPerformanceBean;

public interface CassandraDao {

	public void writeAccountDataToCassandraDaily(String account_id,String accessToken,String client_stamp) throws IOException;
	
	public void writeAccountDataToCassandraWeekly(String accessToken,String account_id,String client_stamp)  throws IOException;

	public void writeAccountDataToCassandraMonthly(String accessToken,String account_id,String client_stamp) throws IOException;
    public void writeCampaignDataToCassandraDaily(String account_id,String accessToken,String client_stamp) throws IOException;
	
	public void writeCampaignDataToCassandraWeekly(String accessToken,String account_id,String client_stamp)  throws IOException;

	public void writeCampaignDataToCassandraMonthly(String accessToken,String account_id,String client_stamp) throws IOException;

    public void writeAdsDataToCassandraDaily(String account_id,String accessToken,String client_stamp) throws IOException;
	
	public void writeAdsDataToCassandraWeekly(String accessToken,String account_id,String client_stamp)  throws IOException;

	public void writeAdsDataToCassandraMonthly(String accessToken,String account_id,String client_stamp) throws IOException;

public void writeAdsetsDataToCassandraDaily(String account_id,String accessToken,String client_stamp) throws IOException;
	
	public void writeAdsetsDataToCassandraWeekly(String accessToken,String account_id,String client_stamp)  throws IOException;

	public void writeAdsetsDataToCassandraMonthly(String accessToken,String account_id,String client_stamp) throws IOException;



}
		

