package com.conduent.ibts.alpr.process.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LoadJpaApplicationContext {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadJpaApplicationContext.class);	

	private static ApplicationContext applicationContext = null;
	private static final String DATABASE_QUERY = "database-queries.xml";

	private LoadJpaApplicationContext() {
		logger.info("Inside LoadJpaApplicationContext constructor()");
	}
	
	public  static synchronized  ApplicationContext getApplicationContext() {
		logger.info("Inside LoadJpaApplicationContext.getApplicationContext()");
		
		if (applicationContext == null) {	
			
			logger.info("Inside LoadJpaApplicationContext.getApplicationContext() applicationContext == null");
			
			applicationContext = new ClassPathXmlApplicationContext(DATABASE_QUERY);			
		}
		return applicationContext;
	}
}
