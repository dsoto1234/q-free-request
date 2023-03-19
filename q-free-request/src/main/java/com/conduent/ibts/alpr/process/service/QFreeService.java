package com.conduent.ibts.alpr.process.service;

import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;

public interface QFreeService {

	void getTransactionsToEnqueue(QFreeBatchDto batchDto);
	
}

