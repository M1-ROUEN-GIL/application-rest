package fr.univrouen.sepa26.controllers;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.services.EndpointDocumentationService;
import fr.univrouen.sepa26.services.SepaService;
import fr.univrouen.sepa26.services.XsltTransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;
import java.util.Optional;

/**
 * Contrôleur Web pour l'interface graphique Thymeleaf.
 * Gère les pages HTML du service (accueil, aide, formulaire de transfert, résumé et vue détaillée XSLT).
 * Respecte le principe de responsabilité unique (SRP) et l'inversion de dépendances (DIP).
 */
@Controller
public class SepaWebController {

    private final SepaService sepaService;
    private final XsltTransformationService xsltTransformationService;
    private final EndpointDocumentationService endpointDocumentationService;

    @Autowired
    public SepaWebController(SepaService sepaService,
                             XsltTransformationService xsltTransformationService,
                             EndpointDocumentationService endpointDocumentationService) {
        this.sepaService = Objects.requireNonNull(sepaService, "sepaService must not be null");
        this.xsltTransformationService = Objects.requireNonNull(xsltTransformationService, "xsltTransformationService must not be null");
        this.endpointDocumentationService = Objects.requireNonNull(endpointDocumentationService, "endpointDocumentationService must not be null");
    }

    /**
     * Page d'accueil de l'application.
     *
     * @param model Modèle UI Spring
     * @return Nom de la vue "index"
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("projectName", "Projet SEPA26");
        model.addAttribute("version", "1.0.0");
        model.addAttribute("developer", "Florian Pépin & Umm-Habibah Ouattara");
        model.addAttribute("universityLogo", "https://www.choisirlanormandie.fr/app/uploads/2024/08/logo-universite-de-rouen.png");
        return "index";
    }

    /**
     * Page d'aide listant l'ensemble des points d'accès documentés.
     *
     * @param model Modèle UI Spring
     * @return Nom de la vue "help"
     */
    @GetMapping("/help")
    public String help(Model model) {
        model.addAttribute("endpoints", endpointDocumentationService.getEndpointsDocumentation());
        return "help";
    }

    /**
     * Formulaire de téléversement et transfert de flux XML.
     *
     * @return Nom de la vue "transfert"
     */
    @GetMapping("/transfert")
    public String transfert() {
        return "transfert";
    }

    /**
     * Affiche la liste des 10 dernières transactions au format HTML.
     *
     * @param model Modèle UI Spring
     * @return Nom de la vue "summary"
     */
    @GetMapping(value = "/sepa26/resume/html", produces = MediaType.TEXT_HTML_VALUE)
    public String getResumeHtml(Model model) {
        model.addAttribute("documents", sepaService.getLast10());
        return "summary";
    }

    /**
     * Récupère le détail d'un document en HTML transformé par XSLT.
     *
     * @param id Identifiant technique du document
     * @return Rendu HTML si trouvé, ou flux XML d'erreur
     */
    @GetMapping(value = "/sepa26/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getHtmlDetail(@PathVariable("id") long id) {
        Optional<Document> doc = sepaService.getById(id);
        if (doc.isPresent()) {
            try {
                String xml = sepaService.convertToXml(doc.get());
                return xsltTransformationService.transformSepa(xml);
            } catch (Exception e) {
                return buildXmlError(String.valueOf(id), e.getMessage());
            }
        }
        return buildXmlError(String.valueOf(id), "DOC NOT FOUND");
    }

    private String buildXmlError(String id, String message) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<error>\n"
                + "  <status>" + id + "</status>\n"
                + "  <message>" + message + "</message>\n"
                + "</error>";
    }
}
