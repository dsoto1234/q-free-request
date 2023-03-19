package com.conduent.ibts.alpr.process.service.impl;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dao.impl.QfreeResponseAckDaoImpl;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.dto.TviolXferStatistics;
import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;
import com.conduent.ibts.alpr.process.service.ProcessReceivedAck;
import com.conduent.ibts.alpr.process.utility.EnqueueRetryStrategy;
import com.conduent.ibts.alpr.process.utility.MasterCache;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLogger;
import com.conduent.ibts.alpr.process.vo.QFreeRequestVO;
import com.conduent.ibts.alpr.process.vo.QFreeResponseAcknowledgementVO;

import cloud.intrada.common.StatusEnumeration;
import cloud.intrada.passage.PassageAcknowledgementType;
import cloud.intrada.passage.PassageType;
import cloud.intrada.passage.service.IPassageService;
import cloud.intrada.passage.service.PassageService;
import jakarta.xml.ws.BindingProvider;

@Service
//public class ProcessReceivedAckImpl implements ProcessReceivedAck {
public class ProcessReceivedAckImpl {

	Logger logger = LoggerFactory.getLogger(ProcessReceivedAckImpl.class);

	private final static String THIS_CLASS_NAME = "ProcessReceivedAckImpl";
	private static String strMethodName = "";

	@Autowired
	QfreeResponseAckDaoImpl responseAckDao;

	@Autowired
	EnqueueRetryStrategy retryStrategy;
	
	@Autowired
	MasterCache masterCache;

	private IPassageService passageService;
	private final static String USERNAME = "daniel.soto@conduent.com";
	private final static String PASSWORD = "Asldkj298fgyalkjxcbn$6892398zbhlalks$48298";
	
	public ProcessReceivedAckImpl() {

		logger.info("******* in ProcessReceivedAckImpl() constructor *********");
		try {
			
			BindingProvider provider = (BindingProvider) new PassageService().getWSHttpBindingIPassageService();
			provider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, USERNAME);
			provider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, PASSWORD);

			this.passageService = (IPassageService) provider;

