package com.conduent.ibts.alpr.process.dto;

public class ImageDto  {

	
	public ImageDto() {
	}
	
	public String URI;
	public String data;
	public String hostImageId;
	public String cameraId;
	public String vehicleSide;
	public String target;
	
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getHostImageId() {
		return hostImageId;
	}
	public void setHostImageId(String hostImageId) {
		this.hostImageId = hostImageId;
	}
	public String getCameraId() {
		return cameraId;
	}
	public void setCameraId(String cameraId) {
		this.cameraId = cameraId;
	}
	public String getVehicleSide() {
		return vehicleSide;
	}
	public void setVehicleSide(String vehicleSide) {
		this.vehicleSide = vehicleSide;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	@Override
	public String toString() {
		return "ImageDto [URI=" + URI + ", data=" + data + ", hostImageId=" + hostImageId + ", cameraId=" + cameraId
				+ ", vehicleSide=" + vehicleSide + ", target=" + target + "]";
	}
	
	
}
