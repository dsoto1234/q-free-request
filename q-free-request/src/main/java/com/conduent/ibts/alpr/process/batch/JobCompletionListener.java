package com.conduent.ibts.alpr.process.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobCompletionListener extends JobExecutionListenerSupport{
//public class JobCompletionListener extends JobExecutionListener{	
	
	private static final Logger logger = LoggerFactory.getLogger(BatchJobController.class);

	@Override
    public void beforeJob(JobExecution jobExecution) {

       System.out.println("Job started at: "+ jobExecution.getStartTime());
       System.out.println("Status of the Job: "+jobExecution.getStatus());
    }
	
	@Override
	public void afterJob(JobExecution jobExecution) {
		  System.out.println("Job Ended at: "+ jobExecution.getEndTime());
	       System.out.println("Status of the Job: "+jobExecution.getStatus());
				
		if(jobExecution.getStatus() == BatchStatus.COMPLETED)
			logger.info("in JobCompletionListener.afterJob() Batch job completed successfully");
		
	}

}
