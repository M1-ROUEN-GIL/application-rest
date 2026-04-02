package fr.univrouen.sepa26.controllers;

import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.model.DocumentList;
import fr.univrouen.sepa26.services.SepaService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

/**
 * Contrôleur gérant les requêtes de consultation (GET).
 */
@RestController
@RequestMapping("/sepa26")
public class GetController {

    @Autowired
    private SepaService sepaService;

    /**
     * Affiche le résumé des 10 dernières transactions au format XML.
     */
    @GetMapping(value = "/resume/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public DocumentList getResumeXml() {
        return new DocumentList(sepaService.getLast10());
    }

    /**
     * Affiche le résumé des 10 dernières transactions au format HTML.
     */
    @GetMapping(value = "/resume/html", produces = MediaType.TEXT_HTML_VALUE)
    public String getResumeHtml() {
        List<Document> docs = sepaService.getLast10();
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Résumé des transactions</title></head><body>");
        html.append("<h1>Liste des 10 dernières transactions</h1>");
        html.append("<table border='1'><tr><th>Date</th><th>Identifiant</th><th>Montant</th></tr>");
        
        for (Document doc : docs) {
            if (!doc.getDrctDbtTxInfs().isEmpty()) {
                String pmtId = doc.getDrctDbtTxInfs().get(0).getPmtId();
                html.append("<tr>")
                    .append("<td>").append(doc.getCreDtTm()).append("</td>")
                    .append("<td><a href='/sepa26/html/").append(doc.getId()).append("'>").append(pmtId).append("</a></td>")
                    .append("<td>").append(doc.getCtrlSum()).append("</td>")
                    .append("</tr>");
            }
        }
        
        html.append("</table></body></html>");
        return html.toString();
    }

    /**
     * Récupère le détail complet d'une transaction au format XML.
     */
    @GetMapping(value = "/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public Object getXmlDetail(@PathVariable int id) {
        Document doc = sepaService.getById(id);
        if (doc == null) {
            return "<error><id>" + id + "</id><status>ERROR</status></error>";
        }
        return doc;
    }

    /**
     * Récupère le détail complet d'une transaction au format HTML via transformation XSLT.
     */
    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String getHtmlDetail(@PathVariable int id) {
        Document doc = sepaService.getById(id);
        if (doc == null) {
            return "<html><body><h1>ERROR</h1><p>Identifiant " + id + " incorrect</p></body></html>";
        }

        try {
            // Marshalling JAXB vers String
            JAXBContext context = JAXBContext.newInstance(Document.class);
            Marshaller marshaller = context.createMarshaller();
            StringWriter writer = new StringWriter();
            marshaller.marshal(doc, writer);
            String xml = writer.toString();

            // Transformation XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            ClassPathResource xsltFile = new ClassPathResource("xml/sepa26.tp3.xslt");
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile.getInputStream()));
            
            StringWriter resultWriter = new StringWriter();
            transformer.transform(new StreamSource(new java.io.StringReader(xml)), new StreamResult(resultWriter));
            
            return resultWriter.toString();
        } catch (Exception e) {
            return "<html><body><h1>Erreur lors de la transformation</h1><p>" + e.getMessage() + "</p></body></html>";
        }
    }
}
