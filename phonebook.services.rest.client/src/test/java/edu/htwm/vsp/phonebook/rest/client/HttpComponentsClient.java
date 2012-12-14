package edu.htwm.vsp.phonebook.rest.client;

import edu.htwm.vsp.phone.service.PhoneNumber;
import edu.htwm.vsp.phone.service.PhoneUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static java.net.HttpURLConnection.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.HttpURLConnection;

import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.nio.cs.StreamDecoder;

/**
 * Shows how Apache HttpClient can be used to build a simple http REST client.
 *
 * @author hol
 */
public class HttpComponentsClient extends RESTClient {

    private final String xml = "application/xml";
    private final String json = "application/json";

    @BeforeClass
    public static void setUpClass() {
    }

    @Before
    public void setUp() throws NamingException {
    }

    private PhoneUser verifyAddUser(String userName, int expectedStatusCode, String contentType) throws IOException, JAXBException, Exception {

        System.out.println("create new PhoneUser: " + userName + " ... \n");
        HttpClient client = new HttpClient();



        // -- build HTTP POST request
        PostMethod method = new PostMethod(getServiceBaseURI() + "/users");
        method.addParameter("name", userName);

        // -- determine the mime type you wish to receive here 
        method.setRequestHeader("Accept", contentType);

        int responseCode = client.executeMethod(method);

        assertEquals(expectedStatusCode, responseCode);

        String response = responseToString(method.getResponseBodyAsStream());
        System.out.println(response);
        String content = method.getResponseHeader("Content-Type").getValue();
        /* Antwort zurückgeben */
        return getUserFromResponse(response, content);

    }

    /**
     * funktioniert noch nicht
     *
     * @param expectedStatusCode
     * @throws IOException
     */
    private void verifyListAllUsers(int expectedStatusCode) throws IOException {
        System.out.println("List All Users ... \n");

        HttpClient client = new HttpClient();

        // -- build HTTP GET request
        GetMethod method = new GetMethod(getServiceBaseURI() + "/users");
        int responseCode = client.executeMethod(method);


        assertEquals(expectedStatusCode, responseCode);



    }

    /**
     *
     *
     * @param stream
     * @return
     */
    private String responseToString(InputStream stream) {
        if (stream == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        BufferedReader br = null;
        try {
            System.out.println("HTTP response body:");
            br = new BufferedReader(new InputStreamReader(stream));
            String readLine;
            while (((readLine = br.readLine()) != null)) {
                result.append(readLine);
            }
        } catch (IOException io) {
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
            }
        }

        return result.toString();
    }

    private PhoneUser verifyAddNumber(String userID, String phoneCaption, String phoneNumber,
            int expectedStatusCode, String contentType) throws IOException, JAXBException, Exception {

        System.out.println("Add PhoneNumber: caption: " + phoneCaption + ", number: "
                + phoneNumber + " for PhoneUser: " + userID + " ... \n");

        HttpClient client = new HttpClient();
        // -- build HTTP POST request
        PostMethod method = new PostMethod(getServiceBaseURI() + "/users" + "/" + userID);


        // Form-Parameter hinzufügen
        method.addParameter("caption", phoneCaption);
        method.addParameter("number", phoneNumber);

        // -- determine the mime type you wish to receive here 
        method.setRequestHeader("Accept", contentType);

        int responseCode = client.executeMethod(method);

        assertEquals(expectedStatusCode, responseCode);

        String response = responseToString(method.getResponseBodyAsStream());
        System.out.println(response);
        String content = method.getResponseHeader("Content-Type").getValue();
        /* Antwort zurückgeben */
        return getUserFromResponse(response, content);

    }

    private PhoneUser verifyGetUserInfo(String userID, int expectedStatusCode, String contentType) throws IOException, JAXBException, JAXBException, Exception {

        System.out.println("Get Info for PhoneUser with id: " + userID + " ... \n");


        HttpClient client = new HttpClient();

        // -- build HTTP GET request
        GetMethod method = new GetMethod(getServiceBaseURI() + "/users" + "/" + userID);
        // -- determine the mime type you wish to receive here 
        method.setRequestHeader("Accept", contentType);
        int responseCode = client.executeMethod(method);

        assertEquals(expectedStatusCode, responseCode);

        String response = responseToString(method.getResponseBodyAsStream());
        System.out.println(response);
        String content = method.getResponseHeader("Content-Type").getValue();
        /* Antwort zurückgeben */
        return getUserFromResponse(response, content);

    }

    private void verifyDeleteNumber(String userID, String phoneCaption, int expectedStatusCode, String contentType) throws IOException {

        System.out.println("Deleing PhoneNumber: " + phoneCaption + " of phoneUser: "
                + userID + " ...\n");

        HttpClient client = new HttpClient();
        // -- build HTTP DELETE request
        DeleteMethod method = new DeleteMethod(getServiceBaseURI() + "/users" + "/" + userID + "/numbers/" + phoneCaption);
        int responseCode = client.executeMethod(method);

        assertEquals(expectedStatusCode, responseCode);


    }

