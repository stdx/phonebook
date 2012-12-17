package edu.htwm.vsp.phone.services.jpa;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;

import edu.htwm.vsp.phone.service.PhoneUser;
import edu.htwm.vsp.phone.service.PhonebookService;
import edu.htwm.vsp.phone.service.inmemory.PhoneServiceInMemory;

public abstract class BaseTest {

    PhonebookService phoneService;

    /**
     * Diese Methode wird vor Beginn jeder Testmethode aufgerufen. Dies erfolgt
     * durch JUnit und wird über die Annotation {@link Before} konfiguriert.
     */
    @Before
    public void buildService() {
        phoneService = new PhoneServiceInMemory();
    }

    public PhoneUser createRandomUser(PhonebookService phoneService) {
        return createUser(phoneService, RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
    }

    public PhoneUser createUser(PhonebookService phoneService, String userName) {
        return phoneService.createUser(userName);
    }
}
