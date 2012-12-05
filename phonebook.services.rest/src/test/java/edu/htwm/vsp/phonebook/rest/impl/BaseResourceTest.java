package edu.htwm.vsp.phonebook.rest.impl;

import org.junit.After;
import org.junit.Before;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

import edu.htwm.vsp.phone.bind.PhoneModule;
import edu.htwm.vsp.phone.service.PhonebookService;

public abstract class BaseResourceTest {

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
	
}
