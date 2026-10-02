# ==============================================================================
# Dockerfile Multi-Stage pour SEPA26 (Java 25 LTS & Spring Boot 4.1.1)
# ==============================================================================

# Étape 1 : Compilation et packaging avec Maven et JDK 25
FROM eclipse-temurin:25-jdk-alpine AS builder

RUN apk add --no-cache maven

WORKDIR /build

# Copie des fichiers sources, configuration checkstyle et pom.xml
COPY pom.xml checkstyle.xml* ./
COPY src ./src

# Compilation de l'exécutable Spring Boot
RUN mvn clean package -DskipTests

# Étape 2 : Image d'exécution légère et sécurisée avec JRE 25
FROM eclipse-temurin:25-jre-alpine

WORKDIR /opt

# Création d'un utilisateur non-root dédié pour Spring Boot
RUN addgroup -S spring && adduser -S spring -G spring

# Copie du binaire produit depuis l'étape de compilation
COPY --from=builder /build/target/sepa26server.jar /opt/sepa26server.jar

USER spring:spring

EXPOSE 8100

ENTRYPOINT ["java", "-jar", "sepa26server.jar"]
