package com.conduent.ibts.alpr.process.dao.impl;

import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.conduent.app.timezone.utility.TimeZoneConv;
import com.conduent.ibts.alpr.process.config.LoadJpaQueries;
import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dto.QFreeQueueDto;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLogger;

@Repository
//public class QFreeDaoImpl implements QFreeDao {
public class QFreeDaoImpl {

	private static final Logger logger = LoggerFactory.getLogger(QFreeDaoImpl.class);

	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	private TimeZoneConv timeZoneConv;

	/*
	@Override
	public List<QFreeQueueResponseDto> getAllTransactionFromLprTable(QFreeBatchDto batchDto) {
		logger.info("entering QFreeDaoImpl.getAllTransactionFromLprTable() batchDto: " + batchDto);
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String query = LoadJpaQueries.getQueryById("GET_ALL_TXNS_FROM_LPR_TABLE_WITHOUT_PLAZA_AGENCY_COND");		
		paramSource.addValue(Constants.LPR_TYPE, batchDto.getLprType(), Types.VARCHAR);
		
		if (null != batchDto.getAgencyId() && 0 != batchDto.getAgencyId().intValue()) 
		{
			query = LoadJpaQueries.getQueryById("GET_ALL_TXNS_FROM_LPR_TABLE_WITH_AGENCY_COND");
			paramSource.addValue(Constants.AGENCY_ID, batchDto.getAgencyId(), Types.NUMERIC);
		}
		if (null != batchDto.getPlazaId() && 0 != batchDto.getPlazaId().intValue()) 
		{
			query = LoadJpaQueries.getQueryById("GET_ALL_TXNS_FROM_LPR_TABLE_WITH_PLAZA_COND");
			paramSource.addValue(Constants.PLAZA_ID, batchDto.getPlazaId(), Types.NUMERIC);
		}
		
		logger.info("in QFreeDaoImpl.getAllTransactionFromLprTable() query:"+query);
		logger.info("in QFreeDaoImpl.getAllTransactionFromLprTable() paramSource:"+paramSource);
		
		logger.info("exiting QFreeDaoImpl.getAllTransactionFromLprTable() batchDto: " + batchDto);
		
		return namedJdbcTemplate.query(query, paramSource,
				BeanPropertyRowMapper.newInstance(QFreeQueueResponseDto.class));
	}*/

	//@Override
	public boolean updateLprStatus(QFreeQueueDto qFreeQueueDto, MultiLogger extLog) {
		logger.info("entering QFreeDaoImpl.updateLprStatus() qFreeQueueDto: "+qFreeQueueDto+" threadName: "+Thread.currentThread().getName());
		extLog.info("entering QFreeDaoImpl.updateLprStatus() qFreeQueueDto: "+qFreeQueueDto+" threadName: "+Thread.currentThread().getName());
		
		boolean isUpdated = false;
		int result = 0;
		try {
			MapSqlParameterSource paramSource = new MapSqlParameterSource();

			String queryforUpdate = LoadJpaQueries.getQueryById("UPDATE_LPR_STATUS");

			paramSource.addValue(Constants.LPR_STATUS, Constants.ENQUEUED, Types.VARCHAR);
			paramSource.addValue(Constants.LANE_TX_ID, qFreeQueueDto.getLaneTxId(), Types.NUMERIC);
			paramSource.addValue(Constants.UPDATE_TS, timeZoneConv.currentTime(), Types.TIMESTAMP);
			
			logger.info("in QFreeDaoImpl.updateLprStatus() queryforUpdate: "+ queryforUpdate);
			logger.info("in QFreeDaoImpl.updateLprStatus() paramsource: "+ paramSource);
			extLog.info("in QFreeDaoImpl.updateLprStatus() queryforUpdate: "+ queryforUpdate);
			extLog.info("in QFreeDaoImpl.updateLprStatus() paramsource: "+ paramSource);

			try {
				result = namedJdbcTemplate.update(queryforUpdate, paramSource);
				if (result > 0) {
					isUpdated = true;
					return isUpdated;
				}
			} catch (EmptyResultDataAccessException empty) {
			}
		} catch (Exception e) {
			logger.error("error in QFreeDaoImpl.updateLprStatus() qFreeQueueDto: "+qFreeQueueDto+" threadName: "+Thread.currentThread().getName()+"  e"+ e.getMessage());
			extLog.error("error in QFreeDaoImpl.updateLprStatus() qFreeQueueDto: "+qFreeQueueDto+" threadName: "+Thread.currentThread().getName()+"  e"+ e.getMessage());
		}
		
		logger.info("exiting QFreeDaoImpl.updateLprStatus() qFreeQueueDto: " + qFreeQueueDto+" threadName: "+Thread.currentThread().getName());
		extLog.info("exiting QFreeDaoImpl.updateLprStatus() qFreeQueueDto: " + qFreeQueueDto+" threadName: "+Thread.currentThread().getName());
		
		return isUpdated;
	}

