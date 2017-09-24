package com.reporting.daoImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.cassandra.core.CassandraTemplate;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.reporting.bean.AccountPerformanceBean;
import com.reporting.bean.CassandraCredentials;
import com.reporting.bean.DailyAdsPerformanceBean;
import com.reporting.bean.DailyAdsetsPerformanceBean;
import com.reporting.bean.DailyCampaignPerformanceBean;
import com.reporting.bean.MonthlyAccountPerformanceBean;
import com.reporting.bean.MonthlyAdsPerformanceBean;
import com.reporting.bean.MonthlyAdsetsPerformanceBean;
import com.reporting.bean.MonthlyCampaignPerformanceBean;
import com.reporting.bean.WeeklyAccountPerformanceBean;
import com.reporting.bean.WeeklyAdsPerformanceBean;
import com.reporting.bean.WeeklyAdsetsPerformanceBean;
import com.reporting.bean.WeeklyCampaignPerformanceBean;
import com.reporting.dao.CassandraDao;

public class CassandraCRUDImpl implements CassandraDao {

	private Cluster cluster;

	private Session session;
	CassandraCredentials cassandraCredentials;

	List<InetSocketAddress> address;

	private CassandraTemplate cassandraOperations;

	public void getConection() {

		if (session != null) {

		} else {

			address = new ArrayList<InetSocketAddress>();
			address.add(new InetSocketAddress(cassandraCredentials.getContactpoint(),
					Integer.parseInt(cassandraCredentials.getPort())));
			try {
				cluster = Cluster.builder().addContactPointsWithPorts(address).build();

				/*
				 * cluster =
				 * Cluster.builder().addContactPointsWithPorts(address).
				 * withCredentials("root", "Dig$dev%") .build();
				 */

				session = cluster.connect(cassandraCredentials.getDb());
				cassandraOperations = new CassandraTemplate(session);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public CassandraCRUDImpl() {

	}

	public CassandraCRUDImpl(CassandraCredentials cassandraCredentials) {
		this.cassandraCredentials = cassandraCredentials;

	}

	List<WeeklyAccountPerformanceBean> weeklyAccount = new ArrayList<WeeklyAccountPerformanceBean>();
	List<MonthlyAccountPerformanceBean> monthlyAccount = new ArrayList<MonthlyAccountPerformanceBean>();
	List<AccountPerformanceBean> dailyAccount = new ArrayList<AccountPerformanceBean>();
	List<WeeklyCampaignPerformanceBean> weeklyCampaignAccount = new ArrayList<WeeklyCampaignPerformanceBean>();
	List<MonthlyCampaignPerformanceBean> monthlyCampaignAccount = new ArrayList<MonthlyCampaignPerformanceBean>();
	List<DailyCampaignPerformanceBean> dailyCampaignAccount = new ArrayList<DailyCampaignPerformanceBean>();
	List<WeeklyAdsPerformanceBean> weeklyAdsAccount = new ArrayList<WeeklyAdsPerformanceBean>();
	List<MonthlyAdsPerformanceBean> monthlyAdsAccount = new ArrayList<MonthlyAdsPerformanceBean>();
	List<DailyAdsPerformanceBean> dailyAdsAccount = new ArrayList<DailyAdsPerformanceBean>();
	List<WeeklyAdsetsPerformanceBean> weeklyAdsetsAccount = new ArrayList<WeeklyAdsetsPerformanceBean>();
	List<MonthlyAdsetsPerformanceBean> monthlyAdsetsAccount = new ArrayList<MonthlyAdsetsPerformanceBean>();
	List<DailyAdsetsPerformanceBean> dailyAdsetsAccount = new ArrayList<DailyAdsetsPerformanceBean>();

	public void writeAccountDataToCassandraDaily(String accessToken, String account_id, String client_stamp)
			throws IOException {

		URL url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&&level=account&date_preset=yesterday"
				+ "&access_token=" + accessToken);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output = "", full = "";
		JSONObject result1;
		while ((output = br.readLine()) != null) {
			System.out.println(output);
			full += output;
			JSONObject JObject = new JSONObject(output);
			JSONArray result = JObject.getJSONArray("data");
			result1 = result.getJSONObject(0);

			AccountPerformanceBean addAdAccountData = new AccountPerformanceBean(result1.getString("date_start"),
					result1.getString("date_stop"), result1.getString("account_name"), result1.getString("account_id"),
					result1.getString("social_reach"), result1.getString("spend"),
					result1.getString("inline_link_clicks"), result1.getString("frequency"),
					result1.getString("impressions"), result1.getString("clicks"), result1.getString("cpc"),
					result1.getString("ctr"), client_stamp);
			dailyAccount.add(addAdAccountData);

		}

	}

	public void writeAccountDataToCassandraWeekly(String accessToken, String account_id, String client_stamp)
			throws IOException {

		URL Weekly_url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&level=account&date_preset=last_week_mon_sun"
				+ "&access_token=" + accessToken);

		HttpURLConnection weekly_conn = (HttpURLConnection) Weekly_url.openConnection();
		weekly_conn.setRequestMethod("GET");
		weekly_conn.setRequestProperty("Accept", "application/json");

		if (weekly_conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + weekly_conn.getResponseCode());
		}
		BufferedReader Weekly_br = new BufferedReader(new InputStreamReader((weekly_conn.getInputStream())));

		String weeklyOutput = "", data = "";
		JSONObject weekly_result;
		while ((weeklyOutput = Weekly_br.readLine()) != null) {
			System.out.println(weeklyOutput);
			data += weeklyOutput;
			JSONObject WeeklyJObject = new JSONObject(weeklyOutput);
			JSONArray Weeklyresult = WeeklyJObject.getJSONArray("data");
			weekly_result = Weeklyresult.getJSONObject(0);

			WeeklyAccountPerformanceBean addWeekly = new WeeklyAccountPerformanceBean(
					weekly_result.getString("date_start"), weekly_result.getString("date_stop"),
					weekly_result.getString("account_name"), weekly_result.getString("account_id"),
					weekly_result.getString("social_reach"), weekly_result.getString("spend"),
					weekly_result.getString("inline_link_clicks"), weekly_result.getString("frequency"),
					weekly_result.getString("impressions"), weekly_result.getString("clicks"),
					weekly_result.getString("cpc"), weekly_result.getString("ctr"), client_stamp);
			weeklyAccount.add(addWeekly);
		}

	}

