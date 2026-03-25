package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DrctDbtTxInf")
@XmlAccessorType(XmlAccessType.FIELD)
public class DrctDbtTxInf {
	@XmlElement(name = "PmtId")
	private String pmtId;
	
	@XmlElement(name = "InstdAmt")
	private String instdAmt;
	
	@XmlElement(name = "RmtInf")
	private String rmtInf;
	
	public String getPmtId() {
		return pmtId;
	}
	
    public void setPmtId(String pmtId) {
    	this.pmtId = pmtId;
    }

    public String getInstdAmt() {
    	return instdAmt;
    }
    
    public void setInstdAmt(String instdAmt) {
    	this.instdAmt = instdAmt;
    }

    public String getRmtInf() {
    	return rmtInf;
    }
    
    public void setRmtInf(String rmtInf) {
    	this.rmtInf = rmtInf;
    }
}
