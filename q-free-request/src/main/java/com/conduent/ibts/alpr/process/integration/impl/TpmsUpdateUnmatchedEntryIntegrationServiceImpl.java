package com.conduent.ibts.alpr.process.integration.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.conduent.app.timezone.utility.TimeZoneConv;
import com.conduent.ibts.alpr.process.config.ConfigVariable;
import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dto.TUnmatchedEntryTxDTO;
import com.conduent.ibts.alpr.process.integration.TpmsUpdateUnmatchedEntryIntegrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


@Service
public class TpmsUpdateUnmatchedEntryIntegrationServiceImpl implements TpmsUpdateUnmatchedEntryIntegrationService {

	private static final Logger TPMS_INTEGRATION_LOG = LoggerFactory
			.getLogger(TpmsUpdateUnmatchedEntryIntegrationServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ConfigVariable configVariable;
	
	@Autowired
	private TimeZoneConv timeZoneConv;

	
	private TUnmatchedEntryTxDTO prepareDataForTpmsCall(String laneTxId, long violXferId, Map<String,Long> codeIdMap) {
		
		TPMS_INTEGRATION_LOG.info("entering TpmsUpdateUnmatchedEntryIntegrationServiceImpl.prepareDataForTpmsCall() laneTxId: " + laneTxId+
			"  violXferId: "+violXferId + "  codeIdMap.get(Constants.CODE_VALUE_DMVREREVIEW).intValue(): "+codeIdMap.get(Constants.CODE_VALUE_DMVREREVIEW).intValue() +
			" codeIdMap.get(Constants.CODE_VALUE_IMGREVIEW).intValue(): "+codeIdMap.get(Constants.CODE_VALUE_IMGREVIEW).intValue());
		
		TUnmatchedEntryTxDTO tUnmatchedEntryTxRequest = new TUnmatchedEntryTxDTO();
		tUnmatchedEntryTxRequest.setLaneTxId(Long.valueOf(laneTxId));
		tUnmatchedEntryTxRequest.setTxStatus(codeIdMap.get(Constants.CODE_VALUE_DMVREREVIEW).intValue());
		tUnmatchedEntryTxRequest.setUpdateTs(timeZoneConv.currentTime());
		tUnmatchedEntryTxRequest.setEventType(codeIdMap.get(Constants.CODE_VALUE_IMGREVIEW).intValue());
		tUnmatchedEntryTxRequest.setImageBatchId(String.valueOf(violXferId));
		tUnmatchedEntryTxRequest.setOutputFileId(String.valueOf(violXferId));
		tUnmatchedEntryTxRequest.setOutputFileType(Constants.TVIOLTX_OUTPUT_FILE_TYPE_TR);
				
		TPMS_INTEGRATION_LOG.info("exiting TpmsUpdateUnmatchedEntryIntegrationServiceImpl.prepareDataForTpmsCall() tUnmatchedEntryTxRequest: "+tUnmatchedEntryTxRequest);
		
		return tUnmatchedEntryTxRequest;
	}

	/**
	 * This method consumes the API from TPMS sub system
	 * 
	 * @return boolean
	 */
	@Override
	public boolean updateTUnmatchedEntryTable(String laneTxId, long violXferId, Map<String,Long> codeIdMap) {
		
		TPMS_INTEGRATION_LOG.info("entering TpmsUpdateUnmatchedEntryIntegrationServiceImpl.updateTUnmatchedEntryTable() laneTxId: "+laneTxId + " violXferId: "+violXferId);
		
		boolean status = false;
		TUnmatchedEntryTxDTO tUnmatchedEntryTxRequest = null;
		
		try {
			
			tUnmatchedEntryTxRequest = prepareDataForTpmsCall(laneTxId, violXferId, codeIdMap);
			
			TPMS_INTEGRATION_LOG.info("in TpmsUpdateUnmatchedEntryIntegrationServiceImpl.updateTUnmatchedEntryTable() request for tpms api :{}",tUnmatchedEntryTxRequest);
			TPMS_INTEGRATION_LOG.info("in TpmsUpdateUnmatchedEntryIntegrationServiceImpl.updateTUnmatchedEntryTable() Calling TPMS update unmatched entry table API");
			
			String tUnmatchedEntryTxReq = new ObjectMapper().writeValueAsString(tUnmatchedEntryTxRequest);
			
			TPMS_INTEGRATION_LOG.info("in TpmsUpdateUnmatchedEntryIntegrationServiceImpl.updateTUnmatchedEntryTable() Json for request to TPMS Integration  : {}", tUnmatchedEntryTxReq);

			ResponseEntity<String> result = restTemplate.postForEntity(configVariable.getTpmsUnmatchedUrl(), tUnmatchedEntryTxReq, String.class);
			
			String tpmsResponse = new ObjectMapper().writeValueAsString(result);
			
			TPMS_INTEGRATION_LOG.info("in TpmsUpdateUnmatchedEntryIntegrationServiceImpl.updateTUnmatchedEntryTable() Json for response from response entity TPMS  : {}", tpmsResponse);
			
			if (result.getStatusCodeValue() == 200) {
				JsonObject jsonObject = new Gson().fromJson(result.getBody(), JsonObject.class);
				Gson gson = new Gson();
				status = gson.fromJson(jsonObject.getAsJsonObject("status"), boolean.class);
				TPMS_INTEGRATION_LOG.info("in TpmsUpdateUnmatchedEntryIntegrationServiceImpl.updateTUnmatchedEntryTable() Json for response from TPMS  : {}", status);
			}
		
	} catch (HttpStatusCodeException e) {		
		TPMS_INTEGRATION_LOG.info("Error in TpmsUpdateUnmatchedEntryIntegrationServiceImpl.updateTUnmatchedEntryTable() Exception: {} while TPMS api  call  {}", 
				e.getMessage(), tUnmatchedEntryTxRequest.getLaneTxId());
	} catch (Exception e) {
		TPMS_INTEGRATION_LOG.info("Error in TpmsUpdateUnmatchedEntryIntegrationServiceImpl.updateTUnmatchedEntryTable()  Exception: {} while TPMS api  call  {}", 
				e.getMessage(), tUnmatchedEntryTxRequest.getLaneTxId());
	}
		
		TPMS_INTEGRATION_LOG.info("exiting TpmsUpdateUnmatchedEntryIntegrationServiceImpl.updateTUnmatchedEntryTable() laneTxId: "+laneTxId + " violXferId: "+violXferId);
		return status;
	}

}
