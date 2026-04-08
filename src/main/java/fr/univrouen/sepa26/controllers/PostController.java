package fr.univrouen.sepa26.controllers;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import jakarta.xml.bind.JAXBContext;

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
    @PostMapping(value = "/insert", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    /*public SepaResponse insert(@RequestBody Document doc) {
        // 1. Validation XSD
        if (!sepaService.validateXSD(doc)) {
            return new SepaResponse("ERROR");
        }

        // 2. Sauvegarde (la vérification métier PmtId est faite dans le service)
        Document saved = sepaService.save(doc);
        if (saved == null) {
            return new SepaResponse("ERROR");
        }
        return new SepaResponse(saved.getId(), "INSERTED");
    }*/
    
    public SepaResponse insert(@RequestBody String xmlRaw) {
    	//System.out.println("XML reçu : " + xmlRaw);
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