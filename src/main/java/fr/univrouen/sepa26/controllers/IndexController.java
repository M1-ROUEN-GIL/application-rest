package fr.univrouen.sepa26.controllers;

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
	public String help() {
		return "help";
	}

}
