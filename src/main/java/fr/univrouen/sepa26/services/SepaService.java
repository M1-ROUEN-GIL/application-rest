package fr.univrouen.sepa26.services;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.repository.DocumentRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

/**
 * Service métier pour la gestion des documents SEPA.
 * Cette couche contient la logique de validation, de transformation et de persistance.
 */
@Service
public class SepaService {

    @Autowired
    private DocumentRepository repository;

    /**
     * Classe interne pour encapsuler le résultat de validation XSD.
     */
    public static class ValidationResult {
        public final boolean valid;
        public final String errorMessage;

        public ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public ValidationResult(boolean valid) {
            this(valid, null);
        }
    }

    /**
     * Sauvegarde un document en base de données.
     * Réalise une vérification de l'unicité du PmtId (contrainte métier SEPA).
     * @param doc Le document à enregistrer.
     * @return Le document sauvegardé avec son ID généré, ou null si un doublon de PmtId est détecté.
     */
    public Document save(Document doc) {
        try {
            if (doc.getCstmrDrctDbtInitn() == null) {
                return null;
            }
            for (Document.PmtInf pmt : doc.getCstmrDrctDbtInitn().getPmtInfs()) {
                if (pmt.getDrctDbtTxInfs() == null) continue;
                for (Document.DrctDbtTxInf tx : pmt.getDrctDbtTxInfs()) {
                    String pmtId = tx.getPmtId();
                    if (pmtId != null && exists(pmtId)) {
                        return null;
                    }
                }
            }
            return repository.save(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Vérifie si un identifiant de paiement existe déjà en base.
     * @param pmtId L'identifiant à vérifier.
     * @return true s'il existe déjà, false sinon.
     */
    public boolean exists(String pmtId) {
        return repository.findByPmtId(pmtId).isPresent();
    }

    /**
     * Récupère un document par son identifiant technique.
     * @param id L'ID du document.
     * @return Un Optional contenant le document.
     */
    public Optional<Document> getById(long id) {
        return repository.findById(id);
    }

    /**
     * Récupère la liste des 10 derniers documents.
     * @return Liste de documents.
     */
    public List<Document> getLast10() {
        return repository.findLast10();
    }

    /**
     * Supprime un document par son ID.
     * @param id L'ID du document à supprimer.
     * @return true si supprimé, false si le document n'existe pas.
     */
    public boolean delete(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Transforme un objet Document en chaîne XML formatée.
     * @param doc Le document à transformer.
     * @return La chaîne XML.
     */
    public String convertToXml(Document doc) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Document.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(doc, sw);
            return sw.toString();
        } catch (Exception e) {
            return "<error>" + e.getMessage() + "</error>";
        }
    }
    
    /**
     * Valide un flux XML brut en le comparant au schéma XSD.
     * @param xmlContent contenu XML.
     * @return true si le XML est conforme, false sinon.
     */
    public boolean validateXSDRaw(String xmlContent) {
        return validateXSDRawWithDetails(xmlContent).valid;
    }

    /**
     * Valide un flux XML brut en retournant un résultat détaillé.
     * @param xmlContent contenu XML.
     * @return ValidationResult avec le statut et le message d'erreur.
     */
    public ValidationResult validateXSDRawWithDetails(String xmlContent) {
        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new ClassPathResource("xml/sepa26.tp1.xsd").getURL());

            Validator validator = schema.newValidator();

            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(new org.xml.sax.InputSource(new StringReader(xmlContent)));

            validator.validate(new javax.xml.transform.dom.DOMSource(document));

            return new ValidationResult(true);

        } catch (Exception e) {
            String errorMsg = "Erreur de validation XSD : " + e.getMessage();
            System.err.println(errorMsg);
            return new ValidationResult(false, errorMsg);
        }
    }
    
    /**
     * Classe interne pour le résultat de parsing XML.
     */
    public static class ParseResult {
        public final Document document;
        public final String errorMessage;

        public ParseResult(Document document) {
            this.document = document;
            this.errorMessage = null;
        }

        public ParseResult(String errorMessage) {
            this.document = null;
            this.errorMessage = errorMessage;
        }
    }

    /**
     * Désérialise un flux XML brut en un objet Document.
     * @param xmlContent contenu XML représentant un Document.
     * @return l'objet Document désérialisé, ou null en cas d'échec.
     */
    public static Document parseXml(String xmlContent) {
        return parseXmlWithDetails(xmlContent).document;
    }

    /**
     * Désérialise un flux XML brut avec capture du message d'erreur.
     * @param xmlContent contenu XML représentant un Document.
     * @return ParseResult avec le document ou le message d'erreur.
     */
    public static ParseResult parseXmlWithDetails(String xmlContent) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Document.class);
            Document doc = (Document) jc.createUnmarshaller()
                .unmarshal(new java.io.StringReader(xmlContent));
            return new ParseResult(doc);
        } catch (Exception e) {
            String message = (e.getMessage() != null) ? e.getMessage() : e.toString();
            String errorMsg = "Erreur de parsing XML (JAXB) : " + message;
            System.err.println(errorMsg);
            return new ParseResult(errorMsg);
        }
    }
    
    /**
     * Effectue la recherche des documents dans la base selon un date et/ou un montant.
     * @param date date minimale pour la balise creDtTm, ou null si pas utilisé.
     * @param sum montant minimal pour la balise ctrlSum, ou null si pas utilisé.
     * @return
     */
    public List<Document> search(LocalDateTime date, Double sum) {
    	return repository.search(date, sum);
    }
    
    
    /*
     * /**
     * Valide un objet Document par rapport au schéma XSD.
     * @param doc Le document à valider.
     * @return true si le document est valide, false sinon.
     
     * public boolean validateXSD(Document doc) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Document.class);
            JAXBSource source = new JAXBSource(jc, doc);

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new ClassPathResource("xml/sepa26.tp1.xsd").getURL());

            Validator validator = schema.newValidator();
            validator.validate(source);
            return true;
        } catch (Exception e) {
            //System.err.println("Erreur de validation XSD : " + e.getMessage());
        	e.printStackTrace();
            return false;
        }
    }
    public boolean validateXSD(Document doc) {
        try {
            String xml = convertToXml(doc);

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new ClassPathResource("xml/sepa26.tp1.xsd").getURL());

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xml)));

            return true;
        } catch (Exception e) {
            //e.printStackTrace();
        	System.out.println("Erreur de validation XSD : " + e.getMessage());
            return false;
        }
    }
    */
}