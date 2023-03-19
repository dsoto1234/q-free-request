package com.conduent.ibts.alpr.process.dao.impl;

import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.conduent.ibts.alpr.process.config.LoadJpaQueries;
import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.dto.TviolXferStatistics;
import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;
import com.conduent.ibts.alpr.process.integration.impl.TpmsUpdateUnmatchedEntryIntegrationServiceImpl;
import com.conduent.ibts.alpr.process.integration.impl.TviolTxEventIntegrationApiServiceImpl;
import com.conduent.ibts.alpr.process.utility.MasterCache;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLogger;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

import cloud.intrada.passage.PassageAcknowledgementType;

@Repository
//public class QfreeResponseAckDaoImpl implements QfreeResponseAckDao {
public class QfreeResponseAckDaoImpl {

	Logger logger = LoggerFactory.getLogger(QfreeResponseAckDaoImpl.class);
	static String THIS_CLASS_NAME = "QfreeResponseAckDaoImpl";

	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	private StatisticsUpdateDaoImpl xferStatsUpdateDao;

	@Autowired
	private MasterCache masterCache;
	
	@Autowired
	private TpmsUpdateUnmatchedEntryIntegrationServiceImpl tpmsUpdateUnmatched;
	
	@Autowired
	private TviolTxEventIntegrationApiServiceImpl eventIntegrationApiService;
	
	//@Autowired
	//private TimeZoneConv timeZoneConv;
	
	public QfreeResponseAckDaoImpl() {
		logger.info("entering QfreeResponseAckDaoImpl() constructor");
	}

	//@Transactional(rollbackFor = SQLException.class)
	//@Transactional(rollbackFor = {SQLException.class, VectorImageReviewException.class})
	
