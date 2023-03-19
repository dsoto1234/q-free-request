package com.conduent.ibts.alpr.process.service;

import java.util.List;

import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.dto.QFreeQueueResponseDto;
import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;
import com.conduent.ibts.alpr.process.vectorlogger.CreateMultiLogger;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

import cloud.intrada.passage.PassageAcknowledgementType;
import cloud.intrada.passage.PassageType;

public interface ProcessReceivedAck {

	public boolean sentToQfree(List<PassageType> qFreePassageList,
			QFreeBatchDto batchDto, List<QFreeRequestVO> qFreeRequestVO, CreateMultiLogger extLog) throws VectorImageReviewException;


	List<PassageType> processAcknowledment(List<PassageType> qFreeRequest, List<PassageAcknowledgementType> ackList,
			List<PassageType> toBeEnqueuedPassages, List<QFreeRequestVO> txnsToBeEnqueue,
			QFreeBatchDto batchDto,CreateMultiLogger extLog) throws VectorImageReviewException;
}
