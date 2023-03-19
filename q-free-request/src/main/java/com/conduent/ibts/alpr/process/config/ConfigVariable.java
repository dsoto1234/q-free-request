package com.conduent.ibts.alpr.process.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.conduent.ibts.alpr.process.batch.config.BatchConfig;

@Component
public class ConfigVariable {
	private static final Logger logger = LoggerFactory.getLogger(ConfigVariable.class);	

	@Value("${config.configfilepath}")
	private String configfilepath;
	
	@Value("${qfreequeue.streamid}")
	private String streamId;
	
	@Value("${tpms.unmatched}")
	private String tpmsUnmatchedUrl;
	
	@Value("${cacerts.location}")
	private String cacertsLocation;
	
	@Value("${ibts.event}")
	private String tVioltxEventApi;
	
	@Value("${multi.logger.location}")
	private String multiLoggerLocation;
	
	@Value("${filepath.log}")
	private String logFilePath;
	
	public ConfigVariable() {
		logger.info("Inside ConfigVariable() constructor()");
	}
	
	public String gettVioltxEventApi() {
		return tVioltxEventApi;
	}

	public void settVioltxEventApi(String tVioltxEventApi) {
		this.tVioltxEventApi = tVioltxEventApi;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
		
	public String getMultiLoggerLocation() {
		return multiLoggerLocation;
	}

	public void setMultiLoggerLocation(String multiLoggerLocation) {
		this.multiLoggerLocation = multiLoggerLocation;
	}
	
	public String getCacertsLocation() {
		return cacertsLocation;
	}

	public void setCacertsLocation(String cacertsLocation) {
		this.cacertsLocation = cacertsLocation;
	}

	public String getConfigfilepath() {
		return configfilepath;
	}

	public void setConfigfilepath(String configfilepath) {
		this.configfilepath = configfilepath;
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	public String getTpmsUnmatchedUrl() {
		return tpmsUnmatchedUrl;
	}

	public void setTpmsUnmatchedUrl(String tpmsUnmatchedUrl) {
		this.tpmsUnmatchedUrl = tpmsUnmatchedUrl;
	}
	
	
}
