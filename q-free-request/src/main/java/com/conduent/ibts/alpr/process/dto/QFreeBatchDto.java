package com.conduent.ibts.alpr.process.dto;

import java.time.LocalDateTime;
 
public class QFreeBatchDto{

	private Long runId;
	private Long agencyId;
	private Long plazaId;
	private String isEnabled;
	private String lprType;
	private String executionStatus;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Long totalThreads;
	private Long threadIndex;
	private Long batchSize;
	
	public QFreeBatchDto() {}
	
	
	public QFreeBatchDto(Long runId, Long agencyId, Long plazaId, String isEnabled, String lprType, String executionStatus,
		LocalDateTime startTime, Long totalThreads, Long threadIndex, LocalDateTime endTime, Long batchSize) 
	{
		this.runId = runId;
		this.agencyId = agencyId;
		this.plazaId =plazaId ;
		this.isEnabled  = isEnabled;
		this.lprType  = lprType;
		this.executionStatus = executionStatus;
		this.startTime  = startTime;
		this.totalThreads  = totalThreads;
		this.threadIndex = threadIndex;
		this.endTime  = endTime;
		this.batchSize  = batchSize;			
	}		
	
	public Long getRunId() {
		return runId;
	}
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	public Long getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(Long agencyId) {
		this.agencyId = agencyId;
	}
	public Long getPlazaId() {
		return plazaId;
	}
	public void setPlazaId(Long plazaId) {
		this.plazaId = plazaId;
	}
	public String getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getLprType() {
		return lprType;
	}
	public void setLprType(String lprType) {
		this.lprType = lprType;
	}
	
	public String getExecutionStatus() {
		return executionStatus;
	}
	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public Long getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(Long batchSize) {
		this.batchSize = batchSize;
	}
	
	public Long getTotalThreads() {
		return totalThreads;
	}
	public void setTotalThreads(Long totalThreads) {
		this.totalThreads = totalThreads;
	}
	public Long getThreadIndex() {
		return threadIndex;
	}
	public void setThreadIndex(Long threadIndex) {
		this.threadIndex = threadIndex;
	}
	@Override
	public String toString() {
		return "QFreeBatchDto [runId=" + runId + ", agencyId=" + agencyId + ", plazaId=" + plazaId + ", isEnabled="
				+ isEnabled + ", lprType=" + lprType + ", executionStatus=" + executionStatus + ", startTime="
				+ startTime + ", endTime=" + endTime + ", totalThreads=" + totalThreads + ", threadIndex=" + threadIndex
				+ ", batchSize=" + batchSize + "]";
	}
	
	
}
