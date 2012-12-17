package edu.htwm.vsp.phonebook.rest.impl;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.htwm.vsp.phone.service.PhoneUser;
import edu.htwm.vsp.phone.service.PhonebookService;
import edu.htwm.vsp.phonebook.rest.PhonebookResource;
import edu.htwm.vsp.phonebook.rest.UserRef;
import edu.htwm.vsp.phonebook.rest.UserRefList;

public class PhonebookResourceImpl implements PhonebookResource {

    @Context
    private PhonebookService phoneService;

    @Override
    public Response listUsers(UriInfo uriInfo) {

        List<PhoneUser> allUsersFromDB = getPhoneService().fetchAllUsers();
        
        Response r = null;
        if (allUsersFromDB == null || allUsersFromDB.isEmpty()) { // return nothing
            r = Response.noContent().build();
        
        } else { // return the links to the known users
        	List<UserRef> userRefs = toRefs(uriInfo, allUsersFromDB);
        	GenericEntity<List<UserRef>> usersToReturn = new GenericEntity<List<UserRef>>(userRefs){};
        	r = Response.ok(usersToReturn).build();
        }
        
        return r;
    }

    private List<UserRef> toRefs(UriInfo uriInfo, List<PhoneUser> allUsersFromDB) {
    	UserRefList userRefList = new UserRefList(allUsersFromDB.size());
    	for(PhoneUser user : allUsersFromDB)
    		userRefList.add(UserRef.fromUser(user, toUri(uriInfo, user)));
    	return userRefList;
	}

	@Override
    public Response createUser(UriInfo uriInfo, String name) {
        PhoneUser newUser = getPhoneService().createUser(name);
        Response createdResponse = Response.created(toUri(uriInfo, newUser)).entity(newUser).build(); 
        return createdResponse;
    }
	
	private URI toUri(UriInfo uriInfo, PhoneUser user) {
		UriBuilder absolutePathBuilder = uriInfo.getAbsolutePathBuilder();
        URI userUri = absolutePathBuilder.path(PhonebookResource.class, "getUser").build(user.getId());
        return userUri;
	}
	

    @Override
    public Response getUser(int userID) {

        PhoneUser userById = phoneService.findUserById(userID);

        Response r = null;
        if (userById == null) {
            r = Response.status(Status.NOT_FOUND).build();
        } else {
            r = Response.ok(userById).build();
        }
        return r;
    }

    public PhonebookService getPhoneService() {
        return phoneService;
    }

    public void setPhoneService(PhonebookService phoneService) {
        this.phoneService = phoneService;
    }

    @Override
    public Response addNumber(UriInfo uriInfo, int userID, String caption, String number) {

        PhoneUser userById = phoneService.findUserById(userID);

        // Falls der User nicht existiert -> breche ab mit Fehler-Code 404
        if (userById == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        // setze Telefonnummer
        userById.setNumber(caption, number);

        UriBuilder absolutePathBuilder = uriInfo.getAbsolutePathBuilder();
        URI created = absolutePathBuilder.path(PhonebookResource.class, "getUser").build("");

        return Response.created(created).entity(userById).build();
    }

    @Override
    public Response deleteUser(int userID) {
        PhoneUser userById = phoneService.findUserById(userID);

        // Falls der User nicht existiert -> breche ab mit Fehler-Code 404
        if (userById == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        phoneService.deleteUser(userById.getId());

        return Response.ok().build();
    }

    @Override
    public Response deleteNumber(int userID, String caption) {
        PhoneUser userById = phoneService.findUserById(userID);

        // Falls der User nicht existiert -> breche ab mit Fehler-Code 404
        if (userById == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        // Falls der User die Nummer mit der Caption nicht enthält -> Fehler 404
        if (!userById.containsNumberWithCaption(caption)) {
            return Response.status(Status.NOT_FOUND).build();
        }


        userById.deleteNumber(caption);

        return Response.ok("number deleted").entity(userById).build();
    }
}