	//@Override
	public boolean checkLprStatus(String msgKey, String processStatus, MultiLogger extLog) {
		logger.info("entering QFreeDaoImpl.checkLprStatus() msgKey: "+msgKey+" processStatus: "+processStatus+" threadName: "+Thread.currentThread().getName());
		extLog.info("entering QFreeDaoImpl.checkLprStatus() msgKey: "+msgKey+" processStatus: "+processStatus+" threadName: "+Thread.currentThread().getName());
				
		Boolean isStatusQueued = false;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String query = LoadJpaQueries.getQueryById("CHECK_LPR_STATUS_IN_T_LPR_QUEUE");
		paramSource.addValue(Constants.MSG_KEY, msgKey);
		paramSource.addValue(Constants.LPR_STATUS, processStatus);

		Integer count = null;
		
		logger.info("in QFreeDaoImpl.checkLprStatus()  query: " + query);
		logger.info("in QFreeDaoImpl.checkLprStatus()  paramSource: " + paramSource);
		extLog.info("in QFreeDaoImpl.checkLprStatus()  query: " + query);
		extLog.info("in QFreeDaoImpl.checkLprStatus()  paramSource: " + paramSource);
				
		try {
			count = namedJdbcTemplate.queryForObject(query, paramSource, Integer.class);
		} catch (EmptyResultDataAccessException error) {
			logger.info("error in QFreeDaoImpl.checkLprStatus() for msgKey:"+ msgKey+"  processStatus: "+processStatus+" threadName: "+Thread.currentThread().getName()+" error: " +error.getMessage());
			extLog.info("error in QFreeDaoImpl.checkLprStatus() for msgKey:"+ msgKey+"  processStatus: "+processStatus+" threadName: "+Thread.currentThread().getName()+" error: " +error.getMessage());

		} catch (Exception e) {
			logger.info("error in QFreeDaoImpl.checkLprStatus() for msgKey:"+ msgKey+"  processStatus: "+processStatus+" threadName: "+Thread.currentThread().getName()+"  e: " +e.getMessage());
			extLog.info("error in QFreeDaoImpl.checkLprStatus() for msgKey:"+ msgKey+"  processStatus: "+processStatus+" threadName: "+Thread.currentThread().getName()+"  e: " +e.getMessage());
		}
		if (count > 0) {			
			isStatusQueued = true;
		} 
		
		logger.info("exiting QFreeDaoImpl.checkLprStatus() msgKey: "+ msgKey+"  processStatus: "+processStatus+ "  count: "+count + 
				" isStatusQueued: "+isStatusQueued +" threadName: "+Thread.currentThread().getName());

		extLog.info("exiting QFreeDaoImpl.checkLprStatus() msgKey: "+ msgKey+"  processStatus: "+processStatus+ "  count: "+count + 
				" isStatusQueued: "+isStatusQueued +" threadName: "+Thread.currentThread().getName());
		
		return isStatusQueued;
	}

