
package com.conduent.ibts.alpr.process.dto;

import java.io.Serializable;

public class VehicleType implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3060680135540474111L;
	
	
	private String make;
    private String model;
    private String color;
    
    
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}


    
}
