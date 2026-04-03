package fr.univrouen.sepa26.services;

import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

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
import jakarta.xml.bind.util.JAXBSource;

/**
 * Service métier pour la gestion des documents SEPA.
 * Cette couche contient la logique de validation, de transformation et de persistance.
 */
@Service
public class SepaService {

    @Autowired
    private DocumentRepository repository;

    /**
     * Valide un objet Document par rapport au schéma XSD.
     * @param doc Le document à valider.
     * @return true si le document est valide, false sinon.
     */
    public boolean validateXSD(Document doc) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Document.class);
            JAXBSource source = new JAXBSource(jc, doc);

            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new ClassPathResource("xml/sepa26.tp1.xsd").getURL());

            Validator validator = schema.newValidator();
            validator.validate(source);
            return true;
        } catch (Exception e) {
            System.err.println("Erreur de validation XSD : " + e.getMessage());
            return false;
        }
    }

    /**
     * Sauvegarde un document en base de données.
     * Réalise une vérification de l'unicité du PmtId (contrainte métier SEPA).
     * @param doc Le document à enregistrer.
     * @return Le document sauvegardé avec son ID généré, ou null si un doublon de PmtId est détecté.
     */
    public Document save(Document doc) {
        if (!doc.getDrctDbtTxInfs().isEmpty()) {
            String pmtId = doc.getDrctDbtTxInfs().get(0).getPmtId();
            if (pmtId != null && exists(pmtId)) {
                return null;
            }
        }
        return repository.save(doc);
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
}
