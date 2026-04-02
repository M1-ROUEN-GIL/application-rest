package fr.univrouen.sepa26.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.model.SepaResponse;
import fr.univrouen.sepa26.services.SepaService;

/**
 * Contrôleur gérant les modifications de données (POST, DELETE).
 */
@RestController
@RequestMapping("/sepa26")
public class PostController {

    @Autowired
    private SepaService sepaService;

    /**
     * Insère un nouveau flux SEPA dans la base.
     * Valide le flux via XSD et vérifie l'unicité avant l'insertion.
     */
    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public SepaResponse insert(@RequestBody Document doc) {
        // 1. Validation XSD
        if (!sepaService.validateXSD(doc)) {
            return new SepaResponse("ERROR");
        }

        // 2. Enregistrement (vérifie l'unicité du PmtId)
        Document saved = sepaService.save(doc);
        if (saved == null) {
            return new SepaResponse("ERROR");
        }

        return new SepaResponse(saved.getId(), "INSERTED");
    }

    /**
     * Supprime une transaction par son identifiant.
     */
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public SepaResponse delete(@PathVariable long id) {
        if (sepaService.delete(id)) {
            return new SepaResponse(id, "DELETED");
        } else {
            return new SepaResponse("ERROR");
        }
    }
}
