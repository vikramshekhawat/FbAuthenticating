package com.reporting.bean;

@Table(value = "facebook_daily_adsets_account_performance")
public class DailyAdsetsPerformanceBean {
	@PrimaryKeyColumn(name = "client_stamp", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	@Column
	private String client_stamp;


}
