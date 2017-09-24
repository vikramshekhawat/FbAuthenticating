package com.reporting.bean;

@Table(value = "facebook_monthly_ads_account_performance")
public class MonthlyAdsPerformanceBean {
	@PrimaryKeyColumn(name = "client_stamp", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	@Column
	private String client_stamp;


}
