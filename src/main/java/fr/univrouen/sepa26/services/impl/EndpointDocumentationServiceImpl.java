package fr.univrouen.sepa26.services.impl;

import fr.univrouen.sepa26.services.EndpointDocumentationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implémentation du service fournissant la documentation des points d'accès.
 */
@Service
public class EndpointDocumentationServiceImpl implements EndpointDocumentationService {

    private final List<Map<String, String>> endpoints;

    public EndpointDocumentationServiceImpl() {
        List<Map<String, String>> list = new ArrayList<>();

        list.add(createEndpoint(
            "/",
            "GET",
            "Affiche la page d'accueil",
            "HTML",
            "Affiche la page d'accueil du projet avec les informations générales (nom du projet, version, auteurs, logo de l'Université de Rouen)."
        ));

        list.add(createEndpoint(
            "/help",
            "GET",
            "Affiche la page d'aide",
            "HTML",
            "Affiche la liste exhaustive des opérations gérées par le service REST (URL, méthode, résumé, formats attendus et retournés)."
        ));

        list.add(createEndpoint(
            "/transfert",
            "GET",
            "Formulaire de transfert de flux XML",
            "HTML",
            "Interface interactive permettant de téléverser et soumettre un flux XML SEPA vers le service REST."
        ));

        list.add(createEndpoint(
            "/sepa26/resume/xml",
            "GET",
            "Affiche la liste des transactions stockées",
            "XML",
            "Liste simplifiée (au format XML) des 10 dernières transactions enregistrées (identifiant, date, montant)."
        ));

        list.add(createEndpoint(
            "/sepa26/resume/html",
            "GET",
            "Affiche la liste des transactions stockées",
            "HTML",
            "Mêmes informations simplifiées que précédemment, présentées dans un tableau interactif HTML."
        ));

        list.add(createEndpoint(
            "/sepa26/xml/{id}",
            "GET",
            "Affiche le contenu complet d'un document",
            "XML conforme au schéma XSD",
            "Intégralité du document SEPA dont l'identifiant technique est {id}. En cas d'identifiant introuvable, retourne une réponse XML avec status ERROR."
        ));

        list.add(createEndpoint(
            "/sepa26/html/{id}",
            "GET",
            "Affiche le contenu complet d'un document en HTML",
            "HTML",
            "Rendu HTML stylisé du document {id} transformé via la feuille XSLT (sepa26.xslt). En cas d'erreur, retourne un flux XML d'erreur."
        ));

        list.add(createEndpoint(
            "/sepa26/insert",
            "POST",
            "Ajoute un nouveau document en base",
            "XML",
            "Flux XML ISO 20022 à insérer. Le flux est validé par le schéma XSD (paramètre validate optionnel). Vérifie l'unicité du PmtId. Retourne le statut INSERTED avec l'identifiant généré ou ERROR."
        ));

        list.add(createEndpoint(
            "/sepa26/delete/{id}",
            "DELETE",
            "Supprime le document dont l'identifiant est {id}",
            "XML",
            "Suppression du document et de ses transactions associées. Retourne un flux XML avec statut DELETED et l'identifiant supprimé, ou ERROR si non trouvé."
        ));

        list.add(createEndpoint(
            "/sepa26/search?date=...&sum=...",
            "GET",
            "Recherche multicritères de documents",
            "Flux XML",
            "Recherche des documents selon la date minimale (CreDtTm >= date) et/ou la somme minimale (CtrlSum >= sum). Retourne status OK, NONE ou ERROR."
        ));

        this.endpoints = Collections.unmodifiableList(list);
    }

    private Map<String, String> createEndpoint(String url, String method, String operation, String returnFormat, String description) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("url", url);
        map.put("method", method);
        map.put("operation", operation);
        map.put("return", returnFormat);
        map.put("description", description);
        return map;
    }

    @Override
    public List<Map<String, String>> getEndpointsDocumentation() {
        return endpoints;
    }
}
