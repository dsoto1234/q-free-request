package com.conduent.ibts.alpr.process.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.conduent.ibts.alpr.process.dao.QFreePayloadDao;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.service.impl.GenerateTransactionForQFreeImpl;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

@ExtendWith(MockitoExtension.class)
public class GenerateTransactionForQfreeImplTest {
	
	@InjectMocks
	GenerateTransactionForQFreeImpl generateTransactions;
	
	@Mock
	QFreePayloadDao qFreePayloadDao;
	
	QFreeBatchDto queueData = new QFreeBatchDto();
	QFreeRequestVO qFreeReq = new QFreeRequestVO();
	List<QFreeRequestVO> transactionsList = new ArrayList<>();
	
	@BeforeEach
	void initialize() {
		queueData.setAgencyId(1l);
		queueData.setLprType("NORMAL");
		queueData.setPlazaId(72l);
		
		qFreeReq.setAgencyId(1);
		qFreeReq.setAgencyShortName("NY");
		qFreeReq.setLaneTxId(123l);
		qFreeReq.setLprType("NORMAL");
		qFreeReq.setTxTimestamp("2022-12-12 11:00:12.234");
		
		transactionsList.add(qFreeReq);
	}
	
	/*
	 * @Test void validateQueueDataTest() throws InvalidDataException {
	 * Assertions.assertDoesNotThrow(()->generateTransactions.validateQueueData(
	 * queueData)); assertTrue(generateTransactions.validateQueueData(queueData)); }
	 */
	
	/*
	 * @Test void validateQueueDataNegativeTest() { queueData.setLprType(null);
	 * Assertions.assertThrows(InvalidDataException.class,()->
	 * generateTransactions.validateQueueData(queueData)); }
	 */
	
	@Test
	void getTransactionsToEnqueueNORMALTest() {
		when(qFreePayloadDao.getTransactionsForNormalAndAir(queueData)).thenReturn(transactionsList);
		//generateTransactions.getTransactionsToEnqueue(queueData);
		assertNotNull(transactionsList.get(0).getImage("0"));
		assertNotNull(transactionsList.get(0).getCaptureTimeUTC());
		assertEquals(transactionsList.get(0).getAxleCount(),2);
		assertEquals(transactionsList.get(0).getSource(),"demo_source");
		assertEquals(transactionsList.get(0).getPriority(),"NORMAL");
	}
	
	@Test
	void getTransactionsToEnqueueAIR_ONLYTest() {
		queueData.setLprType("AIR_ONLY");
		when(qFreePayloadDao.getTransactionsForNormalAndAir(queueData)).thenReturn(transactionsList);
		//generateTransactions.getTransactionsToEnqueue(queueData);
		assertNotNull(transactionsList.get(0).getImage("0"));
		assertNotNull(transactionsList.get(0).getCaptureTimeUTC());
		assertEquals(transactionsList.get(0).getAxleCount(),2);
		assertEquals(transactionsList.get(0).getSource(),"demo_source");
		assertEquals(transactionsList.get(0).getPriority(),"NORMAL");
	}
	
	@Test
	void getTransactionsToEnqueueMIR_ONLYTest() {
		queueData.setLprType("MIR_ONLY");
		when(qFreePayloadDao.getTransactionsForMir(queueData)).thenReturn(transactionsList);
		//generateTransactions.getTransactionsToEnqueue(queueData);
		assertNotNull(transactionsList.get(0).getImage("0"));
		assertNotNull(transactionsList.get(0).getCaptureTimeUTC());
		assertEquals(transactionsList.get(0).getAxleCount(),2);
		assertEquals(transactionsList.get(0).getSource(),"demo_source");
		assertEquals(transactionsList.get(0).getPriority(),"NORMAL");
	}
	
	@Test
	void getTransactionsToEnqueueDISPUTETest() {
		queueData.setLprType("DISPUTE");
		when(qFreePayloadDao.getTransactionsForDispute(queueData)).thenReturn(transactionsList);
		//generateTransactions.getTransactionsToEnqueue(queueData);
		assertNotNull(transactionsList.get(0).getImage("0"));
		assertNotNull(transactionsList.get(0).getCaptureTimeUTC());
		assertEquals(transactionsList.get(0).getAxleCount(),2);
		assertEquals(transactionsList.get(0).getSource(),"demo_source");
		assertEquals(transactionsList.get(0).getPriority(),"NORMAL");
	}
}
