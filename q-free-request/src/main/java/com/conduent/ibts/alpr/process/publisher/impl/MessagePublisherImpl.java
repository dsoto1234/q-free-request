package com.conduent.ibts.alpr.process.publisher.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.conduent.ibts.alpr.process.config.ConfigVariable;
import com.conduent.ibts.alpr.process.publisher.MessagePublisher;
import com.google.common.util.concurrent.Uninterruptibles;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.streaming.StreamAdminClient;
import com.oracle.bmc.streaming.StreamClient;
import com.oracle.bmc.streaming.model.PutMessagesDetails;
import com.oracle.bmc.streaming.model.PutMessagesDetailsEntry;
import com.oracle.bmc.streaming.model.PutMessagesResultEntry;
import com.oracle.bmc.streaming.model.Stream;
import com.oracle.bmc.streaming.requests.GetStreamRequest;
import com.oracle.bmc.streaming.requests.PutMessagesRequest;
import com.oracle.bmc.streaming.responses.GetStreamResponse;
import com.oracle.bmc.streaming.responses.PutMessagesResponse;

//@Component
public class MessagePublisherImpl implements MessagePublisher{
	
	private static final Logger logger = LoggerFactory.getLogger(MessagePublisherImpl.class);

	@Autowired
	ConfigVariable configVariable;

	private StreamClient streamClient;
	
	public void getStreamclient(String streamId) throws IOException {

		File file = ResourceUtils.getFile(configVariable.getConfigfilepath());
		
		final String configurationFilePath = file.toString();
		
		final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath);
		final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);
		logger.info("stream configurationFilePath {}", configurationFilePath);
		final StreamAdminClient adminClient = StreamAdminClient.builder().build(provider);
		logger.info("stream id {}",streamId);
		Stream stream = getStream(adminClient, streamId);
		streamClient = StreamClient.builder().stream(stream).build(provider);
		
	}
	
	private Stream getStream(StreamAdminClient adminClient, String streamId) {
		GetStreamResponse getResponse = adminClient.getStream(GetStreamRequest.builder().streamId(streamId).build());
		return getResponse.getStream();
	}
	
	 public long publishMessages(List<PutMessagesDetailsEntry> messages) {
	        long msgCounter = 0;



	       try {
	            //getStreamclient(configVariable.getStreamId());
	            logger.info("Going to push message to OCI Stream for {}", messages.get(0).getKey());
	            LocalDateTime from;
	            LocalDateTime to;

	           String streamId = configVariable.getStreamId();

	            from = LocalDateTime.now();
	            if (streamClient == null) {
	                getStreamclient(streamId);
	            }
	            to = LocalDateTime.now();

	           logger.info("Stream Client - Completed in {} ms", ChronoUnit.MILLIS.between(from, to));



	           PutMessagesDetails messagesDetails = PutMessagesDetails.builder().messages(messages).build();
	            PutMessagesRequest putRequest = PutMessagesRequest.builder().streamId(streamId)
	                    .putMessagesDetails(messagesDetails).build();

	            from = LocalDateTime.now();
	            PutMessagesResponse putResponse = streamClient.putMessages(putRequest);
	            to = LocalDateTime.now();

	           logger.info("Put message - Completed in {} ms", ChronoUnit.MILLIS.between(from, to));



	           // the putResponse can contain some useful metadata for handling failures
	            for (PutMessagesResultEntry entry : putResponse.getPutMessagesResult().getEntries()) {
	                if (StringUtils.isNotBlank(entry.getError())) {



	                   logger.error("Message , Error {}: {}", entry.getError(), entry.getErrorMessage());
	                } else {
	                    msgCounter++;
	                    logger.info("Message  published to partition {}, offset {}, msgCounter {}", entry.getPartition(),
	                            entry.getOffset(), msgCounter);
	                    return msgCounter;
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            logger.error("Fail to publish: Error: {}", e.getMessage());
	            return msgCounter;
	        }
	        catch (com.oracle.bmc.model.BmcException e) {
	            e.printStackTrace();
	            logger.error("Fail to process messages: com.oracle.bmc.model.BmcException: {}", e.getMessage());
	            //return msgCounter;
	            Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);
	            logger.info("Retrying again for OCI publish for : {}", messages.get(0).getKey());
	            publishMessages(messages);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            logger.error("Fail to process messages: Error: {}", e.getMessage());
	            return msgCounter;
	        }



	       return msgCounter;



	   }
}
