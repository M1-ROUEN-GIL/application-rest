package fr.univrouen.sepa26.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Entité racine représentant un document SEPA ISO 20022.
 * Respecte le principe de responsabilité unique (SRP) en déléguant
 * les sous-structures métier à leurs entités et composants dédiés.
 */
@XmlRootElement(
    name = "Document",
    namespace = "http://univ.fr/sepa26"
)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"cstmrDrctDbtInitn"})
@JacksonXmlRootElement(localName = "Document")
@Entity
@Table(name = "documents")
public class Document {

    /** Identifiant unique technique en base de données */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    @XmlTransient
    private Long idDoc;

    /** Conteneur d'initiation de prélèvement direct */
    @OneToOne(cascade = CascadeType.ALL)
    @JacksonXmlProperty(localName = "CstmrDrctDbtInitn")
    @JoinColumn(name = "cstmr_drct_dbt_init_id", referencedColumnName = "cstmr_direct_debit_initiation_id")
    @XmlElement(name = "CstmrDrctDbtInitn")
    private CstmrDrctDbtInitn cstmrDrctDbtInitn;

    public Long getId() {
        return idDoc;
    }

    public void setId(Long idDoc) {
        this.idDoc = idDoc;
    }

    public CstmrDrctDbtInitn getCstmrDrctDbtInitn() {
        return cstmrDrctDbtInitn;
    }

    public void setCstmrDrctDbtInitn(CstmrDrctDbtInitn cstmrDrctDbtInitn) {
        this.cstmrDrctDbtInitn = cstmrDrctDbtInitn;
    }
}