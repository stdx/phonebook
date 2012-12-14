package edu.htwm.vsp.phonebook.rest.impl;

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

        // hole alle User
        List<PhoneUser> allUsers = null;

        // falls leer -> gebe StatusCode (NO CONTENT) 


        // gebe Liste zurück
        return Response.ok(allUsers).build();


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

        // hole User
        PhoneUser user = null;

        // Falls der User nicht existiert -> gebe Fehler-Code 404 zurück


        // setze Telefonnummer


        // gebe User zurück
        UriBuilder absolutePathBuilder = uriInfo.getAbsolutePathBuilder();
        URI created = absolutePathBuilder.path(PhonebookResource.class, "getUser").build("");
        return Response.created(created).entity(user).build();
    }

    @Override
    public Response deleteUser(int userID) {
        // hole User
        PhoneUser user = null;

        // Falls der User nicht existiert -> gebe Fehler-Code 404 zurück

        // lösche User

        // gebe OK zurück
        return Response.ok().build();
    }

    @Override
    public Response deleteNumber(int userID, String caption) {
        // hole User
        PhoneUser user = null;

        // Falls der User nicht existiert -> gebe Fehler-Code 404 zurück

        // Falls der User die Nummer mit der Caption nicht enthält -> Fehler 404

        // lösche Nummer

        // gebe OK zurück
        return Response.ok().entity(user).build();
    }
}
