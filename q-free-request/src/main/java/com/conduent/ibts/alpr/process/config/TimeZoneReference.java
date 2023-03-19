package com.conduent.ibts.alpr.process.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.conduent.app.timezone.utility.TimeZoneConv;

@Configuration
@ComponentScan("com.conduent.app")
public class TimeZoneReference {
	
	private static final Logger logger = LoggerFactory.getLogger(TimeZoneReference.class);	

	@Bean("timeZoneConv")
	public TimeZoneConv getCurrentDateAndTime() {
		logger.info("Inside TimeZoneReference.getCurrentDateAndTime()");
		
		return new TimeZoneConv();
	}
}