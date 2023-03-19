package com.conduent.ibts.alpr.process.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLoggerExt;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLogger;

public class BatchRowMapper implements RowMapper<QFreeBatchDto> {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchRowMapper.class);
	
	@Override
	public QFreeBatchDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		logger.info("<<<<< entering BatchRowMapper.mapRow() rowNum: "+rowNum+" threadName: "+Thread.currentThread().getName()+" >>>>>");
		
		QFreeBatchDto qFreeBatchDto = new QFreeBatchDto(				
				rs.getLong("RUN_ID"),
				rs.getLong("AGENCY_ID"),
				rs.getLong("PLAZA_ID"),
				rs.getString("IS_ENABLED"),
				rs.getString("LPR_TYPE"),
				rs.getString("EXECUTION_STATUS"),
				rs.getTimestamp("START_TIME").toLocalDateTime(),
				rs.getLong("TOTAL_THREADS"),
				rs.getLong("THREAD_INDEX"),
				rs.getTimestamp("END_TIME").toLocalDateTime(),
				rs.getLong("BATCH_SIZE")								
				);
		
		logger.info("<<<<< exiting BatchRowMapper.mapRow() threadName: "+Thread.currentThread().getName()+ " >>>>>");
		
		return qFreeBatchDto;
	}

}
