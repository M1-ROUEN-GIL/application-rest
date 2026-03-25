package fr.univrouen.sepa26.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PmtInf {
	@XmlElement(name = "PmtInfId")
	private String pmtInfId;
	
	@XmlElement(name = "NbOfTxs")
	private int nbOfTxs;
	
	@XmlElement(name = "CtrlSum")
	private double ctrlSum;
	
	@XmlElement(name = "ReqdColltnDt")
	private String reqdColltnDt;
	
	@XmlElement(name = "DrctDbtTxInf")
	private List<DrctDbtTxInf> transactions;
	
	public String getPmtInfId() {
		return pmtInfId;
	}
	
    public void setPmtInfId(String pmtInfId) {
    	this.pmtInfId = pmtInfId;
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

    public String getReqdColltnDt() {
    	return reqdColltnDt;
    }
    
    public void setReqdColltnDt(String d) {
    	this.reqdColltnDt = d;
    }

    public List<DrctDbtTxInf> getTransactions() {
    	return transactions;
    }
    
    public void setTransactions(List<DrctDbtTxInf> t) {
    	this.transactions = t;
    }
}
