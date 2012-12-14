package edu.htwm.vsp.phonebook.rest;

import java.net.URI;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import edu.htwm.vsp.phone.service.PhoneUser;

@XmlRootElement(name = UserRef.ELEMENT_NAME)
public class UserRef {
	
	public static final String 
		ELEMENT_NAME = "user-ref";
	
	private int id;
	private URI href;
	
	@XmlValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@XmlAttribute
	public URI getHref() {
		return href;
	}
	public void setHref(URI href) {
		this.href = href;
	}
	
	public static UserRef fromUser(PhoneUser phoneUser, URI uri) {
		
		UserRef userRef = new UserRef();
		userRef.setId(phoneUser.getId());
		userRef.setHref(uri);
		
		return userRef;
	}
}
