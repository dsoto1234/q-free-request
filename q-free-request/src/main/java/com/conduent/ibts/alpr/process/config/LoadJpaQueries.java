package com.conduent.ibts.alpr.process.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadJpaQueries {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadJpaQueries.class);
	private static Map<String, String> queryMap = null;		
	private static Map<String, String> queryMapReport = null;
	
	private LoadJpaQueries() {
		logger.info("Inside LoadJpaQueries constructor()");
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized   Map<String, String> getQueriesMap() {
		//logger.info("Inside LoadJpaQueries.getQueriesMap()");
		
		if (queryMap == null) {
			Object obj = LoadJpaApplicationContext.getApplicationContext().getBean("queries");			
			queryMap = (Map<String, String>) obj;		
		}
		return queryMap;
	}
	
	public static  String getQueryById(String queryId) {
		//logger.info("Inside LoadJpaQueries.getQueryById() queryId: "+queryId);
		
		String query = null;
		query = getQueriesMap().get(queryId);
		return query;
	}
	
	/**
	 * @Function : to load xml based on name of bean (bean name mapped with xml bean id param)
	 * @param queryBean
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static synchronized   Map<String, String> getQueriesMap(String queryBean)  {
		//logger.info("Inside LoadJpaQueries.getQueriesMap() queryBean: "+queryBean);
		
		if (queryMapReport == null) {
			Object obj = LoadJpaApplicationContext.getApplicationContext().getBean(queryBean);			
			queryMapReport = (Map<String, String>) obj;			
		}
		return queryMapReport;
	}
	
	
	/**
	 * @Function : to fetch the query from passed querybean (bean name mapped with xml bean id param)
	 * @param queryBean
	 * @param queryId
	 * @return
	 * @throws Exception 
	 */
	public static String getQueryById(String queryBean, String queryId)  {
		//logger.info("Inside LoadJpaQueries.getQueryById() queryBean: "+queryBean+"  queryId: "+queryId);
		
		String query = null;
		query = getQueriesMap(queryBean).get(queryId);

		return query;
	}
}
