package edu.htwm.vsp.phonebook.rest.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.htwm.vsp.phone.service.PhoneNumber;
import edu.htwm.vsp.phone.service.PhoneUser;
import edu.htwm.vsp.phone.service.inmemory.PhoneServiceInMemory;
import edu.htwm.vsp.phonebook.rest.PhonebookResource;
import edu.htwm.vsp.phonebook.rest.UserRef;
import edu.htwm.vsp.phonebook.rest.UserRefList;

public class UsersResourceImplTest {

    private PhonebookResourceImpl usersResource;
    private UriInfo uriInfo;
	private PhoneServiceInMemory phoneService;

    @Before
    public void prepareResourcesToTest() {
    	
    	phoneService = new PhoneServiceInMemory();
    	
        usersResource = new PhonebookResourceImpl();
        usersResource.setPhoneService(phoneService);
    }

    @Before
    public void mockUriInfo() {

        uriInfo = mock(UriInfo.class);
        final UriBuilder fromResource = UriBuilder.fromResource(PhonebookResource.class);
        when(uriInfo.getAbsolutePathBuilder()).thenAnswer(new Answer<UriBuilder>() {
            @Override
            public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
                return fromResource;
            }
        });
    }

    /**
     * Testet, ob sich ein neuer Nutzer anlegen lässt Status-Code 201 (created)
     * zurückgegeben wird die zurückgegebene URI stimmt
     *
     * Anschließend wird getestet, der neu angelegte auch existiert:Status-Code
     * 200 (OK) und ob der zurückgegene User identisch mit dem erzeugten ist
     *
     */
    @Test
    public void createSingleNewUserWorks() throws Exception {

        String expectedName = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
        Response createUserResponse = usersResource.createUser(uriInfo, expectedName);

        PhoneUser createdUser = (PhoneUser) createUserResponse.getEntity();

        // check correct response code 
        assertThat(createUserResponse.getStatus(), is(Status.CREATED.getStatusCode()));
        assertThat(createdUser.getName(), is(expectedName));

        // check location
        MultivaluedMap<String, Object> metadata = createUserResponse.getMetadata();
        URI location = (URI) metadata.getFirst("Location");
        System.out.println(location);
        assertThat(location.getPath(), containsString("users/" + createdUser.getId()));

        // fetch the user by ID and compare it with the previously created user
        Response fetchUserResponse = usersResource.getUser(createdUser.getId());
        assertThat(fetchUserResponse.getStatus(), is(Status.OK.getStatusCode()));

        PhoneUser fetchedUser = (PhoneUser) fetchUserResponse.getEntity();
        assertThat(fetchedUser, is(createdUser));
    }

    /**
     * Testet, ob ein nichtexistierender User eine Fehlermeldung 404 (NOT FOUND)
     * zurückgibt
     */
    @Test
    public void fetchNotExistingUserGives404() {
        Response fetchUserResponse = usersResource.getUser(Integer.MAX_VALUE);

        assertThat(fetchUserResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
    }

    /**
     * Testet, dass alle Nutzer ausgegeben werden können
     */
    @Test
    public void testFetchAllUsers() {

        /* erzeuge zufälligen Nutzer */
        PhoneUser randomUser = phoneService.createUser(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));

        Response fetchUserResponse = usersResource.listUsers(uriInfo);

        
        assertThat(fetchUserResponse.getStatus(), is(Status.OK.getStatusCode()));
        
        // TODO immer Generics verwenden! Warum hier nicht?
//        List allUsers = (List) fetchUserResponse.getEntity();
		List<UserRef> userRefs = ((GenericEntity<UserRefList>) fetchUserResponse.getEntity()).getEntity();
        
        // Prüfe, dass Liste nicht leer ist
        assertThat(userRefs.isEmpty(), is(false));
        
        
        // lösche anschließend Nutzer
        phoneService.deleteUser(randomUser.getId());

    }

    /**
     * Testet, dass listUsers() 204 (no content) für eine leere List zuückgibt
     */
    @Test
    public void testFetchAllUsersFromEmptyListGives204() {

        // Lösche alle User und Teste dass List leer
        phoneService.deleteAllUsers();
        assertThat(phoneService.fetchAllUsers().isEmpty(), is(true));


        // Prüfe, das listUsers StatusCode 204 zurückgibt
        Response fetchUserResponse = usersResource.listUsers(uriInfo);
        assertThat(fetchUserResponse.getStatus(), is(Status.NO_CONTENT.getStatusCode()));
        List allUsers = (List) fetchUserResponse.getEntity();
    }

    /**
     * Hilfsmethode, erzeugt einen zufälligen Nutzer
     *
     * @return
     */
    private PhoneNumber createRandomNumber() {
        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        return new PhoneNumber(phoneNumberCaption, phoneNumber);
    }

    /**
     * Methode testet, ob sich eine Telefonnummer für einen Nutzer anlegen und
     * löschen lässt
     */
    @Test
    public void testThatNewNumberisCreatedAndDeleted() {

        /* erzeuge zufälligen Nutzer */
        PhoneUser randomUser = phoneService.createUser(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
        PhoneNumber randomNumber = createRandomNumber();

        /*----------------------------------*/
        /* Nummer hinzufügen                */
        /*----------------------------------*/


        // Füge Telefonnummer hinzu und prüfe auf Status Code 201 (created)
        Response addNumberResponse = usersResource.addNumber(uriInfo, randomUser.getId(), randomNumber.getCaption(), randomNumber.getNumber());
        assertThat(addNumberResponse.getStatus(), is(Status.CREATED.getStatusCode()));

        /*
         * rufe User ab und Prüfe, ob
         * neu erzeugte Nummber enthalten ist
         */
        PhoneUser fetchedUserWithNumber = (PhoneUser) usersResource.getUser(randomUser.getId()).getEntity();
        assertThat(fetchedUserWithNumber.getPhoneNumbers().contains(randomNumber), is(true));

        /*----------------------------------*/
        /* Nummer löschen                   */
        /*----------------------------------*/

        // Prüfe dass Status-Code 200 zuückgegeben wird
        Response deleteNumberResponse = usersResource.deleteNumber(randomUser.getId(), randomNumber.getCaption());
        assertThat(deleteNumberResponse.getStatus(), is(Status.OK.getStatusCode()));

        // Prüfe, dass Nummer gelöscht wurde        
        PhoneUser userWithDeletedNumber = (PhoneUser) deleteNumberResponse.getEntity();
        assertThat(userWithDeletedNumber.getPhoneNumbers().contains(randomNumber), is(false));
    }

    /**
     * Testet, dass addNumber 404 zurückgibt, wenn kein User gefunden wurde
     */
    @Test
    public void checkAddNumberGives404() {
        // Lösche alle User
        phoneService.deleteAllUsers();

        /* erzeuge zufällige Nummer und User*/
        PhoneUser randomUser = new PhoneUser(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
        PhoneNumber randomNumber = createRandomNumber();

        /* füge Nummer zu Nutzer hinzu und prüfe, dass 404 zurückgegben wird */
        Response addNumberResponse = usersResource.addNumber(uriInfo, randomUser.getId(), randomNumber.getCaption(), randomNumber.getNumber());
        assertThat(addNumberResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
    }

    /**
     * Testet, dass deleteNumber 404 zurückgibt, wenn kein User gefunden wurde
     * bzw. zu einem User die PhoneNumber nicht existiert
     */
    @Test
    public void checkDeleteNumberGives404() {

        /* erzeuge zufällige Nummer und User*/
        PhoneUser randomUser = phoneService.createUser(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
        PhoneNumber randomNumber = createRandomNumber();

        /**
         * prüfen, dass 404 zurückgegeben wird, da User keine Nummer hat
         */
        Response deleteNumberResponse = usersResource.deleteNumber(randomUser.getId(), randomNumber.getCaption());
        assertThat(deleteNumberResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));

        // Lösche alle User
        phoneService.deleteAllUsers();

        /**
         * prüfen, dass 404 zurückgegeben wird, da User nicht gefunden wurde
         */
        deleteNumberResponse = usersResource.deleteNumber(randomUser.getId(), randomNumber.getCaption());
        assertThat(deleteNumberResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));

    }

    /**
     * Testet, dass sich ein exisitierender User löschen lässt, und 404
     * zurückgibt, falls dieser nicht gefunden wurde
     */
    @Test
    public void checkDeleteUser() {

        PhoneUser randomUser = phoneService.createUser(RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));

        /**
         * prüfen, dass Status-Code 200 zuückgegeben wurde und PhoneUser
         * tatsächlich gelöscht wurde
         */
        Response deleteUserResponse = usersResource.deleteUser(randomUser.getId());
        assertThat(deleteUserResponse.getStatus(), is(Status.OK.getStatusCode()));
        assertThat(phoneService.fetchAllUsers().contains(randomUser), is(false));

        /**
         * prüfen, dass erneutes Löschen fehlschlägt (da User bereits gelöscht
         * wurde)
         */
        deleteUserResponse = usersResource.deleteUser(randomUser.getId());
        assertThat(deleteUserResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));

    }
}
