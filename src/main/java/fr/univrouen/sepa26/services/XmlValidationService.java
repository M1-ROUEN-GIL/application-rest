package fr.univrouen.sepa26.services;

import fr.univrouen.sepa26.dto.ValidationResult;

/**
 * Service dédié à la validation de flux XML par rapport aux schémas XSD.
 * Respecte le principe de responsabilité unique (SRP).
 */
public interface XmlValidationService {

    /**
     * Valide un flux XML brut contre le schéma XSD et retourne un résultat détaillé.
     *
     * @param xmlContent le contenu XML sous forme de chaîne de caractères
     * @return ValidationResult contenant l'indicateur de validité et le message d'erreur éventuel
     */
    ValidationResult validate(String xmlContent);

    /**
     * Valide un flux XML brut contre le schéma XSD.
     *
     * @param xmlContent le contenu XML
     * @return true si le flux est valide, false sinon
     */
    boolean isValid(String xmlContent);
}
