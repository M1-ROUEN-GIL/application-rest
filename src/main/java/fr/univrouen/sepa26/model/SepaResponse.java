package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.*;

@XmlRootElement(name = "SepaResponse")
@JacksonXmlRootElement(localName = "SepaResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "status"})
public class SepaResponse {

    @XmlElement
    @JacksonXmlProperty(localName = "id")
    private Long id;

    @XmlElement
    @JacksonXmlProperty(localName = "status")
    private String status;

    public SepaResponse() {}

    public SepaResponse(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    public SepaResponse(String status) {
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
