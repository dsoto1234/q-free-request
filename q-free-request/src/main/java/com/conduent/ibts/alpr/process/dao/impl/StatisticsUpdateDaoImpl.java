package com.conduent.ibts.alpr.process.dao.impl;

import java.sql.Timestamp;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.conduent.ibts.alpr.process.config.LoadJpaQueries;
import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dao.StatisticsUpdateDao;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.dto.TviolXferStatistics;
import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;
import com.conduent.ibts.alpr.process.utility.CommanUtility;
import com.conduent.ibts.alpr.process.utility.MasterCache;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

import cloud.intrada.passage.PassageAcknowledgementType;

@Repository
public class StatisticsUpdateDaoImpl implements StatisticsUpdateDao {

	Logger logger = LoggerFactory.getLogger(StatisticsUpdateDaoImpl.class);
	private static final String THIS_CLASS_NAME = "StatisticsUpdateDaoImpl";

	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	@Autowired
	MasterCache masterCache;
	
	@Autowired
	CommanUtility commonUtility;

	private Long getViolXferId() {
		logger.info("entering StatisticsUpdateDaoImpl.getViolXferId() getting file id from sequence");
		
		Long violXferId = null;		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String queryToGetFileId = LoadJpaQueries.getQueryById("GET_VIOL_XFER_FILE_ID_SEQ_NUM");
		logger.info("in StatisticsUpdateDaoImpl.getViolXferId() Query to get file id from sequence :{}", queryToGetFileId);
		try {
			violXferId = namedJdbcTemplate.queryForObject(queryToGetFileId, paramSource, Long.class);
		} catch (DataAccessException e) {
			logger.error("error in StatisticsUpdateDaoImpl.getViolXferId() Could not fetch file id! from sequence");
		} catch(Exception e) {
			logger.error("Exception in StatisticsUpdateDaoImpl.getViolXferId() while getting viol_xfer_id from sequence :{}", e.getMessage());
		}

		return violXferId;
	}

