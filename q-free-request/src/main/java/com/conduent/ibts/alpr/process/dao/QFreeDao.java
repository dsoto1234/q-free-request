package com.conduent.ibts.alpr.process.dao;

import java.util.List;

import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.dto.QFreeQueueDto;
import com.conduent.ibts.alpr.process.dto.QFreeQueueResponseDto;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLogger;

public interface QFreeDao {

	void updateExecutionStatusAndStartTime(Long runId, String executionStatus, MultiLogger extLog);
	
	void updateExecutionStatusAndEndTime(Long runId, String executionStatus, MultiLogger extLog);

	boolean updateLprStatus(QFreeQueueDto qFreeQueueDto, MultiLogger extLog);

	boolean checkLprStatus(String msgKey, String processStatus, MultiLogger extLog);

	int updateLprStatus(String msgKey, String processStatus, MultiLogger extLog);

	//List<QFreeQueueResponseDto> getAllTransactionFromLprTable(QFreeBatchDto batchDto);

	boolean checkSimilarRowInBatchTable(Long runId, Long agencyId, Long plazaId, String lprType, MultiLogger extLog);
	
	
}
