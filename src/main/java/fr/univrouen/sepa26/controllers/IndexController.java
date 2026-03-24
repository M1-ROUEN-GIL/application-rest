package fr.univrouen.sepa26.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	@GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
	public String index() {
		return "<h1>Projet REST Sepa26</h1>" +
			   "<ul>" +
			   "<li><a href='/xml'>Exemple XML (Modèle Document)</a></li>" +
			   "<li><a href='/resume'>Résumé des flux</a></li>" +
			   "<li><a href='/test?nb=1&search=test'>Test Paramètres</a></li>" +
			   "</ul>";
	}

}