package edu.htwm.vsp.phonebook.rest.impl;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.htwm.vsp.phone.service.PhonebookService;
import edu.htwm.vsp.phone.service.PhoneUser;
import edu.htwm.vsp.phonebook.rest.UsersResource;

public class UsersResourceImpl implements UsersResource {

	private PhonebookService phoneService;
	
	@Override
	public Response listUsers() {
		
		List<PhoneUser> allUsers = getPhoneService().fetchAllUsers();
		
		Response r = null;
		if(allUsers == null || allUsers.isEmpty()) {
			r = Response.noContent().build();
		} else {
			r = Response.ok(allUsers).build();
		}
		
		return r;
	}

	@Override
	public Response addUser(UriInfo uriInfo, String name) {
		
		PhoneUser newUser = getPhoneService().createUser(name);
		
		UriBuilder absolutePathBuilder = uriInfo.getAbsolutePathBuilder();
		URI created = absolutePathBuilder.path(UsersResource.class, "getUser").build(newUser.getId());
		
		return Response.created(created).build();
	}

	@Override
	public Response getUser(int userID) {
		// TODO Auto-generated method stub
		return null;
	}

	public PhonebookService getPhoneService() {
		return phoneService;
	}

	public void setPhoneService(PhonebookService phoneService) {
		this.phoneService = phoneService;
	}

}
