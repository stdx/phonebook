package edu.htwm.vsp.phone.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * A user has many {@link PhoneNumber}s.
 *
 * @author std
 *
 */
@Entity
@XmlRootElement(name = "user")
public class PhoneUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<PhoneNumber> phoneNumbers;

    public PhoneUser() {
        this("INVALID_USER");
    }

    public PhoneUser(String name) {
        this.name = name;
        this.phoneNumbers = new HashSet<PhoneNumber>();
    }

    public PhoneUser(PhoneUser phoneUser) {
        this(phoneUser.getName());

        // clone remaining properties
        this.setId(phoneUser.getId());
        for (PhoneNumber otherNumber : phoneUser.getPhoneNumbers()) {
            this.phoneNumbers.add(new PhoneNumber(otherNumber));
        }
    }

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "number")
    @XmlElementWrapper(name = "phone-numbers")
    public Collection<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setNumbers(Collection<PhoneNumber> phoneNumbers) {
        for (PhoneNumber newNumber : phoneNumbers) {
            this.phoneNumbers.add(newNumber);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((phoneNumbers == null) ? 0 : phoneNumbers.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PhoneUser other = (PhoneUser) obj;
        if (id != other.id) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (phoneNumbers == null) {
            if (other.phoneNumbers != null) {
                return false;
            }
        } else if (!phoneNumbers.equals(other.phoneNumbers)) {
            return false;
        }
        return true;
    }

    /**
     * fügt Telefonnummer zu einem Nutzer hinzu. Falls bereits eine Nummer mit
     * der übergebenen caption existiert, wird diese überschrieben
     *
     * @param caption Caption
     * @param number Nummer
     */
    public void setNumber(String caption, String number) {

        // Falls Nummer schon vorhanden --> ersetzen
        if (containsNumberWithCaption(caption)) {
            PhoneNumber num = getNumberWithCaption(caption);
            num.setNumber(number);
        } else {
            this.phoneNumbers.add(new PhoneNumber(caption, number));
        }

    }

    @Override
    public String toString() {
        return "PhoneUser [id=" + id + ", name=" + name + ", phoneNumbers=" + phoneNumbers + "]";
    }

    /**
     * Holt PhoneNumber mit übergebenen caption
     *
     * @param caption
     * @return PhoneNumber mit caption, null falls nicht vorhanden
     */
    public PhoneNumber getNumberWithCaption(String caption) {
        if (!containsNumberWithCaption(caption)) {
            return null;
        }
        for (PhoneNumber num : phoneNumbers) {
            if (num.getCaption().equals(caption)) {
                return num;
            }
        }
        return null;
    }

    public boolean containsNumberWithCaption(String caption) {

        for (PhoneNumber num : phoneNumbers) {
            if (num.getCaption().equals(caption)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsNumberWithNumber(String number) {

        for (PhoneNumber num : phoneNumbers) {
            if (num.getNumber().equals(number)) {
                return true;
            }
        }
        return false;
    }

    public void deleteAllNumbers() {
        this.phoneNumbers.clear();
    }

    public void deleteNumber(String caption) {
        for (PhoneNumber num : phoneNumbers) {
            if (num.getCaption().equals(caption)) {
                phoneNumbers.remove(num);
                break;
            }
        }

    }
}
