package fr.univrouen.sepa26.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * Objet de valeur représentant un agent financier bancaire (CdtrAgt, DbtrAgt).
 * Reutilisé selon DRY pour la banque du créancier et celle du débiteur.
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class Agent {

    @XmlElement(name = "FinInstnId")
    @Embedded
    private FinInstnId finInstnId;

    public FinInstnId getFinInstnId() {
        return finInstnId;
    }

    public void setFinInstnId(FinInstnId finInstnId) {
        this.finInstnId = finInstnId;
    }
}
