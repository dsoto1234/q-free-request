package com.conduent.ibts.alpr.process.dto;

import java.io.Serializable;

public class RequestTViolTxEventDto implements Serializable {
	private static final long serialVersionUID = 8718623593157558731L;
	
	private String addressSource;
	private Long laneTxId;
	
	public String getAddressSource() {
		return addressSource;
	}

	public void setAddressSource(String addressSource) {
		this.addressSource = addressSource;
	}
	public Long getLaneTxId() {
		return laneTxId;
	}

	public void setLaneTxId(Long laneTxId) {
		this.laneTxId = laneTxId;
	}

	

}
