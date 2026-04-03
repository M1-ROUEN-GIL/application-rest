package fr.univrouen.sepa26.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("projectName", "Projet XML Sepa26");
		model.addAttribute("version", "1.0");
		model.addAttribute("developers", "Florian & Umm-Habibah");
		model.addAttribute("universityLogo", "https://www.choisirlanormandie.fr/app/uploads/2024/08/logo-universite-de-rouen.png");
		return "landing"; // le nom du fichier thymeleaf
	}
}
