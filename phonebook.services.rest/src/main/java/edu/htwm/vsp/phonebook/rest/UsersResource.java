package edu.htwm.vsp.phonebook.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import edu.htwm.vsp.phone.service.PhoneUser;

@Path("users")
public interface UsersResource {

	public static final String USER_ID = "id";
	
	/**
	 * Creates a new {@link PhoneUser} with the given name.
	 * 
	 * @param uriInfo
	 *            Injected by JAX-RS. Used for building the correct path to the
	 *            newly created resource.
	 * @param name
	 *            The name of the new user.
	 * 
	 * @return 201 CREATED and the path for accessing the new user
	 */
	@POST
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	Response createUser(
		@Context UriInfo uriInfo,
		@FormParam("name") String name);
	
	/**
	 * Fetches an existing user by its id.
	 * 
	 * @param userID
	 *            The id of the user to fetch.
	 * @return 200 - and the found user if one exists, an appropriate HTTP status
	 *         code else.
	 */
	@GET
	@Path("{" + USER_ID + "}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	Response getUser(
		@QueryParam(USER_ID) int userID);
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	Response listUsers();
	
}
