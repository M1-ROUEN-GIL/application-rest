package fr.univrouen.sepa26.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import fr.univrouen.sepa26.model.Document;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Objet de transfert de données (DTO) pour encapsuler une liste de documents.
 * Principalement utilisé pour le rendu XML de la liste des documents récents.
 */
@XmlRootElement(name = "DocumentList")
@JacksonXmlRootElement(localName = "DocumentList")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentList {

    /** Liste des documents encapsulés */
    @XmlElement(name = "Document")
    @JacksonXmlProperty(localName = "Document")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Document> documents;

    public DocumentList() {
    }

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