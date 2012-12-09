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

	/**
	 * Diese Methode wird vor Beginn jeder Testmethode aufgerufen. Dies erfolgt
	 * durch JUnit und wird über die Annotation {@link Before} konfiguriert.
	 */
	@Before
	public void buildService() {
		
		// den IoC-Container (Guice) erstellen
		Injector injector = Guice.createInjector(PhoneModule.buildForTest());
		
		/*
		 * Für den Zugriff auf die Datenbank wird durch die Verwendung der
		 * Guice-Persist Extension ein PersistService benötigt, der vor dem
		 * ersten Zugriff auf die Datenbank gestartet werden muss und kurz vor
		 * Beendigung der Anwendung geschlossen werden muss.
		 * 
		 * 1. Referenz auf den PersistService aus dem IoC-Container besorgen
		 * 2. den PersistService starten
		 */
		persistService = injector.getInstance(PersistService.class);
		persistService.start();
		
		/*
		 * Der PhonebookService wird ebenfalls über den IoC-Container verwaltet.
		 * Die Konfiguration für das Binding der
		 * PhonebookService-Implementierung findet sich im PhoneModule.
		 */
		phoneService = injector.getInstance(PhonebookService.class);
	}
	
	/**
	 * Diese Methode wird nach Beendigung jeder Testmethode aufgerufen. Dies
	 * erfolgt durch JUnit und wird über die Annotation {@link After}
	 * konfiguriert.
	 */
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
