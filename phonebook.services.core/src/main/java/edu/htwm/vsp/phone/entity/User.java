package edu.htwm.vsp.phone.entity;

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
public class User extends BaseEntity {
	
	private String name;
	
	@OneToMany(
		cascade = CascadeType.ALL)
	private List<PhoneNumber> phoneNumbers;
	
	protected User() {
		
	}
	
	public User(String name) {
		this.name = name;
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
}
