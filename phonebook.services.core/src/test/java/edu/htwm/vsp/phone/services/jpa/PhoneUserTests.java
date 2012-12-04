package edu.htwm.vsp.phone.services.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import edu.htwm.vsp.phone.service.PhoneUser;

public class PhoneUserTests extends BaseTest {
	
	/**
	 * Testet ob ein Nutzer erfolgreich erstellt wird.
	 */
	@Test
	public void createValidUser() {
		
		/*
		 * Erzeugt einen Nutzer mit einem zufälligen Namen.
		 */
		String expectedName = RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
		PhoneUser expectedUser = phoneService.createUser(expectedName);
		
		assertThat(expectedUser.getName(), is(expectedName));
		
		/*
		 * TODO Den neu erstellten Nutzer aus der Datenbank per ID holen und dann mit
		 * dem erwarteten Nutzer vergleichen.
		 */
		PhoneUser userFetchedFromDB = null;
		
		assertThat(userFetchedFromDB, is(expectedUser));
		
		/*
		 * TODO Alle Nutzer aus der Datenbank holen und prüfen ob der neu
		 * erstellte Nutzer enthalten ist.
		 */
		
		// -----
		List<PhoneUser> allUsers = Collections.emptyList();
		assertThat(userFetchedFromDB, isIn(allUsers));
		// ------
		
	}
	
	/**
	 * Löschen eines Nutzers.
	 */
	@Test
	public void deleteExistingUser() {
		
		/*
		 * Erzeugen eines Nutzers mit einem zufälligen Namen.
		 */
		PhoneUser expectedUser = createRandomUser(phoneService);
		
		// TODO den Nutzer löschen
		
		/**
		 * TODO Versuchen den gelöschten Nutzer per ID (expectedUser.getId())
		 * aus der Datenbank zu holen und überprüfen ob der Rückgabewert null
		 * ist.
		 * 
		 * s. Dokumentation PhoneService
		 * 
		 */
		expectedUser = null;
		assertThat(expectedUser, is(nullValue()));
		
	}

}
