
package com.conduent.ibts.alpr.process.dto;

import java.io.Serializable;


public class IdentificationType implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2455990956770090563L;
	
	private String registration;
    private String jurisdiction;
    private String plateType;
    private VehicleType vehicle;
    
	public String getRegistration() {
		return registration;
	}
	public void setRegistration(String registration) {
		this.registration = registration;
	}
	public String getJurisdiction() {
		return jurisdiction;
	}
	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}
	public String getPlateType() {
		return plateType;
	}
	public void setPlateType(String plateType) {
		this.plateType = plateType;
	}
	public VehicleType getVehicle() {
		return vehicle;
	}
	public void setVehicle(VehicleType vehicle) {
		this.vehicle = vehicle;
	}
	

    
}
