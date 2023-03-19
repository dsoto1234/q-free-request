package com.conduent.ibts.alpr.process.integration;

import java.util.Map;

/**
 * This interface is used to perform integration operation
 * {@link TpmsUpdateUnmatchedEntryIntegrationService
 * 
 * @author AJAYV
 * @version 1.0
 * @since 28-05-2021
 *
 */
public interface TpmsUpdateUnmatchedEntryIntegrationService {
	
	/**
	 * This method consumes the API from FPMS sub system 
	 * @param accountDetailsDTO 
	*/
	boolean updateTUnmatchedEntryTable(String laneTxId, long violXferId, Map<String,Long> codeIdMap);

}
