package com.conduent.ibts.alpr.process.utility;

import org.springframework.stereotype.Component;

import com.conduent.ibts.alpr.process.constants.Constants;

@Component
public class EnqueueRetryStrategy {

	public static final int DEFAULT_RETRIES = Constants.RETRY_ATTEMPS;
	public static final long DEFAULT_WAIT_TIME_IN_MILLI = Constants.WAIT_TIME_IN_MILLISECONDS;

	private int numberOfRetries;
	private int numberOfTriesLeft;
	private long timeToWait;
	private int numberOfAttemps = 0;

	public EnqueueRetryStrategy() {
		this(DEFAULT_RETRIES, DEFAULT_WAIT_TIME_IN_MILLI);
	}

	public EnqueueRetryStrategy(int numberOfRetries, long timeToWait) {
		this.numberOfRetries = numberOfRetries;
		this.numberOfTriesLeft = numberOfRetries;
		this.timeToWait = timeToWait;
	}

	public boolean shouldRetry() {
		return numberOfTriesLeft > 0;
	}

	public void retryOccured() throws Exception {
		if (!shouldRetry()) {
			printRetry(numberOfAttemps, 1);			
			throw new Exception("Retry Failed: Total "+numberOfRetries+" attempts made at interval "+getTimeToWait()+" ms\n");
		}
		numberOfTriesLeft--;
		numberOfAttemps=numberOfAttemps+1;
		
		if (numberOfAttemps > 1)
			printRetry(numberOfAttemps, 0);
		
		waitUntilNextTry();
	}

	public int getNumberOfAttemps() {
		return numberOfAttemps;
	}
	
	public long getTimeToWait() {
		return timeToWait;
	}

	private void waitUntilNextTry() {
		try {
			Thread.sleep(getTimeToWait());
		} catch (InterruptedException ignored) {
		}
	}
	
	public void printRetry(int noOfAttemp, int cnt) {			 
		 if (cnt == 0 && noOfAttemp == 2)
			 noOfAttemp = 1;
		 else if (cnt == 0 && noOfAttemp == 3)
			 noOfAttemp = 2;
		 				 
		 switch (noOfAttemp) {
			  case 1:
			    System.out.println("Retry: "+noOfAttemp+" of "+numberOfRetries);
			    break;
			  case 2:
				  System.out.println("Retry: "+noOfAttemp+" of "+numberOfRetries);
			    break;
			  case 3:
				  System.out.println("Retry: "+noOfAttemp+" of "+numberOfRetries);
			    break;
			}			
    }

}
