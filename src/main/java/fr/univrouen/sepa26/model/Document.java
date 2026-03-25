package fr.univrouen.sepa26.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Document")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document {
	@XmlElement(name = "GrpHdr")
	private GrpHdr grpHdr;
	
	@XmlElement(name = "PmtInf")
	private List<PmtInf> pmtInf;
	
	public GrpHdr getGrpHdr() {
		return grpHdr;
	}
	
	public void setGrpHdr(GrpHdr grpHdr) {
		this.grpHdr = grpHdr;
	}
	
	public List<PmtInf> getPmtInf() {
		return pmtInf;
	}
	
	public void setPmtInf(List<PmtInf> pmtInf) {
		this.pmtInf = pmtInf;
	}
}
