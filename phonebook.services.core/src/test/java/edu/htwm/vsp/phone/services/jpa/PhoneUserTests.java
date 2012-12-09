package edu.htwm.vsp.phone.services.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.nullValue;

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
		 * Den neu erstellten Nutzer aus der Datenbank per ID holen und dann mit
		 * dem erwarteten Nutzer vergleichen.
		 */
		PhoneUser userFetchedFromDB = phoneService.findUserById(expectedUser.getId());
		assertThat(userFetchedFromDB, is(expectedUser));
		
		/*
		 * Alle Nutzer aus der Datenbank holen und prüfen ob der neu erstellte
		 * Nutzer enthalten ist.
		 */
		List<PhoneUser> allUsers = phoneService.fetchAllUsers();
		assertThat(userFetchedFromDB, isIn(allUsers));
		
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
		
		// den Nutzer löschen
		phoneService.deleteUser(expectedUser.getId());
		
		/**
		 * Versuchen den gelöschten Nutzer per ID (expectedUser.getId())
		 * aus der Datenbank zu holen und überprüfen ob der Rückgabewert null
		 * ist.
		 * 
		 * s. Dokumentation PhoneService
		 * 
		 */
		expectedUser = phoneService.findUserById(expectedUser.getId());
		assertThat(expectedUser, is(nullValue()));
		
	}

}
