/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.htwm.vsp.phone.services.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import edu.htwm.vsp.phone.service.PhoneNumber;
import edu.htwm.vsp.phone.service.PhoneUser;

/**
 * Testet, ob die JAXB-Annotationen korrekt gesetzt sind und das das XML-Mapping
 * fehlerfrei funktioniert
 * 
 * @author adopleb
 * 
 */
public class XmlMappingTest extends BaseTest {

    private File validXmlFile = new File("src/test/resources/anne.xml");
    private File testfileSave = new File("src/test/resources/random.xml");
	
    private Marshaller marshaller;
	private Unmarshaller unmarshaller;

    @Before
    public void prepareXmlMarshalling() throws JAXBException {
    	
    	/* 
    	 * Code Duplizierung vermeiden 
    	 * (DRY -> http://de.wikipedia.org/wiki/Don%E2%80%99t_repeat_yourself) und 
    	 * aufwendige, immer wieder verwendete Objekte nur einmal erzeugen
    	 * 
    	 */
        JAXBContext context = JAXBContext.newInstance(PhoneUser.class, PhoneNumber.class);
       
        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        unmarshaller = context.createUnmarshaller();
    }
    
    /**
     * Testet ob eine XML-Datei korrekt angelegt wird
     */
    @SuppressWarnings("resource")
	@Test
    public void writingToAndReadingFromFileWorks() throws IOException, JAXBException {

    	// create temporary file
    	File xmlFile = File.createTempFile("phonebook", ".xml");
    	xmlFile.deleteOnExit();
    	
    	System.out.println(xmlFile.getAbsolutePath());
    	
        /*
         * Erzeugt einen Nutzer mit einem zufälligen Namen und einer zufälligen Telefonnummer.
         */
        PhoneUser expectedUser = createRandomUser(phoneService);
        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        expectedUser.setNumber(phoneNumberCaption, phoneNumber);

        // XML-Datei schreiben
        // TODO --> FileWrite müsste geschlossen werden
        // marshaller.marshal(newUser, new FileWriter(testfileSave));
        // Teste ob Datei existiert und lesbar ist
        // TODO Tests nicht sinnvoll, entfernen
//        assertThat(testfileSave.exists(), is(true));
//        assertThat(testfileSave.isFile(), is(true));
        
        assertThat(xmlFile.length(), is(0L));
        marshaller.marshal(expectedUser, xmlFile);
        
        assertThat(xmlFile.length(), greaterThan(0L));
        
        // deserialize
        PhoneUser deserializedUser = (PhoneUser)unmarshaller.unmarshal(xmlFile);
        
        // check deserialization creates new object
        assertThat(deserializedUser, is(not(sameInstance(expectedUser))));
        assertThat(deserializedUser, is(expectedUser));
    }

    /**
     * liest aus XML-Datei *
     */
    @Test
    public void loadFromXmlFile() throws IOException, JAXBException {

        PhoneUser loadedUser = (PhoneUser) unmarshaller.unmarshal(validXmlFile);

        Collection<PhoneNumber> phoneNumbers = loadedUser.getPhoneNumbers();

        // prüft, ob die beiden Telefonnummern enthalten sind
        assertThat(phoneNumbers.size(), is(2));
        // prüft, ob der Name erkannt wurde
        assertThat(loadedUser.getName(), is("anne"));
        // prüft, dass die ID stimmt
        assertThat(loadedUser.getId(), is(1));
    }
}
