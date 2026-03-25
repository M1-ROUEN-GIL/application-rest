package fr.univrouen.sepa26.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.univrouen.sepa26.model.DrctDbtTxInf;
import fr.univrouen.sepa26.model.Sepa26;
import fr.univrouen.sepa26.service.SepaService;

@RestController
public class GetController {
	@Autowired
	private SepaService service;
	
	@GetMapping(value = "/resume", produces = MediaType.APPLICATION_XML_VALUE)
	public String getResume() {
		List<DrctDbtTxInf> txs = service.getAll();
		if (txs.isEmpty()) {
			return "<transactions><message>Aucune transaction enregistrée</message></transactions>";
		}
		StringBuilder sb = new StringBuilder("<transactions>");
		for (DrctDbtTxInf tx : txs) {
			sb.append("<transaction>")
				.append("<PmtId>")
				.append(tx.getPmtId())
				.append("</PmtId>")
				.append("<InstdAmt>")
				.append(tx.getInstdAmt())
				.append("</InstdAmt>")
				.append("</transaction>");
		}
		sb.append("</transactions>");
		return sb.toString();
	}
	
	@GetMapping(value = "/guid", produces = MediaType.APPLICATION_XML_VALUE)
	public String getByGuid(@RequestParam(value = "guid") String guid) {
		DrctDbtTxInf tx = service.getByPmtId(guid);
		if (tx == null) {
			return "<result><status>ERROR</status>" +
						"<message>Transaction non trouvée : " + guid + "</message></result>";
		}
		return "<DrctDbtTxInf>" +
			"<PmtId>" + tx.getPmtId() + "</PmtId>" +
			"<InstdAmt>" + tx.getInstdAmt() + "</InstdAmt>" +
			"<RmtInf>" + tx.getRmtInf() + "</RmtInf>" +
			"</DrctDbtTxInf>"
		;
	}
	
	@RequestMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody Sepa26 getXML() {
		Sepa26 sepa = new Sepa26("123", "Test model", "2026-17-03T08:01:02");
		return sepa;
	}
}
