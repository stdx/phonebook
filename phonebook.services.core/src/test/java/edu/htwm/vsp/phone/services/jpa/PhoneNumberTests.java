package edu.htwm.vsp.phone.services.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import edu.htwm.vsp.phone.service.PhoneNumber;
import edu.htwm.vsp.phone.service.PhoneUser;

public class PhoneNumberTests extends BaseTest {

	@Test
	public void addSingleNumberToUser() {
		
		PhoneUser newUser = createRandomUser(phoneService);
		
		String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
		String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
		
		PhoneNumber newNumber = new PhoneNumber(newUser, phoneNumberCaption, phoneNumber);
		newUser.getPhoneNumbers().add(newNumber);
		
		PhoneUser userFetchedFromDB = phoneService.findUserById(newUser.getId());
		assertThat(userFetchedFromDB.getPhoneNumbers().size(), is(1));
	}
}
