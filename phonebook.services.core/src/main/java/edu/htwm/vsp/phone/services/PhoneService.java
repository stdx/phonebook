package edu.htwm.vsp.phone.services;

import java.util.List;

import edu.htwm.vsp.phone.entity.User;

public interface PhoneService {
	
	/**
	 * 
	 * Creates a new {@link User} and returns the managed Entity.
	 * 
	 * @param name
	 *            The name of the new {@link User}.
	 * @return The new {@link User}.
	 */
	User createUser(String name);

	User findUserByName(String name) throws NotFoundException;

	/**
	 * Fetches a {@link User} by its assigned ID.
	 * 
	 * @param userID
	 *            The id of the {@link User}.
	 * @return The {@link User} if one was found, null otherwise.
	 */
	User findUserById(int userID);

	List<User> fetchAllUsers();
}