	//@Override
	public int updateLprStatus(String msgKey, String processStatus, MultiLogger extLog) {
		logger.info("entering QFreeDaoImpl.updateLprStatus() msgKey: " + msgKey + "  processStatus: "+ processStatus+" threadName: "+Thread.currentThread().getName());
		extLog.info("entering QFreeDaoImpl.updateLprStatus() msgKey: " + msgKey + "  processStatus: "+ processStatus+" threadName: "+Thread.currentThread().getName());
		
		int result = 0;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String query = LoadJpaQueries.getQueryById("UPDATE_T_LPR_QUEUE_LPR_STATUS");

		paramSource.addValue(Constants.MSG_KEY, msgKey, Types.NUMERIC);
		paramSource.addValue(Constants.LPR_STATUS, processStatus, Types.VARCHAR);
		paramSource.addValue(Constants.UPDATE_TS, timeZoneConv.currentTime(), Types.TIMESTAMP);

		logger.info("in QFreeDaoImpl.updateLprStatus() query: "+query);
		logger.info("in QFreeDaoImpl.updateLprStatus() paramSource: "+paramSource);
		extLog.info("in QFreeDaoImpl.updateLprStatus() query: "+query);
		extLog.info("in QFreeDaoImpl.updateLprStatus() paramSource: "+paramSource);
				
		try {

			result = namedJdbcTemplate.update(query, paramSource);

		} catch (DataAccessException e) {
			logger.info("error in QFreeDaoImpl.updateLprStatus() for msgKey: " + msgKey + "  processStatus: "+ processStatus+" threadName: "+Thread.currentThread().getName() + " e: " + e.getMessage());
			extLog.info("error in QFreeDaoImpl.updateLprStatus() for msgKey: " + msgKey + "  processStatus: "+ processStatus+" threadName: "+Thread.currentThread().getName() + " e: " + e.getMessage());
			e.printStackTrace();
		}
		
		logger.info("exiting QFreeDaoImpl.updateLprStatus() msgKey: "+msgKey + "  processStatus: "+ processStatus + 
				" result: " + result + " threadName: "+Thread.currentThread().getName());

		extLog.info("exiting QFreeDaoImpl.updateLprStatus() msgKey: "+msgKey + "  processStatus: "+ processStatus + 
				" result: " + result + " threadName: "+Thread.currentThread().getName());
		return result;
	}

	//@Override
	public void updateExecutionStatusAndStartTime(Long runId, String executionStatus, MultiLogger extLog) {
		extLog.info("entering QFreeDaoImpl.updateExecutionStatusAndStartTime() runId: "+runId + " executionStatus: "+executionStatus+
			" threadName: "+Thread.currentThread().getName());				
		
		int result = 0;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String query = LoadJpaQueries.getQueryById("UPDATE_Q_FREE_REQ_BATCH_EXECUTION_STATUS_AND_START_TIME");
		
		paramSource.addValue(Constants.RUN_ID, 				runId, 						Types.NUMERIC);
		paramSource.addValue(Constants.EXECUTION_STATUS, 	executionStatus, 			Types.VARCHAR);
		paramSource.addValue(Constants.START_TIME, 			timeZoneConv.currentTime(), Types.TIMESTAMP);

		extLog.info("in QFreeDaoImpl.updateExecutionStatusAndStartTime() query: "+ query);
		extLog.info("in QFreeDaoImpl.updateExecutionStatusAndStartTime() paramSource: "+ paramSource);
		
		try 
		{
			result = namedJdbcTemplate.update(query, paramSource);

		} catch (DataAccessException e) 
		{
			extLog.info("Error in QFreeDaoImpl.updateExecutionStatusAndStartTime() for runId: "+runId+" executionStatus: "+executionStatus +" e: "+e);
			e.printStackTrace();
		}
		
		extLog.info("exiting QFreeDaoImpl.updateExecutionStatusAndStartTime() runId: "+runId+" executionStatus: "+executionStatus+" number of rows updated: "+ result+"\n");
	}

	//@Override
	public void updateExecutionStatusAndEndTime(Long runId, String executionStatus, MultiLogger extLog) {
		extLog.info("entering QFreeDaoImpl.updateExecutionStatusAndEndTime() runId: "+runId+" executionStatus: "+executionStatus+
			" threadName: "+Thread.currentThread().getName());
				
		int result = 0;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String query = LoadJpaQueries.getQueryById("UPDATE_Q_FREE_REQ_BATCH_EXECUTION_STATUS_AND_END_TIME");
		
		paramSource.addValue(Constants.RUN_ID, runId, Types.NUMERIC);
		paramSource.addValue(Constants.EXECUTION_STATUS, executionStatus, Types.VARCHAR);
		paramSource.addValue(Constants.END_TIME, timeZoneConv.currentTime(), Types.TIMESTAMP);

		extLog.info("in QFreeDaoImpl.updateExecutionStatusAndEndTime() query: "+ query);
		extLog.info("in QFreeDaoImpl.updateExecutionStatusAndEndTime() paramSource: "+ paramSource);
		
		try {
			result = namedJdbcTemplate.update(query, paramSource);

		} catch (DataAccessException e) {
			extLog.info("Error in QFreeDaoImpl.updateExecutionStatusAndEndTime() for runId: "+runId+" executionStatus: "+
					executionStatus+" e: "+e);
			e.printStackTrace();
		}
		extLog.info("exiting QFreeDaoImpl.updateExecutionStatusAndEndTime() runId: "+runId+" executionStatus: "+executionStatus+
		" number of rows updated : "+ result);

	}

