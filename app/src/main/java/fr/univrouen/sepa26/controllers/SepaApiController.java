package fr.univrouen.sepa26.controllers;

import fr.univrouen.sepa26.dto.DocumentList;
import fr.univrouen.sepa26.dto.ParseResult;
import fr.univrouen.sepa26.dto.SearchResponse;
import fr.univrouen.sepa26.dto.SepaResponse;
import fr.univrouen.sepa26.dto.ValidationResult;
import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.services.SepaService;
import fr.univrouen.sepa26.services.XmlSerializationService;
import fr.univrouen.sepa26.services.XmlValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Contrôleur REST pour l'API SEPA ISO 20022.
 * Regroupe tous les points d'accès fournissant ou consommant du XML sous /sepa26.
 * Respecte le principe de responsabilité unique (SRP) et l'inversion de dépendances (DIP).
 */
@RestController
@RequestMapping("/sepa26")
public class SepaApiController {

    private final SepaService sepaService;
    private final XmlValidationService xmlValidationService;
    private final XmlSerializationService xmlSerializationService;

    @Autowired
    public SepaApiController(SepaService sepaService,
                             XmlValidationService xmlValidationService,
                             XmlSerializationService xmlSerializationService) {
        this.sepaService = Objects.requireNonNull(sepaService, "sepaService must not be null");
        this.xmlValidationService = Objects.requireNonNull(xmlValidationService, "xmlValidationService must not be null");
        this.xmlSerializationService = Objects.requireNonNull(xmlSerializationService, "xmlSerializationService must not be null");
    }

    /**
     * Retourne la liste des 10 derniers documents au format XML.
     *
     * @return Objet DocumentList sérialisé en XML
     */
    @GetMapping(value = "/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public DocumentList getResumeXml() {
        return new DocumentList(sepaService.getLast10());
    }

    /**
     * Retourne le détail d'un document au format XML ou une erreur si introuvable.
     *
     * @param id L'identifiant technique du document
     * @return Le Document sérialisé ou une SepaResponse ERROR
     */
    @GetMapping(value = "/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public Object getXmlDetail(@PathVariable("id") long id) {
        Optional<Document> doc = sepaService.getById(id);
        if (doc.isPresent()) {
            return doc.get();
        }
        return new SepaResponse(id, "ERROR");
    }

    /**
     * Ajoute un nouveau document SEPA envoyé en XML brut.
     * Réalise une validation XSD optionnelle et vérifie l'unicité du PmtId.
     *
     * @param xmlRaw   Flux XML envoyé dans le corps de la requête
     * @param validate Indique si la validation XSD doit être effectuée (défaut: true)
     * @return SepaResponse avec status INSERTED et ID ou status ERROR
     */
    @PostMapping(value = "/insert",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE)
    public SepaResponse insert(@RequestBody String xmlRaw,
                               @RequestParam(value = "validate", defaultValue = "true") boolean validate) {
        try {
            if (validate) {
                ValidationResult validation = xmlValidationService.validate(xmlRaw);
                if (!validation.valid()) {
                    return new SepaResponse("ERROR", validation.errorMessage());
                }
            }

            ParseResult<Document> parseResult = xmlSerializationService.unmarshalFromXml(xmlRaw);
            if (!parseResult.isSuccess()) {
                return new SepaResponse("ERROR", parseResult.errorMessage());
            }

            Document saved = sepaService.save(parseResult.data());
            if (saved == null) {
                return new SepaResponse("ERROR", "Doublon détecté : un PmtId identique existe déjà en base");
            }

            return new SepaResponse(saved.getId(), "INSERTED");
        } catch (Exception e) {
            return new SepaResponse("ERROR", "Erreur interne : " + e.getMessage());
        }
    }

    /**
     * Supprime un document par son identifiant technique.
     *
     * @param id L'identifiant du document à supprimer
     * @return SepaResponse DELETED ou ERROR
     */
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public SepaResponse delete(@PathVariable("id") long id) {
        if (sepaService.delete(id)) {
            return new SepaResponse(id, "DELETED");
        }
        return new SepaResponse("ERROR");
    }

    /**
     * Recherche des documents selon des critères optionnels de date et/ou de montant minimal.
     *
     * @param date Date minimale (YYYY-MM-DD)
     * @param sum  Montant minimal (CtrlSum)
     * @return ResponseEntity avec SearchResponse (OK, NONE ou ERROR)
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<SearchResponse> search(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "sum", required = false) Double sum) {
        try {
            LocalDateTime dateTime = (date != null) ? date.atStartOfDay() : null;
            List<Document> results = sepaService.search(dateTime, sum);
            if (results.isEmpty()) {
                return ResponseEntity.ok(new SearchResponse("NONE"));
            } else {
                DocumentList docList = new DocumentList(results);
                return ResponseEntity.ok(new SearchResponse("OK", docList));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SearchResponse("ERROR"));
        }
    }
}
