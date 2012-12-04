package edu.htwm.vsp.phonebook.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import edu.htwm.vsp.phone.entity.User;

@Path("users")
public interface UsersResource {

	/**
	 * 
	 * Creates a new {@link User} with the given name.
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
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	Response addUser(
		@Context UriInfo uriInfo,
		@FormParam("name") String name);
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	Response getUser(int userID);
	
	Response listUsers();
	
}
