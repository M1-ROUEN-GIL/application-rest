package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un document SEPA.
 * Cette classe fait double office :
 * 1. Entité JPA pour la persistance en base de données SQL.
 * 2. Modèle JAXB/Jackson pour la désérialisation XML.
 */
@XmlRootElement(name = "Document")
@JacksonXmlRootElement(localName = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "documents")
public class Document {

    /** Identifiant unique en base de données */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;

    /** Date et heure de création du document */
    @XmlElement(name = "CreDtTm")
    @JacksonXmlProperty(localName = "CreDtTm")
    private String creDtTm;

    /** Somme de contrôle pour validation */
    @XmlElement(name = "CtrlSum")
    @JacksonXmlProperty(localName = "CtrlSum")
    private Double ctrlSum;

    /** Liste des informations de transactions de débit direct */
    @XmlElement(name = "DrctDbtTxInf")
    @JacksonXmlProperty(localName = "DrctDbtTxInf")
    @JacksonXmlElementWrapper(useWrapping = false)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id")
    private List<DrctDbtTxInf> drctDbtTxInfs = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCreDtTm() { return creDtTm; }
    public void setCreDtTm(String creDtTm) { this.creDtTm = creDtTm; }

    public Double getCtrlSum() { return ctrlSum; }
    public void setCtrlSum(Double ctrlSum) { this.ctrlSum = ctrlSum; }

    public List<DrctDbtTxInf> getDrctDbtTxInfs() {
        return drctDbtTxInfs;
    }

    public void setDrctDbtTxInfs(List<DrctDbtTxInf> drctDbtTxInfs) {
        this.drctDbtTxInfs = drctDbtTxInfs;
    }

    /**
     * Classe interne représentant le détail d'une transaction de débit direct.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @Entity
    @Table(name = "transaction_info")
    public static class DrctDbtTxInf {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @XmlTransient
        private Long id;

        /** Identifiant unique de l'instruction de paiement */
        @XmlElement(name = "PmtId")
        @JacksonXmlProperty(localName = "PmtId")
        @Column(unique = true)
        private String pmtId;

        /** Montant de la transaction */
        @XmlElement(name = "InstdAmt")
        @JacksonXmlProperty(localName = "InstdAmt")
        @Embedded
        private InstdAmt instdAmt;

        /** Informations spécifiques au débit direct */
        @XmlElement(name = "DrctDbtTx")
        @JacksonXmlProperty(localName = "DrctDbtTx")
        @Embedded
        private DrctDbtTx drctDbtTx;

        /** Agent du débiteur (Banque) */
        @XmlElement(name = "DbtrAgt")
        @JacksonXmlProperty(localName = "DbtrAgt")
        @Embedded
        private DbtrAgt dbtrAgt;

        /** Information sur le débiteur */
        @XmlElement(name = "Dbtr")
        @JacksonXmlProperty(localName = "Dbtr")
        @Embedded
        private Dbtr dbtr;

        /** Compte du débiteur */
        @XmlElement(name = "DbtrAcct")
        @JacksonXmlProperty(localName = "DbtrAcct")
        @Embedded
        private DbtrAcct dbtrAcct;

