# SEPA26 - Service REST ISO 20022

Service Spring Boot de gestion, validation XSD et transformation XSLT de virements bancaires conformes à la norme ISO 20022.

---

## Démarrage rapide

### Avec Docker Compose (Recommandé)
Lance l'application Spring Boot (`sepa26-app`) et la base de données MariaDB :

```bash
docker compose up --build
```

L'application est disponible sur : **[http://localhost:8100/](http://localhost:8100/)**

### En local avec Maven
Prérequis : Java 25 et Maven.

```bash
# Lancement des tests
mvn test

# Démarrage de l'application
mvn spring-boot:run
```

---

## Points d'accès principaux

| Endpoint | Méthode | Format | Description |
| :--- | :--- | :--- | :--- |
| `/` | `GET` | HTML | Page d'accueil du service |
| `/help` | `GET` | HTML | Documentation détaillée de l'API |
| `/transfert` | `GET` | HTML | Formulaire d'importation de flux XML |
| `/sepa26/resume/html` | `GET` | HTML | Liste des 10 dernières transactions |
| `/sepa26/resume/xml` | `GET` | XML | Liste des 10 dernières transactions |
| `/sepa26/xml/{id}` | `GET` | XML | Détail complet d'un document SEPA |
| `/sepa26/html/{id}` | `GET` | HTML | Vue détaillée transformée par XSLT |
| `/sepa26/insert` | `POST` | XML | Ajout d'une transaction avec validation XSD |
| `/sepa26/delete/{id}` | `DELETE` | XML | Suppression d'une transaction |
| `/sepa26/search` | `GET` | XML | Recherche multicritères (date, montant) |

---

## Configuration

Les paramètres par défaut sont définis dans `.env.example` et `.env` :
- `SERVER_PORT` : Port du serveur (défaut: `8100`)
- `DB_URL`, `DB_USER`, `DB_PASSWORD` : Identifiants de connexion MariaDB

---

## Licence

Projet sous licence MIT &mdash; voir [LICENSE.md](LICENSE.md).