	private TviolXferStatistics getTViolXferStatistics(PassageAcknowledgementType acknowldegement, String fileType,
			QFreeRequestVO passageTypeVO, TviolXferStatistics violXferStatistics, QFreeBatchDto batchDto) throws VectorImageReviewException {
		
		logger.info("entering StatisticsUpdateDaoImpl.getTViolXferStatistics() laneTxId: "+acknowldegement.getHostPassageID());

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		long violXferFileId = -1;

		logger.info("in StatisticsUpdateDaoImpl.getTViolXferStatistics() getting viol xfer file id from TViolXferStatistics table");
		
		try {
			String recordCount=masterCache.getqFreeProcessParameters().get(Constants.PARAM_NAME_STATS_MAX_RECORD_COUNT);
			logger.info("in StatisticsUpdateDaoImpl.getTViolXferStatistics() STATS_MAX_RECORD_COUNT :{}",recordCount);
			
			String query = LoadJpaQueries.getQueryById("SELECT_T_VIOL_XFER_STATISTICS_CNT_SQL");
			
			paramSource.addValue(Constants.AGENCY_ID, 			passageTypeVO.getAgencyId());
			paramSource.addValue(Constants.TX_DATE, 			commonUtility.convertedDate(passageTypeVO.getTxDate()));
			paramSource.addValue(Constants.SECTION_ID, 			passageTypeVO.getSectionId());
			paramSource.addValue(Constants.PLAZA_ID, 			passageTypeVO.getPlazaId());
			paramSource.addValue(Constants.AET_FLAG, 			passageTypeVO.getAetFlag());
			paramSource.addValue(Constants.IMAGE_RECEIVE_DATE,	commonUtility.convertedDate(passageTypeVO.getImageReceiveDate()));
			paramSource.addValue(Constants.VAR_RECORD_COUNT, 	recordCount);
			paramSource.addValue(Constants.IS_UNMATCHED,		passageTypeVO.getUnmatchedEntryFlag().equalsIgnoreCase("Y") ? "T" : "F");

			logger.info("in StatisticsUpdateDaoImpl.getTViolXferStatistics() query: "+ query);
			logger.info("in StatisticsUpdateDaoImpl.getTViolXferStatistics() paramsource :"+ paramSource);
			
			try {
				violXferFileId = namedJdbcTemplate.queryForObject(query, paramSource, Long.class);
			} catch (EmptyResultDataAccessException e) {
				logger.info("in StatisticsUpdateDaoImpl.getTViolXferStatistics() Data not found in xfer statistics table :{}", e.getMessage());
			}
			violXferStatistics.setViolXferFileId(violXferFileId);

			logger.info("in StatisticsUpdateDaoImpl.getTViolXferStatistics() laneTxId: " + acknowldegement.getHostPassageID() + " violXferStatistics.getViolXferFileId(): "+
					violXferStatistics.getViolXferFileId() + " violTransferFileId: "+ violXferFileId);
			
		} catch (Exception ex2) {
			logger.error("Error in StatisticsUpdateDaoImpl.getTViolXferStatistics() occurred for laneTxId: "+ acknowldegement.getHostPassageID()+ "  while fetching data from tviolxferstatistics table ex2: "+
					ex2.getMessage() + "  "+ex2);
			throw new VectorImageReviewException("Error in StatisticsUpdateDaoImpl.getTViolXferStatistics() occurred for laneTxId: "+ acknowldegement.getHostPassageID()+ "  while fetching data from tviolxferstatistics table :"+
					ex2.getMessage() + "  "+ex2);						
		}
		
		logger.info("exiting StatisticsUpdateDaoImpl.getTViolXferStatistics() acknowldegement.getHostPassageID(): "+acknowldegement.getHostPassageID());
		
		return violXferStatistics;
	}

	
	 //@Transactional(rollbackFor = Exception.class, noRollbackFor = DataAccessException.class)
	 @Transactional(propagation = Propagation.NESTED ,rollbackFor = Exception.class, noRollbackFor = DataAccessException.class)
	@Override
	public boolean updateTViolXferStatistics(PassageAcknowledgementType acknowldegement, String fileType,
		QFreeRequestVO passageTypeVO, TviolXferStatistics violXferStatistics, QFreeBatchDto batchDto) throws VectorImageReviewException {
		
		 logger.info("entering StatisticsUpdateDaoImpl.updateTViolXferStatistics() acknowldegement.getHostPassageID(): "+acknowldegement.getHostPassageID()+
				 " batchDto: "+batchDto);		 
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String query = null;
		int tviolXferStatisticsCnt = 0;
		boolean dbupdateSuccess = false;

		try {

			violXferStatistics = getTViolXferStatistics(acknowldegement, fileType, passageTypeVO, violXferStatistics, batchDto);

			if (violXferStatistics.getViolXferFileId() <= 0) 
			{
				violXferStatistics.setViolXferFileId(getViolXferId());
				
				if (violXferStatistics.getViolXferFileId() <= 0)
					return false;

				logger.info("in StatisticsUpdateDaoImpl.updateTViolXferStatistics() laneTxId: "+acknowldegement.getHostPassageID()+ 
					" violXferId: "+violXferStatistics.getViolXferFileId());

				query = LoadJpaQueries.getQueryById("INSERT_T_VIOL_XFER_STATISTICS_SQL");

				paramSource.addValue(Constants.VIOL_XFER_FILE_ID, 	violXferStatistics.getViolXferFileId(),	Types.INTEGER);
				paramSource.addValue(Constants.FILE_TYPE, 			fileType, 								Types.VARCHAR);
				paramSource.addValue(Constants.RECORD_COUNT, 		1, 										Types.INTEGER);
				paramSource.addValue(Constants.PROCESS_FLAG, 		"C", 									Types.CHAR);
				paramSource.addValue(Constants.AGENCY_ID, 			passageTypeVO.getAgencyId(), 			Types.INTEGER);

				if (Constants.QFREE_NORMAL.equalsIgnoreCase(batchDto.getLprType())
						|| Constants.QFREE_LPR_TYPE_MIR_ONLY.equalsIgnoreCase(batchDto.getLprType())
						|| Constants.QFREE_DISPUTE.equalsIgnoreCase(batchDto.getLprType())) 
				{
					paramSource.addValue(Constants.IS_DOUBLE_BLIND, "T", Types.CHAR);
				} else 
				{
					paramSource.addValue(Constants.IS_DOUBLE_BLIND, "F", Types.CHAR);
				}
				paramSource.addValue(Constants.REVIEWED_COUNT, 	0, 													Types.INTEGER);
				paramSource.addValue(Constants.TX_DATE, 		new Timestamp(passageTypeVO.getTxDate().getTime()), Types.DATE);
				paramSource.addValue(Constants.SECTION_ID, 		passageTypeVO.getSectionId(), 						Types.INTEGER);
				paramSource.addValue(Constants.PLAZA_ID, 		passageTypeVO.getPlazaId(), 						Types.INTEGER);
				paramSource.addValue(Constants.AET_FLAG, 		passageTypeVO.getAetFlag(), 						Types.CHAR);

				if (Constants.QFREE_NORMAL.equalsIgnoreCase(batchDto.getLprType())
						|| Constants.QFREE_LPR_TYPE_MIR_ONLY.equalsIgnoreCase(batchDto.getLprType())) 
				{
					paramSource.addValue(Constants.IS_REREVIEW, "F", Types.CHAR);
				} 
				else if (Constants.QFREE_DISPUTE.equalsIgnoreCase(batchDto.getLprType())) 
				{
					paramSource.addValue(Constants.IS_REREVIEW, "T", Types.CHAR);
				}

				if (passageTypeVO.getUnmatchedEntryFlag() != null && passageTypeVO.getUnmatchedEntryFlag().equalsIgnoreCase("Y")) 
				{
					paramSource.addValue(Constants.IS_UNMATCHED, "T", Types.CHAR);
				} else 
				{
					paramSource.addValue(Constants.IS_UNMATCHED, "F", Types.CHAR);

				}
				paramSource.addValue(Constants.IMAGE_RECEIVE_DATE,	new Timestamp(passageTypeVO.getImageReceiveDate().getTime()));
				
				logger.info("in in StatisticsUpdateDaoImpl.updateTViolXferStatistics() Inserting into T_VIOL_XFER_STATISTICS , query: "+ query);
				logger.info("in in StatisticsUpdateDaoImpl.updateTViolXferStatistics() Inserting into T_VIOL_XFER_STATISTICS paramSource: " + paramSource);
			} 
			else 
			{
				query = LoadJpaQueries.getQueryById("UPDATE_T_VIOL_XFER_STATISTICS_SQL");
				logger.info("in StatisticsUpdateDaoImpl.updateTViolXferStatistics() updating T_VIOL_XFER_STATISTICS , query: "+ query);

				paramSource.addValue(Constants.PROCESS_FLAG, 		"C", 									Types.CHAR);
				paramSource.addValue(Constants.VIOL_XFER_FILE_ID, 	violXferStatistics.getViolXferFileId(), Types.INTEGER);

				logger.info("in StatisticsUpdateDaoImpl.updateTViolXferStatistics() paramSource for updating T_VIOL_XFER_STATISTICS  paramSource: "+ paramSource);

			}
			tviolXferStatisticsCnt = namedJdbcTemplate.update(query, paramSource);

			logger.info("in StatisticsUpdateDaoImpl.updateTViolXferStatistics() T_VIOL_XFER_STATISTICS table insert/update successful: "+ tviolXferStatisticsCnt);

			if (tviolXferStatisticsCnt > 0)
				dbupdateSuccess = true;
		} catch (Exception ex2) 
		{
			dbupdateSuccess = false;
			logger.error("Error in StatisticsUpdateDaoImpl.updateTViolXferStatistics() for  laneTxId = "+acknowldegement.getHostPassageID()+ 
					"ex2: {} " , ex2.getMessage() + ex2);
			throw new VectorImageReviewException("Error in StatisticsUpdateDaoImpl.updateTViolXferStatistics() for laneTxId = [" + acknowldegement.getHostPassageID()
					+ "] ex2: " + ex2.getMessage() + ex2);
		}

		 logger.info("exiting StatisticsUpdateDaoImpl.updateTViolXferStatistics() acknowldegement.getHostPassageID(): "+acknowldegement.getHostPassageID()+
				 " batchDto: "+batchDto + " dbupdateSuccess: "+ dbupdateSuccess);
		 
		return dbupdateSuccess;
	}

}
