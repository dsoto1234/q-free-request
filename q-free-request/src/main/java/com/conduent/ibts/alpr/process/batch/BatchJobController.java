package com.conduent.ibts.alpr.process.batch;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.service.impl.QFreeServiceImpl;

@RestController
public class BatchJobController {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchJobController.class);

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	@Autowired
	private QFreeServiceImpl qfreeService;

	//localhost:8080/qfree/batch/invokeJob?agenyID=?lpr?threadC?index?
	@GetMapping("/qfree/batch/invokeJob")
	ResponseEntity<Object> executeQFree() {
		
		logger.info("<<<<<<< entering BatchJobController.executeQFree() >>>>>>>");
		
		JobParameters jobParameters = new JobParametersBuilder().addLong("startAt:", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).toJobParameters();
		try 
		{
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			while (jobExecution.isRunning())
				logger.info("in BatchJobController.executeQFree() job still running ........");
			
			logger.info("in BatchJobController.executeQFree() jobExecution.getStatus(): "+ jobExecution.getStatus());
		} 
		catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) 
		{						
			e.printStackTrace();
			logger.error("Error in BatchJobController.executeQFree() while processing transactions for Qfree e: {}", e.getMessage());
		}
		
		logger.info("<<<<<<< exiting BatchJobController.executeQFree() >>>>>>>");
		
		return new ResponseEntity<>("Batch invoked",HttpStatus.ACCEPTED);
	}
	

//	@GetMapping("/qfree/invoke/{agencyId}/{plazaId}/{lprType}/{batchSize}/{totalThreads}/{threadIndex}")
//	ResponseEntity<Object> executeQFreeWithPathVariables(
//			@Valid @Digits(fraction = 0, integer = 1) @Min(0) @Max(4) @PathVariable Long agencyId,
//			@Min(0) @PathVariable Long plazaId, @NotBlank @PathVariable String lprType,
//			@Min(1) @PathVariable Long batchSize, @Min(1) @PathVariable Long totalThreads,
//			@Min(0) @PathVariable Long threadIndex) {
//
//		try {
//			QFreeBatchDto batchDto = new QFreeBatchDto();
//			logger.info("Starting to get transactions for RUN_ID :{}, thread_index :{}, thread_name :{}",
//					batchDto.getRunId(), batchDto.getThreadIndex(), Thread.currentThread().getName());
//			if (null != agencyId && null != plazaId
//					&& (lprType.contentEquals("NORMAL") || lprType.contentEquals("AIR_ONLY")
//							|| lprType.contentEquals("DISPUTE"))
//					&& null != batchSize && null != totalThreads && null != threadIndex) {
//
//				if (threadIndex >= totalThreads) {
//					return new ResponseEntity<>(
//							"Thread index must be less than total threads, please pass appropriate value in path variables!",
//							HttpStatus.BAD_REQUEST);
//				}
//
//				batchDto.setAgencyId(agencyId);
//				batchDto.setPlazaId(plazaId);
//				batchDto.setLprType(lprType);
//				batchDto.setBatchSize(batchSize);
//				batchDto.setTotalThreads(totalThreads);
//				batchDto.setThreadIndex(threadIndex);
//				lprType.length();
//				qfreeService.getTransactionsToEnqueue(batchDto);
//			} else {
//				return new ResponseEntity<>(
//						"All fields are mandatory, please pass appropriate value in path variables!",
//						HttpStatus.BAD_REQUEST);
//			}
//
//		} catch (Exception e) {
//			LOGGER.error("Exception occured while processing transactions for Qfree :{}", e.getMessage());
//		}
//		return new ResponseEntity<>("Qfree service invoked", HttpStatus.ACCEPTED);
//	}
//	
	
}
