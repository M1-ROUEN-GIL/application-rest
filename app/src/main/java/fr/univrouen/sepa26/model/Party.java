package fr.univrouen.sepa26.model;

import jakarta.persistence.Embeddable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * Objet de valeur représentant une partie prenante SEPA (Initiating Party, Creditor, Debtor).
 * Respecte le principe DRY en étant réutilisé pour les trois rôles.
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class Party {

    @XmlElement(name = "Nm")
    private String nm;

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }
}