        /** Informations de remise (Libellé du virement) */
        @XmlElement(name = "RmtInf")
        @JacksonXmlProperty(localName = "RmtInf")
        private String rmtInf;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getPmtId() { return pmtId; }
        public void setPmtId(String pmtId) { this.pmtId = pmtId; }
        public InstdAmt getInstdAmt() { return instdAmt; }
        public void setInstdAmt(InstdAmt instdAmt) { this.instdAmt = instdAmt; }
        public DrctDbtTx getDrctDbtTx() { return drctDbtTx; }
        public void setDrctDbtTx(DrctDbtTx drctDbtTx) { this.drctDbtTx = drctDbtTx; }
        public DbtrAgt getDbtrAgt() { return dbtrAgt; }
        public void setDbtrAgt(DbtrAgt dbtrAgt) { this.dbtrAgt = dbtrAgt; }
        public Dbtr getDbtr() { return dbtr; }
        public void setDbtr(Dbtr dbtr) { this.dbtr = dbtr; }
        public DbtrAcct getDbtrAcct() { return dbtrAcct; }
        public void setDbtrAcct(DbtrAcct dbtrAcct) { this.dbtrAcct = dbtrAcct; }
        public String getRmtInf() { return rmtInf; }
        public void setRmtInf(String rmtInf) { this.rmtInf = rmtInf; }
    }

    /** Représentation du montant instruit */
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class InstdAmt {
        /** Valeur numérique du montant */
        @XmlValue
        @JacksonXmlText
        @Column(name = "amount_value")
        private Double value;
        /** Code devise (ex: EUR) */
        @XmlAttribute(name = "Ccy")
        @JacksonXmlProperty(isAttribute = true, localName = "Ccy")
        @Column(name = "amount_ccy")
        private String ccy = "EUR";

        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }
        public String getCcy() { return ccy; }
        public void setCcy(String ccy) { this.ccy = ccy; }
    }

    /** Informations de débit direct */
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class DrctDbtTx {
        /** Informations relatives au mandat */
        @XmlElement(name = "MndtRltdInf")
        @JacksonXmlProperty(localName = "MndtRltdInf")
        @Embedded
        private MndtRltdInf mndtRltdInf;

        public MndtRltdInf getMndtRltdInf() { return mndtRltdInf; }
        public void setMndtRltdInf(MndtRltdInf mndtRltdInf) { this.mndtRltdInf = mndtRltdInf; }
    }

    /** Informations sur le mandat SEPA */
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class MndtRltdInf {
        /** Identifiant du mandat */
        @XmlElement(name = "MndtId")
        @JacksonXmlProperty(localName = "MndtId")
        @Column(name = "mndt_id")
        private String mndtId;
        /** Date de signature du mandat */
        @XmlElement(name = "DtOfSgntr")
        @JacksonXmlProperty(localName = "DtOfSgntr")
        @Column(name = "dt_of_sgntr")
        private String dtOfSgntr;

        public String getMndtId() { return mndtId; }
        public void setMndtId(String mndtId) { this.mndtId = mndtId; }
        public String getDtOfSgntr() { return dtOfSgntr; }
        public void setDtOfSgntr(String dtOfSgntr) { this.dtOfSgntr = dtOfSgntr; }
    }

    /** Agent du débiteur */
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class DbtrAgt {
        /** Identification de l'institution financière */
        @XmlElement(name = "FinInstnId")
        @JacksonXmlProperty(localName = "FinInstnId")
        @Embedded
        private FinInstnId finInstnId;

        public FinInstnId getFinInstnId() { return finInstnId; }
        public void setFinInstnId(FinInstnId finInstnId) { this.finInstnId = finInstnId; }
    }

    /** Identification de l'institution financière */
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class FinInstnId {
        /** Code BIC de la banque */
        @XmlElement(name = "BIC")
        @JacksonXmlProperty(localName = "BIC")
        @Column(name = "agent_bic")
        private String bic;
        /** Identifiant optionnel de l'institution */
        @XmlElement(name = "Id")
        @JacksonXmlProperty(localName = "Id")
        @Column(name = "agent_id")
        private String id;

        public String getBic() { return bic; }
        public void setBic(String bic) { this.bic = bic; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }

    /** Le débiteur */
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class Dbtr {
        /** Nom du débiteur */
        @XmlElement(name = "Nm")
        @JacksonXmlProperty(localName = "Nm")
        @Column(name = "dbtr_nm")
        private String nm;

        public String getNm() { return nm; }
        public void setNm(String nm) { this.nm = nm; }
    }

    /** Compte du débiteur */
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class DbtrAcct {
        /** Identification du compte */
        @XmlElement(name = "Id")
        @JacksonXmlProperty(localName = "Id")
        @Embedded
        private DbtrAcctId id;

        public DbtrAcctId getId() { return id; }
        public void setId(DbtrAcctId id) { this.id = id; }
    }

    /** Identification du compte du débiteur */
    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class DbtrAcctId {
        /** Code IBAN du compte */
        @XmlElement(name = "IBAN")
        @JacksonXmlProperty(localName = "IBAN")
        @Column(name = "dbtr_iban")
        private String iban;
        /** Identifiant privé (si pas d'IBAN) */
        @XmlElement(name = "PrvtId")
        @JacksonXmlProperty(localName = "PrvtId")
        @Column(name = "dbtr_prvt_id")
        private String prvtId;

        public String getIban() { return iban; }
        public void setIban(String iban) { this.iban = iban; }
        public String getPrvtId() { return prvtId; }
        public void setPrvtId(String prvtId) { this.prvtId = prvtId; }
    }
}
