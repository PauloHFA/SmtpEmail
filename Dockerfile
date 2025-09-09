# Use uma imagem Java leve
FROM eclipse-temurin:21-jdk-jammy

# Crie um diretório para a aplicação
WORKDIR /app

# Copie o JAR gerado pelo Maven para dentro da imagem
COPY target/apiapex-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que a aplicação usa
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
