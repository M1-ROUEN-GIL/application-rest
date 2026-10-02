package fr.univrouen.sepa26.services.impl;

import fr.univrouen.sepa26.dto.ParseResult;
import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.services.XmlSerializationService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * Implémentation du service de sérialisation / désérialisation XML JAXB.
 * Met en cache l'instance de JAXBContext pour des opérations performantes et thread-safe.
 */
@Service
public class XmlSerializationServiceImpl implements XmlSerializationService {

    private static final Logger log = LoggerFactory.getLogger(XmlSerializationServiceImpl.class);

    private final JAXBContext jaxbContext;

    public XmlSerializationServiceImpl() {
        try {
            this.jaxbContext = JAXBContext.newInstance(Document.class);
            log.info("JAXBContext initialisé pour Document.class");
        } catch (JAXBException e) {
            log.error("Échec de l'initialisation de JAXBContext", e);
            throw new IllegalStateException("Impossible d'initialiser JAXBContext", e);
        }
    }

    @Override
    public String marshalToXml(Document document) {
        if (document == null) {
            return "<error>Document est null</error>";
        }

        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter writer = new StringWriter();
            marshaller.marshal(document, writer);
            return writer.toString();
        } catch (JAXBException e) {
            log.error("Erreur lors de la sérialisation XML du document", e);
            return "<error>" + e.getMessage() + "</error>";
        }
    }

    @Override
    public ParseResult<Document> unmarshalFromXml(String xmlContent) {
        if (xmlContent == null || xmlContent.isBlank()) {
            return ParseResult.error("Le flux XML est vide");
        }

        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Document document = (Document) unmarshaller.unmarshal(new StringReader(xmlContent));
            return ParseResult.success(document);
        } catch (Exception e) {
            String message = (e.getMessage() != null) ? e.getMessage() : e.toString();
            String errorMsg = "Erreur de parsing XML (JAXB) : " + message;
            log.warn("Échec du parsing XML : {}", errorMsg);
            return ParseResult.error(errorMsg);
        }
    }
}
