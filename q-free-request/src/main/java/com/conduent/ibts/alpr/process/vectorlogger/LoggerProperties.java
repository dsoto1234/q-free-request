/**
 *  
 * @(#)AppVersionReader.java	Aug 7, 2006
 *
 * Copyright 2006 ACS, Inc. All rights reserved.
 */
package com.conduent.ibts.alpr.process.vectorlogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class LoggerProperties {
    
	private  static final Logger logger = LoggerFactory.getLogger(LoggerProperties.class);	
    private  static final String LOG_FILE_NAME = "trcs_logging.properties"; 
    private  static final String VECTOR_EXT_LOG_LEVEL = "VECTOR_EXT_LOG_LEVEL";
    
    private static Properties properties;
   
    public static String logLevel ="SEVERE";    
	public static String multiLoggerFileLocation = null;
    
    @PostConstruct
  	public void initialize() {
  		logger.info("entering LoggerProperties.initialize()");  		
  		getLogLevelProperty();  		  		
  		logger.info("exiting LoggerProperties.getLogLevelProperty()  multiLoggerFileLocation: "+ multiLoggerFileLocation+ "logLevel: "+ logLevel);
  	}
    
    public static synchronized void getLogLevelProperty() {
    	logger.info("entering LoggerProperties.getLogLevelProperty()");
    	
        if (properties != null) { 
        	return ; 
        }
        
        try {
     
       		ClassLoader cl = Thread.currentThread().getContextClassLoader();
        	InputStream inputStream = cl.getResourceAsStream(LOG_FILE_NAME);
            
            properties = new Properties();
            properties.load(inputStream);
            logLevel = (String) properties.get(VECTOR_EXT_LOG_LEVEL);
            inputStream.close();

        } catch (FileNotFoundException fileNotFoundException) {
        	logger.info("error in LoggerProperties.getLogLevelProperty() fileNotFoundException: "+fileNotFoundException);

        } catch (IOException ioException) {
        	logger.info("error in LoggerProperties.getLogLevelProperty() ioException: "+ioException);
        }
    }   
    
     public static String getLogLevel() {
    	 return logLevel;
     }
       
}

