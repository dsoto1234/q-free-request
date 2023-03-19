package com.conduent.ibts.alpr.process.vectorlogger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
//@PropertySource(ignoreResourceNotFound = true, value = "classpath:trcs_logging.properties")
public class MultiLoggerExt {
	
	Logger logger = LoggerFactory.getLogger(MultiLoggerExt.class);
		
	private static String ENQUEUE_LOG_FILE_NAME = "log_enqueue";
	private static String RESULTS_LOG_FILE_NAME = "log_results";
		
	//@Value("${multi.logger.file.location}")
	//public String multiLoggerFileLocation;
		
	@Autowired
	private LoggerProperties loggerProperties;
	
	public MultiLoggerExt() {
		logger.info("<<<<<< entering MultiLoggerExt() constructor >>>>>>");
	}

	public MultiLogger getVectorLoggerExt(long agencyId, long plazaId, long totalThreads, long threadIndex, String lprType, String multiLoggerLoc) {
		
		logger.info("<<<<<< entering MultiLoggerExt.getVectorLoggerExt() agencyId: "+agencyId+" plazaId: "+plazaId+" totalThreads: "+totalThreads+
			" threadIndex: "+threadIndex+" lprType: "+lprType+" multiLoggerLoc: "+multiLoggerLoc+ " logLevel: " + loggerProperties.getLogLevel()+" >>>>>>");
		
		MultiLogger log = null;
		String logFileName=null;
		try {     
			 SimpleDateFormat sdf 	= new SimpleDateFormat("MMddyyyy_hhmmss");
			 String logFilePath 	= multiLoggerLoc;	
			 			 
			 logFileName = logFilePath+ File.separator + ENQUEUE_LOG_FILE_NAME+"_"+agencyId+"_"+plazaId+"_"+totalThreads+"_"+threadIndex+
					"_"+lprType+"_"+ sdf.format(new Date())+".log";
			 
			 logger.info("in MultiLogger.getVectorLoggerExt(): logFileName:  "+ logFileName);
			 			 
			 log = new  MultiLogger(logFileName);
			 String logLevel = loggerProperties.getLogLevel();
			 			 			 			 
			 Level level = Level.parse(logLevel);
			 			 
			 log.setLevel(level.getName());
			 log.getVectorFileHandler().setLevel(level);
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
		return log;
	}
	
	private  MultiLogger getVectorLoggerExt(String threadIndex, String multiLoggerLoc) {
		MultiLogger log = null;
		String logFileName=null;
		
		logger.info("<<<<<< in MultiLoggerExt.getVectorLoggerExt() threadIndex: " + threadIndex+" multiLoggerLoc: "+multiLoggerLoc+
			" logLevel: "+loggerProperties.getLogLevel()+" >>>>>>");

		try {
			//SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_hhmmssSSS");
			SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_hhmmss");

			String logFilePath = multiLoggerLoc;
			logFileName = logFilePath+File.separator+RESULTS_LOG_FILE_NAME+"_"+threadIndex+"_"+ sdf.format(new Date())+".log";	
			
			logger.info("in MultiLoggerExt.getVectorLoggerExt(): logFileName:  "+ logFileName);			
			
			log = new  MultiLogger(logFileName);
			String logLevel = loggerProperties.getLogLevel();
							 			
			Level level = Level.parse(logLevel);
			log.setLevel(level.getName());
			log.getVectorFileHandler().setLevel(level);

		}catch(Exception e) {
			e.printStackTrace();
		}
		return log;
	}	

}
