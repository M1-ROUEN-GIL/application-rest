package fr.univrouen.sepa26.controllers;

import java.io.StringWriter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.dto.DocumentList;
import fr.univrouen.sepa26.dto.SepaResponse;
import fr.univrouen.sepa26.services.SepaService;
import fr.univrouen.sepa26.util.XsltTransformer;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

/**
 * Contrôleur gérant les requêtes de consultation (GET).
 * Fournit des données au format HTML (via Thymeleaf) ou XML (via JAXB).
 */
@Controller
@RequestMapping("/sepa26")
public class GetController {

    @Autowired
    private SepaService sepaService;
    
    @Autowired
    private XsltTransformer xsltTransformer;

    /**
     * Retourne la liste des 10 derniers documents au format XML.
     * @return Un objet DocumentList sérialisé en XML.
     */
    @GetMapping(value = "/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public DocumentList getResumeXml() {
        return new DocumentList(sepaService.getLast10());
    }

    /**
     * Affiche la liste des 10 derniers documents au format HTML.
     * @param model Le modèle Spring UI.
     * @return Le nom de la vue "summary".
     */
    @GetMapping(value = "/resume/html", produces = MediaType.TEXT_HTML_VALUE)
    public String getResumeHtml(Model model) {
        model.addAttribute("documents", sepaService.getLast10());
        return "summary";
    }

    /**
     * Retourne le détail d'un document au format XML.
     * @param id L'identifiant technique du document.
     * @return Le document en XML ou une réponse d'erreur.
     */
    @GetMapping(value = "/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public Object getXmlDetail(@PathVariable long id) {
        Optional<Document> doc = sepaService.getById(id);
        if (doc.isPresent()) {
            return doc.get();
        }
        return new SepaResponse(id, "ERROR");
    }

    /**
     * Récupère le détail d'un document en HTML via son identifiant.
     * @param id identifiant du document à récupèrer.
     * @return chaine HTML contenant le détail du document si trouvé, ou flux XML d'erreur
     * 	si le document n'existe pas ou si erreur.
     */
    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getHtmlDetail(@PathVariable long id) {
    	Optional<Document> doc = sepaService.getById(id);
    	if (doc.isPresent()) {
    		try {
    			JAXBContext context = JAXBContext.newInstance(Document.class);
    			Marshaller marshaller = context.createMarshaller();
    			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    			StringWriter xmlWriter = new StringWriter();
    			marshaller.marshal(doc.get(), xmlWriter);
    			return xsltTransformer.transformSepa(xmlWriter.toString());
    		} catch (Exception e) {
    			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
    	    			+ "<error>\n"
    	    			+ "  <status>id</status>"
    	    			+ "  <message>" + e.getMessage() + "</message>"
    	    			+ "</error>";
    		}
    	}
    	return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
    			+ "<error>\n"
    			+ "  <status>id</status>"
    			+ "  <message>DOC NOT FOUND</message>"
    			+ "</error>";
    }
    
    /*
     *
     * Affiche le détail d'un document sur une page HTML.
     * @param id L'identifiant technique du document.
     * @param model Le modèle Spring UI.
     * @return La vue "summary" ou "error_sepa".
     
    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String getHtmlDetail(@PathVariable long id, Model model) {
        Optional<Document> doc = sepaService.getById(id);
        if (doc.isPresent()) {
            // summary.html attend une liste nommée "documents"
            //model.addAttribute("documents", List.of(doc.get()));
        	model.addAttribute("document", doc.get());
            return "detail";
        }
        model.addAttribute("id", id);
        return "error_sepa";
    }*/
}