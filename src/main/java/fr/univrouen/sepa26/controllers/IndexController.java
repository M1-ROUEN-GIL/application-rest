package fr.univrouen.sepa26.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("projectName", "Projet SEPA26");
		model.addAttribute("version", "0.0.1-SNAPSHOT");
		model.addAttribute("developer", "Prénom + Nom et Florian Pépin");
		return "index";
	}

	@GetMapping("/help")
	public String help() {
		return "help";
	}

}
