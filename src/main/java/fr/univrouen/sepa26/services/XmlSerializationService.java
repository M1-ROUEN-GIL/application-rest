package fr.univrouen.sepa26.services;

import fr.univrouen.sepa26.dto.ParseResult;
import fr.univrouen.sepa26.model.Document;

/**
 * Service dédié à la sérialisation (marshalling) et désérialisation (unmarshalling) XML via JAXB.
 * Respecte le principe de responsabilité unique (SRP).
 */
public interface XmlSerializationService {

    /**
     * Sérialise un objet Document en chaîne de caractères XML formatée.
     *
     * @param document l'entité Document à sérialiser
     * @return la chaîne XML résultante
     */
    String marshalToXml(Document document);

    /**
     * Désérialise une chaîne XML en objet Document avec rapport d'erreur détaillé.
     *
     * @param xmlContent la chaîne XML représentant un Document
     * @return ParseResult contenant le Document ou le message d'erreur
     */
    ParseResult<Document> unmarshalFromXml(String xmlContent);
}
