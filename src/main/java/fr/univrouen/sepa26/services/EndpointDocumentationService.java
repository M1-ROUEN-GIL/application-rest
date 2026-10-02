package fr.univrouen.sepa26.services;

import java.util.List;
import java.util.Map;

/**
 * Service fournissant la documentation des points d'accès (endpoints) pour la page d'aide.
 * Décharge le contrôleur web de la construction procédurale des données (SRP).
 */
public interface EndpointDocumentationService {

    /**
     * Retourne la liste des descriptions de chaque endpoint géré par le service.
     *
     * @return liste ordonnée des métadonnées de documentation
     */
    List<Map<String, String>> getEndpointsDocumentation();
}
