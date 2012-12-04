package edu.htwm.vsp.phone.services.jpa;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Before;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

import edu.htwm.vsp.phone.bind.PhoneModule;
import edu.htwm.vsp.phone.service.PhoneUser;
import edu.htwm.vsp.phone.service.PhonebookService;

public abstract class BaseTest {

	PhonebookService phoneService;
	PersistService persistService;

	@Before
	public void buildService() {
		Injector injector = Guice.createInjector(PhoneModule.buildForTest());
		
		persistService = injector.getInstance(PersistService.class);
		persistService.start();
		
		phoneService = injector.getInstance(PhonebookService.class);
	}
	
	@After
	public void stopServices() {
		persistService.stop();
	}
	
	
	public PhoneUser createRandomUser(PhonebookService phoneService) {
		return createUser(phoneService, RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
	}
	
	public PhoneUser createUser(PhonebookService phoneService, String userName) {
		return phoneService.createUser(userName);
	}
}
