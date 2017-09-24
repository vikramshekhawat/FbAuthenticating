package com.reporting.bean;

@Table(value = "facebook_weekly_ads_account_performance")
public class WeeklyAdsPerformanceBean {
	@PrimaryKeyColumn(name = "client_stamp", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	@Column
	private String client_stamp;

}
