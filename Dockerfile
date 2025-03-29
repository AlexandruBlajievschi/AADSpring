# ---------- Stage 1: Build the JAR ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy all source and the Maven wrapper
COPY . .

# Run Maven package to compile and build your Spring Boot JAR
# (If you want to run tests, remove the -DskipTests flag)
RUN ./mvnw clean package -DskipTests

# ---------- Stage 2: Run the App ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port (Spring Boot default)
EXPOSE 8080

# Launch the application
ENTRYPOINT ["java","-jar","app.jar"]
