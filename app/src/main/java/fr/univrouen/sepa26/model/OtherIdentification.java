package fr.univrouen.sepa26.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * Objet de valeur pour une identification tierce dotée d'un identifiant et d'un nom de schéma.
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class OtherIdentification {

    @XmlElement(name = "Id")
    private String id;

    @XmlElement(name = "SchmeNm")
    @Embedded
    private SchemeName schemeName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SchemeName getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(SchemeName schemeName) {
        this.schemeName = schemeName;
    }
}
