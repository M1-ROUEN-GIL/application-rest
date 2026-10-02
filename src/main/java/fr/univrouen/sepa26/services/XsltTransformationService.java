package fr.univrouen.sepa26.services;

import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Service pour la transformation de flux XML en rendu HTML à l'aide de feuilles XSLT.
 */
public interface XsltTransformationService {

    /**
     * Transforme un document XML en HTML via la feuille de style par défaut (sepa26.xslt).
     *
     * @param xml le flux XML brut
     * @return le rendu HTML
     * @throws TransformerException en cas d'erreur de transformation XSLT
     * @throws IOException          en cas d'erreur d'entrée/sortie
     */
    String transformSepa(String xml) throws TransformerException, IOException;

    /**
     * Transforme un document XML en utilisant une feuille XSLT spécifiée.
     *
     * @param xml      le flux XML brut
     * @param xsltPath chemin dans le classpath de la feuille XSLT
     * @return le rendu HTML
     * @throws TransformerException en cas d'erreur de transformation XSLT
     * @throws IOException          en cas d'erreur d'entrée/sortie
     */
    String transform(String xml, String xsltPath) throws TransformerException, IOException;
}
