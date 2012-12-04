package edu.htwm.vsp.phone.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 * 
 * A user has many {@link PhoneNumber}s.
 * 
 * @author std
 *
 */
@Entity
public class PhoneUser extends BaseEntity {
	
	private String name;
	
	@OneToMany(
		cascade = CascadeType.ALL)
	private List<PhoneNumber> phoneNumbers;
	
	public PhoneUser() {
		this("INVALID_USER");
	}
	
	public PhoneUser(String name) {
		this.name = name;
		phoneNumbers = new ArrayList<PhoneNumber>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PhoneNumber> getPhoneNumbers() {
		if(phoneNumbers == null)
			this.phoneNumbers = new ArrayList<PhoneNumber>();
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PhoneUser other = (PhoneUser) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
	
}
