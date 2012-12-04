package edu.htwm.vsp.phone.service;

import java.util.List;


public interface PhoneService {
	
	/**
	 * 
	 * Creates a new {@link PhoneUser} and returns the managed Entity.
	 * 
	 * @param name
	 *            The name of the new {@link PhoneUser}.
	 * @return The new {@link PhoneUser}.
	 */
	PhoneUser createUser(String name);

	PhoneUser findUserByName(String name) throws NotFoundException;

	/**
	 * Fetches a {@link PhoneUser} by its assigned ID.
	 * 
	 * @param userID
	 *            The id of the {@link PhoneUser}.
	 * @return The {@link PhoneUser} if one was found, null otherwise.
	 */
	PhoneUser findUserById(int userID);

	List<PhoneUser> fetchAllUsers();

	void deleteUser(int userID);
}
