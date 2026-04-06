package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

import fr.univrouen.sepa26.util.LocalDateAdapter;
import fr.univrouen.sepa26.util.LocalDateTimeAdapter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cstmr_direct_debit_initiation_id")
    @XmlElement(name = "CstmrDrctDbtInitn")
    private CstmrDrctDbtInitn cstmrDrctDbtInitn;

    public Long getId() {
    	return id;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }
    
    public CstmrDrctDbtInitn getCstmrDrctDbtInitn() {
    	return cstmrDrctDbtInitn;
    }
    
    public void setCstmrDrctDbtInitn(CstmrDrctDbtInitn cstmrDrctDbtInitn) {
    	this.cstmrDrctDbtInitn = cstmrDrctDbtInitn;
    }
    
    
    @Entity
    @Table(name = "cstmr_direct_debit_initiation")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CstmrDrctDbtInitn {
    	@Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @XmlTransient
        private Long id;
    	
    	@OneToOne(cascade = CascadeType.ALL)
    	@JoinColumn(name = "grp_hdr_id")
    	@XmlElement(name = "GrpHdr")
    	private GrpHdr grpHdr;
    	
    	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    	@JoinColumn(name = "cstmr_direct_debit_initiation_id")
    	@XmlElement(name = "PmtInf")
    	@JacksonXmlElementWrapper(useWrapping = false)
    	private List<PmtInf> pmtInfs = new ArrayList<>();
    	
    	public Long getId() {
        	return id;
        }
        
        public void setId(Long id) {
        	this.id = id;
        }
        
        public GrpHdr getGrpHdr() {
        	return grpHdr;
        }
        
        public void setGrpHdr(GrpHdr grpHdr) {
        	this.grpHdr = grpHdr;
        }
        
        public List<PmtInf> getPmtInfs() {
        	return pmtInfs;
        }
        
        public void setPmtInfs(List<PmtInf> pmtInfs) {
        	this.pmtInfs = pmtInfs;
        }
    }
    
    
    @Entity
    @Table(name = "grp_hdr")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GrpHdr {
    	@Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @XmlTransient
        private Long id;
    	
    	@XmlElement(name = "MsgId")
    	private String msgId;
    	
    	@XmlElement(name = "CreDtTm")
    	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    	private LocalDateTime creDtTm;
    	
    	@XmlElement(name = "NbOfTxs")
    	private int nbOfTxs;
    	
    	@XmlElement(name = "CtrlSum")
    	private double ctrlSum;
    	
    	@Embedded
    	@XmlElement(name = "InitgPty")
    	private Party initgPty;
    	
    	public Long getId() {
        	return id;
        }
        
        public void setId(Long id) {
        	this.id = id;
        }
        
        public String getMsgId() {
			return msgId;
		}

        public void setMsgId(String msgId) {
			this.msgId = msgId;
		}

        public LocalDateTime getCreDtTm() {
			return creDtTm;
		}

        public void setCreDtTm(LocalDateTime creDtTm) {
			this.creDtTm = creDtTm;
		}

        public int getNbOfTxs() {
			return nbOfTxs;
		}

        public void setNbOfTxs(int nbOfTxs) {
			this.nbOfTxs = nbOfTxs;
		}

        public double getCtrlSum() {
			return ctrlSum;
		}

        public void setCtrlSum(double ctrlSum) {
			this.ctrlSum = ctrlSum;
		}

        public Party getInitgPty() {
			return initgPty;
		}

        public void setInitgPty(Party initgPty) {
			this.initgPty = initgPty;
		}
    }
    
    
    @Entity
    @Table(name = "payment_info")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PmtInf {
    	@Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @XmlTransient
        private Long id;
    	
    	@XmlElement(name = "PmtInfId")
    	private String pmtInfId;
    	
    	@XmlElement(name = "NbOfTxs")
    	private Integer nbOfTxs;
    	
    	@XmlElement(name = "CtrlSum")
    	private double ctrlSum;
    	
    	@XmlElement(name = "PmtTpInf")
    	private PaymentTypeInfo pmtTpInf;
    	
    	@XmlElement(name = "ReqdColltnDt")
    	@XmlJavaTypeAdapter(LocalDateAdapter.class)
    	private LocalDate reqdColltnDt;
    	
    	@Embedded
    	@XmlElement(name = "Cdtr")
    	private Party cdtr;
    	
    	@Embedded
    	@XmlElement(name = "CdtrAcct")
    	private Account cdtrAcct;
    	
    	@Embedded
    	@XmlElement(name = "CdtrAgt")
    	private Agent cdtrAgt;
    	
    	@Embedded
    	@XmlElement(name = "CdtrSchmeId")
    	private AccountSchemeId cdtrSchmeId;
    	
    	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    	@JoinColumn(name = "pmtinf_id")
    	@XmlElement(name = "DrctDbtTxInf")
    	@JacksonXmlElementWrapper(useWrapping = false)
    	private List<DrctDbtTxInf> drctDbtTxInfs = new ArrayList<>();
    	
    	public Long getId() {
        	return id;
        }
        
        public void setId(Long id) {
        	this.id = id;
        }
        
        public String getPmtInfId() {
			return pmtInfId;
		}

	    public void setPmtInfId(String pmtInfId) {
			this.pmtInfId = pmtInfId;
		}
	
	    public Integer getNbOfTxs() {
			return nbOfTxs;
		}
	
	    public void setNbOfTxs(Integer nbOfTxs) {
			this.nbOfTxs = nbOfTxs;
		}
	
	    public Double getCtrlSum() {
			return ctrlSum;
		}
	
	    public void setCtrlSum(Double ctrlSum) {
			this.ctrlSum = ctrlSum;
		}
	
	    public PaymentTypeInfo getPmtTpInf() {
			return pmtTpInf;
		}
	
	    public void setPmtTpInf(PaymentTypeInfo pmtTpInf) {
			this.pmtTpInf = pmtTpInf;
		}
	
	    public LocalDate getReqdColltnDt() {
			return reqdColltnDt;
		}
	
	    public void setReqdColltnDt(LocalDate reqdColltnDt) {
			this.reqdColltnDt = reqdColltnDt;
		}
	
	    public Party getCdtr() {
			return cdtr;
		}
	
	    public void setCdtr(Party cdtr) {
			this.cdtr = cdtr;
		}
	
	    public Account getCdtrAcct() {
			return cdtrAcct;
		}
	
	    public void setCdtrAcct(Account cdtrAcct) {
			this.cdtrAcct = cdtrAcct;
		}
	
	    public Agent getCdtrAgt() {
			return cdtrAgt;
		}
	
	    public void setCdtrAgt(Agent cdtrAgt) {
			this.cdtrAgt = cdtrAgt;
		}
	
	    public AccountSchemeId getCdtrSchmeId() {
			return cdtrSchmeId;
		}
	
	    public void setCdtrSchmeId(AccountSchemeId cdtrSchmeId) {
			 this.cdtrSchmeId = cdtrSchmeId;
		}
	
	    public List<DrctDbtTxInf> getDrctDbtTxInfs() {
			return drctDbtTxInfs;
		}
	
	    public void setDrctDbtTxInfs(List<DrctDbtTxInf> drctDbtTxInfs) {
			this.drctDbtTxInfs = drctDbtTxInfs;
		}
	}


	@Entity
	@Table(name = "transaction_info")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class DrctDbtTxInf {
		@Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @XmlTransient
        private Long id;
		
		@XmlElement(name = "PmtId")
		private String pmtId;
		
		@Embedded
		@XmlElement(name = "InstdAmt")
		private InstdAmt instdAmt;
		
		@Embedded
		@XmlElement(name = "DrctDbtTx")
		private DrctDbtTx drctDbtTx;
		
		@Embedded
		@XmlElement(name = "DbtrAgt")
		private Agent dbtrAgt;
		
		@Embedded
		@XmlElement(name = "Dbtr")
		private Party dbtr;
		
		@Embedded
		@XmlElement(name = "DbtrAcct")
		private Account dbtrAcct;
		
		@XmlElement(name = "RmtInf")
		private String rmtInf;
    	
    	public Long getId() {
        	return id;
        }
        
        public void setId(Long id) {
        	this.id = id;
        }
        
        public String getPmtId() {
			return pmtId;
		}
			
        public void setPmtId(String pmtId) {
			this.pmtId = pmtId;
		}
			
        public InstdAmt getInstdAmt() {
			return instdAmt;
		}
			
        public void setInstdAmt(InstdAmt instdAmt) {
			this.instdAmt = instdAmt;
		}
			
        public DrctDbtTx getDrctDbtTx() {
			return drctDbtTx;
		}
			
        public void setDrctDbtTx(DrctDbtTx drctDbtTx) {
			this.drctDbtTx = drctDbtTx;
		}
			
        public Agent getDbtrAgt() {
			return dbtrAgt;
		}
			
        public void setDbtrAgt(Agent dbtrAgt) {
			this.dbtrAgt = dbtrAgt;
		}
			
        public Party getDbtr() {
			return dbtr;
		}
			
        public void setDbtr(Party dbtr) {
			this.dbtr = dbtr;
		}
			
        public Account getDbtrAcct() {
			return dbtrAcct;
		}
			
        public void setDbtrAcct(Account dbtrAcct) {
			this.dbtrAcct = dbtrAcct;
		}
			
        public String getRmtInf() {
			return rmtInf;
		}
			
        public void setRmtInf(String rmtInf) {
			this.rmtInf = rmtInf;
		}
	}
	
	
	@Embeddable
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Party {
		@XmlElement(name = "Nm")
		private String nm;
		
		public String getNm() {
			return nm;
		}
		
        public void setNm(String nm) {
			this.nm = nm;
		}
	}
	
	@Embeddable
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Account {
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
	
	@Embeddable
    @XmlAccessorType(XmlAccessType.FIELD)
	public static class AccountId {
		@XmlElement(name = "IBAN")
        private String iban;

        @XmlElement(name = "PrvtId")
        private String prvtId;

        public String getIban() {
			return iban;
		}

        public void setIban(String iban) {
			this.iban = iban;
		}

        public String getPrvtId() {
			return prvtId;
		}

        public void setPrvtId(String prvtId) {
			this.prvtId = prvtId;
		}
	}
	
	
	@Embeddable
    @XmlAccessorType(XmlAccessType.FIELD)
	public static class Agent {
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
	
	
	
	@Embeddable
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FinInstnId {
        @XmlElement(name = "BIC")
        private String bic;

        @XmlElement(name = "Id")
        private String id;

        public String getBic() {
			return bic;
		}

        public void setBic(String bic) {
			this.bic = bic;
		}

        public String getId() {
			return id;
		}

        public void setId(String id) {
			this.id = id;
		}
    }
	
	
	@Embeddable
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AccountSchemeId {
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
	
	
	@Embeddable
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PaymentTypeInfo {
        @XmlElement(name = "SvcLvl")
        @Embedded
        private ServiceLevel svcLvl;

        @XmlElement(name = "LclInstrm")
        @Embedded
        private LocalInstrument lclInstrm;

        @XmlElement(name = "SeqTp")
        private String seqTp;

        public ServiceLevel getSvcLvl() {
			return svcLvl;
		}

        public void setSvcLvl(ServiceLevel svcLvl) {
			this.svcLvl = svcLvl;
		}

        public LocalInstrument getLclInstrm() {
			return lclInstrm;
		}

        public void setLclInstrm(LocalInstrument lclInstrm) {
			this.lclInstrm = lclInstrm;
		}

        public String getSeqTp() {
			return seqTp;
		}

        public void setSeqTp(String seqTp) {
			this.seqTp = seqTp;
		}
    }
	
	
	@Embeddable
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ServiceLevel {
        @XmlElement(name = "Cd")
        private String cd;

        public String getCd() {
			return cd;
		}

        public void setCd(String cd) {
			this.cd = cd;
		}
    }
	
	
	
	@Embeddable
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class LocalInstrument {
        @XmlElement(name = "Cd")
        private String cd;

        public String getCd() {
			return cd;
		}

        public void setCd(String cd) {
			this.cd = cd;
		}
    }
	
	
	@Embeddable
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DrctDbtTx {
        @XmlElement(name = "MndtRltdInf")
        @Embedded
        private MndtRltdInf mndtRltdInf;

        public MndtRltdInf getMndtRltdInf() {
			return mndtRltdInf;
		}

        public void setMndtRltdInf(MndtRltdInf mndtRltdInf) {
			this.mndtRltdInf = mndtRltdInf;
		}
    }

    @Embeddable
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MndtRltdInf {
        @XmlElement(name = "MndtId")
        private String mndtId;

        @XmlElement(name = "DtOfSgntr")
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate dtOfSgntr;

        public String getMndtId() {
			return mndtId;
		}

        public void setMndtId(String mndtId) {
			this.mndtId = mndtId;
		}

        public LocalDate getDtOfSgntr() {
			return dtOfSgntr;
		}

        public void setDtOfSgntr(LocalDate dtOfSgntr) {
			this.dtOfSgntr = dtOfSgntr;
		}
    }

    @Embeddable
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class InstdAmt {
        @XmlValue
        private Double value;

        @XmlAttribute(name = "Ccy")
        private String ccy;

        public Double getValue() {
			return value;
		}

	    public void setValue(Double value) {
			this.value = value;
		}
	
	    public String getCcy() {
			return ccy;
		}
	
	    public void setCcy(String ccy) {
			this.ccy = ccy;
		}
    }
}