    /**
     * Example code for serializing a PhoneUser via JAXB.
     */
    private void marshalPhoneUserToXML(PhoneUser user, Writer out) throws JAXBException {

        JAXBContext jc = JAXBContext.newInstance(PhoneUser.class);
        assertNotNull(jc);
        Marshaller marshaller = jc.createMarshaller();

        assertNotNull(marshaller);

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                new Boolean(true));
        marshaller.marshal(user, out);
    }

    /**
     * Example code for serializing a PhoneUser via jackson.
     */
    private void marshalPhoneUserToJSON(PhoneUser user, Writer out) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, user);


    }

    /**
     * Example code for deserializing a PhoneUser via JAXB.
     */
    private PhoneUser unmarshalPhoneUserFromXML(Reader in) throws JAXBException {

        JAXBContext jc = JAXBContext.newInstance(PhoneUser.class);
        assertNotNull(jc);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        assertNotNull(unmarshaller);
        Object unmarshalledObj = unmarshaller.unmarshal(in);

        assertNotNull(unmarshalledObj);

        assertTrue(unmarshalledObj instanceof PhoneUser);
        return (PhoneUser) unmarshalledObj;
    }

    /**
     * Example code for deserializing a PhoneUser via jackson.
     */
    private PhoneUser unmarshalPhoneUserFromJSON(Reader in) throws Exception {
        ObjectMapper mapper = new ObjectMapper();


        return mapper.readValue(in, PhoneUser.class);
    }

    private void verifyDeleteUser(String userID, int expectedStatusCode) throws IOException {

        System.out.println("Deleting User: " + userID + " ...\n");

        HttpClient client = new HttpClient();
        // -- build HTTP DELETE request
        DeleteMethod method = new DeleteMethod(getServiceBaseURI() + "/users" + "/" + userID);
        int responseCode = client.executeMethod(method);

        assertEquals(expectedStatusCode, responseCode);
    }

    /**
     * Testet, ob sich ein User anlegen kann und anschließend als XML bzw. Json.
     * Anschließend wird getestet, dass ein User auch gelöscht werden kann
     * zurückgegeben wird.
     */
    @Test
    public void testCreateAndDeleteUserAndGetUserInfo() throws Exception {
        // Testet das Anlegen eines Nutzers
        PhoneUser userXml = verifyAddUser("dieter", HTTP_CREATED, xml);
        PhoneUser userJson = verifyAddUser("dieter", HTTP_CREATED, json);
        
        assertEquals(userXml, userJson);
        verifyAddUser("dieter", HTTP_NOT_ACCEPTABLE, "bla");

        String userId = Integer.toString(userXml.getId());

        // Testet, ob sich dieser Nutzer über GET abrufen lässt und identisch mit dem erstellten User ist
        PhoneUser fetchedUser = verifyGetUserInfo(userId, HTTP_OK, xml);
        assertEquals(userXml, fetchedUser);

        // Testet das Löschen
        verifyDeleteUser(userId, HTTP_OK);

        // erneutes Löschen wird fehlschlagen
        verifyDeleteUser(userId, HTTP_NOT_FOUND);
    }

    /**
     * Testet das Anlegen und Löschen von Telefonnummern
     */
    @Test
    public void addAndDeleteNumber() throws Exception {
        // Nutzer anlegen       
        PhoneUser userXml = verifyAddUser("dieter", HTTP_CREATED, xml);

        // zufällige Nummer anlegen
        String phoneCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        PhoneNumber newNumber = new PhoneNumber(phoneCaption, phoneNumber);

        String userId = Integer.toString(userXml.getId());

        // Nummer zu User speichern und prüfen, dass dies enthalten ist
        PhoneUser fetchedUser = verifyAddNumber(userId, phoneCaption, phoneNumber, 201, xml);
        assertThat(fetchedUser.getPhoneNumbers().contains(newNumber), is(true));


        // Diese Nummer wieder löschen
        verifyDeleteNumber(userId, phoneCaption, HTTP_OK, xml);
        // ein weiteres Löschen sollte fehlschlagen
        verifyDeleteNumber(userId, phoneCaption, HTTP_NOT_FOUND, xml);

        // Nutzer wieder löschen
        verifyDeleteUser(userId, HTTP_OK);

    }

    /**
     * Testet, ob sich ein User anlegen kann und anschließend als XML bzw. Json
     * zurückgegeben wird
     */
    @Test
    public void testCreateUser() throws Exception {
    }

    /**
     * erzeugt einen Nutzer aus der Response des Servers: wandelt XML bzw Json
     * zu Response
     *
     * @param response die Antwort als String
     * @param contentType Der Content-Typ (z.B. xml / json)
     * @return Der PhoneUser oder null
     */
    private PhoneUser getUserFromResponse(String response, String contentType) throws JAXBException, Exception {
        PhoneUser user = null;
        if (contentType.equals(xml)) {
            user = unmarshalPhoneUserFromXML(new StringReader((response)));
        } else if (contentType.equals(json)) {
            user = unmarshalPhoneUserFromJSON(new StringReader((response)));
        }
        return user;
    }
}
