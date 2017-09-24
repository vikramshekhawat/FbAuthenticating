package com.reporting.bean;

@Table(value = "facebook_daily_ads_account_performance")
public class DailyAdsPerformanceBean {
	@PrimaryKeyColumn(name = "client_stamp", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	@Column
	private String client_stamp;


}
