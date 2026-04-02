package fr.univrouen.sepa26;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application SEPA26.
 * Cette classe configure et lance le contexte Spring Boot.
 */
@SpringBootApplication
public class Application {

    /**
     * Point d'entrée principal de l'application.
     * @param args Arguments passés en ligne de commande.
     */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
