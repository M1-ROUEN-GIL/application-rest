package fr.univrouen.sepa26.dto;

/**
 * Encapsule le résultat d'une validation de document XML contre un schéma XSD.
 *
 * @param valid        true si le document est valide selon le schéma, false sinon.
 * @param errorMessage le message d'erreur explicatif en cas d'échec, ou null en cas de succès.
 */
public record ValidationResult(boolean valid, String errorMessage) {

    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult error(String message) {
        return new ValidationResult(false, message);
    }
}
