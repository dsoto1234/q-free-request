package com.conduent.ibts.alpr.process.dao;

import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.dto.QFreeQueueResponseDto;
import com.conduent.ibts.alpr.process.dto.TviolXferStatistics;
import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

import cloud.intrada.passage.PassageAcknowledgementType;

public interface StatisticsUpdateDao {

	boolean updateTViolXferStatistics(PassageAcknowledgementType acknowldegement, String fileType, 
			QFreeRequestVO passageTypeVO, TviolXferStatistics violXferStatistics,QFreeBatchDto batchDto) throws VectorImageReviewException;

}
