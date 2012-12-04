package edu.htwm.vsp.phone.services.jpa;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Before;

import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

import edu.htwm.vsp.phone.bind.Modules;
import edu.htwm.vsp.phone.service.PhonebookService;
import edu.htwm.vsp.phone.service.PhoneUser;

public abstract class BaseTest {

	PhonebookService phoneService;
	PersistService persistService;

	@Before
	public void buildService() {
		Injector injector = Modules.buildForTest();
		
		persistService = injector.getInstance(PersistService.class);
		persistService.start();
		
		phoneService = injector.getInstance(PhonebookService.class);
	}
	
	@After
	public void stopServices() {
		persistService.stop();
	}
	
	
	public PhoneUser createUser(PhonebookService phoneService) {
		return createUser(phoneService, RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
	}
	
	public PhoneUser createUser(PhonebookService phoneService, String userName) {
		return phoneService.createUser(userName);
	}
}
