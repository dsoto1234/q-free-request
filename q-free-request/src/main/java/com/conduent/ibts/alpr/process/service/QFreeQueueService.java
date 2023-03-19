package com.conduent.ibts.alpr.process.service;

import java.io.IOException;

public interface QFreeQueueService {

	public void getMessageFromQueue() throws IOException;
	
	public boolean sendTransactionstoQFreeQueue();
}
