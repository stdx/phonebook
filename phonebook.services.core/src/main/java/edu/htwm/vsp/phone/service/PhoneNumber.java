package edu.htwm.vsp.phone.service;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * A simple entity for storing telephone numbers. A {@link PhoneNumber} contains
 * the number with an arbitrary format and a caption defining the context.
 * 
 * @author std
 * 
 */
@Entity
@XmlRootElement(name="number")
public class PhoneNumber  {

	@Id
	private String caption;
	private String number;
	

	protected PhoneNumber() {
	}

	public PhoneNumber(String caption, String number) {
		this.caption = caption;
		this.number = number;
	}

	public PhoneNumber(PhoneNumber phoneNumber) {
		this(phoneNumber.getCaption(), phoneNumber.getNumber());
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

	
	
	@Override
	public String toString() {
		return "PhoneNumber [caption=" + caption + ", number=" + number + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PhoneNumber other = (PhoneNumber) obj;
		if (caption == null) {
			if (other.caption != null)
				return false;
		} else if (!caption.equals(other.caption))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}


}
