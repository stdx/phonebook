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
 *
 * Testet, ob die JAXB-Annotationen korrekt gesetzt sind und das das XML-Mapping
 * fehlerfrei funktioniert
 */
public class XmlMappingTest extends BaseTest {

    private File testfileLoad = new File("src/test/resources/anne.xml");
    private File testfileSave = new File("src/test/resources/random.xml");

    /**
     * Testet ob eine XML-Datei korrekt angelegt wird
     */
    @Test
    public void createValidXmlFile() throws IOException, JAXBException {

        // Lösche Datei falls schon vorhanden
        if (testfileSave.exists()) {
            testfileSave.delete();
        }

        /*
         * Erzeugt einen Nutzer mit einem zufälligen Namen und einer zufälligen Telefonnummer.
         */
        PhoneUser newUser = createRandomUser(phoneService);
        String phoneNumberCaption = RandomStringUtils.randomAlphanumeric(10);
        String phoneNumber = RandomStringUtils.randomAlphanumeric(8);
        PhoneNumber newNumber = new PhoneNumber(phoneNumberCaption, phoneNumber);
        newUser.getPhoneNumbers().add(newNumber);

        JAXBContext context = JAXBContext.newInstance(PhoneUser.class, PhoneNumber.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // XML-Datei schreiben
        m.marshal(newUser, new FileWriter(testfileSave));
        // Teste ob Datei existiert und lesbar ist
        assertThat(testfileSave.exists(), is(true));
        assertThat(testfileSave.isFile(), is(true));
    }

    /**
     * liest aus XML-Datei *
     */
    @Test
    public void loadFromXmlFile() throws IOException, JAXBException {

        JAXBContext context = JAXBContext.newInstance(PhoneUser.class, PhoneNumber.class);
        Unmarshaller um = context.createUnmarshaller();
        PhoneUser loadedUser = (PhoneUser) um.unmarshal(new FileReader(testfileLoad));

        List<PhoneNumber> phoneNumbers = loadedUser.getPhoneNumbers();

        // prüft, ob die beiden Telefonnummern enthalten sind
        assertThat(phoneNumbers.size(), is(2));
        // prüft, ob der Name erkannt wurde
        assertThat(loadedUser.getName(), is("anne"));
        // prüft, dass die ID stimmt
        assertThat(loadedUser.getId(), is(1));
    }
}
