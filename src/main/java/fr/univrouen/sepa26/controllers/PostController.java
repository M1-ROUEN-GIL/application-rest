package fr.univrouen.sepa26.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.model.DrctDbtTxInf;
import fr.univrouen.sepa26.model.PmtInf;
import fr.univrouen.sepa26.service.SepaService;

@RestController
public class PostController {
	@Autowired
	private SepaService service;
	
	@PostMapping(
			value = "/sepa26",
			consumes = MediaType.APPLICATION_XML_VALUE,
			produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public String addTransaction(@RequestBody DrctDbtTxInf tx) {
		if (service.existsByPmtId(tx.getPmtId())) {
			return "<result><status>ERROR</status>" +
					"<message>Transaction déjà existante :" +
					tx.getPmtId() +
					"</message></result>"
			;
		}
		service.add(tx);
		return "<result><status>INSERTED</status>" +
				"<id>" +
				tx.getPmtId() +
				"</message></result>"
		;
	}
	
	@PostMapping(
			value = "/sepa26/document",
			consumes = MediaType.APPLICATION_XML_VALUE,
			produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public String addDocument(@RequestBody Document doc) {
		if (doc.getPmtInf() == null || doc.getPmtInf().isEmpty()) {
			return "<result><status>ERROR</status><message>Document vide ou invalide</message></result>";
		}
		int count = 0;
		for (PmtInf pmtInf : doc.getPmtInf()) {
			if (pmtInf.getTransactions() != null) {
				for (DrctDbtTxInf tx : pmtInf.getTransactions()) {
					if (!service.existsByPmtId(tx.getPmtId())) {
						service.add(tx);
						count++;
					}
				}
			}
		}
		return "<result><status>INSERTED</status>" +
			"<count>" + count + "</count>" +
			"<msgId>" + doc.getGrpHdr().getMsgId() + "</msgId></result>";
	}
}
