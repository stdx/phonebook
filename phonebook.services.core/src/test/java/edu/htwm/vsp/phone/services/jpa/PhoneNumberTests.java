package edu.htwm.vsp.phone.services.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.not;

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
        PhoneNumber expectedPhoneNumber = new PhoneNumber(phoneNumberCaption, phoneNumber);

        PhoneUser userFetchedFromDB = phoneService.findUserById(newUser.getId());

        assertThat(userFetchedFromDB.getPhoneNumbers().size(), is(0));
        assertThat(expectedPhoneNumber, not(isIn(userFetchedFromDB.getPhoneNumbers())));

        userFetchedFromDB.setNumber(phoneNumberCaption, phoneNumber);

        assertThat(userFetchedFromDB.getPhoneNumbers().size(), is(1));
        assertThat(expectedPhoneNumber, isIn(userFetchedFromDB.getPhoneNumbers()));
    }

    @Test
    public void checkForNotExistingNumberReturnsFalse() {


        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        PhoneNumber newNumber = new PhoneNumber(phoneNumberCaption, phoneNumber);

        PhoneUser expectedUser = createRandomUser(phoneService);
        assertThat(newNumber, not(isIn(expectedUser.getPhoneNumbers())));
    }

    @Test
    public void checkForNumberWithCaption() {

        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);

        PhoneUser user = createRandomUser(phoneService);
        user.setNumber(phoneNumberCaption, phoneNumber);

        assertThat(user.containsNumberWithCaption(phoneNumberCaption), is(true));

        user.deleteNumber(phoneNumberCaption);

        assertThat(user.containsNumberWithCaption(phoneNumberCaption), is(false));

    }

    @Test
    public void checkForNumberWithNumber() {

        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);

        PhoneUser user = createRandomUser(phoneService);
        user.setNumber(phoneNumberCaption, phoneNumber);

        assertThat(user.containsNumberWithNumber(phoneNumber), is(true));

        user.deleteAllNumbers();

        assertThat(user.containsNumberWithNumber(phoneNumber), is(false));
    }
}
