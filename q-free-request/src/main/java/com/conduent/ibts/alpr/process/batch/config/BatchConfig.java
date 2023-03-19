package com.conduent.ibts.alpr.process.batch.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.conduent.ibts.alpr.process.batch.BatchProcessor;
import com.conduent.ibts.alpr.process.batch.BatchRowMapper;
import com.conduent.ibts.alpr.process.batch.BatchWriter;
import com.conduent.ibts.alpr.process.config.LoadJpaQueries;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
		
	private String query = LoadJpaQueries.getQueryById("Get_from_Q_FREE_REQUEST_BATCH");
		
	public BatchConfig() {
		logger.info("<<<<<<<<<< entering BatchConfig() constructor() >>>>>>>>>>");
	}
	
	@Bean
	ItemReader<QFreeBatchDto> dbItemReader(@Qualifier("primaryDS") DataSource dataSource) {
		
		logger.info("entering BatchConfig.dbItemReader()");
		
		BatchRowMapper batchRowMapper = new BatchRowMapper();						
		JdbcCursorItemReader<QFreeBatchDto> itemReader = new JdbcCursorItemReader<>();
		itemReader.setDataSource(dataSource);
		itemReader.setSql(query);
		itemReader.setVerifyCursorPosition(false);
		//itemReader.setRowMapper(new BeanPropertyRowMapper<>(QFreeBatchDto.class));
		itemReader.setRowMapper(batchRowMapper);
		itemReader.setName("QFreeBatchItemReader");
				
		return itemReader;
	}

	@Bean
	ItemProcessor<QFreeBatchDto, QFreeBatchDto> processor() {
		logger.info("entering BatchConfig.processor()");
		return new BatchProcessor();
	}

	@Bean
	ItemWriter<QFreeBatchDto> writer() {
		logger.info("entering BatchConfig.writer()");
		
		return new BatchWriter();
	}
	
	@Bean
	public SkipPolicy skipPolicyRequirement() {
		return new ExceptionSkipPolicy();
	}
	
	@Bean
	Step batchStep(
				PlatformTransactionManager 	transactionManager,
				@Qualifier("dbItemReader") 	ItemReader<QFreeBatchDto> readerRef,
				@Qualifier("writer") 		ItemWriter<QFreeBatchDto> writerRef,
				@Qualifier("processor") 	ItemProcessor<QFreeBatchDto, QFreeBatchDto> processorRef) {
		
		logger.info("entering BatchConfig.batchStep()");
		
		return stepBuilderFactory.get("QFreeBatchStep")
				.transactionManager(transactionManager)
				.<QFreeBatchDto, QFreeBatchDto>chunk(1)
				.reader(readerRef)
				.processor(processorRef)
				.writer(writerRef)
				.faultTolerant()
				.skip(Exception.class)
				.taskExecutor(taskExecutor())
				.build();
	}

	
	/*
	@Bean
	Step batchStep(@Qualifier("dbItemReader") 	ItemReader<QFreeBatchDto> readerRef,
				   @Qualifier("writer") 		ItemWriter<QFreeBatchDto> writerRef,
				   @Qualifier("processor") 		ItemProcessor<QFreeBatchDto, QFreeBatchDto> processorRef) {
		
		logger.info("entering BatchConfig.batchStep()");
		
		return stepBuilderFactory.get("QFreeBatchStep")				
			.<QFreeBatchDto, QFreeBatchDto>chunk(1)			
			.reader(readerRef)
			.processor(processorRef)
			.writer(writerRef)
			//.faultTolerant()
			//.skipLimit(5)
			//.skip(VectorImageReviewException.class)
			
			//.skipPolicy(skipPolicyRequirement())  
			.taskExecutor(taskExecutor())
			.build();
	}
	*/

	@Bean
	Job batchJob(@Qualifier("batchStep") Step qfreeBatchStep) {
		
		logger.info("entering BatchConfig.batchJob()");
		
		return jobBuilderFactory.get("QFreeBatchJob")
			.incrementer(new RunIdIncrementer())
			.flow(qfreeBatchStep)
			.end()
			.build();
	}
	
	@Bean
	TaskExecutor taskExecutor() {
		logger.info("entering BatchConfig.taskExecutor()");
		
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("QfreeThread");
			/*
			 * String threadConcurrencyLimit = masterCache.getqFreeProcessParameters()
			 * .get(Constants.PARAM_NAME_THREAD_CONCURRENCY_LIMIT);
			 */
		taskExecutor.setThreadNamePrefix("qfree-thread");
		taskExecutor.setConcurrencyLimit(100);
		//taskExecutor.setConcurrencyLimit(null!=threadConcurrencyLimit?Integer.valueOf(threadConcurrencyLimit):1);
		return taskExecutor;
	}
	
}
