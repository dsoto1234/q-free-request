package com.conduent.ibts.alpr.process.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.conduent.ibts.alpr.process.dao.impl.QFreeDaoImpl;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.dto.QFreeQueueResponseDto;
import com.conduent.ibts.alpr.process.exception.InvalidDataException;
import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;
import com.conduent.ibts.alpr.process.service.BuildTransactionForQFree;
import com.conduent.ibts.alpr.process.service.GenerateTransactionForQFree;
import com.conduent.ibts.alpr.process.service.ProcessReceivedAck;
import com.conduent.ibts.alpr.process.service.impl.QFreeServiceImpl;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

import cloud.intrada.common.WorkflowEnumeration;
import cloud.intrada.passage.PassageType;

@ExtendWith(MockitoExtension.class)
public class QFreeServiceImplTest {

	@InjectMocks
	QFreeServiceImpl qFreeServiceImpl;
	
	
	
	@Mock
	GenerateTransactionForQFree generateTransactions;
	@Mock
	BuildTransactionForQFree buildTransactions;
	@Mock
	ProcessReceivedAck sendToQFree;
	@Mock
	QFreeDaoImpl qFreeDao;
	
	QFreeBatchDto queueData = new QFreeBatchDto();
	QFreeRequestVO qFreeReq = new QFreeRequestVO();
	PassageType passageType = new PassageType();
	List<QFreeRequestVO> transactionsList = new ArrayList<>();
	List<PassageType> qFreePassageList = new ArrayList<>();
	List<QFreeQueueResponseDto> txn = new ArrayList<>();
	
	@BeforeEach
	void initialize() {
		queueData.setAgencyId(1l);
		queueData.setLprType("NORMAL");
		queueData.setPlazaId(72l);
		
		qFreeReq.setAgencyId(1);
		qFreeReq.setAgencyShortName("NY");
		qFreeReq.setLaneTxId(123l);
		qFreeReq.setLprType("NORMAL");
		
		passageType.setSource("DEMO");
		passageType.setWorkflow(WorkflowEnumeration.NORMAL);
		
		transactionsList.add(qFreeReq);
		qFreePassageList.add(passageType);
		//txn.add(queueData);
		
	}
	
	/*
	@Test
	void getTransactionsToEnqueueTest() throws InvalidDataException, VectorImageReviewException {
		//when(qFreeDao.getAllTransactionFromLprTable(any())).thenReturn(txn);
		when(generateTransactions.getTransactionsToEnqueue(any())).thenReturn(transactionsList);
		when(buildTransactions.buildPassages(any(), any())).thenReturn(qFreePassageList);
		when(sendToQFree.sentToQfree(any(),any(),any())).thenReturn(true);
		//Assertions.assertDoesNotThrow(()-> qFreeServiceImpl.getTransactionsToEnqueue(queueData));
		
	}
	*/
	
	/*
	@Test
	void getTransactionsToEnqueueEmptyTransactionListTest() throws InvalidDataException, VectorImageReviewException {
		transactionsList.removeAll(transactionsList);
		//when(qFreeDao.getAllTransactionFromLprTable(any())).thenReturn(txn);
		when(generateTransactions.getTransactionsToEnqueue(any())).thenReturn(transactionsList);
		//qFreeServiceImpl.getTransactionsToEnqueue(queueData);
		assertEquals(transactionsList.size(),0);
		
	}
	
	@Test
	void getTransactionsToEnqueueNegativeTest() throws InvalidDataException, VectorImageReviewException {
		//when(qFreeDao.getAllTransactionFromLprTable(any())).thenReturn(txn);
		when(generateTransactions.getTransactionsToEnqueue(any())).thenReturn(transactionsList);
		when(buildTransactions.buildPassages(any(), any())).thenReturn(qFreePassageList);
		when(sendToQFree.sentToQfree(any(),any(),any())).thenReturn(false);
		//qFreeServiceImpl.getTransactionsToEnqueue(queueData);
		
	}
	
	*/
	
	
}
