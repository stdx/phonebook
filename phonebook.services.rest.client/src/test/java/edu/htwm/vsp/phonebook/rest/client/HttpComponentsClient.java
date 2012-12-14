package edu.htwm.vsp.phonebook.rest.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
import org.codehaus.jackson.map.ObjectMapper;
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

    @BeforeClass
    public static void setUpClass() {
    }

    @Before
    public void setUp() throws NamingException {
    }

    private void verifyAddUser(String userName, int expectedStatusCode) throws IOException {
        System.out.println("create new PhoneUser: " + userName + " ... \n");

        HttpClient client = new HttpClient();

        // -- build HTTP POST request
        PostMethod method = new PostMethod(getServiceBaseURI() + "/users");
        method.addParameter("name", userName);
        int responseCode = client.executeMethod(method);

        assertEquals(expectedStatusCode, responseCode);

        System.out.println("HTTP response body:");
        BufferedReader br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));
        String readLine;
        while (((readLine = br.readLine()) != null)) {
            System.err.println(readLine);
        }
    }

    private void verifyListAllUsers(int expectedStatusCode) throws IOException {
        System.out.println("List All Users ... \n");

        HttpClient client = new HttpClient();

        // -- build HTTP GET request
        GetMethod method = new GetMethod(getServiceBaseURI() + "/users");
        int responseCode = client.executeMethod(method);


        assertEquals(expectedStatusCode, responseCode);
        printResponse(method.getResponseBodyAsStream());
    }

    private void printResponse(InputStream stream) {

        BufferedReader br = null;
        try {
            System.out.println("HTTP response body:");
            br = new BufferedReader(new InputStreamReader(stream));
            String readLine;
            while (((readLine = br.readLine()) != null)) {
                System.err.println(readLine);
            }
        } catch (IOException io) {
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
            }
        }
    }

    private void verifyAddNumber(String userID, String phoneCaption, String phoneNumber,
            int expectedStatusCode) throws IOException {

        System.out.println("Add PhoneNumber: caption: " + phoneCaption + ", number: "
                + phoneNumber + " for PhoneUser: " + userID + " ... \n");

        HttpClient client = new HttpClient();
        // -- build HTTP POST request
        PostMethod method = new PostMethod(getServiceBaseURI() + "/users" + "/" + userID);


        // Form-Parameter hinzufügen
        method.addParameter("caption", phoneCaption);
        method.addParameter("number", phoneNumber);

        int responseCode = client.executeMethod(method);

        assertEquals(expectedStatusCode, responseCode);
        printResponse(method.getResponseBodyAsStream());
    }

    private void verifyGetUserInfo(String userID, int expectedStatusCode) throws IOException {

        System.out.println("Get Info for PhoneUser with id: " + userID + " ... \n");


        HttpClient client = new HttpClient();

        // -- build HTTP GET request
        GetMethod method = new GetMethod(getServiceBaseURI() + "/users" + "/" + userID);
        int responseCode = client.executeMethod(method);

        assertEquals(expectedStatusCode, responseCode);
        printResponse(method.getResponseBodyAsStream());

    }

    private void verifyDeleteNumber(String userID, String phoneCaption, int expectedStatusCode) throws IOException {

        System.out.println("Deleing PhoneNumber: " + phoneCaption + " of phoneUser: "
                + userID + " ...\n");

        HttpClient client = new HttpClient();
        // -- build HTTP DELETE request
        DeleteMethod method = new DeleteMethod(getServiceBaseURI() + "/users" + "/" + userID + "/numbers/" + phoneCaption);
        int responseCode = client.executeMethod(method);


        printResponse(method.getResponseBodyAsStream());
        assertEquals(expectedStatusCode, responseCode);
    }

    @Test
    public void addUser() throws Exception {
        verifyAddUser("dieter", HTTP_CREATED);
        verifyAddNumber("1", "festnetz", "03727-234234", HTTP_CREATED);
        verifyGetUserInfo("1", HTTP_OK);
        verifyDeleteNumber("1", "festnetz", HTTP_OK);

        verifyGetUserInfo("2", HTTP_OK);
        verifyGetUserInfo("-1", HTTP_NOT_FOUND);
    }
}
