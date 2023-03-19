package com.conduent.ibts.alpr.process.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dao.impl.MasterCacheDaoImpl;
import com.conduent.ibts.alpr.process.dto.TCodesDto;
import com.conduent.ibts.alpr.process.dto.TProcessParameterDto;

@Component
public class MasterCache {

	private static final Logger masterCacheLog = LoggerFactory.getLogger(MasterCache.class);
	
	@Autowired
	MasterCacheDaoImpl masterCacheDao;

	private List<TCodesDto> tCodeDto;
	
	private List<TProcessParameterDto> listOfTprocessParameters;

	private Map<String,String> qFreeProcessParameters;
	
	public MasterCache() {
		masterCacheLog.info("*************************** entering MasterCache() constructor ***********************");
	}
	
	public Map<String, String> getqFreeProcessParameters() {
		return qFreeProcessParameters;
	}

	public void setqFreeProcessParameters(Map<String, String> qFreeProcessParameters) {
		this.qFreeProcessParameters = qFreeProcessParameters;
	}
	
	public List<TProcessParameterDto> getListOfTprocessParameters() {
		return listOfTprocessParameters;
	}

	public void setListOfTprocessParameters(List<TProcessParameterDto> listOfTprocessParameters) {
		this.listOfTprocessParameters = listOfTprocessParameters;
	}

	public List<TCodesDto> gettCodeDto() {
		return tCodeDto;
	}

	public void settCodeDto(List<TCodesDto> tCodeDto) {
		this.tCodeDto = tCodeDto;
	}
	
	@PostConstruct
	public void initialize() {
		System.out.println("Inside initialize method");
		masterCacheLog.debug("Getting master data from db");
		settCodeDto(masterCacheDao.getFromTCodes());
		setListOfTprocessParameters(masterCacheDao.getProcessParameters());
		setqFreeProcessParameters(getQFreeParameters(getListOfTprocessParameters()));

	}
	
	private Map<String,String> getQFreeParameters(List<TProcessParameterDto> listOfTprocessParameters){
		Map<String,String> qFreeParamAndValue = new HashMap<>();
		qFreeParamAndValue = listOfTprocessParameters.stream()
				.filter(param -> Constants.PARAM_GROUP_IMAGEREVIEW.equals(param.getParamGroup())
						&& Constants.PARAM_CODE_QFREE.equals(param.getParamCode())
						&& 0==param.getAgencyId().intValue())
				.collect(Collectors.toMap(TProcessParameterDto::getParamName, TProcessParameterDto::getParamValue));
		masterCacheLog.info(":{}",qFreeParamAndValue);
		return qFreeParamAndValue;
	}
}
