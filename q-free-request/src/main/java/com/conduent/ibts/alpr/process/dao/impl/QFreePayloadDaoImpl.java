package com.conduent.ibts.alpr.process.dao.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.conduent.ibts.alpr.process.config.LoadJpaQueries;
import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dao.QFreePayloadDao;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.dto.QFreeQueueResponseDto;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLogger;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

@Repository
public class QFreePayloadDaoImpl  {

	private static final Logger logger = LoggerFactory.getLogger(QFreePayloadDaoImpl.class);

	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	public List<QFreeRequestVO> getTransactionsForNormalAndAir(QFreeBatchDto batchDto, MultiLogger extLog) {
		
		logger.info("entering QFreePayloadDaoImpl.getTransactionsForNormalAndAir()  batchDto: "+batchDto+ " threadName: "+Thread.currentThread().getName());
		
		List<QFreeRequestVO> qFreeRequestPayloadList = new CopyOnWriteArrayList<>();
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String query = LoadJpaQueries.getQueryById("GET_TX_TO_ENQUEUE_FOR_NORMAL_AIR");
		
		paramSource.addValue(Constants.LPR_TYPE, batchDto.getLprType());
		paramSource.addValue(Constants.LPR_STATUS, Constants.TO_BE_ENQUEUED);
		paramSource.addValue(Constants.AGENCY_ID, batchDto.getAgencyId());
		paramSource.addValue(Constants.PLAZA_ID, batchDto.getPlazaId());
		paramSource.addValue(Constants.TOTAL_THREADS, batchDto.getTotalThreads());
		paramSource.addValue(Constants.THREAD_INDEX, batchDto.getThreadIndex());
		paramSource.addValue(Constants.BATCH_SIZE, batchDto.getBatchSize());
		
		logger.info("in QFreePayloadDaoImpl.getTransactionsForNormalAndAir() query: {}", query);  
		logger.info("in QFreePayloadDaoImpl.getTransactionsForNormalAndAir() paramSource: {}", paramSource);
		
		try 
		{
			qFreeRequestPayloadList = namedJdbcTemplate.query(query, paramSource, BeanPropertyRowMapper.newInstance(QFreeRequestVO.class));
		}	
		catch(Exception e) {
			logger.error("Error while getting transactions for ALPR process lprType: "+batchDto.getLprType() + "  e: "+ e);	
		}
		
		logger.info("exiting QFreePayloadDaoImpl.getTransactionsForNormalAndAir()  batchDto: "+batchDto+ " threadName: "+Thread.currentThread().getName());
		return qFreeRequestPayloadList;
	}

	public List<QFreeRequestVO> getTransactionsForMir(QFreeBatchDto batchDto) {
		
		List<QFreeRequestVO> qFreeRequestPayloadList = new CopyOnWriteArrayList<>();
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String query = LoadJpaQueries.getQueryById("GET_TX_TO_ENQUEUE_FOR_MIR_ONLY");
		
		logger.info("Query to get transactions for lpr_type = MIR : {}", query);
		
		paramSource.addValue(Constants.LPR_TYPE, batchDto.getLprType());
		paramSource.addValue(Constants.LPR_STATUS, Constants.TO_BE_ENQUEUED);
		paramSource.addValue(Constants.AGENCY_ID, batchDto.getAgencyId());
		paramSource.addValue(Constants.PLAZA_ID, batchDto.getPlazaId());
		paramSource.addValue(Constants.TOTAL_THREADS, batchDto.getTotalThreads());
		paramSource.addValue(Constants.THREAD_INDEX, batchDto.getThreadIndex());
		paramSource.addValue(Constants.BATCH_SIZE, batchDto.getBatchSize());
		
		
		logger.info("param source to get transactions for lpr_type = MIR  : {}", paramSource);
		
		
		try {
			qFreeRequestPayloadList = namedJdbcTemplate.query(query, paramSource, BeanPropertyRowMapper.newInstance(QFreeRequestVO.class));
		}catch(Exception e) {
			logger.error("Error while getting transactions for ALPR process lprType: "+batchDto.getLprType() + "  e: "+ e);	
		}

		
		
		return qFreeRequestPayloadList;
	}

	public List<QFreeRequestVO> getTransactionsForDispute(QFreeBatchDto batchDto) {
		List<QFreeRequestVO> qFreeRequestPayloadList = new CopyOnWriteArrayList<>();
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String query = LoadJpaQueries.getQueryById("GET_TX_TO_ENQUEUE_FOR_DISPUTE");
		logger.info("Query to get transactions for lpr_type = DISPUTE  : {}", query);
		paramSource.addValue(Constants.LPR_TYPE, batchDto.getLprType());
		paramSource.addValue(Constants.LPR_STATUS, Constants.TO_BE_ENQUEUED);
		paramSource.addValue(Constants.AGENCY_ID, batchDto.getAgencyId());
		paramSource.addValue(Constants.PLAZA_ID, batchDto.getPlazaId());
		paramSource.addValue(Constants.TOTAL_THREADS, batchDto.getTotalThreads());
		paramSource.addValue(Constants.THREAD_INDEX, batchDto.getThreadIndex());
		paramSource.addValue(Constants.BATCH_SIZE, batchDto.getBatchSize());
		
		logger.info("param source to get transactions for lpr_type = DISPUTE  : {}", paramSource);
		qFreeRequestPayloadList = namedJdbcTemplate.query(query, paramSource, BeanPropertyRowMapper.newInstance(QFreeRequestVO.class));
		return qFreeRequestPayloadList;
	}
}
