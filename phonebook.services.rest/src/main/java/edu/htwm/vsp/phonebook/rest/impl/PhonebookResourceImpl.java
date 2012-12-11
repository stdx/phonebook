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
}
