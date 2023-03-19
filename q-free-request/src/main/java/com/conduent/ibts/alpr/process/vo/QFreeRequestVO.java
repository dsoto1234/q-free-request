package com.conduent.ibts.alpr.process.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import com.conduent.ibts.alpr.process.dto.ImageDto;
import com.conduent.ibts.alpr.process.dto.PassageManualFeedBackMetaDataDto;

public class QFreeRequestVO {
	private Long laneTxId; // Q-Free mapping HostPassageID
	private Date txDate;
	private String txTimestamp;
	private String agencyShortName; // Q-Free mapping tollSystem
	private int agencyId;
	private String plazaName; // Q-Free mapping roadway
	private Long plazaId;
	private Long laneId;
	private Long axleCount;
	private String imageFileIndex;
	private String outFileName;
	private Integer procImageCnt;
	private String source;
	private String priority;
	private String lprType;
	private String lprStatus;
	private XMLGregorianCalendar captureTimeUTC;
	private XMLGregorianCalendar expirationTimeUTC;

	// ********* TX WITHOUT MIR FIELDS END **********
	private String manualPlateNumber;
	private String manualPlateState;
	private String manualPlateType;
	private String manualPlateCountry;
	private String manualReviewIndex;
	private Integer manualRoiTop;
	private Integer manualRoiBottom;
	private Integer manualRoiLeft;
	private Integer manualRoiRight;
	private String unmatchedEntryFlag;
	private String sectionId;
	private String aetFlag;
	private Date imageReceiveDate;
	private Long imageTxId;
	private Long actualClass;
	private String vehicleTypeRequired;
	private String extPlazaId;
	private String extLaneId;

	// ************************************** view
	private String imageUrl1;
	private String imageUrl2;
	private String imageUrl3;
	private String imageUrl4;
	private String imageUrl5;
	private String imageUrl6;
	private String imageUrl7;
	private String imageUrl8;

	private String imageIndex1;
	private String imageIndex2;
	private String imageIndex3;
	private String imageIndex4;
	private String imageIndex5;
	private String imageIndex6;
	private String imageIndex7;
	private String imageIndex8;

	private String imageFacing1;
	private String imageFacing2;
	private String imageFacing3;
	private String imageFacing4;
	private String imageFacing5;
	private String imageFacing6;
	private String imageFacing7;
	private String imageFacing8;

	private String extractLprFlag1;
	private String extractLprFlag2;
	private String extractLprFlag3;
	private String extractLprFlag4;
	private String extractLprFlag5;
	private String extractLprFlag6;
	private String extractLprFlag7;
	private String extractLprFlag8;

	private Map<String, ImageDto> listOfImages = new HashMap<>();
	private PassageManualFeedBackMetaDataDto passageManualFeedBackMetaData;

	// Begin image
	public ImageDto getImage(String key) {
		return listOfImages.get(key);
	}

	public void addImage(String key, ImageDto imageDAO) {
		listOfImages.put(key, imageDAO);
	}

	public Map<String, ImageDto> getAllImages() {
		return listOfImages;
	}

	public boolean containsImage(String key) {
		return listOfImages.containsKey(key);
	}

	// end image

	public Long getLaneTxId() {
		return laneTxId;
	}

	public void setLaneTxId(Long laneTxId) {
		this.laneTxId = laneTxId;
	}

	public Date getTxDate() {
		return txDate;
	}

	public void setTxDate(Date txDate) {
		this.txDate = txDate;
	}

	public String getTxTimestamp() {
		return txTimestamp;
	}

	public void setTxTimestamp(String txTimestamp) {
		this.txTimestamp = txTimestamp;
	}

	public String getAgencyShortName() {
		return agencyShortName;
	}

	public void setAgencyShortName(String agencyShortName) {
		this.agencyShortName = agencyShortName;
	}

	

	public XMLGregorianCalendar getCaptureTimeUTC() {
		return captureTimeUTC;
	}

