package edu.htwm.vsp.phone.service.inmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.htwm.vsp.phone.service.PhoneUser;
import edu.htwm.vsp.phone.service.PhonebookService;

/**
 * 
 * This service mimics the behaviour of managed entities by returning direct
 * references of the stored {@link PhoneUser}s. This class is NOT threadsafe and
 * especially not suited for web environments.
 * 
 * @author std
 * 
 */
public class PhoneServiceInMemory implements PhonebookService {

	private final HashMap<Integer, PhoneUser> users;
	private int lastID = 0;

	public PhoneServiceInMemory() {
		users = new HashMap<Integer, PhoneUser>();
	}

	@Override
	public PhoneUser createUser(String name) {
		
		PhoneUser phoneUser = new PhoneUser(name);
		phoneUser.setId(++lastID);

		users.put(phoneUser.getId(), phoneUser);

		return phoneUser;
	}

	@Override
	public PhoneUser findUserById(int userID) {
		return users.containsKey(userID) ? users.get(userID) : null;
	}

	@Override
	public List<PhoneUser> fetchAllUsers() {
		return new ArrayList<PhoneUser>(users.values());
	}

	@Override
	public void deleteUser(int userID) {
		users.remove(userID);
	}

}
