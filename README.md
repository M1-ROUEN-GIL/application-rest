# SEPA26 - Application Spring Boot
## Présentation
SEPA26 est une application Spring Boot pour la gestion des transactions SEPA. Elle fournit des points de terminaison
REST pour récupérer et gérer les données selon la spécification SEPA26, avec une intégration à une base de données MariaDB pour la persistance.

## Fonctionnalités
- API REST pour la gestion des flux SEPA
- Représentations XML et HTML des flux SEPA
- Intégration avec MariaDB
- Templates Thymeleaf pour les vues web
- Transformations XSLT pour le rendu HTML
- Validation XML selon un schéma XSD

## Architecture
L'application est structurée selon le modèle MVC :
- **Modèles** : Classe Java représentant les entités de données
- **Vues** : Templates Thymeleaf pour le rendu HTML
- **Contrôleurs**: Classes Java gérant les requêtes HTTP et la logique métier

## Pré-requis
- Kit de développement Java (SDK) 21 ou version supérieure
- Maven 4.0.3
- Git
- MariaDB

## Démarrage
### Clôner le dépôt
```bash
git clone https//https://github.com/M1-ROUEN-GIL/application-rest
cd application-rest
```

### Compiler le projet
```bash
mvn clean package
```

### Exécuter l'application localement
```bash
mvn spring-boot:run
```

L'appliation démarrera sur le port 8100 par défaut. Vous pouvez y accéder à l'adresse http://localhost:8100

## Points de terminaison API
L'application expose plusieurs points de terminaisaon REST :
### Gestion des flux
- `GET /sepa26/resume/xml` - Obtenir toutes les trasactions au format XML
- `GET /sepa26/resume/html` - Obtenir toutes les trasactions au format HTML
- `GET /sepa26/xml/{id}` - Obtenir une transaction spécifique par ID au format XML
- `GET /sepa26/html/{id}` - Obtenir une transaction spécifique par ID au format HTML
- `GET /sepa26/search?param1=&param2=` - Rechercher selon soit param1 soit param2
- `POST /sepa26/insert` - Ajouter une nouvelle transaction (format XML)
- `DELETE /sepa26/delete/{id}` - Supprimer une transaction par ID

## Déploiement
L'application est déployée 

## Contributeurs
- Florian Pépin (florian.pepin@etu.univ-rouen.fr)
- Umm-Habibah Ouattara (umm-habibah.ouattara@etu.univ-rouen.fr)

## Licence
Ce projet est dévloppée dans le cadre d'un cours XML à l'Université de Rouen.