	public void setCaptureTimeUTC(XMLGregorianCalendar captureTimeUTC) {
		this.captureTimeUTC = captureTimeUTC;
	}

	public XMLGregorianCalendar getExpirationTimeUTC() {
		return expirationTimeUTC;
	}

	public void setExpirationTimeUTC(XMLGregorianCalendar expirationTimeUTC) {
		this.expirationTimeUTC = expirationTimeUTC;
	}

	public String getPlazaName() {
		return plazaName;
	}

	public void setPlazaName(String plazaName) {
		this.plazaName = plazaName;
	}

	

	public String getImageFileIndex() {
		return imageFileIndex;
	}

	public void setImageFileIndex(String imageFileIndex) {
		this.imageFileIndex = imageFileIndex;
	}

	public String getOutFileName() {
		return outFileName;
	}

	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

	

	public int getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(int agencyId) {
		this.agencyId = agencyId;
	}

	public Long getPlazaId() {
		return plazaId;
	}

	public void setPlazaId(Long plazaId) {
		this.plazaId = plazaId;
	}

	public Long getLaneId() {
		return laneId;
	}

	public void setLaneId(Long laneId) {
		this.laneId = laneId;
	}

	public Long getAxleCount() {
		return axleCount;
	}

	public void setAxleCount(Long axleCount) {
		this.axleCount = axleCount;
	}

	public Integer getProcImageCnt() {
		return procImageCnt;
	}

	public void setProcImageCnt(Integer procImageCnt) {
		this.procImageCnt = procImageCnt;
	}

	public Integer getManualRoiTop() {
		return manualRoiTop;
	}

	public void setManualRoiTop(Integer manualRoiTop) {
		this.manualRoiTop = manualRoiTop;
	}

	public Integer getManualRoiBottom() {
		return manualRoiBottom;
	}

	public void setManualRoiBottom(Integer manualRoiBottom) {
		this.manualRoiBottom = manualRoiBottom;
	}

	public Integer getManualRoiLeft() {
		return manualRoiLeft;
	}

	public void setManualRoiLeft(Integer manualRoiLeft) {
		this.manualRoiLeft = manualRoiLeft;
	}

	public Integer getManualRoiRight() {
		return manualRoiRight;
	}

	public void setManualRoiRight(Integer manualRoiRight) {
		this.manualRoiRight = manualRoiRight;
	}

	public Long getImageTxId() {
		return imageTxId;
	}

	public void setImageTxId(Long imageTxId) {
		this.imageTxId = imageTxId;
	}

	public Long getActualClass() {
		return actualClass;
	}

