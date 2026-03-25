package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "CstmrDrctDbtintitn")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Sepa26 {
	@XmlAttribute
	private String Ccy;
	@XmlElement
	private String MsgId;
	@XmlElement
	private String CreDtTm;
	
	public Sepa26(String ccy, String msgid, String credttm) {
		super();
		this.Ccy = ccy;
		this.MsgId = msgid;
		this.CreDtTm = credttm;
	}
	public Sepa26() {
	}
	
	public String toString() {
		return "Ccy : " + Ccy + "\n(" + MsgId + ") Le = " + CreDtTm; 
	}
}
