
package com.conduent.ibts.alpr.process.dto;

import java.io.Serializable;
import java.util.List;

public class TruthType implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3343448212271729112L;
	
	private String registration;
	private String jurisdiction;
	private String plateType;
	private ImageOfRecordType imageOfRecord;

	/**
	 * Gets the value of the registration property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRegistration() {
		return registration;
	}

	/**
	 * Sets the value of the registration property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setRegistration(String value) {
		this.registration = value;
	}

	/**
	 * Gets the value of the jurisdiction property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getJurisdiction() {
		return jurisdiction;
	}

	/**
	 * Sets the value of the jurisdiction property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setJurisdiction(String value) {
		this.jurisdiction = value;
	}

	/**
	 * Gets the value of the plateType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPlateType() {
		return plateType;
	}

	/**
	 * Sets the value of the plateType property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setPlateType(String value) {
		this.plateType = value;
	}

	/**
	 * Gets the value of the imageOfRecord property.
	 * 
	 * @return possible object is {@link List
	 *         }{@code <}{@link ImageOfRecordType }{@code >}
	 * 
	 */
	public ImageOfRecordType getImageOfRecord() {
		return imageOfRecord;
	}

	/**
	 * Sets the value of the imageOfRecord property.
	 * 
	 * @param value allowed object is {@link List
	 *              }{@code <}{@link ImageOfRecordType }{@code >}
	 * 
	 */
	public void setImageOfRecord(ImageOfRecordType value) {
		this.imageOfRecord = value;
	}

}
