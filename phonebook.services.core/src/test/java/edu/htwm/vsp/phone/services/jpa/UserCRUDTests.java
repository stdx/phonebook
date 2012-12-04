package edu.htwm.vsp.phone.services.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import edu.htwm.vsp.phone.service.PhoneUser;

public class UserCRUDTests extends BaseTest {
	
	@Test
	public void createValidUser() {
		
		String expectedName = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
		
		PhoneUser newUser = createUser(phoneService, expectedName);
		
		assertThat(newUser.getName(), is(expectedName));
		
		PhoneUser userFetchedFromDB = phoneService.findUserById(newUser.getId());
		assertThat(userFetchedFromDB, is(newUser));
	}
	
	@Test
	public void deleteExistingUser() {
		
		PhoneUser expectedUser = createUser(phoneService);
		
		
		PhoneUser userFetchedFromDB = phoneService.findUserById(expectedUser.getId());
		assertThat(userFetchedFromDB, is(expectedUser));

		phoneService.deleteUser(userFetchedFromDB.getId());
		
		userFetchedFromDB = phoneService.findUserById(expectedUser.getId());
		assertThat(userFetchedFromDB, is(nullValue()));
		
	}
	
	

}
