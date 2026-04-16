# 🎓 Campus Management System (Microservices)

![Architecture](https://img.shields.io/badge/Architecture-Microservices-blue.svg)
![Backend](https://img.shields.io/badge/Backend-Spring%20Boot%203-brightgreen)
![Frontend](https://img.shields.io/badge/Frontend-React%20%2B%20Vite-cyan)
![Database](https://img.shields.io/badge/Database-MySQL%20%7C%20MongoDB%20%7C%20H2-blue)

Un système de gestion universitaire complet (Campus Management) basé sur une **architecture Microservices**. Ce projet intègre divers services gérant l'écosystème d'un campus (Bibliothèque, Foyer, Restaurant, Cours, Clubs) de manière distribuée et scalable.

Le dialogue avec le frontend React est sécurisé et unifié grâce à une **API Gateway**, soutenue par **Eureka** pour la découverte de services et **Keycloak** pour l'authentification.

---

## 🏗️ Architecture du Projet

L'écosystème est composé des microservices et serveurs d'infrastructure suivants :

### ⚙️ Composants d'Infrastructure (Core)
* **Eureka Server (`Eureka`)** : Annuaire de registre. Tous les microservices s'y enregistrent (port par défaut : `8761`).
* **Config Server (`ConfigServer`)** : Centre de configuration globalisé (port : `8887`).
* **API Gateway (`Gateway`)** : Point d'entrée unique (Port `8089`). Il route les requêtes frontend vers les bons microservices.
* **Keycloak** : Plateforme d'Identity & Access Management pour l'authentification sécurisée (Jetons JWT).

### 🏷️ Microservices Métiers
* 👤 **User-MS (`user`)** : Gestion des utilisateurs, des rôles et de l'authentification (Connecté à Keycloak).
* 📚 **Bibliothèque (`librairie`)** : Gestion des livres, des emprunts, des catégories et des pénalités de retard.
* 🍽️ **Restaurant (`Restaurant`)** : Gestion des repas (Meals) proposés et des commandes (Orders) étudiantes.
* 🏠 **Foyer (`Micro-service-msFoyer`)** : Gestion des logements, de la capacité des chambres, et des réservations de lits (Attributions).
* 📖 **Cours (`cours-ms`)** : Catalogue des modules d'enseignements, gestion des crédits, et affectations semestrielles.
* 🎭 **Club (`Club`)** : Gestion de la vie associative, des événements et des membres inscrits.

### 💻 Frontend React
* Dossier **`frontend/`** : Interface web (React) qui s'appuie sur le composant `Axios` pour relayer les requêtes HTTP via l'API Gateway.

---

## 🛠️ Technologies Utilisées

| Catégorie | Technologies |
| --- | --- |
| **Backend & Microservices** | Java 17, Spring Boot 3, Spring Data JPA, Spring Cloud (Netflix Eureka, Config, Gateway) |, nestJs
| **Frontend** | React.js, Vite, Axios, React Router, TailwindCSS/CSS (Vanilla) |
| **Base de Données** | MySQL, H2, MongoDB (selon les profils de chaque composant) |
| **Sécurité & IAM** | Keycloak, Spring Security, OAuth2 / JWT |
| **Outils & Conteneurisation** | Maven, Git, Postman, **Docker** |

---

## 🐋 Conteneurisation avec Docker

L'application est conçue pour être "Docker Ready". L'utilisation de **Docker** garantit une portabilité totale et simplifie le déploiement multi-services.

- **Isolation** : Chaque microservice (y compris le Gateway et Eureka) peut être packagé dans sa propre image Docker (via un `Dockerfile`).
- **Orchestration avec Docker Compose** : L'ensemble de l'écosystème peut être déployé en une seule commande (`docker-compose up`) regroupant les microservices, les bases de données (MySQL), et Keycloak, tout en gérant le bon attachement réseau au sein du conteneur.

---

## 🔄 Modes de Communication (Synchrone & Asynchrone)

Afin d'optimiser l'expérience et de garantir la scalabilité, le projet emploie deux modèles majeurs pour la communication intra et inter-services :

### 1. Communication Synchrone (Temps Réel)
La communication synchrone est utilisée lorsque l'application attend immédiatement une réponse pour poursuivre le traitement (Exemple : l'utilisateur qui attend l'affichage de l'interface) :
- **Frontend ➔ Gateway ➔ Microservices** : Réalisée de manière classique via **Appels HTTP/REST**.
- **Entre Microservices (Intra-Cluster)** : Utilisée via **OpenFeign (FeignClient)**. Par exemple, lorsque le service *Foyer/Réservation* vérifie en temps réel les détails d'un étudiant depuis *User-MS*. L'appel est bloquant mais indispensable pour l'integrité des données immédiates.

### 2. Communication Asynchrone (Événementielle)
La communication asynchrone est privilégiée pour des traitements de fond où le blocage de l'interface n'est pas nécessaire, améliorant considérablement la réactivité du système :
- **Event-Driven (Ex: Kafka / RabbitMQ)** : Si implémentée, la création d'un utilisateur ou la validation d'une commande (Restaurant) peut émettre un évènement dans une file de messages, capté ultérieurement par les autres microservices sans bloquer l'émetteur principal.
- **Requêtes Frontend** : Côté React, toutes les intéractions DOM appelant le backend utilisent des `Promises` Javascript (ex. `await Promise.all(...)`), permettant ainsi au navigateur de ne jamais se figer (non-bloquant) et de capturer patiemment la transmission d'erreurs en cas de pépin réseau (`catch`).

---

## 🚀 Guide de Démarrage (Local)

Pour exécuter ce projet localement et harmoniser les connexions :

### 1. Prérequis
- Java 17+
- Node.js & npm (pour React)
- Base de données MySQL installée et active
- Serveur Keycloak (version 26+ fournie dans le répertoire `keycloak-26.3.1`)

### 2. Séquence de Démarrage Backend
L'ordre de démarrage est **déterminant** compte tenu des interdépendances :

1. Démarrez la base de données (ex: WAMP / XAMPP / Docker MySQL).
2. Lancez **`Eureka`** Server. Attendez son initialisation.
3. Lancez **`ConfigServer`**. Attendez la confirmation du bon déploiement.
4. Lancez **vos services métiers** (`user-ms`, `cours-ms`, `Restaurant`, `librairie`, `Micro-service-msFoyer`, `Club`).
5. *Vérification Optionnelle : Accédez à `http://localhost:8761` pour vous assurer que les services sont correctement remontés.*
6. Lancez l'**`API Gateway`** (Doit impérativement être lancé **en dernier** afin qu'il puisse peupler ses routes d'après Eureka).

> **Alerte rechargement** : Toute modification appliquée dans les `.route(..)` du `GatewayApplication.java` nécessite d'être **Recompilée (Rebuild/Clean-Install)** via Maven, suivi du redémarrage du Gateway.

### 3. Démarrage Frontend
Ouvrez un terminal dans le répertoire `frontend/` :
```bash
# Installez les dépendances
npm install

# Lancez l'environnement Node (sur localhost:3000 par défaut)
npm start
```

---

## 🔗 Correspondances Backend/Frontend (Routes API)

L'API Gateway centralise le routage avec la configuration suivante locale (`http://localhost:8089`):

- **`/users/**`** ➔ redirigé vers `lb://user-ms`
- **`/api/courses/**` & `/cours/**`** ➔ redirigé vers `lb://cours-ms`
- **`/api/books/**`, `/api/borrowings/**`, etc.** ➔ redirigé vers `lb://bibliotheque`
- **`/meals/**` & `/orders/**`** ➔ redirigé vers `lb://restaurant`
- **`/foyer/**`, `/chambre/**`, `/reservation/**`** ➔ redirigé vers `lb://microfoyer`
- **`/clubs/**`, `/evenements/**`** ➔ redirigé vers `lb://club`

*(Tout le parsing et l'ajout manuel des `Bearer Tokens` est géré de façon automatique par un intercepteur Axios côté Front (`api.js`)).*

---
✨ *Conçu et packagé pour un déploiement Microservices Scalable et Robuste.*
