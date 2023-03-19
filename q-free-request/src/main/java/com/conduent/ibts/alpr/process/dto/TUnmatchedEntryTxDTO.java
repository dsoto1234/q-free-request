package com.conduent.ibts.alpr.process.dto;

import java.time.LocalDateTime;

public class TUnmatchedEntryTxDTO {

	private Long laneTxId;
	private Integer txStatus;
	private LocalDateTime updateTs;
	private String outputFileType;
	private Integer eventType;
	private String imageBatchId;
	private String outputFileId;
	
	
	
	public String getOutputFileType() {
		return outputFileType;
	}
	public void setOutputFileType(String outputFileType) {
		this.outputFileType = outputFileType;
	}
	public Integer getEventType() {
		return eventType;
	}
	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}
	public String getImageBatchId() {
		return imageBatchId;
	}
	public void setImageBatchId(String imageBatchId) {
		this.imageBatchId = imageBatchId;
	}
	public String getOutputFileId() {
		return outputFileId;
	}
	public void setOutputFileId(String outputFileId) {
		this.outputFileId = outputFileId;
	}
	public Long getLaneTxId() {
		return laneTxId;
	}
	public void setLaneTxId(Long laneTxId) {
		this.laneTxId = laneTxId;
	}
	public Integer getTxStatus() {
		return txStatus;
	}
	public void setTxStatus(Integer txStatus) {
		this.txStatus = txStatus;
	}
	public LocalDateTime getUpdateTs() {
		return updateTs;
	}
	public void setUpdateTs(LocalDateTime updateTs) {
		this.updateTs = updateTs;
	}
	@Override
	public String toString() {
		return "TUnmatchedEntryTxDTO [laneTxId=" + laneTxId + ", txStatus=" + txStatus + ", updateTs=" + updateTs
				+ ", outputFileType=" + outputFileType + ", eventType=" + eventType + ", imageBatchId=" + imageBatchId
				+ ", outputFileId=" + outputFileId + "]";
	}
	
	
}
