package fr.univrouen.sepa26.services;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.model.DocumentRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.util.JAXBSource;

/**
 * Service gérant la logique métier des flux SEPA.
 * Assure la persistance, la validation XSD et la transformation des données.
 */
@Service
public class SepaService {

    @Autowired
    private DocumentRepository repository;

    /**
     * Récupère les 10 dernières transactions enregistrées.
     * @return Liste de documents
     */
    public List<Document> getLast10() {
        return repository.findLast10();
    }

    /**
     * Récupère une transaction par son identifiant unique.
     * @param id Identifiant numérique
     * @return Le document trouvé ou null
     */
    public Document getById(long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Valide un document XML par rapport au schéma XSD.
     * @param doc Le document à valider
     * @return true si valide, false sinon
     */
    public boolean validateXSD(Document doc) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream xsdStream = new ClassPathResource("xml/sepa26.tp1.xsd").getInputStream();
            Schema schema = factory.newSchema(new StreamSource(xsdStream));
            Validator validator = schema.newValidator();
            
            JAXBContext jc = JAXBContext.newInstance(Document.class);
            JAXBSource source = new JAXBSource(jc, doc);
            validator.validate(source);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur de validation XSD : " + e.getMessage());
            return false;
        }
    }

    /**
     * Enregistre un nouveau document en base après vérification d'unicité.
     * @param doc Le document à enregistrer
     * @return Le document enregistré ou null en cas de doublon (PmtId)
     */
    public Document save(Document doc) {
        // Vérification de l'unicité du PmtId pour chaque transaction
        for (Document.DrctDbtTxInf inf : doc.getDrctDbtTxInfs()) {
            if (repository.findByPmtId(inf.getPmtId()).isPresent()) {
                return null;
            }
        }
        return repository.save(doc);
    }

    /**
     * Supprime une transaction de la base.
     * @param id Identifiant de la transaction
     * @return true si supprimé, false si inexistant
     */
    public boolean delete(long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Transforme un document XML en HTML via XSLT.
     * @param doc Le document à transformer
     * @return La chaîne HTML résultante
     */
    public String transformToHtml(Document doc) {
        try {
            // Marshalling JAXB vers String
            JAXBContext context = JAXBContext.newInstance(Document.class);
            Marshaller marshaller = context.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(doc, writer);
            String xml = writer.toString();

            // Transformation XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            InputStream xsltStream = new ClassPathResource("xml/sepa26.tp3.xslt").getInputStream();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltStream));
            
            StringWriter resultWriter = new StringWriter();
            transformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(resultWriter));
            
            return resultWriter.toString();
        } catch (Exception e) {
            return "<html><body><h1>Erreur lors de la transformation</h1><p>" + e.getMessage() + "</p></body></html>";
        }
    }
}
