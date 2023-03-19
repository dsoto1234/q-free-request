package com.conduent.ibts.alpr.process.vo;

import cloud.intrada.common.StatusEnumeration;

public class QFreeResponseAcknowledgementVO {

	private StatusEnumeration ackStatus;	    	     
	private String issPassageID;
	private String description;
	private String hostPassageId;
	private String ackFrom;
	private String ackTo;
	
	public StatusEnumeration getAckStatus() {
		return ackStatus;
	}
	public void setAckStatus(StatusEnumeration ackStatus) {
		this.ackStatus = ackStatus;
	}
	public String getIssPassageID() {
		return issPassageID;
	}
	public void setIssPassageID(String issPassageID) {
		this.issPassageID = issPassageID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHostPassageId() {
		return hostPassageId;
	}
	public void setHostPassageId(String hostPassageId) {
		this.hostPassageId = hostPassageId;
	}
	public String getAckFrom() {
		return ackFrom;
	}
	public void setAckFrom(String ackFrom) {
		this.ackFrom = ackFrom;
	}
	public String getAckTo() {
		return ackTo;
	}
	public void setAckTo(String ackTo) {
		this.ackTo = ackTo;
	}
	
	
}
