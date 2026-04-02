package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Document")
@JacksonXmlRootElement(localName = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document {

    @XmlElement(name = "DrctDbtTxInf")
    @JacksonXmlProperty(localName = "DrctDbtTxInf")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<DrctDbtTxInf> drctDbtTxInfs = new ArrayList<>();

    public List<DrctDbtTxInf> getDrctDbtTxInfs() {
        return drctDbtTxInfs;
    }

    public void setDrctDbtTxInfs(List<DrctDbtTxInf> drctDbtTxInfs) {
        this.drctDbtTxInfs = drctDbtTxInfs;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DrctDbtTxInf {
        @XmlElement(name = "PmtId")
        @JacksonXmlProperty(localName = "PmtId")
        private String pmtId;

        @XmlElement(name = "InstdAmt")
        @JacksonXmlProperty(localName = "InstdAmt")
        private InstdAmt instdAmt;

        @XmlElement(name = "DrctDbtTx")
        @JacksonXmlProperty(localName = "DrctDbtTx")
        private DrctDbtTx drctDbtTx;

        @XmlElement(name = "DbtrAgt")
        @JacksonXmlProperty(localName = "DbtrAgt")
        private DbtrAgt dbtrAgt;

        @XmlElement(name = "Dbtr")
        @JacksonXmlProperty(localName = "Dbtr")
        private Dbtr dbtr;

        @XmlElement(name = "DbtrAcct")
        @JacksonXmlProperty(localName = "DbtrAcct")
        private DbtrAcct dbtrAcct;

        @XmlElement(name = "RmtInf")
        @JacksonXmlProperty(localName = "RmtInf")
        private String rmtInf;

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
    public static class InstdAmt {
        @XmlValue
        @JacksonXmlText
        private Double value;
        @XmlAttribute(name = "Ccy")
        @JacksonXmlProperty(isAttribute = true, localName = "Ccy")
        private String ccy = "EUR";

        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }
        public String getCcy() { return ccy; }
        public void setCcy(String ccy) { this.ccy = ccy; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DrctDbtTx {
        @XmlElement(name = "MndtRltdInf")
        @JacksonXmlProperty(localName = "MndtRltdInf")
        private MndtRltdInf mndtRltdInf;

        public MndtRltdInf getMndtRltdInf() { return mndtRltdInf; }
        public void setMndtRltdInf(MndtRltdInf mndtRltdInf) { this.mndtRltdInf = mndtRltdInf; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MndtRltdInf {
        @XmlElement(name = "MndtId")
        @JacksonXmlProperty(localName = "MndtId")
        private String mndtId;
        @XmlElement(name = "DtOfSgntr")
        @JacksonXmlProperty(localName = "DtOfSgntr")
        private String dtOfSgntr; // Format xs:date (YYYY-MM-DD)

        public String getMndtId() { return mndtId; }
        public void setMndtId(String mndtId) { this.mndtId = mndtId; }
        public String getDtOfSgntr() { return dtOfSgntr; }
        public void setDtOfSgntr(String dtOfSgntr) { this.dtOfSgntr = dtOfSgntr; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DbtrAgt {
        @XmlElement(name = "FinInstnId")
        @JacksonXmlProperty(localName = "FinInstnId")
        private FinInstnId finInstnId;

        public FinInstnId getFinInstnId() { return finInstnId; }
        public void setFinInstnId(FinInstnId finInstnId) { this.finInstnId = finInstnId; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FinInstnId {
        @XmlElement(name = "BIC")
        @JacksonXmlProperty(localName = "BIC")
        private String bic;
        @XmlElement(name = "Id")
        @JacksonXmlProperty(localName = "Id")
        private String id;

        public String getBic() { return bic; }
        public void setBic(String bic) { this.bic = bic; }
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Dbtr {
        @XmlElement(name = "Nm")
        @JacksonXmlProperty(localName = "Nm")
        private String nm;

        public String getNm() { return nm; }
        public void setNm(String nm) { this.nm = nm; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DbtrAcct {
        @XmlElement(name = "Id")
        @JacksonXmlProperty(localName = "Id")
        private DbtrAcctId id;

        public DbtrAcctId getId() { return id; }
        public void setId(DbtrAcctId id) { this.id = id; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DbtrAcctId {
        @XmlElement(name = "IBAN")
        @JacksonXmlProperty(localName = "IBAN")
        private String iban;
        @XmlElement(name = "PrvtId")
        @JacksonXmlProperty(localName = "PrvtId")
        private String prvtId;

        public String getIban() { return iban; }
        public void setIban(String iban) { this.iban = iban; }
        public String getPrvtId() { return prvtId; }
        public void setPrvtId(String prvtId) { this.prvtId = prvtId; }
    }
}

