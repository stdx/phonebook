package edu.htwm.vsp.phonebook.rest.impl;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import edu.htwm.vsp.phone.service.PhoneUser;
import edu.htwm.vsp.phone.service.PhonebookService;
import edu.htwm.vsp.phonebook.rest.UsersResource;

@RequestScoped
public class UsersResourceImpl<E> implements UsersResource {

	@Inject
	private PhonebookService phoneService;
	
	@Override
	public Response listUsers() {
		
		List<PhoneUser> allUsersFromDB = getPhoneService().fetchAllUsers();
		List<PhoneUser> allUsers = new LinkedList<PhoneUser>(allUsersFromDB);
		Response r = null;
		if(allUsers == null || allUsers.isEmpty()) {
			r = Response.noContent().build();
		} else {
			r = Response.ok(allUsers).build();
		}
		
		return r;
	}

	@Override
	public Response createUser(UriInfo uriInfo, String name) {
		
		PhoneUser newUser = getPhoneService().createUser(name);
		
		UriBuilder absolutePathBuilder = uriInfo.getAbsolutePathBuilder();
		URI created = absolutePathBuilder.path(UsersResource.class, "getUser").build(newUser.getId());
		
		return Response.created(created).build();
	}

	@Override
	public Response getUser(int userID) {
		PhoneUser userById = phoneService.findUserById(userID);
		
		Response r = null;
		if(userById == null)
			r = Response.status(Status.NOT_FOUND).build();
		else
			r = Response.ok(userById).build();
		return r;
	}

	public PhonebookService getPhoneService() {
		return phoneService;
	}

	public void setPhoneService(PhonebookService phoneService) {
		this.phoneService = phoneService;
	}

}
