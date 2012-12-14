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
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;

@Path("users")
public interface PhonebookResource {

    public static final String USER_ID_PARAM = "id";
    public static final String PHONE_CAPTION = "caption";

    /**
     * Creates a new {@link PhoneUser} with the given name.
     *
     * @param uriInfo Injected by JAX-RS. Used for building the correct path to
     * the newly created resource.
     * @param name The name of the user to create.
     *
     * @return 201 CREATED and the path for accessing the new user, an
     * appropriate status code otherwise.
     */
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response createUser(
            @Context UriInfo uriInfo,
            @FormParam("name") String name);

    /**
     * Fetches an existing user by its id.
     *
     * @param userID The id of the user to fetch.
     * @return 200 - and the found user if one exists, an appropriate HTTP
     * status code else.
     */
    @GET
    @Path("{" + USER_ID_PARAM + "}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response getUser(
            @PathParam(USER_ID_PARAM) int userID);

    /**
     * Methode fügt eine Telefonnummer zu einem User hinzu. Dazu erhält sie als
     * Parameter caption und number.
     *
     * Aufruf: POST: http://localhost:8080/users/{user_id} - Soll XML oder JSON
     * zurückliefern - erhält Caption und Number als Form-Parameter - userID
     * soll als Path-Parameter übergeben werden
     *
     * @return
     */
    Response addNumber(
            @Context UriInfo uriInfo,
            int userID,
            String caption,
            String number);

    /**
     * Methode löscht eine Telefonnummer eines Users und erhält dazu die
     * captions
     *
     * Aufruf: DELETE: http://localhost:8080/users/{user_id}/numbers/{caption}
     *
     * - erhält Caption und userID als Path-Parameter -
     *
     * @return
     */
    Response deleteNumber(
            int userID,
            String caption);

    /**
     * Methode liefert eine Liste aller PhoneUser zurück
     *
     * Aufruf: GET: http://localhost:8080/users - Soll XML oder JSON
     * zurückliefern
     *
     * @return
     */
    Response listUsers();

    /**
     * Methode löscht einen vorhandenen User
     *
     * Aufruf: DELETE: http://localhost:8080/users/{user_id}/
     *
     * - erhält userID als Path-Parameter -
     *
     * @return
     */
    Response deleteUser(
            int userID);
}
