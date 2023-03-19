package com.conduent.ibts.alpr.process.batch;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLoggerExt;

public class BatchProcessor implements ItemProcessor<QFreeBatchDto, QFreeBatchDto> {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchProcessor.class);
	
	public BatchProcessor() {
		logger.info("<<<<<<<<<<<< entering BatchProcessor() constructor >>>>>>>>>>>>");
	}
	
	@Override
	public QFreeBatchDto process(QFreeBatchDto item) throws Exception {
		
		logger.info("\n ");
		logger.info("\n entering BatchProcessor.process() threadName: "+Thread.currentThread().getName());
		
		//QFreeBatchDto batchDetails = new QFreeBatchDto(item.getAgencyId(), item.getBatchSize(), item.getExecutionStatus(), item.getEndTime().toLocalDate(), 
		//		item.getIsEnabled(), item.getLprType(), item.getPlazaId(), item.getRunId(), item.getStartTime().toLocalTime(), item.getTotalThreads(),item.getThreadIndex());
		
		
		//public QFreeBatchDto(long agencyId, Long batchSize, String executionStatus, LocalDateTime endTime, String isEnabled, String lprType, 
		//		long plazaId, long runId, LocalDateTime startTime, Long totalThreads, Long threadIndex)   	
		
		
		//public QFreeBatchDto(long runId, long agencyId, long plazaId, String isEnabled, String lprType, String executionStatus, LocalDateTime startTime,
		//		Long totalThreads, Long threadIndex, LocalDateTime endTime, Long batchSize) 
		
		QFreeBatchDto batchDetails = new QFreeBatchDto();
		batchDetails.setAgencyId(item.getAgencyId());
		batchDetails.setBatchSize(item.getBatchSize());
		batchDetails.setExecutionStatus(item.getExecutionStatus());
		batchDetails.setEndTime(item.getEndTime());
		batchDetails.setIsEnabled(item.getIsEnabled());
		batchDetails.setLprType(item.getLprType());
		batchDetails.setPlazaId(item.getPlazaId());
		batchDetails.setRunId(item.getRunId());
		batchDetails.setStartTime(item.getStartTime());
		batchDetails.setTotalThreads(item.getTotalThreads());
		batchDetails.setThreadIndex(item.getThreadIndex());
		
		
		logger.info("exiting BatchProcessor.process() batchDetails: "+batchDetails+"  threadName: "+Thread.currentThread().getName());				
		
		return batchDetails;
	}

}
