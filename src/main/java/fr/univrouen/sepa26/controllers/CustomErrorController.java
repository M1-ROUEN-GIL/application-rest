package fr.univrouen.sepa26.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletResponse response) {
        int status = response.getStatus();
        if (status == 404) {
            return "<h1>Erreur 404 - Page non trouvée</h1>";
        } else if (status == 500) {
            return "<h1>Erreur 500 - Erreur interne du serveur</h1>";
        }
        return "<h1>Erreur " + status + "</h1>";
    }
}
