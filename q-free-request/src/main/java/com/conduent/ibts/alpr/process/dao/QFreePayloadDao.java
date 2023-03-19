package com.conduent.ibts.alpr.process.dao;

import java.util.List;

import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

public interface QFreePayloadDao {
	
	public List<QFreeRequestVO> getTransactionsForNormalAndAir(QFreeBatchDto batchDto);
	
	public List<QFreeRequestVO> getTransactionsForMir(QFreeBatchDto batchDto);
	
	public List<QFreeRequestVO> getTransactionsForDispute(QFreeBatchDto batchDto);

}