	@Transactional(propagation = Propagation.NESTED, rollbackFor = { SQLException.class, VectorImageReviewException.class }, noRollbackFor = DataAccessException.class)	
	public boolean updateQueueAndInsertInfoTables(PassageAcknowledgementType acknowledgement, String ackFromQfree, String ackToNy, int numberOfAttemps, 
		QFreeRequestVO qFreeRequestVO, TviolXferStatistics violXferStatistics, QFreeBatchDto batchDto, MultiLogger extLog) throws VectorImageReviewException {
		
		boolean dbUpdate = false;
		String errorMsg = "TO_BE_ENQUEUED_Error_in_";

		extLog.info("entering QfreeResponseAckDaoImpl.updateQueueAndInsertInfoTables() laneTxId: "+acknowledgement.getHostPassageID()+				
			" issPassageId: "+acknowledgement.getIssPassageID()+"  ackFrom: "+ackFromQfree+ "  ackTo: "	+ ackToNy + "  ackStatus: "+ acknowledgement.getValue() +				
			"workflowType: " + batchDto.getLprType()+" numberOfAttemps: " + numberOfAttemps+ "  msg: " + acknowledgement.getDescription() +
			" agencyId: "+ batchDto.getAgencyId() + " violXferStatistics.getViolXferFileId(): "+violXferStatistics.getViolXferFileId()+
			" qFreeRequestVO:" + qFreeRequestVO);
			
		try {

			if (Constants.ACKNOWLEDGEMENT_STATUS_OK.equalsIgnoreCase(acknowledgement.getValue().value())
					&& ( Constants.QFREE_NORMAL.equalsIgnoreCase(batchDto.getLprType())
					||   Constants.QFREE_LPR_TYPE_MIR_ONLY.equalsIgnoreCase(batchDto.getLprType())
					||   Constants.QFREE_DISPUTE.equalsIgnoreCase(batchDto.getLprType()) ) ) {

				dbUpdate = xferStatsUpdateDao.updateTViolXferStatistics(acknowledgement, Constants.FILE_TYPE_TR, qFreeRequestVO, violXferStatistics, batchDto);

				extLog.info("in QfreeResponseAckDaoImpl.updateQueueAndInsertInfoTables() T_VIOL_XFER_STATISTICS dbUpdate: "+dbUpdate);

				// DB ERROR
				if (dbUpdate == false) {					
					updateQFreeQueueTableWithDBError(acknowledgement.getHostPassageID(),errorMsg + "t_viol_xfer_statistics", extLog);
					return false;
				} else if (dbUpdate) {
					extLog.info("in QfreeResponseAckDaoImpl.updateQueueAndInsertInfoTables() Updating TViolImageTx table");
					dbUpdate = updateTViolImageTx(acknowledgement, violXferStatistics, batchDto, extLog);

					if (!dbUpdate) {
						// DB ERROR
						updateQFreeQueueTableWithDBError(acknowledgement.getHostPassageID(),errorMsg + "t_viol_image_tx", extLog);
						return false;
					}
				}

				if (dbUpdate) 
				{
					dbUpdate = false;
					Map<String, Long> codeIdMap = new HashMap<>();
					codeIdMap = masterCache.gettCodeDto().stream()
							.filter(tCodes -> (Constants.CODE_TYPE_EVENT_TYPE.equals(tCodes.getCodeType())
									&& Constants.CODE_VALUE_IMGREVIEW.equals(tCodes.getCodeValue())
									|| Constants.CODE_TYPE_TX_STATUS.equals(tCodes.getCodeType())
											&& Constants.CODE_VALUE_DMVREREVIEW.equals(tCodes.getCodeValue())
									|| Constants.CODE_TYPE_TX_STATUS.equals(tCodes.getCodeType())
											&& Constants.CODE_VALUE_IMGREREVIEW.equals(tCodes.getCodeValue())))
							.collect(Collectors.toMap(t -> t.getCodeValue(), m -> m.getCodeId()));
					
					if (qFreeRequestVO.getUnmatchedEntryFlag().equalsIgnoreCase("Y")) {
						//logger.info("Will update t_Unmatched _Entry_tx table using APi as Unmatched_Entry_Flag = {}",qFreeRequestVO.getUnmatchedEntryFlag());
						dbUpdate = tpmsUpdateUnmatched.updateTUnmatchedEntryTable(acknowledgement.getHostPassageID(), violXferStatistics.getViolXferFileId(), codeIdMap);
					} else {
						extLog.info("Will update t_viol_tx table as Unmatched_Entry_Flag: "+qFreeRequestVO.getUnmatchedEntryFlag());
						dbUpdate = updateQfreeRereviewTxStatus(acknowledgement.getHostPassageID(), violXferStatistics.getViolXferFileId(), codeIdMap, extLog);
					}
					// DB ERROR
					if (!dbUpdate) {
						String tableName = null;
						if (qFreeRequestVO.getUnmatchedEntryFlag().equalsIgnoreCase("Y"))
							tableName = "t_unmatched_entry_tx";
						else
							tableName = "t_viol_tx";

						errorMsg = errorMsg + tableName;					
						updateQFreeQueueTableWithDBError(acknowledgement.getHostPassageID(), errorMsg, extLog);
						return false;
					}
				}
			}
			if (Constants.QFREE_NORMAL.equalsIgnoreCase(batchDto.getLprType()) || Constants.QFREE_AIR_ONLY.equalsIgnoreCase(batchDto.getLprType())
					|| Constants.QFREE_LPR_TYPE_MIR_ONLY.equalsIgnoreCase(batchDto.getLprType()) || Constants.QFREE_DISPUTE.equalsIgnoreCase(batchDto.getLprType())) {
				dbUpdate = false;
				
				dbUpdate = updateTLprQueue(acknowledgement, violXferStatistics, batchDto, qFreeRequestVO, extLog);
			}

			if (acknowledgement.getHostPassageID().equals("20000540871"))
				throw new VectorImageReviewException ("dannyyyyyyyyy sotooooooooo errorrrrrr");			
			
			if (dbUpdate) {
				extLog.info("Inserting into T_LPR_ACK_INFO table!");
				if (!insertTLprAckInfo(acknowledgement, ackFromQfree, ackToNy, numberOfAttemps, qFreeRequestVO, batchDto, extLog)) {
					// DB ERROR
					extLog.info("Updating lpr status with t_lpr_ack_info table error!");
					updateQFreeQueueTableWithDBError(acknowledgement.getHostPassageID(), errorMsg + "t_lpr_ack_info", extLog);
					return false;
				}

			} else if (!dbUpdate) { // DB ERROR
				extLog.info("Updating lpr status with t_lpr_qfree_queue table error!");
				updateQFreeQueueTableWithDBError(acknowledgement.getHostPassageID(), errorMsg + "t_lpr_qfree_queue", extLog);
				return false;
			}
					
		} catch (Exception e) {
			dbUpdate = false;			
			extLog.error("error in QfreeResponseAckDaoImpl.updateQueueAndInsertInfoTables() while updating laneTxId: "+acknowledgement.getHostPassageID()+"  e: "+e.getMessage()+"\n");
			throw new VectorImageReviewException("error in QfreeResponseAckDaoImpl.updateQueueAndInsertInfoTables() while updating laneTxId: "+acknowledgement.getHostPassageID()+"  e: "+e.getMessage() + "\n");
		}

		extLog.info("exiting QfreeResponseAckDaoImpl.updateQueueAndInsertInfoTables() laneTxId: "+acknowledgement.getHostPassageID());	

		return dbUpdate;
	}

