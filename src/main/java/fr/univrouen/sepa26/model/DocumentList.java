package fr.univrouen.sepa26.model;

import jakarta.xml.bind.annotation.*;
import com.fasterxml.jackson.dataformat.xml.annotation.*;
import java.util.List;

@XmlRootElement(name = "DocumentList")
@JacksonXmlRootElement(localName = "DocumentList")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentList {

    @XmlElement(name = "Document")
    @JacksonXmlProperty(localName = "Document")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Document> documents;

    public DocumentList() {}

    public DocumentList(List<Document> documents) {
        this.documents = documents;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
