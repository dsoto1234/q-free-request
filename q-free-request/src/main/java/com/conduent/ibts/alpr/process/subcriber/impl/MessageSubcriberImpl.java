package com.conduent.ibts.alpr.process.subcriber.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.conduent.ibts.alpr.process.config.ConfigVariable;
import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dao.impl.QFreeDaoImpl;
import com.conduent.ibts.alpr.process.dto.QFreeQueueResponseDto;
import com.conduent.ibts.alpr.process.subcriber.MessageSubcriber;
import com.conduent.ibts.alpr.process.utility.CommanUtility;
import com.google.common.util.concurrent.Uninterruptibles;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.streaming.StreamAdminClient;
import com.oracle.bmc.streaming.StreamClient;
import com.oracle.bmc.streaming.model.CreateGroupCursorDetails;
import com.oracle.bmc.streaming.model.Group;
import com.oracle.bmc.streaming.model.Message;
import com.oracle.bmc.streaming.model.Stream;
import com.oracle.bmc.streaming.requests.ConsumerHeartbeatRequest;
import com.oracle.bmc.streaming.requests.CreateGroupCursorRequest;
import com.oracle.bmc.streaming.requests.GetGroupRequest;
import com.oracle.bmc.streaming.requests.GetMessagesRequest;
import com.oracle.bmc.streaming.requests.GetStreamRequest;
import com.oracle.bmc.streaming.responses.CreateGroupCursorResponse;
import com.oracle.bmc.streaming.responses.GetMessagesResponse;
import com.oracle.bmc.streaming.responses.GetStreamResponse;

//@Component
public class MessageSubcriberImpl implements MessageSubcriber {

	private static final Logger logger = LoggerFactory.getLogger(MessageSubcriberImpl.class);

	@Autowired
	QFreeDaoImpl qFreeDao;
	@Autowired
	ConfigVariable configVariable;
	@Autowired
	CommanUtility commanUtility;
	private StreamClient streamClient;
	private String cursor;

	private String node = null;

	private String streamId = null;
	
	public MessageSubcriberImpl() {
		logger.info("in MessageSubcriberImpl constructor");
	}
	

	@PostConstruct
	public void init() {
		try {
			logger.info("Initializing node/cusror for Q-Free process...");
			if (null == streamClient) {
				streamClient = getStreamClient(configVariable.getStreamId());
			}
			if (null == node) {
				node = "QFreeNode-" + commanUtility.getRandomNumber(10);
			} else {
				logger.info("Node name already available as {} : ", node);
			}
			cursor = getCursorByGroup(streamClient, configVariable.getStreamId(), "QFreeGroup", node);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("*************GOT IOException***********" + e.getMessage());
		} catch (com.oracle.bmc.model.BmcException e) {
			e.printStackTrace();
			logger.error("*************GOT com.oracle.bmc.model.BmcException***********" + e.getMessage());
			Uninterruptibles.sleepUninterruptibly(1, TimeUnit.MILLISECONDS);
			init();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("*************GOT Exception***********" + e.getMessage());
		}
	}

	public String getDetails() {
		logger.info("in MessageSubcriberImpl.getDetails()");
		
		GetGroupRequest getGrpRequest = GetGroupRequest.builder().groupName("QFreeGroup")
				.streamId(configVariable.getStreamId()).build();
		Group gp = streamClient.getGroup(getGrpRequest).getGroup();
		return "Group Name:" + gp.getGroupName() + "Partition Reservation:" + gp.getReservations();
	}

	public StreamClient getStreamClient(String streamId) throws IOException {

		logger.info("in MessageSubcriberImpl.getStreamClient()");
		
		File file = ResourceUtils.getFile(configVariable.getConfigfilepath());
		final String configurationFilePath = file.toString();

		final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath);
		final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
		final StreamAdminClient adminClient = StreamAdminClient.builder().build(provider);

