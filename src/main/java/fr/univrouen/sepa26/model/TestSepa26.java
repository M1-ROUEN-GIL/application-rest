package fr.univrouen.sepa26.model;

import org.springframework.core.io.Resource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.StreamUtils;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.IOException;

public class TestSepa26 {

	public String loadFileXML() {
	    try {
	        Resource resource = new DefaultResourceLoader().getResource("classpath:xml/testsepa.xml");
	        InputStream is = resource.getInputStream();
	        return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
	    } catch (IOException e) {
	        return "<error>Erreur lors de la lecture du fichier : " + e.getMessage() + "</error>";
	    }
	}
	
}
