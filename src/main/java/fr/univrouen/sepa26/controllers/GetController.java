package fr.univrouen.sepa26.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.model.DocumentList;
import fr.univrouen.sepa26.model.SepaResponse;
import fr.univrouen.sepa26.services.SepaService;

/**
 * Contrôleur gérant les requêtes de consultation (GET).
 * Utilise Thymeleaf pour le rendu HTML et JAXB pour le rendu XML.
 */
@Controller
@RequestMapping("/sepa26")
public class GetController {

    @Autowired
    private SepaService sepaService;

    /**
     * Affiche le résumé des 10 dernières transactions au format XML.
     */
    @GetMapping(value = "/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public DocumentList getResumeXml() {
        return new DocumentList(sepaService.getLast10());
    }

    /**
     * Affiche le résumé des 10 dernières transactions au format HTML via Thymeleaf.
     */
    @GetMapping(value = "/resume/html", produces = MediaType.TEXT_HTML_VALUE)
    public String getResumeHtml(Model model) {
        List<Document> docs = sepaService.getLast10();
        model.addAttribute("documents", docs);
        // summary.html
        return "summary";
    }

    /**
     * Récupère le détail complet d'une transaction au format XML.
     */
    @GetMapping(value = "/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public Object getXmlDetail(@PathVariable long id) {
        Document doc = sepaService.getById(id);
        if (doc == null) {
        	// Retourne un objet, pas du texte brut
            return new SepaResponse(id, "ERROR");
        }
        return doc;
    }

    /**
     * Récupère le détail complet d'une transaction au format HTML via XSLT.
     */
    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getHtmlDetail(@PathVariable long id) {
        Document doc = sepaService.getById(id);
        if (doc == null) {
            return "<html><body><h1>ERROR</h1><p>Identifiant " + id + " incorrect</p></body></html>";
        }
        return sepaService.transformToHtml(doc);
    }
}
