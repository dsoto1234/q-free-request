package com.conduent.ibts.alpr.process.dao.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.conduent.ibts.alpr.process.config.LoadJpaQueries;
import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dao.MasterCacheDao;
import com.conduent.ibts.alpr.process.dto.TCodesDto;
import com.conduent.ibts.alpr.process.dto.TProcessParameterDto;

@Repository
public class MasterCacheDaoImpl implements MasterCacheDao {

	private static final Logger logger = LoggerFactory.getLogger(MasterCacheDaoImpl.class);

	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	/**
	 * This method is used to get data from T_Codes table
	 * 
	 * @return List<TCodesDto>
	 */
	@Override
	public List<TCodesDto> getFromTCodes() {
		logger.info("entering MasterCacheDaoImpl.getFromTCodes()  Getting t_codes data ......");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();		
		String query = LoadJpaQueries.getQueryById("GET_FROM_TCODES");
		
		logger.info("in MasterCacheDaoImpl.getFromTCodes() query: {} ", query);
		logger.info("in MasterCacheDaoImpl.getFromTCodes() paramSource: {}", paramSource);
		
		return namedJdbcTemplate.query(query, paramSource, BeanPropertyRowMapper.newInstance(TCodesDto.class));
	}
	
	@Override
	public List<TProcessParameterDto> getProcessParameters() {
		logger.info("entering MasterCacheDaoImpl.getProcessParameters()  Getting t_process_parameters data ......");
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		List<TProcessParameterDto> dtos = null;
		String query = LoadJpaQueries.getQueryById("GET_FROM_T_PROCESS_PARAMETER");
		List<String> paramGroup = Arrays.asList("IBTS", "IMAGEREVIEW");
		paramSource.addValue(Constants.PARAM_GROUP, paramGroup);
		
		logger.info("in MasterCacheDaoImpl.getProcessParameters() query: {}", query);
		logger.info("in MasterCacheDaoImpl.getProcessParameters() paramSource: {}", paramSource);
		
		dtos = namedJdbcTemplate.query(query, paramSource, BeanPropertyRowMapper.newInstance(TProcessParameterDto.class));
		return dtos;
	}
}
