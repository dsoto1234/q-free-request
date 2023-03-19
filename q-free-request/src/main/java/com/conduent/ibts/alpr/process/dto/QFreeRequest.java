package com.conduent.ibts.alpr.process.dto;

import java.time.LocalDateTime;
import java.util.List;

import cloud.intrada.common.DisputeWorkflowDetailType;
import cloud.intrada.common.MetadataItemType;
import cloud.intrada.common.PriorityEnumeration;
import cloud.intrada.common.WorkflowEnumeration;


public class QFreeRequest {

	    private String source;
	    private LocalDateTime captureTimeUTC;
	    private List<ImageType> image;
	    private List<MetadataItemType> metadataItem;
	    private TuningWorkflowDetailType tuningWorkflowDetails;
	    private DisputeWorkflowDetailType disputeWorkflowDetails;
	    private String hostPassageID;
	    private LocalDateTime expirationTimeUTC;
	    private PriorityEnumeration priority;
	    private WorkflowEnumeration workflow;
	    
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public LocalDateTime getCaptureTimeUTC() {
			return captureTimeUTC;
		}
		public void setCaptureTimeUTC(LocalDateTime captureTimeUTC) {
			this.captureTimeUTC = captureTimeUTC;
		}
		public List<ImageType> getImage() {
			return image;
		}
		public void setImage(List<ImageType> image) {
			this.image = image;
		}
		public List<MetadataItemType> getMetadataItem() {
			return metadataItem;
		}
		public void setMetadataItem(List<MetadataItemType> metadataItem) {
			this.metadataItem = metadataItem;
		}
		public TuningWorkflowDetailType getTuningWorkflowDetails() {
			return tuningWorkflowDetails;
		}
		public void setTuningWorkflowDetails(TuningWorkflowDetailType tuningWorkflowDetails) {
			this.tuningWorkflowDetails = tuningWorkflowDetails;
		}
		public DisputeWorkflowDetailType getDisputeWorkflowDetails() {
			return disputeWorkflowDetails;
		}
		public void setDisputeWorkflowDetails(DisputeWorkflowDetailType disputeWorkflowDetails) {
			this.disputeWorkflowDetails = disputeWorkflowDetails;
		}
		public String getHostPassageID() {
			return hostPassageID;
		}
		public void setHostPassageID(String hostPassageID) {
			this.hostPassageID = hostPassageID;
		}
		public LocalDateTime getExpirationTimeUTC() {
			return expirationTimeUTC;
		}
		public void setExpirationTimeUTC(LocalDateTime expirationTimeUTC) {
			this.expirationTimeUTC = expirationTimeUTC;
		}
		public PriorityEnumeration getPriority() {
			return priority;
		}
		public void setPriority(PriorityEnumeration priority) {
			this.priority = priority;
		}
		public WorkflowEnumeration getWorkflow() {
			return workflow;
		}
		public void setWorkflow(WorkflowEnumeration workflow) {
			this.workflow = workflow;
		}
	    
	    

}