		Stream stream = getStream(adminClient, streamId);
		return StreamClient.builder().stream(stream).build(provider);

	}

	private String getCursorByGroup(StreamClient streamClient, String streamId, String groupName, String instanceName) {
		
		logger.info("in MessageSubcriberImpl.getCursorByGroup()");
		
		logger.info("Creating a cursor for group {} instance {}", groupName, instanceName);

		this.streamId = streamId;
		CreateGroupCursorDetails cursorDetails = CreateGroupCursorDetails.builder().groupName(groupName)
				.instanceName(instanceName).type(CreateGroupCursorDetails.Type.TrimHorizon).commitOnGet(true).build();

		CreateGroupCursorRequest createCursorRequest = CreateGroupCursorRequest.builder().streamId(streamId)
				.createGroupCursorDetails(cursorDetails).build();

		CreateGroupCursorResponse groupCursorResponse = streamClient.createGroupCursor(createCursorRequest);
		return groupCursorResponse.getCursor().getValue();
	}

	@Scheduled(fixedRate = 10000)
	public void heartBeat() {
		logger.info("in MessageSubcriberImpl.heartBeat()");
		
		try {
			ConsumerHeartbeatRequest consumerHeartbeatRequest = ConsumerHeartbeatRequest.builder().cursor(cursor)
					.streamId(streamId).build();
			streamClient.consumerHeartbeat(consumerHeartbeatRequest);
		} catch (Exception ex) {
			ex.printStackTrace();
			//init();
		}
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	private Stream getStream(StreamAdminClient adminClient, String streamId) {
		logger.info("in MessageSubcriberImpl.getStream()");
		
		GetStreamResponse getResponse = adminClient.getStream(GetStreamRequest.builder().streamId(streamId).build());
		return getResponse.getStream();
	}

	public List<QFreeQueueResponseDto> getMessage(int limit) throws IOException {
		logger.info("in MessageSubcriberImpl.getMessage() limit: "+limit);
		
		List<QFreeQueueResponseDto> queueMessages = null;

		try {
			queueMessages = new ArrayList<>();

			if (streamClient == null) {
				init();
			}

			GetMessagesRequest messagesRequest = GetMessagesRequest.builder().streamId(configVariable.getStreamId())
					.cursor(cursor).limit(limit).build();
			GetMessagesResponse getMessagesResponse = streamClient.getMessages(messagesRequest);

			if (getMessagesResponse != null) {
				Message msg = null;
				if (getMessagesResponse.getItems() != null && !getMessagesResponse.getItems().isEmpty()
						&& getMessagesResponse.getItems().size() > 0) {

					logger.info("Got total msgs: {}", getMessagesResponse.getItems().size());

					for (int i = 0; i < getMessagesResponse.getItems().size(); i++) {
						msg = getMessagesResponse.getItems().get(i);
						if (null != msg) {
							String messageKey = new String(msg.getKey());
							logger.info("Got messageKey from queue {} at index {}", messageKey, i);
							/*if (!qFreeDao.checkLprStatus(messageKey, Constants.ENQUEUED)) {
								logger.info("Record already PROCESSED or IN_PROGRESS with MSG_KEY: {}", messageKey);
							} else {

								if (qFreeDao.updateLprStatus(messageKey, Constants.IN_PROGRESS) > 0) {
									String message = new String(msg.getValue());
									logger.info("Got messageKey from queue {} at index {}", messageKey, i);
									cursor = getMessagesResponse.getOpcNextCursor();
									QFreeQueueResponseDto queueMsg = QFreeQueueResponseDto.dtoFromJson(message);
									logger.info("Subscriber queue dto {} at index {}", queueMsg, i);
									queueMessages.add(queueMsg);
								} else {
									logger.info("Failed to update LPR_STATUS  message {} at index {}", messageKey, i);

								}
							}*/

							// break;
						} else {
							logger.info("No/null Msg in Queue at index {}", i);

						}
					}
				} else {
					Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);

				}
				if (getMessagesResponse != null) {
					cursor = getMessagesResponse.getOpcNextCursor();
				}
			} else {
				logger.info("getMessagesResponse is NULL");
			}

		} catch (Exception ex) {
			logger.info("Exception: {}", ex.getMessage());
			init();
		}
		return queueMessages.isEmpty()?null:queueMessages;
	}
}