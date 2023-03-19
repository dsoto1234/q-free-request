package com.conduent.ibts.alpr.process.service;

import java.util.List;

import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;
import com.conduent.ibts.alpr.process.vectorlogger.CreateMultiLogger;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

import cloud.intrada.passage.PassageType;

public interface BuildTransactionForQFree {

	public List<PassageType> buildPassages(List<QFreeRequestVO> pssgs, QFreeBatchDto batchDto, CreateMultiLogger extLog) throws VectorImageReviewException ;
		
}
