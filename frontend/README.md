# Campus MS — Frontend React

Interface React pour la gestion de tous les microservices universitaires.

## 🚀 Démarrage rapide

```bash
# 1. Installer les dépendances
npm install

# 2. Configurer l'URL de l'API Gateway
cp .env.example .env
# Éditer .env et mettre votre URL

# 3. Lancer l'application
npm start
```

L'application sera disponible sur **http://localhost:3000**

## 📁 Structure

```
src/
├── components/
│   ├── CrudTable.jsx      # Tableau CRUD réutilisable
│   ├── Modal.jsx          # Fenêtre modale
│   └── Sidebar.jsx        # Navigation latérale
├── pages/
│   ├── Dashboard.jsx      # Page d'accueil
│   ├── users/             # MS User — comptes, profils, rôles
│   ├── cours/             # MS Cours — cours, enseignants, supports
│   ├── club/              # MS Club — clubs, événements, membres, réunions
│   ├── foyer/             # MS Foyer — logements, chambres, réclamations
│   ├── bibliotheque/      # MS Bibliothèque — livres, emprunts, réservations
│   └── restaurant/        # MS Restaurant — menus, commandes, tickets
└── services/
    └── api.js             # Tous les appels API vers les microservices
```

## 🔧 Configuration API

Modifiez `src/services/api.js` pour adapter les routes selon vos endpoints :

| Microservice   | Base URL            | Tech         | DB       |
|---------------|---------------------|--------------|----------|
| MS User        | `/api/users`        | Spring Boot  | H2       |
| MS Cours       | `/api/cours`        | Spring Boot  | MySQL    |
| MS Club        | `/api/clubs`        | Spring Boot  | H2       |
| MS Foyer       | `/api/foyer`        | Spring Boot  | MySQL    |
| MS Bibliothèque| `/api/bibliotheque` | Spring Boot  | H2       |
| MS Restaurant  | `/api/restaurant`   | NestJS       | MongoDB  |

## 🔐 Authentification

Le token JWT est automatiquement ajouté depuis `localStorage.getItem('token')`.
Keycloak gère l'authentification — intégrez la lib `keycloak-js` si besoin.

## 📦 Build production

```bash
npm run build
```

Le build sera dans le dossier `build/`.
