package com.reporting.bean;

@Table(value = "facebook_daily_campaign_account_performance")
public class DailyCampaignPerformanceBean {
	@PrimaryKeyColumn(name = "client_stamp", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	@Column
	private String client_stamp;
	

}
