package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class GrpHdr {
	@XmlElement(name = "MsgId")
	private String msgId;
	
	@XmlElement(name = "CreDtTm")
	private String creDtTm;
	
	@XmlElement(name = "NbOfTxs")
	private int nbOfTxs;
	
	@XmlElement(name = "CtrlSum")
	private double ctrlSum;
	
	public String getMsgId() {
		return msgId;
	}
	
    public void setMsgId(String msgId) {
    	this.msgId = msgId;
    }
    
    public String getCreDtTm() {
    	return creDtTm;
    }
    
    public void setCreDtTm(String creDtTm) {
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
}