	//@Transactional(rollbackFor = Exception.class)
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = SQLException.class)
	public boolean updateQFreeQueueTableWithDBError(String laneTxId, String errorMsg, MultiLogger extLog) throws VectorImageReviewException {
		
		extLog.info("entering QfreeResponseAckDaoImpl.updateQFreeQueueTableWithDBError() laneTxId: "+laneTxId+" errorMsg: "+errorMsg);

		boolean dbupdateSuccess = false;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		try {
			String query = LoadJpaQueries.getQueryById("Update_T_LPR_QUEUE_With_DB_ERROR");
			
			extLog.info("Query to update lpr queue table with db error: "+ query);
			
			paramSource.addValue(Constants.LPR_STATUS, errorMsg, Types.VARCHAR);
			paramSource.addValue(Constants.LANE_TX_ID, laneTxId, Types.NUMERIC);
			
			extLog.info("param source to update lpr queue table with db error: "+ paramSource);
			
			int val = namedJdbcTemplate.update(query, paramSource);

			if (val > 0)
				dbupdateSuccess = true;

			extLog.info("in QfreeResponseAckDaoImpl.updateQFreeQueueTableWithDBError() laneTxId: "+laneTxId+" dbupdateSuccess: "+dbupdateSuccess);

		} catch (Exception ex2) {
			extLog.error("updateQFreeQueueTableWithDBError() ex2: "+ ex2.getMessage() + " " + ex2);
			throw new VectorImageReviewException("Exception: laneTxId = [" + laneTxId + "] ex2: " + ex2.getMessage() + ex2);
		}
		
		extLog.info("exiting QfreeResponseAckDaoImpl.updateQFreeQueueTableWithDBError() laneTxId: "+laneTxId);

		return dbupdateSuccess;
	}

	//@Transactional(rollbackFor = Exception.class)
	@Transactional(propagation = Propagation.NESTED, rollbackFor = SQLException.class)
	public boolean insertTLprAckInfo(PassageAcknowledgementType acknowledgement, String ackFromQfree, String ackToNy,
		int numberOfAttemps, QFreeRequestVO qFreeRequestVO, QFreeBatchDto batchDto, MultiLogger extLog) throws VectorImageReviewException {
		
		extLog.info("entering QfreeResponseAckDaoImpl.insertTLprAckInfo() laneTxId: " + acknowledgement.getHostPassageID());
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		boolean dbupdateSuccess = false;
		try {
			String queryStr = LoadJpaQueries.getQueryById("Insert_T_LPR_ACK_INFO");
			
			paramSource.addValue(Constants.HOST_PASSAGE_ID, acknowledgement.getHostPassageID(), Types.NUMERIC);
			paramSource.addValue(Constants.ISS_PASSAGE_ID, acknowledgement.getIssPassageID(), Types.VARCHAR);
			paramSource.addValue(Constants.ACK_TYPE, qFreeRequestVO.getLprStatus(), Types.VARCHAR);
			paramSource.addValue(Constants.ACK_STATUS, acknowledgement.getValue().value(), Types.VARCHAR);
			paramSource.addValue(Constants.ACK_DESCRIPTION, acknowledgement.getDescription(), Types.VARCHAR);
			paramSource.addValue(Constants.NUMBER_OF_ATTEMPTS, numberOfAttemps, Types.INTEGER);
			paramSource.addValue(Constants.ACK_FROM, ackFromQfree, Types.VARCHAR);
			paramSource.addValue(Constants.ACK_TO, ackToNy, Types.VARCHAR);
			paramSource.addValue(Constants.THREAD_ID, batchDto.getThreadIndex(), Types.VARCHAR);
			
			extLog.info("in QfreeResponseAckDaoImpl.insertTLprAckInfo() queryStr: "+ queryStr);
			extLog.info("in QfreeResponseAckDaoImpl.insertTLprAckInfo()   paramSource : "+ paramSource);

			int val = namedJdbcTemplate.update(queryStr, paramSource);

			if (val > 0)
				dbupdateSuccess = true;

		} catch (Exception ex2) {
			extLog.error("error in QfreeResponseAckDaoImpl.insertTLprAckInfo() laneTxId: " + acknowledgement.getHostPassageID() + " ex2: "+ex2);
			throw new VectorImageReviewException("error in QfreeResponseAckDaoImpl.insertTLprAckInfo() laneTxId: " + acknowledgement.getHostPassageID() + " ex2: "+ex2);
		}
		
		extLog.info("exiting QfreeResponseAckDaoImpl.insertTLprAckInfo() laneTxId: " + acknowledgement.getHostPassageID() + "  dbupdateSuccess:"+dbupdateSuccess);

		return dbupdateSuccess;
	}

	//@Transactional(rollbackFor = Exception.class)
	@Transactional(propagation = Propagation.NESTED, rollbackFor = SQLException.class)
	public boolean updateTViolImageTx(PassageAcknowledgementType acknowledgement,TviolXferStatistics violXferStatistics, QFreeBatchDto batchDto,
		MultiLogger extLog) throws VectorImageReviewException {

		extLog.info("entering QfreeResponseAckDaoImpl.updateTViolImageTx() acknowledgement.getHostPassageID(): "+acknowledgement.getHostPassageID() + 
			" violXferStatistics.getViolXferFileId(): "+violXferStatistics.getViolXferFileId()+
			" batchDto: "+batchDto);
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		boolean dbupdateSuccess = false;

		try {
			String query = LoadJpaQueries.getQueryById("Update_T_VIOL_IMAGE_TX_QFREE_TX_WITHOUT_MIR_OR_REREVIEW");			
			
			if (batchDto.getLprType().equalsIgnoreCase(Constants.QFREE_DISPUTE))
				paramSource.addValue(Constants.PROCESS_FLAG, Constants.QFREE_PROCESS_FLAG_REREVIEW, Types.INTEGER);
			else				
				paramSource.addValue(Constants.PROCESS_FLAG, Constants.QFREE_PROCESS_FLAG_DEFAULT,  Types.INTEGER);
									
			paramSource.addValue(Constants.TR_FILE_ID, 		violXferStatistics.getViolXferFileId(),	Types.INTEGER);
			paramSource.addValue(Constants.LANE_TX_ID, 		acknowledgement.getHostPassageID(),		Types.NUMERIC);
			
			extLog.info("in QfreeResponseAckDaoImpl.updateTViolImageTx() query: "+ query);
			extLog.info("in QfreeResponseAckDaoImpl.updateTViolImageTx() paramSource : "+paramSource);
			
			int tViolImageTxCnt = namedJdbcTemplate.update(query, paramSource);

			extLog.info("in QfreeResponseAckDaoImpl.updateTViolImageTx() t_viol_image_txt has been updated tViolImageTxCnt:  "+tViolImageTxCnt);

			if (tViolImageTxCnt > 0)
				dbupdateSuccess = true;

		} catch (Exception ex2) {
			extLog.error("Error in QfreeResponseAckDaoImpl.updateTViolImageTx() for laneTxId: "+acknowledgement.getHostPassageID()+"  ex2: "+ex2.getMessage());
			throw new VectorImageReviewException("Error in QfreeResponseAckDaoImpl.updateTViolImageTx() for laneTxId: "+acknowledgement.getHostPassageID() + "  ex2: "+ex2.getMessage());
		}
		
		extLog.info("exiting QfreeResponseAckDaoImpl.updateTViolImageTx() laneTxId: " + acknowledgement.getHostPassageID());
		return dbupdateSuccess;
	}


	//@Transactional(rollbackFor = Exception.class)
	@Transactional(propagation = Propagation.NESTED, rollbackFor = SQLException.class)
	public boolean updateQfreeRereviewTxStatus(String laneTxId, long violXferId, Map<String, Long> codeIdMap, MultiLogger extLog) throws VectorImageReviewException {

		extLog.info("entering QfreeResponseAckDaoImpl.updateQfreeRereviewTxStatus() laneTxId: "+laneTxId+"  violXferId: "+violXferId
			+ " EXISTING_TX_STATUS: " + codeIdMap.get(Constants.CODE_VALUE_IMGREREVIEW) + " TX_STATUS: " + codeIdMap.get(Constants.CODE_VALUE_DMVREREVIEW));
				

		boolean dbupdateSuccess = false;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		try {
			String queryStr = LoadJpaQueries.getQueryById("ENQUEUE_UPDATE_T_VIOL_TX_STATUS");

			paramSource.addValue(Constants.OUTPUT_FILE_TYPE, 	Constants.TVIOLTX_OUTPUT_FILE_TYPE_TR, 			Types.VARCHAR);
			paramSource.addValue(Constants.EXISTING_TX_STATUS, 	codeIdMap.get(Constants.CODE_VALUE_IMGREREVIEW),Types.INTEGER);
			paramSource.addValue(Constants.TX_STATUS, 			codeIdMap.get(Constants.CODE_VALUE_DMVREREVIEW),Types.INTEGER);
			paramSource.addValue(Constants.EVENT_TYPE, 			codeIdMap.get(Constants.CODE_VALUE_IMGREVIEW),	Types.INTEGER);
			paramSource.addValue(Constants.IMAGE_BATCH_ID, 		violXferId,										Types.INTEGER);
			paramSource.addValue(Constants.OUTPUT_FILE_ID, 		violXferId, 									Types.INTEGER);
			paramSource.addValue(Constants.LANE_TX_ID, 			laneTxId, 										Types.NUMERIC);
			
			extLog.info("in QfreeResponseAckDaoImpl.updateQfreeRereviewTxStatus() queryStr: "+queryStr);
			extLog.info("in QfreeResponseAckDaoImpl.updateQfreeRereviewTxStatus() paramSource: "+ paramSource);
									
			int val = namedJdbcTemplate.update(queryStr, paramSource);
			if (val > 0)
				dbupdateSuccess = eventIntegrationApiService.callingEventApi(Long.valueOf(laneTxId));

			extLog.info("in QfreeResponseAckDaoImpl.updateQfreeRereviewTxStatus() laneTxId: "+laneTxId+"  violXferId: "+violXferId+
				"  dbupdateSuccess: "+dbupdateSuccess);
			
		} catch (Exception ex2) {
			extLog.error("Error in QfreeResponseAckDaoImpl.updateQfreeRereviewTxStatus() for laneTxId: "+laneTxId+"  violXferId: "+violXferId +
				" ex2: "+ ex2.getMessage() + " " + ex2);
			throw new VectorImageReviewException("Error in QfreeResponseAckDaoImpl.updateQfreeRereviewTxStatus() for laneTxId: "+laneTxId+"  violXferId: "+violXferId +
					"ex2: "+ ex2.getMessage() + " " + ex2);
		}
		
		extLog.info("exiting QfreeResponseAckDaoImpl.updateQfreeRereviewTxStatus() laneTxId: "+laneTxId+"  violXferId: "+violXferId);

		return dbupdateSuccess;
	}

	//@Transactional(rollbackFor = Exception.class)
	@Transactional(propagation = Propagation.NESTED, rollbackFor = SQLException.class)
	public boolean updateTLprQueue(PassageAcknowledgementType acknowledgement, TviolXferStatistics violXferStatistics, QFreeBatchDto batchDto, 
		QFreeRequestVO qFreeRequestVO, MultiLogger extLog) throws VectorImageReviewException {
		
		extLog.info("entering QfreeResponseAckDaoImpl.updateTLprQueue()");		
		
		boolean dbUpdateSuccess = false;
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		try {
			String query = LoadJpaQueries.getQueryById("Update_T_LPR_QUEUE_WITHOUT_MIR"); // doubt in columns		
			
			if (Constants.ACKNOWLEDGEMENT_STATUS_OK.equalsIgnoreCase(acknowledgement.getValue().value())
					|| Constants.ACKNOWLEDGEMENT_STATUS_REJECT.equalsIgnoreCase(acknowledgement.getValue().value()))
				qFreeRequestVO.setLprStatus(Constants.ENQUEUE_ACK_RECEIVED);
			else if (Constants.ACKNOWLEDGEMENT_STATUS_RETRY.equalsIgnoreCase(acknowledgement.getValue().value()))
				qFreeRequestVO.setLprStatus(Constants.TO_BE_ENQUEUED);

			paramSource.addValue(Constants.LPR_STATUS, 		qFreeRequestVO.getLprStatus(), 		Types.VARCHAR);
			paramSource.addValue(Constants.ACK_STATUS, 		acknowledgement.getValue().value(), Types.VARCHAR);
			paramSource.addValue(Constants.ISS_PASSAGE_ID, 	acknowledgement.getIssPassageID(), 	Types.VARCHAR);
			paramSource.addValue(Constants.LPR_TYPE, 		batchDto.getLprType(), 				Types.VARCHAR);

			if (Constants.ACKNOWLEDGEMENT_STATUS_OK.equalsIgnoreCase(acknowledgement.getValue().value())		//missing parentessis
					&&  (batchDto.getLprType().equalsIgnoreCase(Constants.QFREE_NORMAL)
					|| batchDto.getLprType().equalsIgnoreCase(Constants.QFREE_DISPUTE)
					|| batchDto.getLprType().equalsIgnoreCase(Constants.QFREE_LPR_TYPE_MIR_ONLY)) ) 
			{
				paramSource.addValue(Constants.OUTPUT_FILE_TYPE, Constants.FILE_TYPE_TR, 				Types.VARCHAR);
				paramSource.addValue(Constants.OUTPUT_FILE_ID, violXferStatistics.getViolXferFileId(), 	Types.INTEGER);
			} else {
				paramSource.addValue(Constants.OUTPUT_FILE_TYPE, "", Types.VARCHAR);
				paramSource.addValue(Constants.OUTPUT_FILE_ID, 0, 	Types.INTEGER);
			}

			paramSource.addValue(Constants.IMAGE_TX_ID, qFreeRequestVO.getImageTxId(),	Types.NUMERIC);

			if (qFreeRequestVO.getUnmatchedEntryFlag() != null && qFreeRequestVO.getUnmatchedEntryFlag().equalsIgnoreCase("T"))
				paramSource.addValue(Constants.UNMATCHED_ENTRY_FLAG, "Y", Types.CHAR);
			else if (qFreeRequestVO.getUnmatchedEntryFlag() != null && qFreeRequestVO.getUnmatchedEntryFlag().equalsIgnoreCase("F"))
				paramSource.addValue(Constants.UNMATCHED_ENTRY_FLAG, "N", Types.CHAR);
			else
				paramSource.addValue(Constants.UNMATCHED_ENTRY_FLAG, qFreeRequestVO.getUnmatchedEntryFlag(), 	Types.CHAR);

			paramSource.addValue(Constants.ACTUAL_CLASS, 			qFreeRequestVO.getActualClass(),			Types.INTEGER);
			paramSource.addValue(Constants.VEHICLE_TYPE_REQUIRE, 	qFreeRequestVO.getVehicleTypeRequired(), 	Types.VARCHAR);
			paramSource.addValue(Constants.LANE_TX_ID, 				acknowledgement.getHostPassageID(),			Types.NUMERIC);

			extLog.info("in QfreeResponseAckDaoImpl.updateTLprQueue() query: "+query);
			extLog.info("in QfreeResponseAckDaoImpl.updateTLprQueue() paramSource: "+ paramSource);
			
			int tQueueUpdateCnt = namedJdbcTemplate.update(query, paramSource);
			
			extLog.info("in QfreeResponseAckDaoImpl.updateTLprQueue() lprStatus= "+qFreeRequestVO.getLprStatus()+ "  ackStatus: " +acknowledgement.getValue().value()+
				"  laneTxId: " + acknowledgement.getHostPassageID() + "  issPassageId: "+acknowledgement.getIssPassageID() + " tQueueUpdateCnt: "+ tQueueUpdateCnt);			

			if (tQueueUpdateCnt > 0)
				dbUpdateSuccess = true;

		} catch (Exception ex2) {
			extLog.info("Error in QfreeResponseAckDaoImpl.updateTLprQueue() lprStatus= "+qFreeRequestVO.getLprStatus()+ "  ackStatus: " +acknowledgement.getValue().value()+
					"  laneTxId: " + acknowledgement.getHostPassageID() + "  issPassageId: "+acknowledgement.getIssPassageID()+ "  ex2: "+ ex2.getMessage());	
				
			throw new VectorImageReviewException(
					"Error in QfreeResponseAckDaoImpl.updateTLprQueue() lprStatus= "+qFreeRequestVO.getLprStatus()+ "  ackStatus: " +acknowledgement.getValue().value()+
					"  laneTxId: " + acknowledgement.getHostPassageID() + "  issPassageId: "+acknowledgement.getIssPassageID()+ "  ex2: "+ ex2.getMessage());
		}
		
		extLog.info("exiting QfreeResponseAckDaoImpl.updateTLprQueue() laneTxId: " + acknowledgement.getHostPassageID() + "  issPassageId: "+acknowledgement.getIssPassageID() + " dbUpdateSuccess: "+ dbUpdateSuccess);

		return dbUpdateSuccess;
	}
}
