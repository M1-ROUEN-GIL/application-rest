package fr.univrouen.sepa26.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur gérant les pages statiques de l'application.
 * Utilise Thymeleaf pour le rendu des vues HTML.
 */
@Controller
public class IndexController {

    /**
     * Page d'accueil de l'application.
     * @param model Le modèle pour passer des données à la vue.
     * @return Le nom de la vue "index".
     */
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("projectName", "Projet SEPA26");
		model.addAttribute("version", "0.0.1-SNAPSHOT");
		model.addAttribute("developer", "Florian Pépin & Umm-Habibah Ouattara");
		model.addAttribute("universityLogo", "https://www.choisirlanormandie.fr/app/uploads/2024/08/logo-universite-de-rouen.png");
		return "index";
	}

    /**
     * Page d'aide listant les points d'accès de l'API.
     * @return Le nom de la vue "help".
     */
	@GetMapping("/help")
	public String help(Model model) {
		List<Map<String, String>> endpoints = new ArrayList<>();
		// Home
		Map<String, String> home = new HashMap<>();
		home.put("url", "/");
		home.put("method", "GET");
		home.put("operation", "Affiche la page d'accueil");
		home.put("return", "HTML");
		home.put("description", "Affiche la page d'accueil du projet avec les informations suivantes:<br>"
				+ "- Nom du projet<br>"
				+ "- Numéro de version<br>"
				+ "- Nom & Prénom du (ou des) développeur(se)(s)<br>"
				+ "- Log de l'Université de ROuen");
		endpoints.add(home);
		
		// help
		Map<String, String> helpPage = new HashMap<>();
		helpPage.put("url", "/help");
		helpPage.put("method", "GET");
		helpPage.put("operation", "Affiche la page d'aide");
		helpPage.put("return", "HTML");
		helpPage.put("description", "Affiche la liste des opérations gérées par le service REST.<br>" +
				"Pour chacune des opérations proposées par le service, sont affichés: <br>"
				+ "- URL<br>"
				+ "- Méthode<br>"
				+ "- Petit résumé de l'opératoin (format attendu, format de retour)");
		endpoints.add(helpPage);
		
		// resume/xml
		Map<String, String> resumeXml = new HashMap<>();
		resumeXml.put("url", "/sepa26/resume/xml");
		resumeXml.put("method", "GET");
		resumeXml.put("operation", "Affiche la liste des transactions stockées.");
		resumeXml.put("return", "XML");
		resumeXml.put("description", "Liste simplifiée des transactions présentes dans la base.<br>"
				+ "Pour chanque transaction, ne seront affichées que les informations suivantes :<br>"
				+ "- id<br>"
				+ "- date<br>"
				+ "- montant");
		endpoints.add(resumeXml);
		
		// resume/html
		Map<String, String> resumeHtml = new HashMap<>();
		resumeHtml.put("url", "/sepa26/resume/html");
		resumeHtml.put("methode", "GET");
		resumeHtml.put("operation", "Affiche la liste des transactions stockées.");
		resumeHtml.put("return", "HTML");
		resumeHtml.put("description", "Liste simplifiée des transactions présentes dans la base.<br>"
				+ "Mêmes informations que précédemment au au format HTML");
		endpoints.add(resumeHtml);
		
		// sepa26/xml/{id}
		Map<String, String> xmlId = new HashMap<>();
		xmlId.put("url", "/sepa26/xml/{id}");
		xmlId.put("method", "GET");
		xmlId.put("operation", "Affiche le contenu complet de la transaction dont l'identifiant est {id}.");
		xmlId.put("return", "XML conforme au schéma XSD");
		xmlId.put("description", "Intégralité de l'article dont l'identifiant est fourni par son {id}.<br>"
				+ "Si l'identifiant est incorrect, retour d'un message d'erreur au format XML contenant :<br>"
				+ "- {id} -> numéro de l'identifiant demandé<br>"
				+ "- status -> ERROR");
		endpoints.add(xmlId);
		
		// sepa26/html/{id}
		Map<String, String> htmlId = new HashMap<>();
		htmlId.put("url", "/sepa26/html/{id}");
		htmlId.put("method", "GET");
		htmlId.put("operation", "Affiche le contenu complet de la transaction dont l'identifiant est {id}.");
		htmlId.put("return", "HTML");
		htmlId.put("description", "Intégralité de l'article dont l'identifiant est fourni par son {id}.<br>"
				+ "Si l'identifiant est incorrect, retour d'un message d'erreur au format HTML contenant :<br>"
				+ "- {id} -> numéro de l'identifiant demandé<br>"
				+ "- status -> ERROR");
		endpoints.add(htmlId);
		
		// sepa26/insert
		Map<String, String> insert = new HashMap<>();
		insert.put("url", "/sepa26/insert");
		insert.put("method", "POST");
		insert.put("operation", "Ajoute une transaction en base");
		insert.put("return", "XML");
		insert.put("description", "Flux XML décrivant une transaction à ajouter, conforme au schéma xsd.<br>"
				+ "Le flux reçu est validé par le schéma XSD de définition sepa26<br>"
				+ "Si le flux est déjà présent, (même pmtId) alors une indication d'erreur est retournée.<br>"
				+ "Si l'opération est réussie, alors le flux est ajouté à la base et sa persistance est assurée.<br>"
				+ "Le flux XML retourné contient les informations suivantes :<br>"
				+ "- id -> numéro d'identifiant du document enregistré<br>"
				+ "- status -> INSERTED<br>"
				+ "En cas d'échec de l'opération, seule la valeur de status est retournée<br>"
				+ "- status -> ERROR");
		endpoints.add(insert);
	
		// sepa26/delete/{id}
		Map<String, String> deleteId = new HashMap<>();
		deleteId.put("url", "/sepa26/delete/{id}");
		deleteId.put("method", "DELETE");
		deleteId.put("operation", "Suppression de la transaction dont l'identifiant est {id}");
		deleteId.put("return", "XML");
		deleteId.put("description", "Si l'opération a réussi, retour des information suivantes :<br>"
				+ "- id -> numéro d'identifiant du document qui a été retiré<br>"
				+ "- status -> DELETED<br>"
				+ "En cas d'échec de l'opération, seule la valeur de status est retournée<br>"
				+ "- status -> ERROR");
		endpoints.add(deleteId);
				
		// sepa26/search?...
		Map<String, String> search = new HashMap<>();
		search.put("url", "/sepa26/search?");
		search.put("method", "GET");
		search.put("operation", "Recherche la liste des transactions.");
		search.put("return", "Flux XML");
		search.put("description", "Recherche la liste des transactions répondant aux contraintes exprimées dans la requête :<br>"
				+ "- date -> Liste des transactions dont la date CreDtTm est => à celle indiquée dans la requête<br>"
				+ "- sum -> Liste des trasactions dont le montnant défini dans la balise ctrlSum est => à celle indiquée dans la requête<br>"
				+ "Si l'opération réussie"
				+ "- status -> Flux XML"
				+ "Si l'opération échoue"
				+ "- status -> ERROR"
				+ "Si succès mais aucun résultat"
				+ "- status -> NONE");
		endpoints.add(search);
		
		model.addAttribute("endpoints", endpoints);
		return "help";
	}

}