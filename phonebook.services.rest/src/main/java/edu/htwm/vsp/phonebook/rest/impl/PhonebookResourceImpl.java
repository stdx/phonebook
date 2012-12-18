package edu.htwm.vsp.phonebook.rest.impl;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.htwm.vsp.phone.service.PhoneUser;
import edu.htwm.vsp.phone.service.PhonebookService;
import edu.htwm.vsp.phonebook.rest.PhonebookResource;

public class PhonebookResourceImpl implements PhonebookResource {

    @Context
    private PhonebookService phoneService;

    @Override
    public Response createUser(UriInfo uriInfo, String name) {
        // Falls keine Name angegeben -> Error 400 (Bad Request)
        if (name.isEmpty()) {
            return Response.status(Status.BAD_REQUEST).build();
        }


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


        // Falls caption leer -> gebe Error 400 zurück


        // setze Telefonnummer


        // Response created zurückgeben
        UriBuilder absolutePathBuilder = uriInfo.getAbsolutePathBuilder();
        URI created = absolutePathBuilder.path(PhonebookResource.class, "getUser").build("");

        return Response.created(created).entity(userById).build();
    }

    @Override
    public Response deleteUser(int userID) {
        //hole User

        // Falls der User nicht existiert -> breche ab mit Fehler-Code 404

        // lösche User

        return Response.ok().build();
    }

    @Override
    public Response deleteNumber(int userID, String caption) {
        //hole User
        PhoneUser user = null;
        // Falls der User nicht existiert -> breche ab mit Fehler-Code 404


        // Falls der User die Nummer mit der Caption nicht enthält -> Fehler 404

        // lösche Nummer



        return Response.ok().entity(user).build();
    }
}