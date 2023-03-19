package com.conduent.ibts.alpr.process.dto;

public class TCodesDto {

	@Override
	public String toString() {
		return "TCodesDto [codeId=" + codeId + ", codeType=" + codeType + ", codeValue=" + codeValue + "]";
	}

	Long codeId;
	String codeType;
	String codeValue;

	public TCodesDto() {
	}

	public Long getCodeId() {
		return codeId;
	}

	public void setCodeId(Long codeId) {
		this.codeId = codeId;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCodeValue() {
		return codeValue;
	}

	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

	public TCodesDto(Long codeId, String codeType, String codeValue) {
		super();
		this.codeId = codeId;
		this.codeType = codeType;
		this.codeValue = codeValue;
	}
}
