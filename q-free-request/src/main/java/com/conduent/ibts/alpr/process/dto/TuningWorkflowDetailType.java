
package com.conduent.ibts.alpr.process.dto;

import java.io.Serializable;

public class TuningWorkflowDetailType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4737727430171008391L;
	
	private TruthType truth;

	/**
	 * Gets the value of the truth property.
	 * 
	 * @return possible object is {@link TruthType }{@code >}
	 * 
	 */
	public TruthType getTruth() {
		return truth;
	}

	/**
	 * Sets the value of the truth property.
	 * 
	 * @param value allowed object is {@link TruthType
	 *              }{@code >}
	 * 
	 */
	public void setTruth(TruthType value) {
		this.truth = value;
	}

}
