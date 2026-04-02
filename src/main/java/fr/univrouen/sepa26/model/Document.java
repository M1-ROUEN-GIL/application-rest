package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Document")
@JacksonXmlRootElement(localName = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;

    @XmlElement(name = "CreDtTm")
    @JacksonXmlProperty(localName = "CreDtTm")
    private String creDtTm;

    @XmlElement(name = "CtrlSum")
    @JacksonXmlProperty(localName = "CtrlSum")
    private Double ctrlSum;

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

    @XmlAccessorType(XmlAccessType.FIELD)
    @Entity
    @Table(name = "transaction_info")
    public static class DrctDbtTxInf {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @XmlTransient
        private Long id;

        @XmlElement(name = "PmtId")
        @JacksonXmlProperty(localName = "PmtId")
        @Column(unique = true)
        private String pmtId;

        @XmlElement(name = "InstdAmt")
        @JacksonXmlProperty(localName = "InstdAmt")
        @Embedded
        private InstdAmt instdAmt;

        @XmlElement(name = "DrctDbtTx")
        @JacksonXmlProperty(localName = "DrctDbtTx")
        @Embedded
        private DrctDbtTx drctDbtTx;

        @XmlElement(name = "DbtrAgt")
        @JacksonXmlProperty(localName = "DbtrAgt")
        @Embedded
        private DbtrAgt dbtrAgt;

        @XmlElement(name = "Dbtr")
        @JacksonXmlProperty(localName = "Dbtr")
        @Embedded
        private Dbtr dbtr;

        @XmlElement(name = "DbtrAcct")
        @JacksonXmlProperty(localName = "DbtrAcct")
        @Embedded
        private DbtrAcct dbtrAcct;

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

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class InstdAmt {
        @XmlValue
        @JacksonXmlText
        @Column(name = "amount_value")
        private Double value;
        @XmlAttribute(name = "Ccy")
        @JacksonXmlProperty(isAttribute = true, localName = "Ccy")
        @Column(name = "amount_ccy")
        private String ccy = "EUR";

        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }
        public String getCcy() { return ccy; }
        public void setCcy(String ccy) { this.ccy = ccy; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class DrctDbtTx {
        @XmlElement(name = "MndtRltdInf")
        @JacksonXmlProperty(localName = "MndtRltdInf")
        @Embedded
        private MndtRltdInf mndtRltdInf;

        public MndtRltdInf getMndtRltdInf() { return mndtRltdInf; }
        public void setMndtRltdInf(MndtRltdInf mndtRltdInf) { this.mndtRltdInf = mndtRltdInf; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class MndtRltdInf {
        @XmlElement(name = "MndtId")
        @JacksonXmlProperty(localName = "MndtId")
        @Column(name = "mndt_id")
        private String mndtId;
        @XmlElement(name = "DtOfSgntr")
        @JacksonXmlProperty(localName = "DtOfSgntr")
        @Column(name = "dt_of_sgntr")
        private String dtOfSgntr;

        public String getMndtId() { return mndtId; }
        public void setMndtId(String mndtId) { this.mndtId = mndtId; }
        public String getDtOfSgntr() { return dtOfSgntr; }
        public void setDtOfSgntr(String dtOfSgntr) { this.dtOfSgntr = dtOfSgntr; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class DbtrAgt {
        @XmlElement(name = "FinInstnId")
        @JacksonXmlProperty(localName = "FinInstnId")
        @Embedded
        private FinInstnId finInstnId;

        public FinInstnId getFinInstnId() { return finInstnId; }
        public void setFinInstnId(FinInstnId finInstnId) { this.finInstnId = finInstnId; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class FinInstnId {
        @XmlElement(name = "BIC")
        @JacksonXmlProperty(localName = "BIC")
        @Column(name = "agent_bic")
        private String bic;
        @XmlElement(name = "Id")
        @JacksonXmlProperty(localName = "Id")
        @Column(name = "agent_id")
        private String id;

        public String getBic() { return bic; }
        public void setBic(String bic) { this.bic = bic; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class Dbtr {
        @XmlElement(name = "Nm")
        @JacksonXmlProperty(localName = "Nm")
        @Column(name = "dbtr_nm")
        private String nm;

        public String getNm() { return nm; }
        public void setNm(String nm) { this.nm = nm; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class DbtrAcct {
        @XmlElement(name = "Id")
        @JacksonXmlProperty(localName = "Id")
        @Embedded
        private DbtrAcctId id;

        public DbtrAcctId getId() { return id; }
        public void setId(DbtrAcctId id) { this.id = id; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @Embeddable
    public static class DbtrAcctId {
        @XmlElement(name = "IBAN")
        @JacksonXmlProperty(localName = "IBAN")
        @Column(name = "dbtr_iban")
        private String iban;
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
