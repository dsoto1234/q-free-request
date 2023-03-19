package com.conduent.ibts.alpr.process.batch;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.conduent.ibts.alpr.process.config.ConfigVariable;
import com.conduent.ibts.alpr.process.constants.Constants;
import com.conduent.ibts.alpr.process.dao.impl.QFreeDaoImpl;
import com.conduent.ibts.alpr.process.dto.QFreeBatchDto;
import com.conduent.ibts.alpr.process.service.impl.QFreeServiceImpl;
import com.conduent.ibts.alpr.process.vectorlogger.CreateMultiLogger;
import com.conduent.ibts.alpr.process.vectorlogger.MultiLogger;

@Component
public class BatchWriter implements ItemWriter<QFreeBatchDto> {

	private static final Logger logger = LoggerFactory.getLogger(BatchWriter.class);
	
	@Autowired
	QFreeDaoImpl qFreeDao;
	
	@Autowired
	QFreeServiceImpl qfreeService;
	
	@Autowired
	private ConfigVariable configVariable;
	
	public BatchWriter() {
		logger.info("*************************** entering BatchWriter() constructor ***********************");
	}
	
	@Override
	public void write(List<? extends QFreeBatchDto> items) throws Exception {		
		logger.info("<<<<<<<<< entering BatchWriter.write() items.size: "+items.size()+" items: "+items+ " threadName: "+Thread.currentThread().getName()+"  >>>>>>>>>\n");
		
		Map<String, ?> mapOnCurrentStatus = items.stream().collect(Collectors.groupingBy(QFreeBatchDto::getExecutionStatus, Collectors.toList()));
		QFreeBatchDto qfreeBatch = items.get(0);
		MultiLogger extLog = null;
		
		if (qfreeBatch != null) {
			CreateMultiLogger createMultiLogger = new CreateMultiLogger();
			extLog = createMultiLogger.getVectorLoggerExt(configVariable.getMultiLoggerLocation(), qfreeBatch.getAgencyId(),
					qfreeBatch.getThreadIndex(), qfreeBatch.getPlazaId(), qfreeBatch.getLprType(), qfreeBatch.getTotalThreads());
		}						 						
				
		extLog.info("<<<<<<<<< entering BatchWriter.write() items.size: "+items.size()+" items: "+items+ " threadName: "+Thread.currentThread().getName()+"  >>>>>>>>>\n");
		
		//LOGGER.info("in BatchWriter.write() items.get(0).getThreadIndex(): "+items.get(0).getThreadIndex() + " threadName:" + Thread.currentThread().getName());
		
		//************* test
		/*mapOnCurrentStatus.forEach((k, v) -> {
			LOGGER.info("??????? key:"+ k + " value: "+ v);
			for (@SuppressWarnings("unchecked")
				Iterator<? extends QFreeBatchDto> iterator = ((List<? extends QFreeBatchDto>) v).iterator(); iterator.hasNext();) 
			{
				QFreeBatchDto batchDto = (QFreeBatchDto) iterator.next();
				LOGGER.info("??????? batchDto:" +batchDto);
			}
		});
		*/
		//************* end test
		/*if (!qFreeDao.checkSimilarRowInBatchTable(items.get(0).getRunId(), items.get(0).getAgencyId(), items.get(0).getPlazaId(), items.get(0).getLprType(), extLog)) 
		{			
			processRunIDList(mapOnCurrentStatus, extLog);
			qFreeDao.updateExecutionStatusAndEndTime(items.get(0).getRunId(), Constants.N, extLog);
			
		}else 
		{
			logger.info("in BatchWriter.write() skipping run_id:{} since same combination is being executed by another thread",  items.get(0).getRunId());
		}*/
		if (!qFreeDao.checkSimilarRowInBatchTable(qfreeBatch.getRunId(), qfreeBatch.getAgencyId(), qfreeBatch.getPlazaId(), qfreeBatch.getLprType(), qfreeBatch.getThreadIndex(), extLog) ) {
			
			processRunIDList(mapOnCurrentStatus, extLog);
				
			qFreeDao.updateExecutionStatusAndEndTime(qfreeBatch.getRunId(), Constants.N, extLog);
		}else {			
			extLog.info("skipping run_id: " + qfreeBatch.getRunId() +" since same combination is being executed by another thread");
		}
		
		
		if (extLog != null && extLog.getVectorFileHandler() != null) {
			extLog.closeFileHandler();
		}
		
		
		logger.info("exiting BatchWriter.write() items: "+items+ " threadName: "+Thread.currentThread().getName());
	}
	
	private void processRunIDList(Map<String, ?> mapOnCurrentStatus, MultiLogger extLog) throws InterruptedException {
		extLog.info("entering BatchWriter.processRunIDList()  mapOnCurrentStatus.size(): "+ mapOnCurrentStatus.size()+"\n");	
		
		mapOnCurrentStatus.forEach((k, v) -> {
			
			extLog.info("in BatchWriter.processRunIDList() k: "+k+" v: "+v+" threadName:" + Thread.currentThread().getName());
			
			String executionStatus = k;
			
			switch (executionStatus) 
			{
				case Constants.N: {
					//LOGGER.info("In RUN_STATUS N:{}  threadName:{}", v, Thread.currentThread().getName());					
					for (@SuppressWarnings("unchecked")
						Iterator<? extends QFreeBatchDto> iterator = ((List<? extends QFreeBatchDto>) v).iterator(); iterator.hasNext();) 
						{
							QFreeBatchDto batchDto = (QFreeBatchDto) iterator.next();
							
							//extLog.info("\n");
							extLog.info("in BatchWriter.processRunIDList() batchDto: "+batchDto + " threadName: "+Thread.currentThread().getName()+"\n");
							
							qFreeDao.updateExecutionStatusAndStartTime(batchDto.getRunId(), Constants.Y, extLog);							
							qfreeService.getTransactionsToEnqueue(batchDto, extLog);
						}
					}
					break;
				case Constants.Y: 
				{
					extLog.info("in BatchWriter.processRunIDList() CURRENT_STATUS Y:"+ v);
				}
					break;
				default:
					break;
			}

		});
	
		extLog.info("exiting BatchWriter.processRunIDList()");
	}

}
