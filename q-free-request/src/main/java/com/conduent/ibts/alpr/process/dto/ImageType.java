
package com.conduent.ibts.alpr.process.dto;

import java.io.Serializable;
import java.util.List;

import cloud.intrada.common.MetadataItemType;
import cloud.intrada.common.VehicleSideEnumeration;

public class ImageType implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9020751893643275133L;
	
	private String uri;
	private byte[] data;
	private List<MetadataItemType> metadataItem;
	private String hostImageID;
	private String issImageID;
	private String cameraID;
	private VehicleSideEnumeration vehicleSide;
	
	
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public List<MetadataItemType> getMetadataItem() {
		return metadataItem;
	}
	public void setMetadataItem(List<MetadataItemType> metadataItem) {
		this.metadataItem = metadataItem;
	}
	public String getHostImageID() {
		return hostImageID;
	}
	public void setHostImageID(String hostImageID) {
		this.hostImageID = hostImageID;
	}
	public String getIssImageID() {
		return issImageID;
	}
	public void setIssImageID(String issImageID) {
		this.issImageID = issImageID;
	}
	public String getCameraID() {
		return cameraID;
	}
	public void setCameraID(String cameraID) {
		this.cameraID = cameraID;
	}
	public VehicleSideEnumeration getVehicleSide() {
		return vehicleSide;
	}
	public void setVehicleSide(VehicleSideEnumeration vehicleSide) {
		this.vehicleSide = vehicleSide;
	}
	
	

	
}
