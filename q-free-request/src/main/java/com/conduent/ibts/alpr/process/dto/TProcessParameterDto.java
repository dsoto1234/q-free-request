package com.conduent.ibts.alpr.process.dto;

public class TProcessParameterDto {

	private String paramValue;
	private String paramCode;
	private String paramName;
	private Long agencyId;
	private Long storeId;
	private String paramConfig;
	private String paramGroup;
	
	public String getParamGroup() {
		return paramGroup;
	}
	public void setParamGroup(String paramGroup) {
		this.paramGroup = paramGroup;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getParamCode() {
		return paramCode;
	}
	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public Long getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(Long agencyId) {
		this.agencyId = agencyId;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getParamConfig() {
		return paramConfig;
	}
	public void setParamConfig(String paramConfig) {
		this.paramConfig = paramConfig;
	}
	@Override
	public String toString() {
		return "TProcessParameterDTO [paramValue=" + paramValue + ", paramCode=" + paramCode + ", paramName="
				+ paramName + ", agencyId=" + agencyId + ", storeId=" + storeId + ", paramConfig=" + paramConfig + "]";
	}
}