	public void setActualClass(Long actualClass) {
		this.actualClass = actualClass;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getLprType() {
		return lprType;
	}

	public void setLprType(String lprType) {
		this.lprType = lprType;
	}
	
	

	/*
	 * public XMLGregorianCalendar getCaptureTimeUTC() { return captureTimeUTC; }
	 * 
	 * public void setCaptureTimeUTC(XMLGregorianCalendar captureTimeUTC) {
	 * this.captureTimeUTC = captureTimeUTC; }
	 * 
	 * public XMLGregorianCalendar getExpirationTimeUTC() { return
	 * expirationTimeUTC; }
	 * 
	 * public void setExpirationTimeUTC(XMLGregorianCalendar expirationTimeUTC) {
	 * this.expirationTimeUTC = expirationTimeUTC; }
	 */

	public String getLprStatus() {
		return lprStatus;
	}

	public void setLprStatus(String lprStatus) {
		this.lprStatus = lprStatus;
	}

	public String getManualPlateNumber() {
		return manualPlateNumber;
	}

	public void setManualPlateNumber(String manualPlateNumber) {
		this.manualPlateNumber = manualPlateNumber;
	}

	public String getManualPlateState() {
		return manualPlateState;
	}

	public void setManualPlateState(String manualPlateState) {
		this.manualPlateState = manualPlateState;
	}

	public String getManualPlateType() {
		return manualPlateType;
	}

	public void setManualPlateType(String manualPlateType) {
		this.manualPlateType = manualPlateType;
	}

	public String getManualPlateCountry() {
		return manualPlateCountry;
	}

	public void setManualPlateCountry(String manualPlateCountry) {
		this.manualPlateCountry = manualPlateCountry;
	}

	public String getManualReviewIndex() {
		return manualReviewIndex;
	}

	public void setManualReviewIndex(String manualReviewIndex) {
		this.manualReviewIndex = manualReviewIndex;
	}

	
	public String getUnmatchedEntryFlag() {
		return unmatchedEntryFlag;
	}

	public void setUnmatchedEntryFlag(String unmatchedEntryFlag) {
		this.unmatchedEntryFlag = unmatchedEntryFlag;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getAetFlag() {
		return aetFlag;
	}

	public void setAetFlag(String aetFlag) {
		this.aetFlag = aetFlag;
	}

	public Date getImageReceiveDate() {
		return imageReceiveDate;
	}

	public void setImageReceiveDate(Date imageReceiveDate) {
		this.imageReceiveDate = imageReceiveDate;
	}

	

	public String getVehicleTypeRequired() {
		return vehicleTypeRequired;
	}

	public void setVehicleTypeRequired(String vehicleTypeRequired) {
		this.vehicleTypeRequired = vehicleTypeRequired;
	}

	public String getExtPlazaId() {
		return extPlazaId;
	}

	public void setExtPlazaId(String extPlazaId) {
		this.extPlazaId = extPlazaId;
	}

	public String getExtLaneId() {
		return extLaneId;
	}

	public void setExtLaneId(String extLaneId) {
		this.extLaneId = extLaneId;
	}

	public String getImageUrl1() {
		return imageUrl1;
	}

	public void setImageUrl1(String imageUrl1) {
		this.imageUrl1 = imageUrl1;
	}

	public String getImageUrl2() {
		return imageUrl2;
	}

	public void setImageUrl2(String imageUrl2) {
		this.imageUrl2 = imageUrl2;
	}

	public String getImageUrl3() {
		return imageUrl3;
	}

	public void setImageUrl3(String imageUrl3) {
		this.imageUrl3 = imageUrl3;
	}

	public String getImageUrl4() {
		return imageUrl4;
	}

	public void setImageUrl4(String imageUrl4) {
		this.imageUrl4 = imageUrl4;
	}

	public String getImageUrl5() {
		return imageUrl5;
	}

	public void setImageUrl5(String imageUrl5) {
		this.imageUrl5 = imageUrl5;
	}

	public String getImageUrl6() {
		return imageUrl6;
	}

	public void setImageUrl6(String imageUrl6) {
		this.imageUrl6 = imageUrl6;
	}

	public String getImageUrl7() {
		return imageUrl7;
	}

	public void setImageUrl7(String imageUrl7) {
		this.imageUrl7 = imageUrl7;
	}

	public String getImageUrl8() {
		return imageUrl8;
	}

	public void setImageUrl8(String imageUrl8) {
		this.imageUrl8 = imageUrl8;
	}

	public String getImageIndex1() {
		return imageIndex1;
	}

	public void setImageIndex1(String imageIndex1) {
		this.imageIndex1 = imageIndex1;
	}

	public String getImageIndex2() {
		return imageIndex2;
	}

	public void setImageIndex2(String imageIndex2) {
		this.imageIndex2 = imageIndex2;
	}

	public String getImageIndex3() {
		return imageIndex3;
	}

	public void setImageIndex3(String imageIndex3) {
		this.imageIndex3 = imageIndex3;
	}

	public String getImageIndex4() {
		return imageIndex4;
	}

	public void setImageIndex4(String imageIndex4) {
		this.imageIndex4 = imageIndex4;
	}

	public String getImageIndex5() {
		return imageIndex5;
	}

	public void setImageIndex5(String imageIndex5) {
		this.imageIndex5 = imageIndex5;
	}

	public String getImageIndex6() {
		return imageIndex6;
	}

	public void setImageIndex6(String imageIndex6) {
		this.imageIndex6 = imageIndex6;
	}

	public String getImageIndex7() {
		return imageIndex7;
	}

	public void setImageIndex7(String imageIndex7) {
		this.imageIndex7 = imageIndex7;
	}

	public String getImageIndex8() {
		return imageIndex8;
	}

	public void setImageIndex8(String imageIndex8) {
		this.imageIndex8 = imageIndex8;
	}

	public String getImageFacing1() {
		return imageFacing1;
	}

	public void setImageFacing1(String imageFacing1) {
		this.imageFacing1 = imageFacing1;
	}

	public String getImageFacing2() {
		return imageFacing2;
	}

	public void setImageFacing2(String imageFacing2) {
		this.imageFacing2 = imageFacing2;
	}

	public String getImageFacing3() {
		return imageFacing3;
	}

	public void setImageFacing3(String imageFacing3) {
		this.imageFacing3 = imageFacing3;
	}

	public String getImageFacing4() {
		return imageFacing4;
	}

	public void setImageFacing4(String imageFacing4) {
		this.imageFacing4 = imageFacing4;
	}

	public String getImageFacing5() {
		return imageFacing5;
	}

	public void setImageFacing5(String imageFacing5) {
		this.imageFacing5 = imageFacing5;
	}

	public String getImageFacing6() {
		return imageFacing6;
	}

	public void setImageFacing6(String imageFacing6) {
		this.imageFacing6 = imageFacing6;
	}

	public String getImageFacing7() {
		return imageFacing7;
	}

	public void setImageFacing7(String imageFacing7) {
		this.imageFacing7 = imageFacing7;
	}

	public String getImageFacing8() {
		return imageFacing8;
	}

	public void setImageFacing8(String imageFacing8) {
		this.imageFacing8 = imageFacing8;
	}

	public String getExtractLprFlag1() {
		return extractLprFlag1;
	}

	public void setExtractLprFlag1(String extractLprFlag1) {
		this.extractLprFlag1 = extractLprFlag1;
	}

	public String getExtractLprFlag2() {
		return extractLprFlag2;
	}

	public void setExtractLprFlag2(String extractLprFlag2) {
		this.extractLprFlag2 = extractLprFlag2;
	}

	public String getExtractLprFlag3() {
		return extractLprFlag3;
	}

	public void setExtractLprFlag3(String extractLprFlag3) {
		this.extractLprFlag3 = extractLprFlag3;
	}

	public String getExtractLprFlag4() {
		return extractLprFlag4;
	}

	public void setExtractLprFlag4(String extractLprFlag4) {
		this.extractLprFlag4 = extractLprFlag4;
	}

	public String getExtractLprFlag5() {
		return extractLprFlag5;
	}

	public void setExtractLprFlag5(String extractLprFlag5) {
		this.extractLprFlag5 = extractLprFlag5;
	}

	public String getExtractLprFlag6() {
		return extractLprFlag6;
	}

	public void setExtractLprFlag6(String extractLprFlag6) {
		this.extractLprFlag6 = extractLprFlag6;
	}

	public String getExtractLprFlag7() {
		return extractLprFlag7;
	}

	public void setExtractLprFlag7(String extractLprFlag7) {
		this.extractLprFlag7 = extractLprFlag7;
	}

	public String getExtractLprFlag8() {
		return extractLprFlag8;
	}

	public void setExtractLprFlag8(String extractLprFlag8) {
		this.extractLprFlag8 = extractLprFlag8;
	}

	public Map<String, ImageDto> getListOfImages() {
		return listOfImages;
	}

	public void setListOfImages(Map<String, ImageDto> listOfImages) {
		this.listOfImages = listOfImages;
	}

	public PassageManualFeedBackMetaDataDto getPassageManualFeedBackMetaData() {
		return passageManualFeedBackMetaData;
	}

	public void setPassageManualFeedBackMetaData(PassageManualFeedBackMetaDataDto passageManualFeedBackMetaData) {
		this.passageManualFeedBackMetaData = passageManualFeedBackMetaData;
	}

	@Override
	public String toString() {
		return "QFreeRequestVO [laneTxId=" + laneTxId + ", txDate=" + txDate + ", txTimestamp=" + txTimestamp
				+ ", agencyShortName=" + agencyShortName + ", agencyId=" + agencyId + ", plazaName=" + plazaName
				+ ", plazaId=" + plazaId + ", laneId=" + laneId + ", axleCount=" + axleCount + ", imageFileIndex="
				+ imageFileIndex + ", outFileName=" + outFileName + ", procImageCnt=" + procImageCnt + ", source="
				+ source + ", priority=" + priority + ", lprType=" + lprType + ", lprStatus=" + lprStatus
				+ ", captureTimeUTC=" + captureTimeUTC + ", expirationTimeUTC=" + expirationTimeUTC
				+ ", manualPlateNumber=" + manualPlateNumber + ", manualPlateState=" + manualPlateState
				+ ", manualPlateType=" + manualPlateType + ", manualPlateCountry=" + manualPlateCountry
				+ ", manualReviewIndex=" + manualReviewIndex + ", manualRoiTop=" + manualRoiTop + ", manualRoiBottom="
				+ manualRoiBottom + ", manualRoiLeft=" + manualRoiLeft + ", manualRoiRight=" + manualRoiRight
				+ ", unmatchedEntryFlag=" + unmatchedEntryFlag + ", sectionId=" + sectionId + ", aetFlag=" + aetFlag
				+ ", imageReceiveDate=" + imageReceiveDate + ", imageTxId=" + imageTxId + ", actualClass=" + actualClass
				+ ", vehicleTypeRequired=" + vehicleTypeRequired + ", extPlazaId=" + extPlazaId + ", extLaneId="
				+ extLaneId + ", imageUrl1=" + imageUrl1 + ", imageUrl2=" + imageUrl2 + ", imageUrl3=" + imageUrl3
				+ ", imageUrl4=" + imageUrl4 + ", imageUrl5=" + imageUrl5 + ", imageUrl6=" + imageUrl6 + ", imageUrl7="
				+ imageUrl7 + ", imageUrl8=" + imageUrl8 + ", imageIndex1=" + imageIndex1 + ", imageIndex2="
				+ imageIndex2 + ", imageIndex3=" + imageIndex3 + ", imageIndex4=" + imageIndex4 + ", imageIndex5="
				+ imageIndex5 + ", imageIndex6=" + imageIndex6 + ", imageIndex7=" + imageIndex7 + ", imageIndex8="
				+ imageIndex8 + ", imageFacing1=" + imageFacing1 + ", imageFacing2=" + imageFacing2 + ", imageFacing3="
				+ imageFacing3 + ", imageFacing4=" + imageFacing4 + ", imageFacing5=" + imageFacing5 + ", imageFacing6="
				+ imageFacing6 + ", imageFacing7=" + imageFacing7 + ", imageFacing8=" + imageFacing8
				+ ", extractLprFlag1=" + extractLprFlag1 + ", extractLprFlag2=" + extractLprFlag2 + ", extractLprFlag3="
				+ extractLprFlag3 + ", extractLprFlag4=" + extractLprFlag4 + ", extractLprFlag5=" + extractLprFlag5
				+ ", extractLprFlag6=" + extractLprFlag6 + ", extractLprFlag7=" + extractLprFlag7 + ", extractLprFlag8="
				+ extractLprFlag8 + ", listOfImages=" + listOfImages + ", passageManualFeedBackMetaData="
				+ passageManualFeedBackMetaData + "]";
	}

	

	
}
