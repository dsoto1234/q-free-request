package com.conduent.ibts.alpr.process.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conduent.ibts.alpr.process.dao.impl.QFreeDaoImpl;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.exception.StopExecutionException;
import com.conduent.ibts.alpr.process.service.QFreeService;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLogger;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;

import cloud.intrada.passage.PassageType;

@Service
public class QFreeServiceImpl {

//public class QFreeServiceImpl implements QFreeService {

	private static final String THIS_CLASS_NAME = "getTransactionsToEnqueue";
	private Logger logger = LoggerFactory.getLogger(QFreeServiceImpl.class);

	@Autowired
	GenerateTransactionForQFreeImpl generateTransactions;
	@Autowired
	BuildTransactionForQFreeImpl buildTransactions;
	@Autowired
	ProcessReceivedAckImpl sendToQFree;
	@Autowired
	QFreeDaoImpl qFreeDao;

	//@Override
	public void getTransactionsToEnqueue(QFreeBatchDto batchDto, MultiLogger extLog) {
		
		extLog.info("entering QFreeServiceImpl.getTransactionsToEnqueue() batchDto: "+batchDto+ " threadName: "+Thread.currentThread().getName()); 

		boolean keepPushingTxnsToQFree = true;
		List<QFreeRequestVO> transactionsList = null;
		List<PassageType> qFreePassageList = null;
		
		try {
			do {
				extLog.info("******************************************* Fetching Transactions ***************************************************************\n");				
				transactionsList = generateTransactions.getTransactionsToEnqueue(batchDto, extLog);
				
				extLog.info("in QFreeServiceImpl.getTransactionsToEnqueue() transactionsList.size(): "+transactionsList.size() +
					" totalThreads: "+batchDto.getTotalThreads()+" threadIndex: "+batchDto.getThreadIndex()+" runId: "+batchDto.getRunId());										
				
				if (null != transactionsList && !transactionsList.isEmpty()) 
				{
					qFreePassageList = buildTransactions.buildPassages(transactionsList, batchDto, extLog);
									
					if (null != qFreePassageList && !qFreePassageList.isEmpty()) 
					{
						keepPushingTxnsToQFree = sendToQFree.sentToQfree(qFreePassageList, batchDto, transactionsList, extLog);
	
						if (keepPushingTxnsToQFree == false) 
							throw new StopExecutionException("in QFreeServiceImpl.getTransactionsToEnqueue() Stop execution");
					}
				} else 
				{
					extLog.info("<<<<<<<<<<<<<<<<<< in QFreeServiceImpl.getTransactionsToEnqueue()  no transactions to enqueue  >>>>>>>>>>>>>>>>>>\n");
					if (extLog != null && extLog.getVectorFileHandler() != null) {
						extLog.closeFileHandler();
					}
					break;
				}
			}while(true);

			
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Error in QFreeServiceImpl.getTransactionsToEnqueue() ex.getMessage: "+ex.getMessage());
		}
		
		logger.info("exiting QFreeServiceImpl.getTransactionsToEnqueue() batchDto: " + batchDto);		 
	}

}
