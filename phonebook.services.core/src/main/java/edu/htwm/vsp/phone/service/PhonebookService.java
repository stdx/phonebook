package edu.htwm.vsp.phone.service;

import java.util.List;

/**
 * A service that manages a phone book.
 * 
 * @author std
 *
 */
public interface PhonebookService {

	/**
	 * Creates a new {@link PhoneUser} and returns the managed Entity.
	 * 
	 * @param name
	 *            The name of the new {@link PhoneUser}.
	 * 
	 * @return The new {@link PhoneUser}.
	 */
	PhoneUser createUser(String name);


	/**
	 * Fetches a {@link PhoneUser} by its assigned ID.
	 * 
	 * @param userID
	 *            The id of the {@link PhoneUser}.
	 * 
	 * @return The {@link PhoneUser} if one was found, null otherwise.
	 */
	PhoneUser findUserById(int userID);

	/**
	 * Fetches all {@link PhoneUser}.
	 * 
	 * @return All existing {@link PhoneUser}s if some exist, an empty
	 *         collection otherwise.
	 */
	List<PhoneUser> fetchAllUsers();

	/**
	 * Deletes the {@link PhoneUser} with the given id.
	 * 
	 * @param userID
	 *            The id of the {@link PhoneUser} to delete.
	 */
	void deleteUser(int userID);
}
