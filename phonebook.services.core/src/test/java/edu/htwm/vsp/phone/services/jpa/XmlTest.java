/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.htwm.vsp.phone.services.jpa;

import edu.htwm.vsp.phone.service.PhoneNumber;
import edu.htwm.vsp.phone.service.PhoneUser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;



import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 *
 * @author adopleb
 */
public class XmlTest extends BaseTest {

    File TESTFILE_LOAD = new File("src/test/resources/anne.xml");
    File TESTFILE_SAVE = new File("src/test/resources/random.xml");

    /**
     * Testet ob eine XML-Datei korrekt angelegt wird
     */
    @Test
    public void createValidXmlFile() throws IOException, JAXBException {

        // Lösche Datei falls schon vorhanden
        if(TESTFILE_SAVE.exists())
        {
             TESTFILE_SAVE.delete();
        }
        
        /*
         * Erzeugt einen Nutzer mit einem zufälligen Namen und einer zufälligen Telefonnummer.
         */
        PhoneUser newUser = createRandomUser(phoneService);
        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        PhoneNumber newNumber = new PhoneNumber(newUser, phoneNumberCaption, phoneNumber);
        newUser.getPhoneNumbers().add(newNumber);

        JAXBContext context = JAXBContext.newInstance(PhoneUser.class, PhoneNumber.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

       

        // XML-Datei schreiben
        m.marshal(newUser, new FileWriter(TESTFILE_SAVE));
        // Teste ob Datei existiert und lesbar ist
        assertThat(TESTFILE_SAVE.exists(), is(true));
        assertThat(TESTFILE_SAVE.isFile(), is(true));


    }

    /**
     * ließt aus XML-Datei *
     */
    @Test
    public void loadFromXmlFile() throws IOException, JAXBException {

        JAXBContext context = JAXBContext.newInstance(PhoneUser.class, PhoneNumber.class);
        Unmarshaller um = context.createUnmarshaller();
        PhoneUser loadedUser = (PhoneUser) um.unmarshal(new FileReader(TESTFILE_LOAD));

        // Da in PhoneUser Annotation @XmlTransient, muss nun die Referenz manuell gesetzt werden
        for (PhoneNumber number : loadedUser.getPhoneNumbers()) {
            number.setUser(loadedUser);
        }

        List<PhoneNumber> phoneNumbers = loadedUser.getPhoneNumbers();

        // prüft, ob die beiden Telefonnummern enthalten sind
        assertThat(phoneNumbers.size(), is(2));        
        // prüft, ob der Name erkannt wurde
        assertThat(loadedUser.getName(), is("anne"));        
        // prüft, dass die ID stimmt
        assertThat(loadedUser.getId(), is(1));


    }
}
