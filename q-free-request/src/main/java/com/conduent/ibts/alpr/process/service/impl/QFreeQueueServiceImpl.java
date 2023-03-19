package com.conduent.ibts.alpr.process.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.conduent.ibts.alpr.process.dao.impl.QFreeDaoImpl;
import com.conduent.ibts.alpr.process.dto.QFreeQueueDto;
import com.conduent.ibts.alpr.process.dto.QFreeQueueResponseDto;
import com.conduent.ibts.alpr.process.publisher.impl.MessagePublisherImpl;
import com.conduent.ibts.alpr.process.service.QFreeQueueService;
import com.conduent.ibts.alpr.process.service.QFreeService;
import com.conduent.ibts.alpr.process.subcriber.MessageSubcriber;
import com.conduent.ibts.alpr.process.utility.LocalDateAdapter;
import com.conduent.ibts.alpr.process.utility.LocalDateTimeAdapter;
import com.google.common.util.concurrent.Uninterruptibles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oracle.bmc.streaming.model.PutMessagesDetailsEntry;

//@Service
public class QFreeQueueServiceImpl implements QFreeQueueService {

	private static final Logger logger = LoggerFactory.getLogger(QFreeQueueServiceImpl.class);

	/*
	 * @Autowired private MessageSubcriber messageSubcriber;
	 * 
	 * @Autowired MessagePublisherImpl messagePublisher;
	 */

	@Autowired
	QFreeDaoImpl qFreeDaoImpl;
	@Autowired
	QFreeService qFreeService;
	@Value("${cacerts.location}")
	private String cacertsLocation;

	@Value("${cacerts.password}")
	private String cacertsPassword;

	@Override
	//@Scheduled(fixedRate=2000)
	public void getMessageFromQueue() throws IOException {
		/*
		 * List<QFreeQueueResponseDto> messageList = null; //setCacertProperty(); while
		 * (!Thread.currentThread().isInterrupted()) { try { messageList =
		 * messageSubcriber.getMessage(30); if (null != messageList &&
		 * !messageList.isEmpty()) { logger.info("QFree processing started !");
		 * messageList.forEach(msg -> { logger.info("Message to be processed:{}", msg);
		 * qFreeService.getTransactionsToEnqueue(msg); }); } else {
		 * logger.info("Message queue do not have message to process"); }
		 * 
		 * // sleepForWhile(); Uninterruptibles.sleepUninterruptibly(3,
		 * TimeUnit.SECONDS); } catch (Exception e) {
		 * logger.error("Exception while process lica {}", e.getMessage());
		 * e.printStackTrace(); } }
		 */

	}

	@Override
	public boolean sendTransactionstoQFreeQueue() {

		boolean isPublished = false;
		/*
		 * try {
		 * 
		 * Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new
		 * LocalDateAdapter()) .registerTypeAdapter(LocalDateTime.class, new
		 * LocalDateTimeAdapter()) .excludeFieldsWithoutExposeAnnotation().create();
		 * List<QFreeQueueDto> transactionList =
		 * qFreeDaoImpl.getAllTransactionForQFreeQueue();
		 * logger.info("dao response :{}", transactionList.size()); for (QFreeQueueDto
		 * transaction : transactionList) {
		 * 
		 * List<PutMessagesDetailsEntry> messages = new LinkedList<>();
		 * 
		 * byte[] msg = gson.toJson(transaction).getBytes(); // String number = "_" +
		 * UUID.randomUUID().toString(); // byte[] msgKey =
		 * transaction.toString().getBytes(); // String msgKeyByteValue =
		 * CommonUtility.getCRC32Checksum(msgKey);
		 * 
		 * messages.add(
		 * PutMessagesDetailsEntry.builder().key(transaction.getLaneTxId().getBytes()).
		 * value(msg).build());
		 * 
		 * if (qFreeDaoImpl.updateLprStatus(transaction)) {
		 * 
		 * logger.info("LPR_STATUS update successfully");
		 * 
		 * if ((messagePublisher.publishMessages(messages) > 0)) { isPublished = true;
		 * logger.info("Msg: {}", transaction); } else {
		 * logger.info("Failed to push into queue"); }
		 * 
		 * } else { logger.info("Failed to update LPR_STATUS"); }
		 * 
		 * } } catch (Exception ex) { logger.info("Exception: {}", ex.getMessage()); }
		 */
		return isPublished;

	}

	
	public void setCacertProperty() {
		logger.info("Adding cacert location :{}",cacertsLocation );
		System.setProperty("javax.net.ssl.trustStore", cacertsLocation);// cacerts location
		System.setProperty("javax.net.ssl.trustStorePassword", cacertsPassword);// changeit
		System.setProperty("javax.net.ssl.keyStore", cacertsLocation);// cacerts location
		System.setProperty("javax.net.ssl.keyStorePassword", cacertsPassword);// changeit
		logger.info("Added cacert successfully");
	}

	/*
	 * @Override public void run() {
	 * 
	 * List<QFreeQueueResponseDto> messageList = null; while
	 * (!Thread.currentThread().isInterrupted()) { try { messageList =
	 * messageSubcriber.getMessage(30); if (null != messageList &&
	 * !messageList.isEmpty()) { logger.info("QFree processing started !");
	 * messageList.forEach(msg -> { logger.info("Message to be processed:{}", msg);
	 * qFreeService.getTransactionsToEnqueue(msg); }); } else {
	 * logger.info("Message queue do not have message to process"); }
	 * 
	 * // sleepForWhile(); Uninterruptibles.sleepUninterruptibly(1,
	 * TimeUnit.SECONDS); } catch (Exception e) {
	 * logger.error("Exception while process lica {}", e.getMessage());
	 * e.printStackTrace(); } } }
	 */

}