	//@Override
	public boolean checkSimilarRowInBatchTable(Long runId, Long agencyId, Long plazaId, String lprType, Long threadIndex, MultiLogger extLog) {
		logger.info("entering QFreeDaoImpl.checkSimilarRowInBatchTable() runId: "+runId+" agencyId: "+agencyId+ " plazaId: "+plazaId+ 
				"  lprType: "+ lprType+"  threadIndex: "+threadIndex+" threadName: "+Thread.currentThread().getName());
			
		
		extLog.info("entering QFreeDaoImpl.checkSimilarRowInBatchTable() runId: "+runId+" agencyId: "+agencyId+ " plazaId: "+plazaId+ 
			"  lprType: "+ lprType+"  threadIndex: "+threadIndex+" threadName: "+Thread.currentThread().getName());
		
		
		Boolean duplicateFound = false;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String query = LoadJpaQueries.getQueryById("CHECK_SIMILAR_ROW_BATCH_TABLE");
		paramSource.addValue(Constants.AGENCY_ID, agencyId);
		paramSource.addValue(Constants.PLAZA_ID, plazaId);
		paramSource.addValue(Constants.LPR_TYPE, lprType);
		paramSource.addValue(Constants.RUN_ID, runId);
		paramSource.addValue(Constants.THREAD_INDEX, threadIndex);

		int count = 0;
		
		extLog.info("in QFreeDaoImpl.checkSimilarRowInBatchTable() query: "+ query +" threadName: "+Thread.currentThread().getName());
		extLog.info("in QFreeDaoImpl.checkSimilarRowInBatchTable() paramSource: "+ paramSource+" threadName: "+Thread.currentThread().getName());
		
		try 
		{
			count = namedJdbcTemplate.queryForObject(query, paramSource, int.class);
		} catch (EmptyResultDataAccessException empty) 
		{			
			extLog.info("Error in QFreeDaoImpl.checkSimilarRowInBatchTable() for runId: "+runId+"  agencyId: "+agencyId+ "  plazaId: "+plazaId+ 					
				" lprType: "+ lprType + " threadName: "+Thread.currentThread().getName() +"  error: "+empty.getMessage());

		} catch (Exception e) {
			extLog.info("Error in QFreeDaoImpl.checkSimilarRowInBatchTable() for runId: "+runId+"  agencyId: "+agencyId+ "  plazaId: "+plazaId+ 					
					 " lprType: "+ lprType + " threadName: "+Thread.currentThread().getName() + "  e: "+e.getMessage());
		}
		if (count > 0) {
			extLog.info("in QFreeDaoImpl.checkSimilarRowInBatchTable() similar row in batch table for AGENCY_ID: "+agencyId+" PLAZA_ID: "+plazaId+
				" LPR_TYPE: "+lprType+" is available");
								
			duplicateFound = true;
		} 
		
		extLog.info("exiting QFreeDaoImpl.checkSimilarRowInBatchTable() runId: "+runId+" agencyId: "+agencyId+ " plazaId: "+plazaId+ 
				"  lprType: "+ lprType+"  threadIndex: "+threadIndex+ "  duplicateFound: "+duplicateFound+" threadName: "+Thread.currentThread().getName()+"\n");

		return duplicateFound;

	}
	
	
	public String getAgencyShortName(Long agencyId) {
		logger.info("Getting agency short name for AGENCY_ID: {}", agencyId);
		String agencyShortName = null;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String query = LoadJpaQueries.getQueryById("GET_AGENCY_SHORT_NAME");
		paramSource.addValue(Constants.AGENCY_ID, agencyId);
		try {
			agencyShortName = namedJdbcTemplate.queryForObject(query, paramSource, String.class);
		} catch (EmptyResultDataAccessException empty) {
			logger.info("No data exists in crm.v_agency table for agencyId: {}", agencyId);

		} catch (Exception e) {
			logger.info("Got Exception while Getting agency short name for AGENCY_ID: {}, : {}", e.getMessage());

		}
		logger.info("agency short name for AGENCY_ID: {}", agencyId);
		
		return agencyShortName;
	}

}
