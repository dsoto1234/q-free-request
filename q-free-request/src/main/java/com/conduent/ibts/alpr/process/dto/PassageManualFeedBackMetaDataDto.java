package com.conduent.ibts.alpr.process.dto;

public class PassageManualFeedBackMetaDataDto {

	public PassageManualFeedBackMetaDataDto() {
	}

	public String manualRegistration;
	public String manualJurisdiction;
	public String manualPlateType;
	public String manualImageOfRecord;
	public String manualRoiTop;
	public String manualRoiBottom;
	public String manualRoiLeft;
	public String manualRoiRight;
	
	public String getManualRegistration() {
		return manualRegistration;
	}
	public void setManualRegistration(String manualRegistration) {
		this.manualRegistration = manualRegistration;
	}
	public String getManualJurisdiction() {
		return manualJurisdiction;
	}
	public void setManualJurisdiction(String manualJurisdiction) {
		this.manualJurisdiction = manualJurisdiction;
	}
	public String getManualPlateType() {
		return manualPlateType;
	}
	public void setManualPlateType(String manualPlateType) {
		this.manualPlateType = manualPlateType;
	}
	public String getManualImageOfRecord() {
		return manualImageOfRecord;
	}
	public void setManualImageOfRecord(String manualImageOfRecord) {
		this.manualImageOfRecord = manualImageOfRecord;
	}
	public String getManualRoiTop() {
		return manualRoiTop;
	}
	public void setManualRoiTop(String manualRoiTop) {
		this.manualRoiTop = manualRoiTop;
	}
	public String getManualRoiBottom() {
		return manualRoiBottom;
	}
	public void setManualRoiBottom(String manualRoiBottom) {
		this.manualRoiBottom = manualRoiBottom;
	}
	public String getManualRoiLeft() {
		return manualRoiLeft;
	}
	public void setManualRoiLeft(String manualRoiLeft) {
		this.manualRoiLeft = manualRoiLeft;
	}
	public String getManualRoiRight() {
		return manualRoiRight;
	}
	public void setManualRoiRight(String manualRoiRight) {
		this.manualRoiRight = manualRoiRight;
	}
	@Override
	public String toString() {
		return "PassageManualFeedBackMetaDataDto [manualRegistration=" + manualRegistration + ", manualJurisdiction="
				+ manualJurisdiction + ", manualPlateType=" + manualPlateType + ", manualImageOfRecord="
				+ manualImageOfRecord + ", manualRoiTop=" + manualRoiTop + ", manualRoiBottom=" + manualRoiBottom
				+ ", manualRoiLeft=" + manualRoiLeft + ", manualRoiRight=" + manualRoiRight + "]";
	}
	
	
	
}
