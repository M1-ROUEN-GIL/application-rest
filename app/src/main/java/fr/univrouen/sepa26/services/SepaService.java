package fr.univrouen.sepa26.services;

import fr.univrouen.sepa26.dto.ParseResult;
import fr.univrouen.sepa26.dto.ValidationResult;
import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.repository.DocumentRepository;
import fr.univrouen.sepa26.services.impl.XmlSerializationServiceImpl;
import fr.univrouen.sepa26.services.impl.XmlValidationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service métier central pour la gestion des documents SEPA.
 * Coordonne les règles métier d'unicité, la persistance JPA et délègue
 * les traitements XML spécialisés aux services dédiés (SRP & DIP).
 */
@Service
public class SepaService {

    private static final Logger LOG = LoggerFactory.getLogger(SepaService.class);

    private final DocumentRepository repository;
    private final XmlValidationService xmlValidationService;
    private final XmlSerializationService xmlSerializationService;

    @Autowired
    public SepaService(DocumentRepository repository,
                       XmlValidationService xmlValidationService,
                       XmlSerializationService xmlSerializationService) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.xmlValidationService = xmlValidationService != null ? xmlValidationService : new XmlValidationServiceImpl();
        this.xmlSerializationService = xmlSerializationService != null ? xmlSerializationService : new XmlSerializationServiceImpl();
    }

    /**
     * Constructeur utilitaire pour les tests unitaires isolant le repository.
     */
    public SepaService(DocumentRepository repository) {
        this(repository, new XmlValidationServiceImpl(), new XmlSerializationServiceImpl());
    }

    /**
     * Sauvegarde un document en base de données.
     * Réalise une vérification de l'unicité du PmtId (contrainte métier SEPA).
     *
     * @param doc Le document à enregistrer.
     * @return Le document sauvegardé avec son ID généré, ou null si un doublon de PmtId est détecté.
     */
    public Document save(Document doc) {
        try {
            if (doc == null || doc.getCstmrDrctDbtInitn() == null) {
                return null;
            }
            if (doc.getCstmrDrctDbtInitn().getPmtInfs() != null) {
                for (Document.PmtInf pmt : doc.getCstmrDrctDbtInitn().getPmtInfs()) {
                    if (pmt.getDrctDbtTxInfs() == null) {
                        continue;
                    }
                    for (Document.DrctDbtTxInf tx : pmt.getDrctDbtTxInfs()) {
                        String pmtId = tx.getPmtId();
                        if (pmtId != null && exists(pmtId)) {
                            LOG.warn("Doublon détecté pour le PmtId: {}", pmtId);
                            return null;
                        }
                    }
                }
            }
            return repository.save(doc);
        } catch (Exception e) {
            LOG.error("Erreur lors de la sauvegarde du document", e);
            return null;
        }
    }

    /**
     * Vérifie si un identifiant de paiement existe déjà en base.
     *
     * @param pmtId L'identifiant à vérifier.
     * @return true s'il existe déjà, false sinon.
     */
    public boolean exists(String pmtId) {
        return repository.findByPmtId(pmtId).isPresent();
    }

    /**
     * Récupère un document par son identifiant technique.
     *
     * @param id L'ID du document.
     * @return Un Optional contenant le document.
     */
    public Optional<Document> getById(long id) {
        return repository.findById(id);
    }

    /**
     * Récupère la liste des 10 derniers documents.
     *
     * @return Liste de documents.
     */
    public List<Document> getLast10() {
        return repository.findTop10ByOrderByIdDocDesc();
    }

    /**
     * Supprime un document par son ID.
     *
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
     * Effectue la recherche des documents dans la base selon une date et/ou un montant.
     *
     * @param date date minimale pour la balise creDtTm, ou null si non utilisé.
     * @param sum  montant minimal pour la balise ctrlSum, ou null si non utilisé.
     * @return liste des documents correspondants
     */
    public List<Document> search(LocalDateTime date, Double sum) {
        return repository.search(date, sum);
    }

    /**
     * Transforme un objet Document en chaîne XML formatée.
     *
     * @param doc Le document à sérialiser.
     * @return La chaîne XML.
     */
    public String convertToXml(Document doc) {
        return xmlSerializationService.marshalToXml(doc);
    }

    /**
     * Valide un flux XML brut contre le schéma XSD.
     *
     * @param xmlContent contenu XML brut.
     * @return true si le XML est conforme, false sinon.
     */
    public boolean validateXSDRaw(String xmlContent) {
        return xmlValidationService.isValid(xmlContent);
    }

    /**
     * Valide un flux XML brut avec détails de validation.
     *
     * @param xmlContent contenu XML brut.
     * @return ValidationResult contenant le statut et l'éventuelle erreur.
     */
    public ValidationResult validateXSDRawWithDetails(String xmlContent) {
        return xmlValidationService.validate(xmlContent);
    }

    /**
     * Désérialise un flux XML brut en Document.
     *
     * @param xmlContent contenu XML représentant un Document.
     * @return l'objet Document désérialisé, ou null en cas d'échec.
     */
    public Document parseXml(String xmlContent) {
        return xmlSerializationService.unmarshalFromXml(xmlContent).data();
    }

    /**
     * Désérialise un flux XML brut avec rapport d'erreur.
     *
     * @param xmlContent contenu XML représentant un Document.
     * @return ParseResult contenant le document ou le message d'erreur.
     */
    public ParseResult<Document> parseXmlWithDetails(String xmlContent) {
        return xmlSerializationService.unmarshalFromXml(xmlContent);
    }
}