package fr.univrouen.sepa26.dto;

/**
 * Encapsule le résultat d'une désérialisation XML en objet typé.
 *
 * @param <T>          type de l'objet désérialisé
 * @param data         l'objet désérialisé si succès, ou null si échec
 * @param errorMessage le message d'erreur si échec, ou null si succès
 */
public record ParseResult<T>(T data, String errorMessage) {

    public boolean isSuccess() {
        return data != null;
    }

    public static <T> ParseResult<T> success(T data) {
        return new ParseResult<>(data, null);
    }

    public static <T> ParseResult<T> error(String errorMessage) {
        return new ParseResult<>(null, errorMessage);
    }
}
