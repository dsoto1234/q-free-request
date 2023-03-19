package com.conduent.ibts.alpr.process.dao;

import java.util.List;

import com.conduent.ibts.alpr.process.dto.TCodesDto;
import com.conduent.ibts.alpr.process.dto.TProcessParameterDto;

public interface MasterCacheDao {

	public List<TCodesDto> getFromTCodes();
	public List<TProcessParameterDto> getProcessParameters() ;
}
