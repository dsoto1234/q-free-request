
package com.conduent.ibts.alpr.process.dto;

import java.io.Serializable;

public class ImageOfRecordType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8114099818469978660L;
	
	private String value;
	private Integer roiTop;
	private Integer roiBottom;
	private Integer roiLeft;
	private Integer roiRight;

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the value of the roiTop property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getRoiTop() {
		return roiTop;
	}

	/**
	 * Sets the value of the roiTop property.
	 * 
	 * @param value allowed object is {@link Integer }
	 * 
	 */
	public void setRoiTop(Integer roiTop) {
		this.roiTop = roiTop;
	}

	/**
	 * Gets the value of the roiBottom property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getRoiBottom() {
		return roiBottom;
	}

	/**
	 * Sets the value of the roiBottom property.
	 * 
	 * @param value allowed object is {@link Integer }
	 * 
	 */
	public void setRoiBottom(Integer roiBottom) {
		this.roiBottom = roiBottom;
	}

	/**
	 * Gets the value of the roiLeft property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getRoiLeft() {
		return roiLeft;
	}

	/**
	 * Sets the value of the roiLeft property.
	 * 
	 * @param value allowed object is {@link Integer }
	 * 
	 */
	public void setRoiLeft(Integer roiLeft) {
		this.roiLeft = roiLeft;
	}

	/**
	 * Gets the value of the roiRight property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getRoiRight() {
		return roiRight;
	}

	/**
	 * Sets the value of the roiRight property.
	 * 
	 * @param value allowed object is {@link Integer }
	 * 
	 */
	public void setRoiRight(Integer roiRight) {
		this.roiRight = roiRight;
	}

}
