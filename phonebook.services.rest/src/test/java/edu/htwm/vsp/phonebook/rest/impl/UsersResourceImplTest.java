package edu.htwm.vsp.phonebook.rest.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Test;

import edu.htwm.vsp.phone.services.PhoneService;
import edu.htwm.vsp.phone.services.jpa.PhoneServiceImpl;
import edu.htwm.vsp.phonebook.rest.UsersResource;

public class UsersResourceImplTest {

	private UsersResource usersResource;

	@Before
	public void prepareResources() {
		
		UsersResourceImpl usersResource = new UsersResourceImpl();
		PhoneService phoneService = new PhoneServiceImpl();
		
		usersResource.setPhoneService(phoneService);
		
		this.usersResource = usersResource;
	}
	
	@Test
	public void createNewUserWorks() {
		
		String expectedName = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) +1);
		
		Response addUserResponse = usersResource.addUser(null, expectedName);
		
		// check status
		assertThat(addUserResponse.getStatus(), is(Response.Status.CREATED.getStatusCode()));
		
		// check location
		MultivaluedMap<String, Object> metadata = addUserResponse.getMetadata();
	}

}
