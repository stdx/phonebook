package edu.htwm.vsp.phone.services.jpa;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Before;

import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

import edu.htwm.vsp.phone.bind.Modules;
import edu.htwm.vsp.phone.service.PhoneService;
import edu.htwm.vsp.phone.service.PhoneUser;

public abstract class BaseTest {

	PhoneService phoneService;
	PersistService persistService;

	@Before
	public void buildService() {
		Injector injector = Modules.buildForTest();
		
		persistService = injector.getInstance(PersistService.class);
		persistService.start();
		
		phoneService = injector.getInstance(PhoneService.class);
	}
	
	@After
	public void stopServices() {
		persistService.stop();
	}
	
	
	public PhoneUser createUser(PhoneService phoneService) {
		return createUser(phoneService, RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
	}
	
	public PhoneUser createUser(PhoneService phoneService, String userName) {
		return phoneService.createUser(userName);
	}
}
