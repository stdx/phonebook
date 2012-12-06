package edu.htwm.vsp.phonebook.rest.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;

public class UsersResourceImplTest extends BaseResourceTest {

//	private UsersResource usersResource;

	private UsersResourceImpl usersResource;

	@Before
	public void prepareResources() {
		
		usersResource = new UsersResourceImpl();
		usersResource.setPhoneService(phoneService);
	}
	/*
	@Test
	public void createSingleNewUserWorks() {
		
		UriInfo uriInfo = mock(UriInfo.class);
		UriBuilder uriBuilder = mock(UriBuilder.class);
		when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
		
		Response createUserResponse = usersResource.createUser(uriInfo, "Gustav");
		
		// check correct response code
		assertThat(createUserResponse.getStatus(), is(Status.OK.getStatusCode()));
		
//		String expectedName = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) +1);
//		
//		Response addUserResponse = usersResource.createUser(null, expectedName);
//		
//		// check status
//		assertThat(addUserResponse.getStatus(), is(Response.Status.CREATED.getStatusCode()));
//		
//		// check location
//		MultivaluedMap<String, Object> metadata = addUserResponse.getMetadata();
	}
*/

}
