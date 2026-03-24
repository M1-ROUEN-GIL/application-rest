package fr.univrouen.sepa26.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.univrouen.sepa26.model.Document;

@RestController
public class GetController {
	
	@GetMapping("/resume")
	public String getListSepa26InXML() {
		return "Envoi de la liste des flux SEPA enregistrés";
	}
	
	@GetMapping("/guid")
	public String getSepa26InXML(@RequestParam(value = "guid") String texte) {
		return ("Détail de la transaction SEPA " + texte);
	}
	
	@GetMapping("/test")
	public String getSepa26InXML
	(
			@RequestParam(value = "nb", defaultValue = "0") Integer nombre,
			@RequestParam(value = "search", defaultValue = "") String recherche
	) {
		return ("Test :<br>" + "guid = " + nombre + "<br>titre = " + recherche);
	}
	
	@RequestMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody Document getXML() {
		Document doc = new Document();
		Document.DrctDbtTxInf inf = new Document.DrctDbtTxInf();
		inf.setPmtId("REF-123");
		
		Document.InstdAmt amt = new Document.InstdAmt();
		amt.setValue(123.45);
		amt.setCcy("EUR");
		inf.setInstdAmt(amt);
		
		Document.Dbtr dbtr = new Document.Dbtr();
		dbtr.setNm("Test Debiteur");
		inf.setDbtr(dbtr);
		
		doc.getDrctDbtTxInfs().add(inf);
		return doc;
	}
	
}
