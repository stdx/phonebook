package edu.htwm.vsp.phonebook.rest.impl;

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
import edu.htwm.vsp.phonebook.rest.PhonebookResource;

public class UsersResourceImplTest extends BaseResourceTest {

	private PhonebookResourceImpl usersResource;

	@Before
	public void prepareResourcesToTest() {
		
		usersResource = new PhonebookResourceImpl();
		usersResource.setPhoneService(phoneService);
	}
	/*
	@Test
	public void createSingleNewUserWorks() throws IllegalArgumentException, UriBuilderException, URISyntaxException, MalformedURLException {
		
		UriInfo uriInfo = mock(UriInfo.class);
		final UriBuilder fromResource = UriBuilder.fromResource(PhonebookResource.class);
		when(uriInfo.getAbsolutePathBuilder()).thenAnswer(new Answer<UriBuilder>() {
			@Override
			public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
				return fromResource;
			}
		});
		
		String expectedName = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) +1);
		Response createUserResponse = usersResource.createUser(uriInfo, expectedName);
		
		PhoneUser createdUser = (PhoneUser)createUserResponse.getEntity();
		
		// check correct response code 
		assertThat(createUserResponse.getStatus(), is(Status.CREATED.getStatusCode()));
		assertThat(createdUser.getName(), is(expectedName));
		
		// check location
		MultivaluedMap<String, Object> metadata = createUserResponse.getMetadata();
		URI location = (URI)metadata.getFirst("Location");
		System.out.println(location);
		assertThat(location.getPath(), containsString("users/" + createdUser.getId()));
		
		// fetch the user by ID and compare it with the previously created user
		Response fetchUserResponse = usersResource.getUser(createdUser.getId());
		assertThat(fetchUserResponse.getStatus(), is(Status.OK.getStatusCode()));
		
		PhoneUser fetchedUser = (PhoneUser)fetchUserResponse.getEntity();
		assertThat(fetchedUser, is(createdUser));
	}
	
	@Test
	public void fetchNotExistingUserGives404() {
		Response fetchUserResponse = usersResource.getUser(Integer.MAX_VALUE);
		
		assertThat(fetchUserResponse.getStatus(), is(Status.NOT_FOUND.getStatusCode()));
	}
*/

}
