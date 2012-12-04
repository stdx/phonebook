package edu.htwm.vsp.phone.service;

import javax.persistence.Entity;

/**
 * 
 * A simple entity for storing telephone numbers. A {@link PhoneNumber} contains
 * the number with an arbitrary format and a caption defining the context.
 * 
 * @author std
 * 
 */
@Entity
public class PhoneNumber extends BaseEntity {

	private String number;
	private String caption;

	protected PhoneNumber() {
	}

	public PhoneNumber(String caption, String number) {
		this.caption = caption;
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

}
