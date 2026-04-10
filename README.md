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
- **Modèles** : Classe Java représentant les entités de données et la logique métier
- **Vues** : Templates Thymeleaf pour le rendu HTML
- **Contrôleurs**: Classes Java gérant les requêtes HTTP

## Pré-requis
- Kit de développement Java (SDK) 21 ou version supérieure
- Maven 4.0.3
- Git
- MariaDB

## Points de terminaison API
L'application expose plusieurs points de terminaison REST :
### Gestion des flux
- `GET /sepa26/resume/xml` - Obtenir toutes les trasactions au format XML
- `GET /sepa26/resume/html` - Obtenir toutes les trasactions au format HTML
- `GET /sepa26/xml/{id}` - Obtenir une transaction spécifique par ID au format XML
- `GET /sepa26/html/{id}` - Obtenir une transaction spécifique par ID au format HTML
- `GET /sepa26/search?param1=&param2=` - Rechercher selon soit param1 soit param2
- `POST /sepa26/insert` - Ajouter une nouvelle transaction (format XML)
- `DELETE /sepa26/delete/{id}` - Supprimer une transaction par ID

## Déploiement
L'application est déployée sur les serveurs suivants:
- vm-pepinflo-xml.univ-rouen.fr / 10.130.162.188
- vm-ouattumm-xml.univ-rouen.fr / 10.130.162.187

L'appliation démarrera sur le port 8100 par défaut. Vous pouvez y accéder à l'adresse http://localhost:8100

## Contributeurs
- Florian Pépin (florian.pepin@etu.univ-rouen.fr)
- Umm-Habibah Ouattara (umm-habibah.ouattara@etu.univ-rouen.fr)

## Licence
Ce projet est développée dans le cadre d'un cours XML à l'Université de Rouen.