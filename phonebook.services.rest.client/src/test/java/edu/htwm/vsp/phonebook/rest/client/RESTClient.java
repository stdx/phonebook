package edu.htwm.vsp.phonebook.rest.client;

import edu.htwm.vsp.phone.service.PhoneUser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public abstract class RESTClient {

    private static final String _serviceBaseURI = "http://localhost:8080";
    private final String serviceBaseURI;
    protected final String xml = "application/xml";
    protected final String json = "application/json";

    public RESTClient() {
        this(_serviceBaseURI);
    }

    public RESTClient(String serviceBaseURI) {
        this.serviceBaseURI = serviceBaseURI;
    }

    public String getServiceBaseURI() {
        return serviceBaseURI;
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
        }

        return user;
    }

    public PhoneUser verifyAddUser(String userName, int expectedStatusCode, String contentType) throws IOException, JAXBException, Exception {

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
    public void verifyListAllUsers(int expectedStatusCode) throws IOException {
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
    public String responseToString(InputStream stream) {
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

    public PhoneUser verifyAddNumber(String userID, String phoneCaption, String phoneNumber,
            int expectedStatusCode, String contentType) throws IOException, JAXBException, Exception {

        System.out.println("Add PhoneNumber: caption: " + phoneCaption + ", number: "
                + phoneNumber + " for PhoneUser: " + userID + " ... \n");

        
        HttpClient client = new HttpClient();
        // -- build HTTP PUT request
        // escapt phoneNumber, da QueryParam
        PutMethod method = new PutMethod(getServiceBaseURI() + "/users" + "/" + userID + "/numbers/"
                + phoneCaption + "?number=" + escapeHtml(phoneNumber));


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

    public PhoneUser verifyGetUserInfo(String userID, int expectedStatusCode, String contentType) throws IOException, JAXBException, JAXBException, Exception {

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

    public void verifyDeleteNumber(String userID, String phoneCaption, int expectedStatusCode, String contentType) throws IOException {

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
    public void marshalPhoneUserToXML(PhoneUser user, Writer out) throws JAXBException {

        JAXBContext jc = JAXBContext.newInstance(PhoneUser.class);
        assertNotNull(jc);
        Marshaller marshaller = jc.createMarshaller();

        assertNotNull(marshaller);

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
                new Boolean(true));
        marshaller.marshal(user, out);
    }

    /**
     * Example code for deserializing a PhoneUser via JAXB.
     */
    public PhoneUser unmarshalPhoneUserFromXML(Reader in) throws JAXBException {

        JAXBContext jc = JAXBContext.newInstance(PhoneUser.class);
        assertNotNull(jc);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        assertNotNull(unmarshaller);
        Object unmarshalledObj = unmarshaller.unmarshal(in);

        assertNotNull(unmarshalledObj);

        assertTrue(unmarshalledObj instanceof PhoneUser);
        return (PhoneUser) unmarshalledObj;
    }

    public void verifyDeleteUser(String userID, int expectedStatusCode) throws IOException {

        System.out.println("Deleting User: " + userID + " ...\n");

        HttpClient client = new HttpClient();
        // -- build HTTP DELETE request
        DeleteMethod method = new DeleteMethod(getServiceBaseURI() + "/users" + "/" + userID);
        int responseCode = client.executeMethod(method);

        assertEquals(expectedStatusCode, responseCode);
    }
}