			if (this.passageService == null)
				logger.info("passageService is null");
			else
				logger.info("passageService is not null");

		} catch (RuntimeException e2) {
			e2.printStackTrace();
			logger.info(new File("").getAbsolutePath());
			logger.info("error e2: " + e2.getMessage());
		} catch (Throwable e4) {
			logger.info(new File("").getAbsolutePath());
			logger.info("error e4: " + e4.getMessage());
		}
		logger.info("************ out EnqueuePassageClient() constructor ***************");
	}

	//@Override
	public boolean sentToQfree(List<PassageType> qFreeRequest, QFreeBatchDto batchDto, List<QFreeRequestVO> transactionsList, MultiLogger extLog) throws VectorImageReviewException  {
		
		extLog.info("********************************************* Send Txnx to QFree ***********************************************************\n");
		extLog.info("entering ProcessReceivedAckImpl.sentToQfree() batchDto: "+batchDto + " qFreeRequest.size(): "+qFreeRequest.size()+
			" transactionsList.size(): "+transactionsList.size()+ " threadName: "+Thread.currentThread().getName());

		boolean keepProcessing = true;

		try {

			List<QFreeRequestVO> txnsToBeEnqueue = new CopyOnWriteArrayList<>(transactionsList);
			List<PassageType> toBeEnqueuedPassages = new CopyOnWriteArrayList<>(qFreeRequest);

			extLog.info("in ProcessReceivedAckImpl.sentToQfree() toBeEnqueuedPassages.size(): "+ toBeEnqueuedPassages.size());

			synchronized (toBeEnqueuedPassages) {
				while (toBeEnqueuedPassages.size() > 0) {
					extLog.info("in ProcessReceivedAckImpl.sentToQfree() number of passages to enqueue to QFREE: "+ toBeEnqueuedPassages.size());

					List<PassageAcknowledgementType> acknowledgementsReceived = passageService.enqueuePassages(toBeEnqueuedPassages);
					extLog.info("************** in ProcessReceivedAckImpl.sentToQfree() getting acknowledgement from QFREE acknowledgements.size(): "+ acknowledgementsReceived.size() +" **************\n");
					
					toBeEnqueuedPassages = new CopyOnWriteArrayList<>();
					if (null != acknowledgementsReceived && !acknowledgementsReceived.isEmpty()) {						
						toBeEnqueuedPassages = processAcknowledment(qFreeRequest, acknowledgementsReceived, toBeEnqueuedPassages, txnsToBeEnqueue, batchDto, extLog);
					}
				}
			}
		} catch (Exception e) {
			keepProcessing = false;
			extLog.error("Error in ProcessReceivedAckImpl.sentToQfree() e: " + e);
			throw new VectorImageReviewException(">>>> error in ProcessReceivedAckImpl.sentToQfree() batchDto: "+batchDto+"  e: " + e);
		}
		
		extLog.info("exiting ProcessReceivedAckImpl.sentToQfree() batchDto: " + batchDto+"\n");
		return keepProcessing;
	}

	//@Override
	public List<PassageType> processAcknowledment(List<PassageType> qFreeRequest, List<PassageAcknowledgementType> ackList,
		List<PassageType> toBeEnqueuedPassages, List<QFreeRequestVO> txnsToBeEnqueue, QFreeBatchDto batchDto, MultiLogger extLog) //throws VectorImageReviewException
	{
		extLog.info("entering ProcessReceivedAckImpl.processAcknowledment() batchDto: "+batchDto+" qFreeRequest.size(): "+qFreeRequest.size()+
			" ackList.size(): "+ackList.size()+"  toBeEnqueuedPassages.size(): "+toBeEnqueuedPassages.size()+ "  txnsToBeEnqueue.size(): "+
			txnsToBeEnqueue.size() +" threadName: "+Thread.currentThread().getName());
		
		QFreeRequestVO qFreeRequestVO = null;
		QFreeResponseAcknowledgementVO acknowledgementVO = null;
		TviolXferStatistics violXferStatistics = new TviolXferStatistics();
		boolean dbUpdate = true;
		List<QFreeResponseAcknowledgementVO> acknowledgementVOList = new CopyOnWriteArrayList<>();

		for (PassageAcknowledgementType acknowledgement : ackList) {
			try 
			{
				qFreeRequestVO = txnsToBeEnqueue.stream()
					.filter(p -> String.valueOf(p.getLaneTxId()).equals(acknowledgement.getHostPassageID())).findFirst().get();

				extLog.info("in ProcessReceivedAckImpl.processAcknowledment() ##### qFreeRequestVO: " + qFreeRequestVO + " #####");
				extLog.info("<<< in ProcessReceivedAckImpl.processAcknowledment() violXferStatistics.getViolXferFileId(): "+violXferStatistics.getViolXferFileId()+" >>> \n");

				switch (acknowledgement.getValue()) {

				case OK:
					// When the status is "OK", the passage has successfully been enqueued. You
					// should save a mapping
					// between the IssPassageID and HostPassageID here.		
				
					try {				
						dbUpdate = responseAckDao.updateQueueAndInsertInfoTables(acknowledgement, Constants.ACK_FROM_QFREE,
								Constants.ACK_TO_NY, retryStrategy.getNumberOfAttemps(), qFreeRequestVO, violXferStatistics, batchDto, extLog);
					} catch(Exception e) {
						extLog.error(
								"\n Exception in QfreeResponseAckDao.updateQueueAndInsertInfoTables() while updating laneTxId: "
										+ acknowledgement.getHostPassageID() + "  e: " + e + "\n");
	
						responseAckDao.updateQFreeQueueTableWithDBError(acknowledgement.getHostPassageID(),
								e.getMessage(), extLog);
					}
					
					
					extLog.info("in ProcessReceivedAckImpl.processAcknowledment() Successfully enqueued passage with LaneIxId: "+acknowledgement.getHostPassageID()+
							 " and PassageID: " + acknowledgement.getIssPassageID()+"  description:" + acknowledgement.getDescription() + " dbUpdate:"+dbUpdate+"\n");
					
					break;

				case REJECT:
					// The status is "REJECT": This usually means that the data is invalid. You
					// should do error handling here.
					// See acknowledgement.getDescription() for more detailed information on the
					// error.
					// NOTE: The IssPassageID is not available at this point.
					
					try {
					
						dbUpdate = responseAckDao.updateQueueAndInsertInfoTables(acknowledgement, Constants.ACK_FROM_QFREE,
								Constants.ACK_TO_NY, retryStrategy.getNumberOfAttemps(), qFreeRequestVO, violXferStatistics, batchDto, extLog);
					}	
					catch (VectorImageReviewException ex) 
					{				
						extLog.error(
								"\n Exception in QfreeResponseAckDao.updateQueueAndInsertInfoTables() while updating laneTxId: "
										+ acknowledgement.getHostPassageID() + "  e: " + ex + "\n");
	
						responseAckDao.updateQFreeQueueTableWithDBError(acknowledgement.getHostPassageID(),
								ex.getMessage(), extLog);
					}
				
					extLog.info("in ProcessReceivedAckImpl.processAcknowledment() Could not enqueue passage with HostID: "+acknowledgement.getHostPassageID()+
						" issPassageId: "+acknowledgement.getIssPassageID()+"  description:"+acknowledgement.getDescription()+" dbUpdate:"+dbUpdate+"\n");								
					
					break;

				case RETRY:
					// A retry status means that the data was valid, but that Intrada Synergy Server
					// could not process the data right
					// now. As such, we should try to resend the data. NOTE: The IssPassageID is not
					// available at this point.
					// logger.info("Scheduling passage with laneTxId:
					// "+acknowledgement.getHostPassageID() + " for retry\n");
					// ISS server could not save passage into their system so retry
					extLog.info("in ProcessReceivedAckImpl.processAcknowledment() RETRY HostID:{}, issPassageId:{}, acknowledgement.getDescription(): {}",
							acknowledgement.getHostPassageID(), acknowledgement.getIssPassageID(), acknowledgement.getDescription()+"\n");
	
					toBeEnqueuedPassages.add(qFreeRequest.stream()
							.filter(p -> p.getHostPassageID().equals(acknowledgement.getHostPassageID())).findFirst().get());
	
					// this condition is so we can capture the ack description since passages do not
					// have description
					if (acknowledgement.getDescription() != null && acknowledgement.getDescription().length() > 0) {
						acknowledgementVO = new QFreeResponseAcknowledgementVO();
						acknowledgementVO.setHostPassageId(acknowledgement.getHostPassageID());
						acknowledgementVO.setDescription(acknowledgement.getDescription());
						acknowledgementVOList.add(acknowledgementVO);
					}
	
					break;

					default:
					extLog.info("in ProcessReceivedAckImpl.processAcknowledment() invalid Ack value status HostID: " + acknowledgement.getHostPassageID()+						
						" issPassageId: " + acknowledgement.getIssPassageID()+ " status: "+acknowledgement.getValue().value()+
						" acknowledgement.getDescription(): " + acknowledgement.getDescription());						 
					break;
				}
			}
			catch(VectorImageReviewException e) {
				logger.error("Error in ProcessReceivedAckImpl.processAcknowledment() while processing acknowledgement from Q-Free for laneTxId: " + acknowledgement.getHostPassageID()+
					" and issPassageId: " + acknowledgement.getIssPassageID() + "  e.getMessage(): " +e.getMessage());
			}catch(Exception e1 ){
				logger.error("Error in ProcessReceivedAckImpl.processAcknowledment() while processing acknowledgement from Q-Free for laneTxId: " + acknowledgement.getHostPassageID()+
						"and issPassageId: " + acknowledgement.getIssPassageID() + "  e1.getMessage(): " +e1.getMessage());
			}			
		}

		// this condition is only for when ACK is RETRY ... only when is RETRY
		// toBeEnqueuedPassages.size() > 0
		if (toBeEnqueuedPassages != null && toBeEnqueuedPassages.size() > 0) {
			// If we scheduled passages for a retry, sleep and try to send them again
			extLog.info("in ProcessReceivedAckImpl.processAcknowledment(): Trying to enqueue passages with RETRY status.");

			try {
				retryStrategy.retryOccured();

			} catch (Exception e) {
				extLog.error("Error in ProcessReceivedAckImpl.processAcknowledment(): while enqueuing passages to Q-Free.  Keep trying to process acknowledgement with RETRY status e.getMessage(): "+e.getMessage());

				if (acknowledgementVOList != null) {
					for (QFreeResponseAcknowledgementVO ackVO : acknowledgementVOList) {
						PassageAcknowledgementType ack = new PassageAcknowledgementType();

						ack.setHostPassageID(ackVO.getHostPassageId());
						ack.setValue(StatusEnumeration.RETRY);
						ack.setDescription(ackVO.getDescription());

						try {
							dbUpdate = responseAckDao.updateQueueAndInsertInfoTables(ack, Constants.ACK_FROM_QFREE,
								Constants.ACK_TO_NY, retryStrategy.getNumberOfAttemps(), qFreeRequestVO, violXferStatistics, batchDto, extLog);
						} catch (VectorImageReviewException e1) 
						{
							extLog.error("Error in ProcessReceivedAckImpl.processAcknowledment(): While updating db for retry with issPassageId: "+ack.getIssPassageID()+
								" e.getMessage(): "+e.getMessage());
							
							try {
								responseAckDao.updateQFreeQueueTableWithDBError(ack.getHostPassageID(), e1.getMessage(),
										extLog);
							} catch (VectorImageReviewException e2) {
								extLog.error(
										"\n Exception in QfreeResponseAckDao.updateQueueAndInsertInfoTables() while updating laneTxId: "
												+ ack.getHostPassageID() + "  e2: " + e2 + "\n");
							}
							
							
						}catch (Exception e1) 
						{
							extLog.error("Error in ProcessReceivedAckImpl.processAcknowledment(): While updating db for retry with issPassageId: "+ack.getIssPassageID()+
								" e.getMessage(): " + e.getMessage());
						}
						extLog.info("in ProcessReceivedAckImpl.processAcknowledment() DB update done for ackStatus = RETRY: " + dbUpdate);
					}
				}
			}
		}

		extLog.info("exiting ProcessReceivedAckImpl.processAcknowledment() batchDto: " + batchDto+"\n");
		return toBeEnqueuedPassages;
	}

}
