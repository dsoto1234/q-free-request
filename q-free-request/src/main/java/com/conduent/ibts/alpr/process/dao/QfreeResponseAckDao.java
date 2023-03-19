package com.conduent.ibts.alpr.process.dao;

import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.dto.TviolXferStatistics;
import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

import cloud.intrada.passage.PassageAcknowledgementType;

public interface QfreeResponseAckDao {


	public boolean updateQueueAndInsertInfoTables(PassageAcknowledgementType acknowledgement, String ackFromQfree,
			String ackToNy, int numberOfAttemps, QFreeRequestVO qFreeRequestVO, TviolXferStatistics violXferStatistics,
			QFreeBatchDto batchDto) throws VectorImageReviewException;
}
