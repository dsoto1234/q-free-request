package com.conduent.ibts.alpr.process.integration.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.conduent.ibts.alpr.process.config.ConfigVariable;
import com.conduent.ibts.alpr.process.dto.RequestTViolTxEventDto;
import com.conduent.ibts.alpr.process.exception.VectorImageReviewException;
import com.conduent.ibts.alpr.process.integration.TviolTxEventIntegrationApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TviolTxEventIntegrationApiServiceImpl implements TviolTxEventIntegrationApiService {

	private static final Logger logger = LoggerFactory.getLogger(TviolTxEventIntegrationApiServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ConfigVariable configVariable;

	@Override
	public boolean callingEventApi(Long laneTxId) throws VectorImageReviewException {
		boolean eventApiSuccess = false;
		try {
			RequestTViolTxEventDto requestTViolTxEventDto = prepareData(laneTxId);
			logger.info("Calling TviolTxEvent API");
			String eventApi = new ObjectMapper().writeValueAsString(requestTViolTxEventDto);
			logger.info("Json for request to TViol tx Event  API  : {}", eventApi);
			ResponseEntity<String> result = restTemplate.postForEntity(configVariable.gettVioltxEventApi(),
					requestTViolTxEventDto, String.class);
			if (HttpStatus.CREATED.equals(result.getStatusCode())) {
				eventApiSuccess = true;
				logger.info("** Response from Tvioltx Event API :{}", result.getBody());
			}
		} catch (HttpStatusCodeException e) {
			logger.info("Exception while Event API call :{},  {}", e.getRawStatusCode(), e.getMessage());
			e.printStackTrace();
			throw new VectorImageReviewException("t_viol_tx_event");
		} catch (JsonProcessingException e) {
			logger.info("Exception while converting LicaDto into json :{}", e.getMessage());
			e.printStackTrace();
			throw new VectorImageReviewException("t_viol_tx_event");
		} catch (BeansException e) {
			logger.info("Exception while copying LicaDto into TViolTxEventDto :{}", e.getMessage());
			e.printStackTrace();
			throw new VectorImageReviewException("t_viol_tx_event");
		} catch (Exception e) {
			e.printStackTrace();
			throw new VectorImageReviewException("t_viol_tx_event");
		}
		return eventApiSuccess;
	}

	private RequestTViolTxEventDto prepareData(Long laneTxId) {
		RequestTViolTxEventDto violTxEventDto = new RequestTViolTxEventDto();
		violTxEventDto.setAddressSource("ALPR");
		violTxEventDto.setLaneTxId(laneTxId);
		return violTxEventDto;
	}
}
