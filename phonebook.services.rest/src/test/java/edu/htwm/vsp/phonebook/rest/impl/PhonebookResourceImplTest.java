package edu.htwm.vsp.phonebook.rest.impl;

import edu.htwm.vsp.phone.service.PhoneNumber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.htwm.vsp.phone.service.PhoneUser;
import edu.htwm.vsp.phone.service.PhonebookService;
import edu.htwm.vsp.phone.service.inmemory.PhoneServiceInMemory;
import edu.htwm.vsp.phonebook.rest.PhonebookResource;


/**
 * Testet die Funktionalität der REST-Methoden unabhängig vom Web-Server
 * (Whitebox-Testing) Das Controller-Objekt phonebookService wird manuell
 * instanziert (als neuer PhoneServiceInMemory) und der Instanz von
 * PhonebookResourcImpl übergeben
 *
 * @author adopleb
 */
public class PhonebookResourceImplTest {

    private PhonebookResourceImpl phonebookResource;
    private UriInfo uriInfo;
    private PhonebookService phoneService;

    /**
     * initialisiert die Komponenten
     */
    @Before
    public void prepareResourcesToTest() {
        phoneService = new PhoneServiceInMemory();
        phonebookResource = new PhonebookResourceImpl();
        phonebookResource.setPhoneService(phoneService);

    }

    /**
     * Hilfsmethode, die die URI-Informationen bereitstellt als Mock-Objekt
     * (Wird sonst vom Web-Server instanziiert)
     */
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
     * @throws IllegalArgumentException
     * @throws UriBuilderException
     * @throws URISyntaxException
     * @throws MalformedURLException
     */
    @Test
    public void createSingleNewUserWorks() throws IllegalArgumentException, UriBuilderException, URISyntaxException, MalformedURLException {



        String expectedName = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
        Response createUserResponse = phonebookResource.createUser(uriInfo, expectedName);

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
        Response fetchUserResponse = phonebookResource.getUser(createdUser.getId());
        assertThat(fetchUserResponse.getStatus(), is(Status.OK.getStatusCode()));

        PhoneUser fetchedUser = (PhoneUser) fetchUserResponse.getEntity();
        assertThat(fetchedUser, is(createdUser));
    }

    /**
     * Testet, ob das Anlegen eines Nutzer ohne Namen fehlschlägt *
     *
     */
    @Test
    public void createSingleUserFails() {

        String name = "";
        Response createUserResponse = phonebookResource.createUser(uriInfo, name);
        assertThat(createUserResponse.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));

    }

    /**
     * Testet, ob ein nichtexistierender User eine Fehlermeldung 404 (NOT FOUND)
     * zurückgibt
     */
    @Test
    public void fetchNotExistingUserGives404() {
        Response fetchUserResponse = phonebookResource.getUser(Integer.MAX_VALUE);

        assertThat(fetchUserResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
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
        Response addNumberResponse = phonebookResource.addNumber(uriInfo, randomUser.getId(), randomNumber.getCaption(), randomNumber.getNumber());
        assertThat(addNumberResponse.getStatus(), is(Status.CREATED.getStatusCode()));

        /*
         * rufe User ab und Prüfe, ob
         * neu erzeugte Nummber enthalten ist
         */
        PhoneUser fetchedUserWithNumber = (PhoneUser) phonebookResource.getUser(randomUser.getId()).getEntity();
        assertThat(fetchedUserWithNumber.getPhoneNumbers().contains(randomNumber), is(true));

        /*----------------------------------*/
        /* Nummer löschen                   */
        /*----------------------------------*/

        // Prüfe dass Status-Code 200 zuückgegeben wird
        Response deleteNumberResponse = phonebookResource.deleteNumber(randomUser.getId(), randomNumber.getCaption());
        assertThat(deleteNumberResponse.getStatus(), is(Status.OK.getStatusCode()));

        // Prüfe, dass Nummer gelöscht wurde        
        PhoneUser userWithDeletedNumber = (PhoneUser) deleteNumberResponse.getEntity();
        assertThat(userWithDeletedNumber.getPhoneNumbers().contains(randomNumber), is(false));
    }

    /**
     * Testet, dass addNumber 404 zurückgibt, wenn kein User gefunden wurde oder
     * caption leer ist
     */
    @Test
    public void checkAddNumberGivesError() {
        // Lösche alle User
        phoneService.deleteAllUsers();

        /* erzeuge zufällige Daten*/
        // zufälliger Nutzer wird erzeugt, aber nicht 
        int randomID = RandomUtils.nextInt(10) + 13;
        String phoneCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(10);

        PhoneNumber randomNumber = createRandomNumber();

        /* füge Nummer zu Nutzer hinzu und prüfe, dass 404 zurückgegben wird */
        Response addNumberResponse =
                phonebookResource.addNumber(uriInfo, randomID, phoneCaption, phoneNumber);
        assertThat(addNumberResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));

        /* füge Nutzer hinzu und teste, dass addNumber() mit leerer caption  fehlschlägt */
        PhoneUser user = phoneService.createUser(RandomStringUtils.randomAlphanumeric(10));
        addNumberResponse =
                phonebookResource.addNumber(uriInfo, user.getId(), "", "");
        assertThat(addNumberResponse.getStatus(), is(Status.BAD_REQUEST.getStatusCode()));

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
        Response deleteNumberResponse = phonebookResource.deleteNumber(randomUser.getId(), randomNumber.getCaption());
        assertThat(deleteNumberResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));

        // Lösche alle User
        phoneService.deleteAllUsers();

        /**
         * prüfen, dass 404 zurückgegeben wird, da User nicht gefunden wurde
         */
        deleteNumberResponse = phonebookResource.deleteNumber(randomUser.getId(), randomNumber.getCaption());
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
        Response deleteUserResponse = phonebookResource.deleteUser(randomUser.getId());
        assertThat(deleteUserResponse.getStatus(), is(Status.OK.getStatusCode()));
        assertThat(phoneService.fetchAllUsers().contains(randomUser), is(false));

        /**
         * prüfen, dass erneutes Löschen fehlschlägt (da User bereits gelöscht
         * wurde)
         */
        deleteUserResponse = phonebookResource.deleteUser(randomUser.getId());
        assertThat(deleteUserResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));

    }
}
