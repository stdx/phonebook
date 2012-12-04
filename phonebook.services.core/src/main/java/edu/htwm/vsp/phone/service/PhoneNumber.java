package edu.htwm.vsp.phone.service;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@XmlRootElement(name = "phone-number")
public class PhoneNumber  {

	@Id
	private String caption;
	private String number;
	@ManyToOne
	private PhoneUser user;
	

	protected PhoneNumber() {
	}

	public PhoneNumber(PhoneUser user, String caption, String number) {
		this.user = user;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
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

	public PhoneUser getUser() {
		return user;
	}

	public void setUser(PhoneUser user) {
		this.user = user;
	}
	
}
