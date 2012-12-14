package edu.htwm.vsp.phonebook.rest;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = UserRef.ELEMENT_NAME + "s")
public class UserRefList extends ArrayList<UserRef>{

	private static final long serialVersionUID = -2631453782816750646L;

	public UserRefList() {
		super();
	}
	
	public UserRefList(Collection<? extends UserRef> c) {
		super(c);
	}

	public UserRefList(int initialCapacity) {
		super(initialCapacity);
	}
	
	public static UserRefList fromPhoneUsers() {
		return null;
	}
	
	
}
