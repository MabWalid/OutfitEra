# Étape 1 : Build de l'application avec Maven et Java 21
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copie des fichiers de configuration de dépendance
COPY pom.xml .

# Téléchargement hors-ligne des dépendances Maven (optimisation du cache Docker layer)
RUN mvn dependency:go-offline -B

# Copie des sources du projet
COPY src ./src

# Compilation et paquetage en fichier JAR
RUN mvn clean package -DskipTests

# Étape 2 : Image d'exécution ultra-léger Runtime Java 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Création d'un utilisateur système non-root pour des raisons de sécurité
RUN addgroup -S outfitera && adduser -S outfitera -G outfitera
USER outfitera:outfitera

# Copie du JAR compilé depuis l'étape de build
COPY --from=build /app/target/outfitera-backend-1.0.0-SNAPSHOT.jar app.jar

# Port d'exposition du serveur
EXPOSE 8080

# Variable d'environnement par défaut
ENV SPRING_PROFILES_ACTIVE=dev

# Commande d'exécution du conteneur
ENTRYPOINT ["java", "-jar", "app.jar"]
