package com.conduent.ibts.alpr.process.vectorlogger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conduent.ibts.alpr.process.service.impl.QFreeQueueServiceImpl;

public class CreateMultiLogger {
	
	private static String ENQUEUE_LOG_FILE_NAME = "log_enqueue";
	private static String RESULTS_LOG_FILE_NAME = "log_results";
	private static final Logger logger = LoggerFactory.getLogger(QFreeQueueServiceImpl.class);
	
	/*public String multiLoggerPath; 
	public Long agencyId;
	public Long threadIndex;
	public Long plazaId;
	public String lprType;
	public Long numberOfThreads;
	*/
	
	public CreateMultiLogger() {}
	
	/*public CreateMultiLogger(String multiLoggerPath, Long agencyId, Long threadIndex, Long plazaId, String lprType, Long numberOfThreads) {
		this.multiLoggerPath = multiLoggerPath;
		
		this.multiLoggerPath = multiLoggerPath;
		this.agencyId = agencyId;
		this.threadIndex = threadIndex;
		this.plazaId = plazaId;
		this.lprType = lprType;
		this.numberOfThreads = numberOfThreads;
	}*/
	
	public MultiLogger getVectorLoggerExt(String multiLoggerPath, Long agencyId,Long threadIndex, Long plazaId, String lprType, Long numberOfThreads) {
		
		MultiLogger log = null;
		String logFileName=null;
		try {
				     
			 SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_hhmmss");
			 String logFilePath = multiLoggerPath;			 
			 logFileName =logFilePath+ File.separator + ENQUEUE_LOG_FILE_NAME+"_"+agencyId+"_"+plazaId+"_"+numberOfThreads+"_"+threadIndex+"_"+lprType+"_"+ sdf.format(new Date())+".log";
			 
			 logger.debug("CreateMultiLogger.getVectorLoggerExt(): logFileName:  ", logFileName);
			 			 
			 log = new  MultiLogger(logFileName);
			 String logLevel = LoggerProperties.getLogLevel();
			 			 			 
			 logger.debug("CreateMultiLogger.getVectorLoggerExt(): logLevel:  ", logLevel);
			 
			 Level level = Level.parse(logLevel);
			 			 
			 log.setLevel(level.getName());
			 log.getVectorFileHandler().setLevel(level);
			 
		}catch(Exception e) {
			e.printStackTrace();
		}
		return log;
	}
	
	public  MultiLogger getVectorLoggerExt(String multiLoggerPath, String threadIndex) 
	{
		MultiLogger log = null;
		String logFileName=null;
		
		System.out.println("<<<<<<<<<<< in ImageReviewProcessor.getVectorLoggerExt() threadIndex: " + threadIndex);

		try {
			//SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_hhmmssSSS");
			SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy_hhmmss");

			String logFilePath = multiLoggerPath;
			logFileName =logFilePath+ File.separator  + RESULTS_LOG_FILE_NAME+"_"+threadIndex+"_"+ sdf.format(new Date())+".log";	
			
			logger.debug("ImageReviewFacadeBean.getVectorLoggerExt(): ", " threadIndex: " + threadIndex+"  logFileName: " + logFileName);
			
			log = new  MultiLogger(logFileName);
			String logLevel = LoggerProperties.getLogLevel();
			
			logger.debug("CreateMultiLogger.getVectorLoggerExt(): threadIndex: " + threadIndex+"  logLevel: " + logLevel);				 
			
			Level level = Level.parse(logLevel);
			log.setLevel(level.getName());
			log.getVectorFileHandler().setLevel(level);

		}catch(Exception e) {
			e.printStackTrace();
		}
		return log;
	}	

}
