package fr.univrouen.sepa26.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import fr.univrouen.sepa26.services.SepaService;

/**
 * Contrôleur responsable du transfert de flux XML.
 */
@Controller
public class TransfertController {
	private final SepaService sepaService;
	
	/**
	 * Constructeur qui injecte le service de validation
	 * @param sepaService validation XML
	 */
	@Autowired
	public TransfertController(SepaService sepaService) {
		this.sepaService = sepaService;
	}
	
	/**
	 * Affiche le formulaire de transfert
	 * @return le nom de la vue "trasnfert"
	 */
	@GetMapping("/transfert")
	public String transfert() {
		return "transfert";
	}
}
