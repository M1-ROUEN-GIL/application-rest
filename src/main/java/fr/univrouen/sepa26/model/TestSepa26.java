package fr.univrouen.sepa26.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class TestSepa26 {
	public String loadFileXML() {
		try {
			Resource resource = new DefaultResourceLoader()
					.getResource("src/main/resources/xml/testsepa.xml");
			StringBuilder sb = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(resource.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			}
			return sb.toString();
		} catch (Exception e) {
			return "<error>" + e.getMessage() + "</error>";
		}
	}
}
