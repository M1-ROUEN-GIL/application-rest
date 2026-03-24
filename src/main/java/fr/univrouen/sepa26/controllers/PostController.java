package fr.univrouen.sepa26.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.model.TestSepa26;

@RestController
public class PostController {

	@PostMapping(value = "/testpost", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public Document postTest(@RequestBody Document doc) {
		return doc;
	}
	
	@PostMapping(value = "/testload", produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public String testLoad() {
		TestSepa26 sepa = new TestSepa26();
		return sepa.loadFileXML();
	}

}
