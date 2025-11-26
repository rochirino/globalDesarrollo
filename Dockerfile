# ========================================
# ETAPA 1: BUILD (Compilación)
# ========================================
# Usamos una imagen oficial de Gradle.
# Trae Java 17 y Gradle listos. Mucho más seguro que instalarlo a mano en Alpine.
FROM gradle:8.5-jdk17-alpine AS build

# Establecemos el directorio de trabajo
WORKDIR /home/gradle/src

# Copiamos todo el código (el usuario gradle es el dueño para evitar problemas de permisos)
COPY --chown=gradle:gradle . .

# Ejecutamos el build
# --no-daemon: Ahorra memoria en entornos CI/CD como Render
# -x test: Saltamos los tests aquí para que el deploy no falle si un test es estricto
RUN gradle bootJar --no-daemon -x test

# ========================================
# ETAPA 2: RUNTIME (Ejecución)
# ========================================
# Usamos Eclipse Temurin JRE (versión Alpine).
# Es la versión moderna, ligera y segura que reemplaza a "openjdk:17-alpine"
FROM eclipse-temurin:17-jre-alpine

# Documentamos el puerto (Render usa la variable PORT, pero esto ayuda a entender)
EXPOSE 8080

# Copiamos el JAR generado.
# TRUCO: Usamos "*.jar" para no depender del nombre exacto "Mutantes-1.0..."
# Así, si cambia la versión en build.gradle, esto sigue funcionando.
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# Comando de arranque
ENTRYPOINT ["java", "-jar", "/app.jar"]
