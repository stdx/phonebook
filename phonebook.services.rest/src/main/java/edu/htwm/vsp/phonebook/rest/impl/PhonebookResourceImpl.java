package edu.htwm.vsp.phonebook.rest.impl;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import edu.htwm.vsp.phone.service.PhoneNumber;
import edu.htwm.vsp.phone.service.PhoneUser;
import edu.htwm.vsp.phone.service.PhonebookService;
import edu.htwm.vsp.phonebook.rest.PhonebookResource;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

public class PhonebookResourceImpl implements PhonebookResource {

    @Context
    private PhonebookService phoneService;

    @Override
    public Response listUsers() {

        List<PhoneUser> allUsersFromDB = getPhoneService().fetchAllUsers();
        List<PhoneUser> allUsers = new LinkedList<PhoneUser>(allUsersFromDB);
        Response r = null;
        if (allUsers == null || allUsers.isEmpty()) {
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
        URI created = absolutePathBuilder.path(PhonebookResource.class, "getUser").build(newUser.getId());

        return Response.created(created).entity(newUser).build();
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
        URI created = absolutePathBuilder.path(PhonebookResource.class, "getUser").build();

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

        return Response.ok("Delete User with id: " + userID).entity(userById).build();
    }
}
