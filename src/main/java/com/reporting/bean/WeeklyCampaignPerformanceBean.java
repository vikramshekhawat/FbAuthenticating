package com.reporting.bean;

@Table(value = "facebook_weekly_campaign_account_performance")
public class WeeklyCampaignPerformanceBean {
	@PrimaryKeyColumn(name = "client_stamp", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	@Column
	private String client_stamp;
	

}
