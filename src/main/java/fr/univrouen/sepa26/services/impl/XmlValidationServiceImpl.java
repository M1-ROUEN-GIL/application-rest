package fr.univrouen.sepa26.services.impl;

import fr.univrouen.sepa26.dto.ValidationResult;
import fr.univrouen.sepa26.services.XmlValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.StringReader;

/**
 * Implémentation du service de validation XSD.
 * Pré-compile et met en cache l'instance de Schema pour garantir des performances optimales et le thread-safety.
 */
@Service
public class XmlValidationServiceImpl implements XmlValidationService {

    private static final Logger log = LoggerFactory.getLogger(XmlValidationServiceImpl.class);
    private static final String SCHEMA_PATH = "xml/sepa26.xsd";

    private final Schema schema;

    public XmlValidationServiceImpl() {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            this.schema = schemaFactory.newSchema(new ClassPathResource(SCHEMA_PATH).getURL());
            log.info("Schéma XSD chargé avec succès : {}", SCHEMA_PATH);
        } catch (Exception e) {
            log.error("Impossible de charger le schéma XSD : {}", SCHEMA_PATH, e);
            throw new IllegalStateException("Erreur d'initialisation du schéma XSD " + SCHEMA_PATH, e);
        }
    }

    @Override
    public ValidationResult validate(String xmlContent) {
        if (xmlContent == null || xmlContent.isBlank()) {
            return ValidationResult.error("Le contenu XML est vide");
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(new InputSource(new StringReader(xmlContent)));

            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(document));

            return ValidationResult.success();
        } catch (Exception e) {
            String errorMsg = "Erreur de validation XSD : " + e.getMessage();
            log.warn("Échec de la validation XML : {}", e.getMessage());
            return ValidationResult.error(errorMsg);
        }
    }

    @Override
    public boolean isValid(String xmlContent) {
        return validate(xmlContent).valid();
    }
}
