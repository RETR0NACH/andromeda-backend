# --- Etapa 1: Construcción (Build) ---
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copiar solo el pom.xml primero para aprovechar la caché de capas de Docker
COPY pom.xml .
# Descargar dependencias (esto se cacheará si no cambia el pom.xml)
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución (Runtime) ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]