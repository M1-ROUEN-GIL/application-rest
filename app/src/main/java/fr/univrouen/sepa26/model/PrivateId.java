package fr.univrouen.sepa26.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * Objet de valeur représentant une identification privée d'un compte ou d'un acteur.
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class PrivateId {

    @XmlElement(name = "Othr")
    @Embedded
    private OtherIdentification othr;

    public OtherIdentification getOthr() {
        return othr;
    }

    public void setOthr(OtherIdentification othr) {
        this.othr = othr;
    }
}
