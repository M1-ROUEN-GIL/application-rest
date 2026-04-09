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
import fr.univrouen.sepa26.dto.SepaResponse;
import fr.univrouen.sepa26.services.SepaService;

/**
 * Contrôleur REST gérant les modifications de données.
 * Traite les envois (POST) et les suppressions (DELETE) de documents.
 */
@RestController
@RequestMapping("/sepa26")
public class PostController {

    @Autowired
    private SepaService sepaService;

    /**
     * Ajoute un nouveau document SEPA.
     * Réalise une validation XSD et vérifie l'unicité du PmtId via le service.
     * @param doc Le document envoyé dans le corps de la requête.
     * @return Un objet SepaResponse (INSERTED ou ERROR).
     */
    @PostMapping(value = "/insert",
    		consumes = MediaType.APPLICATION_XML_VALUE,
    		produces = MediaType.APPLICATION_XML_VALUE)
    public SepaResponse insert(@RequestBody String xmlRaw) {
    	try {
    		if (!sepaService.validateXSDRaw(xmlRaw)) {
                return new SepaResponse("ERROR");
            }
    		Document doc = SepaService.parseXml(xmlRaw);
    		if (doc == null) {
    			return new SepaResponse("ERROR");
    		}
    		Document saved = sepaService.save(doc);
    		if (saved == null) {
    			return new SepaResponse("ERROR");
    		}
    		return new SepaResponse(saved.getId(), "INSERTED");
    	} catch (Exception e) {
    		return new SepaResponse("ERROR");
    	}
    }
    
    

    /**
     * Supprime un document par son identifiant.
     * @param id L'ID du document à supprimer.
     * @return Un objet SepaResponse (DELETED ou ERROR).
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