	public void writeAccountDataToCassandraMonthly(String accessToken, String account_id, String client_stamp)
			throws IOException {

		URL Monthly_url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&level=account&date_preset=last_month"
				+ "&access_token=" + accessToken);

		HttpURLConnection monthly_conn = (HttpURLConnection) Monthly_url.openConnection();
		monthly_conn.setRequestMethod("GET");
		monthly_conn.setRequestProperty("Accept", "application/json");

		if (monthly_conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + monthly_conn.getResponseCode());
		}
		BufferedReader monthly_br = new BufferedReader(new InputStreamReader((monthly_conn.getInputStream())));

		String monthlyOutput = "", monthlydata = "";
		JSONObject monthly_result;
		while ((monthlyOutput = monthly_br.readLine()) != null) {
			System.out.println(monthlyOutput);
			monthlydata += monthlyOutput;
			JSONObject MonthlyJObject = new JSONObject(monthlyOutput);
			JSONArray Monthlyresult = MonthlyJObject.getJSONArray("data");
			monthly_result = Monthlyresult.getJSONObject(0);

			MonthlyAccountPerformanceBean addMonthly = new MonthlyAccountPerformanceBean(
					monthly_result.getString("date_start"), monthly_result.getString("date_stop"),
					monthly_result.getString("account_name"), monthly_result.getString("account_id"),
					monthly_result.getString("social_reach"), monthly_result.getString("spend"),
					monthly_result.getString("inline_link_clicks"), monthly_result.getString("frequency"),
					monthly_result.getString("impressions"), monthly_result.getString("clicks"),
					monthly_result.getString("cpc"), monthly_result.getString("ctr"), client_stamp);
			monthlyAccount.add(addMonthly);
		}
	}

	public void writeCampaignDataToCassandraDaily(String accessToken, String account_id, String client_stamp)
			throws IOException {
		URL url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&&level=account&date_preset=yesterday"
				+ "&access_token=" + accessToken);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output = "", full = "";
		JSONObject result1;
		while ((output = br.readLine()) != null) {
			System.out.println(output);
			full += output;
			JSONObject JObject = new JSONObject(output);
			JSONArray result = JObject.getJSONArray("data");
			result1 = result.getJSONObject(0);

			DailyCampaignPerformanceBean addAdAccountData = new DailyCampaignPerformanceBean(
					result1.getString("date_start"), result1.getString("date_stop"), result1.getString("account_name"),
					result1.getString("account_id"), result1.getString("social_reach"), result1.getString("spend"),
					result1.getString("inline_link_clicks"), result1.getString("frequency"),
					result1.getString("impressions"), result1.getString("clicks"), result1.getString("cpc"),
					result1.getString("ctr"), client_stamp);
			dailyCampaignAccount.add(addAdAccountData);

		}

	}

	public void writeCampaignDataToCassandraWeekly(String accessToken, String account_id, String client_stamp)
			throws IOException {
		URL Weekly_url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&level=account&date_preset=last_week_mon_sun"
				+ "&access_token=" + accessToken);

		HttpURLConnection weekly_conn = (HttpURLConnection) Weekly_url.openConnection();
		weekly_conn.setRequestMethod("GET");
		weekly_conn.setRequestProperty("Accept", "application/json");

		if (weekly_conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + weekly_conn.getResponseCode());
		}
		BufferedReader Weekly_br = new BufferedReader(new InputStreamReader((weekly_conn.getInputStream())));

		String weeklyOutput = "", data = "";
		JSONObject weekly_result;
		while ((weeklyOutput = Weekly_br.readLine()) != null) {
			System.out.println(weeklyOutput);
			data += weeklyOutput;
			JSONObject WeeklyJObject = new JSONObject(weeklyOutput);
			JSONArray Weeklyresult = WeeklyJObject.getJSONArray("data");
			weekly_result = Weeklyresult.getJSONObject(0);

			WeeklyCampaignPerformanceBean addWeekly = new WeeklyCampaignPerformanceBean(
					weekly_result.getString("date_start"), weekly_result.getString("date_stop"),
					weekly_result.getString("account_name"), weekly_result.getString("account_id"),
					weekly_result.getString("social_reach"), weekly_result.getString("spend"),
					weekly_result.getString("inline_link_clicks"), weekly_result.getString("frequency"),
					weekly_result.getString("impressions"), weekly_result.getString("clicks"),
					weekly_result.getString("cpc"), weekly_result.getString("ctr"), client_stamp);
			weeklyCampaignAccount.add(addWeekly);
		}
	}

	public void writeCampaignDataToCassandraMonthly(String accessToken, String account_id, String client_stamp)
			throws IOException {
		URL Monthly_url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&level=account&date_preset=last_month"
				+ "&access_token=" + accessToken);

		HttpURLConnection monthly_conn = (HttpURLConnection) Monthly_url.openConnection();
		monthly_conn.setRequestMethod("GET");
		monthly_conn.setRequestProperty("Accept", "application/json");

		if (monthly_conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + monthly_conn.getResponseCode());
		}
		BufferedReader monthly_br = new BufferedReader(new InputStreamReader((monthly_conn.getInputStream())));

		String monthlyOutput = "", monthlydata = "";
		JSONObject monthly_result;
		while ((monthlyOutput = monthly_br.readLine()) != null) {
			System.out.println(monthlyOutput);
			monthlydata += monthlyOutput;
			JSONObject MonthlyJObject = new JSONObject(monthlyOutput);
			JSONArray Monthlyresult = MonthlyJObject.getJSONArray("data");
			monthly_result = Monthlyresult.getJSONObject(0);

			MonthlyCampaignPerformanceBean addMonthly = new MonthlyCampaignPerformanceBean(
					monthly_result.getString("date_start"), monthly_result.getString("date_stop"),
					monthly_result.getString("account_name"), monthly_result.getString("account_id"),
					monthly_result.getString("social_reach"), monthly_result.getString("spend"),
					monthly_result.getString("inline_link_clicks"), monthly_result.getString("frequency"),
					monthly_result.getString("impressions"), monthly_result.getString("clicks"),
					monthly_result.getString("cpc"), monthly_result.getString("ctr"), client_stamp);
			monthlyCampaignAccount.add(addMonthly);
		}
	}

	public void writeAdsDataToCassandraDaily(String account_id, String accessToken, String client_stamp)
			throws IOException {
		URL url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&&level=account&date_preset=yesterday"
				+ "&access_token=" + accessToken);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output = "", full = "";
		JSONObject result1;
		while ((output = br.readLine()) != null) {
			System.out.println(output);
			full += output;
			JSONObject JObject = new JSONObject(output);
			JSONArray result = JObject.getJSONArray("data");
			result1 = result.getJSONObject(0);

			DailyAdsPerformanceBean addAdAccountData = new DailyAdsPerformanceBean(result1.getString("date_start"),
					result1.getString("date_stop"), result1.getString("account_name"), result1.getString("account_id"),
					result1.getString("social_reach"), result1.getString("spend"),
					result1.getString("inline_link_clicks"), result1.getString("frequency"),
					result1.getString("impressions"), result1.getString("clicks"), result1.getString("cpc"),
					result1.getString("ctr"), client_stamp);
			dailyAdsAccount.add(addAdAccountData);

		}

	}

	public void writeAdsDataToCassandraWeekly(String accessToken, String account_id, String client_stamp)
			throws IOException {
		URL Weekly_url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&level=account&date_preset=last_week_mon_sun"
				+ "&access_token=" + accessToken);

		HttpURLConnection weekly_conn = (HttpURLConnection) Weekly_url.openConnection();
		weekly_conn.setRequestMethod("GET");
		weekly_conn.setRequestProperty("Accept", "application/json");

		if (weekly_conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + weekly_conn.getResponseCode());
		}
		BufferedReader Weekly_br = new BufferedReader(new InputStreamReader((weekly_conn.getInputStream())));

		String weeklyOutput = "", data = "";
		JSONObject weekly_result;
		while ((weeklyOutput = Weekly_br.readLine()) != null) {
			System.out.println(weeklyOutput);
			data += weeklyOutput;
			JSONObject WeeklyJObject = new JSONObject(weeklyOutput);
			JSONArray Weeklyresult = WeeklyJObject.getJSONArray("data");
			weekly_result = Weeklyresult.getJSONObject(0);

			WeeklyAdsPerformanceBean addWeekly = new WeeklyAdsPerformanceBean(weekly_result.getString("date_start"),
					weekly_result.getString("date_stop"), weekly_result.getString("account_name"),
					weekly_result.getString("account_id"), weekly_result.getString("social_reach"),
					weekly_result.getString("spend"), weekly_result.getString("inline_link_clicks"),
					weekly_result.getString("frequency"), weekly_result.getString("impressions"),
					weekly_result.getString("clicks"), weekly_result.getString("cpc"), weekly_result.getString("ctr"),
					client_stamp);
			weeklyAdsAccount.add(addWeekly);
		}

	}

	public void writeAdsDataToCassandraMonthly(String accessToken, String account_id, String client_stamp)
			throws IOException {
		URL Monthly_url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&level=account&date_preset=last_month"
				+ "&access_token=" + accessToken);

		HttpURLConnection monthly_conn = (HttpURLConnection) Monthly_url.openConnection();
		monthly_conn.setRequestMethod("GET");
		monthly_conn.setRequestProperty("Accept", "application/json");

		if (monthly_conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + monthly_conn.getResponseCode());
		}
		BufferedReader monthly_br = new BufferedReader(new InputStreamReader((monthly_conn.getInputStream())));

		String monthlyOutput = "", monthlydata = "";
		JSONObject monthly_result;
		while ((monthlyOutput = monthly_br.readLine()) != null) {
			System.out.println(monthlyOutput);
			monthlydata += monthlyOutput;
			JSONObject MonthlyJObject = new JSONObject(monthlyOutput);
			JSONArray Monthlyresult = MonthlyJObject.getJSONArray("data");
			monthly_result = Monthlyresult.getJSONObject(0);

			MonthlyAdsPerformanceBean addMonthly = new MonthlyAdsPerformanceBean(monthly_result.getString("date_start"),
					monthly_result.getString("date_stop"), monthly_result.getString("account_name"),
					monthly_result.getString("account_id"), monthly_result.getString("social_reach"),
					monthly_result.getString("spend"), monthly_result.getString("inline_link_clicks"),
					monthly_result.getString("frequency"), monthly_result.getString("impressions"),
					monthly_result.getString("clicks"), monthly_result.getString("cpc"),
					monthly_result.getString("ctr"), client_stamp);
			monthlyAdsAccount.add(addMonthly);
		}

	}

	public void writeAdsetsDataToCassandraDaily(String account_id, String accessToken, String client_stamp)
			throws IOException {
		URL url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&&level=account&date_preset=yesterday"
				+ "&access_token=" + accessToken);

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output = "", full = "";
		JSONObject result1;
		while ((output = br.readLine()) != null) {
			System.out.println(output);
			full += output;
			JSONObject JObject = new JSONObject(output);
			JSONArray result = JObject.getJSONArray("data");
			result1 = result.getJSONObject(0);

			DailyAdsetsPerformanceBean addAdAccountData = new DailyAdsetsPerformanceBean(
					result1.getString("date_start"), result1.getString("date_stop"), result1.getString("account_name"),
					result1.getString("account_id"), result1.getString("social_reach"), result1.getString("spend"),
					result1.getString("inline_link_clicks"), result1.getString("frequency"),
					result1.getString("impressions"), result1.getString("clicks"), result1.getString("cpc"),
					result1.getString("ctr"), client_stamp);
			dailyAdsetsAccount.add(addAdAccountData);

		}

	}

	public void writeAdsetsDataToCassandraWeekly(String accessToken, String account_id, String client_stamp)
			throws IOException {
		URL Weekly_url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&level=account&date_preset=last_week_mon_sun"
				+ "&access_token=" + accessToken);

		HttpURLConnection weekly_conn = (HttpURLConnection) Weekly_url.openConnection();
		weekly_conn.setRequestMethod("GET");
		weekly_conn.setRequestProperty("Accept", "application/json");

		if (weekly_conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + weekly_conn.getResponseCode());
		}
		BufferedReader Weekly_br = new BufferedReader(new InputStreamReader((weekly_conn.getInputStream())));

		String weeklyOutput = "", data = "";
		JSONObject weekly_result;
		while ((weeklyOutput = Weekly_br.readLine()) != null) {
			System.out.println(weeklyOutput);
			data += weeklyOutput;
			JSONObject WeeklyJObject = new JSONObject(weeklyOutput);
			JSONArray Weeklyresult = WeeklyJObject.getJSONArray("data");
			weekly_result = Weeklyresult.getJSONObject(0);

			WeeklyAdsetsPerformanceBean addWeekly = new WeeklyAdsetsPerformanceBean(
					weekly_result.getString("date_start"), weekly_result.getString("date_stop"),
					weekly_result.getString("account_name"), weekly_result.getString("account_id"),
					weekly_result.getString("social_reach"), weekly_result.getString("spend"),
					weekly_result.getString("inline_link_clicks"), weekly_result.getString("frequency"),
					weekly_result.getString("impressions"), weekly_result.getString("clicks"),
					weekly_result.getString("cpc"), weekly_result.getString("ctr"), client_stamp);
			weeklyAdsetsAccount.add(addWeekly);
		}

	}

	public void writeAdsetsDataToCassandraMonthly(String accessToken, String account_id, String client_stamp)
			throws IOException {
		URL Monthly_url = new URL("https://graph.facebook.com/v2.10/act_" + account_id
				+ "/insights?fields=cpc,ctr,account_id,account_name,clicks,date_start,date_stop,impressions,"
				+ "frequency,inline_link_clicks,spend,social_reach,website_ctr,website_purchase_roas&level=account&date_preset=last_month"
				+ "&access_token=" + accessToken);

		HttpURLConnection monthly_conn = (HttpURLConnection) Monthly_url.openConnection();
		monthly_conn.setRequestMethod("GET");
		monthly_conn.setRequestProperty("Accept", "application/json");

		if (monthly_conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + monthly_conn.getResponseCode());
		}
		BufferedReader monthly_br = new BufferedReader(new InputStreamReader((monthly_conn.getInputStream())));

		String monthlyOutput = "", monthlydata = "";
		JSONObject monthly_result;
		while ((monthlyOutput = monthly_br.readLine()) != null) {
			System.out.println(monthlyOutput);
			monthlydata += monthlyOutput;
			JSONObject MonthlyJObject = new JSONObject(monthlyOutput);
			JSONArray Monthlyresult = MonthlyJObject.getJSONArray("data");
			monthly_result = Monthlyresult.getJSONObject(0);

			MonthlyAdsetsPerformanceBean addMonthly = new MonthlyAdsetsPerformanceBean(
					monthly_result.getString("date_start"), monthly_result.getString("date_stop"),
					monthly_result.getString("account_name"), monthly_result.getString("account_id"),
					monthly_result.getString("social_reach"), monthly_result.getString("spend"),
					monthly_result.getString("inline_link_clicks"), monthly_result.getString("frequency"),
					monthly_result.getString("impressions"), monthly_result.getString("clicks"),
					monthly_result.getString("cpc"), monthly_result.getString("ctr"), client_stamp);
			monthlyAdsetsAccount.add(addMonthly);
		}
	}

	public void dataAddToCassandra() {
		for (int i = 0; i < dailyAccount.size(); i++) {

			AccountPerformanceBean accountbean = dailyAccount.get(i);
			cassandraOperations.insert(accountbean);

		}

		for (int i = 0; i < weeklyAccount.size(); i++) {

			WeeklyAccountPerformanceBean accountbean = weeklyAccount.get(i);
			cassandraOperations.insert(accountbean);

		}

		for (int i = 0; i < monthlyAccount.size(); i++) {

			MonthlyAccountPerformanceBean accountbean = monthlyAccount.get(i);
			cassandraOperations.insert(accountbean);

		}
		for (int i = 0; i < dailyCampaignAccount.size(); i++) {

			DailyCampaignPerformanceBean accountbean = dailyCampaignAccount.get(i);
			cassandraOperations.insert(accountbean);

		}
		for (int i = 0; i < weeklyCampaignAccount.size(); i++) {

			WeeklyCampaignPerformanceBean accountbean = weeklyCampaignAccount.get(i);
			cassandraOperations.insert(accountbean);

		}
		for (int i = 0; i < monthlyCampaignAccount.size(); i++) {

			MonthlyCampaignPerformanceBean accountbean = monthlyCampaignAccount.get(i);
			cassandraOperations.insert(accountbean);

		}
		
		for (int i = 0; i < dailyAdsAccount.size(); i++) {

			DailyAdsPerformanceBean accountbean = dailyAdsAccount.get(i);
			cassandraOperations.insert(accountbean);

		}
		for (int i = 0; i < weeklyAdsAccount.size(); i++) {

			WeeklyAdsPerformanceBean accountbean = weeklyAdsAccount.get(i);
			cassandraOperations.insert(accountbean);

		}
		for (int i = 0; i < monthlyAdsAccount.size(); i++) {

			MonthlyAdsPerformanceBean accountbean = monthlyAdsAccount.get(i);
			cassandraOperations.insert(accountbean);

		}
		for (int i = 0; i < dailyAdsetsAccount.size(); i++) {

			DailyAdsetsPerformanceBean accountbean = dailyAdsetsAccount.get(i);
			cassandraOperations.insert(accountbean);

		}
		for (int i = 0; i < weeklyAdsetsAccount.size(); i++) {

			WeeklyAdsetsPerformanceBean accountbean = weeklyAdsetsAccount.get(i);
			cassandraOperations.insert(accountbean);

		}
		for (int i = 0; i < monthlyAdsetsAccount.size(); i++) {

			MonthlyAdsetsPerformanceBean accountbean = monthlyAdsetsAccount.get(i);
			cassandraOperations.insert(accountbean);

		}
		
		
		

	}

}
