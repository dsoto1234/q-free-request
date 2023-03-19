package com.conduent.ibts.alpr.process.batch.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.conduent.ibts.alpr.process.batch.BatchProcessor;
import com.conduent.ibts.alpr.process.batch.BatchWriter;
import com.conduent.ibts.alpr.process.batch.JobCompletionListener;
import com.conduent.ibts.alpr.process.config.LoadJpaQueries;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;


//https://www.jackrutorial.com/2018/03/spring-boot-batch-read-from-mysql-database-and-write-into-a-csv-file-tutorial.html

@Configuration
@EnableBatchProcessing
public class BatchConfig2 {
	
	
//	
//	private static final Logger logger = LoggerFactory.getLogger(BatchConfig2.class);	
//	@Autowired
//	private JobBuilderFactory jobBuilderFactory;
//	@Autowired
//	private StepBuilderFactory stepBuilderFactory;
//	
//	private String query = LoadJpaQueries.getQueryById("Get_from_Q_FREE_REQUEST_BATCH");
//	
//	@Autowired
//	public DataSource dataSource;
//	
//	public BatchConfig2() {logger.info("<<<<< entering BatchConfig constructor() >>>>>");}
//	
//	@Bean
//	public DataSource dataSource() {
//		logger.info("<<<<< entering BatchConfig.dataSource() >>>>>");
//		
//		final DriverManagerDataSource dataSource = new DriverManagerDataSource();		
//		//dataSource.setDriverClassName("oracle.jdbc.driver");
//		dataSource.setUrl("jdbc:oracle:thin:@oltpdev_tpurgent?TNS_ADMIN=C:/development/NY_modernizaation/modernization/gibhub_code/qfree_request_service/Wallet_OLTPDEV");
//		dataSource.setUsername("IBTS");
//		dataSource.setPassword("WElcome9874#");
//		
//		logger.info("<<<<< exiting BatchConfig.dataSource() >>>>>");
//		return dataSource;
//	}
//	
//	
//	public class BatchRowMapper implements RowMapper<QFreeBatchDto> {		
//		
//		@Override
//		public QFreeBatchDto mapRow(ResultSet rs, int rowNum) throws SQLException {
//			
//			logger.info("<<<<< entering BatchRowMapper.mapRow() rowNum: "+rowNum+" threadName: "+Thread.currentThread().getName()+" >>>>>");
//			
//			QFreeBatchDto qFreeBatchDto = new QFreeBatchDto();
//			qFreeBatchDto.setRunId(rs.getLong("RUN_ID"));
//			qFreeBatchDto.setAgencyId(rs.getLong("AGENCY_ID"));			
//			qFreeBatchDto.setPlazaId(rs.getLong("PLAZA_ID"));
//			qFreeBatchDto.setIsEnabled(rs.getString("IS_ENABLED"));
//			qFreeBatchDto.setLprType(rs.getString("LPR_TYPE"));
//			qFreeBatchDto.setExecutionStatus(rs.getString("EXECUTION_STATUS"));			
//			qFreeBatchDto.setTotalThreads(rs.getLong("TOTAL_THREADS"));						
//			qFreeBatchDto.setThreadIndex(rs.getLong("THREAD_INDEX"));
//			qFreeBatchDto.setBatchSize(rs.getLong("BATCH_SIZE"));
//			
//			logger.info("<<<<< exiting BatchRowMapper.mapRow() threadName: "+Thread.currentThread().getName()+ " >>>>>");
//			
//			return qFreeBatchDto;
//		}
//	}
//	
//	
//	@Bean
//	ItemReader<QFreeBatchDto> dbItemReader(@Qualifier("primaryDS") DataSource dataSource) {
//		
//		logger.info("<<<<< entering BatchConfig.dbItemReader() >>>>>");
//		
//		JdbcCursorItemReader<QFreeBatchDto> itemReader = new JdbcCursorItemReader<>();
//		itemReader.setDataSource(dataSource);
//		itemReader.setSql(query);
//		itemReader.setVerifyCursorPosition(false);
//		itemReader.setRowMapper(new BatchRowMapper());
//		itemReader.setName("QFreeBatchItemReader");
//
//		return itemReader;
//	}
//
//	@Bean
//	ItemProcessor<QFreeBatchDto, QFreeBatchDto> processor() {
//		logger.info("<<<<< entering BatchConfig.processor() >>>>>");
//		return new BatchProcessor();
//	}
//
//	@Bean
//	ItemWriter<QFreeBatchDto> writer() {
//		logger.info("<<<<< entering BatchConfig.writer() >>>>>");
//		return new BatchWriter();
//	}
//	
//	@Bean
//	JobExecutionListener jobListener() {
//		logger.info("<<<<< entering BatchConfig.jobListener() >>>>>");
//		return new JobCompletionListener();
//	}
//	
//
//	@Bean
//	Step batchStep(@Qualifier("dbItemReader") 	ItemReader<QFreeBatchDto> readerRef,
//				   @Qualifier("writer") 		ItemWriter<QFreeBatchDto> writerRef,
//				   @Qualifier("processor") 		ItemProcessor<QFreeBatchDto, QFreeBatchDto> processorRef) {
//		
//		logger.info("<<<<< entering BatchConfig.batchStep() >>>>>");
//		
//		return stepBuilderFactory.get("QFreeBatchStep")
//			.<QFreeBatchDto, QFreeBatchDto>chunk(1)
//			.reader(readerRef)
//			.processor(processorRef)
//			.writer(writerRef)
//			.taskExecutor(taskExecutor())
//			.build();
//	}
//
//	@Bean
//	Job batchJob(@Qualifier("batchStep") Step qfreeBatchStep) {
//		
//		logger.info("<<<<< entering BatchConfig.batchJob() >>>>>");
//		
//		return jobBuilderFactory.get("QFreeBatchJob")
//			.incrementer(new RunIdIncrementer())
//			.listener(jobListener())
//			.flow(qfreeBatchStep)
//			.end()
//			.build();
//	}
//	@Bean
//	TaskExecutor taskExecutor() {
//		logger.info("<<<<< entering BatchConfig.taskExecutor() >>>>>");
//		
//		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("QfreeThread");
//			/*
//			 * String threadConcurrencyLimit = masterCache.getqFreeProcessParameters()
//			 * .get(Constants.PARAM_NAME_THREAD_CONCURRENCY_LIMIT);
//			 */
//		taskExecutor.setThreadNamePrefix("qfree-thread");
//		taskExecutor.setConcurrencyLimit(100);
//		//taskExecutor.setConcurrencyLimit(null!=threadConcurrencyLimit?Integer.valueOf(threadConcurrencyLimit):1);
//		return taskExecutor;
//	}
//
	
}
