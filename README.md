# 👕 OutfitEra - Virtual Fitting Room & E-Commerce Platform

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-brightgreen?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI_3.0-85EA2D?style=for-the-badge&logo=swagger)

OutfitEra est une plateforme E-Commerce haut de gamme intégrant un module d'**essayage virtuel de vêtements**, des recommandations propulsées par **Spring AI**, un système de **gamification** (badges, classements) et une architecture **Clean Architecture** conforme aux standards de l'industrie.

---

## 🏛️ Architecture du projet Backend

Le backend respecte la **Clean Architecture** et une séparation stricte des responsabilités :

```text
backend/src/main/java/com/outfitera/
├── config/              # Configurations Spring (OpenAPI, JPA, Security, AI)
├── controller/          # Contrôleurs REST (Endpoints API)
├── dto/                 # Objects de transfert de données (Requests, Responses, ApiResponse)
├── entity/              # Entités du Domaine JPA / Hibernate
├── enums/               # Énumérations métier (Rôles, Statuts)
├── exception/           # Exception Handler centralisé et exceptions personnalisées
├── mapper/              # Mappers MapStruct (Transformation Entity <-> DTO)
├── repository/          # Interfaces Repositories Spring Data JPA
├── security/            # Filtres JWT, UserDetailsService, SecurityConfig
├── service/             # Interfaces des services métier
│   └── impl/            # Implémentations concrètes des services métier
└── util/                # Constantes et classes utilitaires
```

---

## 🚀 Démarrage Rapide

### Prérequis
- Java 21 JDK
- Maven 3.9+
- Docker & Docker Compose

### Lancement des Services avec Docker Compose
```bash
# Lancer PostgreSQL et PgAdmin
docker-compose up -d postgres pgadmin

# Lancer toute la stack (PostgreSQL + PgAdmin + Backend)
docker-compose up -d --build
```

- **Swagger UI** : `http://localhost:8080/api/v1/swagger-ui.html`
- **PgAdmin 4** : `http://localhost:5050` (Email: `admin@outfitera.com`, Mdp: `adminpassword`)
- **Health Check** : `http://localhost:8080/api/v1/health`
