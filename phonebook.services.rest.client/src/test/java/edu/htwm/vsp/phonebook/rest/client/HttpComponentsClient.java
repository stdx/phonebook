package edu.htwm.vsp.phonebook.rest.client;

import edu.htwm.vsp.phone.service.PhoneNumber;
import edu.htwm.vsp.phone.service.PhoneUser;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;
import org.apache.commons.lang.RandomStringUtils;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Klasse zum Testen der RESTful Methoden
 *
 * @author Alexander Dopleb
 */
public class HttpComponentsClient extends RESTClient {

    /**
     * Testet, ob sich ein User anlegen kann und danach abgerufen werden kann.
     * Anschließend wird getestet, dass ein User auch gelöscht wird.
     */
    @Test
    public void testCreateAndDeleteUserAndGetUserInfo() throws Exception {

        // Testet das Anlegen eines Nutzers
        PhoneUser user = new PhoneUser("dieter");
        PhoneUser fetchedUser = verifyAddUser(user.getName(), HTTP_CREATED, xml);

        assertEquals(user.getName(), fetchedUser.getName());

        String userId = Integer.toString(fetchedUser.getId());

        // Testet, ob sich dieser Nutzer über GET abrufen lässt und identisch mit dem erstellten User ist
        fetchedUser = verifyGetUserInfo(userId, HTTP_OK, xml);
        assertEquals(user.getName(), fetchedUser.getName());

        // Testet das Löschen
        verifyDeleteUser(userId, HTTP_OK);

        // User sollte danach nicht gefunden werden
        fetchedUser = verifyGetUserInfo(userId, HTTP_NOT_FOUND, xml);

        // erneutes Löschen wird fehlschlagen
        verifyDeleteUser(userId, HTTP_NOT_FOUND);
    }

    /**
     * Testet das Anlegen und Löschen von Telefonnummern
     */
    @Test
    public void addAndDeleteNumber() throws Exception {
        // Nutzer anlegen       
        PhoneUser user = new PhoneUser("dieter");
        PhoneUser fetchedUser = verifyAddUser(user.getName(), HTTP_CREATED, xml);

        // zufällige Nummer anlegen
        String phoneCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        PhoneNumber newNumber = new PhoneNumber(phoneCaption, phoneNumber);
        user.setNumber(phoneCaption, phoneNumber);

        String userId = Integer.toString(fetchedUser.getId());

        // Nummer zu User speichern und prüfen, dass dies enthalten ist
        fetchedUser = verifyAddNumber(userId, phoneCaption, phoneNumber, 201, xml);
        assertThat(fetchedUser.getPhoneNumbers().contains(newNumber), is(true));
        assertEquals(user.getPhoneNumbers(), fetchedUser.getPhoneNumbers());

        // Diese Nummer wieder löschen
        verifyDeleteNumber(userId, phoneCaption, HTTP_OK, xml);
        // ein weiteres Löschen sollte fehlschlagen
        verifyDeleteNumber(userId, phoneCaption, HTTP_NOT_FOUND, xml);

        // Nutzer wieder löschen
        verifyDeleteUser(userId, HTTP_OK);
    }
}
