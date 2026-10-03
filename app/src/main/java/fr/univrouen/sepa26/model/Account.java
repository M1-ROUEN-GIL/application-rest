package fr.univrouen.sepa26.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * Objet de valeur représentant un compte bancaire (CdtrAcct, DbtrAcct).
 * Encapsule un identifiant de compte AccountId.
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class Account {

    @XmlElement(name = "Id")
    @Embedded
    private AccountId id;

    public AccountId getId() {
        return id;
    }

    public void setId(AccountId id) {
        this.id = id;
    }
}
