package com.conduent.ibts.alpr.process.service;

import java.util.List;

import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.vectorlogger.CreateMultiLogger;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

public interface GenerateTransactionForQFree {

	List<QFreeRequestVO> getTransactionsToEnqueue(QFreeBatchDto batchDto, CreateMultiLogger extLog);
		
}
