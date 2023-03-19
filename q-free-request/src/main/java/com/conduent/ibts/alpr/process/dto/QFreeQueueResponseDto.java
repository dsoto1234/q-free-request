package com.conduent.ibts.alpr.process.dto;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

public class QFreeQueueResponseDto {

	@Expose(serialize = true, deserialize = true)
	private String cscLookUpKey;
	@Expose(serialize = true, deserialize = true)
    private String issPassageId;
	@Expose(serialize = true, deserialize = true)
    private String ortFlag;
	@Expose(serialize = true, deserialize = true)
    private String plazaId;
	@Expose(serialize = true, deserialize = true)
    private String agencyId;
	@Expose(serialize = true, deserialize = true)
    private String lprSource;
	@Expose(serialize = true, deserialize = true)
    private String lprStatus;
	@Expose(serialize = true, deserialize = true)
    private String lprType;
	@Expose(serialize = true, deserialize = true)
    private String ackStatus;
	@Expose(serialize = true, deserialize = true)
    private String threadId;
	@Expose(serialize = true, deserialize = true)
    private String outputFileType;
	@Expose(serialize = true, deserialize = true)
    private String outputFileId;
	@Expose(serialize = true, deserialize = true)
    private String imageTxId;
	@Expose(serialize = true, deserialize = true)
    private String laneTxId;
	@Expose(serialize = true, deserialize = true)
    private String unmatchedEntryFlag;
	@Expose(serialize = true, deserialize = true)
    private String updateTs;
	@Expose(serialize = true, deserialize = true)
    private String createTs;
	@Expose(serialize = true, deserialize = true)
    private String updatedBy;
	@Expose(serialize = true, deserialize = true)
    private String createdBy;
	@Expose(serialize = true, deserialize = true)
    private String lprSubtype;
	@Expose(serialize = true, deserialize = true)
    private String imageReceiveDate;
	

    public String getCscLookUpKey() {
        return cscLookUpKey;
    }

    public void setCscLookUpKey(String cscLookUpKey) {
        this.cscLookUpKey = cscLookUpKey;
    }

    public String getIssPassageId() {
        return issPassageId;
    }

    public void setIssPassageId(String issPassageId) {
        this.issPassageId = issPassageId;
    }

    public String getOrtFlag() {
        return ortFlag;
    }

    public void setOrtFlag(String ortFlag) {
        this.ortFlag = ortFlag;
    }

    public String getPlazaId() {
        return plazaId;
    }

    public void setPlazaId(String plazaId) {
        this.plazaId = plazaId;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getLprSource() {
        return lprSource;
    }

    public void setLprSource(String lprSource) {
        this.lprSource = lprSource;
    }

    public String getLprStatus() {
        return lprStatus;
    }

    public void setLprStatus(String lprStatus) {
        this.lprStatus = lprStatus;
    }

    public String getLprType() {
        return lprType;
    }

    public void setLprType(String lprType) {
        this.lprType = lprType;
    }

    public String getAckStatus() {
        return ackStatus;
    }

    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getOutputFileType() {
        return outputFileType;
    }

    public void setOutputFileType(String outputFileType) {
        this.outputFileType = outputFileType;
    }

    public String getOutputFileId() {
        return outputFileId;
    }

    public void setOutputFileId(String outputFileId) {
        this.outputFileId = outputFileId;
    }

    public String getImageTxId() {
        return imageTxId;
    }

    public void setImageTxId(String imageTxId) {
        this.imageTxId = imageTxId;
    }

    public String getLaneTxId() {
        return laneTxId;
    }

    public void setLaneTxId(String laneTxId) {
        this.laneTxId = laneTxId;
    }

    public String getUnmatchedEntryFlag() {
        return unmatchedEntryFlag;
    }

    public void setUnmatchedEntryFlag(String unmatchedEntryFlag) {
        this.unmatchedEntryFlag = unmatchedEntryFlag;
    }

    public String getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(String updateTs) {
        this.updateTs = updateTs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLprSubtype() {
        return lprSubtype;
    }

    public void setLprSubtype(String lprSubtype) {
        this.lprSubtype = lprSubtype;
    }

    public String getCreateTs() {
        return createTs;
    }

    public void setCreateTs(String createTs) {
        this.createTs = createTs;
    }

    public String getImageReceiveDate() {
        return imageReceiveDate;
    }

    public void setImageReceiveDate(String imageReceiveDate) {
        this.imageReceiveDate = imageReceiveDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "LprQueueDto [cscLookUpKey=" + cscLookUpKey + ", issPassageId=" + issPassageId + ", ortFlag=" + ortFlag
                + ", plazaId=" + plazaId + ", agencyId=" + agencyId + ", lprSource=" + lprSource + ", lprStatus="
                + lprStatus + ", lprType=" + lprType + ", ackStatus=" + ackStatus + ", threadId=" + threadId
                + ", outputFileType=" + outputFileType + ", outputFileId=" + outputFileId + ", imageTxId=" + imageTxId
                +", laneTxId=" + laneTxId + ", unmatchedEntryFlag=" + unmatchedEntryFlag
                + ", updateTs=" + updateTs + ", createTs=" + createTs + ", updatedBy=" + updatedBy + ", createdBy="
                + createdBy + ", lprSubtype=" + lprSubtype + ", imageReceiveDate=" + imageReceiveDate + "]";
    }
    
    public static QFreeQueueResponseDto dtoFromJson(String message)
	{
		
    	QFreeQueueResponseDto queueMessageDTO = null;
		Gson gson = new Gson();
		queueMessageDTO = gson.fromJson(message, QFreeQueueResponseDto.class);
		return queueMessageDTO;
 
	}
}
