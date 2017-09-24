package com.reporting.FacebookAds;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.reporting.Enviornment.EnvironmentClass;
import com.reporting.bean.AccountPerformanceBean;
import com.reporting.bean.MonthlyAccountPerformanceBean;
import com.reporting.bean.WeeklyAccountPerformanceBean;
import com.reporting.dao.CassandraDao;
import com.reporting.daoImpl.AllClientInforNeededDaoImpl;
import com.reporting.daoImpl.CassandraCRUDImpl;

public class App {

	public static void main(String[] args) {
		AbstractApplicationContext context = null;
		CassandraCRUDImpl daos = null;

		try {
			context = new ClassPathXmlApplicationContext("spring.xml");

			context.registerShutdownHook();

			AllClientInforNeededDaoImpl information = context.getBean("dao", AllClientInforNeededDaoImpl.class);
			EnvironmentClass env = context.getBean("envBean", EnvironmentClass.class);
			daos = context.getBean("daoCassendra", CassandraCRUDImpl.class);

			List<Map<String, Object>> value = information.getAllClientInfo();
			if (value.size() > 0) {

				String accessToken = (String) value.get(0).get(env.getDataFromPropertyFile("access_token_column"));
				for (int i = 0; i < value.size(); i++) {
					String account_id = (String) value.get(i).get(env.getDataFromPropertyFile("account_id_column"));
					String client_stamp = (String) value.get(i).get(env.getDataFromPropertyFile("client_stamp_column"));

					daos.writeAccountDataToCassandraDaily(accessToken, account_id, client_stamp);
					daos.writeAccountDataToCassandraWeekly(accessToken, account_id, client_stamp);
					daos.writeAccountDataToCassandraMonthly(accessToken, account_id, client_stamp);
					daos.writeCampaignDataToCassandraDaily(accessToken, account_id, client_stamp);
					daos.writeCampaignDataToCassandraWeekly(accessToken, account_id, client_stamp);
					daos.writeCampaignDataToCassandraMonthly(accessToken, account_id, client_stamp);
					daos.writeAdsDataToCassandraDaily(account_id, accessToken, client_stamp);
					daos.writeAdsDataToCassandraWeekly(accessToken, account_id, client_stamp);
					daos.writeAdsDataToCassandraMonthly(accessToken, account_id, client_stamp);
					daos.writeAdsetsDataToCassandraDaily(account_id, accessToken, client_stamp);
					daos.writeAdsetsDataToCassandraWeekly(accessToken, account_id, client_stamp);
					daos.writeAdsetsDataToCassandraMonthly(accessToken, account_id, client_stamp);
					
					

				}
			}
			daos.getConection();

			daos.dataAddToCassandra();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}
}
